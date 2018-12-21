package com.example.prasanthkumar.moviestar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.prasanthkumar.moviestar.Model.Gallery_Posters;
import com.example.prasanthkumar.moviestar.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.PostersViewInfo>
{
    private Context context;
    private List<Gallery_Posters> postersList;

    public PostersAdapter(Context context, List<Gallery_Posters> postersList) {
        this.context = context;
        this.postersList = postersList;
    }

    @NonNull
    @Override
    public PostersViewInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.gallery_card, parent, false);
        return new PostersViewInfo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostersViewInfo holder, int position) {
        Picasso.with(context)
               .load(postersList.get(position).getFile_path())
               .placeholder(R.mipmap.ic_launcher)
               .into(holder.img_Poster);
    }

    @Override
    public int getItemCount() {
        return postersList.size();
    }

    public class PostersViewInfo extends RecyclerView.ViewHolder
    {
        @BindView(R.id.image_gallery) ImageView img_Poster;

        PostersViewInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
