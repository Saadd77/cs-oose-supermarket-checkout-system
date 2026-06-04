package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.delivery.DeliveryRequest;
import fr.cs.oose.delivery.DeliverySlot;

import static org.junit.Assert.*;

public class DeliveryRequestTest {

    @Test
    public void deliveryRequestStoresAddressAndDistance() {
    	DeliverySlot slot = new DeliverySlot("slot1", "10:00-12:00", true, false, 3);
        DeliveryRequest request = new DeliveryRequest("Nabatieh", 10.0, slot);

        assertEquals("Nabatieh", request.getAddress());
        assertEquals(10.0, request.getDistanceKm(), 0.001);
        assertEquals(slot, request.getSlot()); // New assertion    
    }
}