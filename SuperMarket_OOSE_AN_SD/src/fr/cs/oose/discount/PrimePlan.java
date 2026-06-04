package fr.cs.oose.discount;

public class PrimePlan implements DiscountPlan {
    @Override
    public String getName() {
        return "prime";
    }

    @Override
    public double applyDiscount(double total) {
        if (total >= 50.0) {
            return total * 0.8;
        }
        return total;
    }

    @Override
    public double applyDeliveryDiscount(double deliveryFee) {
        return deliveryFee * 0.5;
    }
}