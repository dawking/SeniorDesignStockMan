package com.example.seniordesignstockman;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(String message, int notificationId) {
        // dışta çalışan arayüz, beklemede
        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("message", message); // mesajı pasla
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "chat_channel")
                .setSmallIcon(R.drawable.ic_account_user)
                .setContentTitle("New message")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Chat notifications";
            String description = "Notifications about new messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("chat_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                String message = value.get("message");
                String email = value.get("email");
                mMessageList.add(email + ": " + message);

                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mMessageList.size() - 1);

                int notificationId = (int) System.currentTimeMillis();
                showNotification(message, notificationId);
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
