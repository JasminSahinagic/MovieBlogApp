package com.example.movieblogapp.movieblogapp.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieblogapp.movieblogapp.Model.Post;
import com.example.movieblogapp.movieblogapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieBlogAdapter extends RecyclerView.Adapter<MovieBlogAdapter.ViewHolder>{

    Context context;
    List<Post> postList;

    public MovieBlogAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_blog_raw,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        String string = "Temp";
        holder.textViewUserName.setText(string);
        holder.textViewBlogPostDate.setText(post.getPostDate());
        holder.textViewUserInput.setText(post.getUserInput());
        holder.textViewDirector.setText(post.getDirector());
        holder.textViewWriter.setText(post.getWriter());
        holder.textViewReleasedDate.setText(post.getRelasedDate());
        holder.textViewRating.setText(post.getRating());
        holder.textViewRuntime.setText(post.getRuntime());
        Picasso.with(context).load(post.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUserName;
        public TextView textViewBlogPostDate;
        public TextView textViewUserInput;
        public TextView textViewDirector;
        public TextView textViewWriter;
        public TextView textViewReleasedDate;
        public TextView textViewRating;
        public TextView textViewRuntime;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            textViewUserName = (TextView) itemView.findViewById(R.id.textViewBlogPostUserName);
            textViewBlogPostDate = (TextView) itemView.findViewById(R.id.textViewBlogPostDate);
            textViewUserInput = (TextView) itemView.findViewById(R.id.textViewPostInput);
            textViewDirector = (TextView) itemView.findViewById(R.id.textViewBlogPostDirector);
            textViewWriter = (TextView) itemView.findViewById(R.id.textViewBlogPostWriters);
            textViewReleasedDate = (TextView) itemView.findViewById(R.id.textViewBlogPostReleasedDate);
            textViewRating = (TextView) itemView.findViewById(R.id.textViewBlogPostRating);
            textViewRuntime = (TextView) itemView.findViewById(R.id.textViewBlogPostRuntime);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewBlogPostImage);
        }
    }
}
