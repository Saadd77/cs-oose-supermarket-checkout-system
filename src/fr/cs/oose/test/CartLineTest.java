package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.model.CartLine;
import fr.cs.oose.model.Category;
import fr.cs.oose.model.Item;

import static org.junit.Assert.*;

public class CartLineTest {

    @Test
    public void rawTotalIsComputedCorrectly() {
        Category category = new Category("dairy");
        Item milk = new Item("milk", category, 1.2, 1.0, 50);

        CartLine line = new CartLine(milk, 3);

        assertEquals(3.6, line.getRawTotal(), 0.001);
    }

    @Test
    public void discountedTotalUsesCategoryDiscount() {
        Category category = new Category("fruit-and-vegetables");
        category.setDiscountPercent(10);

        Item apple = new Item("apple", category, 1.0, 0.2, 100);

        CartLine line = new CartLine(apple, 10);

        assertEquals(9.0, line.getDiscountedTotal(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroQuantityThrowsException() {
        Category category = new Category("dairy");
        Item milk = new Item("milk", category, 1.2, 1.0, 50);

        new CartLine(milk, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeQuantityThrowsException() {
        Category category = new Category("dairy");
        Item milk = new Item("milk", category, 1.2, 1.0, 50);

        new CartLine(milk, -2);
    }
}