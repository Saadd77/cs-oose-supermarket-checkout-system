package testmodel;

import org.junit.Test;
import payment.PaymentOutcome;

import static org.junit.Assert.*;

public class PaymentOutcomeTest {

    @Test
    public void paymentOutcomeContainsSuccess() {
        assertEquals(PaymentOutcome.SUCCESS, PaymentOutcome.valueOf("SUCCESS"));
    }

    @Test
    public void paymentOutcomeContainsInsufficientFunds() {
        assertEquals(PaymentOutcome.INSUFFICIENT_FUNDS, PaymentOutcome.valueOf("INSUFFICIENT_FUNDS"));
    }

    @Test
    public void paymentOutcomeContainsPinWrong() {
        assertEquals(PaymentOutcome.PIN_WRONG, PaymentOutcome.valueOf("PIN_WRONG"));
    }

    @Test
    public void paymentOutcomeContainsAuthorizationDenied() {
        assertEquals(PaymentOutcome.AUTH_DENIED, PaymentOutcome.valueOf("AUTH_DENIED"));
    }
}