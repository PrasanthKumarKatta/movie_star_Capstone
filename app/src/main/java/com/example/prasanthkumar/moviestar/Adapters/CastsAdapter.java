package com.example.prasanthkumar.moviestar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prasanthkumar.moviestar.Model.Cast;
import com.example.prasanthkumar.moviestar.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastsAdapter  extends RecyclerView.Adapter<CastsAdapter.CrewInfo> {
    Context ct;
    private List<Cast> castList;

    public CastsAdapter(Context ct, List<Cast> castList)
    {
        this.ct = ct;
        this.castList = castList;
    }

    @NonNull
    @Override
    public CrewInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrewInfo(LayoutInflater.from(parent.getContext()).inflate(R.layout.crew_row_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrewInfo holder, int position) {

        Cast crewCurrentdata = castList.get(position);
        holder.name.append(crewCurrentdata.getName());
        holder.character.append(crewCurrentdata.getCharacter());
        String baseUrl = "https://image.tmdb.org/t/p/w500"+crewCurrentdata.getProfilePath();
        Picasso.with(ct).load(baseUrl).placeholder(R.drawable.loading_gif)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CrewInfo extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView) ImageView imageView;
        @BindView(R.id.name_crew) TextView name;
        @BindView(R.id.character_crew) TextView character;

        public CrewInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
