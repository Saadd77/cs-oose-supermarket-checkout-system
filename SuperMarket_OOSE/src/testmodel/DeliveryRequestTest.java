package testmodel;

import delivery.DeliveryRequest;
import delivery.DeliverySlot;
import org.junit.Test;

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