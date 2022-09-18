package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mCtx;
    public Activity activity;
    private List<Product> productList;
    private RecyclerViewClickListener listener;

    public RelativeLayout relativeLayout;
   // private static final String UPLOAD_URL = "http://adrenal-pairs.000webhostapp.com/delete_user.php";
    private static final String UPLOAD_URL = "https://vivek.ninja/delete_user.php";
    //private static final String UPLOAD_URL1 = "https://adrenal-pairs.000webhostapp.com/accept_temp_data.php";
    private static final String UPLOAD_URL1 = "https://vivek.ninja/accept_temp_data.php";


    public ProductAdapter(Context mCtx, List<Product> productList, Activity activity,RecyclerViewClickListener listener) {
        this.mCtx = mCtx;
        this.activity = activity;
        this.listener=listener;
        this.productList = productList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {


        Product product = productList.get(position);

        //loading the image
        holder.textName.setText(product.getName());
        holder.textEmail.setText(product.getEmail());
        holder.textMob.setText(product.getPhoneno());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AndExAlertDialog.Builder(activity)
                        .setTitle("WARNING !")
                        .setMessage("Are You sure , You Really Want TO ACCEPT This  ??")
                        .setPositiveBtnText("YES")
                        .setNegativeBtnText("NO;")
                        .setImage(R.drawable.warning, 15)
                        .setCancelableOnTouchOutside(true)
                        .OnPositiveClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {

                                StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {

                                                    JSONObject jobj = new JSONObject(response);
                                                    String res = jobj.getString("response");
                                                    Toast.makeText(mCtx.getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                                                    //DO Delete
                                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(mCtx.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();

                                        //String path = getPath(filepath);
                                        map.put("namekey",holder.textName.getText().toString());
                                        map.put("mobilekey", holder.textMob.getText().toString());
                                        map.put("emailkey", holder.textEmail.getText().toString());
                                        //map.put("image",encodeBitmapImage(bitmap));
                                        return map;
                                    }
                                };
                                RequestQueue rq = Volley.newRequestQueue(mCtx.getApplicationContext());
                                rq.add(sr);

                                Toast.makeText(mCtx.getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {
                                Toast.makeText(mCtx.getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();


            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mCtx, "GAga", Toast.LENGTH_SHORT).show();

                new AndExAlertDialog.Builder(activity)
                        .setTitle("ALERT !")
                        .setMessage("Are You sure , You Really Want TO Delete This ??")
                        .setPositiveBtnText("Confirm")
                        .setNegativeBtnText("Cancel;")
                        .setImage(R.drawable.warning, 15)
                        .setCancelableOnTouchOutside(true)
                        .OnPositiveClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {

                                AlertDialog();

                                Toast.makeText(mCtx.getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {
                                Toast.makeText(mCtx.getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();


                StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jobj = new JSONObject(response);
                                    String res = jobj.getString("response");
                                    Toast.makeText(mCtx.getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                                    //DO Delete
                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        //map.put("namekey",name.getText().toString().trim());
                        map.put("mobilekey", holder.textMob.getText().toString());
                        map.put("emailkey", holder.textEmail.getText().toString());
                        //map.put("image",encodeBitmapImage(bitmap));
                        return map;
                    }
                };
                //RequestQueue rq = Volley.newRequestQueue(mCtx.getApplicationContext());
                //rq.add(sr);

                productList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

                //Intent i =new Intent(mCtx.getApplicationContext(),temp.class);
                //mCtx.startActivity(i);
            }
        });
    }

    public void AlertDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(mView);

        EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        if (userInputDialogEditText.getText().toString().isEmpty()) {


                            Toast.makeText(mCtx, "Please Enter Reason !", Toast.LENGTH_SHORT).show();
                            AlertDialog();
                            //Toast.makeText(mCtx, "dsd", Toast.LENGTH_SHORT).show();
                            userInputDialogEditText.setError("Reason Required !!");
                            //}
                            //return;
                        } else if (!userInputDialogEditText.getText().toString().isEmpty()) {
                            if (userInputDialogEditText.getText().toString().length() >= 50) {
                                Toast.makeText(mCtx, "Reason Must Be AT Most 10 Char Long !", Toast.LENGTH_SHORT).show();
                                AlertDialog();
                            } else {
                                Toast.makeText(mCtx, userInputDialogEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        /*
                .setNegativeButton ("Cancel",
                        new DialogInterface.OnClickListener () {
                            public void onClick (DialogInterface dialogBox, int id) {
                                dialogBox.cancel ();
                            }
                        });
        */


        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textName, textEmail, textMob, textId;
        Button accept, reject;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            textEmail = itemView.findViewById(R.id.textEmail);
            textMob = itemView.findViewById(R.id.textMob);
            accept = itemView.findViewById(R.id.BtnAccept);
            reject = itemView.findViewById(R.id.BtnReject);
            relativeLayout = itemView.findViewById(R.id.layoutID);
            itemView.setOnClickListener(this);

            //textId = itemView.findViewById(R.id.textId);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());

        }
    }

    public interface RecyclerViewClickListener {
        //void onRowClick(View view, int position);
        void onButtonYesClick(View view, int position);
        void onClick(View v,int position);
        void onButtonNoClick(View view, int position);
    }



}
