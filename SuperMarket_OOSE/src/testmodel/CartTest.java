package testmodel;

import model.Cart;
import model.Category;
import model.Item;
import org.junit.Test;

import static org.junit.Assert.*;

public class CartTest {

    @Test
    public void cartComputesRawTotalCorrectly() {
        Category category = new Category("fruit-and-vegetables");
        Item apple = new Item("apple", category, 0.3, 0.2, 100);

        Cart cart = new Cart();
        cart.addItem(apple, 10);

        assertEquals(3.0, cart.computeRawTotal(), 0.001);
    }

    @Test
    public void cartComputesTotalWithCategoryDiscount() {
        Category category = new Category("fruit-and-vegetables");
        category.setDiscountPercent(10);

        Item apple = new Item("apple", category, 1.0, 0.2, 100);

        Cart cart = new Cart();
        cart.addItem(apple, 10);

        assertEquals(9.0, cart.computeTotal(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddMoreThanAvailableStock() {
        Category category = new Category("meat");
        Item steak = new Item("steak", category, 12.5, 0.5, 5);

        Cart cart = new Cart();
        cart.addItem(steak, 10);
    }

    @Test
    public void cartCanContainMultipleItems() {
        Category dairy = new Category("dairy");
        Category meat = new Category("meat");

        Item milk = new Item("milk", dairy, 1.2, 1.0, 50);
        Item steak = new Item("steak", meat, 12.5, 0.5, 5);

        Cart cart = new Cart();
        cart.addItem(milk, 2);
        cart.addItem(steak, 1);

        assertEquals(14.9, cart.computeRawTotal(), 0.001);
    }
}