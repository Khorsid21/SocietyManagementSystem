package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adaptermmrejected extends RecyclerView.Adapter<Adaptermmrejected.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    public String reason;
    private List<FetchallR> productList;

    public Adaptermmrejected(Context context, Activity activity, List<FetchallR> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public Adaptermmrejected.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_mm_rejected, parent, false);
        return new Adaptermmrejected.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptermmrejected.myviewholder holder, int position) {
        FetchallR product = productList.get(position);

        holder.name.setText(product.getName());
        holder.phone.setText(product.getPhoneno());
        holder.email.setText(product.getEmail());

        holder.fname.setText(product.getName());
        holder.fphone.setText(product.getPhoneno());
        holder.femail.setText(product.getEmail());
        holder.fcity.setText(product.getCity());
        holder.fblck.setText(product.getBlock());
        holder.fsoc.setText(product.getsName());
        holder.frole.setText(product.getRole());
        holder.fown.setText(product.getPersue());
        holder.freason.setText(product.getReason());

        Picasso.get().load("https://vivek.ninja/images/" + product.getImage()).into(holder.docImg);

        holder.docImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "HIIHIHIHI", Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);

                alertDialogBuilderUserInput.setView(mView);
                ImageView imgi=mView.findViewById(R.id.imggg);
                //img.setImageDrawable(holder.img.getDrawable());
                //Picasso.get(imgi.getContext().).load("https://vivek.ninja/images/" + product.getImage()).into(imgi);
                /*Picasso.get()
                        .load("https://vivek.ninja/images/" + product.getImage())
                        .into(imgi);*/
                Glide.with(imgi.getContext()).load("https://vivek.ninja/images/" + product.getImage()).fitCenter().into(imgi);
                //img.setMaxWidth(mView.);
                //img.setMaxHeight();

                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                //dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name, phone, email, fname, fphone, femail, fcity, fsoc, fblck, frole, fown,freason;

        ImageView allow, deny, statusImg, docImg;

        public myviewholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.mmr_Uname);
            phone = (TextView) itemView.findViewById(R.id.mmr_Umob);
            email = (TextView) itemView.findViewById(R.id.mmr_Uemail);

            fname = (TextView) itemView.findViewById(R.id.mmrUName);
            fphone = (TextView) itemView.findViewById(R.id.mmrUMob1);
            femail = (TextView) itemView.findViewById(R.id.mmrUemail);
            fcity = (TextView) itemView.findViewById(R.id.mmrUcity);
            fsoc = (TextView) itemView.findViewById(R.id.mmrUsocname);
            fblck = (TextView) itemView.findViewById(R.id.mmrUblock);
            frole = (TextView) itemView.findViewById(R.id.mmrUrole);
            fown = (TextView) itemView.findViewById(R.id.mmrUown);
            freason = (TextView) itemView.findViewById(R.id.mmrUReason);

            docImg = (ImageView) itemView.findViewById(R.id.mmrimage1);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_mmr_list);

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
