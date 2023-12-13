package main;

import java.util.Scanner;
import user.User;
import utils.Utils;
import region.Region;
import bill.Bill;
import database.Database;

public class Main {
    
    private static User[] users = {
            new User("user123", "pass123", 12, 4,1)
    };
    private static Region haryana = new Region("Haryana", 10.0);
    private static Region uttarPradesh = new Region("Uttar Pradesh", 6.5);
    private static Region delhi = new Region("Delhi", 8.0);
    private static Region rajasthan = new Region("Rajasthan", 7.0);
    
    private static String[] months = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };
    
    private static double[][] monthlyUnitConsumption = new double[users.length][months.length];
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        users = database.allUsers();
        boolean exitRequested = false;

        while (!exitRequested) {
            System.out.println("Welcome to Electricity Billing System.");

            System.out.println("Select an option:");
            System.out.println("1. Create a new user");
            System.out.println("2. Store Data");
            System.out.println("3. Calculate Electricity Bill");
            System.out.println("4. Show Stored Data");
            System.out.println("5. Exit");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    createUser(scanner,database);
                    break;
                case 2:
                    storeData(scanner,database);
                    break;
                case 3:
                    calculateElectricityBill(scanner);
                    break;
                case 4:
                    showStoredData(scanner);
                    break;
                case 5:
                    exitRequested = true;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

    private static void createUser(Scanner scanner,Database database) {
        System.out.print("Enter a new username: ");
        String newUsername = scanner.next();
        System.out.print("Enter a new password: ");
        String newPassword = scanner.next();
        int i = database.addUser(newUsername,newPassword);
        User newUser = new User(newUsername, newPassword, 12, 4,i);
        User[] newUsers = new User[users.length + 1];
        System.arraycopy(users, 0, newUsers, 0, users.length);
        newUsers[users.length] = newUser;
        users = newUsers;

        System.out.println("User registered successfully!");

        backToMainMenu(scanner);
    }

    private static void storeData(Scanner scanner,Database database) {
    System.out.print("Enter your username: ");
    String enteredUsername = scanner.next();
    System.out.print("Enter your password: ");
    String enteredPassword = scanner.next();
    
    User loggedInUser = authenticateUser(enteredUsername, enteredPassword);

    if (loggedInUser != null) {
        try {
            System.out.print("Enter the month (e.g., January, February, etc.): ");
            String month = scanner.next();

            System.out.print("Enter the units consumed for the month: ");
            int unitsConsumed = scanner.nextInt();

            System.out.println("Available regions:");
            showAvailableRegions();
            String regions[]  = {"Haryana","Uttar Pradesh","Delhi","Rajasthan"};
            System.out.print("Enter the number corresponding to your region: ");
            int regionChoice = scanner.nextInt();

            if (regionChoice < 1 || regionChoice > 4) {
                System.out.println("Invalid choice. Exiting...");
                return;
            }

            Region selectedRegion = getRegion(regionChoice);

            loggedInUser.storeData(month, unitsConsumed, selectedRegion);
            
            Bill bill = new Bill(loggedInUser, selectedRegion, unitsConsumed);
            double totalAmount = bill.calculateBillAmount();
            database.addbills(loggedInUser.id,month,regions[regionChoice-1],unitsConsumed,(int)totalAmount);
            Utils.createTextFile(Integer.toString(loggedInUser.id)+month+".txt",Integer.toString(loggedInUser.id)+"\n"+ month+"\n"+ regions[regionChoice-1]+"\n"+ Integer.toString(unitsConsumed)+"\n"+ Integer.toString((int)totalAmount));
            System.out.println("Data stored successfully!");
            System.out.println("Electricity Bill for " + month + ": Rs. " + totalAmount);
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data. Exiting...");
        }
    } else {
        System.out.println("Invalid username or password. Access denied.");
    }

    backToMainMenu(scanner);
    }

    private static void calculateElectricityBill(Scanner scanner) {
        System.out.println("Available regions:");
        showAvailableRegions();

        System.out.print("Enter the number corresponding to your region: ");
        int regionChoice = scanner.nextInt();

        if (regionChoice < 1 || regionChoice > 4) {
            System.out.println("Invalid choice. Exiting...");
            return;
        }

        Region selectedRegion = getRegion(regionChoice);

        System.out.print("Enter the units consumed: ");
        double unitsConsumed = scanner.nextDouble();

        Bill bill = new Bill(null, selectedRegion, unitsConsumed);
        double totalAmount = bill.calculateBillAmount();

        System.out.println("Bill Amount: Rs. " + totalAmount);

        backToMainMenu(scanner);
    }

    private static void showStoredData(Scanner scanner) {
        System.out.print("Enter your username: ");
        String enteredUsername = scanner.next();
        System.out.print("Enter your password: ");
        String enteredPassword = scanner.next();

        User loggedInUser = authenticateUser(enteredUsername, enteredPassword);

        if (loggedInUser != null) {
            loggedInUser.showStoredData();
        } else {
            System.out.println("Invalid username or password. Access denied.");}

        backToMainMenu(scanner);
    }

    private static void backToMainMenu(Scanner scanner) {
        System.out.print("Press Enter to return to the main menu...");
        scanner.nextLine(); 
        scanner.nextLine(); 
    }

 private static User authenticateUser(String username, String password) {
        for (User user : users) {
            if(user!=null){
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }

    private static Region getRegion(int index) {
        switch (index) {
            case 1:
                return haryana;
            case 2:
                return uttarPradesh;
            case 3:
                return delhi;
            case 4:
                return rajasthan;
            default:
                return null;
        }
    }

    private static void showAvailableRegions() {
        System.out.println("1. " + haryana.getName());
        System.out.println("2. " + uttarPradesh.getName());
        System.out.println("3. " + delhi.getName());
        System.out.println("4. " + rajasthan.getName());
    }
}
