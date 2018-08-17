package com.example.prasanthkumar.moviestar.UIScreens;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.CastsAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Cast;
import com.example.prasanthkumar.moviestar.Model.CastResponse;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Crew extends AppCompatActivity {

    @BindView(R.id.casts_recyclerview) RecyclerView recyclerView_crew;
    private CastsAdapter castsAdapter;
    private List<Cast> castsList;
    private static final String id_key = "id";
    private static final String TAG = Crew.class.getSimpleName();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {

        castsList = new ArrayList<Cast>();
        castsAdapter = new CastsAdapter(this, castsList);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView_crew.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView_crew.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView_crew.setAdapter(castsAdapter);
        castsAdapter.notifyDataSetChanged();

        loadCrewData();

    }

    private void loadCrewData() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt(id_key);

        Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(this, "Please obtaine your new api key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Service apiService = client.getClient().create(Service.class);
            Call<CastResponse> calla = apiService.getMovieCasts(id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            calla.enqueue(new Callback<CastResponse>() {
                @Override
                public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {

                    List<Cast> crewList = response.body().getCast();
                    Log.d(TAG, "" + crewList.size());
                    recyclerView_crew.setAdapter(new CastsAdapter(Crew.this, crewList));
                    recyclerView_crew.smoothScrollToPosition(0);
                     Toast.makeText(Crew.this, ""+crewList.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<CastResponse> call, Throwable t) {
                    Toast.makeText(Crew.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "" + e);
        }
    }
}
