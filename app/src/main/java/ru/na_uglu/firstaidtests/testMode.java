package ru.na_uglu.firstaidtests;

/**
 * Created by User on 03.03.2017.
 */

enum testMode {
    STUDY("study"),
    NORMAL("general"),
    EXPERT("expert");

    private String stringValue;
    private testMode(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
