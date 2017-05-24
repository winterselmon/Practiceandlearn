package com.xyz.practiceandlearn.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xyz.practiceandlearn.Database.MyDatabase;

import java.io.File;

/**
 * Created by 10.10 on 2/24/2017.
 */

public class ShortTalkDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite, readSQLlte;

    public static final String SHORTTALK_SCRIPT = "ShortTalkScript";
    public static final String COLUMN_ID_SHORTTALK_SCRIPT = "Id_shorttalk_script";
    public static final String COLUMN_SHORTTALK_SCRIPT = "Shorttalk_script";
    public static final String SHORTTALK_QUESTION = "ShortTalkQuestion";
    public static final String COLUMN_ID_SHORTTALK_QUESTION = "Id_shorttalk_question";
    public static final String COLUMN_SHORTTALK_QUESTION = "Shorttalk_question";
    public static final String COLUMN_SHORTTALK_ANSWER = "Shorttalk_answer";
    public static final String COLUMN_ID_SHORTTALK_SCRIPT2 = "Id_shorttalk_script2";
    public static final String COLUMN_SHORTTALK_CHOICE_A = "Shorttalk_choice_a";
    public static final String COLUMN_SHORTTALK_CHOICE_B = "Shorttalk_choice_b";
    public static final String COLUMN_SHORTTALK_CHOICE_C = "Shorttalk_choice_c";
    public static final String COLUMN_SHORTTALK_CHOICE_D = "Shorttalk_choice_d";
    public static final String COLUMN_SHORTTALK_DES = "Shorttalk_des";
    public static final String COLUMN_VERSION = "Version";

    public static final String SHORTTALK_SCRIPT_TEST = "ShortTalkScriptTest";
    public static final String COLUMN_ID_SHORTTALK_SCRIPT_TEST = "Id_shorttalk_script";
    public static final String COLUMN_SHORTTALK_SCRIPT_TEST = "Shorttalk_script";
    public static final String SHORTTALK_QUESTION_TEST = "ShortTalkQuestionTest";
    public static final String COLUMN_ID_SHORTTALK_QUESTION_TEST = "Id_shorttalk_question";
    public static final String COLUMN_SHORTTALK_QUESTION_TEST = "Shorttalk_question";
    public static final String COLUMN_SHORTTALK_ANSWER_TEST = "Shorttalk_answer";
    public static final String COLUMN_ID_SHORTTALK_SCRIPT2_TEST = "Id_shorttalk_script2";
    public static final String COLUMN_SHORTTALK_CHOICE_A_TEST = "Shorttalk_choice_a";
    public static final String COLUMN_SHORTTALK_CHOICE_B_TEST = "Shorttalk_choice_b";
    public static final String COLUMN_SHORTTALK_CHOICE_C_TEST = "Shorttalk_choice_c";
    public static final String COLUMN_SHORTTALK_CHOICE_D_TEST = "Shorttalk_choice_d";
    public static final String COLUMN_VERSION_TEST = "Version";

    public ShortTalkDatabase(Context context, File dbname){

        objMyDatabase = new MyDatabase(context, dbname);
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLlte = objMyDatabase.getReadableDatabase();

    }

    public long AddValuesToShortTalkScript (String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTTALK_SCRIPT, strIdScript);
        objContentValues.put(COLUMN_SHORTTALK_SCRIPT, strScript);

        return writeSQLite.insert(SHORTTALK_SCRIPT, null, objContentValues);

    }

    public long AddValuesToShortTalkQuestion (String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strDes, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTTALK_QUESTION, strIdQuestion);
        objContentValues.put(COLUMN_SHORTTALK_QUESTION, strQuestion);
        objContentValues.put(COLUMN_SHORTTALK_ANSWER, strAnswer);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_A, strChoiceA);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_B, strChoiceB);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_C, strChoiceC);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_D, strChoiceD);
        objContentValues.put(COLUMN_SHORTTALK_DES, strDes);
        objContentValues.put(COLUMN_ID_SHORTTALK_SCRIPT2, strIdScript2);

        return writeSQLite.insert(SHORTTALK_QUESTION, null, objContentValues);

    }


    public long AddValuesToShortTalkScriptTest (String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTTALK_SCRIPT_TEST, strIdScript);
        objContentValues.put(COLUMN_SHORTTALK_SCRIPT_TEST, strScript);

        return writeSQLite.insert(SHORTTALK_SCRIPT_TEST, null, objContentValues);

    }

    public long AddValuesToShortTalkQuestionTest (String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTTALK_QUESTION_TEST, strIdQuestion);
        objContentValues.put(COLUMN_SHORTTALK_QUESTION_TEST, strQuestion);
        objContentValues.put(COLUMN_SHORTTALK_ANSWER_TEST, strAnswer);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_A_TEST, strChoiceA);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_B_TEST, strChoiceB);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_C_TEST, strChoiceC);
        objContentValues.put(COLUMN_SHORTTALK_CHOICE_D_TEST, strChoiceD);
        objContentValues.put(COLUMN_ID_SHORTTALK_SCRIPT2_TEST, strIdScript2);

        return writeSQLite.insert(SHORTTALK_QUESTION_TEST, null, objContentValues);

    }




}
