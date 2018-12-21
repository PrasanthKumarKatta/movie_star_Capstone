package com.example.prasanthkumar.moviestar.UIScreens;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {
    @BindView(R.id.recycler_favorites)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh_id)
    SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteDBHelper favoriteMoviesSQLiteDB;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private MovieAdapter_favorites adapter;
    private Cursor cursor;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this,view);
        favoriteMoviesSQLiteDB = new FavoriteDBHelper(getContext());
        initViews();
        return view;

    }

    @SuppressLint("ResourceAsColor")
    private void initViews() {

        adapter = new MovieAdapter_favorites(getContext(), movieList,mRecyclerView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        }
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getAllFav();

    }

    @SuppressLint("NewApi")
    private void getAllFav() {
        SQLiteDatabase sqLiteDatabase = favoriteMoviesSQLiteDB.getReadableDatabase();
        cursor = getContext().getContentResolver().query(MovieStarContentProvider.CONTENT_URI, null, null, null, null);
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
        // getAllFav();
    }

}
