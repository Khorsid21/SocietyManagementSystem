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

public class AdapterGPOnce extends RecyclerView.Adapter<AdapterGPOnce.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<SecShowGOnceData> productList;
    private static final String UPLOAD_URL1 = "https://vivek.ninja/Gatepass/gatepass_once_allow.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/Gatepass/gatepass_once_deny.php";



    public AdapterGPOnce(Context context, Activity activity, List<SecShowGOnceData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdapterGPOnce.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_secshow_gatepass_once, parent, false);
        return new AdapterGPOnce.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGPOnce.myviewholder holder, int position) {
        SecShowGOnceData product = productList.get(position);

        holder.name.setText(product.getUname());
        holder.phone.setText(product.getUphone());
        holder.gatepassno.setText(product.getGatepassno());

        holder.fname.setText(product.getUname());
        holder.fphone.setText(product.getUphone());
        holder.fGPno.setText(product.getGatepassno());
        holder.fsoc.setText(product.getUsoc());
        holder.fblck.setText(product.getUblk());
        holder.fdate.setText(product.getGdate());
        holder.ftime.setText(product.getGtime());
        holder.fvalidity.setText(product.getGval());

        loading= new LoadingDialog(activity);

        if(product.getGstatus().contains("-1")){
            //pending
            //Log.e("Status",product.getStatus()+"Gone");
        }
        else if(product.getGstatus().contains("0")){
            //reject
            //Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.deny.setVisibility(View.GONE);
            holder.statusImg.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
            holder.fstatus.setVisibility(View.VISIBLE);
            holder.fstatus.setText("Gatepass Denied");
        }
        else if(product.getGstatus().contains("1")){
            //Accept
            //Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.statusImg.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
            holder.deny.setVisibility(View.GONE);
            holder.fstatus.setVisibility(View.VISIBLE);
            holder.fstatus.setText("Gatepass Approved");
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
                                        holder.fstatus.setText("Gatepass Approved !");

                                        loading.dismissDialog();

                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Gate Pass Request Approved Successfully !");
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
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Server is Down ! Try After Sometime !");
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

                        map.put("gatepasskey",holder.gatepassno.getText().toString());
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
                                        holder.fstatus.setText("Gatepass Denied !");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Gate Pass Request Rejected Successfully !");
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

                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Server is Down ! Try After Sometime !");
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
                        map.put("gatepasskey",holder.gatepassno.getText().toString());

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
        TextView name, phone, time, gatepassno, fname, fphone, femail, fcity, fsoc, fblck, fdate, ftime, fGPno, fvalidity, fstatus;

        ImageView allow, deny, statusImg;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.sec_gpo_custom_Uname);
            phone = (TextView) itemView.findViewById(R.id.sec_gpo_custom_Utype);
            gatepassno = (TextView) itemView.findViewById(R.id.sec_gpo_custom_UVehno);

            fname = (TextView) itemView.findViewById(R.id.sec_gpo_tvUName);
            fphone = (TextView) itemView.findViewById(R.id.sec_gpo_tvUMob1);
            fsoc = (TextView) itemView.findViewById(R.id.sec_gpo_tvUsocname);
            fblck = (TextView) itemView.findViewById(R.id.sec_gpo_tvUblock);
            fdate = (TextView) itemView.findViewById(R.id.sec_gpo_tvUdate);
            ftime = (TextView) itemView.findViewById(R.id.sec_gpo_tvUtime);
            fGPno = (TextView) itemView.findViewById(R.id.sec_gpo_tvgatepassno);
            fvalidity = (TextView) itemView.findViewById(R.id.sec_gpo_tvUvalidfor);
            fstatus = (TextView) itemView.findViewById(R.id.sec_gpo_tvOStatus);

            statusImg = (ImageView) itemView.findViewById(R.id.sec_gpo_Ustatus);

            allow = (ImageView) itemView.findViewById(R.id.secAllowGPOBtn);
            deny = (ImageView) itemView.findViewById(R.id.secDenyGPOBtn);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_gp_once_list);

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
