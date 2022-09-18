package com.example.societymanagmentsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class emgnadapter extends RecyclerView.Adapter<emgnadapter.myviewholder> {

    Context context;
    public Activity activity;
    public String worker_phoneno;
    String fwdType;
    private List<Emgnnodata> productList;


    public emgnadapter(Context context, Activity activity, List<Emgnnodata> productList,String fwdType) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        this.fwdType=fwdType;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public emgnadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_emgnno_list, parent, false);
        return new emgnadapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull emgnadapter.myviewholder holder, int position) {
        Emgnnodata product = productList.get(position);
        //Toast.makeText(context, "Hiiiiiiiiii"+fwdType, Toast.LENGTH_SHORT).show();
        holder.name.setText(product.getName());
        holder.role.setText(product.getType());

        holder.foldName.setText(product.getName());
        holder.foldAddrs.setText(product.getAddress());
        holder.foldEmail.setText(product.getEmail());
        holder.foldphoneno.setText(product.getPhoneno());
        holder.foldRole.setText(product.getType());

        if(fwdType.contains("Blood")){
            holder.foldBlood.setVisibility(View.VISIBLE);
            holder.foldBloodTV.setVisibility(View.VISIBLE);
            //holder.foldBloodTV.setVisibility(View.VISIBLE);
            holder.foldBlood.setText(product.getSpeciality());
        }

        Picasso.get().load("https://vivek.ninja/emgn/emgnimage/"+product.getImage()).into(holder.cv);

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
        //holder.foldSpecs.setText(product.getSpeciality());
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, email, foldphoneno, foldRole, foldName, foldEmail, role, foldAddrs, foldBloodTV,foldBlood;
        Button call_btn;
        ImageButton imgbtn;
        CircleImageView cv;
        ConstraintLayout extendedLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.emgnno_custom_name);
            role = (TextView) itemView.findViewById(R.id.emgnno_custom_type);


            foldName = itemView.findViewById(R.id.tvEmgnNoName);
            foldRole = itemView.findViewById(R.id.tvEmgnnoType);
            foldEmail = itemView.findViewById(R.id.tvEmgnnoEm);

            foldBlood = itemView.findViewById(R.id.tvEmgnNoBloodType);
            foldBloodTV = itemView.findViewById(R.id.tv_EmgnNo_Blood);

            foldphoneno = itemView.findViewById(R.id.tvEmgnNoNumber);
            foldAddrs = itemView.findViewById(R.id.tvEmgnNoAddress);

            cv = itemView.findViewById(R.id.emgnno_Uphoto);

            call_btn = itemView.findViewById(R.id.custom_callingbtn_emgnno);
            final FoldingCell fc = itemView.findViewById(R.id.emgnno_list_folding_cell);

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
