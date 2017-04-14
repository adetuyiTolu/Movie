package com.crevation.movie.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;

import com.crevation.movie.AppResource;
import com.crevation.movie.R;
import com.crevation.movie.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private List<Movie> movieList;
    Activity context;
    OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pic;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            pic = (ImageView) view.findViewById(R.id.pic);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.OnClick(position);
        }
    }


    public MovieAdapter(List<Movie> movieList, Activity context, OnItemClickListener onItemClickListener) {
        this.movieList = movieList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        //handle image loading
        Picasso.with(context).load(AppResource.IMAGE_PATH + movie.getPosterUrl())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        try {
            return movieList.size();
        } catch (Exception e) {
            return 0;
        }

    }


    public interface OnItemClickListener {
        void OnClick(int itemPosition);
    }
}
