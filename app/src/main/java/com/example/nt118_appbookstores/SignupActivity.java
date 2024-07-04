package com.example.nt118_appbookstores;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.nt118_appbookstores.Models.UserModel;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Context context = SignupActivity.this;
    EditText userName;
    EditText email;
    EditText password;

    EditText enterPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ConstraintLayout signUp = findViewById(R.id.sign_up);
        TextView logIn = findViewById(R.id.log_in);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.user);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signUp() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userNameValue = userName.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String UserID = task.getResult().getUser().getUid();
                            UserModel user = new UserModel(userNameValue, userEmail);

                            DatabaseReference myRef = database.getReference("Users");
                            myRef.setValue("hello");

                            saveUserToFirestore(UserID, user);
                            Toast.makeText(context, "log in succeed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }
                    }
                });
    }

    private void saveUserToFirestore(String userId, UserModel user) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference userRef = firestore.collection("Users").document(userId);
        userRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}