package com.example.prasanthkumar.moviestar.UIScreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.TrailerAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Trailer;
import com.example.prasanthkumar.moviestar.Model.TrailerResponse;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersActivity extends AppCompatActivity {

    private static final  String id_key ="id";
    private static final  String originalTitle_key ="original_title";
    int id;
    String original_Title;
    @BindView(R.id.trailer_recyclerview) RecyclerView recyclerView;
    private  List<Trailer> trailerList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        ButterKnife.bind(this);
        /*Intent intent = getIntent();
        setTitle(original_Title+"'s"+" Trailers");
       */
        initviews();


    }

    private void initviews() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
        Toast.makeText(this, "id:"+id +"\n"+original_Title, Toast.LENGTH_SHORT).show();
        setTitle(original_Title+"'s"+" Trailers");

        trailerList = new ArrayList<>();
        TrailerAdapter adapter = new TrailerAdapter(this,trailerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.scrollToPosition(position);
        adapter.notifyDataSetChanged();
        loadTrailers(id);
    }

    private void loadTrailers(int movie_id) {
        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(this, "Please get your API key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Service api_Service =client.getClient().create(Service.class);
            Call<TrailerResponse> call = api_Service.getMovieTrailer(movie_id,BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailer = response.body().getResults();

                    //  Toast.makeText(DetailsActivity.this, ""+trailer, Toast.LENGTH_SHORT).show();
                    Log.d("JsonResponse",trailer.toString());

                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailer));
                    //recyclerView.smoothScrollToPosition(0);
                    //recyclerView.scrollToPosition(position);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(TrailersActivity.this, "Error Fetching Trailer Data", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
