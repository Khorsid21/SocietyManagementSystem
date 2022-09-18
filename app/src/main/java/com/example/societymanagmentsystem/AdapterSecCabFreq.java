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

public class AdapterSecCabFreq extends RecyclerView.Adapter<AdapterSecCabFreq.myviewholder> {

    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<SecShowFreqData> productList;

    private static final String UPLOAD_URL1 = "https://vivek.ninja/AllowCab/cab_freq_allow.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/AllowCab/cab_freq_deny.php";

    public AdapterSecCabFreq(Context context, Activity activity, List<SecShowFreqData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdapterSecCabFreq.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_secshow_cab_freq, parent, false);
        return new AdapterSecCabFreq.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSecCabFreq.myviewholder holder, int position) {
        SecShowFreqData product = productList.get(position);

        loading= new LoadingDialog(activity);

        if(product.getStatus().contains("-1")){
            //pending
            Log.e("Status",product.getStatus()+"Gone");
        }
        else if(product.getStatus().contains("0")){
            //reject
            Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.deny.setVisibility(View.GONE);
            holder.statusImage.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
            holder.fstatustv.setVisibility(View.VISIBLE);
            holder.fstatustv.setText("Rejected");


        }
        else if(product.getStatus().contains("1")){
            //Accept
            Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.statusImage.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
            holder.deny.setVisibility(View.GONE);
            holder.fstatustv.setVisibility(View.VISIBLE);
            holder.fstatustv.setText("Accepted");
        }


        holder.name.setText(product.getUname());
        holder.phone.setText(product.getUphone());
        holder.stime.setText(product.getOstarttime());
        holder.etime.setText(product.getOendtime());

        holder.fname.setText(product.getUname());
        holder.fphone.setText(product.getUphone());
        holder.femail.setText(product.getUemail());
        holder.fcity.setText(product.getUcity());
        holder.fsoc.setText(product.getUsociety());
        holder.fblck.setText(product.getUblock());
        holder.fvalidity.setText(product.getOvaldays());
        holder.fstime.setText(product.getOstarttime());
        holder.fetime.setText(product.getOendtime());
        String strMain = product.getOdays();
        String arrSplit = strMain.replaceAll(",","\n");
        //for (int i=0; i < arrSplit.length; i++)
        //{
            //System.out.println(arrSplit[i]);
            //Log.e("StringDay",arrSplit);
        //}
        holder.fDays.setText(arrSplit);



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
                                        holder.statusImage.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
                                        holder.fstatustv.setVisibility(View.VISIBLE);
                                        holder.fstatustv.setText("Accepted");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Frequent Time Cab Allowance Request Accepted Successfully !");
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
                                            reasonTv.setText("Currently Twilio API is Facing Some Issues to send SMS, But User Cab Allowance Request is Accepted Successfully !");
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

                        map.put("Odays",product.getOdays());
                        map.put("Ovaldays",holder.fvalidity.getText().toString());
                        map.put("Ostime",holder.fstime.getText().toString());
                        map.put("Oetime",holder.fetime.getText().toString());

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
                                        holder.statusImage.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
                                        holder.fstatustv.setVisibility(View.VISIBLE);
                                        holder.fstatustv.setText("Rejected");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Frequent Time Cab Allowance Request Rejected Successfully !");
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
                                            reasonTv.setText("Currently Twilio API is Facing Some Issues to send SMS, But User Cab Allowance Request is Rejected Successfully !");
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

                        map.put("Odays",product.getOdays());
                        map.put("Ovaldays",holder.fvalidity.getText().toString());
                        map.put("Ostime",holder.fstime.getText().toString());
                        map.put("Oetime",holder.fetime.getText().toString());

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
        TextView name, phone, stime, etime, fname, fphone, femail, fcity, fsoc, fblck, fvalidity, fstime, fetime, fDays,fstatustv;

        ImageView allow, deny,statusImage;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.secf_custom_Uname);
            phone = (TextView) itemView.findViewById(R.id.secf_custom_Utype);
            stime = (TextView) itemView.findViewById(R.id.secf_custom_Ustime);
            etime = (TextView) itemView.findViewById(R.id.secf_custom_Uetime);

            fname = (TextView) itemView.findViewById(R.id.secftvUName);
            fphone = (TextView) itemView.findViewById(R.id.secftvUMob1);
            femail = (TextView) itemView.findViewById(R.id.secftvUemail);
            fcity = (TextView) itemView.findViewById(R.id.secftvUcity);
            fsoc = (TextView) itemView.findViewById(R.id.secftvUsocname);
            fblck = (TextView) itemView.findViewById(R.id.secftvUblock);
            fvalidity = (TextView) itemView.findViewById(R.id.secftvvalper);
            fstime = (TextView) itemView.findViewById(R.id.secftvUstime);
            fetime = (TextView) itemView.findViewById(R.id.secftvUetime);
            fDays = (TextView) itemView.findViewById(R.id.secftvUdays);
            fstatustv = (TextView) itemView.findViewById(R.id.secftvOStatus);

            allow = (ImageView) itemView.findViewById(R.id.secfAllowCabBtn);
            deny = (ImageView) itemView.findViewById(R.id.secfDenyCabBtn);
            statusImage = (ImageView) itemView.findViewById(R.id.secf_Ustatus);


            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_cab_freq_list);
            /*del_btn = itemView.findViewById(R.id.custom_deletebtn_worker);
            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_worker_list);
            cv = itemView.findViewById(R.id.addWorkerphoto);
            fragName = itemView.findViewById(R.id.tvaddWorkerName);
            fragEmail = itemView.findViewById(R.id.tvaddWorkerEm);
            fragPhoneno = itemView.findViewById(R.id.tvaddWorkerNumber);
            fragAddress = itemView.findViewById(R.id.tvaddWorkerAddress);
            fragSpecs = itemView.findViewById(R.id.tvaddWorkerSpeciality);
            fragRole = itemView.findViewById(R.id.tvaddWorkerType);*/

            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);
                }
            });

            /*del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AndExAlertDialog.Builder(activity)
                            .setTitle("Warning !")
                            .setMessage("Are You Sure ? Once You Delete This Worker , then Worker Will be Deleted Permanently??")
                            .setPositiveBtnText("Yes")
                            .setNegativeBtnText("No;")
                            .setImage(R.drawable.warning, 15)
                            .setCancelableOnTouchOutside(true)
                            .OnPositiveClicked(new AndExAlertDialogListener() {
                                @Override
                                public void OnClick(String input) {


                                    StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {

                                                        JSONObject jobj = new JSONObject(response);
                                                        String res = jobj.getString("response");
                                                        if (res.contains("Yes")) {
                                                            productList.remove(holder.getAdapterPosition());
                                                            notifyDataSetChanged();

                                                        } else if (res.contains("No")) {
                                                            Toast.makeText(context, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        //Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                                                        //DO Delete
                                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> map = new HashMap<>();

                                            //String path = getPath(filepath);
                                            //map.put("namekey",name.getText().toString().trim());

                                            map.put("mobilekey", holder.phoneno.getText().toString());
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


                }
            });*/

        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
