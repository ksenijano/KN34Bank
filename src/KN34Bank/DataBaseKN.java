package KN34Bank;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataBaseKN {

    final String dbURL = "jdbc:mysql://localhost:3306/KN34";
    final String user = "root";
    final String password = "1234";
    private String userName;
    private String pswrd = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    User existingUser = new User();


public void userRegistration () {
    Scanner scanner = new Scanner(System.in);
    try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {
//            System.out.println("Connected");
//USERNAME
    boolean matches = false;
        while (!matches) {
            System.out.println("Please enter your username");
            System.out.println("*4 letter and 2 digits*");
            userName = scanner.nextLine();
            existingUser.setUserName(userName);
            Pattern patternUser = Pattern.compile("[a-zA-Z]{4}[0-9]{2}");
            Matcher matcher = patternUser.matcher(userName);
            matches = matcher.matches();
//TODO: ADD check against SQL
            if (matches) {
                System.out.println("Proceed with the next step");
                System.out.println("______________________");
            } else {
                System.out.println("Try another username. This one already exists.");
            }
        }
//PASSWORD
        matches = false;
        while (!matches) {
            System.out.println("Please enter your password");
            System.out.println("*7 letters and 1 digit*");
            pswrd = scanner.nextLine();
            Pattern patternPass = Pattern.compile("[a-zA-Z]{7}[0-9]");
            Matcher matcher = patternPass.matcher(pswrd);
            matches = matcher.matches();
            if (matches) {
                System.out.println("Proceed with the next step");
                System.out.println("______________________");
            } else {
                System.out.println("Try another password");
            }
        }
//FIRSTNAME
        matches = false;
        while (!matches) {
            System.out.println("Please enter your firstname");
            firstName = scanner.nextLine().toUpperCase().trim();
            Pattern patternFirstName = Pattern.compile("[a-zA-Z]{1,15}");
            Matcher matcher = patternFirstName.matcher(firstName);
            matches = matcher.matches();
            if (matches) {
                System.out.println("Proceed with the next step");
                System.out.println("______________________");
            } else {
                System.out.println("Try another First Name");
            }
        }
//LASTNAME
        matches = false;
        while (!matches) {
            System.out.println("Please enter your lastname");
            lastName = scanner.nextLine().toUpperCase().trim();
            Pattern patternLastName = Pattern.compile("[a-zA-z]{1,15}");
            Matcher matcher = patternLastName.matcher(lastName);
            matches = matcher.matches();
            if (matches) {
                System.out.println("Proceed with the next step");
                System.out.println("______________________");
            } else {
                System.out.println("Try another Last Name");
            }
        }
//EMAIL
        matches = false;
        while (!matches) {
            System.out.println("Please enter your e-mail");
            email = scanner.nextLine();
            Pattern patternEmail = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
            Matcher matcher = patternEmail.matcher(email);
            matches = matcher.matches();
            if (matches) {
                System.out.println("Thank you!");
                System.out.println("______________________");
            } else {
                System.out.println("Try another email");
            }
        }
        insertData(conn, userName, pswrd, firstName, lastName, email);
        insertAccount(conn, userName);
        userMenu();
        } catch (SQLException e) {
            System.out.println("No connection " + e);
        }
    }


    public void userLogIn() {
        String userName;
        String pswrd;
        boolean userExistsInDB = false;

        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {
//            System.out.println("Connected");
            do {
                System.out.println("Please enter your username");
                userName = scanner.nextLine().trim();
                existingUser.setUserName(userName);
                System.out.println("Please enter your password");
                pswrd = scanner.nextLine().trim();
            } while (checkUserNameAndPass(conn, userName, pswrd)!= 1);
            userMenu();
        } catch (SQLException e) {
            System.out.println("No connection " + e);
        }
    }


    public static void insertData (Connection conn, String username, String password, String firstname, String lastname, String email) throws SQLException {

        String sql = "INSERT INTO Customers (username, password, firstname, lastname, emailaddress) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, firstname);
        preparedStatement.setString(4, lastname);
        preparedStatement.setString(5, email);
        int rowInserted = preparedStatement.executeUpdate();
        if (rowInserted > 0) {
            System.out.println("Your profile was created successfully");
            System.out.println("______________________");
        } else {
            System.out.println("Something went wrong");
        }
    }


    public static void insertAccount (Connection conn, String username) throws SQLException {

        String sql = "SELECT * FROM Customers WHERE username = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int userID = resultSet.getInt(1);

        String accountSql = "INSERT INTO Accounts (customerid, balance, currency, relatedusername) VALUES (?, 0.00, ?, ?)";
        preparedStatement = conn.prepareStatement(accountSql);
        preparedStatement.setInt(1, userID);
        preparedStatement.setString(2, "EUR");
        preparedStatement.setString(3, username);
        int rowInserted = preparedStatement.executeUpdate();
        if (rowInserted > 0) {
            System.out.println("Your account is opened");
            System.out.println("______________________");
        } else {
            System.out.println("Something went wrong");
        }
    }


    public static int checkUserNameAndPass (Connection conn, String username, String password) throws SQLException {

    String sql = "SELECT count(*) FROM Customers WHERE username = ? AND password = ?";
    PreparedStatement preparedStatement = conn.prepareStatement(sql);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, password);
    ResultSet resultSet = preparedStatement.executeQuery();
    int count = 0;
    if (resultSet.next()) {
        count = resultSet.getInt(1);
        if (count == 1) {
            System.out.println(username + ", welcome to KN34 Bank!");
            System.out.println("______________________\n");
        } else if (count != 1) {
            System.out.println("Login or password were entered incorrectly. Please try again");
        }
    }
    return count;
}


    public void myProfile (Connection conn, String username) throws SQLException {

        String sql = "SELECT * FROM Customers WHERE username = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String userName = resultSet.getString(2);
        String firstName = resultSet.getString(5);
        String lastName = resultSet.getString(4);
        String email = resultSet.getString(6);
        String output = "User information: \n\t User Name: %s \n\t First Name: %s \n\t Last Name: %s \n\t Email: %s";
        System.out.println("______________________");
        System.out.println(String.format(output, userName, firstName, lastName, email));
        System.out.println("______________________");
        userMenu();
        }


    public void myAccounts (Connection conn, String username) throws SQLException {

        String sql = "SELECT * FROM Accounts WHERE relatedusername = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        BigDecimal balance = resultSet.getBigDecimal(3);
        String currency = resultSet.getString(4);
        System.out.println("______________________");
        System.out.println("Your balance: "+balance + " "+currency);
        System.out.println("______________________");
        userMenu();
    }


    public void userMenu () {

        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {
//            System.out.println("Connected");
            int selectedCommand = 0;
            do {
                System.out.println("Please select from menu\n");
                System.out.println("1 - Transfer money (will be available soon :) )");
                System.out.println("2 - My accounts");
                System.out.println("3 - Transactions history (will be available soon :) )");
                System.out.println("4 - My profile");
                System.out.println("5 - Logout");
                selectedCommand = scanner.nextInt();
                if (selectedCommand == 1) {
                    System.out.println("Will be available soon :)! Stay with us!\n"); //!temporary solution
                    userMenu();
                } else if (selectedCommand == 2) {
                    myAccounts(conn, existingUser.getUserName());
                } else if (selectedCommand == 3) {
                    System.out.println("Will be available soon :)! Stay with us!\n"); //!temporary solution
                    userMenu();
                } else if (selectedCommand == 4) {
                      myProfile(conn, existingUser.getUserName());
                } else if (selectedCommand == 5) {
                    System.out.println("Session was completed successfully");
                    System.out.println("Thank you and see you next time! ;] ");
                } else {
                    System.out.println("Wrong command!");
                }
            } while (selectedCommand < 1 || selectedCommand > 5);

        } catch (SQLException e) {
            System.out.println("No connection " + e);
        }
    }
}
