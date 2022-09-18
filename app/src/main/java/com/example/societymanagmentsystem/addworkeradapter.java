package com.example.societymanagmentsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.KeyEvent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;

public class addworkeradapter extends RecyclerView.Adapter<addworkeradapter.myviewholder> {

    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<workerdata> productList;
    private static final String UPLOAD_URL = "https://vivek.ninja/worker/delete_worker.php";

    public addworkeradapter(Context context, Activity activity, List<workerdata> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }


    @Override
    public addworkeradapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_add_worker_list, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(addworkeradapter.myviewholder holder, int position) {

        workerdata product = productList.get(position);


        holder.name.setText(product.getName());
        holder.role.setText(product.getWorkertype());

        holder.fragName.setText(product.getName());
        holder.fragRole.setText(product.getWorkertype());
        holder.fragSpecs.setText(product.getSpeciality());
        holder.fragAddress.setText(product.getAddress());
        holder.fragPhoneno.setText(product.getPhoneno());
        holder.fragEmail.setText(product.getEmail());

        Picasso.get().load("https://vivek.ninja/worker/worker/workerimages/"+product.getWorkerImage()).into(holder.cv);
        //worker_phoneno = product.getPhoneno();
        //Toast.makeText(context, worker_phoneno, Toast.LENGTH_SHORT).show();
        Picasso.get().load("https://vivek.ninja/worker/workerimages/"+product.getWorkerImage()).into(holder.cv);

        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AndExAlertDialog.Builder(activity)
                        .setTitle("Warning !")
                        .setMessage("Are You Sure ? Once You Delete This Worker , then Worker Will be Deleted Permanently??")
                        .setPositiveBtnText("Yes")
                        .setNegativeBtnText("No")
                        .setImage(R.drawable.warning, 15)
                        .setCancelableOnTouchOutside(true)
                        .OnPositiveClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {

                                loading = new LoadingDialog(activity);
                                loading.startLoadingDialog();

                                StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {


                                                    JSONObject jsonObject = new JSONObject(response);
                                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                                    JSONObject js = jsonArray.getJSONObject(0);
                                                    String res = js.getString("repon");
                                                    if (!res.contains("no")) {
                                                        loading.dismissDialog();

                                                        productList.remove(holder.getAdapterPosition());
                                                        notifyDataSetChanged();

                                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                        alertDialogBuilderUserInput.setView(mViewi);
                                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                        reasonTv.setText("Worker Deleted Successfully !");
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
                                                    } else if (res.contains("no")) {
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
                                                        //Toast.makeText(context, "Something Went Wrong ! Try Again Letter !", Toast.LENGTH_SHORT).show();

                                                    }
                                                    //Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                                                    //DO Delete
                                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
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
                                        //Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();

                                        //String path = getPath(filepath);
                                        //map.put("namekey",name.getText().toString().trim());

                                        map.put("workerPhone", product.getPhoneno());
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
        });


    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name, fragName,fragEmail, fragPhoneno, fragRole,role, fragAddress, fragSpecs;
        Button del_btn;
        ImageButton imgbtn;
        CircleImageView cv;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.custom_name_worker);
            role = (TextView) itemView.findViewById(R.id.custom_type_worker);

            del_btn = itemView.findViewById(R.id.custom_deletebtn_worker);
            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_worker_list);
            cv = itemView.findViewById(R.id.addWorkerphoto);
            fragName = itemView.findViewById(R.id.tvaddWorkerName);
            fragEmail = itemView.findViewById(R.id.tvaddWorkerEm);
            fragPhoneno = itemView.findViewById(R.id.tvaddWorkerNumber);
            fragAddress = itemView.findViewById(R.id.tvaddWorkerAddress);
            fragSpecs = itemView.findViewById(R.id.tvaddWorkerSpeciality);
            fragRole = itemView.findViewById(R.id.tvaddWorkerType);

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
