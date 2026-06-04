package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.payment.BankCard;
import fr.cs.oose.payment.POS;
import fr.cs.oose.payment.PaymentOutcome;
import fr.cs.oose.payment.PaymentResult;
import fr.cs.oose.payment.TransactionAuthorizationSystem;

import static org.junit.Assert.*;

public class PaymentTest {

    @Test
    public void paymentIsAcceptedWhenCardPinAndBalanceAreCorrect() {
        TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();
        tas.registerCard(new BankCard("4242424242424242", "1234", 100.0));

        PaymentResult result = tas.authorize("4242424242424242", "1234", 50.0);

        assertTrue(result.isSuccess());
    }

    @Test
    public void paymentIsRefusedWhenPinIsWrong() {
        TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();
        tas.registerCard(new BankCard("4242424242424242", "1234", 100.0));

        PaymentResult result = tas.authorize("4242424242424242", "9999", 50.0);

        assertFalse(result.isSuccess());
        assertEquals("wrong PIN", result.getMessage());
    }

    @Test
    public void paymentIsRefusedWhenFundsAreInsufficient() {
        TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();
        tas.registerCard(new BankCard("4242424242424242", "1234", 20.0));

        PaymentResult result = tas.authorize("4242424242424242", "1234", 50.0);

        assertFalse(result.isSuccess());
        assertEquals("insufficient funds", result.getMessage());
    }

    @Test
    public void paymentIsRefusedForUnregisteredCard() {
        TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();

        PaymentResult result = tas.authorize("0000000000000000", "1234", 50.0);

        assertFalse(result.isSuccess());
        assertEquals("unregistered bank card", result.getMessage());
    }

    @Test
    public void simulatedPaymentSuccessWorks() {
        TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();
        POS pos = new POS(tas);

        pos.simulatePayment(PaymentOutcome.SUCCESS);
        PaymentResult result = pos.pay("whatever", "whatever", 50.0);

        assertTrue(result.isSuccess());
    }

    @Test
    public void simulatedInsufficientFundsWorks() {
        TransactionAuthorizationSystem tas = new TransactionAuthorizationSystem();
        POS pos = new POS(tas);

        pos.simulatePayment(PaymentOutcome.INSUFFICIENT_FUNDS);
        PaymentResult result = pos.pay("whatever", "whatever", 50.0);

        assertFalse(result.isSuccess());
    }
}