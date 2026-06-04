package fr.cs.oose.test;

import org.junit.Test;

import fr.cs.oose.cli.SupermarketCLI;

public class SupermarketCLITest {

    @Test
    public void cliCanExecuteBasicCommands() {
        SupermarketCLI cli = new SupermarketCLI();

        cli.execute("login ceo 123456789");
        cli.execute("setup");
        cli.execute("addItem manoushe bakery 2.0 0.3 100");
        cli.execute("registerCashier Saad Dakdouki saad saadpwd");
        cli.execute("registerCustomer Andrew Nohra andrew Nabatieh andrewpwd");
        cli.execute("logout");

        cli.execute("login saad saadpwd");
        cli.execute("startCheckout andrew");
        cli.execute("scanItem manoushe 5");
        cli.execute("computeBill");
    }

    @Test
    public void cliSupportsQuotedArguments() {
        SupermarketCLI cli = new SupermarketCLI();

        cli.execute("login ceo 123456789");
        cli.execute("setup");
        cli.execute("registerCustomer Andrew Nohra andrew \"Nabatieh Main Street\" andrewpwd");
    }

    @Test
    public void cliHandlesUnknownCommandWithoutCrashing() {
        SupermarketCLI cli = new SupermarketCLI();

        cli.execute("unknownCommand");
    }
}