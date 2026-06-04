package cli;

import system.SupermarketSystem;

import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SupermarketCLI {
    private final SupermarketSystem system = new SupermarketSystem();

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Supermarket Checkout System");
        System.out.println("Type help for commands.");
        System.out.println("Type stop to exit.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("stop")) {
                System.out.println("Exiting...");
                break;
            }

            execute(input);
        }

        scanner.close();
    }

    public void execute(String input) {
        if (input.isEmpty() || input.startsWith("#")) {
            return;
        }

        String[] parts = parseArguments(input);
        String command = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        try {
            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "setup":
                    system.setup();
                    break;

                case "login":
                    checkArgCount(args, 2);
                    system.login(args[0], args[1]);
                    break;

                case "logout":
                    checkArgCount(args, 0);
                    system.logout();
                    break;

                case "registerCashier":
                    checkArgCount(args, 4);
                    system.registerCashier(args[0], args[1], args[2], args[3]);
                    break;

                case "registerCustomer":
                    checkArgCount(args, 5);
                    system.registerCustomer(args[0], args[1], args[2], args[3], args[4]);
                    break;
                    
                case "addItem":
                    checkArgCount(args, 5);
                    system.addItem(
                            args[0],
                            args[1],
                            Double.parseDouble(args[2]),
                            Double.parseDouble(args[3]),
                            Integer.parseInt(args[4])
                    );
                    break;

                case "setCategoryDiscount":
                    checkArgCount(args, 2);
                    system.setCategoryDiscount(args[0], Double.parseDouble(args[1]));
                    break;

                case "startCheckout":
                    checkArgCount(args, 1);
                    system.startCheckout(args[0]);
                    break;

                case "scanItem":
                    checkArgCount(args, 2);
                    system.scanItem(args[0], Integer.parseInt(args[1]));
                    break;

                case "computeBill":
                    checkArgCount(args, 0);
                    system.computeBill();
                    break;
                 
                case "subscribeToPlan":
                    checkArgCount(args, 1);
                    system.subscribeToPlan(args[0]);
                    break;
                    
                case "simulatePayment":
                    checkArgCount(args, 1);
                    system.simulatePayment(args[0]);
                    break;

                case "pay":
                    checkArgCount(args, 2);
                    system.pay(args[0], args[1]);
                    break;

                case "showRevenue":
                    checkArgCount(args, 0);
                    system.showRevenue();
                    break;
                    
                case "showInventory":
                    checkArgCount(args, 0);
                    system.showInventory();
                    break;
                
                case "showSlots":
                    checkArgCount(args, 0);
                    system.showDeliverySlots();
                    break;
                    
                case "requestDelivery":
                    checkArgCount(args, 2); // Now expects address AND slotId
                    system.requestDelivery(args[0], args[1]);
                    break;
                    
                case "runTest":
                    checkArgCount(args, 1);
                    runTest(args[0]);
                    break;
                    
                case "restock":
                    checkArgCount(args, 2);
                    system.restock(args[0], Integer.parseInt(args[1]));
                    break;

                default:
                    System.out.println("Unknown command: " + command);
                    System.out.println("Type help for commands.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String[] parseArguments(String input) {
        java.util.List<String> tokens = new java.util.ArrayList<>();

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(matcher.group(1));
            } else {
                tokens.add(matcher.group(2));
            }
        }

        return tokens.toArray(new String[0]);
    }

    private static void checkArgCount(String[] args, int expected) {
        if (args.length != expected) {
            throw new IllegalArgumentException(
                    "wrong number of arguments. Expected " + expected + ", got " + args.length
            );
        }
    }
    
    private void runTest(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }

            System.out.println("> " + trimmed);
            execute(trimmed);
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("login <username> <password>");
        System.out.println("logout");
        System.out.println("setup");
        System.out.println("registerCashier <firstName> <lastName> <username> <password>");
        System.out.println("registerCustomer <firstName> <lastName> <username> <address> <password>");
        System.out.println("help");
        System.out.println("stop");
        System.out.println("addItem <itemName> <categoryName> <unitPrice> <weight> <initialStock>");
        System.out.println("setCategoryDiscount <categoryName> <discountPercent>");
        System.out.println("startCheckout <customerUsername>");
        System.out.println("scanItem <itemName> <quantity>");
        System.out.println("computeBill");
        System.out.println("subscribeToPlan <planName>");
        System.out.println("simulatePayment <SUCCESS|INSUFFICIENT_FUNDS|PIN_WRONG|AUTH_DENIED>");
        System.out.println("pay <cardNumber> <pin>");
        System.out.println("showRevenue");
        System.out.println("showInventory");
        System.out.println("showSlots");
        System.out.println("requestDelivery <address> <slotId>");
        System.out.println("runTest <testScenario-file>");
        System.out.println("restock <itemName> <quantity>");
    }
}