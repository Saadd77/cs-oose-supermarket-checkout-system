package testmodel;

import model.Manager;
import model.Cashier;
import model.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void managerPasswordCheckWorks() {
        Manager manager = new Manager("CEO", "Manager", "ceo", "123456789");

        assertTrue(manager.checkPassword("123456789"));
        assertFalse(manager.checkPassword("wrong"));
    }

    @Test
    public void cashierPasswordCheckWorks() {
        Cashier cashier = new Cashier("Saad", "Dakdouki", "saad", "saadpwd");

        assertTrue(cashier.checkPassword("saadpwd"));
        assertFalse(cashier.checkPassword("wrong"));
    }

    @Test
    public void customerPasswordCheckWorks() {
        Customer customer = new Customer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        assertTrue(customer.checkPassword("andrewpwd"));
        assertFalse(customer.checkPassword("wrong"));
    }
}