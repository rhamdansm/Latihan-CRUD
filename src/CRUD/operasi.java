package CRUD;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class operasi {

    public static void updateData() throws IOException {

        // Get Original Database
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // Create Temporary Database
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Show Data
        System.out.println("Book List");
        showData();

        // user input to choose the data that want to delete
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nChoose the data that you want to update : ");
        int updateNum = terminalInput.nextInt();

        // looping to each row and determine to skip or delete
        int entryCount = 0;
        boolean isFound = false;

        String data = bufferedInput.readLine();

        while (data != null) {
            entryCount++;
            StringTokenizer st = new StringTokenizer(data, ",");

            //show data that want to update
            if (updateNum == entryCount) {
                isFound = true;

                System.out.println("\nthe data you want to update is");
                System.out.println("------------------------------------");
                System.out.println("Reference\t: " + st.nextToken());
                System.out.println("Year\t\t: " + st.nextToken());
                System.out.println("Author\t\t: " + st.nextToken());
                System.out.println("Publisher\t: " + st.nextToken());
                System.out.println("Title\t\t: " + st.nextToken());

                // Check where the data want to be updated
                String[] fieldData = {"year", "author", "publisher", "title"};
                String[] tempData = new String[4];

                st = new StringTokenizer(data, ",");
                String originalData = st.nextToken();
                int i = 0;

                for (String field:fieldData){
                    boolean isUpdate = utility.getYesNo("Do you want to change " + field + " [y/n]?");
                    originalData = st.nextToken();

                    if (isUpdate) {
                        if (field.equalsIgnoreCase("year")) {
                            System.out.print("Enter new year (YYYY): ");
                            tempData[i] = utility.checkYearFormat();
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("Enter new " + field + " : ");
                            tempData[i] = terminalInput.nextLine();
                        }
                    } else {
                        tempData[i] = originalData;
                    }
                    i ++;
                }

                st = new StringTokenizer(data, ",");
                st.nextToken();

                // Show Updated data
                System.out.println("\nthe data you want to update is");
                System.out.println("------------------------------------");
                System.out.println("Year\t\t: " + st.nextToken() +  " --> " + tempData[0]);
                System.out.println("Author\t\t: " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("Publisher\t: " + st.nextToken() + " --> " + tempData[2]);
                System.out.println("Title\t\t: " + st.nextToken() + " --> " + tempData[3]);

                // confirmation to update
                boolean isUpdate = utility.getYesNo("Are you sure want to change the data ?");

                if(isUpdate) {

                    // Check whether the data already exist or not
                    boolean isExist = utility.bookChecking(tempData, false, true);

                    if (isExist) {
                        System.err.println("The data has been exist in database");
                        bufferedOutput.write(data);
                    } else {
                        // format a new data to database
                        String year = tempData[0];
                        String author = tempData[1];
                        String publisher = tempData[2];
                        String title = tempData[3];

                        //create primary key
                        int noEntry = CRUD.utility.getEntry(tempData[1], tempData[0]) + 1;
                        String newAuthor = author.replaceAll("\\s", "");
                        String primaryKey = newAuthor + "_" + year + "_" + noEntry;

                        // write the new data to database
                        bufferedOutput.write(primaryKey + "," + year + "," + author + "," + publisher + "," + title);
                        System.out.println("Data has been updated");
                    }
                } else {
                    // copy the first data
                    bufferedOutput.write(data);
                }
                bufferedOutput.newLine();

            } else {
                // copy data to tempDB database
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        // Write data to tempDB
        bufferedOutput.flush();

        // close txt file
        bufferedInput.close();
        bufferedOutput.close();
        fileInput.close();
        fileOutput.close();
        System.gc();

        // delete old database and rename tempDB.txt to database.txt
        database.delete();
        tempDB.renameTo(database);
    }

    public static void deleteData() throws IOException {

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

                isDelete = utility.getYesNo("Are you sure you want to delete this data? ");
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
    public static void showData() throws IOException {
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

    public static void findBook() throws  IOException {

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
        utility.bookChecking(keywords, true, false);

    }

    public static void addNewBook() throws IOException {

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
        year = utility.checkYearFormat();

        String[] keywords = {year+","+author+","+publisher+","+title};

        boolean isExist = utility.bookChecking(keywords, false, true);

        if (!isExist) {
            String newAuthor = author.replaceAll("\\s+", "");
            int entry = utility.getEntry(author, year) + 1;
            String primaryKey = newAuthor + "_" + year + "_" + entry;

            System.out.println("\nThe data you entered");
            System.out.println("-------------------------------");
            System.out.println("Author\t\t: " + author);
            System.out.println("Title\t\t: " + title);
            System.out.println("Publisher\t: " + publisher);
            System.out.println("Year\t\t: " + year);

            boolean isTrue = utility.getYesNo("is the data you entered is correct?");

            if(isTrue) {
                bufferOutput.write(primaryKey + "," + year + "," + author + "," + publisher + "," + title);
                bufferOutput.newLine();
                bufferOutput.flush();
                System.out.println("The book has been entered into the database");
            }

        } else {
            System.out.println("The book is already in the database");
            utility.bookChecking(keywords, true, true);
        }

        bufferOutput.close();
    }
}
