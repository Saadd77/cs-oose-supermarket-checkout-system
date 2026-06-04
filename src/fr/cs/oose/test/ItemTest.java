package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.model.Category;
import fr.cs.oose.model.Item;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void stockDecreasesCorrectly() {
        Category category = new Category("meat");
        Item item = new Item("steak", category, 12.5, 0.5, 5);

        item.decreaseStock(2);

        assertEquals(3, item.getStock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotDecreaseMoreThanAvailableStock() {
        Category category = new Category("meat");
        Item item = new Item("steak", category, 12.5, 0.5, 5);

        item.decreaseStock(10);
    }

    @Test
    public void restockIncreasesStockCorrectly() {
        Category category = new Category("dairy");
        Item item = new Item("milk", category, 1.2, 1.0, 50);

        item.restock(10);

        assertEquals(60, item.getStock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotRestockWithNegativeQuantity() {
        Category category = new Category("dairy");
        Item item = new Item("milk", category, 1.2, 1.0, 50);

        item.restock(-5);
    }

    @Test
    public void meatIsPerishable() {
        Category category = new Category("meat");
        Item item = new Item("steak", category, 12.5, 0.5, 5);

        assertTrue(item.isPerishable());
    }

    @Test
    public void dairyIsPerishable() {
        Category category = new Category("dairy");
        Item item = new Item("milk", category, 1.2, 1.0, 50);

        assertTrue(item.isPerishable());
    }

    @Test
    public void fruitAndVegetablesAreNotPerishableInOurImplementation() {
        Category category = new Category("fruit-and-vegetables");
        Item item = new Item("apple", category, 0.3, 0.2, 100);

        assertFalse(item.isPerishable());
    }

    @Test
    public void itemIsLowStockWhenStockBelowThreshold() {
        Category category = new Category("meat");
        Item item = new Item("steak", category, 12.5, 0.5, 5);

        item.decreaseStock(1);

        assertTrue(item.isLowStock());
    }
}