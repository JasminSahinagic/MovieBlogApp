package com.example.movieblogapp.movieblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieblogapp.movieblogapp.Data.MovieBlogAdapter;
import com.example.movieblogapp.movieblogapp.Model.Post;
import com.example.movieblogapp.movieblogapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private TextView textViewDirector;
    private TextView textViewWriter;
    private TextView textViewRelased;
    private TextView textViewRating;
    private TextView textViewRuntime;
    private Button buttonShare ;
    private EditText editTextInput;
    private ImageView imageViewPost;
    private TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseRef;
    private DatabaseReference databaseRef2;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;
    private Uri blogImage;
    private StorageReference storageReference;
    private TextView postDate;
    private String userName;
    private Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        postSetUp();
        textViewTitle.setText("Title: "+post.getTitle());
        textViewDirector.setText("Director: "+post.getDirector());
        textViewWriter.setText("Writer: "+post.getWriter());
        textViewRelased.setText("Relased: "+post.getRelasedDate());
        textViewRating.setText("Rating: "+post.getRating());
        textViewRuntime.setText("Runtime: "+post.getRuntime());
        Picasso.with(this).load(post.getImage()).into(imageViewPost);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        postDate.setText(formattedDate);
        buttonShare.setOnClickListener(this);
    }

    public void postSetUp(){
        postDate = (TextView) findViewById(R.id.textViewPostDate);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        post = (Post) getIntent().getSerializableExtra("Post");
        databaseRef2 =database.getReference().child("Users").child(user.getUid());
        textViewTitle = (TextView) findViewById(R.id.textViewBlogPostTitle);
        textViewDirector = (TextView) findViewById(R.id.textViewBlogPostDirector);
        textViewWriter = (TextView) findViewById(R.id.textViewBlogPostWriters);
        textViewRelased= (TextView) findViewById(R.id.textViewBlogPostReleasedDate);
        textViewRating = (TextView) findViewById(R.id.textViewBlogPostRating);
        textViewRuntime= (TextView) findViewById(R.id.textViewBlogPostRuntime);
        buttonShare = (Button) findViewById(R.id.buttonPostShare);
        editTextInput = (EditText) findViewById(R.id.editTextPostText);
        imageViewPost = (ImageView) findViewById(R.id.imageViewBlogPostImage);
        textView = (TextView) findViewById(R.id.textViewBlogPostUserName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonPostShare:
                addPost();
                break;
        }
    }

    private void addPost() {
        final String userInput = editTextInput.getText().toString().trim();
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        if(!TextUtils.isEmpty(userInput) ){
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    String key = databaseRef.child("BlogPosts").push().getKey();
                    String temp = user.getUid().toString()+key;
                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("userName","");
                    dataToSave.put("title",post.getTitle());
                    dataToSave.put("director",post.getDirector());
                    dataToSave.put("writer",post.getWriter());
                    dataToSave.put("relasedDate",post.getRelasedDate());
                    dataToSave.put("rating",post.getRating());
                    dataToSave.put("userInput",userInput);
                    dataToSave.put("image",post.getImage());
                    dataToSave.put("runtime",post.getRuntime());
                    dataToSave.put("postDate",formattedDate);
                    dataToSave.put("postID",user.getUid()+key);
                    databaseRef.child("BlogPosts").child(user.getUid()+key).setValue(dataToSave);
                    progressDialog.dismiss();
                    startActivity(new Intent(AddPostActivity.this,MovieBlog.class));
                    finish();
        }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}

