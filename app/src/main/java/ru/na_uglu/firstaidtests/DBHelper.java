package ru.na_uglu.firstaidtests;

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
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `tests` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `description` TEXT )");
        insertTests(db);
        db.execSQL("CREATE TABLE `questions` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `test_id` INTEGER NOT NULL, `question` TEXT NOT NULL )");
        insertQuestions(db);
        db.execSQL("CREATE TABLE `answers` ( `id` INTEGER NOT NULL, `question_id` INTEGER NOT NULL, `answer` TEXT NOT NULL, `right` INTEGER DEFAULT 0, PRIMARY KEY(`id`,`question_id`) )");
        insertAnswers(db);

    }

    private void insertTests(SQLiteDatabase db) {
        db.execSQL("INSERT INTO `tests` VALUES (1,'Первая помощь на дороге','Что необходимо знать водителю в чрезвычайной ситуации')");
        db.execSQL("INSERT INTO `tests` VALUES (2,'Первая помощь ребенку','Маленький ребенок может упасть, удариться, съесть что-то не то... Что делать в таких ситуациях')");
        db.execSQL("INSERT INTO `tests` VALUES (3,'Первая помощь на водоемах','О помощи в опасных ситуациях, связанных с водоемами')");
    }

    private void insertQuestions(SQLiteDatabase db) {
    }

    private void insertAnswers(SQLiteDatabase db) {
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
                    -1);
            cursor.moveToNext();
        }
        return tests;
    }
}
