package ru.na_uglu.firstaidtests;

import java.util.Random;

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

    private testAnswer[] getAnswersInRandomOrder() {
        testAnswer[] randomAnswers = new testAnswer[answers.length];
        Random random = new Random();
        boolean[] usedAnswer = new boolean[answers.length];
        int i = 0;
        while (i < randomAnswers.length) {
            int randomAnswerId = random.nextInt(randomAnswers.length);
            if (!usedAnswer[randomAnswerId]) {
                usedAnswer[randomAnswerId] = true;
                randomAnswers[i] = answers[randomAnswerId];
                i++;
            }
        }
        return  randomAnswers;
    }

    public void mixAnswers() {
        answers = getAnswersInRandomOrder();
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

