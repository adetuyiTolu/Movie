package com.crevation.movie.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crevation.movie.R;
import com.crevation.movie.data.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private List<Trailer> trailerList;
    Activity context;
    OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailerName;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            trailerName = (TextView) view.findViewById(R.id.txt_trailer);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.OnClick(position);
        }
    }


    public TrailerAdapter(List<Trailer> trailerList, Activity context, OnItemClickListener onItemClickListener) {
        this.trailerList = trailerList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_trailer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        holder.trailerName.setText("Trailer " + (position + 1));
    }

    @Override
    public int getItemCount() {
        try {
            return trailerList.size();
        } catch (Exception e) {
            return 0;
        }

    }


    public interface OnItemClickListener {
        void OnClick(int itemPosition);
    }
}
