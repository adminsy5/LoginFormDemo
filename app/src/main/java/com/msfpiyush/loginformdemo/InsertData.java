package com.msfpiyush.loginformdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.InputStream;

public class InsertData extends AppCompatActivity {
    EditText EditName,EditCourse,EditEmail;
    Button InsertButton,BrowseButton,ShowInsertData;
    ImageView UserImg;
    Uri filepath;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        UserImg=findViewById(R.id.UserImg);
        EditName=findViewById(R.id.EditName);
        EditCourse=findViewById(R.id.EditCourse);
        EditEmail=findViewById(R.id.EditEmail);
        ShowInsertData=findViewById(R.id.ShowInsertData);
        InsertButton=findViewById(R.id.InsertButton);
        BrowseButton=findViewById(R.id.BrowseButton);

        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null )
                {
                        filepath = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(filepath);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            UserImg.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });

        BrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig=new Intent();
                ig.setType("image/*")
                        .setAction(Intent.ACTION_PICK);
                activityResultLauncher.launch(ig);
            }
        });
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
                uploadToFirebase();
            }
        });
        ShowInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent igx=new Intent(InsertData.this,CrudOpPerform.class);
                startActivity(igx);            }
        });
    }
    private void process() {
        String name=EditName.getText().toString().trim();
        String course=EditCourse.getText().toString().trim();
        String email=EditEmail.getText().toString().trim();

        dataholder object=new dataholder(name,course,email);
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference node=db.getReference("Friend");

        node.child(name).setValue(object);
        EditName.setText("");
        EditCourse.setText("");
        EditEmail.setText("");

        Toast.makeText(this, "Data Inserted !", Toast.LENGTH_SHORT).show();
    }
    private void uploadToFirebase()
    {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader= storage.getReference().child("upload").child("demo.jpg");
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(InsertData.this, "Hurray ! ❤️", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}