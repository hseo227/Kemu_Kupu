package spellingQuiz;

import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import static spellingQuizUtil.FestivalSpeech.speak;

public class ModulePractise extends Module {
    private final int NUMOFQUESTIONS;

    public ModulePractise(int numOfQuestions) {
        super(numOfQuestions);
        NUMOFQUESTIONS = numOfQuestions;
    }

    // this function generate a new word and then ask the user
    // return true, if the quiz is not finished | return false, if the quiz is finished
    public boolean newQuestion() {
        if (currentIndex == NUMOFQUESTIONS) {  // the quiz is finished
            return false;
        }

        setQuizState(QuizState.running);  // now set the state to running
        setResult(Result.mastered);  // set original result to Mastered
        currentIndex++;

        currentWord = words.nextWord();  // set up the word and get the word that is testing on

        // set the labels' messages and also speak out the message
        mainLabelText = "Spell Word " + currentIndex + " of " + NUMOFQUESTIONS + ":";
        promptLabelText = "";
        speak("Please spell", currentWord);

        return true;
    }

    // this function check the spelling (input) and then set up a range of stuff
    public void checkSpelling() {
        // first check if the word is skipped
        if (resultEqualsTo(Result.skipped)) {
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = "Correct answer: " + currentWord;
            promptLabelText = "Press 'Enter' or click 'Check' button again to continue";
            speak("Word skipped", "");

        // if statement for each result after checking the spelling (input)
        } else if ( words.checkUserSpelling(getUserInput()) ) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Correct", "");
            increaseScore();

        } else if (resultEqualsTo(Result.mastered)) {  // still 1st attempt, but incorrect
            setResult(Result.faulted);

            // setting up the labels' text and speak out the message
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = "Hint: " + words.getHintPractiseModule();
            speak("Incorrect, try once more.", currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.failed);
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = "Correct answer: " + currentWord;
            promptLabelText = "Press 'Enter' or click 'Check' button again to continue";
            speak("Incorrect", "");
        }
    }

}
