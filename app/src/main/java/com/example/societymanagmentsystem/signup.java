package com.example.societymanagmentsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import id.zelory.compressor.FileUtil;

import static com.android.volley.Request.Method.POST;

public class signup extends AppCompatActivity {

    Spinner spinnerrole;
    ArrayAdapter<CharSequence> roleAdapter;

    EditText name,email,phoneno,otpcode;
    Button getotp,verifyotp,resendotp,SelImg;
    public  String uname,uemail,uphonenum,verificationId;
    ImageView img ;
    public Bitmap bitmap;
    String encodedImage ;
    PhoneAuthProvider.ForceResendingToken token;
    TextView signin;

    EditText gotp1, gotp2, gotp3, gotp4,gotp5,gotp6;
    LinearLayout otpcodell;

    private File actualImage;

    public String USER;

    RadioGroup rg;
    RadioButton rb;
    public LoadingDialog loading;

    //private static final String UPLOAD_URL ="https://vivek101.heliohost.us/register.php";

    //private static final String UPLOAD_URL ="https://viveksocietyapp.byethost6.com/image.php";

    //private static final String UPLOAD_URL=  "https://adrenal-pairs.000webhostapp.com/signup/image.php";
    private static final String UPLOAD_URL=  "https://vivek.ninja/signup/image.php";
    //private static final String UPLOAD_URL_1=  "https://adrenal-pairs.000webhostapp.com/signup/varify_loginuser.php";
    private static final String UPLOAD_URL_1=  "https://vivek.ninja/signup/varify_loginuser.php";

    //private static final String UPLOAD_URL = "http://vivekdabhi.atwebpages.com/image.php";

    //private static final String UPLOAD_URL ="http://vivekdabhi.infinityfreeapp.com/image.php";

    //private static final String UPLOAD_URL ="http://192.168.43.150/image.php";

    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    private static final int STORAGE_PERMISSION_CODE = 4655;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SelImg = findViewById(R.id.uloadImg);

        requestStoragePermission();

        rg = findViewById(R.id.radioGroup1);

        spinnerrole = findViewById(R.id.rolesignup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneno = findViewById(R.id.mobmno);
        otpcode = findViewById(R.id.otpcode);

        getotp = findViewById(R.id.buttongetotp);
        verifyotp = findViewById(R.id.buttonverifyotp);
        resendotp = findViewById(R.id.resendotp);

        gotp1 = findViewById(R.id.eeotp1);
        gotp2 = findViewById(R.id.eeotp2);
        gotp3 = findViewById(R.id.eeotp3);
        gotp4 = findViewById(R.id.eeotp4);
        gotp5 = findViewById(R.id.eeotp5);
        gotp6 = findViewById(R.id.eeotp6);

        otpcodell = findViewById(R.id.signotpnoll);

        fAuth = FirebaseAuth.getInstance();

        roleAdapter = ArrayAdapter.createFromResource(this,R.array.role,android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerrole.setAdapter(roleAdapter);


        img = findViewById(R.id.UImg);
        signin = findViewById(R.id.txtSignIn);

        gotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    gotp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        gotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    gotp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        gotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    gotp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        gotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    gotp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        gotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    gotp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signup.this,login.class);
                startActivity(i);
            }
        });

        SelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(signup.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();



                //ShowFileChooser();
            }
        });

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() && email.getText().toString().isEmpty() && phoneno.getText().toString().isEmpty()){
                    Toast.makeText(signup.this, "Enter All Fields !", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(name.getText().toString().isEmpty()){
                        name.setError("Name Required");
                        return;
                    }
                    if(email.getText().toString().isEmpty()){
                        email.setError("Email Required !");
                        return;
                    }
                    if(phoneno.getText().toString().isEmpty()){
                        phoneno.setError("Phone Number Required !");
                        return;
                    }

                    if(!(isValidEmailId(email.getText().toString().trim()))) {
                        Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!(isValidMobile(phoneno.getText().toString().trim()))){
                        Toast.makeText(signup.this, "Invalid Mobile Number !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (img.getDrawable() == null){
                        //NO Image
                        Toast.makeText(signup.this, "Upload Image !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    /*if(rg.getCheckedRadioButtonId()==-1)
                    {
                        Toast.makeText(getApplicationContext(), "Please select Role !!", Toast.LENGTH_SHORT).show();
                        return;
                    }*/

                    if(spinnerrole.getSelectedItemPosition() == 0){
                        ((TextView)spinnerrole.getSelectedView()).setError("Error message");
                        //Toast.makeText(login.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    USER = spinnerrole.getSelectedItem().toString();

                    //Toast.makeText(signup.this,USER, Toast.LENGTH_SHORT).show();

                    StringRequest sr = new StringRequest(POST, UPLOAD_URL_1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject jobj = new JSONObject(response);
                                        String res = jobj.getString("response");
                                        //Toast.makeText(signup.this, res, Toast.LENGTH_SHORT).show();

                                        if(res.contains("no")){

                                            loading= new LoadingDialog(signup.this);
                                            loading.startLoadingDialog();
                                            verifyPhoneNumber(uphonenum);

                                        }
                                        else if(res.contains("yes")){
                                            Toast.makeText(signup.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                        }

                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        loading.dismissDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismissDialog();
                            Toast.makeText(signup.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();

                            //String path = getPath(filepath);
                            //map.put("namekey",name.getText().toString().trim());
                            map.put("mobilekey","+91"+phoneno.getText().toString());
                            //map.put("emailkey",email.getText().toString().trim());
                            //map.put("image",encodeBitmapImage(bitmap));
                            return  map;
                        }
                    };
                    RequestQueue rq = Volley.newRequestQueue(signup.this);
                    rq.add(sr);





                    uname = name.getText().toString().trim();
                    uemail = email.getText().toString().trim();
                    uphonenum = "+91"+phoneno.getText().toString();
                    //Toast.makeText(signup.this,uname+uemail+uphonenum, Toast.LENGTH_SHORT).show();

                    //



                }


            }
        });

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get OTP
                if (gotp1.getText().toString().trim().isEmpty()
                        || gotp2.getText().toString().trim().isEmpty()
                        || gotp3.getText().toString().trim().isEmpty()
                        || gotp4.getText().toString().trim().isEmpty()
                        || gotp5.getText().toString().trim().isEmpty()
                        || gotp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(signup.this, "Please Enter OTP No.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String enteredotpcode = gotp1.getText().toString().trim()+
                        gotp2.getText().toString().trim()+
                        gotp3.getText().toString().trim()+
                        gotp4.getText().toString().trim()+
                        gotp5.getText().toString().trim()+
                        gotp6.getText().toString().trim();

                PhoneAuthCredential crediential = PhoneAuthProvider.getCredential(verificationId,enteredotpcode);
                authenicateUser(crediential);
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(signup.this, "nnjjbjhbjbj", Toast.LENGTH_SHORT).show();
                //authenicateUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken ;

                getotp.setVisibility(View.GONE);

                otpcodell.setVisibility(View.VISIBLE);
                //otpcode.setVisibility(View.VISIBLE);
                verifyotp.setVisibility(View.VISIBLE);
                resendotp.setVisibility(View.VISIBLE);
                resendotp.setEnabled(false);
                loading.dismissDialog();

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendotp.setEnabled(true);
            }
        };



    }

    /*public void  rbCLick1(View v)
    {
        int radiobtnID = rg.getCheckedRadioButtonId();
        rb = findViewById(radiobtnID);
        //Toast.makeText(this, rb.getText(), Toast.LENGTH_SHORT).show();

    }*/

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK )
        {
            Uri filepath=data.getData();

            try
            {
                actualImage = FileUtil.from(this, data.getData());
                String len = getReadableFileSize(actualImage.length());
                Log.i("imagesize",len );
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
                img.setVisibility(View.VISIBLE);


            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private String encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        return (Base64.encodeToString(bytesofimage, Base64.DEFAULT));
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                img.setVisibility(View.VISIBLE);
                img.setImageBitmap(bitmap);


                // Toast.makeText(getApplicationContext(),getPath(filepath),Toast.LENGTH_LONG).show();
            } catch (Exception ex) {

            }
        }

    }*/



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
            return  phone.length() == 10;
        }
        return false;
    }

    public void verifyPhoneNumber(String phoneNum) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }




    public void authenicateUser(PhoneAuthCredential credential){
        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Toast.makeText(signup.this, uname+uemail+uphonenum, Toast.LENGTH_SHORT).show();
                //System.out.println(uname);
                Toast.makeText(signup.this, "Success", Toast.LENGTH_SHORT).show();
                //uploadImage();
                //Toast.makeText(signup.this, encodedImage, Toast.LENGTH_SHORT).show();
                //String url = "http://vivekdabhi.atwebpages.com/register.php";
                StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject jobj = new JSONObject(response);
                                    String res = jobj.getString("response");

                                    Toast.makeText(signup.this, res, Toast.LENGTH_SHORT).show();

                                    //Toast.makeText(signup.this, res, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),signup2.class));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(signup.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("namekey",name.getText().toString().trim());
                        map.put("mobilekey","+91"+phoneno.getText().toString().trim());
                        map.put("emailkey",email.getText().toString().trim());
                        map.put("image",encodeBitmapImage(bitmap));
                        map.put("role",USER);
                        return  map;
                    }
                };
                sr.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 500000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 500000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });

                //RequestQueue rq = Volley.newRequestQueue(signup.this);
                //rq.add(sr);


                Intent i= new Intent(signup.this,signup2.class);
                i.putExtra("namekey",name.getText().toString().trim());
                i.putExtra("phoneno","+91"+phoneno.getText().toString());
                i.putExtra("emailkey",email.getText().toString().trim());
                i.putExtra("image",encodeBitmapImage(bitmap));
                i.putExtra("role",USER);
                startActivity(i);

                //startActivity(new Intent(getApplicationContext(),VerifyOTPActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}