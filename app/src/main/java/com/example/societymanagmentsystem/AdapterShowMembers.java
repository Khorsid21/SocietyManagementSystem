package com.example.societymanagmentsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

public class AdapterShowMembers extends RecyclerView.Adapter<AdapterShowMembers.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;


    private List<Fetchall> productList;
    private static final String UPLOAD_URL1 = "https://vivek.ninja/childexit/child_allow.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/childexit/child_deny.php";


    public AdapterShowMembers(Context context, Activity activity, List<Fetchall> productList ) {
        //this.data = data;


        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdapterShowMembers.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_sec_show_members, parent, false);
        return new AdapterShowMembers.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShowMembers.myviewholder holder, int position) {
        Fetchall product = productList.get(position);

        holder.name.setText(product.getName());
        holder.phone.setText(product.getPhoneno());
        holder.email.setText(product.getEmail());

        holder.fname.setText(product.getName());
        holder.fphone.setText(product.getPhoneno());
        holder.femail.setText(product.getEmail());
        holder.fcity.setText(product.getCity());
        holder.fblck.setText(product.getBlock());
        holder.fsoc.setText(product.getSName());
        holder.fown.setText(product.getPersue());

        holder.callMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(activity)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+holder.fphone.getText().toString()));
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
        TextView name, phone, email, fname, fphone, femail, fcity, fsoc, fblck, frole, fown, hblk, hown, showDelBtn, showStatusCode;

        ImageView allow, callMember, statusImg, docImg;

        public myviewholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.ssm_Uname);
            phone = (TextView) itemView.findViewById(R.id.ssm_Umob);
            email = (TextView) itemView.findViewById(R.id.ssm_Uemail);

            fname = (TextView) itemView.findViewById(R.id.ssmUName);
            fphone = (TextView) itemView.findViewById(R.id.ssmUMob1);
            femail = (TextView) itemView.findViewById(R.id.ssmUemail);
            fcity = (TextView) itemView.findViewById(R.id.ssmUcity);
            fsoc = (TextView) itemView.findViewById(R.id.ssmUsocname);
            fblck = (TextView) itemView.findViewById(R.id.ssmUblock);
            fown = (TextView) itemView.findViewById(R.id.ssmUown);


            callMember = (ImageView) itemView.findViewById(R.id.seccall_member);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_ssm_list);

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
