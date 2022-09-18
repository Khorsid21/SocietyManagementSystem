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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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

public class AddEmgnNo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner_type_emgnno;
    ArrayAdapter<CharSequence> emgnnoTypeAdapter;



    String spin_value_worker = "Type";
    String imagePath="";
    EditText emgnnoName,emgnnoPhoneNo,emgnnoEmail,emgnnoSpeciality,emgnnoAddress,emgnCity;
    Button btn_add_emgnno;
    private static final String UPLOAD_URL_1=  "https://vivek.ninja/emgn/add_emgnno.php";
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
        setContentView(R.layout.activity_add_emgn_no);

        emgnnoName = findViewById(R.id.text_name_emgnno);
        emgnnoPhoneNo = findViewById(R.id.text_mobile_emgnno);
        emgnnoEmail = findViewById(R.id.emgnno_email);
        emgnnoSpeciality = findViewById(R.id.emgnnoedSpeciality);
        emgnnoAddress = findViewById(R.id.text_address_rmgnno);
        emgnCity = findViewById(R.id.emgnnoedCity);

        btn_add_emgnno = findViewById(R.id.btn_add_emgnno);
        imageButton = findViewById(R.id.prof_pic_emgnno);

        spinner_type_emgnno = findViewById(R.id.spinner_type_emgnno);
        circleImageView = findViewById(R.id.circular_emgnno_profile_pic);
        circleImageView.setTag("0");

        emgnnoTypeAdapter = ArrayAdapter.createFromResource(this,R.array.EmgnnoType,android.R.layout.simple_spinner_item);
        emgnnoTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type_emgnno.setAdapter(emgnnoTypeAdapter);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AddEmgnNo.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddEmgnNo.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_choosepic, null);
                                TextView camera = mViewi.findViewById(R.id.cameraTitle1);
                                TextView gallary = mViewi.findViewById(R.id.cameraTitle2);
                                TextView cancel = mViewi.findViewById(R.id.cameraTitle3);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddEmgnNo.this);
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

        spinner_type_emgnno.setOnItemSelectedListener(this);

        btn_add_emgnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner_type_emgnno.getSelectedItemPosition() == 0){
                    ((TextView)spinner_type_emgnno.getSelectedView()).setError("Please Select Emergency Type");
                    //Toast.makeText(login.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(emgnnoPhoneNo.getText().toString().isEmpty()){
                    emgnnoPhoneNo.setError("Phone Number Required");
                    return;
                }
                if(emgnCity.getText().toString().isEmpty()){
                    emgnCity.setError("City is Required");
                    return;
                }
                if(emgnnoName.getText().toString().isEmpty()){
                    emgnnoName.setError("Name is Required");
                    return;
                }
                if(emgnnoAddress.getText().toString().isEmpty()){
                    emgnnoAddress.setError("Address Required");
                    return;
                }
                if(circleImageView.getTag().toString().equalsIgnoreCase("0")){
                    Toast.makeText(AddEmgnNo.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(emgnnoEmail.getText().toString().isEmpty()){
                    emgnnoEmail.setError("Email Required");
                    return;
                }
                //
                if(spinner_type_emgnno.getSelectedItemPosition() == 3 && emgnnoSpeciality.getText().toString().isEmpty()){
                    emgnnoSpeciality.setError("Available Blood Type Required !");
                    return;
                }
                //
                if(!(isValidMobile(emgnnoPhoneNo.getText().toString().trim()))){
                    Toast.makeText(AddEmgnNo.this, "Invalid Mobile Number !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(isValidEmailId(emgnnoEmail.getText().toString().trim()))) {
                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //circleImageView.getDrawable().toString();
                //Toast.makeText(AddWorker.this,  circleImageView.getDrawable().toString(), Toast.LENGTH_SHORT).show();
                WORKER_MOBILE = emgnnoPhoneNo.getText().toString();

                loading= new LoadingDialog(AddEmgnNo.this);
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

                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddEmgnNo.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddEmgnNo.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Error Occured At Database Side , Try Again Later !");
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


                                        //Toast.makeText(AddEmgnNo.this, "Sorry ! Try Again Letter !", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(res.contains("yes")){
                                        loading.dismissDialog();

                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddEmgnNo.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddEmgnNo.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Emergency Contact Added Successfully !");
                                        reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                        final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                        reasonBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogi.dismiss();
                                                Intent j = new Intent(AddEmgnNo.this,AddEmgnNoList.class);
                                                startActivity(j);
                                                finish();
                                            }
                                        });

                                        dialogi.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialogi.setCanceledOnTouchOutside(false);
                                        dialogi.show();

                                        //emgnnoName.setText("");

                                        //Toast.makeText(AddEmgnNo.this, "SUCCESS !", Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    loading.dismissDialog();
                                    e.printStackTrace();
                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddEmgnNo.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddEmgnNo.this);
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
                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(AddEmgnNo.this);
                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddEmgnNo.this);
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
                        //Toast.makeText(AddEmgnNo.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("workname",emgnnoName.getText().toString().trim());
                        map.put("workphone","+91"+emgnnoPhoneNo.getText().toString());
                        map.put("workemail",emgnnoEmail.getText().toString().trim());
                        map.put("workadrs",emgnnoAddress.getText().toString());
                        map.put("workimage",encodeBitmapImage(bitmap));
                        map.put("worktype",spinner_type_emgnno.getSelectedItem().toString());
                        map.put("workCity",emgnCity.getText().toString().trim().toLowerCase());

                        map.put("workspec",emgnnoSpeciality.getText().toString());
                        return  map;
                    }
                };
                RequestQueue rq = Volley.newRequestQueue(AddEmgnNo.this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinner_type_emgnno) {
            if (spinner_type_emgnno.getSelectedItemPosition() == 3) {
                emgnnoSpeciality.setVisibility(View.VISIBLE);
            }
            else if(spinner_type_emgnno.getSelectedItemPosition() != 3){
                emgnnoSpeciality.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}