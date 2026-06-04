package testmodel;

import org.junit.Test;
import system.SupermarketSystem;

import static org.junit.Assert.*;

public class SuperMarketSystemTest {

    @Test
    public void managerCanAddItemAndCashierCanComputeBill() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();

        system.addItem("manoushe", "bakery", 2.0, 0.3, 100);
        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        system.logout();

        system.login("saad", "saadpwd");
        system.startCheckout("andrew");
        system.scanItem("manoushe", 5);

        double bill = system.computeBill();

        assertEquals(10.0, bill, 0.001);
    }

    @Test
    public void categoryDiscountIsAppliedInSystemBill() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();

        system.addItem("labneh", "dairy", 3.0, 0.5, 50);
        system.setCategoryDiscount("dairy", 10);

        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        system.logout();

        system.login("saad", "saadpwd");
        system.startCheckout("andrew");
        system.scanItem("labneh", 10);

        double bill = system.computeBill();

        assertEquals(27.0, bill, 0.001);
    }

    @Test
    public void primePlanIsAppliedInSystemBillWhenTotalIsAtLeast50() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();

        system.addItem("kafta", "meat", 10.0, 0.5, 100);
        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        system.logout();

        system.login("andrew", "andrewpwd");
        system.subscribeToPlan("prime");
        system.logout();

        system.login("saad", "saadpwd");
        system.startCheckout("andrew");
        system.scanItem("kafta", 10);

        double bill = system.computeBill();

        assertEquals(80.0, bill, 0.001);
    }

    @Test
    public void deliveryFeeIsReducedForPrimeCustomer() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();

        system.addItem("manoushe", "bakery", 10.0, 0.3, 100);
        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        system.logout();

        system.login("andrew", "andrewpwd");
        system.subscribeToPlan("prime");
        system.requestDelivery("Nabatieh");
        system.logout();

        system.login("saad", "saadpwd");
        system.startCheckout("andrew");
        system.scanItem("manoushe", 10);

        double bill = system.computeBill();

        // Items total = 100
        // Prime discount = 80
        // Base delivery fee = 15
        // Prime delivery discount = 7.5
        // Final total = 87.5
        assertEquals(87.5, bill, 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void cashierCannotAddItem() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();
        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.logout();

        system.login("saad", "saadpwd");
        system.addItem("manoushe", "bakery", 2.0, 0.3, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loginWithWrongPasswordFails() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "wrongpassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void startingCheckoutForUnknownCustomerFails() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();
        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.logout();

        system.login("saad", "saadpwd");
        system.startCheckout("unknownCustomer");
    }

    @Test(expected = IllegalArgumentException.class)
    public void scanningUnknownItemFails() {
        SupermarketSystem system = new SupermarketSystem();

        system.login("ceo", "123456789");
        system.setup();
        system.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        system.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        system.logout();

        system.login("saad", "saadpwd");
        system.startCheckout("andrew");
        system.scanItem("unknownItem", 1);
    }
}