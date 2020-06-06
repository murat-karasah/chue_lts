package com.marun.chue;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; // firebaseAuthentication'ı mAuth nesnesini tanıttık
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;//FirebaseAuth.AuthStateListener üye oturum bilgisini kontorl
    private Button login,register;//login ve register adında 2 farklı buton tanıttık
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        login = (Button) findViewById(R.id.login) ;
        register = (Button) findViewById(R.id.register) ;
        FirebaseUser firebaseUser = mAuth.getCurrentUser(); // oturum bilgilerini alıyor.
        if (firebaseUser != null) { // eğer oturum bilgisi boş değilse yani oturum açıksa bu işlemi yaptırıyor
            Intent intent = new Intent(MainActivity.this,MainMatchesActivity.class);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);//Login gider
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);//Register gider
                startActivity(intent);
            }
        });
    }
}
