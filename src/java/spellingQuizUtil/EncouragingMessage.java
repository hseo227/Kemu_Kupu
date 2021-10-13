package spellingQuizUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class handles all the encouraging messages
 */
public class EncouragingMessage {
    ArrayList<String> messageList = new ArrayList<>();  // all the messages in the file


    /**
     * Setting up the encouraging message list
     * @param result Encouraging messages for that specific result
     */
    public EncouragingMessage(String result) {
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

    /**
     * Get a random message in the message list
     * @return A random encouraging message
     */
    public String getEncourageMsg() {
        // get random message
        Random rand = new Random();
        return messageList.get(rand.nextInt(messageList.size()));
    }

}
