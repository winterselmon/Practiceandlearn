package com.xyz.practiceandlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 2/13/2017.
 */

public class ReadingComprehensionDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String READINGCOMPREHENSION_SCRIPT = "ReadingComprehensionScript";
    public static final String COLUMN_ID_READING_SCRIPT = "Id_reading_script";
    public static final String COLUMN_READING_SCRIPT = "Reading_script";
    public static final String READINGCOMPREHENSION_QUESTION = "ReadingComprehensionQuestion";
    public static final String COLUMN_ID_READING_QUESTION = "Id_reading_question";
    public static final String COLUMN_READING_QUESTION = "Reading_question";
    public static final String COLUMN_READING_ANSWER = "Reading_answer";
    public static final String COLUMN_ID_READING_SCRIPT2 = "Id_reading_script2";
    public static final String COLUMN_READING_CHOICE_A = "Reading_choice_a";
    public static final String COLUMN_READING_CHOICE_B = "Reading_choice_b";
    public static final String COLUMN_READING_CHOICE_C = "Reading_choice_c";
    public static final String COLUMN_READING_CHOICE_D = "Reading_choice_d";
    public static final String COLUMN_READING_DES = "Reading_des";


    public static final String READINGCOMPREHENSION_SCRIPT_TEST = "ReadingComprehensionScriptTest";
    public static final String COLUMN_ID_READING_SCRIPT_TEST = "Id_reading_script";
    public static final String COLUMN_READING_SCRIPT_TEST = "Reading_script";
    public static final String READINGCOMPREHENSION_QUESTION_TEST = "ReadingComprehensionQuestionTest";
    public static final String COLUMN_ID_READING_QUESTION_TEST = "Id_reading_question";
    public static final String COLUMN_READING_QUESTION_TEST = "Reading_question";
    public static final String COLUMN_READING_ANSWER_TEST = "Reading_answer";
    public static final String COLUMN_ID_READING_SCRIPT2_TEST = "Id_reading_script2";
    public static final String COLUMN_READING_CHOICE_A_TEST = "Reading_choice_a";
    public static final String COLUMN_READING_CHOICE_B_TEST = "Reading_choice_b";
    public static final String COLUMN_READING_CHOICE_C_TEST = "Reading_choice_c";
    public static final String COLUMN_READING_CHOICE_D_TEST = "Reading_choice_d";


    public ReadingComprehensionDatabase (Context context, File dbname){

        objMyDatabase = new MyDatabase(context, dbname);
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLite = objMyDatabase.getReadableDatabase();

    }

    public long addValuesToReadingScript(String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_READING_SCRIPT,strIdScript);
        objContentValues.put(COLUMN_READING_SCRIPT,strScript);

        return writeSQLite.insert(READINGCOMPREHENSION_SCRIPT, null, objContentValues);

    }

    public long addValuesToReadingQuestion(String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strDes, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_READING_QUESTION,strIdQuestion);
        objContentValues.put(COLUMN_READING_QUESTION,strQuestion);
        objContentValues.put(COLUMN_READING_ANSWER,strAnswer);
        objContentValues.put(COLUMN_READING_CHOICE_A,strChoiceA);
        objContentValues.put(COLUMN_READING_CHOICE_B,strChoiceB);
        objContentValues.put(COLUMN_READING_CHOICE_C,strChoiceC);
        objContentValues.put(COLUMN_READING_CHOICE_D,strChoiceD);
        objContentValues.put(COLUMN_READING_DES,strDes);
        objContentValues.put(COLUMN_ID_READING_SCRIPT2,strIdScript2);

        return writeSQLite.insert(READINGCOMPREHENSION_QUESTION, null, objContentValues);

    }

    public long addValuesToReadingScriptTest(String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_READING_SCRIPT_TEST,strIdScript);
        objContentValues.put(COLUMN_READING_SCRIPT_TEST,strScript);

        return writeSQLite.insert(READINGCOMPREHENSION_SCRIPT_TEST, null, objContentValues);

    }

    public long addValuesToReadingQuestionTest(String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_READING_QUESTION_TEST,strIdQuestion);
        objContentValues.put(COLUMN_READING_QUESTION_TEST,strQuestion);
        objContentValues.put(COLUMN_READING_ANSWER_TEST,strAnswer);
        objContentValues.put(COLUMN_READING_CHOICE_A_TEST,strChoiceA);
        objContentValues.put(COLUMN_READING_CHOICE_B_TEST,strChoiceB);
        objContentValues.put(COLUMN_READING_CHOICE_C_TEST,strChoiceC);
        objContentValues.put(COLUMN_READING_CHOICE_D_TEST,strChoiceD);
        objContentValues.put(COLUMN_ID_READING_SCRIPT2_TEST,strIdScript2);

        return writeSQLite.insert(READINGCOMPREHENSION_QUESTION_TEST, null, objContentValues);

    }

}
