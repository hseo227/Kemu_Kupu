package spellingQuiz;

import spellingQuizUtil.Hint;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import static spellingQuizUtil.FestivalSpeech.speak;

public class ModuleGames extends Module {
    private static final int NUMBER_OF_QUESTIONS = 5;

    public ModuleGames() {
        super(NUMBER_OF_QUESTIONS);
    }

    // this function generate a new word and then ask the user
    // return true, if the quiz is not finished | return false, if the quiz is finished
    public boolean newQuestion() {
        if (currentIndex == NUMBER_OF_QUESTIONS) {  // the quiz is finished
            return false;
        }

        setQuizState(QuizState.RUNNING);  // now set the state to running
        setResult(Result.MASTERED);  // set original result to Mastered
        currentIndex++;

        currentWord = words.nextWord();  // set up the word and get the word that is testing on

        // set the labels' messages and also speak out the message
        mainLabelText = "Spell Word " + currentIndex + " of " + NUMBER_OF_QUESTIONS + ":";
        promptLabelText = words.getHint(Hint.NO_HINT);
        speak("Please spell", currentWord);

        return true;
    }

    // this function check the spelling (input) and then set up a range of stuff
    public void checkSpelling() {
        // first check if the word is skipped
        if (resultEqualsTo(Result.SKIPPED)) {
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Word skipped", "");

        // if statement for each result after checking the spelling (input)
        } else if ( words.checkUserSpelling(getUserInput()) ) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Correct", "");
            increaseScore();

        } else if (resultEqualsTo(Result.MASTERED)) {  // still 1st attempt, but incorrect
            setResult(Result.FAULTED);

            // setting up the labels' text and speak out the message
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = words.getHint(Hint.GAMES_M_HINT);
            speak("Incorrect, try once more.", currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.FAILED);
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Incorrect", "");
        }
    }

}
