package com.example.prasanthkumar.moviestar.UIScreens;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.CastsAdapter;
import com.example.prasanthkumar.moviestar.Adapters.PostersAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Cast;
import com.example.prasanthkumar.moviestar.Model.CastResponse;
import com.example.prasanthkumar.moviestar.Model.GalleryResponse;
import com.example.prasanthkumar.moviestar.Model.Gallery_Posters;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostersActivity extends AppCompatActivity {

    private static final  String id_key ="id";
    private static final  String originalTitle_key ="original_title";
    int id;
    String original_Title;
    @BindView(R.id.gallary_recyclerview) RecyclerView recyclerView;
    private List<Gallery_Posters> postersList;
   /* private int position;
    private static final String SAVED_LAYOUT_MANAGER = "SavedLayoutManager" ;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
        Toast.makeText(this, "id:"+id +"\n"+original_Title, Toast.LENGTH_SHORT).show();
        setTitle(original_Title+"'s"+" Posters");

        postersList = new ArrayList<>();
        PostersAdapter adapter = new PostersAdapter(this, postersList);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }

       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //recyclerView.scrollToPosition(position);
        adapter.notifyDataSetChanged();
        loadPosters(id);

    }

    private void loadPosters(int id) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(this, "Please get your API key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Client client = new Client();
                Service api_service = client.getClient().create(Service.class);
                Call<GalleryResponse> call = api_service.getMovieImages(id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
                call.enqueue(new Callback<GalleryResponse>() {
                    @Override
                    public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                        List<Gallery_Posters> postersList = response.body().getPosters();
                        recyclerView.setAdapter(new PostersAdapter(getApplicationContext(), postersList));
                    }

                    @Override
                    public void onFailure(Call<GalleryResponse> call, Throwable t) {
                        Toast.makeText(PostersActivity.this, "Error in Fetching in Posters Data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        position = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(SAVED_LAYOUT_MANAGER, position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        savedInstanceState.putInt(SAVED_LAYOUT_MANAGER, position);
        super.onRestoreInstanceState(savedInstanceState);
    }
*/
}
