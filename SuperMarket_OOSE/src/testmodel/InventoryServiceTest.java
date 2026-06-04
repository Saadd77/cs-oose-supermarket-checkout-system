package testmodel;

import inventory.InventoryService;
import inventory.StockObserver;
import model.Cart;
import model.Category;
import model.Item;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryServiceTest {

    private static class TestObserver implements StockObserver {
        private boolean notified = false;

        @Override
        public void update(Item item) {
            notified = true;
        }

        public boolean wasNotified() {
            return notified;
        }
    }

    @Test
    public void stockIsDecreasedAfterSale() {
        Category category = new Category("meat");
        Item steak = new Item("steak", category, 12.5, 0.5, 5);

        Cart cart = new Cart();
        cart.addItem(steak, 2);

        InventoryService service = new InventoryService();
        service.decreaseStockAfterSale(cart);

        assertEquals(3, steak.getStock());
    }

    @Test
    public void observerIsNotifiedWhenPerishableItemIsLowStock() {
        Category category = new Category("meat");
        Item steak = new Item("steak", category, 12.5, 0.5, 5);

        Cart cart = new Cart();
        cart.addItem(steak, 1);

        TestObserver observer = new TestObserver();

        InventoryService service = new InventoryService();
        service.addObserver(observer);
        service.decreaseStockAfterSale(cart);

        assertTrue(observer.wasNotified());
    }

    @Test
    public void observerIsNotNotifiedForNonPerishableItem() {
        Category category = new Category("fruit-and-vegetables");
        Item apple = new Item("apple", category, 1.0, 0.2, 5);

        Cart cart = new Cart();
        cart.addItem(apple, 1);

        TestObserver observer = new TestObserver();

        InventoryService service = new InventoryService();
        service.addObserver(observer);
        service.decreaseStockAfterSale(cart);

        assertFalse(observer.wasNotified());
    }
}