package com.example.prasanthkumar.moviestar.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MovieStarContentProvider extends ContentProvider {
    public static final int TASK =100;
    public static final String AUTHORITY = "com.example.prasanthkumar.moviestar";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY );
    public static final String PATH = Favorites_Contract.FavoriteEntry.TABLE_NAME;

    public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

    FavoriteDBHelper favoriteMoviesSQLiteDB;
    UriMatcher myUri = matcherUri();
    SQLiteDatabase db;

    private UriMatcher matcherUri() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, TASK);
        return  uriMatcher;
    }

    public MovieStarContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int mt = myUri.match(uri);
        db = favoriteMoviesSQLiteDB.getWritableDatabase();
        if (mt == TASK){
            long res = db.delete(Favorites_Contract.FavoriteEntry.TABLE_NAME,"movieid=" + Integer.parseInt(selection), null );

        } else {

        }
        getContext().getContentResolver().notifyChange(uri,null);

        // Implement this to handle requests to delete one or more rows.
        //new UnsupportedOperationException("Not yet implemented");
        return mt;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnUri = null;
        int mt = myUri.match(uri);
        db = favoriteMoviesSQLiteDB.getWritableDatabase();
        if (mt == TASK){
            long result = db.insert(Favorites_Contract.FavoriteEntry.TABLE_NAME,null,values);

            Log.d("data", String.valueOf(result));
            returnUri = ContentUris.withAppendedId(CONTENT_URI, result);
        } else {

        }
        getContext().getContentResolver().notifyChange(uri,null);

        // TODO: Implement this to handle requests to insert a new row.
        //  throw new UnsupportedOperationException("Not yet implemented");

        return  uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        favoriteMoviesSQLiteDB = new FavoriteDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //  throw new UnsupportedOperationException("Not yet implemented");
        Cursor cursor = null;
        Uri returnUri = null;
        int mt = myUri.match(uri);
        db = favoriteMoviesSQLiteDB.getReadableDatabase();
        if (mt == TASK){
            cursor = db.query(Favorites_Contract.FavoriteEntry.TABLE_NAME,projection,selection,selectionArgs,null,null, sortOrder);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        //  throw new UnsupportedOperationException("Not yet implemented");
        return 0;
    }
}
