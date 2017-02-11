package ru.na_uglu.firstaidtests;

import java.util.Date;

/**
 * Created by User on 10.02.2017.
 */

public class firstAidTest {
    int UNDEFINED_ANSWERS = -1;
    Date UNDEFINED_DATE = new Date();
    public String name;
    public String description;
    public int rightAnswers = UNDEFINED_ANSWERS;
    public int wrongAnswers = UNDEFINED_ANSWERS;
    public int starsForUser = 0;
    public Date dateLastRun = UNDEFINED_DATE;

    public firstAidTest(String name, String description, int starsForUser) {
        this.description = description;
        this.name = name;
        this.starsForUser = starsForUser;
    }
}
