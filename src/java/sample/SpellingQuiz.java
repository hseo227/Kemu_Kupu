package sample;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


enum QuizState {
    ready, running, finished, noFailed
}

enum Result {
    mastered, faulted, failed
}

public class SpellingQuiz extends Service<Void> {
    private final int MAXNUMOFQUESTION = 5;


    private int numQuestions, currentIndex;
    private String currentWord, mainLabelText, promptLabelText, userInput;
    private ArrayList<String> words = new ArrayList<String>();  // words that will be tested on
    private ArrayList<String> wordsList;  // all the words from the file
    private QuizState currentQuizState;
    private Result currentResult;
    private static String selectedTopic;


    // Constructor
    public SpellingQuiz() {
        // get all the words in the files
        String wordListLocation = "words/" + selectedTopic;
        wordsList = getWordsInFile(wordListLocation);

        // number of questions in the quiz can only be less than max number of questions
        numQuestions = Math.min(wordsList.size(), MAXNUMOFQUESTION);
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
            mainLabelText = "DISPLAY SCORE";
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

            // setting up the labels' text and speak out the message
            mainLabelText = "Correct";
            promptLabelText = "Press 'Enter' again to continue";
            speak("Correct");

        } else if (resultEqualsTo(Result.mastered)) {  // still 1st attempt, but incorrect
            setResult(Result.faulted);

            // setting up the labels' text and speak out the message
            mainLabelText = "Incorrect, try once more:";
            promptLabelText = "Hint: second letter is '" + currentWord.charAt(1) + "'";
            speak("Incorrect, try once more. " + currentWord + " " + currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.failed);
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = "Incorrect";
            promptLabelText = "Press 'Enter' to attempt next word";
            speak("Incorrect");
        }
    }

    // this function get all the words in the file, allow adding duplicate or not duplicate word
    public static ArrayList<String> getWordsInFile(String fileName) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));
            String line;

            // go through all the lines in the file and add into the list
            while ((line = readFile.readLine()) != null) {
                list.add(line);
            }
            readFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
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

    // this method will speak the word again, only the word
    public void speakAgain() {
        speak(currentWord);
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

    // selectedTopic's setter
    public static void setTopic(String topic) {
        selectedTopic = topic;
    }
}
