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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.ValueEventListener;

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
    @BindView(R.id.trailer_recyclerview) StatefulRecyclerView recyclerView;
    private  List<Trailer> trailerList;
    private int position;
    private static final String SAVED_LAYOUT_MANAGER = "SavedLayoutManager" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setTitle(original_Title+"'s"+" Trailers");
        checkInternet();

    }
    private void checkInternet()
    {
        if (InternetConnection.isNetworkAvailable(getApplicationContext())){

            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();
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
        id = intent.getExtras().getInt(id_key);
        original_Title = intent.getExtras().getString(originalTitle_key);
       // Toast.makeText(this, "id:"+id +"\n"+original_Title, Toast.LENGTH_SHORT).show();
        setTitle(original_Title+"'s"+" Trailers");

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
                    Log.d("JsonResponse",trailer.toString());
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailer));
                    //recyclerView.smoothScrollToPosition(0);
                    //recyclerView.scrollToPosition(position);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(TrailersActivity.this, "Error in Fetching Trailer Data", Toast.LENGTH_SHORT).show();

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

  /*  @Override
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
