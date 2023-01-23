package com.msfpiyush.loginformdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.List;

public class InsertData extends AppCompatActivity {
    EditText EditName,EditCourse,EditEmail;
    Button InsertButton,BrowseButton;
    ImageView UploadImg;
    Uri filepath;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        EditName=findViewById(R.id.EditName);
        EditCourse=findViewById(R.id.EditCourse);
        EditEmail=findViewById(R.id.EditEmail);
        InsertButton=findViewById(R.id.InsertButton);
        BrowseButton=findViewById(R.id.BrowseButton);

        BrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InsertData.this, "Try", Toast.LENGTH_SHORT).show();
                requestPermissionsListener();
//                Dexter.withContext(InsertData.this)
//                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .withListener(new CompositePermissionListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Toast.makeText(InsertData.this, "working", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                                Toast.makeText(InsertData.this, "Error!", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }, DialogOnDeniedPermissionListener.Builder
//                                .withContext(InsertData.this).withTitle("Read permission")
//                                .withMessage("Reading the sdcard is required").withButtonText(android.R.string.yes)
//                                .build())).check();
                //.check();
//                Dexter.withContext(getApplicationContext())
//                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Toast.makeText(InsertData.this, "working", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                                Toast.makeText(InsertData.this, "error", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }).check();
            }
        });
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null) //l-->f and f-->l
                {
                    filepath=result.getData().getData();
                    try
                    {
                        InputStream inputStream=getContentResolver().openInputStream(filepath);
                        bitmap= BitmapFactory.decodeStream(inputStream);
                        UploadImg.setImageBitmap(bitmap);
                    }
                    catch(Exception ignored)
                    {

                    }
                }
            }
        });

//        BrowseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Dexter.withContext(getApplicationContext())
//                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Intent ig=new Intent(Intent.ACTION_GET_CONTENT);
//                                ig.setType("image/*");
//                                activityResultLauncher.launch(ig);
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                                Toast.makeText(InsertData.this, "Error !", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }).check();
//            }
//        });
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase();
            }
        });
    }

    private void requestPermissionsListener() {
        Dexter.withContext(this)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(Manifest.permission.CAMERA,
                        // below is the list of permissions
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS)
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            Toast.makeText(InsertData.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, we will show user a dialog message.
//                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                // below line is use to run the permissions on same thread and to check the permissions
                .onSameThread().check();
    }

    // below is the shoe setting dialog method which is use to display a dialogue message.
//    private void showSettingsDialog() {
//        // we are displaying an alert dialog for permissions
//        AlertDialog.Builder builder = new AlertDialog.Builder(InsertData.this);
//
//        // below line is the title for our alert dialog.
//        builder.setTitle("Need Permissions");
//
//        // below line is our message for our dialog
//        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
//        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
//            // this method is called on click on positive button and on clicking shit button
//            // we are redirecting our user from our app to the settings page of our app.
//            dialog.cancel();
//            // below is the intent from which we are redirecting our user.
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            Uri uri = Uri.fromParts("package", getPackageName(), null);
//            intent.setData(uri);
//            startActivityForResult(intent, 101);
//        });
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            // this method is called when user click on negative button.
//            dialog.cancel();
//        });
//        // below line is used to display our dialog
//        builder.show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        if(requestCode==1 && resultCode==RESULT_OK) //l-->f and f-->l
//        {
//            filepath=data.getData();
//            try
//            {
//                InputStream inputStream=getContentResolver().openInputStream(filepath);
//                bitmap= BitmapFactory.decodeStream(inputStream);
//                UploadImg.setImageBitmap(bitmap);
//            }
//            catch(Exception ex)
//            {
//
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void uploadToFirebase()
    {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference uploader= storage.getReference().child("image1");
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(InsertData.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                });
    }
}