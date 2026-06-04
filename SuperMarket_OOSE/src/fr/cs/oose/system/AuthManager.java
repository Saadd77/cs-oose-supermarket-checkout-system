package fr.cs.oose.system;

import java.util.HashMap;
import java.util.Map;

import fr.cs.oose.model.Cashier;
import fr.cs.oose.model.Customer;
import fr.cs.oose.model.Manager;
import fr.cs.oose.model.User;

public class AuthManager {
    private final Map<String, User> users = new HashMap<>();
    private User currentUser;

    public AuthManager() {
        // Default CEO
        users.put("ceo", new Manager("CEO", "Manager", "ceo", "123456789"));
    }

    public void setupDefaultUsers() {
        users.put("cashier1", new Cashier("Default", "Cashier", "cashier1", "pass"));
    }

    public void login(String username, String password) {
        User user = users.get(username);
        if (user == null) throw new IllegalArgumentException("unknown user: " + username);
        if (!user.checkPassword(password)) throw new IllegalArgumentException("wrong password");
        currentUser = user;
    }

    public void logout() {
        if (currentUser == null) throw new IllegalStateException("no user is currently logged in");
        currentUser = null;
    }

    public void registerCashier(String firstName, String lastName, String username, String password) {
        requireManager();
        if (users.containsKey(username)) throw new IllegalArgumentException("username already exists");
        users.put(username, new Cashier(firstName, lastName, username, password));
    }

    public void registerCustomer(String firstName, String lastName, String username, String address, String password) {
        requireManager();
        if (users.containsKey(username)) throw new IllegalArgumentException("username already exists");
        users.put(username, new Customer(firstName, lastName, username, address, password));
    }

    public User getCurrentUser() { return currentUser; }
    public User getUser(String username) { return users.get(username); }

    public void requireManager() {
        if (!(currentUser instanceof Manager)) throw new IllegalStateException("manager login required");
    }

    public void requireCashier() {
        if (!(currentUser instanceof Cashier)) throw new IllegalStateException("cashier login required");
    }

    public void requireCustomer() {
        if (!(currentUser instanceof Customer)) throw new IllegalStateException("customer login required");
    }
}