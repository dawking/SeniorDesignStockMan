package com.example.seniordesignstockman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    TextView firebasenameview;
    Button toast;


    private ImageView addItems, deleteItems, scanItems, viewInventory, logout;
    private Button chatActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebasenameview = findViewById(R.id.firebasename);

        // this is for username to appear after login

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");
        firebasenameview.setText("Hoş Geldiniz, "+  resultemail);
        logout = findViewById(R.id.logoutimg);
        addItems = findViewById(R.id.addItems);
        deleteItems =  findViewById(R.id.deleteItems);
        scanItems =  findViewById(R.id.scanItems);
        viewInventory =  findViewById(R.id.viewInventory);
        chatActivity = findViewById(R.id.chatActivity);
        addItems.setOnClickListener(this);
        deleteItems.setOnClickListener(this);
        chatActivity.setOnClickListener(this);
        scanItems.setOnClickListener(this);
        viewInventory.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        if(view.findViewById(R.id.logoutimg) == view){Logout();
        }
        switch (view.getId()){
            case R.id.addItems : i = new Intent(this,additemActivity.class); startActivity(i); break;
            case R.id.deleteItems : i = new Intent(this,deleteItemsActivity.class);startActivity(i); break;
            case R.id.scanItems : i = new Intent(this,scanItemsActivity.class);startActivity(i); break;
            case R.id.viewInventory : i = new Intent(this,viewInventoryActivity.class);startActivity(i); break;
            case R.id.chatActivity : i = new Intent(this, ChatActivity.class); startActivity(i); break;
            default: break;

        }


    }






    public void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(dashboardActivity.this,LoginActivity.class));
        Toast.makeText(dashboardActivity.this,"Çıkış Başarılı", Toast.LENGTH_SHORT).show();

    }


}
