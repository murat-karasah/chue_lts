package com.marun.chue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private Button mRegister;
    private EditText mName,mEmail,mPass;
    private RadioGroup mradioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(Register.this,AddProfile.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        mRegister = (Button) findViewById(R.id.register);
        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.pass);
        mradioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressdialog = new ProgressDialog(Register.this);
                progressdialog.show();
                progressdialog.setContentView(R.layout.progress_dialog);
                progressdialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                int selectId = mradioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectId);
                if (radioButton==null || radioButton.getText()==null){
                    Toast.makeText(Register.this,"Cinsiyet boş bırakılamaz", Toast.LENGTH_LONG).show();
                    progressdialog.dismiss();
                    return;
                }
                final  String email = mEmail.getText().toString();
                final  String name = mName.getText().toString();
                final  String password = mPass.getText().toString();
                if(email.isEmpty() ){
                    Toast.makeText(Register.this,"Email boş bırakılamaz", Toast.LENGTH_LONG).show();
                    progressdialog.dismiss();
                    return;
                }
                if(name.isEmpty() ){
                    Toast.makeText(Register.this,"İsim boş bırakılamaz", Toast.LENGTH_LONG).show();
                    progressdialog.dismiss();
                    return;
                }
                if(password.isEmpty() ){
                    Toast.makeText(Register.this,"Şifre boş bırakılamaz", Toast.LENGTH_LONG).show();
                    progressdialog.dismiss();
                    return;
                }
                if(password.length() < 8  ){
                    Toast.makeText(Register.this,"Şifre 8 karakterden küçük olamaz", Toast.LENGTH_LONG).show();
                    progressdialog.dismiss();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(Register.this,"Geçerli bir email adresi giriniz", Toast.LENGTH_LONG).show();
                            progressdialog.dismiss();
                        }
                        else{
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name",name);
                            userInfo.put("sex",radioButton.getText().toString());
                            userInfo.put("profileImageUrl","default");
                            userInfo.put("Char","default");
                            userInfo.put("Profile","1");
                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
      mAuth.removeAuthStateListener(firebaseAuthStateListener);
    //    progressdialog.dismiss();
    }
}
