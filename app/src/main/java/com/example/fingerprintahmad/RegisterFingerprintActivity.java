package com.example.fingerprintahmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterFingerprintActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refID, refUser, refRemoveFinger, refSucces, refEnroll, refNewId;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private Spinner spinner;
    private CheckBox chPilih, chTempel1, chTempel2, chDone;
    private ProgressBar progressBar;
    private MaterialButton button;
    int id_jari;
    String userId;
    int dataEnroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fingerprint);

        user = auth.getCurrentUser();
        userId = auth.getUid();

        spinner = findViewById(R.id.spinner);
        chDone = findViewById(R.id.ch_berhasil);
        chPilih = findViewById(R.id.ch_pilihId);
        chTempel1 = findViewById(R.id.ch_tempeljari1);
        chTempel2 = findViewById(R.id.ch_tempeljari2);
        button = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.progressbarrfinger);

        refRemoveFinger = database.getReference("fingerprint").child("feedback").child("cekJari1");
        refRemoveFinger.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer integer = snapshot.getValue(Integer.class);
                if (integer == 1) {
                    chTempel1.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refSucces = database.getReference("fingerprint").child("feedback").child("cekJari2");
        refSucces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer integer = snapshot.getValue(Integer.class);
                if (integer == 1) {
                    chTempel2.setChecked(true);
                    chDone.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chPilih.isChecked()) {
                    chPilih.setError("Pilih ID!");
                    chPilih.requestFocus();
                    return;
                }
                if (!chTempel1.isChecked()) {
                    chTempel1.setError("Tempel Jari Pada sensor");
                    chTempel1.requestFocus();
                    return;
                }
                if (!chDone.isChecked()) {
                    chDone.setError("Tempel Jari yang sama");
                    chDone.requestFocus();
                }

                progressBar.setVisibility(View.VISIBLE);

                refEnroll = database.getReference("fingerprint").child("enroll");
                refEnroll.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dataEnroll = snapshot.getValue(Integer.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (dataEnroll == 0) {
                    startActivity(new Intent(RegisterFingerprintActivity.this, MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                    refRemoveFinger.setValue(0);
                    refSucces.setValue(0);
                    finish();
                }
            }
        });

        String[] value = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
                "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
                "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
                "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
                "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65",
                "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76",
                "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87",
                "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98",
                "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109",
                "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120",
                "121", "122", "123", "124", "125", "126", "127"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterFingerprintActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = adapterView.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                id_jari = Integer.parseInt(id);
                refID = database.getReference("fingerprint").child("new_id");
                if (id_jari > 0) {
                    refID.setValue(id_jari).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chPilih.setChecked(true);
                                refUser = database.getReference("User");

                                Map map = new HashMap();
                                map.put("Id", id_jari);
                                refUser.child(userId).updateChildren(map);

                                Toast.makeText(RegisterFingerprintActivity.this, "Id Sidik Jari di Setor", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterFingerprintActivity.this, "Gagal memasukkan Id sidik jari", Toast.LENGTH_LONG).show();
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refID.setValue(0);
                                }
                            }, 1000);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}