package com.example.seniordesignstockman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText eMail;
    private EditText Password;
    private Button Login;
    private TextView passwordreset;
    private EditText passwordresetemail;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private FirebaseAuth auth;
    private ProgressDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.emailSignIn);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        relativeLayout = findViewById(R.id.rellayoutlogin);
        passwordreset = findViewById(R.id.forgotPassword);
        passwordresetemail = findViewById(R.id.emailSignIn);
        progressBar = (ProgressBar) findViewById(R.id.progressBars);
        progressBar.setVisibility(View.INVISIBLE);
        auth = FirebaseAuth.getInstance();
        processDialog = new ProgressDialog(this);


        Login.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                try {
                    validate(eMail.getText().toString(), Password.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Gerekli Alanlar Boş", Toast.LENGTH_SHORT).show();

                }

            }
        });

        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPasword();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager immn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immn.hideSoftInputFromWindow(relativeLayout.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });


    }


    public void resetPasword() {
        final String resetemail = passwordresetemail.getText().toString();

        if (resetemail.isEmpty()) {
            passwordresetemail.setError("E-Mail Giriniz!");
            passwordresetemail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(resetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Hesabınızı nasıl kurtarabileceğinize dair yönerge gönderilmiştir!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Mesaj gönderimi başarısız oldu!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void validate(String userEmail, String userPassword) {
        if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
            processDialog.setMessage("Lütfen Bekleyin");
            processDialog.show();

            auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        processDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Giriş BAŞARILI!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "E-mail veya Parola Yanlış Girildi!", Toast.LENGTH_LONG).show();
                        processDialog.dismiss();
                    }
                }
            });
        }
        else    {
            passwordresetemail.setError("E-Mail Giriniz!");
            passwordresetemail.requestFocus();

            Password.setError("Parola Giriniz");
            passwordreset.requestFocus();
            vibration vib = new vibration();
            vib.vibrator();
            return;

        }
    }
}

