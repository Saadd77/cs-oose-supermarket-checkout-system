package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.model.Cashier;
import fr.cs.oose.model.Customer;
import fr.cs.oose.model.Manager;
import fr.cs.oose.model.User;
import fr.cs.oose.system.AuthManager;

import static org.junit.Assert.*;

public class AuthManagerTest {

    @Test
    public void ceoExistsByDefaultAndCanLogin() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");

        assertNotNull(auth.getCurrentUser());
        assertTrue(auth.getCurrentUser() instanceof Manager);
        assertEquals("ceo", auth.getCurrentUser().getUsername());
    }

    @Test
    public void setupDefaultUsersCreatesDefaultCashier() {
        AuthManager auth = new AuthManager();

        auth.setupDefaultUsers();

        User cashier = auth.getUser("cashier1");

        assertNotNull(cashier);
        assertTrue(cashier instanceof Cashier);
        assertTrue(cashier.checkPassword("pass"));
    }

    @Test
    public void logoutClearsCurrentUser() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.logout();

        assertNull(auth.getCurrentUser());
    }

    @Test(expected = IllegalStateException.class)
    public void logoutWithoutLoggedUserThrowsException() {
        AuthManager auth = new AuthManager();

        auth.logout();
    }

    @Test(expected = IllegalArgumentException.class)
    public void loginWithUnknownUserThrowsException() {
        AuthManager auth = new AuthManager();

        auth.login("unknown", "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void loginWithWrongPasswordThrowsException() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "wrongpassword");
    }

    @Test
    public void managerCanRegisterCashier() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");

        User user = auth.getUser("saad");

        assertNotNull(user);
        assertTrue(user instanceof Cashier);
        assertTrue(user.checkPassword("saadpwd"));
    }

    @Test
    public void managerCanRegisterCustomer() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        User user = auth.getUser("andrew");

        assertNotNull(user);
        assertTrue(user instanceof Customer);
        assertTrue(user.checkPassword("andrewpwd"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registeringExistingUsernameThrowsException() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        auth.registerCustomer("Andrew", "Nohra", "saad", "Nabatieh", "andrewpwd");
    }

    @Test(expected = IllegalStateException.class)
    public void cashierCannotRegisterAnotherCashier() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        auth.logout();

        auth.login("saad", "saadpwd");
        auth.registerCashier("Other", "Cashier", "other", "otherpwd");
    }

    @Test(expected = IllegalStateException.class)
    public void customerCannotRegisterCashier() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        auth.logout();

        auth.login("andrew", "andrewpwd");
        auth.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
    }

    @Test
    public void requireManagerAcceptsManager() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");

        auth.requireManager();
    }

    @Test(expected = IllegalStateException.class)
    public void requireManagerRejectsCashier() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        auth.logout();

        auth.login("saad", "saadpwd");

        auth.requireManager();
    }

    @Test
    public void requireCashierAcceptsCashier() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCashier("Saad", "Dakdouki", "saad", "saadpwd");
        auth.logout();

        auth.login("saad", "saadpwd");

        auth.requireCashier();
    }

    @Test(expected = IllegalStateException.class)
    public void requireCashierRejectsManager() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");

        auth.requireCashier();
    }

    @Test
    public void requireCustomerAcceptsCustomer() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");
        auth.registerCustomer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");
        auth.logout();

        auth.login("andrew", "andrewpwd");

        auth.requireCustomer();
    }

    @Test(expected = IllegalStateException.class)
    public void requireCustomerRejectsManager() {
        AuthManager auth = new AuthManager();

        auth.login("ceo", "123456789");

        auth.requireCustomer();
    }
}