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

public class AdapterGPFreq extends RecyclerView.Adapter<AdapterGPFreq.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<SecShowGFreqData> productList;

    private static final String UPLOAD_URL1 = "https://vivek.ninja/Gatepass/gatepass_freq_allow.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/Gatepass/gatepass_freq_deny.php";

    public AdapterGPFreq(Context context, Activity activity, List<SecShowGFreqData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdapterGPFreq.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_secshow_gatepass_freq, parent, false);
        return new AdapterGPFreq.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGPFreq.myviewholder holder, int position) {

        SecShowGFreqData product = productList.get(position);

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
            holder.statusImage.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
            holder.fstatustv.setVisibility(View.VISIBLE);
            holder.fstatustv.setText("Gatepass Denied");


        }
        else if(product.getGstatus().contains("1")){
            //Accept
           // Log.e("Status",product.getStatus()+"Gone");
            holder.allow.setVisibility(View.GONE);
            holder.statusImage.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
            holder.deny.setVisibility(View.GONE);
            holder.fstatustv.setVisibility(View.VISIBLE);
            holder.fstatustv.setText("Gatepass Approved");
        }

        holder.name.setText(product.getUname());
        holder.phone.setText(product.getUphone());
        holder.gatepass.setText(product.getGatepassno());


        holder.fname.setText(product.getUname());
        holder.fphone.setText(product.getUphone());
        holder.fgp.setText(product.getGatepassno());
        holder.fsoc.setText(product.getUsoc());
        holder.fblck.setText(product.getUblk());
        holder.fvalidity.setText(product.getGval());
        holder.fstime.setText(product.getGstime());
        holder.fetime.setText(product.getGetime());
        String strMain = product.getGdays();
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
                                        holder.fstatustv.setText("Gatepass Approved");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Frequent Time Gatepass Request Approved !");
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
                                            reasonTv.setText("Server is Down at this Moment , Try Again Later !");
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

                        map.put("gatepasskey",product.getGatepassno());

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
                                        holder.fstatustv.setText("Gatepass Denied");

                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Frequent Time Gatepass Request Denied Successfully !");
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
                                            reasonTv.setText("Server is Down at this Moment, Try Again Later !");
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
                        map.put("gatepasskey",product.getGatepassno());

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
        TextView name, phone, gatepass, etime, fname, fphone, fgp, fcity, fsoc, fblck, fvalidity, fstime, fetime, fDays, fstatustv;

        ImageView allow, deny, statusImage;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.sec_gpf_custom_Uname);
            phone = (TextView) itemView.findViewById(R.id.sec_gpf_custom_Utype);
            gatepass = (TextView) itemView.findViewById(R.id.sec_gpf_custom_Ugpno);


            fname = (TextView) itemView.findViewById(R.id.sec_gpf_tvUName);
            fphone = (TextView) itemView.findViewById(R.id.sec_gpf_tvUMob1);
            fgp = (TextView) itemView.findViewById(R.id.sec_gpf_tvgatepass);
            fsoc = (TextView) itemView.findViewById(R.id.sec_gpf_tvUsocname);
            fblck = (TextView) itemView.findViewById(R.id.sec_gpf_tvUblock);
            fvalidity = (TextView) itemView.findViewById(R.id.sec_gpf_tvvalper);
            fstime = (TextView) itemView.findViewById(R.id.sec_gpf_tvUstime);
            fetime = (TextView) itemView.findViewById(R.id.sec_gpf_tvUetime);
            fDays = (TextView) itemView.findViewById(R.id.sec_gpf_tvUdays);
            fstatustv = (TextView) itemView.findViewById(R.id.sec_gpf_tvOStatus);

            allow = (ImageView) itemView.findViewById(R.id.secfAllowGPi);
            deny = (ImageView) itemView.findViewById(R.id.secfDenyGP);
            statusImage = (ImageView) itemView.findViewById(R.id.sec_gpf_Ustatus);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_cab_freq_list);

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
