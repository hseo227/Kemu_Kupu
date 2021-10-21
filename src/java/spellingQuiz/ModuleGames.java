package spellingQuiz;

import spellingQuizUtil.*;

import static spellingQuizUtil.FestivalSpeech.speak;


/**
 * This class stores only the functionalities of Games Module
 */
public class ModuleGames extends Module {
    private static final int NUMBER_OF_QUESTIONS = 5;

    /**
     * Constructor
     * Start the quiz with number of question 5
     */
    public ModuleGames() {
        super(NUMBER_OF_QUESTIONS);
    }

    /**
     * This function check the spelling (input)
     * And then set up the labels, speak, increase score with respective Result
     * Finally add current word statistics
     */
    public void checkSpelling() {
        int scoreIncreased = 0;

        // first check if the word is skipped
        if (resultEqualsTo(Result.SKIPPED)) {
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Word skipped", "");

        // if statement for each result after checking the spelling (input)
        } else if ( words.checkUserSpelling(userInput) ) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Correct", "");
            scoreIncreased = score.increaseScore(getResult());

        } else if (resultEqualsTo(Result.MASTERED)) {  // still 1st attempt, but incorrect
            setResult(Result.FAULTED);

            // setting up the labels' text and speak out the message
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = words.getHint(Hint.GAMES_M_HINT);
            speak("Incorrect, try once more.", currentWord);
            return;  // so do not add any statistics

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.FAILED);
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Incorrect", "");
        }

        // add current word statistics
        statistics.addStatistics(currentWord, getResult(), scoreIncreased, Timer.stop());
    }

}
