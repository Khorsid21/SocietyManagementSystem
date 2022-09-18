package com.example.societymanagmentsystem;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.FileUtil;
import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;

public class AdapterSocNotice extends RecyclerView.Adapter<AdapterSocNotice.myviewholder> {

    Context context;
    public Activity activity;
    LoadingDialog loading;
    public String reason;
    File file;
    private int CAPTURE_PHOTO = 104;
    String dirPath, fileName;
    public Bitmap bitmap;
    boolean mHasDoubleClicked;
    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private File actualImage;
    int IMAGEVIEW_SELECTER = -1;

    String role;
    private List<SocNoticeData> productList;

    private static final String UPLOAD_URL1 = "https://vivek.ninja/Soc_Notice/delete_notice.php";
    private static final String UPLOAD_URL2 = "https://vivek.ninja/Soc_Notice/edit_textfrag.php";

    public AdapterSocNotice(Context context, Activity activity, String role, List<SocNoticeData> productList) {
        //this.data = data;
        this.activity = activity;
        //this.listener=listener;
        this.productList = productList;
        this.context = context;
        this.role = role;
        //this.movieList = movieList;
    }

    public AdapterSocNotice() {

    }

    @NonNull
    @Override
    public AdapterSocNotice.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_soc_notice, parent, false);
        return new AdapterSocNotice.myviewholder(view);
    }


    private boolean findDoubleClick(int IMAGEVIEW_SELECTER,String url) {
        // Get current time in nano seconds.
        long pressTime = System.currentTimeMillis();
        // If double click...
        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            mHasDoubleClicked = true;

            // double click event....
        } else { // If not double click....
            mHasDoubleClicked = false;
            Handler myHandler = new Handler() {
                public void handleMessage(Message m) {

                    if (!mHasDoubleClicked) {
                        // single click event
                        if (IMAGEVIEW_SELECTER == 1) {

                            String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                    .format(System.currentTimeMillis());
                            AndroidNetworking.initialize(context);
                            File filepath = Environment.getExternalStorageDirectory();
                            File dir = new File(filepath+"/Society App");
                            dir.mkdirs();
                            fileName = "SOCNOTICE"+time+".PNG";
                            file = new File(dir, fileName);

                            AndroidNetworking.download(url, String.valueOf(dir), fileName)
                                    .build()
                                    .startDownload(new DownloadListener() {
                                        @Override
                                        public void onDownloadComplete() {

                                            Toast.makeText(activity, "Download Complete ! Image Stored at :"+dir, Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                        } else if (IMAGEVIEW_SELECTER == 2) {

                        }

                        Bundle b = new Bundle();


                    }
                }
            };
            Message m = new Message();
            myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
        }
        lastPressTime = pressTime;
        return mHasDoubleClicked;
    }


    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        //ImageView imageView;
        public GetImageFromUrl(String img){
            //this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            //imageView.setImageBitmap(bitmap);
        }
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // do you stuff
        Log.e("GONE", "YEEEEEEEEEEEEEEEES");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 104) {
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                bitmap = (Bitmap) data.getExtras().get("data");

                /*if(bitmap!=null) {
                    Log.e("E<pty","NOT empty");
                    myviewholder.ITFimg.setImageBitmap(bitmap);

                }
                else{
                    Log.e("E<pty","empty");
                }*/



                /*if (IMAGEVIEW_SELECTER == 1) {
                    Log.e("Gone","YES");
                    IFimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    IFimg.setImageBitmap(bitmap);
                    IFimg.setTag("Uploaded");
                    IMAGEVIEW_SELECTER = -1;
                }*/
                //final_image = encodeBitmapImage(captureImage);

            }
            /*if (requestCode == 1) {
                Uri filepath = data.getData();
                try {
                    actualImage = FileUtil.from(activity, data.getData());
                    InputStream inputStream =  context.getContentResolver().openInputStream(filepath);
                    if(bitmap!=null){
                        bitmap.recycle();
                        bitmap = null;
                    }
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }*/
        }


    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSocNotice.myviewholder holder, int position) {


        /*holder.postedby.setText("Posted by :" + product.getName());
        holder.noticetitle.setText(product.getNoticeTitle());

        DateFormat df = new SimpleDateFormat("hh:mm aa");
        String currTime = df.format(Calendar.getInstance().getTime());

        DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
        String currDate = df2.format(Calendar.getInstance().getTime());

        holder.datetime.setText(currDate + " " + currTime);*/

        if (role.equalsIgnoreCase("admin")) {
            SocNoticeData product = productList.get(position);
            holder.postedby.setText("Posted by :" + product.getName());
            holder.noticetitle.setText(product.getNoticeTitle());

            DateFormat df = new SimpleDateFormat("hh:mm aa");
            String currTime = df.format(Calendar.getInstance().getTime());

            DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
            String currDate = df2.format(Calendar.getInstance().getTime());

            holder.datetime.setText(currDate + " " + currTime);

            holder.DeleteNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AndExAlertDialog.Builder(activity)
                            .setTitle("WARNING !")
                            .setMessage("Are You sure , You Really Want TO Delete This Notice ??")
                            .setPositiveBtnText("YES")
                            .setNegativeBtnText("NO;")
                            .setImage(R.drawable.warning, 15)
                            .setCancelableOnTouchOutside(true)
                            .OnPositiveClicked(new AndExAlertDialogListener() {
                                @Override
                                public void OnClick(String input) {
                                    //Toast.makeText(context,product.getImage(), Toast.LENGTH_SHORT).show();
                                    StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.e("Response", response);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                                                        JSONObject js = jsonArray.getJSONObject(0);
                                                        String res = js.getString("repon");
                                                        //Log.e("DAS",response+"-"+res);

                                                        if (!res.contains("no")) {
                                                            productList.remove(holder.getAdapterPosition());
                                                            notifyDataSetChanged();
                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Notice Deleted Successfuly !");
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
                                                        } else if (res.contains("no")) {
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
                                            map.put("id", product.getId());
                                            map.put("noticetype", product.getNoticeType());
                                            if (product.getNoticeType().equalsIgnoreCase("2")) {
                                                Log.e("Inside", product.getNoticeImg());

                                                map.put("image", product.getNoticeImg());
                                            } else if (product.getNoticeType().equalsIgnoreCase("3")) {
                                                Log.e("Inside2", product.getNoticeImg());

                                                map.put("image", product.getNoticeImg());
                                            }
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


            if (product.getNoticeType().equalsIgnoreCase("1")) {
                holder.TFN.setVisibility(View.VISIBLE);
                holder.IFN.setVisibility(View.GONE);
                holder.ITFN.setVisibility(View.GONE);
                holder.TFnoticeDesc.setText(product.getNoticeDesc());

                holder.editNotice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.TFnoticeDesc.setVisibility(View.GONE);
                        holder.editTextFragment.setText(holder.TFnoticeDesc.getText().toString());
                        holder.editTextFragment.setVisibility(View.VISIBLE);
                        holder.edittextF.setVisibility(View.VISIBLE);

                        holder.editTextFDone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show();
                                if (holder.editTextFragment.getText().toString().equalsIgnoreCase(holder.TFnoticeDesc.getText().toString())) {
                                    Toast.makeText(context, "Please Edit Notice Description", Toast.LENGTH_SHORT).show();
                                } else {

                                    StringRequest sr = new StringRequest(POST, UPLOAD_URL2,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                                                        JSONObject js = jsonArray.getJSONObject(0);
                                                        String res = js.getString("repon");
                                                        //Log.e("DAS",response+"-"+res);
                                                        if (!res.contains("Fail")) {

                                                            holder.TFnoticeDesc.setVisibility(View.VISIBLE);
                                                            holder.TFnoticeDesc.setText(holder.editTextFragment.getText().toString());
                                                            holder.editTextFragment.setVisibility(View.GONE);
                                                            holder.edittextF.setVisibility(View.GONE);

                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Notice Updated Successfully !");
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
                                                        } else if (res.contains("Fail")) {

                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Error Occured at Database Side , Try After Some Time!");
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

                                                        //loading= new LoadingDialog(login.this);

                                                        //Toast.makeText(context, "Sorry !", Toast.LENGTH_SHORT).show();


                                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        //loading.dismissDialog();
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
                                            //loading.dismissDialog();
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
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> map = new HashMap<>();

                                            //String path = getPath(filepath);
                                            map.put("id", product.getId());
                                            map.put("noticeDesc", holder.editTextFragment.getText().toString());

                                            return map;
                                        }


                                    };
                                    RequestQueue rq = Volley.newRequestQueue(context);
                                    rq.add(sr);

                                }
                            }
                        });
                        holder.editTextFCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                                holder.TFnoticeDesc.setVisibility(View.VISIBLE);
                                //holder.editTextFragment.setText(holder.TFnoticeDesc.getText().toString());
                                holder.editTextFragment.setVisibility(View.GONE);
                                holder.edittextF.setVisibility(View.GONE);
                            }
                        });
                    }
                });


            }
            else if (product.getNoticeType().equalsIgnoreCase("2")) {
                holder.TFN.setVisibility(View.GONE);
                holder.IFN.setVisibility(View.VISIBLE);
                holder.ITFN.setVisibility(View.GONE);
                holder.editNotice.setVisibility(View.GONE);

               /* holder.DeleteNotice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();
                        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                .format(System.currentTimeMillis());
                        AndroidNetworking.initialize(context);
                        File filepath = Environment.getExternalStorageDirectory();
                        File dir = new File(filepath+"/Society App");
                        dir.mkdirs();
                        fileName = "SOCNOTICE"+time+".PNG";
                        file = new File(dir, fileName);

                        AndroidNetworking.download(url, String.valueOf(dir), fileName)
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {

                                        Toast.makeText(activity, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });

                    }
                });*/

                Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.IFNoticeImg);
            }
            else if (product.getNoticeType().equalsIgnoreCase("3")) {
                holder.TFN.setVisibility(View.GONE);
                holder.IFN.setVisibility(View.GONE);
                holder.ITFN.setVisibility(View.VISIBLE);

                holder.ITFnoticeDesc.setText(product.getNoticeDesc());
                Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.ITFNoticeImg);


                ///

                holder.editNotice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.ITFnoticeDesc.setVisibility(View.GONE);
                        holder.ITFNoticeImg.setVisibility(View.GONE);
                        holder.editImageTextFragment.setText(holder.ITFnoticeDesc.getText().toString());
                        holder.editImageTextFragment.setVisibility(View.VISIBLE);
                        holder.ITFimg.setVisibility(View.VISIBLE);
                        holder.editimagetextF.setVisibility(View.VISIBLE);

                        Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.ITFimg);
                        //holder.ITFimg.setTag("0");


                        holder.editImageTextFDone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (holder.editImageTextFragment.getText().toString().equalsIgnoreCase(holder.ITFnoticeDesc.getText().toString())) {
                                    Toast.makeText(context, "Please Edit Notice Title", Toast.LENGTH_SHORT).show();
                                } else {

                                    StringRequest sr = new StringRequest(POST, UPLOAD_URL2,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                                                        JSONObject js = jsonArray.getJSONObject(0);
                                                        String res = js.getString("repon");
                                                        //Log.e("DAS",response+"-"+res);
                                                        if (!res.contains("Fail")) {

                                                            holder.ITFnoticeDesc.setVisibility(View.VISIBLE);
                                                            holder.editimagetextF.setVisibility(View.GONE);
                                                            holder.ITFnoticeDesc.setText(holder.editImageTextFragment.getText().toString());
                                                            holder.editImageTextFragment.setVisibility(View.GONE);
                                                            holder.editImageTextFragment.setVisibility(View.GONE);
                                                            holder.ITFimg.setVisibility(View.GONE);
                                                            holder.ITFNoticeImg.setVisibility(View.VISIBLE);

                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Notice Updated Successfully !");
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
                                                        } else if (res.contains("Fail")) {

                                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(activity);
                                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
                                                            alertDialogBuilderUserInput.setView(mViewi);
                                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                            reasonTv.setText("Error Occured at Database Side , Try After Some Time!");
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

                                                        //loading= new LoadingDialog(login.this);

                                                        //Toast.makeText(context, "Sorry !", Toast.LENGTH_SHORT).show();


                                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        //loading.dismissDialog();
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
                                            //loading.dismissDialog();
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
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> map = new HashMap<>();

                                            //String path = getPath(filepath);
                                            map.put("id", product.getId());
                                            map.put("noticeDesc", holder.editImageTextFragment.getText().toString());

                                            return map;
                                        }


                                    };
                                    RequestQueue rq = Volley.newRequestQueue(context);
                                    rq.add(sr);

                                }
                            }
                        });
                        holder.ITFimgeditImageTextFCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();

                                holder.ITFnoticeDesc.setVisibility(View.VISIBLE);
                                holder.ITFNoticeImg.setVisibility(View.VISIBLE);
                                //holder.editImageTextFragment.setText(holder.ITFnoticeDesc.getText().toString());
                                holder.editimagetextF.setVisibility(View.GONE);
                                holder.ITFimg.setVisibility(View.GONE);
                            }
                        });
                    }
                });

                ///


            }

        }
        else if (role.equalsIgnoreCase("security")) {
            SocNoticeData product = productList.get(position);
            Log.e("GONE","SOCIETY");
            holder.DeleteNotice.setVisibility(View.GONE);
            holder.postedby.setText("Posted by :" + product.getName());
            holder.noticetitle.setText(product.getNoticeTitle());

            DateFormat df = new SimpleDateFormat("hh:mm aa");
            String currTime = df.format(Calendar.getInstance().getTime());

            DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
            String currDate = df2.format(Calendar.getInstance().getTime());

            holder.datetime.setText(currDate + " " + currTime);
            if (product.getNoticeUser().equalsIgnoreCase("2")) {

                if (product.getNoticeType().equalsIgnoreCase("1")) {
                    holder.TFN.setVisibility(View.VISIBLE);
                    holder.IFN.setVisibility(View.GONE);
                    holder.ITFN.setVisibility(View.GONE);

                    holder.TFnoticeDesc.setText(product.getNoticeDesc());
                    holder.editNotice.setVisibility(View.GONE);

                }
                else if (product.getNoticeType().equalsIgnoreCase("2")) {
                    holder.TFN.setVisibility(View.GONE);
                    holder.IFN.setVisibility(View.VISIBLE);
                    holder.ITFN.setVisibility(View.GONE);
                    holder.editNotice.setVisibility(View.GONE);
                    Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.IFNoticeImg);

                    String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();

                    holder.IFNoticeImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // TODO Auto-generated method stub
                            IMAGEVIEW_SELECTER = 1;
                            // TODO Auto-generated method stub
                            findDoubleClick(IMAGEVIEW_SELECTER,url);
                            if (mHasDoubleClicked) {

                                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                                alertDialogBuilderUserInput.setView(mView);
                                ImageView imgi=mView.findViewById(R.id.imggg);

                                new GetImageFromUrl("Hii").execute(url);
                                if(bitmap!=null){
                                    Log.e("Null","NO");
                                }
                                else {
                                    Log.e("Null","YES");
                                }
                                imgi.setImageBitmap( bitmap);
                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();


                            }
                        }
                    });

               /* holder.DeleteNotice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();
                        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                .format(System.currentTimeMillis());
                        AndroidNetworking.initialize(context);
                        File filepath = Environment.getExternalStorageDirectory();
                        File dir = new File(filepath+"/Society App");
                        dir.mkdirs();
                        fileName = "SOCNOTICE"+time+".PNG";
                        file = new File(dir, fileName);

                        AndroidNetworking.download(url, String.valueOf(dir), fileName)
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {

                                        Toast.makeText(activity, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });

                    }
                });*/


                }
                else if (product.getNoticeType().equalsIgnoreCase("3")) {
                    holder.TFN.setVisibility(View.GONE);
                    holder.IFN.setVisibility(View.GONE);
                    holder.ITFN.setVisibility(View.VISIBLE);
                    holder.editNotice.setVisibility(View.GONE);
                    holder.ITFnoticeDesc.setText(product.getNoticeDesc());
                    Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.ITFNoticeImg);

                    String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();

                    holder.ITFNoticeImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // TODO Auto-generated method stub
                            IMAGEVIEW_SELECTER = 1;
                            // TODO Auto-generated method stub
                            findDoubleClick(IMAGEVIEW_SELECTER,url);
                            if (mHasDoubleClicked) {

                                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                                alertDialogBuilderUserInput.setView(mView);
                                ImageView imgi=mView.findViewById(R.id.imggg);

                                new GetImageFromUrl("Hii").execute(url);
                                if(bitmap!=null){
                                    Log.e("Null","NO");
                                }
                                else {
                                    Log.e("Null","YES");
                                }
                                imgi.setImageBitmap( bitmap);
                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();


                            }
                        }
                    });


                }

            }

        }
        else if (role.equalsIgnoreCase("Society Member")) {
            SocNoticeData product = productList.get(position);

            Log.e("GONE","in Soc");
            //if (product.getNoticeUser().equalsIgnoreCase("1")) {
                holder.DeleteNotice.setVisibility(View.GONE);
                if (product.getNoticeType().equalsIgnoreCase("1") && product.getNoticeUser().equalsIgnoreCase("1")) {
                    holder.postedby.setText("Posted by :" + product.getName());
                    holder.noticetitle.setText(product.getNoticeTitle());

                    DateFormat df = new SimpleDateFormat("hh:mm aa");
                    String currTime = df.format(Calendar.getInstance().getTime());

                    DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
                    String currDate = df2.format(Calendar.getInstance().getTime());

                    holder.datetime.setText(currDate + " " + currTime);
                    holder.TFN.setVisibility(View.VISIBLE);
                    holder.IFN.setVisibility(View.GONE);
                    holder.ITFN.setVisibility(View.GONE);

                    holder.TFnoticeDesc.setText(product.getNoticeDesc());
                    holder.editNotice.setVisibility(View.GONE);

                    holder.editNotice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            holder.TFnoticeDesc.setVisibility(View.GONE);
                            holder.editTextFragment.setText(holder.TFnoticeDesc.getText().toString());
                            holder.editTextFragment.setVisibility(View.VISIBLE);
                            holder.edittextF.setVisibility(View.VISIBLE);

                        }
                    });
                }
                else if (product.getNoticeType().equalsIgnoreCase("2") && product.getNoticeUser().equalsIgnoreCase("1")) {
                    holder.postedby.setText("Posted by :" + product.getName());
                    holder.noticetitle.setText(product.getNoticeTitle());

                    DateFormat df = new SimpleDateFormat("hh:mm aa");
                    String currTime = df.format(Calendar.getInstance().getTime());

                    DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
                    String currDate = df2.format(Calendar.getInstance().getTime());

                    holder.datetime.setText(currDate + " " + currTime);
                    Log.e("GONE","YEEEPPPP");
                    holder.TFN.setVisibility(View.GONE);
                    holder.IFN.setVisibility(View.VISIBLE);
                    holder.ITFN.setVisibility(View.GONE);
                    holder.editNotice.setVisibility(View.GONE);
                    Toast.makeText(context, product.getNoticeImg(), Toast.LENGTH_SHORT).show();
                    Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.IFNoticeImg);

                    String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();

                    holder.IFNoticeImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // TODO Auto-generated method stub
                            IMAGEVIEW_SELECTER = 1;
                            // TODO Auto-generated method stub
                            findDoubleClick(IMAGEVIEW_SELECTER,url);
                            if (mHasDoubleClicked) {

                                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                                alertDialogBuilderUserInput.setView(mView);
                                ImageView imgi=mView.findViewById(R.id.imggg);

                                new GetImageFromUrl("Hii").execute(url);
                                if(bitmap!=null){
                                    Log.e("Null","NO");
                                }
                                else {
                                    Log.e("Null","YES");
                                }
                                imgi.setImageBitmap( bitmap);
                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();


                            }
                        }
                    });
               /* holder.DeleteNotice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();
                        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                .format(System.currentTimeMillis());
                        AndroidNetworking.initialize(context);
                        File filepath = Environment.getExternalStorageDirectory();
                        File dir = new File(filepath+"/Society App");
                        dir.mkdirs();
                        fileName = "SOCNOTICE"+time+".PNG";
                        file = new File(dir, fileName);

                        AndroidNetworking.download(url, String.valueOf(dir), fileName)
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {

                                        Toast.makeText(activity, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });

                    }
                });*/


                }
                else if (product.getNoticeType().equalsIgnoreCase("3") && product.getNoticeUser().equalsIgnoreCase("1")) {
                    //SocNoticeData product = productList.get(position);
                    Log.e("GONE","SOCIETY");
                    holder.postedby.setText("Posted by :" + product.getName());
                    holder.noticetitle.setText(product.getNoticeTitle());

                    DateFormat df = new SimpleDateFormat("hh:mm aa");
                    String currTime = df.format(Calendar.getInstance().getTime());

                    DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
                    String currDate = df2.format(Calendar.getInstance().getTime());

                    holder.datetime.setText(currDate + " " + currTime);
                    holder.TFN.setVisibility(View.GONE);
                    holder.IFN.setVisibility(View.GONE);
                    holder.ITFN.setVisibility(View.VISIBLE);
                    holder.editNotice.setVisibility(View.GONE);
                    holder.ITFnoticeDesc.setText(product.getNoticeDesc());
                    Picasso.get().load("https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg()).into(holder.ITFNoticeImg);

                    String url = "https://vivek.ninja/Soc_Notice/NoticeImage/" + product.getNoticeImg();

                    holder.ITFNoticeImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // TODO Auto-generated method stub
                            IMAGEVIEW_SELECTER = 1;
                            // TODO Auto-generated method stub
                            findDoubleClick(IMAGEVIEW_SELECTER,url);
                            if (mHasDoubleClicked) {

                                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                                View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                                alertDialogBuilderUserInput.setView(mView);
                                ImageView imgi=mView.findViewById(R.id.imggg);

                                new GetImageFromUrl("Hii").execute(url);
                                if(bitmap!=null){
                                    Log.e("Null","NO");
                                }
                                else {
                                    Log.e("Null","YES");
                                }
                                imgi.setImageBitmap( bitmap);
                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();


                            }
                        }
                    });


                }

            //}
        }
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView postedby, datetime, noticetitle, TFnoticeDesc, ITFnoticeDesc;
        ConstraintLayout IFN, ITFN, TFN;
        EditText editTextFragment, editImageTextFragment;
        LinearLayout edittextF, editimagetextF;
        ImageView IFNoticeImg, ITFNoticeImg, DeleteNotice, DownloadImg, editNotice, editTextFDone, editTextFCancel, ITFimg, editImageTextFDone, ITFimgeditImageTextFCancel;


        public myviewholder(View itemView) {
            super(itemView);

            postedby = (TextView) itemView.findViewById(R.id.postedbyii);
            datetime = (TextView) itemView.findViewById(R.id.noticedatetime);
            noticetitle = (TextView) itemView.findViewById(R.id.titlenotice);
            TFnoticeDesc = (TextView) itemView.findViewById(R.id.TNFDesc);
            ITFnoticeDesc = (TextView) itemView.findViewById(R.id.ITNoticeDesc);
            IFNoticeImg = (ImageView) itemView.findViewById(R.id.ITNFimage);
            ITFNoticeImg = (ImageView) itemView.findViewById(R.id.ITnoticeimage);
            DeleteNotice = (ImageView) itemView.findViewById(R.id.deletenotice);
            editNotice = (ImageView) itemView.findViewById(R.id.editnotice);
            editTextFDone = (ImageView) itemView.findViewById(R.id.edittextfragmentdone);
            editImageTextFDone = (ImageView) itemView.findViewById(R.id.editimagetextfragmentdone);

            ITFimg = (ImageView) itemView.findViewById(R.id.editIMGimagetextnoticefragment);
            editTextFCancel = (ImageView) itemView.findViewById(R.id.edittextfragmentnotdone);
            ITFimgeditImageTextFCancel = (ImageView) itemView.findViewById(R.id.editimagetextfragmentnotdone);
            editTextFragment = (EditText) itemView.findViewById(R.id.edittextnoticefragment);
            editImageTextFragment = (EditText) itemView.findViewById(R.id.editimagetextnoticefragment);


            IFN = (ConstraintLayout) itemView.findViewById(R.id.imagenoticefragment);
            edittextF = (LinearLayout) itemView.findViewById(R.id.edittextfragmentLL);
            editimagetextF = (LinearLayout) itemView.findViewById(R.id.editimagetextfragmentLL);
            ITFN = (ConstraintLayout) itemView.findViewById(R.id.imagetextnoticefragment);
            TFN = (ConstraintLayout) itemView.findViewById(R.id.textnoticefragment);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
