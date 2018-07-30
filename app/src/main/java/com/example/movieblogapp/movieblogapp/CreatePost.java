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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity implements View.OnClickListener {
    
    private Button buttonCreatePost;
    private EditText editTextCreatePost;
    private ImageButton imageButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private static final int GALLERY_CODE=1;
    private Uri postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        cPostSetUp();
        buttonCreatePost.setOnClickListener(this);
        imageButton.setOnClickListener(this);
    }
    
    public void cPostSetUp(){
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        buttonCreatePost = (Button) findViewById(R.id.buttonCreatePost);
        imageButton = (ImageButton) findViewById(R.id.imageButtonCreatePost);
        editTextCreatePost = (EditText) findViewById(R.id.editTextCreatePost);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCreatePost:
                createPost();
                break;
            case R.id.imageButtonCreatePost:
                findPostImage();
                break;
        }
    }

    private void createPost() {
        final String input = editTextCreatePost.getText().toString().trim();
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        if(!TextUtils.isEmpty(input) && postImage!=null){
            StorageReference filepath = storageReference.child("User_post_images").child(postImage.getLastPathSegment());
            filepath.putFile(postImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    Uri taskSnapshotDownloadUrl = taskSnapshot.getDownloadUrl();
                    String key = mDatabase.child("Tasks").push().getKey();
                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("userName","");
                    dataToSave.put("title","");
                    dataToSave.put("director","");
                    dataToSave.put("writer","");
                    dataToSave.put("relasedDate","");
                    dataToSave.put("rating","");
                    dataToSave.put("userInput",input);
                    dataToSave.put("image",taskSnapshotDownloadUrl.toString());
                    dataToSave.put("runtime","");
                    dataToSave.put("postDate",formattedDate);
                    dataToSave.put("postID",user.getUid()+key);
                    mDatabase.child("BlogPosts").child(user.getUid()+key).setValue(dataToSave);
                    progressDialog.dismiss();
                    startActivity(new Intent(CreatePost.this,MovieBlog.class));
                    finish();
                }

            });
        }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void findPostImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_CODE && resultCode  == RESULT_OK){
            postImage = data.getData();
            imageButton.setImageURI(postImage);
        }
    }
}
