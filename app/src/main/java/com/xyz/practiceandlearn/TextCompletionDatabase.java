package com.xyz.practiceandlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 2/24/2017.
 */

public class TextCompletionDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TEXTCOMPLETION_SCRIPT = "TextCompletionScript";
    public static final String COLUMN_ID_TEXTCOM_SCRIPT = "Id_textcom_script";
    public static final String COLUMN_TEXTCOM_SCRIPT = "Textcom_script";
    public static final String TEXTCOMPLETION_QUESTION = "TextCompletionQuestion";
    public static final String COLUMN_ID_TEXTCOM_QUESTION = "Id_textcom_question";
    public static final String COLUMN_TEXTCOM_QUESTION = "Textcom_question";
    public static final String COLUMN_TEXTCOM_ANSWER = "Textcom_answer";
    public static final String COLUMN_ID_TEXTCOM_SCRIPT2 = "Id_textcom_script2";
    public static final String COLUMN_TEXTCOM_CHOICE_A = "Textcom_choice_a";
    public static final String COLUMN_TEXTCOM_CHOICE_B = "Textcom_choice_b";
    public static final String COLUMN_TEXTCOM_CHOICE_C = "Textcom_choice_c";
    public static final String COLUMN_TEXTCOM_CHOICE_D = "Textcom_choice_d";
    public static final String COLUMN_TEXTCOM_DES = "Textcom_des";

    public static final String TEXTCOMPLETION_SCRIPT_TEST = "TextCompletionScriptTest";
    public static final String COLUMN_ID_TEXTCOM_SCRIPT_TEST = "Id_textcom_script";
    public static final String COLUMN_TEXTCOM_SCRIPT_TEST = "Textcom_script";
    public static final String TEXTCOMPLETION_QUESTION_TEST = "TextCompletionQuestionTest";
    public static final String COLUMN_ID_TEXTCOM_QUESTION_TEST = "Id_textcom_question";
    public static final String COLUMN_TEXTCOM_QUESTION_TEST = "Textcom_question";
    public static final String COLUMN_TEXTCOM_ANSWER_TEST = "Textcom_answer";
    public static final String COLUMN_ID_TEXTCOM_SCRIPT2_TEST = "Id_textcom_script2";
    public static final String COLUMN_TEXTCOM_CHOICE_A_TEST = "Textcom_choice_a";
    public static final String COLUMN_TEXTCOM_CHOICE_B_TEST = "Textcom_choice_b";
    public static final String COLUMN_TEXTCOM_CHOICE_C_TEST = "Textcom_choice_c";
    public static final String COLUMN_TEXTCOM_CHOICE_D_TEST = "Textcom_choice_d";


    public TextCompletionDatabase (Context context, File dbname){

        objMyDatabase = new MyDatabase(context, dbname);
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLite = objMyDatabase.getReadableDatabase();

    }

    public long AddValuesToTextCompletionScript (String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_TEXTCOM_SCRIPT, strIdScript);
        objContentValues.put(COLUMN_TEXTCOM_SCRIPT, strScript);

        return writeSQLite.insert(TEXTCOMPLETION_SCRIPT, null, objContentValues);

    }

    public long AddvaluesToTextCompletionQuestion (String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strDes, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_TEXTCOM_QUESTION, strIdQuestion);
        objContentValues.put(COLUMN_TEXTCOM_QUESTION, strQuestion);
        objContentValues.put(COLUMN_TEXTCOM_ANSWER, strAnswer);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_A, strChoiceA);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_B, strChoiceB);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_C, strChoiceC);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_D, strChoiceD);
        objContentValues.put(COLUMN_TEXTCOM_DES, strDes);
        objContentValues.put(COLUMN_ID_TEXTCOM_SCRIPT2, strIdScript2);

        return writeSQLite.insert(TEXTCOMPLETION_QUESTION, null, objContentValues);

    }

    public long AddValuesToTextCompletionScriptTest (String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_TEXTCOM_SCRIPT_TEST, strIdScript);
        objContentValues.put(COLUMN_TEXTCOM_SCRIPT_TEST, strScript);

        return writeSQLite.insert(TEXTCOMPLETION_SCRIPT_TEST, null, objContentValues);

    }

    public long AddvaluesToTextCompletionQuestionTest (String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_TEXTCOM_QUESTION_TEST, strIdQuestion);
        objContentValues.put(COLUMN_TEXTCOM_QUESTION_TEST, strQuestion);
        objContentValues.put(COLUMN_TEXTCOM_ANSWER_TEST, strAnswer);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_A_TEST, strChoiceA);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_B_TEST, strChoiceB);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_C_TEST, strChoiceC);
        objContentValues.put(COLUMN_TEXTCOM_CHOICE_D_TEST, strChoiceD);
        objContentValues.put(COLUMN_ID_TEXTCOM_SCRIPT2_TEST, strIdScript2);

        return writeSQLite.insert(TEXTCOMPLETION_QUESTION_TEST, null, objContentValues);

    }


}
