package com.example.prasanthkumar.moviestar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.Model.Review;
import com.example.prasanthkumar.moviestar.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewInfo>
{
    private Context context;
    private List<Review> reviewsList;

    public ReviewAdapter(Context context, List<Review> reviewsList)
    {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewInfo onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.review_card,parent,false);
        return new ReviewInfo(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewInfo holder, int position)
    {
        if (reviewsList == null)
        {
            Toast.makeText(context, "No Reviews for this Movie", Toast.LENGTH_SHORT).show();

        }else {
            holder.review.setText(reviewsList.get(position).getContent());
            holder.review_author.setText(context.getString(R.string.author_review_adaptertext) + reviewsList.get(position).getAuthor());
        }
    }

    @Override
    public int getItemCount()
    {
        return reviewsList.size();
    }

    public class ReviewInfo extends RecyclerView.ViewHolder
    {
        @BindView(R.id.review_author) TextView review_author;
        @BindView(R.id.reviews_id) TextView review;

        ReviewInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}

