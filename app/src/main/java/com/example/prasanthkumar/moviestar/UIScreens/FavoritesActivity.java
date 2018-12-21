package com.example.prasanthkumar.moviestar.UIScreens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.Adapters.MovieAdapter;
import com.example.prasanthkumar.moviestar.Adapters.MovieAdapter_favorites;
import com.example.prasanthkumar.moviestar.CheckInternet.InternetConnection;
import com.example.prasanthkumar.moviestar.Data.FavoriteDBHelper;
import com.example.prasanthkumar.moviestar.Data.MovieStarContentProvider;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {

    @BindView(R.id.recycler_favorites)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh_id)
    SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteDBHelper favoriteMoviesSQLiteDB;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private MovieAdapter_favorites adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        favoriteMoviesSQLiteDB = new FavoriteDBHelper(this);
        checkInternet();

    }

    @SuppressLint("ResourceAsColor")
    private void checkInternet() {
        if (InternetConnection.isNetworkAvailable(Objects.requireNonNull(this))){
            initViews();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Need Permissions");
            builder.setMessage(getString(R.string.permissions));
            builder.setPositiveButton(getString(R.string.goto_settings_positive_btn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getAllFav();
                }
            });
            builder.show();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initViews() {

        adapter = new MovieAdapter_favorites(this, movieList,mRecyclerView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getAllFav();
    }

    @SuppressLint("NewApi")
    private void getAllFav() {
        SQLiteDatabase sqLiteDatabase = favoriteMoviesSQLiteDB.getReadableDatabase();
        cursor = getContentResolver().query(MovieStarContentProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()){

            Movie fav_Movie =
                    new Movie(cursor.getString(2),
                            cursor.getString(5),
                            cursor.getString(7),
                            cursor.getDouble(3),
                            cursor.getString(4),
                            cursor.getInt(1),
                            cursor.getString(6)
                    );
            movieList.add(fav_Movie);
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.fav_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.fav_reset)
        {
            movieList.clear();
            Toast.makeText(this, "Reset of Favorites done", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
