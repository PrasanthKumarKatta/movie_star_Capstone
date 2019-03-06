package com.example.prasanthkumar.moviestar.UIScreens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.TrailerAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.CheckInternet.InternetConnection;
import com.example.prasanthkumar.moviestar.Model.Trailer;
import com.example.prasanthkumar.moviestar.Model.TrailerResponse;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersActivity extends AppCompatActivity {

    private static final  String id_key ="id";
    private static final  String originalTitle_key ="original_title";
    private int id;
    private String original_Title;
    @BindView(R.id.trailer_recyclerview) StatefulRecyclerView recyclerView;
    private  List<Trailer> trailerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setTitle(original_Title+"'s"+" Trailers");
        checkInternet();

    }
    private void checkInternet()
    {
        if (InternetConnection.isNetworkAvailable(getApplicationContext())){
            initviews();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(R.string.permissions);
            builder.setMessage(R.string.error_dialog_internet);
            builder.setIcon(R.drawable.ic_sentiment_dissatisfied_black_24dp);
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

                }
            });
            builder.show();
        }
    }

    private void initviews() {
        Intent intent = getIntent();
        id = Objects.requireNonNull(intent.getExtras()).getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
       // Toast.makeText(this, "id:"+id +"\n"+original_Title, Toast.LENGTH_SHORT).show();
        setTitle(original_Title+"'s"+getString(R.string.trailersTitle));

        trailerList = new ArrayList<>();
        TrailerAdapter adapter = new TrailerAdapter(this,trailerList);

        recyclerView.setAdapter(adapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
       // recyclerView.scrollToPosition(position);
        adapter.notifyDataSetChanged();
        loadTrailers(id);
    }

    private void loadTrailers(int movie_id) {
        try{
            Client client = new Client();
            Service api_Service = Client.getClient().create(Service.class);
            Call<TrailerResponse> call = api_Service.getMovieTrailer(movie_id,BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                    List<Trailer> trailer = Objects.requireNonNull(response.body()).getResults();
                    Log.d("JsonResponse",trailer.toString());
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailer));
                }

                @Override
                public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(TrailersActivity.this, R.string.errorTrailerData, Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("Error",e.getMessage());
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
