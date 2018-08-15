package com.example.prasanthkumar.moviestar.UIScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prasanthkumar.moviestar.BuildConfig;
import com.example.prasanthkumar.moviestar.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    private static final String API = BuildConfig.TheYoutubeAPIKey;
    @BindView(R.id.youtubePlayer_id) YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        ButterKnife.bind(this);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                String vid =getIntent().getStringExtra("videoKey");
                youTubePlayer.loadVideo(vid);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(API, onInitializedListener);

    }
}
