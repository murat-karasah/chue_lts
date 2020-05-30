package com.marun.chue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMatchesActivity extends AppCompatActivity {
    private cards cards_data[];
    private com.marun.chue.arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;
    ListView listView;
    List<cards> rowItems;
    BottomNavigationView mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_matches);
        mMenu = findViewById(R.id.menu);
        mMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user:
                        Intent intent = new Intent(MainMatchesActivity.this,Account.class);
                        startActivity(intent);                        return true;
                    case R.id.home:
                        Intent intent2 = new Intent(MainMatchesActivity.this,MainMatchesActivity.class);
                        startActivity(intent2);                        return true;
                    case R.id.matchs:
                        Intent intent3 = new Intent(MainMatchesActivity.this,Matches.class);
                        startActivity(intent3);                        return true;
                }
                return false;
            }
        });
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        checkUserSex();
        rowItems = new ArrayList<cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("Negatif").child(currentUId).setValue(true);
                Toast.makeText(MainMatchesActivity.this, "Sola", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("Pozitif").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(MainMatchesActivity.this, "Sağa", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }
            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });
        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainMatchesActivity.this, "Sağa veye Sola kaydırınız", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("Pozitif").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(MainMatchesActivity.this, "Eşleşme Başarılı", Toast.LENGTH_LONG).show();
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private String userChar;
    private String userSex;
    private String oppositeUserSex;
    private Integer userAge, maxAge=55;
    private Integer minAge=18;

    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer profile = Integer.parseInt(dataSnapshot.child("Profile").getValue().toString());
                    if (profile == 1){
                        Toast.makeText(MainMatchesActivity.this,"Lütfen Profilinizi Oluşturunuz", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainMatchesActivity.this,AddProfile.class);
                        startActivity(intent);
                        return;
                    }
                    if (profile == 2){
                        Toast.makeText(MainMatchesActivity.this,"Lütfen Testi Tamamlayınız", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainMatchesActivity.this,Test.class);
                        startActivity(intent);
                        return;
                    }
                    if (dataSnapshot.child("age").getValue() != null && dataSnapshot.child("matchsAge1").getValue() != null &&
                            dataSnapshot.child("matchsAge2").getValue() != null ){
                        userAge= Integer.parseInt(dataSnapshot.child("age").getValue().toString());
                        minAge =Integer.parseInt(dataSnapshot.child("matchsAge1").getValue().toString());
                         maxAge =Integer.parseInt(dataSnapshot.child("matchsAge2").getValue().toString());
                    }
                    if (dataSnapshot.child("Char").getValue() != null){
                        userChar= dataSnapshot.child("Char").getValue().toString();

                    }
                    if (dataSnapshot.child("sex").getValue() != null) {
                        userSex = dataSnapshot.child("sex").getValue().toString();
                        switch (userSex) {
                            case "Erkek":
                                oppositeUserSex = "Kadın";
                                break;
                            case "Kadın":
                                oppositeUserSex = "Erkek";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void getOppositeSexUsers() {
        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    if (isSuitableForUser(snap)){

                        String profileImageUrl = "default";
                       if (!snap.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = snap.child("profileImageUrl").getValue().toString();
                     }
                        cards item = new cards(snap.getKey(), snap.child("name").getValue().toString(), profileImageUrl, Integer.parseInt(snap.child("age").getValue().toString()),snap.child("info").getValue().toString());
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private boolean isSuitableForUser(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
            if (!dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)){
               return false;
           }
            Map<String, List<String>> map = new HashMap<>();
            map.put("INFP", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("ENFP", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("INFJ", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("ENFJ", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP","ISFP"}));
            map.put("INTJ", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("ENTJ", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("INTP", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("ENTP", Arrays.asList(new String[] {"INFP", "ENFP", "INTJ","ENTJ", "INTP", "ENTP"}));
            map.put("ISFP", Arrays.asList(new String[] {"ESTJ", "ESFJ", "ENFJ"}));
            map.put("ESFP", Arrays.asList(new String[] {"ISTJ", "ISFJ"}));
            map.put("ISTP", Arrays.asList(new String[] {"ESTJ", "ISFJ"}));
            map.put("ESTP", Arrays.asList(new String[] {"ISTJ", "ISFJ"}));
            map.put("ISFJ", Arrays.asList(new String[] {"ESTJ", "ISFJ", "ISTJ","ESTP", "ESFP"}));
            map.put("ESFJ", Arrays.asList(new String[] {"ESTJ", "ISFJ", "ISTJ","ISFJ", "ISTP", "ISFP"}));
            map.put("ISTJ", Arrays.asList(new String[] {"ESTJ", "ISFJ", "ISTJ","ISFJ", "ESTP", "ESFP"}));
            map.put("ESTJ", Arrays.asList(new String[] {"ESTJ", "ISFJ", "ISTJ","ISFJ", "ISTP", "ISFP", "INTP"}));
            String Char = dataSnapshot.child("Char").getValue().toString();
            if (map.get(userChar).contains(Char))      {
            }else{
                return false;
            }

           Integer age = Integer.parseInt(dataSnapshot.child("age").getValue().toString());


         //  int range = Math.abs(age-userAge);
           if (age>= minAge &&  age<= maxAge){
               return true;
           }
           return false;
        }else{
            return false;
        }
    }


}

