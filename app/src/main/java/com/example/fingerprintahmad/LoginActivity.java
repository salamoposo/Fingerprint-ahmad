package com.example.fingerprintahmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private MaterialButton login_btn;
    private TextView noAccount_tv;
    private EditText editText_email, editText_pass;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressBar = findViewById(R.id.progress_barrLogin);
        login_btn = findViewById(R.id.bt_login);
        noAccount_tv = findViewById(R.id.no_account);
        editText_email = findViewById(R.id.editText_email);
        editText_pass = findViewById(R.id.editText_pass);

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = editText_email.getText().toString().trim();
                String Pass = editText_pass.getText().toString().trim();

                if (Email.isEmpty()){
                    editText_email.setError("Masukkan Email");
                    editText_email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    editText_email.setError("Email tidak valid");
                    editText_email.requestFocus();
                    return;
                }
                if (Pass.isEmpty()){
                    editText_pass.setError("Masukkan Password");
                    editText_pass.requestFocus();
                    return;
                }
                if (Pass.length()<6){
                    editText_pass.setError("Password Minimal 6 Karakter");
                    editText_pass.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        noAccount_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}