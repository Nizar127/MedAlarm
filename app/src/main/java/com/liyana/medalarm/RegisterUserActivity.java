package com.liyana.medalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class RegisterUserActivity extends AppCompatActivity {

    EditText mSignupname,mSignupemail, mSignuppassword, mAge;
    Button Register;
    DatabaseReference mBase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String fullname, email,age, password, productRandomKey, saveCurrentDate,saveCurrentTime, USERID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mSignupname     = findViewById(R.id.fullName);
        mSignupemail    = findViewById(R.id.email);
        mSignuppassword = findViewById(R.id.password);
        mAge            = findViewById(R.id.age);
        Register        = findViewById(R.id.signUp);

        mBase = FirebaseDatabase.getInstance().getReference("User");

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {
        fullname = mSignupname.getText().toString().trim();
        email    = mSignupemail.getText().toString().trim();
        password = mSignuppassword.getText().toString().trim();
        age      = mAge.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(this, "Fullname required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "Age is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password Required", Toast.LENGTH_SHORT).show();
        }

        else if(password.length() <6){
            Toast.makeText(this,"Password need to be longer", Toast.LENGTH_SHORT).show();

        } else{
            storeData();
        }
    }

    private void storeData() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    USERID = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "Get Data:" + USERID);

                    Toast.makeText(RegisterUserActivity.this,"User Created", Toast.LENGTH_SHORT).show();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("fullname",fullname);
                    userMap.put("age",age);
                    userMap.put("email", email);

                    mBase.child(USERID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                //loadingBar.dismiss();
                                Toast.makeText(RegisterUserActivity.this,"User is added successfully..", Toast.LENGTH_SHORT ).show();
                            }else{
                                String message = task.getException().toString();
                                Toast.makeText(RegisterUserActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }

            }
        });

    }
}