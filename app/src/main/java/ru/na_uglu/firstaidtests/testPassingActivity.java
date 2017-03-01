package ru.na_uglu.firstaidtests;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Time;
import java.util.Random;

public class testPassingActivity extends AppCompatActivity {

    DBHelper myDB;
    int currentQuestion = -1;
    String testName;
    int[] userAnswers;
    int answersSet = 0;
    testQuestion[] localQuestionsInOrder;
    boolean rightAnswer[];
    String checkedAnswerText = "";
    testQuestion questionToShow;

    Long testStarted;

    int UNSET_ANSWER = -1;
    int UNSET_QUESTION_NUMBER = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_passing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testName = getIntent().getExtras().getString("testName");
        this.setTitle(testName);

        myDB = new DBHelper(this);
        localQuestionsInOrder = myDB.getRandomQuestions(testName);

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

    public void onClickCheckResult(View v) {
        int rightAnswersCount = getRightAnswersCount();
        myDB.saveTestResult(testName, rightAnswersCount, userAnswers.length, (int) (System.currentTimeMillis()/1000-testStarted));

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
        checkNextPrevButtonsState();

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

    private void checkNextPrevButtonsState() {
        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button prevButton = (Button) findViewById(R.id.previousButton);
        final int maxCurrentQuestion = userAnswers.length-1;
        if (currentQuestion == 0) {
                prevButton.setVisibility(View.INVISIBLE);
        } else if (currentQuestion == maxCurrentQuestion) {
                nextButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
            prevButton.setVisibility(View.VISIBLE);
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
