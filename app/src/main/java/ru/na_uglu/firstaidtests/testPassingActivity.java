package ru.na_uglu.firstaidtests;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class testPassingActivity extends AppCompatActivity {

    DBHelper myDB;
    int currentQuestion = -1;
    String testName;
    testMode mode;

    int[] userAnswers;
    int answersSet = 0;
    testQuestion[] localQuestionsInOrder;
    boolean rightAnswer[];
    String checkedAnswerText = "";
    testQuestion questionToShow;
    boolean prevNextButtonsEnabled;

    Long testStarted;

    int UNSET_ANSWER = -1;
    int TIME_ON_TEST = 60000;
    int TIMEBAR_STEP = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_passing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testName = getIntent().getExtras().getString("testName");
        this.setTitle(testName);

        mode = (testMode) getIntent().getSerializableExtra("mode");

        myDB = new DBHelper(this);
        if (mode == testMode.STUDY) {
            localQuestionsInOrder = myDB.getLowRatedQuestions(testName, 2);
            enablePrevNextButtons(false);
        } else if (mode == testMode.EXPERT) {
            localQuestionsInOrder = myDB.getRandomQuestions(testName, 2);
            enablePrevNextButtons(false);
            CountDownTimer closePassTimer = new CountDownTimer(TIME_ON_TEST, TIMEBAR_STEP) {
                @Override
                public void onTick(long millisUntilFinished) {
                    ProgressBar timeBar = (ProgressBar) findViewById(R.id.timeBar);
                    timeBar.setProgress((int) ((TIME_ON_TEST-millisUntilFinished)/TIMEBAR_STEP));
                }

                @Override
                public void onFinish() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Button button = (Button) findViewById(R.id.getResultButton);
                            button.performClick();
                        }
                    });
                }
            };
            closePassTimer.start();

            ProgressBar timeBar = (ProgressBar) findViewById(R.id.timeBar);
            timeBar.setVisibility(View.VISIBLE);
            timeBar.setMax(60);
        } else {
            localQuestionsInOrder = myDB.getRandomQuestions(testName);
            enablePrevNextButtons(true);
        }

        userAnswers = new int[localQuestionsInOrder.length];
        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = UNSET_ANSWER;
        }

        currentQuestion++;
        showQuestion();

        RadioGroup answers = (RadioGroup) findViewById(R.id.answersRadioGroup);
        answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId >= 0) { // Бывают нажатия на кнопки с перекрытием...
                    RadioButton checkedButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    checkedAnswerText = (String) checkedButton.getText();

                    Button buttonSaveAnswer = (Button) findViewById(R.id.answerAcceptButton);
                    buttonSaveAnswer.setEnabled(true);
                }
            }
        });

        Button checkResults = (Button) findViewById(R.id.getResultButton);
        checkResults.setVisibility(View.INVISIBLE);

        testStarted = System.currentTimeMillis()/1000;
    }

    private void enablePrevNextButtons(boolean b) {
        prevNextButtonsEnabled = b;
    }

    public void onClickCheckResult(View v) {
        int rightAnswersCount = getRightAnswersCount();
        myDB.saveTestResult(testName, rightAnswersCount, userAnswers.length, (int) (System.currentTimeMillis()/1000-testStarted), mode);

        Intent intent  = new Intent(v.getContext(), resultsActivity.class);
        intent.putExtra("allAnswersCount", userAnswers.length);
        intent.putExtra("rightAnswersCount", rightAnswersCount);
        intent.putExtra("testName", testName);
        startActivity(intent);
    }

    private int getRightAnswersCount() {
        int rightAnswersCount = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            rightAnswersCount += checkUserAnswer(i, userAnswers[i]);
        }
        return rightAnswersCount;
    }

    private int checkUserAnswer(int questionNumber, int userAnswer) {
        testQuestion question = localQuestionsInOrder[questionNumber];
        if (question.answers[userAnswer].isRight) {
            return 1;
        } else {
            return 0;
        }
    }

    public void onClickAcceptAnswers(View v) {
        saveUserAnswer();
        if (currentQuestion < userAnswers.length-1) {
            currentQuestion++;
        }
        showQuestion();
    }

    private void saveUserAnswer() {
        if (userAnswers[currentQuestion] == UNSET_ANSWER) {
            answersSet++;
        }
        userAnswers[currentQuestion] = questionToShow.getAnswerId(checkedAnswerText);
    }

    private void showQuestion() {
        checkControlButtonsState();

        questionToShow = localQuestionsInOrder[currentQuestion];
        TextView questionText = (TextView) findViewById(R.id.testQuestion);
        questionText.setText(questionToShow.question);

        RadioGroup answersRadioGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        answersRadioGroup.removeAllViews();
        rightAnswer = new boolean[questionToShow.answers.length];
        int i = 0;
        for (testAnswer answer : questionToShow.answers) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer.text);
            radioButton.setPadding(2, 4, 0, 1);
            if (userAnswers[currentQuestion] == i) {
                radioButton.setChecked(true);
            }
            answersRadioGroup.addView(radioButton);
            rightAnswer[i++] = answer.isRight;
        }

        Button buttonSaveAnswer = (Button) findViewById(R.id.answerAcceptButton);
        buttonSaveAnswer.setEnabled(false);
    }

    private void checkControlButtonsState() {
        if (prevNextButtonsEnabled) {
            Button nextButton = (Button) findViewById(R.id.nextButton);
            Button prevButton = (Button) findViewById(R.id.previousButton);
            final int maxCurrentQuestion = userAnswers.length - 1;
            if (currentQuestion == 0) {
                prevButton.setVisibility(View.INVISIBLE);
            } else if (currentQuestion == maxCurrentQuestion) {
                nextButton.setVisibility(View.INVISIBLE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
            }

        }
        if (allAnswersSet()) {
            Button checkResults = (Button) findViewById(R.id.getResultButton);
            checkResults.setVisibility(View.VISIBLE);
        }
    }

    private boolean allAnswersSet() {
        boolean answersSetEqualAllAnswers = false;
        if (answersSet == userAnswers.length) {
            answersSetEqualAllAnswers = true;
        }
        return answersSetEqualAllAnswers;
    }

    public void prevButtonClick(View view) {
        currentQuestion--;
        showQuestion();
    }

    public void nextButtonClick(View view) {
        currentQuestion++;
        showQuestion();
    }
}
