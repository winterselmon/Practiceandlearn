package com.xyz.practiceandlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 2/1/2017.
 */

public class ShortConDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String SHORTCONVERSATION_SCRIPT = "ShortConversationScript";
    public static final String COLUMN_ID_SHORTCONVERSATION_SCRIPT = "Id_shortc_script";
    public static final String COLUMN_SHORTCONVERSATION_SCRIPT = "Shortc_script";
    public static final String SHORTCONVERSATION_QUESTION = "ShortConversationQuestion";
    public static final String COLUMN_ID_SHORTCONVERSATION_QUESTION = "Id_shortc_question";
    public static final String COLUMN_SHORTCONVERSATION_QUESTION = "Shortc_question";
    public static final String COLUMN_SHORTCONVERSATION_ANSWER = "Shortc_answer";
    public static final String COLUMN_ID_SHORTCONVERSATION_SCRIPT2 = "Id_shortc_script2";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_A = "Shortc_choice_a";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_B = "Shortc_choice_b";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_C = "Shortc_choice_c";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_D = "Shortc_choice_d";
    public static final String COLUMN_SHORTCONVERSATION_DES = "shortc_des";


    public static final String SHORTCONVERSATION_SCRIPT_TEST = "ShortConversationScriptTest";
    public static final String COLUMN_ID_SHORTCONVERSATION_SCRIPT_TEST = "Id_shortc_script";
    public static final String COLUMN_SHORTCONVERSATION_SCRIPT_TEST = "Shortc_script";
    public static final String SHORTCONVERSATION_QUESTION_TEST = "ShortConversationQuestionTest";
    public static final String COLUMN_ID_SHORTCONVERSATION_QUESTION_TEST = "Id_shortc_question";
    public static final String COLUMN_SHORTCONVERSATION_QUESTION_TEST = "Shortc_question";
    public static final String COLUMN_SHORTCONVERSATION_ANSWER_TEST = "Shortc_answer";
    public static final String COLUMN_ID_SHORTCONVERSATION_SCRIPT2_TEST = "Id_shortc_script2";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_A_TEST = "Shortc_choice_a";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_B_TEST = "Shortc_choice_b";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_C_TEST = "Shortc_choice_c";
    public static final String COLUMN_SHORTCONVERSATION_CHOICE_D_TEST = "Shortc_choice_d";


    public ShortConDatabase(Context context, String dbname){

        objMyDatabase = new MyDatabase(context, dbname);
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLite = objMyDatabase.getReadableDatabase();
    }

    public long addValuesToShortConversationScript(String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTCONVERSATION_SCRIPT,strIdScript);
        objContentValues.put(COLUMN_SHORTCONVERSATION_SCRIPT, strScript);

        return writeSQLite.insert(SHORTCONVERSATION_SCRIPT, null, objContentValues);
    }

    public long addValuesToShortConversationQuestion(String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strDes, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTCONVERSATION_QUESTION,strIdQuestion);
        objContentValues.put(COLUMN_SHORTCONVERSATION_QUESTION,strQuestion);
        objContentValues.put(COLUMN_SHORTCONVERSATION_ANSWER,strAnswer);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_A, strChoiceA);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_B, strChoiceB);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_C, strChoiceC);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_D, strChoiceD);
        objContentValues.put(COLUMN_SHORTCONVERSATION_DES, strDes);
        objContentValues.put(COLUMN_ID_SHORTCONVERSATION_SCRIPT2,strIdScript2);

        return writeSQLite.insert(SHORTCONVERSATION_QUESTION, null, objContentValues);
    }

    public long addValuesToShortConversationScriptTest(String strIdScript, String strScript){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTCONVERSATION_SCRIPT_TEST,strIdScript);
        objContentValues.put(COLUMN_SHORTCONVERSATION_SCRIPT_TEST, strScript);

        return writeSQLite.insert(SHORTCONVERSATION_SCRIPT_TEST, null, objContentValues);
    }

    public long addValuesToShortConversationQuestionTest(String strIdQuestion, String strQuestion, String strAnswer, String strChoiceA, String strChoiceB, String strChoiceC, String strChoiceD, String strIdScript2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_SHORTCONVERSATION_QUESTION_TEST,strIdQuestion);
        objContentValues.put(COLUMN_SHORTCONVERSATION_QUESTION_TEST,strQuestion);
        objContentValues.put(COLUMN_SHORTCONVERSATION_ANSWER_TEST,strAnswer);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_A_TEST, strChoiceA);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_B_TEST, strChoiceB);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_C_TEST, strChoiceC);
        objContentValues.put(COLUMN_SHORTCONVERSATION_CHOICE_D_TEST, strChoiceD);
        objContentValues.put(COLUMN_ID_SHORTCONVERSATION_SCRIPT2_TEST,strIdScript2);

        return writeSQLite.insert(SHORTCONVERSATION_QUESTION_TEST, null, objContentValues);
    }


}
