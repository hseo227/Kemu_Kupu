package fileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
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
}
