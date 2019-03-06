package com.example.prasanthkumar.moviestar.UIScreens;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.MovieAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.CheckInternet.InternetConnection;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.Model.MoviesResponse;
import com.example.prasanthkumar.moviestar.NetworkUtils.TeluguAsyncTask;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    @BindView(R.id.main_content) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview1) StatefulRecyclerView mRecyclerView1;
    @BindView(R.id.recyclerview2) StatefulRecyclerView mRecyclerView2;
    @BindView(R.id.recyclerview3) StatefulRecyclerView mRecyclerView3;
    @BindView(R.id.recyclerview4) StatefulRecyclerView mRecyclerView4;
    @BindView(R.id.recyclerview5) StatefulRecyclerView mRecyclerView5;
    private ArrayList<Movie> movieList;
    private MovieAdapter adapter;
    private int mPosition;
    private String language = null;

    private LinearLayoutManager llm;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        llm = new LinearLayoutManager(getActivity());

        checkInternet();

        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("pos");
            movieList = (ArrayList<Movie>) savedInstanceState.getSerializable("ss");
            mRecyclerView1.setAdapter(new MovieAdapter(getActivity(), movieList));
            mRecyclerView2.setAdapter(new MovieAdapter(getActivity(), movieList));
            mRecyclerView3.setAdapter(new MovieAdapter(getActivity(), movieList));
            mRecyclerView4.setAdapter(new MovieAdapter(getActivity(), movieList));
            mRecyclerView5.setAdapter(new MovieAdapter(getActivity(), movieList));
        }

        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        return view;
    }
    private void checkInternet() {
        if (InternetConnection.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            Toast.makeText(getContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
            initviews();
            initviews1();
            initviews2();
            initviews3();
            initviews4();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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
            builder.setNegativeButton(R.string.cancel_alert, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }


    private void initviews() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);

        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView1.setAdapter(adapter);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView1.addItemDecoration(new DividerItemDecoration(mRecyclerView1.getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        language = "te";

        TeluguAsyncTask teluguAsyncTask = new TeluguAsyncTask(getActivity(),mRecyclerView1);
        teluguAsyncTask.execute();
    }

    private void initviews1() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);
        mRecyclerView2.setAdapter(adapter);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(mRecyclerView2.getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

        });
        language = "en";
        loadEnglishMovies(language);

    }

    private void loadEnglishMovies(String language) {
        try {
            Client client = new Client();
            Service api_Service = Client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getEnglishMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {

                    List<Movie> movieList = Objects.requireNonNull(response.body()).getResults();

                    mRecyclerView2.setAdapter(new MovieAdapter(getContext(), movieList));

                    //mRecyclerView.smoothScrollToPosition(0);
                    //  mRecyclerView1.scrollToPosition(position);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), "Error in Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initviews2() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);
        mRecyclerView3.setAdapter(adapter);
        mRecyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView3.addItemDecoration(new DividerItemDecoration(mRecyclerView3.getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), R.string.moviesResfreshed, Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        language = "hi";
        loadHindiMovies(language);

    }

    private void loadHindiMovies(String language) {
        try {
            Client client = new Client();
            Service api_Service = Client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getHindiMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = Objects.requireNonNull(response.body()).getResults();

                    mRecyclerView3.setAdapter(new MovieAdapter(getContext(), movieList));
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {

                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), "Error in Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initviews3() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);

        mRecyclerView4.setAdapter(adapter);
        mRecyclerView4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView4.addItemDecoration(new DividerItemDecoration(mRecyclerView4.getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        language = "pa";
        loadPanjabiMovies(language);

    }

    private void loadPanjabiMovies(String language) {
        try {
            Client client = new Client();
            Service api_Service = Client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getPanjabiMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = Objects.requireNonNull(response.body()).getResults();

                    mRecyclerView4.setAdapter(new MovieAdapter(getContext(), movieList));
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {

                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), R.string.errorAPIData, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initviews4() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);
        mRecyclerView5.setAdapter(adapter);
        mRecyclerView5.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView5.addItemDecoration(new DividerItemDecoration(mRecyclerView5.getContext(), DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), R.string.moviesResfreshed, Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        language = "ta";
        loadTamilMovies(language);
    }

    private void loadTamilMovies(String language) {
        try {
            Client client = new Client();
            Service api_Service = Client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getTamilMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {

                    List<Movie> movieList = Objects.requireNonNull(response.body()).getResults();

                    mRecyclerView5.setAdapter(new MovieAdapter(getContext(), movieList));
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {

                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), R.string.errorAPIData, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (movieList!=null){
            outState.putSerializable("SS",movieList);
            int position = llm.findFirstCompletelyVisibleItemPosition();
            outState.putInt("pos", position);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Objects.requireNonNull(getActivity()).finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
