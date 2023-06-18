package com.example.seniordesignstockman;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText mMessageEditText;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;
    private Button mClearButton;
    private List<String> mMessageList = new ArrayList<>();
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageDatabaseReference = FirebaseDatabase.getInstance().getReference().child("messages");

        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
        mRecyclerView = findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(mMessageList);
        mMessageRecyclerView.setAdapter(mAdapter);

        mAuth = FirebaseAuth.getInstance();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageEditText.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String email = user.getEmail();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("message", message);
                        map.put("email", email.substring(0, email.indexOf("@")));
                        mMessageDatabaseReference.push().setValue(map);
                        mMessageEditText.setText("");
                    }
                }
            }
        });
        mClearButton = findViewById(R.id.clearButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessageList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                String message = value.get("message");
                String email = value.get("email");
                mMessageList.add(email + ": " + message);

                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mMessageList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        mMessageDatabaseReference.addChildEventListener(mChildEventListener);
    }
}
