package ru.ifmo.md.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Алексей on 11.11.2014.
 */
public class CandidateDBHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DB_NAME = "alex700candidate";
    public static final int DB_VERSION = 2;

    //request for CANDIDATE
    public static final String CANDIDATE_TABLE_NAME = "candidates";
    public static final String CANDIDATE_NAME = "name";
    public static final String CANDIDATE_COUNT = "count";
    public static final String CANDIDATE_ID = _ID;

    public static final String CREATE_CANDIDATE_TABLE_REQUEST = "CREATE TABLE " +
            CANDIDATE_TABLE_NAME + "(" +
            CANDIDATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CANDIDATE_NAME + " TEXT," +
            CANDIDATE_COUNT + " INTEGER)";


    public CandidateDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CANDIDATE_TABLE_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CANDIDATE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_key=ON");
    }

    public void deleteTable(SQLiteDatabase db) {
        db.delete(CANDIDATE_TABLE_NAME, null, null);
    }
}
