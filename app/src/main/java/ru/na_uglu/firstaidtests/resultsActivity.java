package ru.na_uglu.firstaidtests;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class resultsActivity extends AppCompatActivity {

    int testId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int allAnswersCount = getIntent().getIntExtra("allAnswersCount", 0);
        int rightAnswersCount = getIntent().getIntExtra("rightAnswersCount", 0);
        testId = getIntent().getIntExtra("testId", 0);

        TextView allAnswersCountOnScreen = (TextView) findViewById(R.id.allAnswersCount);
        allAnswersCountOnScreen.setText(Integer.toString(allAnswersCount));
        TextView rightAnswersCountOnScreen = (TextView) findViewById(R.id.rightAnswersCount);
        rightAnswersCountOnScreen.setText(Integer.toString(rightAnswersCount));
    }

}
