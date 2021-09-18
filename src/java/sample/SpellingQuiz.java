package sample;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


enum Mode {
    newSpellingQuiz, reviewMistakes
}

enum QuizState {
    ready, running, finished, noFailed
}

enum Result {
    mastered, faulted, failed
}

public class SpellingQuiz extends Service<Void> {
    private int numQuestions, currentIndex;
    private String currentWord, mainLabelText, promptLabelText, userInput;
    private ArrayList<String> words = new ArrayList<String>();  // words that will be tested on
    private ArrayList<String> wordsList;  // all the words from the file
    private static final HashMap<String, String> FILES = new HashMap<String, String>();  // stores the files locations
    private static final ArrayList<String> FILE_NAME = new ArrayList<String>();  // stores the files names, the main 3
    private static Mode currentMode;
    private QuizState currentQuizState;
    private Result currentResult;
    private static String selectedTopic;


    // this method will only run once and will run at the start of the program
    // set up all the statistics files
    public static void initialise() {
        // setting up the files names
        FILE_NAME.add("mastered_file");
        FILE_NAME.add("faulted_file");
        FILE_NAME.add("failed_file");

        // setting up the files locations
        FILES.put(FILE_NAME.get(0), "./.statistics/mastered");
        FILES.put(FILE_NAME.get(1), "./.statistics/faulted");
        FILES.put(FILE_NAME.get(2), "./.statistics/failed");
        FILES.put("wordHistory_file", "./.statistics/word_history");

        // create the files and directory if they don't exist yet
        String tempFileName = FILES.get("wordHistory_file");
        String dirName = tempFileName.substring(0, tempFileName.lastIndexOf("/"));

        // create the directory first
        File file = new File(dirName);
        file.mkdirs();

        // and then create the file(s) that are missing
        for (String fileName : FILES.values()) {
            if (!fileName.equals(FILES.get("wordList_file"))) {  // other than word list file
                try {
                    file = new File(fileName);
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Constructor
    public SpellingQuiz() {
        // get all the words in the files
        // if new spelling quiz, get the words from "popular"
        // else (review mistakes) get the words from "failed list"
        if (currentMode == Mode.newSpellingQuiz) {

            // change here later

            wordsList = getWordsInFile(FILES.get("wordList_file"), false);  // false = no duplicate
        } else {
            wordsList = getWordsInFile(FILES.get("failed_file"), false);  // false = no duplicate
        }

        numQuestions = Math.min(wordsList.size(), 3);  // number of questions in the quiz can only be 1, 2 or 3
        currentIndex = 0;
        currentWord = "";
        mainLabelText = "";
        promptLabelText = "";
        setUserInput("");
        setQuizState(QuizState.ready);
        setResult(Result.mastered);

        // if there is no failed words, change the state
        if (numQuestions == 0) {
            setQuizState(QuizState.noFailed);
        }
    }

    // this method is invoked by the Server thread
    // this method will then call either newQuestion() or checkSpelling() depends on the QuizState
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // if the quiz state is ready for the next question, then generate next question
                if (quizStateEqualsTo(QuizState.ready)) {
                    newQuestion();

                // otherwise, check the spelling
                } else {
                    checkSpelling();
                }

                // Title = main label;  Message = prompt label
                // update the text in mainLabel and promptLabel
                updateTitle(mainLabelText);
                updateMessage(promptLabelText);
                return null;
            }
        };
    }

    // this function generate a new word and then ask the user
    private void newQuestion() {
        if (currentIndex == numQuestions) {  // the quiz is finished
            setQuizState(QuizState.finished);
            mainLabelText = "Finished";
            promptLabelText = "";
            return;
        }

        setQuizState(QuizState.running);  // now set the state to running
        setResult(Result.mastered);  // set original result to Mastered
        currentIndex++;
        Random rand = new Random();
        String randWord;

        // get a random word from the word list, that has all the words from the file
        do {
            randWord = wordsList.get(rand.nextInt(wordsList.size()));
        } while (words.contains(randWord));  // loop until get a different word

        currentWord = randWord;
        words.add(randWord);

        // set the labels' messages and also speak out the message
        mainLabelText = "Spell word " + currentIndex + " of " + numQuestions + ":";
        promptLabelText = "";
        speak("Please spell " + currentWord);
    }

    // this function check the spelling (input) and then set up a range of stuff
    private void checkSpelling() {
        // if statement for each result after checking the spelling (input)
        if (equalStrings(getUserInput(), currentWord)) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.ready);  // set the state to ready for the next question
            modifyFile();

            // setting up the labels' text and speak out the message
            mainLabelText = "Correct";
            promptLabelText = "Press 'Enter' again to continue";
            speak("Correct");

        } else if (resultEqualsTo(Result.mastered)) {  // still 1st attempt, but incorrect
            setResult(Result.faulted);

            // setting up the labels' text and speak out the message
            mainLabelText = "Incorrect, try once more:";
            promptLabelText = "Wrong spelling: " + getUserInput().trim();
            speak("Incorrect, try once more. " + currentWord + " " + currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.failed);
            setQuizState(QuizState.ready);  // set the state to ready for the next question
            modifyFile();

            // setting up the labels' text and speak out the message
            mainLabelText = "Incorrect";
            promptLabelText = "Press 'Enter' again to continue";
            speak("Incorrect");
        }
    }

    // this function modify the content of the file (add, remove or move)
    private void modifyFile() {
        try {
            // open up the specific file respectively (mastered, faulted, failed)       true = append
            PrintWriter writeFile = new PrintWriter(new FileWriter(FILES.get(FILE_NAME.get(getResult().ordinal())), true));

            if (modeEqualsTo(Mode.newSpellingQuiz)) {  // add the word into the specific file list
                writeFile.println(currentWord);

            } else if (modeEqualsTo(Mode.reviewMistakes)) {
                if (resultEqualsTo(Result.mastered) || resultEqualsTo(Result.faulted)) {  // Mastered and Faulted

                    // remove the word from the list and from the file
                    wordsList.remove(currentWord);
                    removeWordFromFile(currentWord, FILES.get("failed_file"));

                    if (resultEqualsTo(Result.faulted)) {  // only faulted
                        writeFile.println(currentWord);
                    }
                }
            }

            writeFile.close();

            // now write the word and result into the history file, which will be used for show statistics
            PrintWriter writeHistoryFile = new PrintWriter(new FileWriter(FILES.get("wordHistory_file"), true));
            writeHistoryFile.println(currentWord + " " + getResult().ordinal());
            writeHistoryFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this function get all the words in the file, allow adding duplicate or not duplicate word
    public static ArrayList<String> getWordsInFile(String fileName, boolean duplicate) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));
            String line;

            // go through all the lines in the file
            while ((line = readFile.readLine()) != null) {
                if (duplicate) {  // add duplicate
                    list.add(line);
                } else if (!list.contains(line)) {  // ignore duplicate (that is the word is already in the list)
                    list.add(line);
                }
            }
            readFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // this function remove the specific word from the file
    private void removeWordFromFile(String word, String fileName) throws IOException {
        // get all the words in the file, allow duplicate
        ArrayList<String> list = getWordsInFile(fileName, true);

        // overwrite the file without the specific word
        PrintWriter writeFile = new PrintWriter(new FileWriter(fileName));

        for (String i : list) {
            if (!i.equals(word)) {
                writeFile.println(i);
            }
        }
        writeFile.close();
    }

    // this function compares both string with ignore cases and spaces outside the words
    private boolean equalStrings(String a, String b) {
        return a.trim().equalsIgnoreCase(b.trim());
    }

    // this function will speak out the message using bash script
    private void speak(String message) {
        try {
            String command = "echo " + message + " | festival --tts";
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mode's getter, setter and equals to
    public static void setMode(Mode newMode) {
        currentMode = newMode;
    }

    private static Mode getMode() {
        return currentMode;
    }

    public static boolean modeEqualsTo(Mode mode) {
        return getMode() == mode;
    }

    // QuizState's getter, setter and equals to
    private void setQuizState(QuizState newQuizState) {
        currentQuizState = newQuizState;
    }

    private QuizState getQuizState() {
        return currentQuizState;
    }

    public boolean quizStateEqualsTo(QuizState quizState) {
        return getQuizState() == quizState;
    }

    // Result's getter, setter and equals to
    private void setResult(Result newResult) {
        currentResult = newResult;
    }

    private Result getResult() {
        return currentResult;
    }

    public boolean resultEqualsTo(Result result) {
        return getResult() == result;
    }

    // userInput's getter and setter
    public void setUserInput(String newUserInput) {
        userInput = newUserInput;
    }

    private String getUserInput() {
        return userInput;
    }

    // this function get all the files' locations
    public static HashMap<String, String> getFILES() {
        return FILES;
    }

    // selectedTopic's setter
    public static void setTopic(String topic) {
        selectedTopic = topic;
    }
}
