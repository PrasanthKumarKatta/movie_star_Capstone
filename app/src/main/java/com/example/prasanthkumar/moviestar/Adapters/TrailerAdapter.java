package com.example.prasanthkumar.moviestar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.Model.Trailer;
import com.example.prasanthkumar.moviestar.R;
import com.example.prasanthkumar.moviestar.UIScreens.YoutubePlayerActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerInfo> {

    private Context context;
    private List<Trailer> trailerList;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trailer_card,parent,false);
        return new TrailerInfo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerInfo holder, final int position)
    {
        holder.trailer_Title.setText(trailerList.get(position).getName());

        holder.imageView.initialize(BuildConfig.TheYoutubeAPIKey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
               // youTubeThumbnailLoader=youTubeThumbnailLoader;
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new ThumbnailLoadedListener());
                youTubeThumbnailLoader.setVideo(trailerList.get(position).getKey());;
            }
            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerInfo extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_thumbnail_img) YouTubeThumbnailView imageView;
        @BindView(R.id.title_trailer) TextView trailer_Title;

        TrailerInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    callYoutube(pos);
                }
            });

        }
    }

    private void callYoutube(int pos) {
        if (pos !=RecyclerView.NO_POSITION)
        {
            Trailer clickedItems = trailerList.get(pos);
            String videoId = clickedItems.getKey();
            Intent i = new Intent(context, YoutubePlayerActivity.class);
            i.putExtra("videoKey",videoId);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


    private class ThumbnailLoadedListener implements YouTubeThumbnailLoader.OnThumbnailLoadedListener {
        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

        }
    }
}
