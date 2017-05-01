package com.xyz.practiceandlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.security.PublicKey;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 1/16/2017.
 */

public class PhotoDatabase {

    private MyDatabase objMyDatabase;
    private SQLiteDatabase writeSQLite , readSQLite;

    public static final String PHOTOGRAPHS_QUESTION = "PhotographsQuestion";
    public static final String COLUMN_ID_PHOTO_QUESTION = "Id_photo_question";
    public static final String COLUMN_PHOTO_ANSWER = "Photo_answer";
    public static final String PHOTOGRAPHS_CHOICE = "PhotographsChoice";
    public static final String COLUMN_ID_PHOTO_CHOICE = "Id_photo_choice";
    public static final String COLUMN_PHOTO_CHOICE_A = "Photo_choice_a";
    public static final String COLUMN_PHOTO_CHOICE_B = "Photo_choice_b";
    public static final String COLUMN_PHOTO_CHOICE_C = "Photo_choice_c";
    public static final String COLUMN_PHOTO_CHOICE_D = "Photo_choice_d";
    public static final String COLUMN_PHOTO_DES = "Photo_des";
    public static final String COLUMN_ID_PHOTO_QUESTION2 = "Id_photo_question2";


    // TEST
    public static final String PHOTOGRAPHS_QUESTION_TEST = "PhotographsQuestionTest";
    public static final String COLUMN_ID_PHOTO_QUESTION_TEST = "Id_photo_question";
    public static final String COLUMN_PHOTO_ANSWER_TEST = "Photo_answer";
    public static final String PHOTOGRAPHS_CHOICE_TEST = "PhotographsChoiceTest";
    public static final String COLUMN_ID_PHOTO_CHOICE_TEST = "Id_photo_choice";
    public static final String COLUMN_PHOTO_CHOICE_A_TEST = "Photo_choice_a";
    public static final String COLUMN_PHOTO_CHOICE_B_TEST = "Photo_choice_b";
    public static final String COLUMN_PHOTO_CHOICE_C_TEST = "Photo_choice_c";
    public static final String COLUMN_PHOTO_CHOICE_D_TEST = "Photo_choice_d";
    public static final String COLUMN_ID_PHOTO_QUESTION2_TEST = "Id_photo_question2";

    public PhotoDatabase(Context context, File dbname){

        objMyDatabase = new MyDatabase(context, dbname);
        writeSQLite = objMyDatabase.getWritableDatabase();
        readSQLite = objMyDatabase.getReadableDatabase();

    }

    public long AddValuesToPhotographsQuestion(String strQuestion, String strAnswer){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_PHOTO_QUESTION,strQuestion);
        objContentValues.put(COLUMN_PHOTO_ANSWER,strAnswer);

        return writeSQLite.insert(PHOTOGRAPHS_QUESTION, null, objContentValues);
    }

    public long AddValuesToPhotographsChoice(String strIdChoice, String strChoiceA, String strChoiceB, String strChoiceC,
                                             String strChoiceD, String strChoiceDes, String strQuestion2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_PHOTO_CHOICE,strIdChoice);
        objContentValues.put(COLUMN_PHOTO_CHOICE_A,strChoiceA);
        objContentValues.put(COLUMN_PHOTO_CHOICE_B,strChoiceB);
        objContentValues.put(COLUMN_PHOTO_CHOICE_C,strChoiceC);
        objContentValues.put(COLUMN_PHOTO_CHOICE_D,strChoiceD);
        objContentValues.put(COLUMN_PHOTO_DES,strChoiceDes);
        objContentValues.put(COLUMN_ID_PHOTO_QUESTION2,strQuestion2);

        return  writeSQLite.insert(PHOTOGRAPHS_CHOICE, null, objContentValues);
    }

    public long AddValuesToPhotographsQuestionTest(String strQuestion, String strAnswer){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_PHOTO_QUESTION_TEST,strQuestion);
        objContentValues.put(COLUMN_PHOTO_ANSWER_TEST,strAnswer);

        return writeSQLite.insert(PHOTOGRAPHS_QUESTION_TEST, null, objContentValues);
    }

    public long AddValuesToPhotographsChoiceTest(String strIdChoice, String strChoiceA, String strChoiceB, String strChoiceC,
                                                 String strChoiceD, String strQuestion2){

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_PHOTO_CHOICE_TEST,strIdChoice);
        objContentValues.put(COLUMN_PHOTO_CHOICE_A_TEST,strChoiceA);
        objContentValues.put(COLUMN_PHOTO_CHOICE_B_TEST,strChoiceB);
        objContentValues.put(COLUMN_PHOTO_CHOICE_C_TEST,strChoiceC);
        objContentValues.put(COLUMN_PHOTO_CHOICE_D_TEST,strChoiceD);
        objContentValues.put(COLUMN_ID_PHOTO_QUESTION2_TEST,strQuestion2);

        return  writeSQLite.insert(PHOTOGRAPHS_CHOICE_TEST, null, objContentValues);
    }
}
