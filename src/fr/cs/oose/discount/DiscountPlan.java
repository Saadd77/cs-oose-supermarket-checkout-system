package fr.cs.oose.discount;

public interface DiscountPlan {
    String getName();
    double applyDiscount(double total);
    double applyDeliveryDiscount(double deliveryFee);
}