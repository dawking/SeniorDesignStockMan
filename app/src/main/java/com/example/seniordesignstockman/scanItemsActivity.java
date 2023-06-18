package com.example.seniordesignstockman;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class scanItemsActivity extends AppCompatActivity {
    public static EditText resultsearcheview;
    private FirebaseAuth firebaseAuth;
    ImageButton scantosearch;
    Button searchbtn;
    Button takePictureButton;
    Adapter adapter;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_items);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        resultsearcheview = findViewById(R.id.searchfield);
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);

        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);


        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));


        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivitysearch.class));
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = resultsearcheview.getText().toString();
                firebasesearch(searchtext);
            }
        });


    }
    private void firebasesearch(String searchtext) {



        Query firebaseSearchQuery = mdatabaseReference.orderByChild("itembarcode").startAt(searchtext).endAt(searchtext + "\uf8ff");

        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>()
                .setQuery(firebaseSearchQuery, Items.class)
                .build();

        FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>(options) {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position, @NonNull Items items) {
                usersViewHolder.setDetails(getApplicationContext(), items.getItembarcode(), items.getItemcategory(), items.getItemname(), items.getItemprice());
            }
        };

        // Set the adapter to the RecyclerView
        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(View itemView){
            super(itemView);
            mView =itemView;
        }

        public void setDetails(Context ctx,String itembarcode, String itemcategory, String itemname, String itemprice){
            TextView item_barcode = (TextView) mView.findViewById(R.id.viewitembarcode);
            TextView item_name = (TextView) mView.findViewById(R.id.viewitemname);
            TextView item_category = (TextView) mView.findViewById(R.id.viewitemcategory);
            TextView item_price = (TextView) mView.findViewById(R.id.viewitemprice);

            item_barcode.setText(itembarcode);
            item_category.setText(itemcategory);
            item_name.setText(itemname);
            item_price.setText(itemprice);
        }

    }
}
