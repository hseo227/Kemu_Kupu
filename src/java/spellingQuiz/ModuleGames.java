package spellingQuiz;

import spellingQuizUtil.Hint;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import spellingQuizUtil.TestedWords;

import static spellingQuizUtil.FestivalSpeech.speak;


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
     */
    public void checkSpelling() {
        // first check if the word is skipped
        if (resultEqualsTo(Result.SKIPPED)) {
            setQuizState(QuizState.READY);  // set the state to ready for the next question
            TestedWords.addIncorrect(currentWord);
            
            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Word skipped", "");

        // if statement for each result after checking the spelling (input)
        } else if ( words.checkUserSpelling(getUserInput()) ) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.READY);  // set the state to ready for the next question
            TestedWords.addCorrect(currentWord);

            // setting up the labels' text and speak out the message
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Correct", "");
            increaseScore();

        } else if (resultEqualsTo(Result.MASTERED)) {  // still 1st attempt, but incorrect
            setResult(Result.FAULTED);
            TestedWords.addIncorrect(currentWord);

            // setting up the labels' text and speak out the message
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = words.getHint(Hint.GAMES_M_HINT);
            speak("Incorrect, try once more.", currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.FAILED);
            setQuizState(QuizState.READY);  // set the state to ready for the next question
            TestedWords.addIncorrect(currentWord);

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Incorrect", "");
        }
    }

}
