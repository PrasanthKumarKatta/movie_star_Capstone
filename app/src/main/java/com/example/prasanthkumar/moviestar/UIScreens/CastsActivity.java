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
import com.example.prasanthkumar.moviestar.Adapters.CastsAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Cast;
import com.example.prasanthkumar.moviestar.Model.CastResponse;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastsActivity extends AppCompatActivity {

    private static final String id_key = "id";
    private static final String originalTitle_key = "original_title";
    private int id;
    private String original_Title;
    @BindView(R.id.casts_recyclerview)
    StatefulRecyclerView recyclerView;
    private ArrayList<Cast> castList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casts);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initViews();

    }

    private void initViews() {
        Intent intent = getIntent();
        id = Objects.requireNonNull(intent.getExtras()).getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
        setTitle(original_Title + "'s" + " Casts");

        castList = new ArrayList<>();
        CastsAdapter adapter = new CastsAdapter(this, castList);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.scrollToPosition(position);
        adapter.notifyDataSetChanged();
        loadCasts(id);
    }

    private void loadCasts(int id) {
        try {

            Client client = new Client();
            Service api_service = Client.getClient().create(Service.class);
            Call<CastResponse> call = api_service.getMovieCasts(id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<CastResponse>() {
                @Override
                public void onResponse(@NonNull Call<CastResponse> call, @NonNull Response<CastResponse> response) {
                    List<Cast> castList = null;
                    if (response.body() != null) {
                        castList = Objects.requireNonNull(response.body()).getCast();
                    }
                    recyclerView.setAdapter(new CastsAdapter(getApplicationContext(), castList));

                }

                @Override
                public void onFailure(@NonNull Call<CastResponse> call, @NonNull Throwable t) {
                    Toast.makeText(CastsActivity.this, R.string.errorCastApiData, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SS", castList);
        int position = llm.findFirstCompletelyVisibleItemPosition();
        outState.putInt("pos", position);
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
