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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class AdapterSecDeliOnce extends RecyclerView.Adapter<AdapterSecDeliOnce.myviewholder> {

    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<SecShowDeliOnceData> productList;

    private static final String UPLOAD_URL1 = "https://vivek.ninja/Delivery/deli_once_allow.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/Delivery/deli_once_deny.php";

    public AdapterSecDeliOnce(Context context, Activity activity, List<SecShowDeliOnceData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdapterSecDeliOnce.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_secshow_deli_once, parent, false);
        return new AdapterSecDeliOnce.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSecDeliOnce.myviewholder holder, int position) {
        SecShowDeliOnceData product = productList.get(position);

        holder.name.setText(product.getUname());
        holder.phone.setText(product.getUphone());
        holder.time.setText(product.getODtime());

        holder.fname.setText(product.getUname());
        holder.fphone.setText(product.getUphone());
        holder.femail.setText(product.getUemail());
        holder.fcity.setText(product.getUcity());
        holder.fsoc.setText(product.getUsociety());
        holder.fblck.setText(product.getUblock());
        holder.fdate.setText(product.getODdate());
        holder.ftime.setText(product.getODtime());
        holder.fvalidity.setText(product.getODvalidity());

        loading= new LoadingDialog(activity);
        //Log.e("Status",product.getStatus());
        if(product.getStatus().contains("-1")){
            //pending
            Log.e("Status",product.getStatus()+"Gone");
        }
        else if(product.getStatus().contains("0")){
            //reject
            Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.deny.setVisibility(View.GONE);
            holder.statusImg.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
            holder.fstatus.setVisibility(View.VISIBLE);
            holder.fstatus.setText("Rejected");


        }
        else if(product.getStatus().contains("1")){
            //Accept
            Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.statusImg.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
            holder.deny.setVisibility(View.GONE);
            holder.fstatus.setVisibility(View.VISIBLE);
            holder.fstatus.setText("Accepted");
        }

        holder.allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.startLoadingDialog();
                StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    String res = js.getString("repon");
                                    Log.e("DAS",response+"-"+res);
                                    if(!res.contains("Fail")){
                                        holder.allow.setVisibility(View.GONE);
                                        holder.deny.setVisibility(View.GONE);
                                        holder.statusImg.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
                                        holder.fstatus.setVisibility(View.VISIBLE);
                                        holder.fstatus.setText("Accepted");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("One Time Delivery Allowance Request Accepted Successfully !");
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
                                        //Toast.makeText(context, "SMS Send SuccessFully !", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(res.contains("Fail")){
                                        String status_code = js.getString("rcode");
                                        if(status_code.contains("1111")){
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Currently Twilio API is Facing Some Issues to send SMS, But User Delivery Allowance Request is Accepted Successfully !");
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
                                            //Toast.makeText(context, "Currently Twilio API is Facing Some Issues , Try Again Later ! ", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(status_code.contains("2222")){
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Error Occured At Database Side ! Try After Some Time !");
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
                                            //Toast.makeText(context, "Error Occured At Database Side ! Try After Some Time !", Toast.LENGTH_SHORT).show();
                                        }
                                        //loading= new LoadingDialog(login.this);

                                        //Toast.makeText(context, "Sorry !", Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    loading.dismissDialog();
                                    e.printStackTrace();
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
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismissDialog();
                        Log.e("JSON Parser", "Error parsing data " + error.toString());
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
                        //Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("namekey",holder.name.getText().toString());
                        map.put("mobilekey",holder.phone.getText().toString());
                        map.put("societykey",holder.fsoc.getText().toString());
                        map.put("blockkey",holder.fblck.getText().toString());
                        map.put("Odate",holder.fdate.getText().toString());
                        map.put("Otime",holder.ftime.getText().toString());
                        map.put("Ovalidity",holder.fvalidity.getText().toString());

                        return  map;
                    }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                };
                RequestQueue rq = Volley.newRequestQueue(context);
                rq.add(sr);

                //Toast.makeText(context, "Allowed Toasted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.startLoadingDialog();
                StringRequest sr = new StringRequest(POST, UPLOAD_URL2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    String res = js.getString("repon");

                                    Log.e("DAS",response+"-"+res);
                                    if(!res.contains("Fail")){
                                        holder.allow.setVisibility(View.GONE);
                                        holder.deny.setVisibility(View.GONE);
                                        holder.statusImg.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
                                        holder.fstatus.setVisibility(View.VISIBLE);
                                        holder.fstatus.setText("Rejected");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("One Time Delivery Allowance Request is Rejected Successfully !");
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
                                        //Toast.makeText(context, "SMS Send SuccessFully !", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(res.contains("Fail")){
                                        String status_code = js.getString("rcode");
                                        if(status_code.contains("1111")){
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Currently Twilio API is Facing Some Issues to send SMS, But User Delivery Allowance Request is Rejected Successfully !");
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
                                            //Toast.makeText(context, "Currently Twilio API is Facing Some Issues , Try Again Later ! ", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(status_code.contains("2222")){
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Error Occured At Database Side ! Try After Some Time !");
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
                                            //Toast.makeText(context, "Error Occured At Database Side ! Try After Some Time !", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    loading.dismissDialog();
                                    e.printStackTrace();
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
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismissDialog();
                        Log.e("JSON Parser", "Error parsing data " + error.toString());
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
                        //Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("namekey",holder.name.getText().toString());
                        map.put("mobilekey",holder.phone.getText().toString());
                        map.put("societykey",holder.fsoc.getText().toString());
                        map.put("blockkey",holder.fblck.getText().toString());
                        map.put("Odate",holder.fdate.getText().toString());
                        map.put("Otime",holder.ftime.getText().toString());
                        map.put("Ovalidity",holder.fvalidity.getText().toString());

                        return  map;
                    }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                };
                RequestQueue rq = Volley.newRequestQueue(context);
                rq.add(sr);
                //Toast.makeText(context, "Deny Toasted", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name,phone,time,vehno,fname,fphone,femail,fcity,fsoc,fblck,fdate,ftime,fvalidity,fstatus;

        ImageView allow,deny,statusImg;

        public myviewholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.sec_delio_custom_Uname);
            phone = (TextView) itemView.findViewById(R.id.sec_delio_custom_Utype);
            time = (TextView) itemView.findViewById(R.id.sec_delio_custom_UVehno);

            fname = (TextView) itemView.findViewById(R.id.secdeliotvUName);
            fphone = (TextView) itemView.findViewById(R.id.secdeliotvUMob1);
            femail = (TextView) itemView.findViewById(R.id.secdeliotvUemail);
            fcity = (TextView) itemView.findViewById(R.id.secdeliotvUcity);
            fsoc = (TextView) itemView.findViewById(R.id.secdeliotvUsocname);
            fblck = (TextView) itemView.findViewById(R.id.secdeliotvUblock);
            fdate = (TextView) itemView.findViewById(R.id.secdeliotvUdate);
            ftime = (TextView) itemView.findViewById(R.id.secdeliotvUtime);
            fvalidity = (TextView) itemView.findViewById(R.id.secdeliotvUvalidfor);
            fstatus = (TextView) itemView.findViewById(R.id.secdeliotvOStatus);

            statusImg = (ImageView) itemView.findViewById(R.id.sec_delio_Ustatus);
            allow = (ImageView) itemView.findViewById(R.id.secdelioAllowCabBtn);
            deny = (ImageView) itemView.findViewById(R.id.secdelioDenyCabBtn);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_deli_once_list);

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
