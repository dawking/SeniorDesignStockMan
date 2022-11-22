package com.example.seniordesignstockman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user!=null){
            finish();
            Toast.makeText(this, "Giriş Yapıldı", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this, dashboardActivity.class));
        }


    }
    public void login (View view)
    {
        startActivity(new Intent(this,LoginActivity.class));

    }
    public void register (View view){
        startActivity(new Intent(this,RegisterActivity.class));

    }
}