package com.example.prasanthkumar.moviestar.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Objects;


public class MovieStarContentProvider extends ContentProvider {

    private static final int TASK = 100;
    private static final String AUTHORITY = "com.example.prasanthkumar.moviestar";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    private static final String Path = Favorites_Contract.FavoriteEntry.TABLE_NAME;

    public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                                          .appendEncodedPath(Path)
                                          .build();
    private UriMatcher myUri = matcherUri();
    private SQLiteDatabase db;
    private FavoriteDBHelper favoriteDBHelperDB;

    private UriMatcher matcherUri()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Path, TASK);

        return uriMatcher;

    }

    public MovieStarContentProvider() {

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int mt = myUri.match(uri);
        db = favoriteDBHelperDB.getWritableDatabase();
        if (mt ==  TASK){
            long res = db.delete(Favorites_Contract.FavoriteEntry.TABLE_NAME,"movieid="+ Integer.parseInt(selection),null);

        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,null);

        return mt;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnuri = null;
        int mt = myUri.match(uri);
        db = favoriteDBHelperDB.getWritableDatabase();
        if (mt == TASK){
            long result = db.insert(Favorites_Contract.FavoriteEntry.TABLE_NAME, null, values);

            Log.d("data",String.valueOf(result));
            returnuri = ContentUris.withAppendedId(CONTENT_URI, result);

        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,null);

        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        favoriteDBHelperDB = new FavoriteDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");

        Cursor cursor = null;
        Uri returnUri = null;
        int mt = myUri.match(uri);
        db = favoriteDBHelperDB.getReadableDatabase();
        if (mt == TASK){
            cursor = db.query(Favorites_Contract.FavoriteEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
      //  throw new UnsupportedOperationException("Not yet implemented");
        return 0;
    }
}
