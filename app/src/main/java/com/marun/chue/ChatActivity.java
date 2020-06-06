package com.marun.chue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private String currentUId;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private EditText mSendEditText;
    private ImageView mSendButton;
    private String currentUserID, matchId, chatId;
    DatabaseReference mDatabaseUser, mDatabaseChat,mDatabaseX;
    private DatabaseReference usersDb;
    BottomNavigationView mMenu;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        matchId = getIntent().getExtras().getString("matchId");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        scrollView = findViewById(R.id.nestedScroll);
        currentUId = mAuth.getCurrentUser().getUid();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseX = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");
        getChatId();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }


        });
        mMenu = findViewById(R.id.menu);
        mMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user:
                        Intent intent = new Intent(ChatActivity.this,Account.class);
                        startActivity(intent);                        return true;
                    case R.id.home:
                        Intent intent2 = new Intent(ChatActivity.this,MainMatchesActivity.class);
                        startActivity(intent2);                        return true;
                    case R.id.matchs:
                        Intent intent3 = new Intent(ChatActivity.this,Matches.class);
                        startActivity(intent3);                        return true;
                }
                return false;
            }
        });
    }
    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            isLastMessage();

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);

    }
    private void isLastMessage() {
        mDatabaseX.addListenerForSingleValueEvent(new ValueEventListener() {
            String sendMessageText = mSendEditText.getText().toString();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mDatabaseX.child("Last").setValue(sendMessageText);
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("last").setValue(sendMessageText);
                    //  mDatabaseX.child(currentUId).child("connections").child("matches").child(matchId).child("Value").setValue(keys);


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
   private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    isConnectionMatch();

                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);

                    getChatMessages();


                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void isConnectionMatch() {
        mDatabaseX.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String keys = "2";
                    mDatabaseX.child("Value").setValue(keys);
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("Value").setValue(keys);
                  //  mDatabaseX.child(currentUId).child("connections").child("matches").child(matchId).child("Value").setValue(keys);


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();

                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                }

            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}
