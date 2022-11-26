package com.example.seniordesignstockman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,editTextcPassword;
    public Button UserRegisterBtn;
    private ProgressBar progressBar;
    private ConstraintLayout relativeLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.departmentName);
        editTextEmail = findViewById(R.id.emailRegister);
        editTextPassword = findViewById(R.id.passwordRegister);
        editTextcPassword= findViewById(R.id.confirmPassword);
        UserRegisterBtn= findViewById(R.id.button_register);
//        editTextPhone = findViewById(R.id.edit_text_phone);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        relativeLayout = findViewById(R.id.rellayout);
        mAuth = FirebaseAuth.getInstance();

        //  findViewById(R.id.button_register).setOnClickListener(this);

        UserRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });
      relativeLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(),
                      InputMethodManager.RESULT_UNCHANGED_SHOWN);
          }
      });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }



    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString().trim();
        String cpassword = editTextcPassword.getText().toString().trim();
        // final String phone = editTextPhone.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Boş Olamaz!");
            editTextEmail.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            editTextName.setError("Boş Olamaz!");
            editTextName.requestFocus();
            return;
        }



        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Geçerli bir E-MAIL Adresi giriniz!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Boş Olamaz!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Parolanız 6 haneden az olamaz!");
            editTextPassword.requestFocus();
            return;
        }
        if(!password.equals(cpassword)){
            editTextcPassword.setError("Parolalar eşleşmiyor!");
            editTextcPassword.requestFocus();
            return;
        }

//        if (phone.isEmpty()) {
//            editTextPhone.setError(getString(R.string.input_error_phone));
//            editTextPhone.requestFocus();
//            return;
//        }
//
//        if (phone.length() != 10) {
//            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
//            editTextPhone.requestFocus();
//            return;
//        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

//                            addStudent();

                            FirebaseUser user = mAuth.getCurrentUser();
                            //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            //important to retrive data and send data based on user email
                            FirebaseUser usernameinfirebase = mAuth.getCurrentUser();
                            String UserID=usernameinfirebase.getEmail();
                            // String result = UserID.substring(0, UserID.indexOf("@"));
                            String resultemail = UserID.replace(".","");

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(resultemail).child("UserDetails")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {

                                                Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterActivity.this,MainActivity.class)); //Dashboard vardi
                                            } else {
                                                //display a failure message
                                            }
                                        }
                                    });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Kayıt Başarısız oldu", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }



    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        this.overridePendingTransition(R.anim.fade_in,
                R.anim.fade_out);
    }





}