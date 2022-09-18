package com.example.societymanagmentsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.FileUtil;

import static com.android.volley.Request.Method.POST;

public class AddNotice extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView IFimg, ITFimg;
    EditText noticeTitle, TFnoticeDesc, ITFnoticeDesc;
    Button submit;
    int IMAGEVIEW_SELECTER = -1;
    ConstraintLayout textonly, imageonly, imagetext;
    Spinner notice_type_worker,notice_user_spinner;
    ArrayAdapter<CharSequence> noticeTypeAdapter,noticeUserAdapter;
    private int CAPTURE_PHOTO = 104;
    public Bitmap bitmap,bmp;
    private File actualImage;
    GestureDetector gestureDetector;
    boolean tapped;
    int notice_type=-1,notice_user=-1;

    String userName,userPhone,userCity,userSociety,userBlock;

    private static final String UPLOAD_URL1 = "https://vivek.ninja/Soc_Notice/add_notice.php";

    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    boolean mHasDoubleClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        Intent i = getIntent();
        userName = i.getStringExtra("name");
        userPhone = i.getStringExtra("phoneno");
        userCity = i.getStringExtra("city");
        userSociety = i.getStringExtra("society");
        userBlock = i.getStringExtra("block");

        //gestureDetector = new GestureDetector(AddNotice.this,new GestureListener());
        submit = findViewById(R.id.submitnotice);
        IFimg = findViewById(R.id.imagefragmentimg);
        ITFimg = findViewById(R.id.imagetextfragmentimage);
        noticeTitle = findViewById(R.id.noticetitle);
        TFnoticeDesc = findViewById(R.id.noticedesc);
        ITFnoticeDesc = findViewById(R.id.imagetextfragmentnoticedesc);
        textonly = findViewById(R.id.textfragment);
        imageonly = findViewById(R.id.imagefragment);
        imagetext = findViewById(R.id.imagetextfragment);
        notice_type_worker = findViewById(R.id.noticetypespinner);
        notice_user_spinner = findViewById(R.id.noticeUserspinner);

        noticeTypeAdapter = ArrayAdapter.createFromResource(this, R.array.noticetype, android.R.layout.simple_spinner_item);
        noticeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notice_type_worker.setAdapter(noticeTypeAdapter);

        notice_type_worker.setOnItemSelectedListener(this);

        noticeUserAdapter = ArrayAdapter.createFromResource(this, R.array.noticeuser, android.R.layout.simple_spinner_item);
        noticeUserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notice_user_spinner.setAdapter(noticeUserAdapter);
        notice_user_spinner.setOnItemSelectedListener(this);

        /*IFimg.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);
            }

        });*/


        IFimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMAGEVIEW_SELECTER = 1;
                // TODO Auto-generated method stub
                findDoubleClick(IMAGEVIEW_SELECTER);
                if (mHasDoubleClicked) {


                            //Toast.makeText(context, "HIIHIHIHI", Toast.LENGTH_SHORT).show();
                            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(AddNotice.this);
                            View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);

                            alertDialogBuilderUserInput.setView(mView);
                            ImageView imgi=mView.findViewById(R.id.imggg);
                            //img.setImageDrawable(holder.img.getDrawable());
                            //Picasso.get(imgi.getContext().).load("https://vivek.ninja/images/" + product.getImage()).into(imgi);
                             /*Picasso.get()
                        .load("https://vivek.ninja/images/" + product.getImage())
                        .into(imgi);*/

                    imgi.setImageBitmap(bitmap);

                    ////////Glide.with(imgi.getContext()).load("https://vivek.ninja/images/" + product.getImage()).fitCenter().into(imgi);

                            //img.setMaxWidth(mView.);
                            //img.setMaxHeight();

                            final AlertDialog dialog = alertDialogBuilderUserInput.create();
                            //dialog.setCanceledOnTouchOutside(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.show();
                    //Toast.makeText(AddNotice.this, "Double click", Toast.LENGTH_SHORT).show();

                }
            }
        });

        ITFimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMAGEVIEW_SELECTER = 2;
                // TODO Auto-generated method stub
                findDoubleClick(IMAGEVIEW_SELECTER);
                if (mHasDoubleClicked) {
                    //Toast.makeText(AddNotice.this, "Double click", Toast.LENGTH_SHORT).show();

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(AddNotice.this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.fragement_img, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);

                    alertDialogBuilderUserInput.setView(mView);
                    ImageView imgi=mView.findViewById(R.id.imggg);
                    //img.setImageDrawable(holder.img.getDrawable());
                    //Picasso.get(imgi.getContext().).load("https://vivek.ninja/images/" + product.getImage()).into(imgi);
                /*Picasso.get()
                        .load("https://vivek.ninja/images/" + product.getImage())
                        .into(imgi);*/

                    imgi.setImageBitmap(bitmap);

                    ////////Glide.with(imgi.getContext()).load("https://vivek.ninja/images/" + product.getImage()).fitCenter().into(imgi);

                    //img.setMaxWidth(mView.);
                    //img.setMaxHeight();

                    final AlertDialog dialog = alertDialogBuilderUserInput.create();
                    //dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Notice Title Required !");
                    return;
                }
                if(notice_type_worker.getSelectedItemPosition()==0){
                    Toast.makeText(AddNotice.this, "Please Select Notice Type !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(notice_user_spinner.getSelectedItemPosition()==0){
                    Toast.makeText(AddNotice.this, "Please Select Notice User !", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (notice_type_worker.getSelectedItemPosition()){
                    case 1:
                        notice_type=1;
                        if(TFnoticeDesc.getText().toString().isEmpty()){
                            TFnoticeDesc.setError("Notice Description Required !");
                            return;
                        }
                        break;
                    case 2:
                        if(IFimg.getTag().toString().equalsIgnoreCase("0")){
                            Toast.makeText(AddNotice.this, "Please Upload Image !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        notice_type=2;
                        break;
                    case 3:
                        if(ITFnoticeDesc.getText().toString().isEmpty()){
                            ITFnoticeDesc.setError("Notice Description Required !");
                            return;
                        }
                        if(ITFimg.getTag().toString().equalsIgnoreCase("0")){
                            Toast.makeText(AddNotice.this, "Please Upload Image !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        notice_type=3;
                        break;
                }
                switch (notice_user_spinner.getSelectedItemPosition()){
                    case 1:
                        notice_user=1;
                        break;
                    case 2:
                        notice_user=2;
                        break;
                }
                //Log.e("BItmap ",encodeBitmapImage(bitmap));
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

                                    if(!res.contains("no")){
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Notice Uploaded Successfully !");
                                        reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                        final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                        reasonBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogi.dismiss();
                                                Intent k = new Intent(AddNotice.this,Dashboard.class);
                                                startActivity(k);
                                                finish();
                                            }
                                        });

                                        dialogi.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialogi.setCanceledOnTouchOutside(false);
                                        dialogi.show();
                                        //Toast.makeText(context, "SuccessFully !", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(res.contains("no")){
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Error Occured At Database Side ! Try Again Later !");
                                        reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                        final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                        reasonBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogi.dismiss();
                                                Intent k = new Intent(AddNotice.this,Dashboard.class);
                                                startActivity(k);
                                                finish();
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
                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                    alertDialogBuilderUserInput.setView(mViewi);
                                    TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                    ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                    Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                    reasonTv.setText(e.getMessage());
                                    reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                            getResources().getDimension(R.dimen.result_font));
                                    reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                    reasonBtn.setText("OK");
                                    reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                    final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                    reasonBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogi.dismiss();
                                            Intent k = new Intent(AddNotice.this,Dashboard.class);
                                            startActivity(k);
                                            finish();
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
                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                        alertDialogBuilderUserInput.setView(mViewi);
                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                        reasonTv.setText(error.toString());
                        reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                getResources().getDimension(R.dimen.result_font));
                        reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                        reasonBtn.setText("OK");
                        reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                        final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                        reasonBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogi.dismiss();
                                Intent k = new Intent(AddNotice.this,Dashboard.class);
                                startActivity(k);
                                finish();
                            }
                        });

                        dialogi.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogi.setCanceledOnTouchOutside(false);
                        dialogi.show();
                        Toast.makeText(AddNotice.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("name", userName);
                        map.put("phone", userPhone);
                        map.put("city", userCity);
                        map.put("society", userSociety);
                        map.put("block", userBlock);
                        map.put("noticeTitle", noticeTitle.getText().toString());
                        map.put("noticeType", String.valueOf(notice_type));
                        map.put("noticeUser", String.valueOf(notice_user));
                        if(notice_type_worker.getSelectedItemPosition()==1){
                            map.put("noticeDesc", TFnoticeDesc.getText().toString());
                        }else if (notice_type_worker.getSelectedItemPosition()==2){
                            map.put("noticeImage",encodeBitmapImage(bitmap));
                        }else if(notice_type_worker.getSelectedItemPosition()==3){
                            map.put("noticeImage",encodeBitmapImage(bitmap));
                            map.put("noticeDesc",ITFnoticeDesc.getText().toString());
                        }
                        return map;
                    }
                };
                RequestQueue rq = Volley.newRequestQueue(AddNotice.this);
                rq.add(sr);

            }
        });

        /*IFimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMAGEVIEW_SELECTER = 1;
                Dexter.withActivity(AddNotice.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_choosepic, null);
                                TextView camera = mViewi.findViewById(R.id.cameraTitle1);
                                TextView gallary = mViewi.findViewById(R.id.cameraTitle2);
                                TextView cancel = mViewi.findViewById(R.id.cameraTitle3);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                alertDialogBuilderUserInput.setView(mViewi);

                                alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                        return i == KeyEvent.KEYCODE_BACK;
                                    }
                                });


                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                camera.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent,CAPTURE_PHOTO);
                                        dialog.dismiss();
                                        //Toast.makeText(AddWorker.this, "Camera", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                gallary.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                        dialog.dismiss();
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        //Toast.makeText(AddWorker.this, "Cancel", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //intent.setType("image/*");
                                ///startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                //startActivityForResult(intent,CAPTURE_PHOTO);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });*/

        /*ITFimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMAGEVIEW_SELECTER = 2;
                Dexter.withActivity(AddNotice.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_choosepic, null);
                                TextView camera = mViewi.findViewById(R.id.cameraTitle1);
                                TextView gallary = mViewi.findViewById(R.id.cameraTitle2);
                                TextView cancel = mViewi.findViewById(R.id.cameraTitle3);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                alertDialogBuilderUserInput.setView(mViewi);

                                alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                        return i == KeyEvent.KEYCODE_BACK;
                                    }
                                });


                                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                camera.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent,CAPTURE_PHOTO);
                                        dialog.dismiss();
                                        //Toast.makeText(AddWorker.this, "Camera", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                gallary.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                        dialog.dismiss();
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        //Toast.makeText(AddWorker.this, "Cancel", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //intent.setType("image/*");
                                ///startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                //startActivityForResult(intent,CAPTURE_PHOTO);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });*/


    }

    /*public class GestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(AddNotice.this, "SIngle CLicked", Toast.LENGTH_SHORT).show();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            tapped = !tapped;

            if (tapped) {
                Toast.makeText(AddNotice.this, "Double CLicked", Toast.LENGTH_SHORT).show();
            } else {

            }

            return true;
        }
    }*/

    private boolean findDoubleClick(int IMAGEVIEW_SELECTER) {
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

                            Dexter.withActivity(AddNotice.this)
                                    .withPermission(Manifest.permission.CAMERA)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_choosepic, null);
                                            TextView camera = mViewi.findViewById(R.id.cameraTitle1);
                                            TextView gallary = mViewi.findViewById(R.id.cameraTitle2);
                                            TextView cancel = mViewi.findViewById(R.id.cameraTitle3);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                            alertDialogBuilderUserInput.setView(mViewi);

                                            alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                                    return i == KeyEvent.KEYCODE_BACK;
                                                }
                                            });


                                            final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                            camera.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    startActivityForResult(intent, CAPTURE_PHOTO);
                                                    dialog.dismiss();
                                                    //Toast.makeText(AddWorker.this, "Camera", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            gallary.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                                    intent.setType("image/*");
                                                    startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1);
                                                    dialog.dismiss();
                                                }
                                            });
                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                    //Toast.makeText(AddWorker.this, "Cancel", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            //Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            //intent.setType("image/*");
                                            ///startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                            //startActivityForResult(intent,CAPTURE_PHOTO);
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {

                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            token.continuePermissionRequest();
                                        }
                                    }).check();
                        } else if (IMAGEVIEW_SELECTER == 2) {
                            Dexter.withActivity(AddNotice.this)
                                    .withPermission(Manifest.permission.CAMERA)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response)
                                        {
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddNotice.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_choosepic, null);
                                            TextView camera = mViewi.findViewById(R.id.cameraTitle1);
                                            TextView gallary = mViewi.findViewById(R.id.cameraTitle2);
                                            TextView cancel = mViewi.findViewById(R.id.cameraTitle3);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddNotice.this);
                                            alertDialogBuilderUserInput.setView(mViewi);

                                            alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                                    return i == KeyEvent.KEYCODE_BACK;
                                                }
                                            });


                                            final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                            camera.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    startActivityForResult(intent,CAPTURE_PHOTO);
                                                    dialog.dismiss();
                                                    //Toast.makeText(AddWorker.this, "Camera", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            gallary.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                                    intent.setType("image/*");
                                                    startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                                    dialog.dismiss();
                                                }
                                            });
                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                    //Toast.makeText(AddWorker.this, "Cancel", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            //Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            //intent.setType("image/*");
                                            ///startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                                            //startActivityForResult(intent,CAPTURE_PHOTO);
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {

                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            token.continuePermissionRequest();
                                        }
                                    }).check();
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

    private String encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        return (Base64.encodeToString(bytesofimage, Base64.DEFAULT));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 104) {
                if(bitmap!=null){
                    bitmap.recycle();
                    bitmap = null;
                }
                bitmap = (Bitmap) data.getExtras().get("data");
                if (IMAGEVIEW_SELECTER == 1) {
                    Log.e("Gone","YES");
                    IFimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    IFimg.setImageBitmap(bitmap);
                    IFimg.setTag("Uploaded");
                    IMAGEVIEW_SELECTER = -1;
                } else if (IMAGEVIEW_SELECTER == 2) {
                    ITFimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ITFimg.setImageBitmap(bitmap);
                    ITFimg.setTag("Uploaded");
                    IMAGEVIEW_SELECTER = -1;
                }
                //final_image = encodeBitmapImage(captureImage);

            }
            if (requestCode == 1) {
                Uri filepath = data.getData();
                try {
                    actualImage = FileUtil.from(this, data.getData());
                    //String len = getReadableFileSize(actualImage.length());
                    //Log.i("imagesize",len );
                    InputStream inputStream = getContentResolver().openInputStream(filepath);
                    if(bitmap!=null){
                        bitmap.recycle();
                        bitmap = null;
                    }
                    bitmap = BitmapFactory.decodeStream(inputStream);

                    if (IMAGEVIEW_SELECTER == 1) {
                        IFimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        IFimg.setImageBitmap(bitmap);
                        IFimg.setTag("Uploaded");
                        IMAGEVIEW_SELECTER = -1;
                    } else if (IMAGEVIEW_SELECTER == 2) {
                        ITFimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ITFimg.setImageBitmap(bitmap);
                        ITFimg.setTag("Uploaded");
                        IMAGEVIEW_SELECTER = -1;
                    }

                    //final_image = encodeBitmapImage(bitmap);
                    //encodeBitmapImage(bitmap);
                    //img.setVisibility(View.VISIBLE);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.noticetypespinner) {
            if(bitmap!=null){
                bitmap.recycle();
                bitmap = null;
            }
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.uploadimg);
            IFimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            IFimg.setImageBitmap(bmp);
            IFimg.setTag("0");
            ITFimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ITFimg.setImageBitmap(bmp);
            ITFimg.setTag("0");


            if (notice_type_worker.getSelectedItemPosition() == 1) {
                textonly.setVisibility(View.VISIBLE);
                imageonly.setVisibility(View.GONE);
                imagetext.setVisibility(View.GONE);


            } else if (notice_type_worker.getSelectedItemPosition() == 2) {

                textonly.setVisibility(View.GONE);
                imageonly.setVisibility(View.VISIBLE);
                imagetext.setVisibility(View.GONE);


            } else if (notice_type_worker.getSelectedItemPosition() == 3) {
                textonly.setVisibility(View.GONE);
                imageonly.setVisibility(View.GONE);
                imagetext.setVisibility(View.VISIBLE);


            } else if (notice_type_worker.getSelectedItemPosition() == 0) {
                textonly.setVisibility(View.GONE);
                imageonly.setVisibility(View.GONE);
                imagetext.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}