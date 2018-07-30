package com.example.movieblogapp.movieblogapp.Data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieblogapp.movieblogapp.Model.Movie;
import com.example.movieblogapp.movieblogapp.MovieDetails;
import com.example.movieblogapp.movieblogapp.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {


    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        String poster = movie.getPoster();
        holder.textViewMovieTitle.setText(movie.getTitle());
        holder.textViewMovieReleased.setText(movie.getDvdRelease());
        holder.textViewMovieCategory.setText(movie.getMovieType());
        Picasso.with(context).load(poster).into(holder.imageViewMovies);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageViewMovies;
        public TextView textViewMovieTitle;
        public TextView textViewMovieReleased;
        public TextView textViewMovieCategory;

        public ViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            textViewMovieTitle = (TextView) itemView.findViewById(R.id.textViewMovieTitle);
            textViewMovieReleased = (TextView) itemView.findViewById(R.id.textViewMovieReleased);
            textViewMovieCategory = (TextView) itemView.findViewById(R.id.textViewMovieCategory);
            imageViewMovies = (ImageView) itemView.findViewById(R.id.imageViewMovieImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Movie movie = movieList.get(getAdapterPosition());
                    Intent intent = new Intent(context, MovieDetails.class);
                    intent.putExtra("Movie", movie);
                    context.startActivity(intent);

                }
            });
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()){

            }
        }
    }
}
