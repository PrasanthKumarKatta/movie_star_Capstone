package com.example.prasanthkumar.moviestar.UIScreens;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.PostersAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.GalleryResponse;
import com.example.prasanthkumar.moviestar.Model.Gallery_Posters;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostersActivity extends AppCompatActivity {

    private static final  String id_key ="id";
    private static final  String originalTitle_key ="original_title";
    private int id;
    private String original_Title;
    @BindView(R.id.gallary_recyclerview) StatefulRecyclerView recyclerView;
    private List<Gallery_Posters> postersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        id = Objects.requireNonNull(intent.getExtras()).getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
        setTitle(original_Title+"'s"+" Posters");

        postersList = new ArrayList<>();
        PostersAdapter adapter = new PostersAdapter(this, postersList);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();
        loadPosters(id);

    }

    private void loadPosters(int id) {
        try {
            Client client = new Client();
            Service api_service = Client.getClient().create(Service.class);
            Call<GalleryResponse> call = api_service.getMovieImages(id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<GalleryResponse>() {
                @Override
                public void onResponse(@NonNull Call<GalleryResponse> call, @NonNull Response<GalleryResponse> response) {
                    List<Gallery_Posters> postersList = Objects.requireNonNull(response.body()).getPosters();
                    recyclerView.setAdapter(new PostersAdapter(getApplicationContext(), postersList));
                }

                @Override
                public void onFailure(Call<GalleryResponse> call, Throwable t) {
                    Toast.makeText(PostersActivity.this, R.string.posterErrorToast, Toast.LENGTH_SHORT).show();
                }
            });
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

}
