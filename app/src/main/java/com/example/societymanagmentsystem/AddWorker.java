package com.example.societymanagmentsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.FileUtil;

import static com.android.volley.Request.Method.POST;

public class AddWorker extends AppCompatActivity {

    Spinner spinner_type_worker;
    ArrayAdapter<CharSequence> workerTypeAdapter;
    String spin_value_worker = "Type";
    String imagePath="";
    EditText workerName,workerPhoneNo,workerEmail,workerSpeciality,workerAddress,workerCity;
    Button btn_add_Worker;
    private static final String UPLOAD_URL_1=  "https://vivek.ninja/worker/add_worker.php";
    private File actualImage;
    ImageButton imageButton;
    LoadingDialog loading;
    private int CAPTURE_PHOTO = 104;
    public Bitmap bitmap;
    String WORKER_MOBILE;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);

        workerName = findViewById(R.id.text_name_worker);
        workerPhoneNo = findViewById(R.id.text_mobile_worker);
        workerEmail = findViewById(R.id.worker_email);
        workerSpeciality = findViewById(R.id.edSpeciality);
        workerAddress = findViewById(R.id.text_address_worker);
        workerCity = findViewById(R.id.text_city_worker);
        btn_add_Worker = findViewById(R.id.btn_add_worker);
        imageButton = findViewById(R.id.prof_pic_worker);
        spinner_type_worker = findViewById(R.id.spinner_type_worker);
        circleImageView = findViewById(R.id.circular_worker_profile_pic);
        circleImageView.setTag("0");

        workerTypeAdapter = ArrayAdapter.createFromResource(this,R.array.WorkerSpec,android.R.layout.simple_spinner_item);
        workerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type_worker.setAdapter(workerTypeAdapter);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AddWorker.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddWorker.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_choosepic, null);
                                TextView camera = mViewi.findViewById(R.id.cameraTitle1);
                                TextView gallary = mViewi.findViewById(R.id.cameraTitle2);
                                TextView cancel = mViewi.findViewById(R.id.cameraTitle3);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddWorker.this);
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
        });


        btn_add_Worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner_type_worker.getSelectedItemPosition() == 0){
                    ((TextView)spinner_type_worker.getSelectedView()).setError("Error message");
                    //Toast.makeText(login.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(workerPhoneNo.getText().toString().isEmpty()){
                    workerPhoneNo.setError("Phone Number Required");
                    return;
                }
                if(workerName.getText().toString().isEmpty()){
                    workerName.setError("Name is Required");
                    return;
                }
                if(circleImageView.getTag().toString().equalsIgnoreCase("0")){
                    Toast.makeText(AddWorker.this, "Please upload Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(workerAddress.getText().toString().isEmpty()){
                    workerAddress.setError("Address Required");
                    return;
                }
                if(workerEmail.getText().toString().isEmpty()){
                    workerEmail.setError("Email Required");
                    return;
                }
                if(workerCity.getText().toString().isEmpty()){
                    workerCity.setError("City Required");
                    return;
                }
                if(workerSpeciality.getText().toString().isEmpty()){
                    workerSpeciality.setError("Worker Speciality Required");
                    return;
                }
                if(!(isValidMobile(workerPhoneNo.getText().toString().trim()))){
                    Toast.makeText(AddWorker.this, "Invalid Mobile Number !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(isValidEmailId(workerEmail.getText().toString().trim()))) {
                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //circleImageView.getDrawable().toString();
                //Toast.makeText(AddWorker.this,  circleImageView.getDrawable().toString(), Toast.LENGTH_SHORT).show();
                WORKER_MOBILE = workerPhoneNo.getText().toString();

                loading= new LoadingDialog(AddWorker.this);
                loading.startLoadingDialog();

                StringRequest sr = new StringRequest(POST, UPLOAD_URL_1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    String res = js.getString("repon");

                                    if(res.contains("no")){
                                        loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddWorker.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddWorker.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Error Occured At Database Side , Try Again Later !");
                                        reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
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
                                        //Toast.makeText(AddWorker.this, "Sorry ! Try Again Letter !", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(res.contains("yes")){
                                        loading.dismissDialog();
                                        //workerName.setText("");
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddWorker.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddWorker.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Worker Added SuccessFully !");
                                        reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                        final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                        reasonBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogi.dismiss();
                                                Intent j = new Intent(AddWorker.this,Add_Worker_List.class);
                                                startActivity(j);
                                                finish();
                                            }
                                        });

                                        dialogi.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialogi.setCanceledOnTouchOutside(false);
                                        dialogi.show();


                                        //Toast.makeText(AddWorker.this, "SUCCESS !", Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    loading.dismissDialog();
                                    e.printStackTrace();

                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddWorker.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddWorker.this);
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
                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddWorker.this);
                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddWorker.this);
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
                            }
                        });

                        dialogi.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogi.setCanceledOnTouchOutside(false);
                        dialogi.show();

                        //Toast.makeText(AddWorker.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("workname",workerName.getText().toString().trim());
                        map.put("workphone","+91"+workerPhoneNo.getText().toString());
                        map.put("workemail",workerEmail.getText().toString().trim());
                        map.put("workadrs",workerAddress.getText().toString());
                        map.put("workimage",encodeBitmapImage(bitmap));
                        map.put("worktype",spinner_type_worker.getSelectedItem().toString());
                        map.put("workspec",workerSpeciality.getText().toString());
                        map.put("workcity",workerCity.getText().toString().trim().toLowerCase());
                        return  map;
                    }
                };
                RequestQueue rq = Volley.newRequestQueue(AddWorker.this);
                rq.add(sr);


            }
        });




    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 10;
        }
        return false;
    }
    private String encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        return (Base64.encodeToString(bytesofimage, Base64.DEFAULT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode ==104){
                bitmap= (Bitmap)data.getExtras().get("data");
                circleImageView.setImageBitmap(bitmap);
                circleImageView.setTag("1");
                //final_image = encodeBitmapImage(captureImage);

            }
            if(requestCode==1){
                Uri filepath=data.getData();
                try
                {
                    actualImage = FileUtil.from(this, data.getData());
                    //String len = getReadableFileSize(actualImage.length());
                    //Log.i("imagesize",len );
                    InputStream inputStream=getContentResolver().openInputStream(filepath);
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    circleImageView.setImageBitmap(bitmap);
                    circleImageView.setTag("1");
                    //final_image = encodeBitmapImage(bitmap);
                    //encodeBitmapImage(bitmap);
                    //img.setVisibility(View.VISIBLE);


                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}