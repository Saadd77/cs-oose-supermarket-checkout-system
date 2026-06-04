package fr.cs.oose.system;

import fr.cs.oose.delivery.DeliveryRequest;
import fr.cs.oose.delivery.DeliveryService;
import fr.cs.oose.delivery.DeliverySlot;
import fr.cs.oose.discount.*;
import fr.cs.oose.inventory.InventoryService;
import fr.cs.oose.inventory.ManagerNotifier;
import fr.cs.oose.inventory.SupplierNotifier;
import fr.cs.oose.model.*;
import fr.cs.oose.payment.*;

import java.util.HashMap;
import java.util.Map;

public class SupermarketSystem {
    // Replaced users map and currentUser with AuthManager
    private final AuthManager auth = new AuthManager();
    
    private final Map<String, Category> categories = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();
    private Cart currentCart;
    private Customer checkoutCustomer;
    private final TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();
    private final POS pos = new POS(tas);
    private double lastComputedBill = 0.0;
    private double revenue = 0.0;
    private final InventoryService inventoryService = new InventoryService();
    private final DeliveryService deliveryService = new DeliveryService();
    
    public SupermarketSystem() {
        inventoryService.addObserver(new ManagerNotifier());
        inventoryService.addObserver(new SupplierNotifier());
    }

    public void setup() {
        auth.setupDefaultUsers();
        tas.registerCard(new BankCard("4242424242424242", "1234", 10000.0));
        System.out.println("Default setup loaded.");
    }

    // --- Authentication Wrapper Methods ---
    public void login(String username, String password) {
        auth.login(username, password);
        System.out.println("Logged in as " + username);
    }

    public void logout() {
        String username = auth.getCurrentUser() != null ? auth.getCurrentUser().getUsername() : "unknown";
        auth.logout();
        System.out.println("Logged out from " + username);
    }

    public void registerCashier(String firstName, String lastName, String username, String password) {
        auth.registerCashier(firstName, lastName, username, password);
        System.out.println("Cashier registered: " + username);
    }

    public void registerCustomer(String firstName, String lastName, String username, String address, String password) {
        auth.registerCustomer(firstName, lastName, username, address, password);
        System.out.println("Customer registered: " + username);
    }

    // --- Core Operations ---
    public void addItem(String itemName, String categoryName, double unitPrice, double weight, int initialStock) {
        auth.requireManager();

        if (items.containsKey(itemName)) {
            throw new IllegalArgumentException("item already exists: " + itemName);
        }

        Category category = categories.get(categoryName);
        if (category == null) {
            category = new Category(categoryName);
            categories.put(categoryName, category);
        }

        Item item = new Item(itemName, category, unitPrice, weight, initialStock);
        items.put(itemName, item);

        System.out.println("Item added: " + itemName);
    }

    public void setCategoryDiscount(String categoryName, double discountPercent) {
        auth.requireManager();

        Category category = categories.get(categoryName);
        if (category == null) {
            throw new IllegalArgumentException("unknown category: " + categoryName);
        }

        category.setDiscountPercent(discountPercent);
        System.out.println("Discount set for category " + categoryName + ": " + discountPercent + "%");
    }

    public void startCheckout(String customerUsername) {
        auth.requireCashier();

        User user = auth.getUser(customerUsername);
        if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("unknown customer: " + customerUsername);
        }

        checkoutCustomer = (Customer) user;
        currentCart = new Cart();

        System.out.println("Checkout started for customer: " + customerUsername);
    }

    public void scanItem(String itemName, int quantity) {
        auth.requireCashier();

        if (currentCart == null) {
            throw new IllegalStateException("no active checkout session");
        }

        Item item = items.get(itemName);
        if (item == null) {
            throw new IllegalArgumentException("unknown item: " + itemName);
        }

        currentCart.addItem(item, quantity);
        System.out.println("Scanned: " + quantity + " x " + itemName);
    }

    public double computeBill() {
        auth.requireCashier();

        if (currentCart == null) {
            throw new IllegalStateException("no active checkout session");
        }

        double rawTotal = currentCart.computeRawTotal();
        double afterCategoryDiscounts = currentCart.computeTotal();
        double afterCustomerPlan = checkoutCustomer.getPlan().applyDiscount(afterCategoryDiscounts);

        double deliveryFee = 0.0;

        if (checkoutCustomer.getPendingDeliveryRequest() != null) {
            double baseDeliveryFee = deliveryService.computeBaseDeliveryFee(
                    currentCart,
                    checkoutCustomer.getPendingDeliveryRequest()
            );

            deliveryFee = checkoutCustomer.getPlan().applyDeliveryDiscount(baseDeliveryFee);

            System.out.printf("Base delivery fee: %.2f%n", baseDeliveryFee);
            System.out.printf("Delivery fee after plan discount: %.2f%n", deliveryFee);
        }

        double finalTotal = afterCustomerPlan + deliveryFee;

        System.out.printf("Total before category discounts: %.2f%n", rawTotal);
        System.out.printf("Total after category discounts: %.2f%n", afterCategoryDiscounts);
        System.out.printf("Customer plan: %s%n", checkoutCustomer.getPlan().getName());
        System.out.printf("Total after customer plan: %.2f%n", afterCustomerPlan);
        System.out.printf("Final total: %.2f%n", finalTotal);

        lastComputedBill = finalTotal;
        return finalTotal;
    }

    public void subscribeToPlan(String planName) {
        auth.requireCustomer();

        Customer customer = (Customer) auth.getCurrentUser();
        DiscountPlan plan;

        switch (planName) {
            case "normal":
                plan = new NormalPlan();
                break;
            case "prime":
                plan = new PrimePlan();
                break;
            case "platinum":
                plan = new PlatinumPlan();
                break;
            default:
                throw new IllegalArgumentException("unknown plan: " + planName);
        }

        customer.setPlan(plan);
        System.out.println("Customer subscribed to plan: " + plan.getName());
    }
    
    public void simulatePayment(String outcomeText) {
        auth.requireCashier();

        PaymentOutcome outcome = PaymentOutcome.valueOf(outcomeText);
        pos.simulatePayment(outcome);

        System.out.println("Next payment outcome forced to: " + outcome);
    }

    public void pay(String cardNumber, String pin) {
        auth.requireCashier();

        if (currentCart == null) {
            throw new IllegalStateException("no active checkout session");
        }

        if (lastComputedBill <= 0) {
            throw new IllegalStateException("compute bill before payment");
        }

        PaymentResult result = pos.pay(cardNumber, pin, lastComputedBill);

        if (!result.isSuccess()) {
            System.out.println("Payment refused: " + result.getMessage());
            return;
        }

        inventoryService.decreaseStockAfterSale(currentCart);
        revenue += lastComputedBill;

        // R10 LOGIC: Successfully paid, officially book the slot!
        if (checkoutCustomer.getPendingDeliveryRequest() != null) {
            checkoutCustomer.getPendingDeliveryRequest().getSlot().book();
        }

        System.out.println("Payment accepted: " + result.getMessage());
        System.out.printf("Receipt printed. Amount paid: %.2f%n", lastComputedBill);
        
        checkoutCustomer.clearPendingDeliveryRequest();
        
        currentCart = null;
        checkoutCustomer = null;
        lastComputedBill = 0.0;
    }

    public void showRevenue() {
        auth.requireManager();
        System.out.printf("Total revenue: %.2f%n", revenue);
    }
    
    public void showInventory() {
        auth.requireManager();

        System.out.println("Inventory:");

        for (Item item : items.values()) {
            System.out.printf(
                    "- %s | category: %s | price: %.2f | weight: %.2f kg | stock: %d%n",
                    item.getName(),
                    item.getCategory().getName(),
                    item.getUnitPrice(),
                    item.getWeight(),
                    item.getStock()
            );
        }
    }
    
    // R10 LOGIC: Added slotId parameter to track delivery capacities
    public void requestDelivery(String address, String slotId) {
        auth.requireCustomer();
        Customer customer = (Customer) auth.getCurrentUser();

        DeliverySlot slot = deliveryService.getSlot(slotId);
        if (slot == null) {
            throw new IllegalArgumentException("Unknown slot ID");
        }
        if (!slot.hasCapacity()) {
            throw new IllegalStateException("This delivery slot is fully booked.");
        }

        double distanceKm = 10.0;
        customer.setPendingDeliveryRequest(new DeliveryRequest(address, distanceKm, slot));

        System.out.println("Delivery requested for next purchase to: " + address + " during " + slot.getTimeWindow());
    }

    public void showDeliverySlots() {
        deliveryService.displayAvailableSlots();
    }
    
    public void restock(String itemName, int quantity) {
        auth.requireManager();

        Item item = items.get(itemName);
        if (item == null) {
            throw new IllegalArgumentException("unknown item: " + itemName);
        }

        item.restock(quantity);
        System.out.println("Restocked " + itemName + " by " + quantity + ". New stock: " + item.getStock());
    }
}