package model;

import discount.DiscountPlan;
import discount.NormalPlan;
import delivery.DeliveryRequest;

public class Customer extends User {
    private final String address;
    private DiscountPlan plan;
    private DeliveryRequest pendingDeliveryRequest;

    public Customer(String firstName, String lastName, String username, String address, String password) {
        super(firstName, lastName, username, password);
        this.address = address;
        this.plan = new NormalPlan();
    }

    public String getAddress() {
        return address;
    }

    public DiscountPlan getPlan() {
        return plan;
    }

    public void setPlan(DiscountPlan plan) {
        this.plan = plan;
    }
    
    public DeliveryRequest getPendingDeliveryRequest() {
        return pendingDeliveryRequest;
    }

    public void setPendingDeliveryRequest(DeliveryRequest pendingDeliveryRequest) {
        this.pendingDeliveryRequest = pendingDeliveryRequest;
    }

    public void clearPendingDeliveryRequest() {
        this.pendingDeliveryRequest = null;
    }
}