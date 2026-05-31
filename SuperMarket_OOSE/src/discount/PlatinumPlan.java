package discount;

public class PlatinumPlan implements DiscountPlan {
    @Override
    public String getName() {
        return "platinum";
    }

    @Override
    public double applyDiscount(double total) {
        return total * 0.7;
    }

    @Override
    public double applyDeliveryDiscount(double deliveryFee) {
        return 0.0;
    }
}