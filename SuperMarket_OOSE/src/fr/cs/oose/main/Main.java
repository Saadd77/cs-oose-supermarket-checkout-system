package fr.cs.oose.main;

import java.io.File;

import fr.cs.oose.cli.SupermarketCLI;

public class Main {
    public static void main(String[] args) {
        SupermarketCLI cli = new SupermarketCLI();
        
        // Auto-load the configuration file if it exists
        File configFile = new File("my_supermarket.ini");
        if (configFile.exists()) {
            System.out.println("Loading initial configuration from my_supermarket.ini...");
            cli.execute("runTest my_supermarket.ini");
        } else {
            System.out.println("Warning: my_supermarket.ini not found. Starting with empty system.");
        }
        
        cli.start();
    }
}