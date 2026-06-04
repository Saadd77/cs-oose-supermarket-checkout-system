package fr.cs.oose.payment;

public class BankCard {
    private final String cardNumber;
    private final String pin;
    private double balance;

    public BankCard(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
    }

    public boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    public void debit(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("insufficient funds");
        }

        balance -= amount;
    }
}