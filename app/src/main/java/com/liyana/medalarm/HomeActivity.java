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
import android.widget.CalendarView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.liyana.medalarm.adapter.MedAdapter;
import com.liyana.medalarm.model.MedicineLibrary;

import static android.content.ContentValues.TAG;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomMenu;
    RecyclerView thishomerecycler;
    MedAdapter myadapter;
    DatabaseReference mBase;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String UserID;
    Query query;
    FloatingActionButton fab;
    CalendarView calendar;
    MedicineLibrary medicineLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        medicineLibrary = new MedicineLibrary();

        calendar = findViewById(R.id.calendarView);
        thishomerecycler = findViewById(R.id.homeRecycler);
        calendar.getDate();

        fab = findViewById(R.id.addMedicine);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddMedicineActivity.class);
                startActivity(intent);
            }
        });
        UserID = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "UserID: "+UserID);

        try{
            thishomerecycler.setLayoutManager(new LinearLayoutManager(this));

            mBase = FirebaseDatabase.getInstance().getReference("Medicine");
            query = mBase.orderByChild("medicineID").equalTo(UserID);
            Log.d(TAG, "mBase: "+mBase);

            FirebaseRecyclerOptions<MedicineLibrary> options = new FirebaseRecyclerOptions.Builder<MedicineLibrary>()
                    .setQuery(query,MedicineLibrary.class)
                    .build();
            myadapter = new MedAdapter(options);
            thishomerecycler.setAdapter(myadapter);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"no data",Toast.LENGTH_SHORT).show();
        }



        bottomMenu     = findViewById(R.id.bottom_navigation);

        bottomMenu.setSelectedItemId(R.id.home);

        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        return true;
                    case R.id.aidkit:
                        startActivity(new Intent(getApplicationContext(), AidKitActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myadapter.stopListening();
    }
}