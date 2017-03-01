package ru.na_uglu.firstaidtests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.GregorianCalendar;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "FirstAidTests.db";

    int UNSET_QUESTION_NUMBER = -1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `tests` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`title` TEXT NOT NULL, `description` TEXT )");
        insertTests(db);
        db.execSQL("CREATE TABLE `questions` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
                "`test_id` INTEGER NOT NULL, `question` TEXT NOT NULL, `comment` TEXT )");
        insertQuestions(db);
        db.execSQL("CREATE TABLE `answers` ( `id` INTEGER NOT NULL, `question_id` INTEGER NOT NULL, " +
                "`answer` TEXT NOT NULL, `right` INTEGER DEFAULT 0, PRIMARY KEY(`id`,`question_id`) )");
        insertAnswers(db);
        db.execSQL("CREATE TABLE `test_done` ( `test_id` INTEGER NOT NULL, "+
                "`test_done_datetime` INTEGER NOT NULL, `questions_asked` INTEGER NOT NULL, " +
                "`questions_right` INTEGER NOT NULL, `running_mode` TEXT DEFAULT 'general', " +
                "`test_done_time` INTEGER, PRIMARY KEY(`test_id`,`test_done_datetime`) )");

    }

    private void insertTests(SQLiteDatabase db) {
        db.execSQL("INSERT INTO `tests` VALUES (1,'Первая помощь на дороге','Что необходимо знать водителю в чрезвычайной ситуации')");
        db.execSQL("INSERT INTO `tests` VALUES (2,'Первая помощь ребенку','Маленький ребенок может упасть, удариться, съесть что-то не то... Что делать в таких ситуациях')");
    }

    private void insertQuestions(SQLiteDatabase db) {
        db.execSQL("INSERT INTO `questions` VALUES (1,1,'Когда следует начинать сердечно-легочную реанимацию пострадавшего?',NULL);");
        db.execSQL("INSERT INTO `questions` VALUES (2,1,'Какие сведения необходимо сообщить диспетчеру для вызова «Скорой медицинской помощи» при ДТП?','Наиболее полная информация позволяет службе «Скорой медицинской помощи» определиться с количеством высылаемых бригад медицинских специалистов, их специализацией, упрощает выбор пути и сокращает время приезда автомобиля «Скорой медицинской помощи» и других служб. ');");
        db.execSQL("INSERT INTO `questions` VALUES (3,1,'Как следует расположить руки на грудной клетке пострадавшего при давлении руками на его грудину (выполнении непрямого массажа сердца)?','Обращаем внимание, что в правильном ответе «ладони обеих рук накладываются одна на другую», т. к. выполнение непрямого массажа сердца требует больших физических усилий.');");
    }

    private void insertAnswers(SQLiteDatabase db) {
        db.execSQL("INSERT INTO `answers` VALUES (1,1,'При наличии болей в области сердца и затрудненного дыхания',0);");
        db.execSQL("INSERT INTO `answers` VALUES (2,1,'При потере пострадавшим сознания, независимо от наличия пульса на сонной артерии, и признаков дыхания',0);");
        db.execSQL("INSERT INTO `answers` VALUES (3,1,'При потере пострадавшим сознания и отсутствии пульса на сонной артерии, а также признаков дыхания',1);");
        db.execSQL("INSERT INTO `answers` VALUES (4,2,'Указать общеизвестные ориентиры, ближайшие к месту ДТП. Сообщить о количестве пострадавших, указать их пол и возраст',0);");
        db.execSQL("INSERT INTO `answers` VALUES (5,2,'Указать улицу и номер дома, ближайшего к месту ДТП. Сообщить, кто пострадал в ДТП (пешеход, водитель автомобиля или пассажиры), и описать травмы, которые они получили.',0);");
        db.execSQL("INSERT INTO `answers` VALUES (6,2,'Указать точное место совершенного ДТП (назвать улицу, номер дома и общеизвестные ориентиры, ближайшие к месту ДТП). Сообщить о количестве пострадавших, их пол, примерный возраст и о наличии у них при- знаков жизни, а также сильного кровотечения.',1);");
        db.execSQL("INSERT INTO `answers` VALUES (7,3,'Основания ладоней обеих рук должны располагаться на грудной клетке на два пальца выше мечевидного отростка так, чтобы большой палец одной руки указывал в сторону левого плеча пострадавшего, а другой – в сторону правого плеча',0);");
        db.execSQL("INSERT INTO `answers` VALUES (8,3,'Основания ладоней обеих рук, которые накладываются одна на другую, должны располагаться на грудной клетке на два пальца выше мечевидного отростка так, чтобы большой палец одной руки указывал в сторону подбородка пострадавшего, а другой – в сторону живота',1);");
        db.execSQL("INSERT INTO `answers` VALUES (9,3,'Давление руками на грудину выполняют основанием ладони только одной руки, расположенной на грудной клетке на два пальца выше мечевидного отростка. Направление большого пальца не имеет значения.',0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tests");
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS answers");
        db.execSQL("DROP TABLE IF EXISTS test_done");
        onCreate(db);
    }

    public firstAidTest[] getTests() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfTests = (int) DatabaseUtils.queryNumEntries(db, "tests");
        firstAidTest[] tests = new firstAidTest[numberOfTests];
        Cursor cursor = db.rawQuery("SELECT * FROM tests", null);
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            tests[i++] = new firstAidTest(
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    getTestResult(cursor.getInt(cursor.getColumnIndex("id")))
            );
            cursor.moveToNext();
        }
        cursor.close();
        return tests;
    }

    private int getTestResult(int testId) {
        final int MAX_STARS = 5;

        Long timestampLong = System.currentTimeMillis()/1000;
        String timestampNow = timestampLong.toString();
        Long timeDifferenceForActualTest = Long.valueOf(30*24*60*60);
        String maxTimeDifference = timeDifferenceForActualTest.toString();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT questions_right, questions_asked FROM test_done " +
                "WHERE ?-test_done_datetime < ? AND test_id = ?",
                new String[]{timestampNow, maxTimeDifference, Integer.toString(testId)});
        int markForTest = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int questions_right = cursor.getInt(cursor.getColumnIndex("questions_right"));
            int questions_all = cursor.getInt(cursor.getColumnIndex("questions_asked"));
            int mark = Math.round(questions_right * MAX_STARS / questions_all);
            if (markForTest < mark)
            markForTest = mark;
            cursor.moveToNext();
        }
        cursor.close();
        return markForTest ;
    }

    public int getInTestQuestionCount(String testName) {
        int testId = getTestId(testName);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorForQuestion = db.rawQuery("SELECT * FROM questions WHERE test_id = ?", new String[]{Integer.toString(testId)});
        int count = cursorForQuestion.getCount();
        cursorForQuestion.close();
        return count;
    }

    private int getTestId(String testName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorForTestId = db.rawQuery("SELECT id FROM tests WHERE title = ?", new String[]{testName});
        cursorForTestId.moveToFirst();
        int testId = 1;
        if (cursorForTestId.getCount() > 0) {
            testId = cursorForTestId.getInt(cursorForTestId.getColumnIndex("id"));
        }
        cursorForTestId.close();
        return testId;
    }

    public testQuestion getQuestion(String testName, int questionNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        int testId = getTestId(testName);

        Cursor cursorForQuestion = db.rawQuery("SELECT id, question FROM questions WHERE test_id = ?", new String[]{Integer.toString(testId)});
        String questionText = "";
        int questionId = 0;
        if (cursorForQuestion.moveToPosition(questionNumber)) {
            questionText = cursorForQuestion.getString(cursorForQuestion.getColumnIndex("question"));
            questionId = cursorForQuestion.getInt(cursorForQuestion.getColumnIndex("id"));
        }
        cursorForQuestion.close();

        Cursor cursorForAnswers = db.rawQuery("SELECT answer, right FROM answers WHERE question_id = ?", new String[]{Integer.toString(questionId)});
        cursorForAnswers.moveToFirst();
        testAnswer[] answers = new testAnswer[cursorForAnswers.getCount()];
        int i = 0;
        while (!cursorForAnswers.isAfterLast()) {
            String answerText = cursorForAnswers.getString(cursorForAnswers.getColumnIndex("answer"));
            boolean answerRight = false;
            if (cursorForAnswers.getInt(cursorForAnswers.getColumnIndex("right")) == 1) {
                answerRight = true;
            }
            answers[i] = new testAnswer(answerText, answerRight);
            i++;
            cursorForAnswers.moveToNext();
        }
        cursorForAnswers.close();

        testQuestion question = new testQuestion(questionText, answers);
        return question;
    }

    public void saveTestResult(int testId, int rightQuestions, int allAskedQuestions, int testDoneTime) {
        Long timestampLong = System.currentTimeMillis()/1000;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues testResult = new ContentValues(5);
        testResult.put("test_id", testId);
        testResult.put("test_done_datetime", timestampLong);
        testResult.put("questions_asked", allAskedQuestions);
        testResult.put("questions_right", rightQuestions);
        testResult.put("test_done_time", testDoneTime);
        db.insert("test_done", null, testResult);
    }

    public void saveTestResult(int testId, int rightQuestions, int allAskedQuestions) {
        saveTestResult(testId, rightQuestions, allAskedQuestions, 0);
    }

    public void saveTestResult(String testName, int rightQuestions, int allAskedQuestions) {
        saveTestResult(getTestId(testName), rightQuestions, allAskedQuestions);
    }

    public void saveTestResult(String testName, int rightQuestions, int allAskedQuestions, int testDoneTime) {
        saveTestResult(getTestId(testName), rightQuestions, allAskedQuestions, testDoneTime);
    }

    public testQuestion[] getRandomQuestions(String testName) {
        int[] questionMapping;

        testQuestion[] questions = new testQuestion[getInTestQuestionCount(testName)];
        questionMapping = new int[questions.length];
        for (int i = 0; i < questions.length; i++) {
            questionMapping[i] = UNSET_QUESTION_NUMBER;
        }

        for (int i = 0; i < questionMapping.length; i++) {
            questionMapping[getRandomFreeQuestion(questionMapping)] = i;
        }

        for (int i = 0; i < questionMapping.length; i++) {
            questions[i] = getQuestion(testName, questionMapping[i]);
            questions[i].mixAnswers();
        }

        return questions;
    }

    private int getRandomFreeQuestion(int[] questionMapping) {
        Random random = new Random();
        int freeQuestionNumber = random.nextInt(questionMapping.length);
        while (questionMapping[freeQuestionNumber] != UNSET_QUESTION_NUMBER) {
            freeQuestionNumber = random.nextInt(questionMapping.length);
        }
        return freeQuestionNumber;
    }
}
