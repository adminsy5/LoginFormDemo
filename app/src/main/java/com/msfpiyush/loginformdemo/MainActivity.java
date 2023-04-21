package com.msfpiyush.loginformdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msfpiyush.loginformdemo.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Intent intent;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        binding.AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidate()){
                    mAuth.createUserWithEmailAndPassword(binding.EditEmail.getText().toString(),binding.EditPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent ig = new Intent(MainActivity.this, HomePage.class);
                                ig.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ig);
                                showToast("Successfully Registered");
                            }else{
                                showToast(Objects.requireNonNull(task.getException()).toString());
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isValidate() {
        if(Objects.requireNonNull(binding.EditEmail.getText()).toString().isEmpty()){
            showToast("Email can't Be Empty");
            return false;
        }else if (Objects.requireNonNull(binding.EditPass.getText()).toString().isEmpty()){
            showToast("Please Enter Your Password ");
            return false;
        }else if (binding.EditPass.getText().toString().length()<6){
            showToast("Password Length Must be 6 char");
            return false;
        }else if(Objects.requireNonNull(binding.EditPassConf.getText()).toString().isEmpty()){
            showToast("Please Enter Confirm Password");
            return false;
        }else if(!binding.EditPassConf.getText().toString().matches(binding.EditPass.getText().toString())){
            showToast("Confirm Password Must Be Same");
            return false;
        }
        return true;
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}