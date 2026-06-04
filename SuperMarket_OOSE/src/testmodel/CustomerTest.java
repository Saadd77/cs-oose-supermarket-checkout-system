package testmodel;

import discount.PrimePlan;
import model.Customer;
import org.junit.Test;

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

        delivery.DeliveryRequest request = new delivery.DeliveryRequest("Nabatieh", 10.0);
        customer.setPendingDeliveryRequest(request);

        assertNotNull(customer.getPendingDeliveryRequest());
        assertEquals("Nabatieh", customer.getPendingDeliveryRequest().getAddress());
    }

    @Test
    public void customerCanClearPendingDeliveryRequest() {
        Customer customer = new Customer("Andrew", "Nohra", "andrew", "Nabatieh", "andrewpwd");

        delivery.DeliveryRequest request = new delivery.DeliveryRequest("Nabatieh", 10.0);
        customer.setPendingDeliveryRequest(request);
        customer.clearPendingDeliveryRequest();

        assertNull(customer.getPendingDeliveryRequest());
    }
}