package ru.na_uglu.firstaidtests;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class testPassingActivity extends AppCompatActivity {

    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_passing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String testName = getIntent().getExtras().getString("testName");
        this.setTitle(testName);

        myDB = new DBHelper(this);
        testQuestion questionToShow = myDB.getQuestion(testName, 0);

        TextView questionText = (TextView) findViewById(R.id.testQuestion);
        questionText.setText(questionToShow.question);

        RadioGroup answersRadioGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
        for (testAnswer answer : questionToShow.answers) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer.text);
            answersRadioGroup.addView(radioButton);
        }
    }

}