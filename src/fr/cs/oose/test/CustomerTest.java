package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.delivery.DeliveryRequest;
import fr.cs.oose.delivery.DeliverySlot;
import fr.cs.oose.discount.PrimePlan;
import fr.cs.oose.model.Customer;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void customerStartsWithNormalPlan() {
        Customer customer = new Customer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        assertEquals("normal", customer.getPlan().getName());
    }

    @Test
    public void customerPlanCanBeChanged() {
        Customer customer = new Customer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        customer.setPlan(new PrimePlan());

        assertEquals("prime", customer.getPlan().getName());
    }

    @Test
    public void customerCanStorePendingDeliveryRequest() {
        Customer customer = new Customer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        DeliverySlot slot = new DeliverySlot("slot1", "10:00-12:00", false, true, 3);
        DeliveryRequest request = new DeliveryRequest("Nabatieh", 10.0, slot);

        customer.setPendingDeliveryRequest(request);

        assertNotNull(customer.getPendingDeliveryRequest());
        assertEquals("Nabatieh", customer.getPendingDeliveryRequest().getAddress());
        assertEquals(10.0, customer.getPendingDeliveryRequest().getDistanceKm(), 0.001);
        assertEquals("slot1", customer.getPendingDeliveryRequest().getSlot().getId());
        assertEquals("10:00-12:00", customer.getPendingDeliveryRequest().getSlot().getTimeWindow());
    }

    @Test
    public void customerCanClearPendingDeliveryRequest() {
        Customer customer = new Customer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        DeliverySlot slot = new DeliverySlot("slot1", "10:00-12:00", false, true, 3);
        DeliveryRequest request = new DeliveryRequest("Nabatieh", 10.0, slot);

        customer.setPendingDeliveryRequest(request);
        customer.clearPendingDeliveryRequest();

        assertNull(customer.getPendingDeliveryRequest());
    }

    @Test
    public void deliverySlotCanBeBooked() {
        DeliverySlot slot = new DeliverySlot("slot1", "10:00-12:00", false, true, 3);

        assertTrue(slot.hasCapacity());

        slot.book();

        assertTrue(slot.hasCapacity());
    }

    @Test(expected = IllegalStateException.class)
    public void deliverySlotThrowsExceptionWhenFullyBooked() {
        DeliverySlot slot = new DeliverySlot("slot1", "10:00-12:00", false, true, 1);

        slot.book();
        slot.book();
    }
}