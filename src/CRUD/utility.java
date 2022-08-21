package CRUD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class utility {
    static boolean bookChecking(String[] keywords, boolean isDisplay, boolean isMerge) throws IOException {
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

    protected static String checkYearFormat() throws IOException {
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

    public static int getEntry(String author, String year) throws IOException{
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

    // Function to get option yes or not
    public static boolean getYesNo(String message) {
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
    public static void clearScreen() {
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
