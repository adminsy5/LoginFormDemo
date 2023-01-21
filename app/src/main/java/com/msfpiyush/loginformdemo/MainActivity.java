package com.msfpiyush.loginformdemo;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
Button btnSignUp,btnSignIn;
EditText EditEmail,EditPass,EditPassConf;
TextView RegisterText;
String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//ProgressDialog progressDialog;
FirebaseAuth mAuth;
FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegisterText=findViewById(R.id.RegisterText);
        EditEmail=findViewById(R.id.EditEmail);
        EditPass=findViewById(R.id.EditPass);
        EditPassConf=findViewById(R.id.EditPassConf);
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignUp=findViewById(R.id.btnSignUp);
        //progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ig=new Intent(MainActivity.this,HomePage.class);
                startActivity(ig);
            }
        });
        btnSignUp.setOnClickListener(view -> PerForAuth());
        ActionBar ad=getSupportActionBar();
        ColorDrawable cd=new ColorDrawable(Color.parseColor("#7F525D"));
        ad.setBackgroundDrawable(cd);
    }
    private void PerForAuth()
    {
        String email=EditEmail.getText().toString();
        String password=EditPass.getText().toString();
        String confPassword=EditPassConf.getText().toString();

        if(!email.matches(emailPattern))
        {
            EditEmail.setError("Enter Correct Email !");
        }else if (password.isEmpty()||password.length()<6)
        {
            EditPass.setError("Enter Right Password !");
        }else if (!password.equals(confPassword))
        {
            EditPassConf.setError("Password not match !");
        }else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        SendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Registration Successfully !", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(MainActivity.this, "Oops !"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void SendUserToNextActivity() {
        Intent ig=new Intent(MainActivity.this,HomePage.class);
        ig.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ig);
    }
}