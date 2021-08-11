package com.liyana.medalarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class EditorActivity extends AppCompatActivity {

    Button addDoctorBtn;
    EditText nameDoc,phoneDoc,emailDoc, typeDoc;
    ImageView doctorImgHere, backBtn;
    String addDataEditor, UserID, nameDocTxt, phoneDocTxt, emailDocTxt, typeDocTxt, downloadImgUrl;;
    DatabaseReference mBase;
    String saveCurrentDate, saveCurrentTime, productRandomKey;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Uri imgUri;
    StorageReference editImgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mBase = FirebaseDatabase.getInstance().getReference("Doctor");
        backBtn = findViewById(R.id.backhomebtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editImgRef = FirebaseStorage.getInstance().getReference().child("Doctor Images");

        addDoctorBtn = findViewById(R.id.addingDocBtn);
        nameDoc = findViewById(R.id.nameEditText);
        phoneDoc = findViewById(R.id.phoneEditText);
        emailDoc = findViewById(R.id.emailEditText);
        typeDoc = findViewById(R.id.doctorTypeText);
        doctorImgHere = findViewById(R.id.doctorImg);

        addDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    submitData();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        doctorImgHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imgUri).setAspectRatio(1,1).start(EditorActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            doctorImgHere.setImageURI(imgUri);
        }else {
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();

        }

    }

    private void submitData() {
        nameDocTxt = nameDoc.getText().toString().trim();
        phoneDocTxt = phoneDoc.getText().toString().trim();
        emailDocTxt = emailDoc.getText().toString().trim();
        typeDocTxt = typeDoc.getText().toString().trim();

        if(imgUri == null){
            Toast.makeText(this, "Product image required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nameDocTxt)) {
            Toast.makeText(this, "Doctor name is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneDocTxt)) {
            Toast.makeText(this, "Phone Number is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailDocTxt)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(typeDocTxt)) {
            Toast.makeText(this, "Type is required", Toast.LENGTH_SHORT).show();
        }else{
            StoreInformation(nameDocTxt,phoneDocTxt,emailDocTxt,typeDocTxt);
        }
    }

    private void StoreInformation(String nameDocTxt, String phoneDocTxt, String emailDocTxt, String typeDocTxt) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;
        addDataEditor = mBase.push().getKey();
        //Log.d(TAG, "saveDocData: "+addDataEditor);

        final StorageReference filePath = editImgRef.child(imgUri.getLastPathSegment() + addDataEditor + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imgUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(EditorActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditorActivity.this, "Product Image uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull  Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(EditorActivity.this, "Product image url received", Toast.LENGTH_SHORT).show();

                            saveDocData(nameDocTxt,emailDocTxt,phoneDocTxt,typeDocTxt);
                        }
                    }
                });
            }
        });




    }

    private void saveDocData(String nameDocTxt, String emailDocTxt, String phoneDocTxt, String typeDocTxt) {
        HashMap<String, Object> doctorMap = new HashMap<>();
        doctorMap.put("doctorID",UserID);
        doctorMap.put("nameDoctor", nameDocTxt);
        doctorMap.put("ImageDoc", downloadImgUrl);
        doctorMap.put("emailDoctor", emailDocTxt);
        doctorMap.put("phoneDoctor", phoneDocTxt);
        doctorMap.put("doctorType",typeDocTxt);



        mBase.child(addDataEditor).updateChildren(doctorMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(EditorActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(EditorActivity.this,"New Doctor Inserted", Toast.LENGTH_SHORT).show();

                }else{
                    String message = task.getException().toString();
                    Toast.makeText(EditorActivity.this,"Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}