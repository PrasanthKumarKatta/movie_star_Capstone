package com.example.prasanthkumar.moviestar.NetworkUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.Adapters.MovieAdapter;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.Model.MoviesResponse;
import com.example.prasanthkumar.moviestar.UIScreens.Home;
import com.example.prasanthkumar.moviestar.UIScreens.MainActivity;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TeluguAsyncTask extends AsyncTask<String, Void, String> {
    private final String teluguurl = "https://api.themoviedb.org/3/discover/movie?api_key=09aedacfebd0c729aff684f082080554&with_original_language=te";
    private static final String results = "results";
    private static final String id = "id";
    private static final String vote_count = "vote_count";
    private static final String video = "video";
    private static final String vote_average = "vote_average";
    private static final String title = "title";
    private static final String popularity = "popularity";
    private static final String poster_path = "poster_path";
    private static final String original_language = "original_language";
    private static final String original_title = "original_title";
    private static final String backdrop_path = "backdrop_path";
    private static final String adult = "adult";
    private static final String overview = "overview";
    private static final String release_date = "release_date";

    private Context context;

    RecyclerView rv1;

    public TeluguAsyncTask(FragmentActivity home, RecyclerView mRecyclerView1) {
        this.context = home;
        this.rv1=mRecyclerView1;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(teluguurl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ArrayList<Movie> arrayList = new ArrayList<>();
        if (s != null) {
            try {
                JSONObject rootObject = new JSONObject(s);
                JSONArray resultsArray = rootObject.getJSONArray(results);
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsonObject = resultsArray.getJSONObject(i);

                    arrayList.add(new Movie(jsonObject.getString(original_title),
                            jsonObject.getString(poster_path),
                            jsonObject.getString(overview),
                            Double.parseDouble(jsonObject.getString(vote_average)),
                            jsonObject.getString(release_date),
                            jsonObject.getInt(id),
                            jsonObject.getString(backdrop_path)));

                }
                rv1.setAdapter(new MovieAdapter(context, arrayList));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, "sorry!..", Toast.LENGTH_SHORT).show();
        }
    }
}
