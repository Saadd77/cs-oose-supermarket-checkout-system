package fr.cs.oose.payment;

public class POS {
    private final TransactionAuthorizationSystem tas;
    private PaymentOutcome forcedOutcome;

    public POS(TransactionAuthorizationSystem tas) {
        this.tas = tas;
        this.forcedOutcome = null;
    }

    public void simulatePayment(PaymentOutcome outcome) {
        this.forcedOutcome = outcome;
    }

    public PaymentResult pay(String cardNumber, String pin, double amount) {
        if (forcedOutcome != null) {
            PaymentOutcome outcome = forcedOutcome;
            forcedOutcome = null;

            switch (outcome) {
                case SUCCESS:
                    return new PaymentResult(true, "simulated payment success");
                case INSUFFICIENT_FUNDS:
                    return new PaymentResult(false, "simulated insufficient funds");
                case PIN_WRONG:
                    return new PaymentResult(false, "simulated wrong PIN");
                case AUTH_DENIED:
                    return new PaymentResult(false, "simulated authorization denied");
                default:
                    return new PaymentResult(false, "unknown simulated outcome");
            }
        }

        return tas.authorize(cardNumber, pin, amount);
    }
}