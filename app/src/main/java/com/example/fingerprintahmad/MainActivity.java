package com.example.fingerprintahmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView hi_user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference user_ref, pintu_ref, refRiwayat;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private Button open_bt;
    private ImageView lock, profile;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        open_bt = findViewById(R.id.bt_open);
        hi_user = findViewById(R.id.selamat);
        lock = findViewById(R.id.lockLogo);
        profile = findViewById(R.id.profile);

        firebaseUser = auth.getCurrentUser();
        String currenUser = firebaseUser.getUid();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });

        user_ref = database.getReference("User");
        user_ref.child(currenUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);
                if (userProfile != null){
                    String nama = userProfile.NamaLengkap;
                    hi_user.setText("Hi, " + nama);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        open_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                pintu_ref = database.getReference("kontrolPintu");
                pintu_ref.setValue(1);

                user_ref = database.getReference("User");
                user_ref.child(currenUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user!=null){
                            String nama = user.NamaLengkap;
                            Integer id = user.Id;

                            calendar = Calendar.getInstance();
                            simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
                            String date;
                            date = simpleDateFormat.format(calendar.getTime());
                            Map map = new HashMap();
                            map.put("fullname", nama);
                            map.put("id", id);
                            map.put("time", date);
                            refRiwayat = database.getReference("history").push();
                            refRiwayat.setValue(map);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lock.setImageResource(R.drawable.ic_baseline_lock_24);
                        pintu_ref.setValue(0);
                    }
                }, 3000);

            }
        });


    }
}