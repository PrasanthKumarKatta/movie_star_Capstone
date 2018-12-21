package com.example.prasanthkumar.moviestar.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.UIScreens.Home;

import java.util.ArrayList;
import java.util.List;

import static com.example.prasanthkumar.moviestar.UIScreens.MainActivity.TAG1;

public class FavoriteDBHelper  extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "moviestar.db";
    public static final int DATABASE_Version = 1;
    private static final String LOGTAG = "FAVORITE";

    public static final String GET_FAVORITE_QUERY = "SELECT * FROM " + Favorites_Contract.FavoriteEntry.TABLE_NAME;
    public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + Favorites_Contract.FavoriteEntry.TABLE_NAME;

    Context ctx;
    SQLiteDatabase db;
    SQLiteOpenHelper dbHandler;

    public FavoriteDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }

    public void open()
    {
        Log.i(LOGTAG,"Database Opened");
        db = dbHandler.getWritableDatabase();
    }

    public void close()
    {
        Log.i(LOGTAG,"Database Closed");
        dbHandler.close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + Favorites_Contract.FavoriteEntry.TABLE_NAME + "(" +
                        Favorites_Contract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_BACKDROP_IMG + " TEXT NOT NULL, " +
                        Favorites_Contract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL);" ;
        try {
            db.execSQL(SQL_CREATE_FAVORITE_TABLE);
            this.onCreate(db);
           // Toast.makeText(ctx, "Table created", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
           // Log.d(TAG1, e.getMessage());
           // Toast.makeText(ctx, "error:sqldb:"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_QUERY);
        this.onCreate(db);
    }

    public void addFavorite(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID,movie.getId());
        values.put(Favorites_Contract.FavoriteEntry.COLUMN_TITLE, movie.getOriginal_title());
        values.put(Favorites_Contract.FavoriteEntry.COLUMN_USERRATING, movie.getVoteAverage());
        values.put(Favorites_Contract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(Favorites_Contract.FavoriteEntry.COLUMN_BACKDROP_IMG, movie.getBackdrop_path());
        values.put(Favorites_Contract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(Favorites_Contract.FavoriteEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteFavorite(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Favorites_Contract.FavoriteEntry.TABLE_NAME, Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID + "=" + id, null);

    }

    public List<Movie> getAllFavorites()
    {
        String[] clumns = {
                Favorites_Contract.FavoriteEntry._ID,
                Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID,
                Favorites_Contract.FavoriteEntry.COLUMN_TITLE,
                Favorites_Contract.FavoriteEntry.COLUMN_USERRATING,
                Favorites_Contract.FavoriteEntry.COLUMN_POSTER_PATH,
                Favorites_Contract.FavoriteEntry.COLUMN_BACKDROP_IMG,
                Favorites_Contract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
                };
        String sortOrder = Favorites_Contract.FavoriteEntry._ID + "ASC";
        List<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(GET_FAVORITE_QUERY,null);
            if (cursor.moveToFirst())
            {
                do {
                    Movie movie = new Movie();
                    movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID))));
                    movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_TITLE)));
                    movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_USERRATING))));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_RELEASEDATE)));
                    movie.setPosterPath(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_POSTER_PATH)));
                    movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_BACKDROP_IMG)));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex(Favorites_Contract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));

                    favoriteList.add(movie);

                }while (cursor.moveToNext());

            }
            cursor.close();
            db.close();
            return favoriteList;
    }

    public boolean search(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
       // int num = db.rawQuery(GET_FAVORITE_QUERY + " where movieid=" + id, null).getCount();
        int num = db.rawQuery("SELECT * FROM "+ Favorites_Contract.FavoriteEntry.TABLE_NAME +" where movieid="+ id, null).getCount();

        if (num == 0)
        {
            return  false;
        } else {
            return true;
        }
    }
}
