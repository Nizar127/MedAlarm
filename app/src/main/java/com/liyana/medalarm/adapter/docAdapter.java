package com.liyana.medalarm.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liyana.medalarm.AidKitActivity;
import com.liyana.medalarm.R;
import com.liyana.medalarm.model.DoctorContact;
import com.squareup.picasso.Picasso;

public class docAdapter extends FirebaseRecyclerAdapter<DoctorContact, docAdapter.DocViewHolder> {
  String TAG ="";
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public docAdapter(@NonNull  FirebaseRecyclerOptions<DoctorContact> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull  docAdapter.DocViewHolder holder, int position, @NonNull  DoctorContact model) {
        holder.contactEmail.setText(model.getEmailDoctor());
        holder.contactType.setText(model.getDoctorType());
        holder.contactNum.setText(model.getPhoneDoctor());
        holder.contactName.setText(model.getNameDoctor());
        Picasso.get().load(model.getImageDoc()).into(holder.imgDoc);
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"No Edit Available", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = getRef(position).getKey();
                Log.d(TAG, "Delete Position: "+key);

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Doctor");
                dbref.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), AidKitActivity.class);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        });


    }

    @NonNull

    @Override
    public DocViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new docAdapter.DocViewHolder(view);
    }

    public class DocViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDoc, editBtn, deleteBtn;
        TextView contactName, contactNum, contactType, contactEmail;


        public DocViewHolder(@NonNull  View itemView) {
            super(itemView);

            imgDoc       = itemView.findViewById(R.id.imageContactDoc);
            contactName  = itemView.findViewById(R.id.textNameDoc);
            contactNum   = itemView.findViewById(R.id.textNumberDoc);
            contactType  = itemView.findViewById(R.id.textTypeofContact);
            contactEmail = itemView.findViewById(R.id.textEmailDoc);
            editBtn      = itemView.findViewById(R.id.editBtnItem);
            deleteBtn    = itemView.findViewById(R.id.deleteBtnItem);


        }
    }
}
