package com.liyana.medalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.liyana.medalarm.adapter.docAdapter;
import com.liyana.medalarm.model.DoctorContact;
import com.liyana.medalarm.model.MedicineLibrary;

import static android.content.ContentValues.TAG;

public class AidKitActivity extends AppCompatActivity {

    RecyclerView doctorRecycler;
    Button addDocBtn;
    docAdapter theadapter;
    DatabaseReference dbRef;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String UserID;
    Query query;
    BottomNavigationView bottomMenu;
    DoctorContact doctorContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aid_kit);

        UserID = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "UserID: "+UserID);

        doctorContact = new DoctorContact();
        doctorRecycler = findViewById(R.id.aidKitRecycler);
        addDocBtn      = findViewById(R.id.docBtn);
        bottomMenu     = findViewById(R.id.bottom_navigation);

        addDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AidKitActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        bottomMenu.setSelectedItemId(R.id.aidkit);

        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.aidkit:
                        startActivity(new Intent(getApplicationContext(), AidKitActivity.class));
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        try{

            doctorRecycler.setLayoutManager(new LinearLayoutManager(this));


            dbRef = FirebaseDatabase.getInstance().getReference("Doctor");

            //query = dbRef.orderByChild("doctorID").equalTo(UserID);

            FirebaseRecyclerOptions<DoctorContact> options = new FirebaseRecyclerOptions.Builder<DoctorContact>()
                    .setQuery(dbRef, DoctorContact.class)
                    .build();

            theadapter = new docAdapter(options);
            doctorRecycler.setAdapter(theadapter);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        theadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        theadapter.stopListening();
    }
}