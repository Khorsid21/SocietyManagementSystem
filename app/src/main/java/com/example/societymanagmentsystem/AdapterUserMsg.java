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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;

public class AdapterUserMsg extends RecyclerView.Adapter<AdapterUserMsg.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    String SMS_CODE="ACc95f4b7da47abe11bf62150fa6aa4d66";
    private List<UserMsgData> productList;
    private static final String UPLOAD_URL1 = "https://vivek.ninja/SecMsg/delete_UMentry.php";



    public AdapterUserMsg(Context context, Activity activity, List<UserMsgData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }


    @NonNull
    @Override
    public AdapterUserMsg.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_user_msg, parent, false);
        return new AdapterUserMsg.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUserMsg.myviewholder holder, int position) {
        UserMsgData product = productList.get(position);

        holder.name.setText(product.getUname());
        holder.msg.setText(product.getMsg());

        holder.fname.setText(product.getUname());
        holder.femail.setText(product.getUemail());
        holder.fphone.setText(product.getUphone());
        holder.fsocname.setText(product.getUsoc());
        holder.fcity.setText(product.getUcity());
        holder.fblock.setText(product.getUblk());
        holder.fMsg.setText(product.getMsg());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(activity)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+product.getUphone()));
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

                //Toast.makeText(context, "Called", Toast.LENGTH_SHORT).show();
            }
        });
        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+product.getUphone());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                //it.putExtra("sms_body", "SMS Application ! - Yoo Homie !");
                activity.startActivity(it);

               // Toast.makeText(context, "SMS", Toast.LENGTH_SHORT).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AndExAlertDialog.Builder(activity)
                        .setTitle("Warning !")
                        .setMessage("Are You Sure ? It Will be Deleted Permanently !")
                        .setPositiveBtnText("Yes")
                        .setNegativeBtnText("No;")
                        .setImage(R.drawable.warning, 15)
                        .setCancelableOnTouchOutside(true)
                        .OnPositiveClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {

                                loading = new LoadingDialog(activity);
                                loading.startLoadingDialog();

                                StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    Log.e("RRR",response);
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                                    JSONObject js = jsonArray.getJSONObject(0);
                                                    String res = js.getString("repon");
                                                    if (!res.contains("no")) {
                                                        loading.dismissDialog();
                                                        productList.remove(holder.getAdapterPosition());
                                                        notifyDataSetChanged();
                                                        //productList.remove(holder.getAdapterPosition());
                                                        Toast.makeText(context, "Society Member's Message Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                        //notifyDataSetChanged();
                                                    } else if (res.contains("no")) {
                                                        loading.dismissDialog();
                                                        Toast.makeText(context, "Something Went Wrong ! Try Again Letter !", Toast.LENGTH_SHORT).show();

                                                    }
                                                    //Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                                                    //DO Delete
                                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    loading.dismissDialog();
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismissDialog();
                                        Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();

                                        //String path = getPath(filepath);
                                        //map.put("namekey",name.getText().toString().trim());

                                        map.put("namekey", holder.fname.getText().toString());
                                        map.put("emailkey", holder.femail.getText().toString());
                                        map.put("phonekey", holder.fphone.getText().toString());
                                        map.put("sockey", holder.fsocname.getText().toString());
                                        map.put("blkkey", holder.fblock.getText().toString());
                                        map.put("citykey", holder.fcity.getText().toString());
                                        map.put("msgkey", holder.fMsg.getText().toString());
                                        return map;
                                    }
                                };
                                RequestQueue rq = Volley.newRequestQueue(context.getApplicationContext());
                                rq.add(sr);


                            }
                            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                        })
                        .OnNegativeClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {
                                Toast.makeText(context.getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();


                //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name,msg, femail, fphone, fsocname, fblock, fcity, fname, fMsg;

        ImageView call, sms,delete;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.MUM_uname);
            msg = (TextView) itemView.findViewById(R.id.MUM_umsg);

            fname = (TextView) itemView.findViewById(R.id.MUMUName);
            femail = (TextView) itemView.findViewById(R.id.MUMemail);
            fphone = (TextView) itemView.findViewById(R.id.MUMphone);
            fsocname = (TextView) itemView.findViewById(R.id.MUMsocname);
            fblock = (TextView) itemView.findViewById(R.id.MUMblock);
            fcity = (TextView) itemView.findViewById(R.id.MUMcity);
            fMsg = (TextView) itemView.findViewById(R.id.MUMMessage);

            call =  itemView.findViewById(R.id.MUMCallBtn);
            delete =  itemView.findViewById(R.id.MUM_del);
            sms =  itemView.findViewById(R.id.MUMMsgBtn);




            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_user_msg_list);
            /*del_btn = itemView.findViewById(R.id.custom_deletebtn_worker);
            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_worker_list);
            cv = itemView.findViewById(R.id.addWorkerphoto);
            fragName = itemView.findViewById(R.id.tvaddWorkerName);
            fragEmail = itemView.findViewById(R.id.tvaddWorkerEm);
            fragPhoneno = itemView.findViewById(R.id.tvaddWorkerNumber);
            fragAddress = itemView.findViewById(R.id.tvaddWorkerAddress);
            fragSpecs = itemView.findViewById(R.id.tvaddWorkerSpeciality);
            fragRole = itemView.findViewById(R.id.tvaddWorkerType);*/

            delete = itemView.findViewById(R.id.MUM_del);

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
