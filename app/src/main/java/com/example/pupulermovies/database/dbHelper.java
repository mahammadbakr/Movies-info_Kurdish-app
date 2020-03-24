package com.example.pupulermovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="movies.db";
    private static final int DATABASE_VERSION= 1;

    public static final String TABLE_NAME = "movies";
    public static final String TABLE_NAME2 = "bookmark";

    public static final String COLUMN_MOVIES_ID = "id";
    public static final String COLUMN_MOVIES_TITLE = "title";
    public static final String COLUMN_MOVIES_OVERVIEW = "overview";
    public static final String COLUMN_MOVIES_IMAGE = "img";
    public static final String COLUMN_MOVIES_DATE = "date";
    public static final String COLUMN_MOVIES_POPULARITY = "popularity";
    public static final String COLUMN_MOVIES_VOTEAVARAGE = "vote";
    public static final String COLUMN_MOVIES_FIRSTTYPE = "first";

    //###############################################################
    public static final String COLUMN_BOOKMARK_ID = "id";
    public static final String COLUMN_BOOKMARK_TITLE = "title";
    public static final String COLUMN_BOOKMARK_OVERVIEW = "overview";
    public static final String COLUMN_BOOKMARK_IMAGE = "img";
    public static final String COLUMN_BOOKMARK_DATE = "date";
    public static final String COLUMN_BOOKMARK_POPULARITY = "popularity";
    public static final String COLUMN_BOOKMARK_VOTEAVARAGE = "vote";
    public static final String COLUMN_BOOKMARK_FIRSTTYPE = "first";




    public dbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORKERS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_MOVIES_ID + " INTEGER ," +
                COLUMN_MOVIES_TITLE  + " TEXT," +
                COLUMN_MOVIES_OVERVIEW + " TEXT," +
                COLUMN_MOVIES_IMAGE +" TEXT,"+
                COLUMN_MOVIES_DATE +" TEXT,"+
                COLUMN_MOVIES_POPULARITY +" TEXT,"+
                COLUMN_MOVIES_VOTEAVARAGE +" TEXT,"+
                COLUMN_MOVIES_FIRSTTYPE +" INTEGER"+
                ")";

        String CREATE_BOOKMARK_TABLE = "CREATE TABLE " + TABLE_NAME2 +
                "(" +
                COLUMN_BOOKMARK_ID + " INTEGER ," +
                COLUMN_BOOKMARK_TITLE  + " TEXT," +
                COLUMN_BOOKMARK_OVERVIEW + " TEXT," +
                COLUMN_BOOKMARK_IMAGE +" TEXT,"+
                COLUMN_BOOKMARK_DATE +" TEXT,"+
                COLUMN_BOOKMARK_POPULARITY +" TEXT,"+
                COLUMN_BOOKMARK_VOTEAVARAGE +" TEXT,"+
                COLUMN_BOOKMARK_FIRSTTYPE +" INTEGER"+
                ")";

        db.execSQL(CREATE_WORKERS_TABLE);
        db.execSQL(CREATE_BOOKMARK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME +";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2 +";");

        // Create tables again
        onCreate(db);
    }


}
