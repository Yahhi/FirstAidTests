package ru.na_uglu.firstaidtests;

/**
 * Created by User on 13.02.2017.
 */

public class testQuestion extends Object {
    public String question;
    public testAnswer[] answers;

    public testQuestion(String text, String answerRight, String answerWrong1, String answerWrong2, String answerWrong3) {
        question = text;
        answers = new testAnswer[4];
        answers[0] = new testAnswer(answerRight, true);
        answers[1] = new testAnswer(answerWrong1, false);
        answers[2] = new testAnswer(answerWrong2, false);
        answers[3] = new testAnswer(answerWrong3, false);
    }

    public testQuestion(String text, testAnswer[] answers) {
        question = text;
        this.answers = answers;
    }

    public int getAnswerId(String text) {
        int id = 0;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].text == text) {
                id = i;
                break;
            }
        }
        return id;
    }
}

