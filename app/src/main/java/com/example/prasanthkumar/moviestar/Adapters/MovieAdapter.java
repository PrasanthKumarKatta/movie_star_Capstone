package com.example.prasanthkumar.moviestar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.UIScreens.DetailsActivity;
import com.example.prasanthkumar.moviestar.Model.Movie;
import com.example.prasanthkumar.moviestar.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieInfo> {
    private Context context;
    private List<Movie> movieList;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_card, parent, false);
        return new MovieInfo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieInfo holder, int position) {

        Picasso.with(context)
                .load(movieList.get(position).getPosterPath())
                .placeholder(R.drawable.loading_gif).into(holder.thumbnail_img);
        Toast.makeText(context, ""+movieList.get(position).getPosterPath()+"\n"+movieList.get(position).getOriginal_title(), Toast.LENGTH_SHORT).show();
        Double rating = Double.parseDouble(String.valueOf(movieList.get(position).getVoteAverage()));
        holder.movie_rating.setText(String.valueOf(rating)+"/10");
    }

    @Override
    public int getItemCount()
    {
        return movieList.size();
    }

    public class MovieInfo extends RecyclerView.ViewHolder
    {

        @BindView(R.id.thumbnail_img) ImageView thumbnail_img;
        @BindView(R.id.movie_rating) TextView movie_rating;

        public MovieInfo(View itemView)
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
            i.putExtra("original_title", clickedDataItems.getOriginal_title());
            Toast.makeText(context, ""+ clickedDataItems.getOriginal_title(), Toast.LENGTH_SHORT).show();
            i.putExtra("poster_path", clickedDataItems.getPosterPath());
            i.putExtra("overview", clickedDataItems.getOverview());
            i.putExtra("vote_average", clickedDataItems.getVoteAverage());
            i.putExtra("release_date", clickedDataItems.getReleaseDate());
            i.putExtra("id",clickedDataItems.getId());
            i.putExtra("backdropImg",clickedDataItems.getBackdrop_path());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }
    }
}
