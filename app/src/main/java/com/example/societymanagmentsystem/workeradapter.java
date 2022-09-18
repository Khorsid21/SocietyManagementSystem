package com.example.societymanagmentsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class workeradapter extends RecyclerView.Adapter<workeradapter.myviewholder> {

    Context context;
    public Activity activity;
    public String worker_phoneno;
    private List<workerdata> productList;


    public workeradapter(Context context, Activity activity, List<workerdata> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @Override
    public workeradapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_workser_list, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull workeradapter.myviewholder holder, int position) {
        workerdata product = productList.get(position);


        holder.name.setText(product.getName());
        holder.role.setText(product.getWorkertype());

        holder.foldName.setText(product.getName());
        holder.foldAddrs.setText(product.getAddress());
        holder.foldEmail.setText(product.getEmail());
        holder.foldphoneno.setText(product.getPhoneno());
        holder.foldRole.setText(product.getWorkertype());
        holder.foldSpecs.setText(product.getSpeciality());

        worker_phoneno = product.getPhoneno();
        //Toast.makeText(context, worker_phoneno, Toast.LENGTH_SHORT).show();
        Picasso.get().load("https://vivek.ninja/images/3.jpg").into(holder.cv);

        holder.call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(activity)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+holder.foldphoneno.getText().toString()));
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, email, foldphoneno,foldRole,foldName,foldEmail, role, foldAddrs, foldSpecs;
        Button call_btn;
        ImageButton imgbtn;
        CircleImageView cv;
        ConstraintLayout extendedLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.custom_name_worker);
            role = (TextView) itemView.findViewById(R.id.custom_type_worker);

            foldName = itemView.findViewById(R.id.tvWorkerName);
            foldRole = itemView.findViewById(R.id.tvWorkerType);
            foldEmail = itemView.findViewById(R.id.tvWorkerEm);
            foldSpecs = itemView.findViewById(R.id.tvWorkerSpeciality);
            foldphoneno = itemView.findViewById(R.id.tvWorkerNumber);
            foldAddrs = itemView.findViewById(R.id.tvWorkerAddress);



            cv = itemView.findViewById(R.id.photo);
            call_btn = itemView.findViewById(R.id.custom_callingbtn_worker);
            final FoldingCell fc = itemView.findViewById(R.id.folding_cell);

            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);
                }
            });



        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
