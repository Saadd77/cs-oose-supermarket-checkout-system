package fr.cs.oose.model;

import fr.cs.oose.delivery.DeliveryRequest;
import fr.cs.oose.discount.DiscountPlan;
import fr.cs.oose.discount.NormalPlan;

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