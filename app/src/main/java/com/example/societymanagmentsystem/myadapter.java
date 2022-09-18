package com.example.societymanagmentsystem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.android.volley.Request.Method.POST;

import android.support.v4.app.*;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder> {


    //Fetchall []data;
    Context context;
    //private static final String UPLOAD_URL = "http://adrenal-pairs.000webhostapp.com/Admin/reject_userlist.php";
    private static final String UPLOAD_URL = "https://vivek.ninja/Admin/reject_userlist.php";
    //private static final String UPLOAD_URL1 = "https://adrenal-pairs.000webhostapp.com/Admin/accept_userlist.php";
    private static final String UPLOAD_URL1 = "https://vivek.ninja/Admin/accept_userlist.php";
    public Activity activity;
    public String reason;
    private List<Fetchall> productList;
    //List<Fetchall> movieList;


    public myadapter(Context context, Activity activity, List<Fetchall> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        Fetchall product = productList.get(position);
        //holder.name.setText(product.getName());
        holder.name.setText(product.getName());
        holder.phoneno.setText(product.getPhoneno());
        holder.email.setText(product.getEmail());


        //holder.role.setText(product.getRole());
        //holder.city.setText(product.getCity());
        //holder.ownership.setText(product.getPersue());
        //holder.society.setText(product.getSName());
        // holder.block.setText(product.getBlock());



        /*Fetchall datum=data[position];
        holder.name.setText(datum.getName());
        holder.phoneno.setText(datum.getPhoneno());
        holder.role.setText(datum.getRole());
        holder.city.setText(datum.getCity());
        holder.ownership.setText(datum.getPersue());
        holder.society.setText(datum.getSName());
        holder.block.setText(datum.getBlock());
        holder.email.setText(datum.getEmail());*/

        //boolean isExpanded = data[position].isExpanded();
        //boolean isExpanded = productList.get(position).isExpanded();
        //holder.extendedLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        //Picasso.get().load("https://vivek.ninja/images/" + product.getImage()).into(holder.img);



        /*Glide.with(holder.img.getContext())
                .load("https://vivek.ninja/images/" + product.getImage())
                .into(holder.img);*/

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
                                Toast.makeText(context, product.getImage(), Toast.LENGTH_SHORT).show();
                                StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                                    JSONObject js = jsonArray.getJSONObject(0);
                                                    String res = js.getString("repon");
                                                    Log.e("DAS", response + "-" + res);
                                                    if (!res.contains("Fail")) {

                                                        Toast.makeText(context, "Accepted !", Toast.LENGTH_SHORT).show();
                                                    } else if (res.contains("Fail")) {

                                                        Toast.makeText(context, "Fail ! ", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();

                                        //String path = getPath(filepath);
                                        map.put("namekey", holder.name.getText().toString());
                                        map.put("mobilekey", holder.phoneno.getText().toString());
                                        map.put("emailkey", holder.email.getText().toString());
                                        map.put("role", holder.role.getText().toString());
                                        map.put("image", product.getImage());
                                        map.put("city", holder.city.getText().toString());
                                        map.put("society", holder.society.getText().toString());
                                        map.put("block", holder.block.getText().toString());
                                        map.put("ownership", holder.ownership.getText().toString());


                                        //map.put("image",encodeBitmapImage(bitmap));
                                        return map;
                                    }
                                };
                                RequestQueue rq = Volley.newRequestQueue(context.getApplicationContext());
                                rq.add(sr);

                                Toast.makeText(context.getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                            }
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

                                //AlertDialog();

                                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);

                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                alertDialogBuilderUserInput.setView(mView);
                                EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                                alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                        return i == KeyEvent.KEYCODE_BACK;
                                    }
                                });

                                alertDialogBuilderUserInput.setPositiveButton("Send",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.setCanceledOnTouchOutside(false);


                                dialog.show();
                                //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (userInputDialogEditText.getText().toString().isEmpty()) {
                                            userInputDialogEditText.setError("Enter Reason !");
                                        } else if (!userInputDialogEditText.getText().toString().isEmpty() && userInputDialogEditText.getText().toString().length() <= 50) {
                                            reason = userInputDialogEditText.getText().toString();
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
                                                                    dialog.dismiss();
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
                                                    map.put("namekey", holder.name.getText().toString());
                                                    map.put("mobilekey", holder.phoneno.getText().toString());
                                                    map.put("emailkey", holder.email.getText().toString());
                                                    map.put("role", holder.role.getText().toString());
                                                    map.put("image", product.getImage());
                                                    map.put("city", holder.city.getText().toString());
                                                    map.put("society", holder.society.getText().toString());
                                                    map.put("block", holder.block.getText().toString());
                                                    map.put("ownership", holder.ownership.getText().toString());
                                                    map.put("reason", reason);
                                                    return map;
                                                }
                                            };
                                            RequestQueue rq = Volley.newRequestQueue(context.getApplicationContext());
                                            rq.add(sr);
                                            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();

                                        }
                                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                                    }
                                });


                                Toast.makeText(context.getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new AndExAlertDialogListener() {
                            @Override
                            public void OnClick(String input) {
                                Toast.makeText(context.getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

/*
                StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jobj = new JSONObject(response);
                                    String res = jobj.getString("response");
                                    Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();

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
                        map.put("emailkey", holder.email.getText().toString());
                        //map.put("image",encodeBitmapImage(bitmap));
                        return map;
                    }
                }; */
                //RequestQueue rq = Volley.newRequestQueue(mCtx.getApplicationContext());
                //rq.add(sr);


                //productList.remove(holder.getAdapterPosition());
                //notifyDataSetChanged();

                //Intent i =new Intent(mCtx.getApplicationContext(),temp.class);
                //mCtx.startActivity(i);
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "HIIHIHIHI", Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);

                alertDialogBuilderUserInput.setView(mView);
                ImageView imgi = mView.findViewById(R.id.imggg);
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

                /*Dialog builder = new Dialog(activity);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });

                ImageView imageView = new ImageView(activity);

                imageView.setImageDrawable(holder.img.getDrawable());
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.show();*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
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


                            Toast.makeText(context, "Please Enter Reason !", Toast.LENGTH_SHORT).show();
                            AlertDialog();
                            //Toast.makeText(mCtx, "dsd", Toast.LENGTH_SHORT).show();
                            userInputDialogEditText.setError("Reason Required !!");
                            //}
                            //return;
                        } else if (!userInputDialogEditText.getText().toString().isEmpty()) {
                            if (userInputDialogEditText.getText().toString().length() >= 50) {
                                Toast.makeText(context, "Reason Must Be AT Most 10 Char Long !", Toast.LENGTH_SHORT).show();
                                AlertDialog();
                            } else {
                                reason = userInputDialogEditText.getText().toString();
                                //Toast.makeText(context, userInputDialogEditText.getText().toString(), Toast.LENGTH_SHORT).show();
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


    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, email, phoneno, role, society, block, ownership, city;
        Button accept, reject;
        ImageButton imgbtn;
        ConstraintLayout extendedLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name1);
            email = (TextView) itemView.findViewById(R.id.email1);
            phoneno = (TextView) itemView.findViewById(R.id.phone1);

            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_member_list);
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);
                }
            });

            /*img = (ImageView) itemView.findViewById(R.id.image1);

            role = (TextView) itemView.findViewById(R.id.role2);
            society = (TextView) itemView.findViewById(R.id.society2);
            block = (TextView) itemView.findViewById(R.id.block2);
            ownership = (TextView) itemView.findViewById(R.id.owner2);
            city = (TextView) itemView.findViewById(R.id.city2);

            imgbtn = itemView.findViewById(R.id.imgbtn);
            extendedLayout = itemView.findViewById(R.id.extended);
            accept = itemView.findViewById(R.id.accept1);
            reject = itemView.findViewById(R.id.reject1);*/



            /*tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fetchall fetch = data[getAdapterPosition()];
                    fetch.setExpanded(!fetch.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });*/
            //accept=itemView.findViewById(R.id.BtnAccept1);
        }


    }


}
