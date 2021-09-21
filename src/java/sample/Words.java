package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Words {
    private int currentIndex;
    private String currentWord;  // the word that is currently testing
    private final ArrayList<String> wordsList;  // words that will be tested on


    public Words(String selectedTopic, int numOfQuestions) {
        currentIndex = 0;
        currentWord = "";
        
        // get specific number of random words in the file
        String wordListLocation = "words/" + selectedTopic;
        wordsList = getRandomWordsInFile(wordListLocation, numOfQuestions);
    }

    // this function get specific number of random words in the file
    private ArrayList<String> getRandomWordsInFile(String fileName, int numOfWords) {
        ArrayList<String> allWordsInFile = new ArrayList<String>();  // all the words in the file
        ArrayList<String> wordsList = new ArrayList<String>();  // specific number of random words from the file

        // First, get all the words in the file
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));
            String line;

            // go through all the lines in the file and add into the list
            while ((line = readFile.readLine()) != null) {
                allWordsInFile.add(line);
            }
            readFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Finally, get random words
        Random rand = new Random();
        String randWord;

        for (int i = 0; i < numOfWords; i++) {
            // get a random word from the list, that has all the words from the file
            do {
                randWord = allWordsInFile.get(rand.nextInt(allWordsInFile.size()));
            } while (wordsList.contains(randWord));  // loop until get a different word

            wordsList.add(randWord);
        }

        return wordsList;
    }

    // setting up for the next word to be tested on, and then return that word
    public String nextWord() {
        currentWord = wordsList.get(currentIndex++);
        return currentWord;
    }

    // this function compares both string with ignore cases and spaces outside the words
    public boolean checkUserSpelling(String userSpelling) {
        return userSpelling.trim().equalsIgnoreCase(currentWord.trim());
    }
}
