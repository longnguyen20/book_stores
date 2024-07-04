package com.example.nt118_appbookstores;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nt118_appbookstores.databinding.ActivityEditProfileBinding;
import com.example.nt118_appbookstores.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {

    //    ActivityEditProfileBinding binding;
    EditText edtName, edtPhone, edtEmail;
    Button btnUpdate;

    ImageView btnBack;

    CircleImageView imgProfile;
    FirebaseAuth auth;
    FirebaseStorage firebaseStorage;

    FirebaseFirestore firestore;
    FirebaseDatabase database;
    LoadingDialog loadingDialog;
    Uri profileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtEmail = findViewById(R.id.upt_email);
        edtName = findViewById(R.id.upt_name);
        edtPhone = findViewById(R.id.upt_phone);
        imgProfile = findViewById(R.id.img_profile);
        btnUpdate = findViewById(R.id.save);
        btnBack = findViewById(R.id.edit_profile_back_btn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        loadingDialog = new LoadingDialog(this);


        firestore = FirebaseFirestore.getInstance();

        // Retrieve user data from Firestore
        firestore.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Parse the data from the document
                                UserModel userModel = document.toObject(UserModel.class);

                                // Update the UI with Firestore data
                                Glide.with(Edit_profile.this).load(userModel.getProfileImg()).into(imgProfile);
                                edtEmail.setText(userModel.getEmail());
                                edtName.setText(userModel.getName());
                                edtPhone.setText(userModel.getPhone());
                            } else {
                                // Document does not exist
                                Toast.makeText(Edit_profile.this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle failures
                            Toast.makeText(Edit_profile.this, "Error getting user document: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateUserProfile() {
        loadingDialog.ShowDialog("Updating");

        final String userId = FirebaseAuth.getInstance().getUid();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", edtName.getText().toString().trim());
        updatedData.put("phone", edtPhone.getText().toString().trim());

        // Update user profile in Firestore
        firestore.collection("Users").document(userId)
                .update(updatedData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.HideDialog();
                            Toast.makeText(Edit_profile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingDialog.HideDialog();
                            Toast.makeText(Edit_profile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 33 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileUri = data.getData();
            imgProfile.setImageURI(profileUri);

            // Upload the selected image to Firebase Storage
            uploadProfileImageToStorage();
        }
    }

    private void uploadProfileImageToStorage() {
        if (profileUri != null) {
            final StorageReference reference = firebaseStorage.getReference()
                    .child("profile_pictures")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(profileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get the download URL
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Update the user profile image URL in Firestore
                                    updateProfileImageUrlInFirestore(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Edit_profile.this, "Failed to upload image to Firebase Storage", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateProfileImageUrlInFirestore(String imageUrl) {
        final String userId = FirebaseAuth.getInstance().getUid();

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("profileImg", imageUrl);

        // Update the user profile image URL in Firestore
        firestore.collection("Users").document(userId)
                .update(updatedData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.HideDialog();
                            Toast.makeText(Edit_profile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingDialog.HideDialog();
                            Toast.makeText(Edit_profile.this, "Failed to update profile image URL in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
