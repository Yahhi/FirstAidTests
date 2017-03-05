package ru.na_uglu.firstaidtests;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

public class ModeNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBHelper myDB;
    testMode mode = testMode.NORMAL;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDB = new DBHelper(this);
        fillTestList();

        ListView listOfTests = (ListView)findViewById(R.id.listOfTests);
        listOfTests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstAidTest clickedTest = (firstAidTest) parent.getItemAtPosition(position);

                Intent intent = new Intent(parent.getContext(), testPassingActivity.class);
                intent.putExtra("testName", clickedTest.name);
                intent.putExtra("mode", mode);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mode", mode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mode = (testMode) savedInstanceState.getSerializable("mode");
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferencies = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor preferenciesEditor = preferencies.edit();
        preferenciesEditor.putString("mode", mode.toString());
        preferenciesEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferencies = getPreferences(MODE_PRIVATE);
        String modeString = preferencies.getString("mode", testMode.NORMAL.toString());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (modeString.equals(testMode.EXPERT.toString())) {
            mode = testMode.EXPERT;
            navigationView.setCheckedItem(R.id.expert_mode);
        } else if (modeString.equals(testMode.STUDY.toString())) {
            mode = testMode.STUDY;
            navigationView.setCheckedItem(R.id.study_mode);
        } else {
            mode = testMode.NORMAL;
            navigationView.setCheckedItem(R.id.standart_mode);
        }

    }

    private void fillTestList() {
        firstAidTest[] userTests = myDB.getTests(mode);
        firstAidTestsAdapter adapter;
        adapter = new firstAidTestsAdapter(this, userTests);
        ListView listOfTests = (ListView)findViewById(R.id.listOfTests);
        listOfTests.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mode_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.study_mode) {
            mode = testMode.STUDY;
            fillTestList();
        } else if (id == R.id.standart_mode) {
            mode = testMode.NORMAL;
            fillTestList();
        } else if (id == R.id.expert_mode) {
            mode = testMode.EXPERT;
            fillTestList();
        } else if (id == R.id.statistics) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


