package com.xyz.practiceandlearn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;

import static com.xyz.practiceandlearn.Global.basedir;

/**
 * Created by 10.10 on 1/16/2017.
 */

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TOEIC.db";
    public static final int DATABASE_VERSION = 1;
    public static final String PHOTOGRAPHS_QUESTION = "CREATE TABLE PhotographsQuestion(Id_photo_question INTEGER PRIMARY KEY,"+" Photo_answer TEXT)";
    public static final String PHOTOGRAPHS_CHOICE = "CREATE TABLE PhotographsChoice(Id_photo_choice INTEGER PRIMARY KEY, "+" Photo_choice_a TEXT, Photo_choice_b TEXT, Photo_choice_c TEXT, Photo_choice_d TEXT, Photo_des TEXT, Id_photo_question2 INTEGER)";
    public static final String QUESTIONANDRESPONSE_QUESTION = "CREATE TABLE QuestionAndResponseQuestion(Id_qandr_question INTEGER PRIMARY KEY, "+" Qandr_question TEXT, Qandr_answer TEXT)";
    public static final String QUESTIONANDRESPONSE_CHOICE = "CREATE TABLE QuestionAndResponse(Id_qandr_choice INTEGER PRIMARY KEY, "+" Qandr_choice_a TEXT, Qandr_choice_b TEXT, Qandr_choice_c TEXT, Qandr_des TEXT, Id_qandr_question2 INTEGER)";
    public static final String SHORTCONVERSATION_SCRIPT = "CREATE TABLE ShortConversationScript(Id_shortc_script INTEGER PRIMARY KEY, "+" Shortc_script TEXT)";
    public static final String SHORTCONVERSATION_QUESTION = "CREATE TABLE ShortConversationQuestion(Id_shortc_question INTEGER PRIMARY KEY, "+" Shortc_question TEXT, Shortc_answer TEXT, Shortc_choice_a TEXT, Shortc_choice_b TEXT, Shortc_choice_c TEXT, Shortc_choice_d TEXT, shortc_des TEXT,  Id_shortc_script2 INTEGER)";
    public static final String SHORTTALK_SCRIPT = "CREATE TABLE ShortTalkScript(Id_shorttalk_script INTEGER PRIMARY KEY, "+" Shorttalk_script TEXT)";
    public static final String SHORTTALK_QUESTION = "CREATE TABLE ShortTalkQuestion(Id_shorttalk_question INTEGER PRIMARY KEY, "+" Shorttalk_question TEXT, Shorttalk_answer TEXT, Shorttalk_choice_a TEXT, Shorttalk_choice_b TEXT, Shorttalk_choice_c TEXT, Shorttalk_choice_d TEXT, Shorttalk_des TEXT, Id_shorttalk_script2 INTEGER)";
    public static final String INCOMPLETE_SENTENCE_QUESTION = "CREATE TABLE IncompleteSentenceQuestion(Id_incomplete_question INTEGER PRIMARY KEY, "+" Incomplete_question TEXT, Incomplete_answer TEXT)";
    public static final String INCOMPLETE_SENTENCE_CHOICE = "CREATE TABLE IncompleteSentenceChoice(Id_incomplete_choice INTEGER PRIMARY KEY, "+" Incomplete_choice_a TEXT, Incomplete_choice_b TEXT, Incomplete_choice_c TEXT, Incomplete_choice_d TEXT, Incomplete_des TEXT, Id_incomplete_question2 INTEGER)";
    public static final String TEXTCOMPLETION_SCRIPT = "CREATE TABLE TextCompletionScript(Id_textcom_script INTEGER PRIMARY KEY, "+" Textcom_script TEXT)";
    public static final String TEXTCOMPLETION_QUESTION = "CREATE TABLE TextCompletionQuestion(Id_textcom_question INTEGER PRIMARY KEY, "+" Textcom_question TEXT, Textcom_answer TEXT, Textcom_choice_a TEXT, Textcom_choice_b TEXT, Textcom_choice_c TEXT, Textcom_choice_d TEXT, Textcom_des TEXT, Id_textcom_script2 INTEGER)";
    public static final String READING_COMPREHENSION_SCRIPT = "CREATE TABLE ReadingComprehensionScript(Id_reading_script INTEGER PRIMARY KEY, "+" Reading_script TEXT)";
    public static final String READING_COMPREHENSION_QUESTION = "CREATE TABLE ReadingComprehensionQuestion(Id_reading_question INTEGER PRIMARY KEY, "+" Reading_question TEXT, Reading_answer TEXT, Reading_choice_a TEXT, Reading_choice_b TEXT, Reading_choice_c TEXT, Reading_choice_d TEXT, Reading_des TEXT, Id_reading_script2 INTEGER)";

    // TEST
    public static final String PHOTOGRAPHS_QUESTION_TEST = "CREATE TABLE PhotographsQuestionTest(Id_photo_question INTEGER PRIMARY KEY,"+" Photo_answer TEXT)";
    public static final String PHOTOGRAPHS_CHOICE_TEST = "CREATE TABLE PhotographsChoiceTest(Id_photo_choice INTEGER PRIMARY KEY, "+" Photo_choice_a TEXT, Photo_choice_b TEXT, Photo_choice_c TEXT, Photo_choice_d TEXT, Id_photo_question2 INTEGER)";
    public static final String QUESTIONANDRESPONSE_QUESTION_TEST = "CREATE TABLE QuestionAndResponseQuestionTest(Id_qandr_question INTEGER PRIMARY KEY, "+" Qandr_question TEXT, Qandr_answer TEXT)";
    public static final String QUESTIONANDRESPONSE_CHOICE_TEST = "CREATE TABLE QuestionAndResponseChoiceTest(Id_qandr_choice INTEGER PRIMARY KEY, "+" Qandr_choice_a TEXT, Qandr_choice_b TEXT, Qandr_choice_c TEXT, Id_qandr_question2 INTEGER)";
    public static final String SHORTCONVERSATION_SCRIPT_TEST = "CREATE TABLE ShortConversationScriptTest(Id_shortc_script INTEGER PRIMARY KEY, "+" Shortc_script TEXT)";
    public static final String SHORTCONVERSATION_QUESTION_TEST = "CREATE TABLE ShortConversationQuestionTest(Id_shortc_question INTEGER PRIMARY KEY, "+" Shortc_question TEXT, Shortc_answer TEXT, Shortc_choice_a TEXT, Shortc_choice_b TEXT, Shortc_choice_c TEXT, Shortc_choice_d TEXT,  Id_shortc_script2 INTEGER)";
    public static final String SHORTTALK_SCRIPT_TEST = "CREATE TABLE ShortTalkScriptTest(Id_shorttalk_script INTEGER PRIMARY KEY, "+" Shorttalk_script TEXT)";
    public static final String SHORTTALK_QUESTION_TEST = "CREATE TABLE ShortTalkQuestionTest(Id_shorttalk_question INTEGER PRIMARY KEY, "+" Shorttalk_question TEXT, Shorttalk_answer TEXT, Shorttalk_choice_a TEXT, Shorttalk_choice_b TEXT, Shorttalk_choice_c TEXT, Shorttalk_choice_d TEXT, Id_shorttalk_script2 INTEGER)";
    public static final String INCOMPLETE_SENTENCE_QUESTION_TEST = "CREATE TABLE IncompleteSentenceQuestionTest(Id_incomplete_question INTEGER PRIMARY KEY, "+" Incomplete_question TEXT, Incomplete_answer TEXT)";
    public static final String INCOMPLETE_SENTENCE_CHOICE_TEST = "CREATE TABLE IncompleteSentenceChoiceTest(Id_incomplete_choice INTEGER PRIMARY KEY, "+" Incomplete_choice_a TEXT, Incomplete_choice_b TEXT, Incomplete_choice_c TEXT, Incomplete_choice_d TEXT, Id_incomplete_question2 INTEGER)";
    public static final String TEXTCOMPLETION_SCRIPT_TEST = "CREATE TABLE TextCompletionScriptTest(Id_textcom_script INTEGER PRIMARY KEY, "+" Textcom_script TEXT)";
    public static final String TEXTCOMPLETION_QUESTION_TEST = "CREATE TABLE TextCompletionQuestionTest(Id_textcom_question INTEGER PRIMARY KEY, "+" Textcom_question TEXT, Textcom_answer TEXT, Textcom_choice_a TEXT, Textcom_choice_b TEXT, Textcom_choice_c TEXT, Textcom_choice_d TEXT, Id_textcom_script2 INTEGER)";
    public static final String READING_COMPREHENSION_SCRIPT_TEST = "CREATE TABLE ReadingComprehensionScriptTest(Id_reading_script INTEGER PRIMARY KEY, "+" Reading_script TEXT)";
    public static final String READING_COMPREHENSION_QUESTION_TEST = "CREATE TABLE ReadingComprehensionQuestionTest(Id_reading_question INTEGER PRIMARY KEY, "+" Reading_question TEXT, Reading_answer TEXT, Reading_choice_a TEXT, Reading_choice_b TEXT, Reading_choice_c TEXT, Reading_choice_d TEXT, Id_reading_script2 INTEGER)";
    public String Photo_choice_a;

    //photo database



    public MyDatabase(Context context, File dbname) {
        super(context, String.valueOf(dbname), null, DATABASE_VERSION);
        //basedir.toString()+"/V1/TOEIC.db"
        //super(context,"/storage/sdcard/sdcard/V1/TOEIC.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //sqLiteDatabase.execSQL(PHOTOGRAPHS_QUESTION);
        //sqLiteDatabase.execSQL(PHOTOGRAPHS_CHOICE);
        //sqLiteDatabase.execSQL(QUESTIONANDRESPONSE_QUESTION);
        //sqLiteDatabase.execSQL(QUESTIONANDRESPONSE_CHOICE);
        //sqLiteDatabase.execSQL(SHORTCONVERSATION_SCRIPT);
        //sqLiteDatabase.execSQL(SHORTCONVERSATION_QUESTION);
        //sqLiteDatabase.execSQL(INCOMPLETE_SENTENCE_QUESTION);
        //sqLiteDatabase.execSQL(INCOMPLETE_SENTENCE_CHOICE);
        //sqLiteDatabase.execSQL(READING_COMPREHENSION_SCRIPT);
        //sqLiteDatabase.execSQL(READING_COMPREHENSION_QUESTION);
        //sqLiteDatabase.execSQL(SHORTTALK_SCRIPT);
        //sqLiteDatabase.execSQL(SHORTTALK_QUESTION);
        //sqLiteDatabase.execSQL(TEXTCOMPLETION_SCRIPT);
        //sqLiteDatabase.execSQL(TEXTCOMPLETION_QUESTION);


        //TEST
        //sqLiteDatabase.execSQL(PHOTOGRAPHS_QUESTION_TEST);
        //sqLiteDatabase.execSQL(PHOTOGRAPHS_CHOICE_TEST);
        //sqLiteDatabase.execSQL(QUESTIONANDRESPONSE_QUESTION_TEST);
        //sqLiteDatabase.execSQL(QUESTIONANDRESPONSE_CHOICE_TEST);
        //sqLiteDatabase.execSQL(SHORTCONVERSATION_SCRIPT_TEST);
        //sqLiteDatabase.execSQL(SHORTCONVERSATION_QUESTION_TEST);
        //sqLiteDatabase.execSQL(INCOMPLETE_SENTENCE_QUESTION_TEST);
        //sqLiteDatabase.execSQL(INCOMPLETE_SENTENCE_CHOICE_TEST);
        //sqLiteDatabase.execSQL(READING_COMPREHENSION_SCRIPT_TEST);
        //sqLiteDatabase.execSQL(READING_COMPREHENSION_QUESTION_TEST);
        //sqLiteDatabase.execSQL(SHORTTALK_SCRIPT_TEST);
        //sqLiteDatabase.execSQL(SHORTTALK_QUESTION_TEST);
        //sqLiteDatabase.execSQL(TEXTCOMPLETION_SCRIPT_TEST);
        //sqLiteDatabase.execSQL(TEXTCOMPLETION_QUESTION_TEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
