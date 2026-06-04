package testmodel;

import delivery.DeliveryRequest;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeliveryRequestTest {

    @Test
    public void deliveryRequestStoresAddressAndDistance() {
        DeliveryRequest request = new DeliveryRequest("Nabatieh", 10.0);

        assertEquals("Nabatieh", request.getAddress());
        assertEquals(10.0, request.getDistanceKm(), 0.001);
    }
}