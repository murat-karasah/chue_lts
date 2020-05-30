package com.marun.chue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {
    private RangeBar rangebar;
    TextView text_view;
    private TextView mNameField, mAge,mSex, mInfo;
    private Button userAdd;
    private ImageView mProfileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    String age1,age2;
    private String userId, name, age, profileImageUrl,sex,info;
    private Uri resultUri;
    BottomNavigationView mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        rangebar = findViewById(R.id.rangebar1);
        final TextView text_view = findViewById(R.id.text_view);
        mNameField = (TextView) findViewById(R.id.name);
        mAge =(TextView) findViewById(R.id.mAge);
        mInfo =(TextView) findViewById(R.id.info);
        mSex= (TextView) findViewById(R.id.mSex);
        mProfileImage = (ImageView) findViewById(R.id.profileImage);
        userAdd = (Button) findViewById(R.id.userAdd);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
         getUserInfo();
        mMenu = findViewById(R.id.menu);
        mMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user:
                        Intent intent = new Intent(Account.this,Account.class);
                        startActivity(intent);                        return true;
                    case R.id.home:
                        Intent intent2 = new Intent(Account.this,MainMatchesActivity.class);
                        startActivity(intent2);                        return true;
                    case R.id.matchs:
                        Intent intent3 = new Intent(Account.this,Matches.class);
                        startActivity(intent3);                        return true;
                }
                return false;
            }
        });
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, final String leftPinValue, final String rightPinValue) {
                text_view.setText(String.format("%s-%s", leftPinValue, rightPinValue));
                userAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveUserInformation();
                    }
                    private void saveUserInformation() {
                        Map userInfo = new HashMap();
                        userInfo.put("matchsAge1",leftPinValue);
                        userInfo.put("matchsAge2",rightPinValue);
                        mUserDatabase.updateChildren(userInfo);
                    }
                });
            }
            @Override
            public void onTouchStarted(RangeBar rangeBar) {
                Log.d("RangeBar", "Touch ended");
            }
            @Override
            public void onTouchEnded(RangeBar rangeBar) {
                Log.d("RangeBar", "Touch started");
            }
        });
}
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();
                        mNameField.setText(name);
                    }
                    if(map.get("age")!=null){
                        age = map.get("age").toString();
                        mAge.setText(age);
                    }
                   if(map.get("sex")!=null){
                       sex = map.get("sex").toString();
                       mSex.setText(sex);
                   }
                    if(map.get("info")!=null){
                        info = map.get("info").toString();
                        mInfo.setText(info);
                    }
                    Glide.clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void exit(View view){
         mAuth.signOut();
        Intent intent = new Intent(Account.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
