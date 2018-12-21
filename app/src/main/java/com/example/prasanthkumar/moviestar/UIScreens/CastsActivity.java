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
import com.example.prasanthkumar.moviestar.Adapters.TrailerAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Cast;
import com.example.prasanthkumar.moviestar.Model.CastResponse;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastsActivity extends AppCompatActivity {

    private static final String id_key = "id";
    private static final String originalTitle_key = "original_title";
    int id;
    String original_Title;
    @BindView(R.id.casts_recyclerview)
    StatefulRecyclerView recyclerView;
    private ArrayList<Cast> castList;
    /*private int position;
    private LinearLayoutManager llm;
*/
   /* private int position;
    private static final String SAVED_LAYOUT_MANAGER = "SavedLayoutManager" ;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casts);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
       /* if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("pos");
            castList = (ArrayList<Cast>) savedInstanceState.getSerializable("ss");
            recyclerView.setLayoutManager(llm);
        }*/
    }

    private void initViews() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
        Toast.makeText(this, "id:" + id + "\n" + original_Title, Toast.LENGTH_SHORT).show();
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
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(this, "Please get your API key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }

            Client client = new Client();
            Service api_service = client.getClient().create(Service.class);
            Call<CastResponse> call = api_service.getMovieCasts(id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<CastResponse>() {
                @Override
                public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                    List<Cast> castList = response.body().getCast();
                    recyclerView.setAdapter(new CastsAdapter(getApplicationContext(), castList));

                }

                @Override
                public void onFailure(Call<CastResponse> call, Throwable t) {
                    Toast.makeText(CastsActivity.this, "Error in Fetching in Casts Data", Toast.LENGTH_SHORT).show();
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

   /* @Override
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
