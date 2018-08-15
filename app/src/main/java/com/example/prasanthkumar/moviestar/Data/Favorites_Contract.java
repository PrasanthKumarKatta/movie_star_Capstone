package com.example.prasanthkumar.moviestar.Data;

import android.provider.BaseColumns;

public class Favorites_Contract {
    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";
        public static final String COLUMN_BACKDROP_IMG = "backdrop";

    }
}