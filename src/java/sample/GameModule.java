package sample;

public class GameModule extends Module {
    private static final int NUMOFQUESTIONS = 5;

    public GameModule() {
        super(NUMOFQUESTIONS);
    }

    // this function generate a new word and then ask the user
    protected void newQuestion() {
        if (currentIndex == NUMOFQUESTIONS) {  // the quiz is finished
            setQuizState(QuizState.finished);
            return;
        }

        setQuizState(QuizState.running);  // now set the state to running
        setResult(Result.mastered);  // set original result to Mastered
        currentIndex++;

        currentWord = words.nextWord();  // set up the word and get the word that is testing on

        // set the labels' messages and also speak out the message
        mainLabelText = "Spell Word " + currentIndex + " of " + NUMOFQUESTIONS + ":";
        promptLabelText = "";
        speak("Please spell", currentWord);
        
    }

    // this function check the spelling (input) and then set up a range of stuff
    protected void checkSpelling() {
        // first check if the word is skipped
        if (resultEqualsTo(Result.skipped)) {
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Word skipped", "");

        // if statement for each result after checking the spelling (input)
        } else if ( words.checkUserSpelling(getUserInput()) ) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Correct", "");

        } else if (resultEqualsTo(Result.mastered)) {  // still 1st attempt, but incorrect
            setResult(Result.faulted);

            // setting up the labels' text and speak out the message
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = "Hint: second letter is '" + currentWord.charAt(1) + "'";
            speak("Incorrect, try once more.", currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.failed);
            setQuizState(QuizState.ready);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Incorrect", "");
        }
    }

}
