package spellingQuizUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Words {
    private int currentIndex;
    private String currentWord;  // the word that is currently testing
    private final String[] wordsList;  // words that will be tested on
    private static String selectedTopic;


    public Words(int numOfQuestions) {
        currentIndex = 0;
        currentWord = "";
        
        // get specific number of random words in the file
        String wordListLocation = "words/" + selectedTopic;
        wordsList = getRandomWordsInFile(wordListLocation, numOfQuestions);
    }

    // this function get specific number of random words in the file
    private String[] getRandomWordsInFile(String fileName, int numOfWords) {
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

        return wordsList.toArray(new String[0]);
    }

    // setting up for the next word to be tested on, and then return that word
    public String nextWord() {
        currentWord = wordsList[currentIndex++];
        return currentWord;
    }

    // this function compares both string with ignore cases and spaces outside the words
    public boolean checkUserSpelling(String userSpelling) {
        return currentWord.trim().equalsIgnoreCase(userSpelling.trim());
    }

    // selectedTopic's setter
    public static void setTopic(String topic) {
        selectedTopic = topic;
    }

    // this function return the number of letters of the current word, excluding space and comma
    public int getNumOfLettersOfWord() {
        int count = 0;

        for (char i : currentWord.toCharArray()) {
            if (Character.isLetter(i)) {
                count++;
            }
        }
        return count;
    }

    // this function return the the number of words (lines) in the files
    public static int getNumOfWordsInWordsList() {
        String fileName = "words/" + selectedTopic;
        int count = 0;

        try {
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));

            // count the lines
            while ((readFile.readLine()) != null) {
                count++;
            }
            readFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

    // hint for the current word
    public String getHint(Hint hintType) {
        ArrayList<Integer> indexesOfHints = new ArrayList<>();
        StringBuilder hint = new StringBuilder();

        // for practise module, the hint is to add random number of letters into the word
        if (hintType == Hint.PRACTISE_M_HINT) {
            // number of letters hints depends on the size of the current word, excluding space and comma
            int numOfLettersHints = (int) Math.ceil((double) getNumOfLettersOfWord() / 4);

            Random rand = new Random();
            int randIndex;

            // get the random index of letter in the word (index of the hint)
            for (int i = 0; i < numOfLettersHints; i++) {
                // loop until get a different index
                do {
                    randIndex = rand.nextInt(currentWord.length());
                } while (indexesOfHints.contains(randIndex) || !Character.isLetter(currentWord.charAt(randIndex)));

                indexesOfHints.add(randIndex);
            }

        // for games module, the hint is the second letter of the word
        } else if (hintType == Hint.GAMES_M_HINT) {
            indexesOfHints.add(1);
        }

        // now build the hint
        for (int i = 0; i < currentWord.length(); i++) {
            if (indexesOfHints.contains(i) || !Character.isLetter(currentWord.charAt(i))) {
                hint.append(currentWord.charAt(i));  // this is the letter hint
            } else {
                hint.append("_");  // blank, let the user to guess it
            }
            hint.append(" ");
        }

        return hint.toString();
    }
}
