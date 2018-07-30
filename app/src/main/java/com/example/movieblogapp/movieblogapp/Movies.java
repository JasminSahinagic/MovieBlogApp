package com.example.movieblogapp.movieblogapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieblogapp.movieblogapp.Data.MovieRecyclerViewAdapter;
import com.example.movieblogapp.movieblogapp.Model.Movie;
import com.example.movieblogapp.movieblogapp.Util.Constants;
import com.example.movieblogapp.movieblogapp.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movies extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private Button buttonSearch;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setUpMovies();
        buttonSearch.setOnClickListener(this);
    }

    public void setUpMovies(){
        queue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        Prefs prefs = new Prefs(Movies.this);
        String search = prefs.getSearch();
        movieList = getMovieList(search);
        adapter = new MovieRecyclerViewAdapter(Movies.this, movieList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
    }

    public List<Movie> getMovieList(String search){
        movieList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + search + Constants.URL_RIGHT + Constants.API_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray moviesArray = response.getJSONArray("Search");
                    for (int i = 0; i < moviesArray.length(); i++) {
                        JSONObject movieObj = moviesArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("Title"));
                        movie.setYear("Year Released: " + movieObj.getString("Year"));
                        movie.setMovieType("Type: " + movieObj.getString("Type"));
                        movie.setPoster(movieObj.getString("Poster"));
                        movie.setImdbId(movieObj.getString("imdbID"));
                        movieList.add(movie);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        return movieList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSearch:
                searchMovies();
                break;
        }
    }

    public void searchMovies(){
        builder = new AlertDialog.Builder(this);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.search_dialog, null);
        Button buttonKey = (Button) view.findViewById(R.id.buttonDialogSearch);
        final EditText editTextSearchInput = (EditText) view.findViewById(R.id.editTextSearchInput);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        buttonKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = editTextSearchInput.getText().toString();
                Prefs prefs = new Prefs(Movies.this);
                if(!TextUtils.isEmpty(temp)){
                    prefs.setSearch(temp);
                    movieList.clear();
                    getMovieList(temp);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }else{
                    Toast.makeText(Movies.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

