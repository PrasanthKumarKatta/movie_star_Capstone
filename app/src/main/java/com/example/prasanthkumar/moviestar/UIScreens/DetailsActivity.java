package com.example.prasanthkumar.moviestar.UIScreens;



import android.app.ActivityOptions;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.API.Client;
import com.example.prasanthkumar.moviestar.API.Service;
import com.example.prasanthkumar.moviestar.Adapters.ReviewAdapter;
import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Data.FavoriteDBHelper;
import com.example.prasanthkumar.moviestar.Data.Favorites_Contract;
import com.example.prasanthkumar.moviestar.Data.MovieStarContentProvider;
import com.example.prasanthkumar.moviestar.HomeWidget.MovieStarWidget;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.Model.Review;
import com.example.prasanthkumar.moviestar.Model.ReviewResponse;
import com.example.prasanthkumar.moviestar.R;
import com.example.prasanthkumar.moviestar.RoomData.MovieEntity;
import com.example.prasanthkumar.moviestar.RoomData.MovieViewModel;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private static final String OrginalTitle_key = "original_title";
    private static final String Poster_path_key = "poster";
    private static final String Release_date_key = "release";
    private static final String Vote_average_key = "vote";
    private static final String Overview_key = "overview";
    private static final String Id_key = "id";
    private static final String backDropImg_key = "backdropImg";

    private int id;
    @BindView(R.id.backdrop_image) ImageView backDrop_img;
    @BindView(R.id.thumbnail_img_header) ImageView img;
    @BindView(R.id.title_details) TextView nameOfMovie;
    @BindView(R.id.plotSynopsis)
    TextView plotSynopsis;
    @BindView(R.id.user_Rating)
    TextView userRating;
    @BindView(R.id.release_dates)
    TextView releaseDate;
    @BindView(R.id.appbar)
     AppBarLayout appBarLayout;
    @BindView(R.id.trailer_fab)
     FloatingActionButton  trailers_fab;
    @BindView(R.id.casts_fab)
     FloatingActionButton casts_fab;
    @BindView(R.id.posters_fab)
     FloatingActionButton posters_fab;
    @BindView(R.id.favorite_button)
    MaterialFavoriteButton materialFavoriteButton;
    @BindView(R.id.recyclerView_reviews)
    StatefulRecyclerView recyclerView_review;
    @BindView(R.id.reviews_tv) TextView noreview;
    private ReviewAdapter adapter_review;
    private List<Review> reviewsList;
    private FavoriteDBHelper favDB = new FavoriteDBHelper(this);
    private int movie_id;

    private MovieViewModel mMovieViewModel;
    private String thumbnail;
    private String movieName;
    private String synopsis;
    private double vote_average;
    private String rating;
    private String dateOfRelease;
    private String backDropImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        initCollapsingToolbar();
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        if (savedInstanceState !=null){
            movie_id = savedInstanceState.getInt(Id_key);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(OrginalTitle_key)) {
            id = Objects.requireNonNull(getIntent().getExtras()).getInt(Id_key);

            thumbnail = "http://image.tmdb.org/t/p/w500" + getIntent().getExtras().getString(Poster_path_key);
            movieName = getIntent().getExtras().getString(OrginalTitle_key);
            synopsis = getIntent().getExtras().getString(Overview_key);
            vote_average = getIntent().getDoubleExtra(Vote_average_key, 0);
            rating = Double.toString(vote_average);
            dateOfRelease = getIntent().getExtras().getString(Release_date_key);
            backDropImg = "https://image.tmdb.org/t/p/w500" + getIntent().getExtras().getString(backDropImg_key);

            Picasso.with(this)
                    .load("" + backDropImg)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(backDrop_img);

            Picasso.with(this)
                    .load("" + thumbnail)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(img);
            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

            //Setting data to Home widget
            SharedPreferences sharedPreferences = getSharedPreferences("movies_description", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("movie_des_key", synopsis);
            editor.apply();

            Intent i = new Intent(this, MovieStarWidget.class);
            i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(getApplicationContext())
                    .getAppWidgetIds(new ComponentName(getApplication(), MovieStarWidget.class));
            i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(i);

            movie_id = getIntent().getExtras().getInt(Id_key);
            /*
            if (favDB.search(movie_id)) {
                materialFavoriteButton.setFavorite(true);
            }
            */
            Toast.makeText(this, ""+mMovieViewModel.checkFav(movie_id), Toast.LENGTH_SHORT).show();
            if (mMovieViewModel.checkFav(movie_id))
            {
                materialFavoriteButton.setFavorite(true);
            }

            materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                    if (favorite) {
/*
                        SharedPreferences.Editor editor = getSharedPreferences("com.example.prasanthkumar.moviestar.UIScreens",MODE_PRIVATE).edit();
                        editor.putBoolean("FavoriteAdded",true);
                        editor.apply();*/

                        //saveFav();
                        //savFavorite();
                        mMovieViewModel.insert(setValuesToEntity());


                        Snackbar.make(buttonView, "Add to Favorite",
                                Snackbar.LENGTH_SHORT).show();
                    } else {

                       /* movie_id = getIntent().getExtras().getInt(Id_key);

                        String stringId = Integer.toString(id);
                        int uri = getContentResolver().delete(MovieStarContentProvider.CONTENT_URI, stringId, null);

                        SharedPreferences.Editor editor =
                                getSharedPreferences("com.example.prasanthkumar.moviestar.UIScreens",MODE_PRIVATE).edit();
                        editor.putBoolean("Favorite Removed",true);
                        editor.apply();
                        */

                        mMovieViewModel.delete(setValuesToEntity());
                        Snackbar.make(buttonView, "Removed from Favorite",
                                Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, R.string.noApiData, Toast.LENGTH_SHORT).show();
        }
        trailers_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, TrailersActivity.class);
                movie_id = getIntent().getExtras().getInt(Id_key);
                i.putExtra(Id_key, movie_id);
                i.putExtra(OrginalTitle_key, getIntent().getExtras().getString(OrginalTitle_key));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(i);
                startActivity(i,
                        ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this).toBundle());
            }
        });
        casts_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, CastsActivity.class);
                movie_id = getIntent().getExtras().getInt(Id_key);
                i.putExtra(Id_key, movie_id);
                i.putExtra(OrginalTitle_key, getIntent().getExtras().getString(OrginalTitle_key));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(i);
                startActivity(i,
                        ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this).toBundle());
            }
        });
        posters_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, PostersActivity.class);
                movie_id = getIntent().getExtras().getInt(Id_key);
                i.putExtra(Id_key, movie_id);
                i.putExtra(OrginalTitle_key, getIntent().getExtras().getString(OrginalTitle_key));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(i);
                startActivity(i,
                        ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this).toBundle());
            }
        });

        initViews(movie_id);

    }

    private void savFavorite() {
        mMovieViewModel.checkFav(movie_id);
        if (mMovieViewModel.checkFav(movie_id))
        {
            materialFavoriteButton.setFavorite(true);

            mMovieViewModel.delete(setValuesToEntity());

        }
        else {
            materialFavoriteButton.setFavorite(false);
            mMovieViewModel.insert(setValuesToEntity());
        }
    }

    private Movie setValuesToEntity()
    {
        Movie movieEntityInfo = new Movie();
        movieEntityInfo.setOriginal_title(movieName);
        movieEntityInfo.setId(movie_id);
        movieEntityInfo.setVoteAverage(vote_average);
        movieEntityInfo.setBackdrop_path(backDropImg);
        movieEntityInfo.setPosterPath(thumbnail);
        movieEntityInfo.setReleaseDate(dateOfRelease);
        return movieEntityInfo;
    }

    private void saveFav() {
           SQLiteDatabase db = favDB.getWritableDatabase();
           ContentValues cv = new ContentValues();
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_MOVIEID, Objects.requireNonNull(getIntent().getExtras()).getInt(Id_key));
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_TITLE, getIntent().getExtras().getString(OrginalTitle_key));
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_USERRATING, getIntent().getExtras().getDouble(Vote_average_key));
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_RELEASEDATE, getIntent().getExtras().getString(Release_date_key));
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_POSTER_PATH, getIntent().getExtras().getString(Poster_path_key));
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_BACKDROP_IMG, getIntent().getExtras().getString(backDropImg_key));
           cv.put(Favorites_Contract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, getIntent().getExtras().getString(Overview_key));

           Uri uri = getContentResolver().insert(MovieStarContentProvider.CONTENT_URI, cv);
       }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbarLayout.setTitle(getIntent().getExtras().getString(originalTitle_key));
                    collapsingToolbarLayout.setTitle("Movie Details");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }

            }
        });
    }

    private void initViews(int mId) {
        reviewsList = new ArrayList<>();
        adapter_review = new ReviewAdapter(this, reviewsList);

            recyclerView_review.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView_review.setAdapter(adapter_review);
            recyclerView_review.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            adapter_review.notifyDataSetChanged();

            loadReviews(mId);

    }

    private void loadReviews(int mId) {
          try {
              Client client = new Client();
              Service apiService = Client.getClient().create(Service.class);
              Call<ReviewResponse> call = apiService.getMovieReviews(mId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
              call.enqueue(new Callback<ReviewResponse>() {
                  @Override
                  public void onResponse(@NonNull Call<ReviewResponse> call, Response<ReviewResponse> response) {

                      List<Review> reviews = null;
                      if (response.body() != null) {
                          reviews = Objects.requireNonNull(response.body()).getResults();
                      }

                      if (Objects.requireNonNull(reviews).isEmpty()) {
                          Toast.makeText(DetailsActivity.this, getResources().getString(R.string.noreview_text), Toast.LENGTH_SHORT).show();
                          noreview.setVisibility(View.VISIBLE);
                          noreview.setText(R.string.noreview_text);
                      }else {
                          noreview.setVisibility(View.INVISIBLE);
                      }
                      recyclerView_review.setAdapter(new ReviewAdapter(getApplicationContext(), reviews));
                  }

                  @Override
                  public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                      Toast.makeText(DetailsActivity.this, "Error Fetching Trailer Data", Toast.LENGTH_SHORT).show();
                  }
              });
          } catch (Exception e) {

            Toast.makeText(DetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Id_key, movie_id );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
