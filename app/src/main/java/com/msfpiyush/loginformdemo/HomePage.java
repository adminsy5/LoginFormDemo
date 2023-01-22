package com.msfpiyush.loginformdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
Button btnForPass,btnLogin;
EditText EnterPass,EnterEmail;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btnLogin=findViewById(R.id.btnLogin);
        EnterEmail=findViewById(R.id.EnterEmail);
        EnterPass=findViewById(R.id.EnterPass);
        firebaseAuth=FirebaseAuth.getInstance();
        btnForPass=findViewById(R.id.btnForPass);

        ActionBar ad=getSupportActionBar();
        ColorDrawable cd=new ColorDrawable(Color.parseColor("#7F525D"));
        ad.setBackgroundDrawable(cd);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=EnterEmail.getText().toString();
                String password=EnterPass.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(HomePage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        GotoCrud();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomePage.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnForPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=EnterEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(HomePage.this, "Email Sent !", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void GotoCrud() {
        Intent ig=new Intent(HomePage.this,InsertData.class);
        startActivity(ig);
    }
}