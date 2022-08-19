import jdk.jfr.FlightRecorderListener;

import java.io.*;
import java.time.Year;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        // Create terminal input for main menu
        Scanner terminalInput = new Scanner(System.in);
        String userChoice;
        boolean isContinue = true;

        while (isContinue) {

            clearScreen(); // Clear Section per loop

            // Create an options on the main menu
            System.out.println("Library Database");
            System.out.println("1.\tSee All Book");
            System.out.println("2.\tFind Book");
            System.out.println("3.\tAdd new book");
            System.out.println("4.\tEdit Book Data");
            System.out.println("5.\tDelete Book Data");

            System.out.print("\n\nYour Choice: ");
            userChoice = terminalInput.next();

            // create logic for the selected menu
            switch (userChoice) {
                case "1": // to see all books
                    System.out.println("=================");
                    System.out.println("List Of All Books");
                    System.out.println("=================");
                    showData();
                    break;
                case "2": // to find book
                    System.out.println("=========");
                    System.out.println("Find Book");
                    System.out.println("=========");
                    findBook();
                    break;
                case "3": // to add new book
                    System.out.println("============");
                    System.out.println("Add New Book");
                    System.out.println("============");
                    addNewBook();
                    showData();
                    break;
                case "4": // to edit book data
                    System.out.println("==============");
                    System.out.println("Edit Book Data");
                    System.out.println("==============");
                    break;
                case "5": // to delete book data
                    System.out.println("================");
                    System.out.println("Delete Book Data");
                    System.out.println("================");
                    deleteData();
                    break;
                default: // for error option
                    System.err.println("you did not enter your input or your choice is not in the menu");
            }

            isContinue = getYesNo("Do you want to continue [y/n] ?");
            if (!isContinue) {
                System.out.println("Sayonara");
            }
        }
    }

    // Function to show data
    private static void showData() throws IOException {
        // data variable initiation
        FileReader fileinput;
        BufferedReader bufferInput;

        // check whether the database exists or not
        try {
            fileinput = new FileReader("database.txt"); // read file database
            bufferInput = new BufferedReader(fileinput); // create buffer input from database
        } catch (Exception e) {
            System.err.println("database not found");
            System.err.println("Please add data first");
            return;
        }

        String data = bufferInput.readLine(); // read database per line
        int noData = 0;

        // Create a column name
        System.out.println("\n| No |\tYear |\tAuthor                   |\tpublisher                |\tBook title");
        System.out.println("--------------------------------------------------------------------------------------");

        // looping for database per line
        while (data != null) {
            noData ++;

            StringTokenizer stringTOken = new StringTokenizer(data, ","); // create token with delimeter comma ","

            stringTOken.nextToken();
            System.out.printf("| %2d ", noData);
            System.out.printf("|\t%4s  ", stringTOken.nextToken());
            System.out.printf("|\t%-20s     ", stringTOken.nextToken());
            System.out.printf("|\t%-20s     ", stringTOken.nextToken());
            System.out.printf("|\t%-20s     ", stringTOken.nextToken());
            System.out.print("\n");

            data = bufferInput.readLine();
        }
        System.out.println("--------------------------------------------------------------------------------------");

    }

    private static void findBook() throws  IOException {

        // check whether the database exists or not
        try {
            File file = new File("database.txt"); // read file database
        } catch (Exception e) {
            System.err.println("database not found");
            System.err.println("Please add data first");
            return;
        }

        // Get keyword
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Enter keyword : ");
        String findWord = terminalInput.nextLine();
        System.out.println(findWord);

        String[] keywords = findWord.split("\\s+");

        // Check keyword in Database
        bookChecking(keywords, true, false);

    }

    private static void addNewBook() throws IOException {

        FileWriter fileOutput = new FileWriter("database.txt", true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        Scanner terminalInput = new Scanner(System.in);
        String author, title, publisher, year;

        System.out.println("Please Input The Data");
        System.out.print("Author : ");
        author = terminalInput.nextLine();
        System.out.print("title : ");
        title = terminalInput.nextLine();
        System.out.print("publisher : ");
        publisher = terminalInput.nextLine();
        System.out.print("year : ");
        year = checkYearFormat();

        String[] keywords = {year+","+author+","+publisher+","+title};

        boolean isExist = bookChecking(keywords, false, true);

        if (!isExist) {
            String newAuthor = author.replaceAll("\\s+", "");
            int entry = getEntry(author, year) + 1;
            String primaryKey = newAuthor + "_" + year + "_" + entry;

            System.out.println("\nThe data you entered");
            System.out.println("-------------------------------");
            System.out.println("Author\t\t: " + author);
            System.out.println("Title\t\t: " + title);
            System.out.println("Publisher\t: " + publisher);
            System.out.println("Year\t\t: " + year);

            boolean isTrue = getYesNo("is the data you entered is correct?");

            if(isTrue) {
                bufferOutput.write(primaryKey + "," + year + "," + author + "," + publisher + "," + title);
                bufferOutput.newLine();
                bufferOutput.flush();
                System.out.println("The book has been entered into the database");
            }

        } else {
            System.out.println("The book is already in the database");
            bookChecking(keywords, true, true);
        }

        bufferOutput.close();
    }

    private static boolean bookChecking(String[] keywords, boolean isDisplay, boolean isMerge) throws IOException{
        FileReader fileinput = new FileReader("database.txt"); // read file database
        BufferedReader bufferInput = new BufferedReader(fileinput); // create buffer input from database
        String data = bufferInput.readLine();

        boolean isExist = false;

        List<List<String>> result = new ArrayList<List<String>>();
        for(int i = 0; i < 2; i++)  {
            result.add(new ArrayList<String>());
        }

        while (data != null){
            isExist = true;
            boolean isExistRecomendation = false;


            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
                isExistRecomendation = isExistRecomendation || data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist) {
                result.get(0).add(data);
            } else if (isExistRecomendation) {
                result.get(1).add(data);
            }

            data = bufferInput.readLine();
        }

        if(isDisplay) {
            for(int i = 0; i < 2; i++){
                int dataNumber = 0;

                if(!isMerge) {
                    if (i == 0) {
                        System.out.print("\nthe book you are looking for");
                    } else {
                        System.out.print("\nbook recommendations for you");
                    }
                    System.out.println("\n| No |\tYear |\tAuthor                   |\tpublisher                |\tBook title");
                    System.out.println("--------------------------------------------------------------------------------------");
                }

                for(String book:result.get(i)) {
                    dataNumber ++;
                    StringTokenizer stringToken = new StringTokenizer(book, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d ", dataNumber);
                    System.out.printf("|\t%4s ", stringToken.nextToken());
                    System.out.printf("|\t%-20s     ", stringToken.nextToken());
                    System.out.printf("|\t%-20s     ", stringToken.nextToken());
                    System.out.printf("|\t%s", stringToken.nextToken());
                    System.out.print("\n");
                }
            }
        }

        for(String keyword:keywords){
            isExist = true;
            isExist = isExist && result.get(0).toString().toLowerCase().contains(keyword.toLowerCase());
        }

        return isExist;
    }

    private static String checkYearFormat() throws IOException {
        boolean yearValid = false;
        Scanner terminalInput = new Scanner(System.in);
        String yearInput = terminalInput.nextLine();

        while (!yearValid){
            try {
                Year.parse(yearInput);
                yearValid = true;
            } catch (Exception e) {
                yearValid = false;
                System.err.println("Year format is wrong, Please enter again !");
                System.out.print("year : ");
                yearInput = terminalInput.nextLine();
            }
        }

        return yearInput;
    }

    private static int getEntry(String author, String year) throws IOException{
        FileReader fileinput = new FileReader("database.txt"); // read file database
        BufferedReader bufferInput = new BufferedReader(fileinput); // create buffer input from database

        int entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while(data != null){
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            author = author.replaceAll("\\s+", "");

            if(author.equalsIgnoreCase(dataScanner.next()) && year.equalsIgnoreCase(dataScanner.next())) {
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        } return entry;
    }

    private static void deleteData() throws IOException {

        // Get Original Database
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // Create a temporary database
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Show Data
        System.out.println("Book List");
        showData();

        // user input to choose the data that want to delete
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nChoose the data that you want to delete : ");
        int deleteNum = terminalInput.nextInt();

        // looping to each row and determine to skip or delete
        int entryCount = 0;
        boolean isFound = false;

        String data = bufferedInput.readLine();

        while (data != null) {
            entryCount++;
            boolean isDelete = false;
            StringTokenizer st = new StringTokenizer(data, ",");

            //show data that want to delete
            if (deleteNum == entryCount) {
                isFound = true;

                System.out.println("\nthe data you want to delete is");
                System.out.println("------------------------------------");
                System.out.println("Reference\t: " + st.nextToken());
                System.out.println("Year\t\t: " + st.nextToken());
                System.out.println("Author\t\t: " + st.nextToken());
                System.out.println("Publisher\t: " + st.nextToken());
                System.out.println("Title\t\t: " + st.nextToken());

                isDelete = getYesNo("Are you sure you want to delete this data? ");
            }

            if (isDelete) {
                // Skip moving this row from database to temporaryDB
                System.out.println("Data deleted successfully");
            } else {
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }

            data = bufferedInput.readLine();
        }

        if(!isFound) {
            System.err.println("The book you are looking for does not exist");;
        }

        // Write data to tempDB
        bufferedOutput.flush();

        bufferedInput.close();
        bufferedOutput.close();
        fileInput.close();
        fileOutput.close();
        System.gc();

        database.delete();
        tempDB.renameTo(database);
    }

    // Function to get option yes or not
    private static boolean getYesNo(String message) {
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n" + message);
        String userChoice = terminalInput.next();

        while (!userChoice.equalsIgnoreCase("y") && !userChoice.equalsIgnoreCase("n")) {
            System.err.println("Pliss enter the answer only y or n !");
            System.out.print("\n" + message);
            userChoice = terminalInput.next();
        }

        return userChoice.equalsIgnoreCase("y");

    }

    // Function to clear Section
    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception ex) {
            System.err.println("Cant clear section");
        }
    }
}
