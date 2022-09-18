package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.Request.Method.POST;

public class AdapterManageSoc extends RecyclerView.Adapter<AdapterManageSoc.myviewholder>{
    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<SocData> productList;
    List<SocData> datalist;
    String society , city;
    private static final String UPLOAD_URL2 = "https://vivek.ninja/ManageSociety/soc_change.php";
    private static final String UPLOAD_URL3 = "https://vivek.ninja/ManageSociety/del_soc.php";
    private static final String url = "https://vivek.ninja/ManageSociety/fetch_all_soc.php";

    public AdapterManageSoc(Context context, Activity activity, List<SocData> productList,String society,String city) {
        //this.data = data;

        this.activity = activity;
        //this.listener=listener;
        ///

        ///
        //this.persons = new ArrayList<>(persons);
        this.productList = productList;
        this.context = context;
        this.society=society;
        this.city=city;
        //this.movieList = movieList;
    }






    @NonNull
    @Override
    public AdapterManageSoc.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_manage_soc, parent, false);
        return new AdapterManageSoc.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterManageSoc.myviewholder holder, int position) {
        //updateData(productList);
        SocData product = productList.get(position);


        holder.city.setText(product.getCity());
        holder.soc.setText(product.getsName());
        holder.blk.setText(product.getBlock());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempCity,tempSoc,tempBlk;
                tempBlk = product.getBlock();
                tempCity = product.getCity();
                tempSoc = product.getsName();

                LayoutInflater layoutInflaterMSG = LayoutInflater.from(activity);
                View ViewMSG = layoutInflaterMSG.inflate(R.layout.fragment_edit_soc_details, null);
                AlertDialog.Builder MSGalertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                MSGalertDialogBuilderUserInput.setView(ViewMSG);
                //
                final EditText editCity = ViewMSG.findViewById(R.id.editcity);
                final EditText editSoc = ViewMSG.findViewById(R.id.editsoc);
                final TextView Title = ViewMSG.findViewById(R.id.HeadingTitle);
                final EditText editBlock = ViewMSG.findViewById(R.id.editblock);
                final CardView DelDone = ViewMSG.findViewById(R.id.EditDone);
                final CardView DelCancel = ViewMSG.findViewById(R.id.EditCancel);
                //final Button selTime = mViewi.findViewById(R.id.ACESelTimeBtn);
                final AlertDialog dialogi = MSGalertDialogBuilderUserInput.create();

                editCity.setText(tempCity);
                editBlock.setText(tempBlk);
                editSoc.setText(tempSoc);

                Title.setText("Edit Block / Wing Details ");

                DelDone.setOnClickListener(new View.OnClickListener() {
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
                                            //Log.e("ResponseFull",response);
                                            String res = js.getString("repon");
                                            if(!res.contains("no")){
                                                //userEmail= res;
                                                holder.blk.setText(editBlock.getText().toString());
                                                Toast.makeText(activity, "Block/Wing Edited Successfully", Toast.LENGTH_SHORT).show();
                                                dialogi.dismiss();

                                            }
                                            else if(res.contains("no")){
                                                //loading= new LoadingDialog(login.this);
                                                //loading.dismissDialog();

                                                Toast.makeText(activity, "Error Occured At Database Side ! Try Again Later !", Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();

                                map.put("newSoc",society);
                                map.put("newBlk",editBlock.getText().toString());
                                map.put("newCity",city);
                                map.put("oldblk",tempBlk);

                                return  map;
                            }


                        };

                        RequestQueue rq = Volley.newRequestQueue(activity);
                        rq.add(sr);


                    }
                });

                DelCancel.setOnClickListener(new View.OnClickListener() {
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
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest sr = new StringRequest(POST, UPLOAD_URL3,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    //Log.e("ResponseFull",response);
                                    String res = js.getString("repon");
                                    if(!res.contains("no")){
                                        //userEmail= res;
                                        productList.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();

                                        Toast.makeText(activity, "Block/Wing Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        //dialogi.dismiss();
                                    }
                                    else if(res.contains("no")){
                                        //loading= new LoadingDialog(login.this);
                                        //loading.dismissDialog();


                                        Toast.makeText(activity, "Error Occured At Database Side ! Try Again Later !", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        map.put("blkkey",holder.blk.getText().toString());

                        return  map;
                    }


                };

                RequestQueue rq = Volley.newRequestQueue(activity);
                rq.add(sr);
            }
        });


    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name, fragName, fragEmail, fragPhoneno, fragRole, role, fragAddress, fragBlood, fragBloodTV;
        TextView city,soc,blk;
        Button del_btn;
        ImageView delete,edit,deldone,delcanc;
        CircleImageView cv;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            city = (TextView) itemView.findViewById(R.id.edCname);
            soc = (TextView) itemView.findViewById(R.id.edsocname);
            blk = (TextView) itemView.findViewById(R.id.edblkname);

            delete = (ImageView) itemView.findViewById(R.id.MSdelete);
            edit = (ImageView) itemView.findViewById(R.id.MSEdit);
            deldone = (ImageView) itemView.findViewById(R.id.MSdeleteDone);
            delcanc = (ImageView) itemView.findViewById(R.id.MSdeleteCalcel);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
