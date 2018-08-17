package com.example.prasanthkumar.moviestar.UIScreens;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.MovieAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.Model.MoviesResponse;
import com.example.prasanthkumar.moviestar.R;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.recyclerview1) RecyclerView mRecyclerView1;
    @BindView(R.id.recyclerview2) RecyclerView mRecyclerView2;
    @BindView(R.id.recyclerview3) RecyclerView mRecyclerView3;
    @BindView(R.id.recyclerview4) RecyclerView mRecyclerView4;
    @BindView(R.id.recyclerview5) RecyclerView mRecyclerView5;
    private ArrayList<Movie> movieList=new ArrayList<>();
    private MovieAdapter adapter;
    private int position;
    public String language=null;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        initviews();
        initviews1();
        initviews2();
        initviews3();
        initviews4();
        return view;
    }

    private void initviews() {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);

      /*  if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        }
      */

      //mRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView1.setAdapter(adapter);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView1.addItemDecoration(new DividerItemDecoration(mRecyclerView1.getContext(),DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        language="te";
        loadTeluguMovies(language);

    }

    private void loadTeluguMovies(String language) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
                // pd.dismiss();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);
//          Call<MoviesResponse> call = api_Service.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            Call<MoviesResponse> call = api_Service.getTeluguMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,language);
            //"&with_original_language=te"
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = response.body().getResults();

                    mRecyclerView1.setAdapter(new MovieAdapter(getContext(), movieList));

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

    private void initviews1()
    {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);


        //mRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView2.setAdapter(adapter);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(mRecyclerView2.getContext(),DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        language="en";
        loadEnglishMovies(language);

    }

    private void loadEnglishMovies(String language)
    {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getEnglishMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = response.body().getResults();

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

    private void initviews2()
    {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);

      /*  if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        }
      */

        //mRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView3.setAdapter(adapter);
        mRecyclerView3.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView3.addItemDecoration(new DividerItemDecoration(mRecyclerView3.getContext(),DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        language="hi";
        loadHindiMovies(language);

    }

    private void loadHindiMovies(String language)
    {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();

                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getHindiMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = response.body().getResults();

                    mRecyclerView3.setAdapter(new MovieAdapter(getContext(), movieList));

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

    private void initviews3()
    {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);

        //mRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView4.setAdapter(adapter);
        mRecyclerView4.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView4.addItemDecoration(new DividerItemDecoration(mRecyclerView4.getContext(),DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        language="pa";
        loadPanjabiMovies(language);

    }

    private void loadPanjabiMovies(String language)
    {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getPanjabiMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = response.body().getResults();

                    mRecyclerView4.setAdapter(new MovieAdapter(getContext(), movieList));

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


    private void initviews4()
    {
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(getContext(), movieList);

        //mRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView5.setAdapter(adapter);
        mRecyclerView5.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView5.addItemDecoration(new DividerItemDecoration(mRecyclerView5.getContext(),DividerItemDecoration.HORIZONTAL));
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(getContext(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        language="ta";
        loadTamilMovies(language);

    }

    private void loadTamilMovies(String language)
    {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getContext(), "Please get API key firstly From themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Service api_Service = client.getClient().create(Service.class);
            Call<MoviesResponse> call = api_Service.getTamilMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,language);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movieList = response.body().getResults();

                    mRecyclerView5.setAdapter(new MovieAdapter(getContext(), movieList));

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


}
