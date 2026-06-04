package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.delivery.DeliveryRequest;
import fr.cs.oose.delivery.DeliveryService;
import fr.cs.oose.delivery.DeliverySlot;
import fr.cs.oose.model.Cart;
import fr.cs.oose.model.Category;
import fr.cs.oose.model.Item;

import static org.junit.Assert.*;

public class DeliveryServiceTest {

    @Test
    public void deliveryUnder10KgAndWithin30KmCosts15() {
        Category category = new Category("fruit-and-vegetables");
        Item apple = new Item("apple", category, 1.0, 0.2, 100);

        Cart cart = new Cart();
        cart.addItem(apple, 10);

        DeliverySlot neutralSlot = new DeliverySlot("testSlot", "12:00-14:00", false, false, 5);
        DeliveryRequest request = new DeliveryRequest("Paris", 10.0, neutralSlot);
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

        DeliverySlot neutralSlot = new DeliverySlot("testSlot", "12:00-14:00", false, false, 5);
        DeliveryRequest request = new DeliveryRequest("Paris", 10.0, neutralSlot);
        DeliveryService service = new DeliveryService();

        service.computeBaseDeliveryFee(cart, request);
    }
}