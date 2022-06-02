package com.example.fingerprintahmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView haveAcount_tv;
    MaterialButton register_btn;
    private EditText nama_edt, email_edt, pass_edt, confirmpass_edt;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference Pengguna;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nama_edt =  findViewById(R.id.edt_nama);
        email_edt = findViewById(R.id.edt_email);
        pass_edt = findViewById(R.id.edt_pass);
        confirmpass_edt = findViewById(R.id.edt_confirmpass);

        haveAcount_tv = findViewById(R.id.have_account);
        haveAcount_tv.setOnClickListener(this);
        register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_btn:
                registerUser();
                break;
            case R.id.have_account:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {

    }


    private void registerUser() {

        String Nama = nama_edt.getText().toString().trim();
        String Email = email_edt.getText().toString().trim();
        String Password = pass_edt.getText().toString().trim();
        String ConfirmPass = confirmpass_edt.getText().toString().trim();
        Integer Id = 0;

        if (Nama.isEmpty()){
            nama_edt.setError("Masukkan Nama Lengkap");
            nama_edt.requestFocus();
            return;
        }

        if (Email.isEmpty()){
            email_edt.setError("Masukkan Email");
            email_edt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email_edt.setError("Email tidak valid");
            email_edt.requestFocus();
            return;
        }
        if (Password.isEmpty()){
            pass_edt.setError("Masukkan Password");
            pass_edt.requestFocus();
            return;
        }
        if (Password.length()<6){
            pass_edt.setError("Password Minimal 6");
            pass_edt.requestFocus();
            return;
        }
        if (!ConfirmPass.equals(Password)){
            confirmpass_edt.setError("Password Tidak Sama");
            confirmpass_edt.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(Nama, Email, Password, Id);
                            String userID = mAuth.getUid();
                            assert userID != null;
                            Pengguna = database.getReference("User").child(userID);
                            Pengguna.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(RegisterActivity.this, RegisterFingerprintActivity.class));
                                    finish();
                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}