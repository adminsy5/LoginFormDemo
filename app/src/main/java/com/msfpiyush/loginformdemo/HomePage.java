package com.msfpiyush.loginformdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.msfpiyush.loginformdemo.databinding.ActivityHomePageBinding;

import java.util.Objects;

public class HomePage extends AppCompatActivity {
ActivityHomePageBinding binding;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
           binding.btnLogin.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(isValidate()) {
                       firebaseAuth.signInWithEmailAndPassword(Objects.requireNonNull(binding.EnterEmail.getText()).toString(), Objects.requireNonNull(binding.EnterPass.getText()).toString())
                               .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                   @Override
                                   public void onSuccess(AuthResult authResult) {
                                       showToast("Login Successfully");
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       showToast(e.getMessage());
                                   }
                               });
                   }
               }
           });
    }

    private boolean isValidate() {
        if(Objects.requireNonNull(binding.EnterEmail.getText()).toString().isEmpty()){
            showToast("Please enter email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.EnterEmail.getText().toString()).matches()){
            showToast("Enter Valid Email");
            return false;
        } else if ((Objects.requireNonNull(binding.EnterPass.getText()).toString().isEmpty())) {
            showToast("Enter Your Password");
            return false;
        }
        return true;
    }


    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}