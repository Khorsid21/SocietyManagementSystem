package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class Adaptermmaccepted extends RecyclerView.Adapter<Adaptermmaccepted.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    public String reason;
    private List<Fetchall> productList;
    private static final String UPLOAD_URL= "https://vivek.ninja/Admin/del_sec.php";
    String maincity,mainsociety;

    public Adaptermmaccepted(Context context, Activity activity, List<Fetchall> productList,String city,String society) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        this.maincity=city;
        this.mainsociety=society;

        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public Adaptermmaccepted.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_mm_accepted, parent, false);
        return new Adaptermmaccepted.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptermmaccepted.myviewholder holder, int position) {
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
        holder.frole.setText(product.getRole());
        holder.fown.setText(product.getPersue());
        Picasso.get().load("https://vivek.ninja/images/" + product.getImage()).into(holder.docImg);
        holder.docImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "HIIHIHIHI", Toast.LENGTH_SHORT).show();

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



        if(product.getCity().equalsIgnoreCase(maincity) && product.getSName().equalsIgnoreCase(mainsociety)){
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                                        JSONObject js = jsonArray.getJSONObject(0);
                                        Log.e("ResponseFull",response);
                                        String res = js.getString("repon");
                                        if(!res.contains("Fail")){
                                            //userEmail= res;
                                            productList.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Security Deleted Successfuly !");
                                            reasonTv.setTextColor(activity.getResources().getColor(R.color.responsePositive));
                                            reasonBtn.setText("OK");
                                            reasonIv.setBackground(activity.getResources().getDrawable(R.drawable.check));
                                            final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialogi.dismiss();
                                                }
                                            });

                                            dialogi.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialogi.setCanceledOnTouchOutside(false);
                                            dialogi.show();

                                            //Toast.makeText(context, "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                        }
                                        else if(res.contains("Fail")){
                                            //loading= new LoadingDialog(login.this);
                                            //loading.dismissDialog();
                                            //Toast.makeText(context, "NOooooooo", Toast.LENGTH_SHORT).show();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Error Occured At Database Side ! Try Again Later !");
                                            reasonTv.setTextColor(activity.getResources().getColor(R.color.responseNegative));
                                            reasonBtn.setText("OK");
                                            reasonIv.setBackground(activity.getResources().getDrawable(R.drawable.wrong));
                                            final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialogi.dismiss();
                                                }
                                            });

                                            dialogi.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialogi.setCanceledOnTouchOutside(false);
                                            dialogi.show();
                                            //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                        }

                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        //loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText(e.getMessage());
                                        reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                activity.getResources().getDimension(R.dimen.result_font));
                                        reasonTv.setTextColor(activity.getResources().getColor(R.color.responseNegative));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(activity.getResources().getDrawable(R.drawable.wrong));
                                        final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                        reasonBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogi.dismiss();
                                            }
                                        });

                                        dialogi.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialogi.setCanceledOnTouchOutside(false);
                                        dialogi.show();
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //loading.dismissDialog();
                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                            alertDialogBuilderUserInput.setView(mViewi);
                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                            reasonTv.setText(error.toString());
                            reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                    activity.getResources().getDimension(R.dimen.result_font));
                            reasonTv.setTextColor(activity.getResources().getColor(R.color.responseNegative));
                            reasonBtn.setText("OK");
                            reasonIv.setBackground(activity.getResources().getDrawable(R.drawable.wrong));
                            final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogi.dismiss();
                                }
                            });

                            dialogi.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialogi.setCanceledOnTouchOutside(false);
                            dialogi.show();
                            Log.e("JSON Parser", "Error parsing data " + error.toString());
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("mobilekey",holder.fphone.getText().toString());
                            map.put("emailkey",holder.femail.getText().toString());
                            return  map;
                        }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                    };
                    RequestQueue rq = Volley.newRequestQueue(context);
                    rq.add(sr);
                }
            });
            if(product.getRole().contains("Security")){
                holder.hblk.setVisibility(View.GONE);
                holder.hown.setVisibility(View.GONE);
                holder.fblck.setVisibility(View.GONE);
                holder.fown.setVisibility(View.GONE);

                holder.showDelBtn.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.showStatusCode.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.showDelBtn.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.showStatusCode.setVisibility(View.VISIBLE);
        }


    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name, phone, email, fname, fphone, femail, fcity, fsoc, fblck, frole, fown, hblk,hown,showDelBtn,showStatusCode;

        ImageView allow, delete, statusImg, docImg;

        public myviewholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.mma_Uname);
            phone = (TextView) itemView.findViewById(R.id.mma_Umob);
            email = (TextView) itemView.findViewById(R.id.mma_Uemail);

            fname = (TextView) itemView.findViewById(R.id.mmaUName);
            fphone = (TextView) itemView.findViewById(R.id.mmaUMob1);
            femail = (TextView) itemView.findViewById(R.id.mmaUemail);
            fcity = (TextView) itemView.findViewById(R.id.mmaUcity);
            fsoc = (TextView) itemView.findViewById(R.id.mmaUsocname);
            fblck = (TextView) itemView.findViewById(R.id.mmaUblock);
            frole = (TextView) itemView.findViewById(R.id.mmaUrole);
            fown = (TextView) itemView.findViewById(R.id.mmaUown);

            hown = (TextView) itemView.findViewById(R.id.mmatvvehtime);
            hblk = (TextView) itemView.findViewById(R.id.mmablcoktv);
            showDelBtn = (TextView) itemView.findViewById(R.id.sectvOStatussd);
            showStatusCode = (TextView) itemView.findViewById(R.id.sectvOStatus);


            docImg = (ImageView) itemView.findViewById(R.id.mmaimage1);
            delete = (ImageView) itemView.findViewById(R.id.mmasecDenyCabBtn);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_mma_list);

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
