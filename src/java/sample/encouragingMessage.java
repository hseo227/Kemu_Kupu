package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class encouragingMessage {
    ArrayList<String> messageList = new ArrayList<String>();  // all the messages in the file

    public encouragingMessage(String result) {
        // get all the messages in the file
        String fileName = "messages/" + result;

        // First, get all the words in the file
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));
            String line;

            // go through all the lines in the file and add into the list
            while ((line = readFile.readLine()) != null) {
            	messageList.add(line);
            }
            readFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getEncourageMsg() {
        // get random message
        Random rand = new Random();
        return messageList.get(rand.nextInt(messageList.size()));
    }

}
