package payment;

import java.util.HashMap;
import java.util.Map;

public class TransactionAuthorizationSystem {
    private final Map<String, BankCard> cards = new HashMap<>();

    public void registerCard(BankCard card) {
        cards.put(card.getCardNumber(), card);
    }

    public PaymentResult authorize(String cardNumber, String pin, double amount) {
        BankCard card = cards.get(cardNumber);

        if (card == null) {
            return new PaymentResult(false, "unregistered bank card");
        }

        if (!card.checkPin(pin)) {
            return new PaymentResult(false, "wrong PIN");
        }

        if (!card.hasEnoughBalance(amount)) {
            return new PaymentResult(false, "insufficient funds");
        }

        card.debit(amount);
        return new PaymentResult(true, "payment authorized");
    }
}