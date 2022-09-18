package com.example.societymanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class addemgnnoadapter extends RecyclerView.Adapter<addemgnnoadapter.myviewholder>{

    Context context;
    public Activity activity;
    LoadingDialog loading;
    private List<Emgnnodata> productList;
    private static final String UPLOAD_URL = "https://vivek.ninja/emgn/delete_emgnno.php";

    public addemgnnoadapter(Context context, Activity activity, List<Emgnnodata> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        //this.movieList = movieList;
    }

    @NonNull
    @Override
    public addemgnnoadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_add_emgnno_list, parent, false);
        return new addemgnnoadapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addemgnnoadapter.myviewholder holder, int position) {
        Emgnnodata product = productList.get(position);


        holder.name.setText(product.getName());
        holder.role.setText(product.getType());


        holder.fragName.setText(product.getName());
        holder.fragRole.setText(product.getType());
        holder.fragAddress.setText(product.getAddress());
        holder.fragPhoneno.setText(product.getPhoneno());
        holder.fragEmail.setText(product.getEmail());

        if(product.getType().contains("Blood")){
            holder.fragBlood.setVisibility(View.VISIBLE);
            holder.fragBlood.setText(product.getSpeciality());
            holder.fragBloodTV.setVisibility(View.VISIBLE);
        }

        Picasso.get().load("https://vivek.ninja/emgn/emgnimage/"+product.getImage()).into(holder.cv);

        holder.del_btn.setOnClickListener(new View.OnClickListener() {
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
                                                        reasonTv.setText("Emergency Service Deleted Successfully !");
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

                                //Toast.makeText(context, "Deleted HAHA", Toast.LENGTH_SHORT).show();


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
        TextView name, fragName, fragEmail, fragPhoneno, fragRole, role, fragAddress, fragBlood,fragBloodTV;
        Button del_btn;
        ImageButton imgbtn;
        CircleImageView cv;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.custom_name_emgnno);
            role = (TextView) itemView.findViewById(R.id.custom_type_emgnno);

            del_btn = itemView.findViewById(R.id.custom_deletebtn_emgnno);

            cv = itemView.findViewById(R.id.addEmgnNophoto);

            fragName = itemView.findViewById(R.id.tvaddEmgnNoName);
            fragEmail = itemView.findViewById(R.id.tvaddEmgnNoEm);
            fragPhoneno = itemView.findViewById(R.id.tvaddEmgnNoNumber);
            fragAddress = itemView.findViewById(R.id.tvaddEmgnNoAddress);
            fragBlood = itemView.findViewById(R.id.tvaddEmgnNoBlood);
            fragBloodTV = itemView.findViewById(R.id.tvaddEmgnNoBloodType);
            fragRole = itemView.findViewById(R.id.tvaddEmgnNoType);


            final FoldingCell fc = itemView.findViewById(R.id.folding_cell_add_emgnno_list);

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


