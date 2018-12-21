package com.example.prasanthkumar.moviestar.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.R;
import com.example.prasanthkumar.moviestar.UIScreens.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter_favorites extends RecyclerView.Adapter<MovieAdapter_favorites.MovieInfo>
{
    private Context context;
    private List<Movie> movieList;
    RecyclerView rv;
    public static final String Title = "original_title";
    public static final String Poster_path = "poster";
    public static final String Release_date = "release";
    public static final String Vote_average = "vote";
    public static final String Overview = "overview";
    public static final String Id = "id";
    private static final String backDropImg_key = "backdropImg";

    public MovieAdapter_favorites(Context context, List<Movie> movieList, RecyclerView rv)
    {
        this.context = context;
        this.movieList = movieList;
        this.rv = rv;
    }

    @NonNull
    @Override
    public MovieInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_favorites, parent, false);
        return new MovieInfo(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieInfo holder, int position) {

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w500" + movieList.get(position).getPosterPath())
                .placeholder(R.drawable.loading_gif).into(holder.thumbnail_img);
        Double rating = Double.parseDouble(String.valueOf(movieList.get(position).getVoteAverage()));
        holder.movie_rating.setText(String.valueOf(rating)+"/10");
    }

    @Override
    public int getItemCount()
    {
        if(movieList.size() !=0){

            return movieList.size();
        }
        Toast.makeText(context, "You have No Favorites yet!", Toast.LENGTH_SHORT).show();
        Snackbar.make( rv , "No Favorite Movies yet!",
                Snackbar.LENGTH_SHORT).show();
        return 0;
    }

    public class MovieInfo extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_img) ImageView thumbnail_img;
        @BindView(R.id.movie_rating) TextView movie_rating;

        MovieInfo(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = getLayoutPosition();
                    Toast.makeText(context, "pos:"+pos, Toast.LENGTH_SHORT).show();
                    passDataToDetails(pos);

                }
            });

        }
    }

    private void passDataToDetails(int pos) {
        if(pos != RecyclerView.NO_POSITION) {
            Movie clickedDataItems = movieList.get(pos);
            Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra(Title, clickedDataItems.getOriginal_title());
            Toast.makeText(context, ""+ clickedDataItems.getOriginal_title(), Toast.LENGTH_SHORT).show();
            i.putExtra(Poster_path, clickedDataItems.getPosterPath());
            i.putExtra(Overview, clickedDataItems.getOverview());
            i.putExtra(Vote_average, clickedDataItems.getVoteAverage());
            i.putExtra(Release_date, clickedDataItems.getReleaseDate());
            i.putExtra(Id,clickedDataItems.getId());
            i.putExtra(backDropImg_key,clickedDataItems.getBackdrop_path());

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }
    }

}
