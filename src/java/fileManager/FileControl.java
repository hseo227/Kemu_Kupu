package fileManager;

import java.io.*;
import java.util.ArrayList;

/**
 * This class contains all the necessary controls of the file.
 */
public class FileControl {

    /**
     * Read and get the contents the file
     * @param fileName The file that it is going to read
     * @return ArrayList of contents
     */
    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> listOfItems = new ArrayList<>();

        try {
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));
            String line;

            // go through all the lines in the file and add into the list
            while ((line = readFile.readLine()) != null) {
                listOfItems.add(line);
            }
            readFile.close();

        } catch (IOException e) {
            System.err.println("Failed to read " + fileName);
        }

        return listOfItems;
    }

    /**
     * Overwrite the contents of the file
     * @param fileName The file that it is going to write
     * @param listOfItems The contents that it is to store
     */
    public static void writeFile(String fileName, ArrayList<String> listOfItems) {
        try {
            PrintWriter writeFile = new PrintWriter(new FileWriter(fileName));

            for (String i : listOfItems) {
                writeFile.println(i);
            }
            writeFile.close();

        } catch (IOException e) {
            System.err.println("Failed to write into " + fileName);
        }
    }
}
