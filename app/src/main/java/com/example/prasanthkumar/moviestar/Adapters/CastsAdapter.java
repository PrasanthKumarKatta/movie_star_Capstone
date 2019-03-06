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

public class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.CastsInfo>
{
    private Context context;
    private List<Cast> castList;
    public CastsAdapter(Context context, List<Cast> castList)
    {
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastsInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.crew_row_card,parent,false);
        return new CastsInfo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CastsInfo holder, int position)
    {
        holder.name_crew.setText( castList.get(position).getName());
        holder.character.setText(castList.get(position).getCharacter());
        Picasso.with(context)
                .load(castList.get(position).getProfile_link())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img);

    }
    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CastsInfo extends RecyclerView.ViewHolder
    {
        @BindView(R.id.image_crew) ImageView img;
        @BindView(R.id.name_crew) TextView name_crew;
        @BindView(R.id.character_crew) TextView character;

        CastsInfo(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
