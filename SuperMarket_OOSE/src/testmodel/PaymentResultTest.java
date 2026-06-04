package testmodel;

import org.junit.Test;
import payment.PaymentResult;

import static org.junit.Assert.*;

public class PaymentResultTest {

    @Test
    public void successfulPaymentResultStoresSuccessAndMessage() {
        PaymentResult result = new PaymentResult(true, "payment authorized");

        assertTrue(result.isSuccess());
        assertEquals("payment authorized", result.getMessage());
    }

    @Test
    public void failedPaymentResultStoresFailureAndMessage() {
        PaymentResult result = new PaymentResult(false, "wrong PIN");

        assertFalse(result.isSuccess());
        assertEquals("wrong PIN", result.getMessage());
    }
}