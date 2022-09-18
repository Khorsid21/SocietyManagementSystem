package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
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

import static com.android.volley.Request.Method.POST;

public class Adaptermmpending extends RecyclerView.Adapter<Adaptermmpending.myviewholder> {

    Context context;
    public Activity activity;
    LoadingDialog loading;
    public String reason;
    private List<Fetchall> productList;
    String maincity,mainsociety;
    private static final String UPLOAD_URL1 = "https://vivek.ninja/Admin/accept_userlist_30_3.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/Admin/reject_userlist_30_3.php";

    public Adaptermmpending(Context context, Activity activity, List<Fetchall> productList,String city,String society) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        this.maincity=city;
        this.mainsociety=society;
        //this.movieList = movieList;
    }


    @NonNull
    @Override
    public Adaptermmpending.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_mm_pending, parent, false);
        return new Adaptermmpending.myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name, phone,email, fname, fphone, femail, fcity, fsoc, fblck,frole,fown,hblock,hOwn;
        LinearLayout maskdm;
        ImageView allow, deny, statusImg,docImg;

        public myviewholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.mmp_Uname);
            phone = (TextView) itemView.findViewById(R.id.mmp_Umob);
            email = (TextView) itemView.findViewById(R.id.mmp_Uemail);

            fname = (TextView) itemView.findViewById(R.id.mmpName);
            fphone = (TextView) itemView.findViewById(R.id.mmpMob);
            femail = (TextView) itemView.findViewById(R.id.mmpUemail);
            fcity = (TextView) itemView.findViewById(R.id.mmpUcity);
            fsoc = (TextView) itemView.findViewById(R.id.mmpUsocname);
            fblck = (TextView) itemView.findViewById(R.id.mmpUblock);

            maskdm = (LinearLayout) itemView.findViewById(R.id.maskdm);

            hblock = (TextView) itemView.findViewById(R.id.mmpblcoktv);
            hOwn = (TextView) itemView.findViewById(R.id.mmptvvehtime);


            frole = (TextView) itemView.findViewById(R.id.mmpUrole);



            fown = (TextView) itemView.findViewById(R.id.mmpUown);

            statusImg = (ImageView) itemView.findViewById(R.id.mmp_Ustatus);
            allow = (ImageView) itemView.findViewById(R.id.mmpAccept);
            deny = (ImageView) itemView.findViewById(R.id.mmpReject);
            docImg = (ImageView) itemView.findViewById(R.id.image1);


            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_mm_pending);

            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.toggle(false);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptermmpending.myviewholder holder, int position) {
        Fetchall product = productList.get(position);

        holder.name.setText(product.getName());
        holder.phone.setText(product.getPhoneno());
        holder.email.setText(product.getEmail());

        holder.fname.setText(product.getName());
        holder.fphone.setText(product.getPhoneno());
        holder.femail.setText(product.getEmail());
        holder.fcity.setText(product.getCity());
        holder.fblck.setText(product.getBlock());
        holder.fsoc.setText(product.getSName());
        holder.frole.setText(product.getRole());
        holder.fown.setText(product.getPersue());
        Picasso.get().load("https://vivek.ninja/images/" + product.getImage()).into(holder.docImg);

        if(product.getRole().contains("Security")){
            Log.e("check1","Gone In");
            holder.hblock.setVisibility(View.GONE);
            holder.hOwn.setVisibility(View.GONE);

            holder.fown.setVisibility(View.GONE);
            holder.fblck.setVisibility(View.GONE);


        }

        if(product.getCity().equalsIgnoreCase(maincity) && product.getSName().equalsIgnoreCase(mainsociety)){
            holder.allow.setOnClickListener(new View.OnClickListener() {
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
                                    Toast.makeText(context,product.getImage(), Toast.LENGTH_SHORT).show();
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

                                                            productList.remove(holder.getAdapterPosition());
                                                            notifyDataSetChanged();

                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Member Accepted Successfuly !");
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
                                                            //Toast.makeText(context, "SuccessFully !", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if(res.contains("Fail")){
                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Error Occured At Database Side ! Try Again Later !");
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
                                                            //Toast.makeText(context, "Fail ! ", Toast.LENGTH_SHORT).show();
                                                        }
                                                        //loading= new LoadingDialog(login.this);

                                                        //Toast.makeText(context, "Sorry !", Toast.LENGTH_SHORT).show();


                                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
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
                                            Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> map = new HashMap<>();

                                            //String path = getPath(filepath);
                                            map.put("namekey", holder.name.getText().toString());
                                            map.put("mobilekey", holder.fphone.getText().toString());
                                            map.put("emailkey", holder.email.getText().toString());
                                            map.put("role", holder.frole.getText().toString());
                                            map.put("image", product.getImage());
                                            map.put("city", holder.fcity.getText().toString());
                                            map.put("society", holder.fsoc.getText().toString());
                                            map.put("block", holder.fblck.getText().toString());
                                            map.put("ownership", holder.fown.getText().toString());

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

            holder.deny.setOnClickListener(new View.OnClickListener() {
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
                                            new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {

                                                }
                                            });

                                    final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                    dialog.setCanceledOnTouchOutside(false);





                                    dialog.show();
                                    //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if(userInputDialogEditText.getText().toString().isEmpty()){
                                                userInputDialogEditText.setError("Enter Reason !");
                                            }
                                            else if(!userInputDialogEditText.getText().toString().isEmpty() && userInputDialogEditText.getText().toString().length() <= 50){
                                                reason = userInputDialogEditText.getText().toString();
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
                                                                        productList.remove(holder.getAdapterPosition());
                                                                        notifyDataSetChanged();
                                                                        //Toast.makeText(context, "SuccessFully !", Toast.LENGTH_SHORT).show();
                                                                        dialog.dismiss();
                                                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                                        alertDialogBuilderUserInput.setView(mViewi);
                                                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                                        reasonTv.setText("Member Rejected Successfuly !");
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
                                                                    }
                                                                    else if(res.contains("Fail")){
                                                                        dialog.dismiss();
                                                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                                        alertDialogBuilderUserInput.setView(mViewi);
                                                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                                        reasonTv.setText("Error Occured At Database Side ! Try Again Later !");
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
                                                                        //Toast.makeText(context, "Fail ! ", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    //loading= new LoadingDialog(login.this);

                                                                    //Toast.makeText(context, "Sorry !", Toast.LENGTH_SHORT).show();
                                                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                                } catch (JSONException e) {
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
                                                                    //dialog.dismiss();
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
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
                                                        Toast.makeText(context.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> map = new HashMap<>();

                                                        //String path = getPath(filepath);
                                                        //map.put("namekey",name.getText().toString().trim());
                                                        map.put("namekey", holder.name.getText().toString());
                                                        map.put("mobilekey", holder.fphone.getText().toString());
                                                        map.put("emailkey", holder.email.getText().toString());
                                                        map.put("role", holder.frole.getText().toString());
                                                        map.put("image",product.getImage());
                                                        map.put("city", holder.fcity.getText().toString());
                                                        map.put("society", holder.fsoc.getText().toString());
                                                        map.put("block", holder.fblck.getText().toString());
                                                        map.put("ownership", holder.fown.getText().toString());
                                                        map.put("reason",reason);
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
                }
            });

        }
        else {
            holder.maskdm.setVisibility(View.GONE);
        }
        /*else if(product.getRole()!="Security"){
            holder.hblock.setVisibility(View.VISIBLE);
            holder.hOwn.setVisibility(View.VISIBLE);

            holder.fown.setVisibility(View.VISIBLE);
            holder.fblck.setVisibility(View.VISIBLE);


        }*/

        holder.docImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "HIIHIHIHI", Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);

                alertDialogBuilderUserInput.setView(mView);
                ImageView imgi=mView.findViewById(R.id.imggg);
                //img.setImageDrawable(holder.img.getDrawable());
                //Picasso.get(imgi.getContext().).load("https://vivek.ninja/images/" + product.getImage()).into(imgi);
                /*Picasso.get()
                        .load("https://vivek.ninja/images/" + product.getImage())
                        .into(imgi);*/
                Glide.with(imgi.getContext()).load("https://vivek.ninja/images/" + product.getImage()).fitCenter().into(imgi);
                //img.setMaxWidth(mView.);
                //img.setMaxHeight();

                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
