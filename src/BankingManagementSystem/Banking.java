package BankingManagementSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Banking extends Application {

    private static final String IMAGE_URL = "file:///C:/Users/HP/OneDrive/Desktop/projects/Bank Management System/atm2.png";
    private static final String username = "saisowjanya";
    private static final String password = "Sowjanya@162";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Load the image
        Image image = new Image(IMAGE_URL);

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);

        // Create a layout to hold the image
        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        // Create the scene
        Scene scene = new Scene(root, image.getWidth(), image.getHeight());

        // Set the scene to the stage and show it
        primaryStage.setScene(scene);
        primaryStage.setTitle("Banking System");
        primaryStage.show();

        // Database connection and banking management system
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;

            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1, Register");
                System.out.println("2, Login");
                System.out.println("3, Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");
                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2.Exit");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1.Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
