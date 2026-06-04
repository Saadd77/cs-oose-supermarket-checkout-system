package testmodel;

import delivery.DeliveryRequest;
import delivery.DeliveryService;
import model.Cart;
import model.Category;
import model.Item;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeliveryServiceTest {

    @Test
    public void deliveryUnder10KgAndWithin30KmCosts15() {
        Category category = new Category("fruit-and-vegetables");
        Item apple = new Item("apple", category, 1.0, 0.2, 100);

        Cart cart = new Cart();
        cart.addItem(apple, 10);

        DeliveryRequest request = new DeliveryRequest("Paris", 10.0);
        DeliveryService service = new DeliveryService();

        assertEquals(15.0, service.computeBaseDeliveryFee(cart, request), 0.001);
    }

    @Test
    public void deliveryWeightIsComputedCorrectly() {
        Category category = new Category("fruit-and-vegetables");
        Item apple = new Item("apple", category, 1.0, 0.2, 100);

        Cart cart = new Cart();
        cart.addItem(apple, 10);

        DeliveryService service = new DeliveryService();

        assertEquals(2.0, service.computeTotalWeight(cart), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void deliveryAbove50KgIsRefused() {
        Category category = new Category("meat");
        Item chicken = new Item("chicken", category, 8.0, 10.0, 100);

        Cart cart = new Cart();
        cart.addItem(chicken, 6);

        DeliveryRequest request = new DeliveryRequest("Paris", 10.0);
        DeliveryService service = new DeliveryService();

        service.computeBaseDeliveryFee(cart, request);
    }
}