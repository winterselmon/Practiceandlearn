package com.xyz.practiceandlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 1/21/2017.
 */

public class QuestionAndResponseDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String QuestionAndResponse_Question = "QuestionAndResponseQuestion";
    public static final String COLUMN_ID_QANDR_QUESTION = "Id_qandr_question";
    public static final String COLUMN_QANDR_QUESTION = "Qandr_question";
    public static final String COLUMN_QANDR_ANSWER = "Qandr_answer";
    public static final String QUESTIONANDRESPONSE_CHOICE = "QuestionAndResponse";
    public static final String COLUMN_ID_QANDR_CHOICE = "Id_qandr_choice";
    public static final String COLUMN_QANDR_CHOICE_A = "Qandr_choice_a";
    public static final String COLUMN_QANDR_CHOICE_B = "Qandr_choice_b";
    public static final String COLUMN_QANDR_CHOICE_C = "Qandr_choice_c";
    public static final String COLUMN_QANDR_DES = "Qandr_des";
    public static final String COLUMN_ID_QANDR_QUESTION2 = "Id_qandr_question2";


    public static final String QuestionAndResponse_Question_TEST = "QuestionAndResponseQuestionTest";
    public static final String COLUMN_ID_QANDR_QUESTION_TEST = "Id_qandr_question";
    public static final String COLUMN_QANDR_QUESTION_TEST = "Qandr_question";
    public static final String COLUMN_QANDR_ANSWER_TEST = "Qandr_answer";
    public static final String QUESTIONANDRESPONSE_CHOICE_TEST = "QuestionAndResponseChoiceTest";
    public static final String COLUMN_ID_QANDR_CHOICE_TEST = "Id_qandr_choice";
    public static final String COLUMN_QANDR_CHOICE_A_TEST = "Qandr_choice_a";
    public static final String COLUMN_QANDR_CHOICE_B_TEST = "Qandr_choice_b";
    public static final String COLUMN_QANDR_CHOICE_C_TEST = "Qandr_choice_c";
    public static final String COLUMN_ID_QANDR_QUESTION2_TEST = "Id_qandr_question2";

    public QuestionAndResponseDatabase(Context context){

        objMyDatabase = new MyDatabase(context, basedir.toString()+"/V1/TOEIC.db");
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLite = objMyDatabase.getReadableDatabase();

    }

    public long addValuseToQuestionAndResponseQuestion(String strIdQuesion, String strQuestion, String strAnswer){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_QANDR_QUESTION, strIdQuesion);
        objContentValues.put(COLUMN_QANDR_QUESTION, strQuestion);
        objContentValues.put(COLUMN_QANDR_ANSWER, strAnswer);

        return writeSQLite.insert(QuestionAndResponse_Question, null, objContentValues);

    }

    public long addValuseToQuestionAndResponseChoice(String strIdChoice, String strChoiceA, String strChoiceB, String strChoiceC, String strDes, String strIdQuestion2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_QANDR_CHOICE, strIdChoice);
        objContentValues.put(COLUMN_QANDR_CHOICE_A, strChoiceA);
        objContentValues.put(COLUMN_QANDR_CHOICE_B, strChoiceB);
        objContentValues.put(COLUMN_QANDR_CHOICE_C, strChoiceC);
        objContentValues.put(COLUMN_QANDR_DES, strDes);
        objContentValues.put(COLUMN_ID_QANDR_QUESTION2, strIdQuestion2);

        return writeSQLite.insert(QUESTIONANDRESPONSE_CHOICE, null, objContentValues);

    }

    public long addValuseToQuestionAndResponseQuestionTest(String strIdQuestion, String strQuestion, String strAnswer){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_QANDR_QUESTION_TEST, strIdQuestion);
        objContentValues.put(COLUMN_QANDR_QUESTION_TEST, strQuestion);
        objContentValues.put(COLUMN_QANDR_ANSWER_TEST, strAnswer);

        return writeSQLite.insert(QuestionAndResponse_Question_TEST, null, objContentValues);

    }

    public long addValuseToQuestionAndResponseChoiceTest(String strIdChoice, String strChoiceA, String strChoiceB, String strChoiceC, String strIdQuestion2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_QANDR_CHOICE_TEST, strIdChoice);
        objContentValues.put(COLUMN_QANDR_CHOICE_A_TEST, strChoiceA);
        objContentValues.put(COLUMN_QANDR_CHOICE_B_TEST, strChoiceB);
        objContentValues.put(COLUMN_QANDR_CHOICE_C_TEST, strChoiceC);
        objContentValues.put(COLUMN_ID_QANDR_QUESTION2_TEST, strIdQuestion2);

        return writeSQLite.insert(QUESTIONANDRESPONSE_CHOICE_TEST, null, objContentValues);

    }

}
