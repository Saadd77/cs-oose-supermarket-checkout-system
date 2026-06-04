package testmodel;

import discount.DiscountPlan;
import discount.NormalPlan;
import discount.PrimePlan;
import discount.PlatinumPlan;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountPlanTest {

    @Test
    public void normalPlanDoesNotChangeTotal() {
        DiscountPlan plan = new NormalPlan();

        assertEquals(100.0, plan.applyDiscount(100.0), 0.001);
    }

    @Test
    public void primePlanAppliesDiscountWhenTotalIsAtLeast50() {
        DiscountPlan plan = new PrimePlan();

        assertEquals(80.0, plan.applyDiscount(100.0), 0.001);
    }

    @Test
    public void primePlanDoesNotApplyDiscountBelow50() {
        DiscountPlan plan = new PrimePlan();

        assertEquals(40.0, plan.applyDiscount(40.0), 0.001);
    }

    @Test
    public void primePlanAppliesDiscountExactlyAt50() {
        DiscountPlan plan = new PrimePlan();

        assertEquals(40.0, plan.applyDiscount(50.0), 0.001);
    }

    @Test
    public void platinumPlanAlwaysAppliesDiscount() {
        DiscountPlan plan = new PlatinumPlan();

        assertEquals(70.0, plan.applyDiscount(100.0), 0.001);
    }

    @Test
    public void normalPlanDoesNotReduceDeliveryFee() {
        DiscountPlan plan = new NormalPlan();

        assertEquals(15.0, plan.applyDeliveryDiscount(15.0), 0.001);
    }

    @Test
    public void primePlanReducesDeliveryFeeByHalf() {
        DiscountPlan plan = new PrimePlan();

        assertEquals(7.5, plan.applyDeliveryDiscount(15.0), 0.001);
    }

    @Test
    public void platinumPlanMakesDeliveryFree() {
        DiscountPlan plan = new PlatinumPlan();

        assertEquals(0.0, plan.applyDeliveryDiscount(15.0), 0.001);
    }
}