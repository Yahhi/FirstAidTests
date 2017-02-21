package ru.na_uglu.firstaidtests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 13.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "FirstAidTests.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `tests` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `description` TEXT, `top_result` INTEGER DEFAULT 0, `tryes_count` INTEGER DEFAULT 0)");
        insertTests(db);
        db.execSQL("CREATE TABLE `questions` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `test_id` INTEGER NOT NULL, `question` TEXT NOT NULL, `comment` TEXT )");
        insertQuestions(db);
        db.execSQL("CREATE TABLE `answers` ( `id` INTEGER NOT NULL, `question_id` INTEGER NOT NULL, `answer` TEXT NOT NULL, `right` INTEGER DEFAULT 0, PRIMARY KEY(`id`,`question_id`) )");
        insertAnswers(db);

    }

    private void insertTests(SQLiteDatabase db) {
        db.execSQL("INSERT INTO `tests` VALUES (1,'Первая помощь на дороге','Что необходимо знать водителю в чрезвычайной ситуации', -1, 0)");
        db.execSQL("INSERT INTO `tests` VALUES (2,'Первая помощь ребенку','Маленький ребенок может упасть, удариться, съесть что-то не то... Что делать в таких ситуациях', -1, 0)");
        db.execSQL("INSERT INTO `tests` VALUES (3,'Первая помощь на водоемах','О помощи в опасных ситуациях, связанных с водоемами', -1, 0)");
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
        onCreate(db);
    }

    public firstAidTest[] getTests() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfTests = (int) DatabaseUtils.queryNumEntries(db, "tests");
        firstAidTest[] tests = new firstAidTest[numberOfTests];
        Cursor cursor = db.rawQuery("SELECT * FROM tests", null);
        cursor.moveToFirst();
        int i = 0;
        while (cursor.isAfterLast() == false) {
            tests[i++] = new firstAidTest(
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getInt(cursor.getColumnIndex("top_result"))
            );
            cursor.moveToNext();
        }
        return tests;
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
        while (cursorForAnswers.isAfterLast() == false) {
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

    public int saveTestResult(int testId, int starsCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues testResult = new ContentValues(1);
        testResult.put("top_result", starsCount);
        return db.update("tests", testResult, "id=?", new String[]{Integer.toString(testId)});
    }

    public int saveTestResult(String testName, int starsCount) {
        int result = saveTestResult(getTestId(testName), starsCount);
        return result;
    }
}
