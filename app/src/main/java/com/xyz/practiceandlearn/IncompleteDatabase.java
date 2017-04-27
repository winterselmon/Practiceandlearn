package com.xyz.practiceandlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.security.PublicKey;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 2/7/2017.
 */

public class IncompleteDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String INCOMPLETE_QUESTION = "IncompleteSentenceQuestion";
    public static final String COLUMN_ID_INCOMPLETE_QUESTION = "Id_incomplete_question";
    public static final String COLUMN_INCOMPLETE_QUESTION = "Incomplete_question";
    public static final String COLUMN_INCOMPLETE_ANSWER = "Incomplete_answer";
    public static final String INCOMPLETE_CHOICE = "IncompleteSentenceChoice";
    public static final String COLUMN_ID_INCOMPLETE_CHOICE = "Id_incomplete_choice";
    public static final String COLUMN_INCOMPLETE_CHOICE_A = "Incomplete_choice_a";
    public static final String COLUMN_INCOMPLETE_CHOICE_B = "Incomplete_choice_b";
    public static final String COLUMN_INCOMPLETE_CHOICE_C = "Incomplete_choice_c";
    public static final String COLUMN_INCOMPLETE_CHOICE_D = "Incomplete_choice_d";
    public static final String COLUMN_INCOMPLETE_DES = "Incomplete_des";
    public static final String COLUMN_ID_INCOMPLETE_QUESTION2 = "Id_incomplete_question2";

    public static final String INCOMPLETE_QUESTION_TEST = "IncompleteSentenceQuestionTest";
    public static final String COLUMN_ID_INCOMPLETE_QUESTION_TEST = "Id_incomplete_question";
    public static final String COLUMN_INCOMPLETE_QUESTION_TEST = "Incomplete_question";
    public static final String COLUMN_INCOMPLETE_ANSWER_TEST = "Incomplete_answer";
    public static final String INCOMPLETE_CHOICE_TEST = "IncompleteSentenceChoiceTest";
    public static final String COLUMN_ID_INCOMPLETE_CHOICE_TEST = "Id_incomplete_choice";
    public static final String COLUMN_INCOMPLETE_CHOICE_A_TEST = "Incomplete_choice_a";
    public static final String COLUMN_INCOMPLETE_CHOICE_B_TEST = "Incomplete_choice_b";
    public static final String COLUMN_INCOMPLETE_CHOICE_C_TEST = "Incomplete_choice_c";
    public static final String COLUMN_INCOMPLETE_CHOICE_D_TEST = "Incomplete_choice_d";
    public static final String COLUMN_ID_INCOMPLETE_QUESTION2_TEST = "Id_incomplete_question2";

    public IncompleteDatabase (Context context, String dbname){

        objMyDatabase = new MyDatabase(context, dbname);
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLite = objMyDatabase.getReadableDatabase();

    }

    public long addValuesToIncompleteQuestion(String strIdQuestion, String strQuestion, String strAnswer){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_INCOMPLETE_QUESTION,strIdQuestion);
        objContentValues.put(COLUMN_INCOMPLETE_QUESTION,strQuestion);
        objContentValues.put(COLUMN_INCOMPLETE_ANSWER,strAnswer);

        return writeSQLite.insert(INCOMPLETE_QUESTION, null, objContentValues);

    }

    public long addValuesToIncompleteChoice(String strIdChoice, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strDes, String strIdQuestion2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_INCOMPLETE_CHOICE,strIdChoice);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_A,strChoiceA);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_B,strChoiceB);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_C,strChoiceC);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_D,strChoiceD);
        objContentValues.put(COLUMN_INCOMPLETE_DES,strDes);
        objContentValues.put(COLUMN_ID_INCOMPLETE_QUESTION2,strIdQuestion2);

        return writeSQLite.insert(INCOMPLETE_CHOICE, null, objContentValues);

    }

    public long addValuesToIncompleteQuestionTest(String strIdQuestion, String strQuestion, String strAnswer){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_INCOMPLETE_QUESTION_TEST,strIdQuestion);
        objContentValues.put(COLUMN_INCOMPLETE_QUESTION_TEST,strQuestion);
        objContentValues.put(COLUMN_INCOMPLETE_ANSWER_TEST,strAnswer);

        return writeSQLite.insert(INCOMPLETE_QUESTION_TEST, null, objContentValues);

    }

    public long addValuesToIncompleteChoiceTest(String strIdChoice, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strIdQuestion2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_INCOMPLETE_CHOICE_TEST,strIdChoice);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_A_TEST,strChoiceA);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_B_TEST,strChoiceB);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_C_TEST,strChoiceC);
        objContentValues.put(COLUMN_INCOMPLETE_CHOICE_D_TEST,strChoiceD);
        objContentValues.put(COLUMN_ID_INCOMPLETE_QUESTION2_TEST,strIdQuestion2);

        return writeSQLite.insert(INCOMPLETE_CHOICE_TEST, null, objContentValues);

    }
}
