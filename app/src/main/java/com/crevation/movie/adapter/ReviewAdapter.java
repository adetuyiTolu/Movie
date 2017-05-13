package com.crevation.movie.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crevation.movie.AppResource;
import com.crevation.movie.R;
import com.crevation.movie.data.model.Movie;
import com.crevation.movie.data.model.Review;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private List<Review> reviewList;
    Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuthor, reviewContent;

        public MyViewHolder(View view) {
            super(view);
            reviewAuthor = (TextView) view.findViewById(R.id.review_by);
            reviewContent = (TextView) view.findViewById(R.id.review_content);
        }

    }


    public ReviewAdapter(List<Review> reviewList, Activity context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Review review = reviewList.get(position);

    }

    @Override
    public int getItemCount() {
        try {
            return reviewList.size();
        } catch (Exception e) {
            return 0;
        }

    }
}
