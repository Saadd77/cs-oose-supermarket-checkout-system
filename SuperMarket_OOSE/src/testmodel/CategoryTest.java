package testmodel;

import model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void categoryDiscountIsAppliedCorrectly() {
        Category category = new Category("fruit-and-vegetables");
        category.setDiscountPercent(10);

        assertEquals(90.0, category.applyDiscount(100.0));
    }

    @Test
    public void invalidDiscountThrowsException() {
        Category category = new Category("dairy");

        assertThrows(IllegalArgumentException.class, () -> {
            category.setDiscountPercent(150);
        });
    }
}