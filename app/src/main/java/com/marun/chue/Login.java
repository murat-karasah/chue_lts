package com.marun.chue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class Login extends AppCompatActivity {
    private Button mLogin;
    private EditText mEmail, mPassword;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){

                    Intent intent = new Intent(Login.this,MainMatchesActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mLogin = (Button) findViewById(R.id.login);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.pass);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String email = mEmail.getText().toString();
                final  String password = mPassword.getText().toString();



                if(email.isEmpty()  || password.isEmpty() ){
                    Toast.makeText(Login.this,"Email ve Şifre giriniz", Toast.LENGTH_LONG).show();

                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(Login.this,"Giriş Yap Hatalı", Toast.LENGTH_LONG).show();

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

    }
}
