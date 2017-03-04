package ru.na_uglu.firstaidtests;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class resultsActivity extends AppCompatActivity {

    String testName;
    int allAnswersCount;
    int rightAnswersCount;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allAnswersCount = getIntent().getIntExtra("allAnswersCount", 0);
        rightAnswersCount = getIntent().getIntExtra("rightAnswersCount", 0);
        testName = getIntent().getStringExtra("testName");

        TextView allAnswersCountOnScreen = (TextView) findViewById(R.id.allAnswersCount);
        allAnswersCountOnScreen.setText(Integer.toString(allAnswersCount));
        TextView rightAnswersCountOnScreen = (TextView) findViewById(R.id.rightAnswersCount);
        rightAnswersCountOnScreen.setText(Integer.toString(rightAnswersCount));

        ShareButton facebookShareButton = (ShareButton) findViewById(R.id.facebookShareButton);
        ShareLinkContent testCompletedLink = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://na-uglu.ru"))
                .setContentTitle(testName)
                .setContentDescription("Я прошел тест " + testName +
                        " с результатом " + Integer.toString(rightAnswersCount) +
                        " из " + Integer.toString(allAnswersCount)+ ".").build();
        facebookShareButton.setShareContent(testCompletedLink);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_program_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}
