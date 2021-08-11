package com.liyana.medalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomMenu;
    Button signOutBtn,setRemindBtn;
    DatabaseReference mBase;
    String UserID;
    TextView thisemailTxt, thisageTxt, thisfullnameTxt;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        signOutBtn     = findViewById(R.id.signOut);
        setRemindBtn   = findViewById(R.id.SetAlarm);
        bottomMenu     = findViewById(R.id.bottom_navigation);
        thisageTxt     = findViewById(R.id.thisage);
        thisemailTxt   = findViewById(R.id.thisemailAddress);
        thisfullnameTxt= findViewById(R.id.thisfullname);

        UserID = fAuth.getCurrentUser().getUid();

        mBase = FirebaseDatabase.getInstance().getReference("User");

        setRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logging out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        try{
            mBase = FirebaseDatabase.getInstance().getReference("User").child(UserID);
            mBase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.getResult().exists()){
                        Log.d(TAG, "task: "+mBase);
                        mBase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Object importDataName = snapshot.child("fullname").getValue();
                                Object importDataAge  = snapshot.child("age").getValue();
                                Object importDataEmail= snapshot.child("email").getValue();

                                if(importDataAge != null){
                                    thisageTxt.setText(importDataAge.toString());
                                }
                                if(importDataEmail != null){
                                    thisemailTxt.setText(importDataEmail.toString());
                                }
                                if(importDataName != null){
                                    thisfullnameTxt.setText(importDataName.toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        bottomMenu.setSelectedItemId(R.id.profile);

        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.aidkit:
                        startActivity(new Intent(getApplicationContext(), AidKitActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                }
                return false;
            }
        });

    }
}