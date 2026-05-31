package system;

import model.*;
import discount.*;
import payment.*;
import inventory.InventoryService;
import inventory.ManagerNotifier;
import inventory.SupplierNotifier;
import delivery.DeliveryRequest;
import delivery.DeliveryService;

import java.util.HashMap;
import java.util.Map;

public class SupermarketSystem {
    private final Map<String, User> users = new HashMap<>();
    private User currentUser;
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
        users.put("ceo", new Manager("CEO", "Manager", "ceo", "123456789"));

        inventoryService.addObserver(new ManagerNotifier());
        inventoryService.addObserver(new SupplierNotifier());
    }

    public void setup() {
        users.put("cashier1", new Cashier("Default", "Cashier", "cashier1", "pass"));
        tas.registerCard(new BankCard("4242424242424242", "1234", 10000.0));
        System.out.println("Default setup loaded.");
    }

    public void login(String username, String password) {
        User user = users.get(username);

        if (user == null) {
            throw new IllegalArgumentException("unknown user: " + username);
        }

        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("wrong password");
        }

        currentUser = user;
        System.out.println("Logged in as " + username);
    }

    public void logout() {
        if (currentUser == null) {
            throw new IllegalStateException("no user is currently logged in");
        }

        System.out.println("Logged out from " + currentUser.getUsername());
        currentUser = null;
    }

    public void registerCashier(String firstName, String lastName, String username, String password) {
        requireManager();

        if (users.containsKey(username)) {
            throw new IllegalArgumentException("username already exists: " + username);
        }

        users.put(username, new Cashier(firstName, lastName, username, password));
        System.out.println("Cashier registered: " + username);
    }

    public void registerCustomer(String firstName, String lastName, String username, String address, String password) {
        requireManager();

        if (users.containsKey(username)) {
            throw new IllegalArgumentException("username already exists: " + username);
        }

        users.put(username, new Customer(firstName, lastName, username, address, password));
        System.out.println("Customer registered: " + username);
    }

    private void requireManager() {
        if (!(currentUser instanceof Manager)) {
            throw new IllegalStateException("manager login required");
        }
    }
    
    public void addItem(String itemName, String categoryName, double unitPrice, double weight, int initialStock) {
        requireManager();

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
        requireManager();

        Category category = categories.get(categoryName);

        if (category == null) {
            throw new IllegalArgumentException("unknown category: " + categoryName);
        }

        category.setDiscountPercent(discountPercent);
        System.out.println("Discount set for category " + categoryName + ": " + discountPercent + "%");
    }

    public void startCheckout(String customerUsername) {
        requireCashier();

        User user = users.get(customerUsername);

        if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("unknown customer: " + customerUsername);
        }

        checkoutCustomer = (Customer) user;
        currentCart = new Cart();

        System.out.println("Checkout started for customer: " + customerUsername);
    }

    public void scanItem(String itemName, int quantity) {
        requireCashier();

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
        requireCashier();

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

    private void requireCashier() {
        if (!(currentUser instanceof Cashier)) {
            throw new IllegalStateException("cashier login required");
        }
    }
    
    public void subscribeToPlan(String planName) {
        requireCustomer();

        Customer customer = (Customer) currentUser;

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
    
    private void requireCustomer() {
        if (!(currentUser instanceof Customer)) {
            throw new IllegalStateException("customer login required");
        }
    }
    
    public void simulatePayment(String outcomeText) {
        requireCashier();

        PaymentOutcome outcome = PaymentOutcome.valueOf(outcomeText);
        pos.simulatePayment(outcome);

        System.out.println("Next payment outcome forced to: " + outcome);
    }

    public void pay(String cardNumber, String pin) {
        requireCashier();

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

        System.out.println("Payment accepted: " + result.getMessage());
        System.out.printf("Receipt printed. Amount paid: %.2f%n", lastComputedBill);
        
        checkoutCustomer.clearPendingDeliveryRequest();
        
        currentCart = null;
        checkoutCustomer = null;
        lastComputedBill = 0.0;
    }

    public void showRevenue() {
        requireManager();
        System.out.printf("Total revenue: %.2f%n", revenue);
    }
    
    public void showInventory() {
        requireManager();

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
    
    public void requestDelivery(String address) {
        requireCustomer();

        Customer customer = (Customer) currentUser;

        // Simple fixed distance for now.
        // Later you can improve this if needed.
        double distanceKm = 10.0;

        customer.setPendingDeliveryRequest(new DeliveryRequest(address, distanceKm));

        System.out.println("Delivery requested for next purchase to: " + address);
    }
    
    public void restock(String itemName, int quantity) {
        requireManager();

        Item item = items.get(itemName);

        if (item == null) {
            throw new IllegalArgumentException("unknown item: " + itemName);
        }

        item.restock(quantity);
        System.out.println("Restocked " + itemName + " by " + quantity + ". New stock: " + item.getStock());
    }
}