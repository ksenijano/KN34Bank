package KN34Bank;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to KN34 Bank!");
        System.out.println("______________________");
        System.out.println("Open Bank Account? Y/N");

        char openAccount = 0;
        while (openAccount != 'y' || openAccount != 'n') {
            Scanner scanner = new Scanner(System.in);
            openAccount = scanner.next().toLowerCase().charAt(0);
            if (openAccount == 'y') {
                DataBaseKN myData = new DataBaseKN(); myData.userRegistration();
                break;
            } else if (openAccount == 'n') {
                System.out.println("Existing user? Would you like to log in? Y/N");
                char logIn = scanner.next().toLowerCase().charAt(0);
                if (logIn == 'y') {
                    DataBaseKN myUserLogin = new DataBaseKN(); myUserLogin.userLogIn();
                    break;
                } else if (logIn == 'n') {
                    System.out.println("GoodBye! Maybe, next time!");
                    break;
                }
            }
            System.out.println("Please enter one of two options: Y or N");
        }
    }
}


