package com.marun.chue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.net.Uri;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProfile extends AppCompatActivity {
    private EditText mAge ,mInfo;
    private Button mAdd;
    private ProgressDialog progressdialog;

    private ImageView mImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mCostomerDatabase;
    private String userId,age,info,profileImageUrl;
    private Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);
        mAge = (EditText) findViewById(R.id.age);
        mInfo = (EditText) findViewById(R.id.info);
        mImage = (ImageView) findViewById(R.id.image);
        mAdd=(Button) findViewById(R.id.add);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCostomerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfromation();
            }
        });
    }
    private void saveUserInfromation() {
        progressdialog = new ProgressDialog(AddProfile.this);
        progressdialog.show();
        progressdialog.setContentView(R.layout.progress_dialog);
        progressdialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        age = mAge.getText().toString();
        info = mInfo.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("age",age);
        userInfo.put("info",info);
        userInfo.put("Profile","2");
        if(age.isEmpty() ){
            Toast.makeText(AddProfile.this,"Yaş boş bırakılamaz", Toast.LENGTH_LONG).show();
            return;
        }
        if(info.isEmpty() ){
            Toast.makeText(AddProfile.this,"Hakkında", Toast.LENGTH_LONG).show();
            return;
        }
        mCostomerDatabase.updateChildren(userInfo);
        if (resultUri == null){
            Toast.makeText(AddProfile.this,"Lütfen profil resmi giriniz", Toast.LENGTH_LONG).show();
            return;
        }
            if (resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImageUrl").child(userId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20 ,byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
           uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                   task.addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
        String imageLink = uri.toString();
        Map userInfo = new HashMap();
        userInfo.put("profileImageUrl", imageLink);
        mCostomerDatabase.updateChildren(userInfo);
        finish();
        return;
    }
});
               }
           });
        }else{}
        Intent intent = new Intent(AddProfile.this,Test.class);
        startActivity(intent);
        finish();
        return;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mImage.setImageURI(resultUri);    }}
    public void exit(View view){
        mAuth.signOut();
        Intent intent = new Intent(AddProfile.this,MainActivity.class);
        startActivity(intent);
        finish();}
}
