package fr.cs.oose.discount;

public class NormalPlan implements DiscountPlan {
    @Override
    public String getName() {
        return "normal";
    }

    @Override
    public double applyDiscount(double total) {
        return total;
    }

    @Override
    public double applyDeliveryDiscount(double deliveryFee) {
        return deliveryFee;
    }
}