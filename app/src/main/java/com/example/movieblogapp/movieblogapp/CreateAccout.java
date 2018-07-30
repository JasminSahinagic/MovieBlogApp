package com.example.movieblogapp.movieblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class CreateAccout extends AppCompatActivity implements View.OnClickListener {

        private Button buttonSignUp;
        private ImageButton imageButtonSignUp;
        private EditText editTextFirstName;
        private EditText editTextLastName;
        private EditText editTextYourEmail;
        private EditText editTextPassword;
        private EditText editTextBirthday;
        private FirebaseUser user;
        private FirebaseAuth mAuth;
        private StorageReference storageReference;
        private DatabaseReference databaseReference;
        private ProgressDialog progressDialog;
        private Uri profileImage;
        private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_accout);
        setUpCreateAccount();
        buttonSignUp.setOnClickListener(this);
        imageButtonSignUp.setOnClickListener(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }
    public void setUpCreateAccount(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        buttonSignUp = (Button) findViewById(R.id.buttonAccSignUp);
        imageButtonSignUp = (ImageButton) findViewById(R.id.imageButtonAccount);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextYourEmail = (EditText) findViewById(R.id.editTextYourEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextBirthday = (EditText) findViewById(R.id.editTextBirthday);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAccSignUp:
                createNewUser();
                break;
            case R.id.imageButtonAccount:
                userImage();
                break;
        }
    }

    private void userImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("My Crop")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .setCropMenuCropButtonIcon(R.drawable.donewhite).start(this);
    }

    private void createNewUser() {
        final String name = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextYourEmail.getText().toString().trim();
        final String pass = editTextPassword.getText().toString().trim();
        final String birthday = editTextBirthday.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(birthday) && profileImage != null){
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                StorageReference filepath = storageReference.child("User_profile_images").child(profileImage.getLastPathSegment());
                                filepath.putFile(profileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Uri taskSnapshotDownloadUrl = taskSnapshot.getDownloadUrl();
                                        Map<String, String> dataToSave = new HashMap<>();
                                        dataToSave.put("name",name);
                                        dataToSave.put("lastName",lastName);
                                        dataToSave.put("image",taskSnapshotDownloadUrl.toString());
                                        dataToSave.put("email",email);
                                        dataToSave.put("password",pass);
                                        dataToSave.put("birthday",birthday);
                                        progressDialog.dismiss();
                                        String userId = user.getUid().toString();
                                        databaseReference.child("Users").child(userId).setValue(dataToSave);
                                        startActivity(new Intent(CreateAccout.this,MovieBlog.class));
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(CreateAccout.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImage = result.getUri();
                imageButtonSignUp.setImageURI(profileImage);
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
