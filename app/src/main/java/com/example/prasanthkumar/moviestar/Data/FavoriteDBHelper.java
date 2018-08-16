package com.example.prasanthkumar.moviestar.Data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static com.example.prasanthkumar.moviestar.UIScreens.MainActivity.TAG1;

public class FavoriteDBHelper  extends SQLiteOpenHelper
{
    private static final String DATABSE_NAME = "favorites.db";
    private static final int DATABSE_VERSION = 1;
    private static final String LOGTAG = "FAVORITE";

    public  static final String GET_FAVORITE_QUERY = "SELECT * FROM " + Favorites_Contract.FavoriteEntry.TABLE_NAME;
    public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + Favorites_Contract.FavoriteEntry.TABLE_NAME;

    private SQLiteOpenHelper dbHandler;
    private SQLiteDatabase db;
    private Context context;

    public FavoriteDBHelper(Context context) {
        super(context,DATABSE_NAME,null,DATABSE_VERSION);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG,"Database Closed");
        dbHandler.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + Favorites_Contract.FavoriteEntry.TABLE_NAME+" (" +
                        Favorites_Contract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_BACKDROP_IMG + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL );";

        try {
            db.execSQL(SQL_CREATE_FAVORITE_TABLE);
            this.onCreate(db);
            Toast.makeText(context, "Table created", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Log.d(TAG1,e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_QUERY);
        this.onCreate(db);
    }

    public boolean searchData(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int num = db.rawQuery("SELECT * FROM "+ Favorites_Contract.FavoriteEntry.TABLE_NAME +" where movieid="+ id, null).getCount();
        if (num == 0){
            return false;
        }else {
            return true;
        }
    }
}
