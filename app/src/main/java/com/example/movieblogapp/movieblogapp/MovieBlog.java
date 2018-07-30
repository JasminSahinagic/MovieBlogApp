package com.example.movieblogapp.movieblogapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.movieblogapp.movieblogapp.Data.MovieBlogAdapter;
import com.example.movieblogapp.movieblogapp.Model.Movie;
import com.example.movieblogapp.movieblogapp.Model.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MovieBlog extends AppCompatActivity implements View.OnClickListener{

    private Button buttonMovies;
    private DatabaseReference databaseRef;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Post> postList;
    private Button buttonLogOut;
    private Button buttonCreatePost;
    private FirebaseAuth mAuth;
    private List<Post> tempList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_blog);
        moviesSetUp();
        buttonMovies.setOnClickListener(this);
        buttonCreatePost.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                postList.add(post);
                HashSet<Post> uniqueValues = new HashSet<>(postList);
                for (Post value : uniqueValues) {
                    tempList.add(value);
                }
                Collections.reverse(tempList);
                adapter = new MovieBlogAdapter(MovieBlog.this, tempList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                postList.clear();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonMovies:
                startActivity(new Intent(MovieBlog.this, Movies.class));
                postList.clear();
                tempList.clear();
                break;
            case R.id.buttonCreatePost:
                startActivity(new Intent(MovieBlog.this, CreatePost.class));
                postList.clear();
                tempList.clear();
                break;
            case R.id.buttonLogOut:
                mAuth.getInstance().signOut();
                startActivity(new Intent(MovieBlog.this, MainActivity.class));
                finish();
                break;
        }
    }

    public void moviesSetUp(){

        postList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("BlogPosts");
        databaseRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovieBlog);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tempList = new ArrayList<>();
        buttonMovies = (Button) findViewById(R.id.buttonMovies);
        buttonCreatePost = (Button) findViewById(R.id.buttonCreatePost);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
    }
}
