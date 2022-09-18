package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class AdapterChildExit extends RecyclerView.Adapter<AdapterChildExit.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    String SMS_CODE="ACc95f4b7da47abe11bf62150fa6aa4d66";
    private List<AllowChildExitData> productList;
    private static final String UPLOAD_URL1 = "https://vivek.ninja/childexit/child_allow.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/childexit/child_deny.php";


    public AdapterChildExit(Context context, Activity activity, List<AllowChildExitData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public AdapterChildExit.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_child_exit, parent, false);
        return new AdapterChildExit.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        AllowChildExitData product = productList.get(position);

        holder.cname.setText(product.getChildname());
        holder.gname.setText(product.getGname());
        holder.gemail.setText(product.getGemail());
        holder.gphone.setText(product.getGphoneno());
        holder.gsocname.setText(product.getGsoc());
        holder.gblock.setText(product.getGblk());
        holder.gvalidity.setText(product.getTimeper());

        if(product.getStatus().toString().contains("-1")){

            holder.allow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                            holder.deny.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
                                            holder.statusTV.setVisibility(View.VISIBLE);
                                            holder.statusTV.setText("Approved");

                                            //loading.dismissDialog();
                                            Toast.makeText(context, "SMS Send SuccessFully !", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(res.contains("Fail")){
                                            String status_code = js.getString("rcode");
                                            if(status_code.contains("1111")){
                                                //loading.dismissDialog();
                                                Toast.makeText(context, "Currently Twilio API is Facing Some Issues to send SMS, But User Child Exit Request is Allowed Successfully ! ", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(status_code.contains("2222")){
                                               // loading.dismissDialog();
                                                Toast.makeText(context, "Error Occured At Database Side ! Try After Some Time !", Toast.LENGTH_SHORT).show();
                                            }
                                            //loading= new LoadingDialog(login.this);

                                            //Toast.makeText(context, "Sorry !", Toast.LENGTH_SHORT).show();
                                        }

                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        //loading.dismissDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //loading.dismissDialog();
                            Log.e("JSON Parser", "Error parsing data " + error.toString());
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();

                            //String path = getPath(filepath);
                            map.put("cnamekey",holder.cname.getText().toString());
                            map.put("gnamekey",holder.gname.getText().toString());
                            map.put("gemailkey",holder.gemail.getText().toString());
                            map.put("gmobilekey",holder.gphone.getText().toString());
                            map.put("gsocietykey",holder.gsocname.getText().toString());
                            map.put("gblockkey",holder.gblock.getText().toString());
                            map.put("gvalidity",holder.gvalidity.getText().toString());

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
            holder.deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                            holder.deny.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
                                            holder.statusTV.setVisibility(View.VISIBLE);
                                            holder.statusTV.setText("Rejected");

                                            //loading.dismissDialog();
                                            Toast.makeText(context, "SMS Send SuccessFully !", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(res.contains("Fail")){
                                            String status_code = js.getString("rcode");
                                            if(status_code.contains("1111")){
                                                //loading.dismissDialog();
                                                Toast.makeText(context, "Currently Twilio API is Facing Some Issues to send SMS, But User Child Exit Request is Denied Successfully ! ", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(status_code.contains("2222")){
                                                //loading.dismissDialog();
                                                Toast.makeText(context, "Error Occured At Database Side ! Try After Some Time !", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        //loading.dismissDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //loading.dismissDialog();
                            Log.e("JSON Parser", "Error parsing data " + error.toString());
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();

                            //String path = getPath(filepath);
                            map.put("cnamekey",holder.cname.getText().toString());
                            map.put("gnamekey",holder.gname.getText().toString());
                            map.put("gemailkey",holder.gemail.getText().toString());
                            map.put("gmobilekey",holder.gphone.getText().toString());
                            map.put("gsocietykey",holder.gsocname.getText().toString());
                            map.put("gblockkey",holder.gblock.getText().toString());
                            map.put("gvalidity",holder.gvalidity.getText().toString());

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

        }
        else if(product.getStatus().toString().contains("1")){
            holder.allow.setVisibility(View.GONE);
            holder.deny.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.accepted));
            holder.statusTV.setVisibility(View.VISIBLE);

            holder.statusTV.setText("Approved");

        }
        else if(product.getStatus().toString().contains("0")){
            holder.allow.setVisibility(View.GONE);
            holder.deny.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.rejected));
            holder.statusTV.setVisibility(View.VISIBLE);
            holder.statusTV.setText("Rejected");
        }
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        TextView cname,gname, gemail, gphone, gsocname, gblock, gvalidity, statusTV, fsoc, fblck, fdate, ftime, fvehno, fvalidity, fstatus;

        ImageView allow, deny;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            cname = (TextView) itemView.findViewById(R.id.ACEcname);
            gname = (TextView) itemView.findViewById(R.id.ACEgname);
            gemail = (TextView) itemView.findViewById(R.id.ACEgemail);
            gphone = (TextView) itemView.findViewById(R.id.ACEgphone);
            gsocname = (TextView) itemView.findViewById(R.id.ACEsocname);
            gblock = (TextView) itemView.findViewById(R.id.ACEblock);
            gvalidity = (TextView) itemView.findViewById(R.id.ACEvalidity);

            statusTV = (TextView) itemView.findViewById(R.id.ACEtvStatusName);
            allow =  itemView.findViewById(R.id.ACEAllowChildBtn);
            deny =  itemView.findViewById(R.id.ACEDenyChildBtn);




        }
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
}
