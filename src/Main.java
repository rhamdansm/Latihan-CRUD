// import java library
import java.io.*;
import java.util.*;

// import CRUD library
import CRUD.operasi;
import CRUD.utility;


public class Main {
    public static void main(String[] args) throws IOException {

        // Create terminal input for main menu
        Scanner terminalInput = new Scanner(System.in);
        String userChoice;
        boolean isContinue = true;

        while (isContinue) {

            utility.clearScreen(); // Clear Section per loop

            // Create an options on the main menu
            System.out.println("Library Database");
            System.out.println("1.\tSee All Book");
            System.out.println("2.\tFind Book");
            System.out.println("3.\tAdd new book");
            System.out.println("4.\tUpdate Book Data");
            System.out.println("5.\tDelete Book Data");

            System.out.print("\n\nYour Choice: ");
            userChoice = terminalInput.next();

            // create logic for the selected menu
            switch (userChoice) {
                case "1": // to see all books
                    System.out.println("=================");
                    System.out.println("List Of All Books");
                    System.out.println("=================");
                    operasi.showData();
                    break;
                case "2": // to find book
                    System.out.println("=========");
                    System.out.println("Find Book");
                    System.out.println("=========");
                    operasi.findBook();
                    break;
                case "3": // to add new book
                    System.out.println("============");
                    System.out.println("Add New Book");
                    System.out.println("============");
                    operasi.addNewBook();
                    operasi.showData();
                    break;
                case "4": // to Update book data
                    System.out.println("==============");
                    System.out.println("Update Book Data");
                    System.out.println("==============");
                    operasi.updateData();
                    break;
                case "5": // to delete book data
                    System.out.println("================");
                    System.out.println("Delete Book Data");
                    System.out.println("================");
                    operasi.deleteData();
                    break;
                default: // for error option
                    System.err.println("you did not enter your input or your choice is not in the menu");
            }

            isContinue = utility.getYesNo("Do you want to continue [y/n] ?");
            if (!isContinue) {
                System.out.println("Sayonara");
            }
        }
    }
}
