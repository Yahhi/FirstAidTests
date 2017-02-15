package ru.na_uglu.firstaidtests;

/**
 * Created by User on 13.02.2017.
 */

public class testAnswer extends Object {
    public String text;
    public Boolean isRight;
    public Boolean isGivenAnswer = false;

    public testAnswer(String text, boolean isRight) {
        this.text = text;
        this.isRight = isRight;
    }
}
