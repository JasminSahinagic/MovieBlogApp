package com.example.movieblogapp.movieblogapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieblogapp.movieblogapp.Model.Movie;
import com.example.movieblogapp.movieblogapp.Model.Post;
import com.example.movieblogapp.movieblogapp.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetails extends AppCompatActivity implements View.OnClickListener {

    private Movie movie;
    private TextView textViewTitle;
    private ImageView movieImage;
    private TextView textViewYear;
    private TextView textViewDirecor;
    private TextView textViewActors;
    private TextView textViewCategory;
    private TextView textViewRating;
    private TextView textViewWriters;
    private TextView textViewPlot;
    private TextView textViewBox;
    private TextView textViewRuntime;
    private Button buttonShare;
    private Post post;
    private RequestQueue queue;
    private String movieId;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        queue = Volley.newRequestQueue(this);
        movie = (Movie) getIntent().getSerializableExtra("Movie");
        movieId = movie.getImdbId();
        setUpUI();
        getMovieDetails(movieId);
        buttonShare.setOnClickListener(this);
    }

    private void getMovieDetails(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + id+ Constants.API_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if (response.has("Ratings")) {
                        JSONArray ratings = response.getJSONArray("Ratings");
                        String source = null;
                        String value = null;
                        if (ratings.length() > 0) {
                            JSONObject mRatings = ratings.getJSONObject(ratings.length() - 1);
                            source = mRatings.getString("Source");
                            value = mRatings.getString("Value");
                            textViewRating.setText(source + " : " + value);
                            post.setRating(source + " : " + value);
                        }else {
                            textViewRating.setText("Ratings: N/A");
                        }
                        textViewTitle.setText(response.getString("Title"));
                        post.setTitle(response.getString("Title"));
                        textViewYear.setText("Released: " + response.getString("Released"));
                        textViewDirecor.setText("Director: " + response.getString("Director"));
                        post.setDirector(response.getString("Director"));
                        textViewWriters.setText("Writers: " + response.getString("Writer"));
                        post.setWriter(response.getString("Writer"));
                        textViewPlot.setText("Plot: " + response.getString("Plot"));
                        textViewRuntime.setText("Runtime: " + response.getString("Runtime"));
                        post.setRuntime(response.getString("Runtime"));
                        textViewActors.setText("Actors: " + response.getString("Actors"));
                        post.setRelasedDate(response.getString("Released"));
                        Picasso.with(getApplicationContext())
                                .load(response.getString("Poster"))
                                .into(movieImage);
                        post.setImage(response.getString("Poster"));
                        Log.d("Image myyyimage",post.getImage().toString());

                        textViewBox.setText("Box Office: " + response.getString("BoxOffice"));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error:", error.getMessage());

            }
        });
        queue.add(jsonObjectRequest);

    }

    private void setUpUI() {
        post = new Post();
        textViewTitle = (TextView) findViewById(R.id.textViewMovieTitleDetails);
        movieImage = (ImageView) findViewById(R.id.imageViewMovieDetails);
        textViewYear = (TextView) findViewById(R.id.textViewMovieRelelaseDetails);
        textViewDirecor = (TextView) findViewById(R.id.textViewMovieDirectorDetails);
        textViewCategory = (TextView) findViewById(R.id.textViewMovieCategoryDetails);
        textViewRating = (TextView) findViewById(R.id.textViewMovieRatingDetails);
        textViewWriters = (TextView) findViewById(R.id.textViewMovieWritersDetails);
        textViewPlot = (TextView) findViewById(R.id.textViewMoviePlotDetails);
        textViewBox = (TextView) findViewById(R.id.textViewMovieBoxDetails);
        textViewRuntime = (TextView) findViewById(R.id.textViewRunTime);
        textViewActors = (TextView) findViewById(R.id.textViewMovieActorsDetails);
        buttonShare = (Button) findViewById(R.id.buttonDetailsShare);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonDetailsShare:
                Intent intent = new Intent(this, AddPostActivity.class);
                intent.putExtra("Post", post);
                startActivity(intent);
                break;
        }

    }

}
