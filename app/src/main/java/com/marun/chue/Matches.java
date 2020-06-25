package com.marun.chue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Matches extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewChatList;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.Adapter mChatlistAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;
    DatabaseReference  mDatabaseChat;
    private String currentUserID;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private String cusrrentUserID;
    private TextView empty_view,empty_view2;
    BottomNavigationView mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        mMenu = findViewById(R.id.menu);
        mMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user:
                        Intent intent = new Intent(Matches.this,Account.class);
                        startActivity(intent);                        return true;
                    case R.id.home:
                        Intent intent2 = new Intent(Matches.this,MainMatchesActivity.class);
                        startActivity(intent2);                        return true;
                    case R.id.matchs:
                        Intent intent3 = new Intent(Matches.this,Matches.class);
                        startActivity(intent3);                        return true;
                }
                return false;
            }
        });
        cusrrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRecyclerViewChatList = (RecyclerView) findViewById(R.id.recyclerViewChatList);
        empty_view = (TextView) findViewById(R.id.empty_view);
        empty_view2 = (TextView) findViewById(R.id.empty_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
     //   mRecyclerView.setNestedScrollingEnabled(false);
     //   mRecyclerView.setHasFixedSize(true);

                mChatListLayoutManager = new LinearLayoutManager(Matches.this , LinearLayoutManager.VERTICAL, false);

        mMatchesLayoutManager = new LinearLayoutManager(Matches.this , LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewChatList.setLayoutManager(mChatListLayoutManager);

        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mChatlistAdapter = new ChatListAdapter (getDataSetChatList(), Matches.this);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), Matches.this);
        mRecyclerViewChatList.setAdapter(mChatlistAdapter);

        mRecyclerView.setAdapter(mMatchesAdapter);
        getUserMatchId();
        getChat();


    }



    private void getChat() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(cusrrentUserID).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        if (match.child("Value").getValue().equals("2")){

                        FetchChatInformation(match.getKey());
                    } }
                }

                    empty_view2.setVisibility(View.VISIBLE);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(cusrrentUserID).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        if (match.child("Value").getValue().equals("1")){

                        FetchMatchInformation(match.getKey());
                        }}
                }

                    empty_view.setVisibility(View.VISIBLE);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FetchMatchInformation(final String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    DatabaseReference chatDb = FirebaseDatabase.getInstance().getReference().child("Chat");
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FetchChatInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String last = "";
                    String name = "";
                    String profileImageUrl = "";
                    if (dataSnapshot.child("connections").child("matches").child(cusrrentUserID).child("last").getValue() != null)
                    {
                        last = dataSnapshot.child("connections").child("matches").child(cusrrentUserID).child("last").getValue().toString();

                    }
                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }

                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    ChatListObject obj = new ChatListObject(userId, name, profileImageUrl,last);
                    resultsChatList.add(obj);
                    mChatlistAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }




    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();
    private ArrayList<ChatListObject> resultsChatList = new ArrayList<ChatListObject>();
    private List<ChatListObject> getDataSetChatList() {
        return resultsChatList;
    }
    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }

}


