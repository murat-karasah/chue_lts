package com.marun.chue;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Matches extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        mAuth = FirebaseAuth.getInstance();
    }
    public void exit(View view){
        mAuth.signOut();
        Intent intent = new Intent(Matches.this,MainActivity.class);
        startActivity(intent);
        finish();}
}
