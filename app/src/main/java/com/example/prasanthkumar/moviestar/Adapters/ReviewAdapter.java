package com.example.prasanthkumar.moviestar.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prasanthkumar.moviestar.Model.Review;
import com.example.prasanthkumar.moviestar.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewInfo> {

    private Context context;
    private List<Review> reviewsList;

    public ReviewAdapter(Context context, List<Review> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewAdapter.ReviewInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_card,parent,false);
        return new ReviewInfo(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewInfo holder, int position) {
        if (reviewsList.get(position).getContent().equals(" "))
        {
            holder.review.setText("No Reviews Found");
        }else {
            holder.review.setText(reviewsList.get(position).getContent());
            holder.review_author.setText("Author: " + reviewsList.get(position).getAuthor());
            //  Toast.makeText(context, ""+reviewsList.get(position).getContent(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewInfo extends RecyclerView.ViewHolder {
        @BindView(R.id.review_author) TextView review_author;
        @BindView(R.id.reviews_id) TextView review;
        public ReviewInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            //   review = itemView.findViewById(R.id.reviews_id);

        }
    }
}

