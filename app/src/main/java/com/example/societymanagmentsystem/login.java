package com.example.societymanagmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static java.security.AccessController.getContext;

public class login extends AppCompatActivity {

    public String USER;
    Spinner spinnerrole;
    ArrayAdapter<CharSequence> roleAdapter;
    //ArrayList<String> rolelist= new ArrayList<>();
    RadioGroup rg;
    RadioButton rb;
    LinearLayout signupll,otpll;
    EditText phonenumber,otpcode;
    Button sendOtp,verifyOtp,resendotp;
    ImageView otp1,otp2;
    TextView text1,text2,mob1,mob2,cCode;
    String userPhoneNumber,verificationId,userEmail,userName,userSociety,userBlock,userCity;
    PhoneAuthProvider.ForceResendingToken token;
    //private static final String UPLOAD_URL=  "https://adrenal-pairs.000webhostapp.com/login/varify_loginuser.php";
    private static final String UPLOAD_URL=  "https://vivek.ninja/login/varify_loginuser.php";
   //private static final String UPLOAD_URL=  "https://vivek.ninja/connect.php";
    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks,callbacks1;
    public static  final String fileName="USER_PROFILE";
    public LoadingDialog loading;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    TextView signup;
    TextView tempImg;
    EditText gotp1, gotp2, gotp3, gotp4,gotp5,gotp6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rg = findViewById(R.id.radioGroup);

        spinnerrole = findViewById(R.id.rolelogin);

        //first Image
        otp1 = findViewById(R.id.otp1);
        //second image
        otp2 = findViewById(R.id.otp2);
        //We will send you an One Time Password on this mobile number
        text1 = findViewById(R.id.text1);
        //Enter The OTP Send TO
        text2 = findViewById(R.id.text2);
        //Enter Mobile Number
        mob1 = findViewById(R.id.mob1);
        //Otp send on given no..
        mob2 = findViewById(R.id.mob2);

        gotp1 = findViewById(R.id.eotp1);
        gotp2 = findViewById(R.id.eotp2);
        gotp3 = findViewById(R.id.eotp3);
        gotp4 = findViewById(R.id.eotp4);
        gotp5 = findViewById(R.id.eotp5);
        gotp6 = findViewById(R.id.eotp6);

        signupll = findViewById(R.id.signupll);

        //ccode & mob Number
        cCode = findViewById(R.id.cCode);
        phonenumber = findViewById(R.id.inputmobile);
        //Enter OTP CODe
        otpcode = findViewById(R.id.enterotp);

        //Send OTP
        sendOtp = findViewById(R.id.buttongetotp);
        //Verify OTP
        verifyOtp = findViewById(R.id.buttonverifyotp);
        //resent OTP
        resendotp = findViewById(R.id.resendotp);
        otpll = findViewById(R.id.otpnoll);

        signup = findViewById(R.id.txtSignUp);

        tempImg = findViewById(R.id.c1Code);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        /*if(sharedPreferences.contains(isLoggedIn)){
            Intent i = new Intent(login.this,Dashboard.class);
            startActivity(i);
        }*/

        roleAdapter = ArrayAdapter.createFromResource(this, R.array.role, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerrole.setAdapter(roleAdapter);
        //spinnerrole.setOnItemSelectedListener(this);

        requestQueue = Volley.newRequestQueue(this);
        /*
        otp2.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        mob2.setVisibility(View.GONE);
        otpcode.setVisibility(View.GONE);
        verifyOtp.setVisibility(View.GONE);
        resendotp.setVisibility(View.GONE);
        */


        //
        if(isLoggedIn || isSignedIn){
            Intent i = new Intent(login.this,Dashboard.class);
            startActivity(i);
            finish();
        }
        else{
        fAuth = FirebaseAuth.getInstance();


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

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(login.this,signup.class);
                    startActivity(i);

                }
            });


        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                        .setActivity(login.this)
                        .setPhoneNumber(userPhoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(callbacks1)
                        .build();


                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phonenumber.getText().toString().isEmpty()) {
                    phonenumber.setError("Phone Number Required");
                    return;
                }
                if (!isValidMobile(phonenumber.getText().toString().trim())) {
                    Toast.makeText(login.this, "Invalid Mobile Number !", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*if(rg.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select !!", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if (spinnerrole.getSelectedItemPosition() == 0) {
                    ((TextView) spinnerrole.getSelectedView()).setError("Error message");
                    //Toast.makeText(login.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading = new LoadingDialog(login.this);
                Log.e("Start", "1");
                loading.startLoadingDialog();

                USER = spinnerrole.getSelectedItem().toString();
                //Toast.makeText(login.this, USER, Toast.LENGTH_SHORT).show();
                //Toast.makeText(login.this,rb.getText(), Toast.LENGTH_SHORT).show();

                //protected Map<String, String> getParams() throws AuthFailureError {

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
                                        //userEmail= res;
                                        //loading.dismissDialog();
                                        userEmail = js.getString("email");
                                        userName = js.getString("name");
                                        userSociety = js.getString("s_name");
                                        userCity = js.getString("city");
                                        userBlock = js.getString("block");
                                        Log.e("DSD", userName + "-" + userEmail + "-" + userSociety + "-" + userCity + "-" + userBlock);
                                        userPhoneNumber = "+91" + phonenumber.getText().toString();
                                        verifyPhoneNumber(userPhoneNumber);

                                    } else if (res.contains("no")) {
                                        //loading= new LoadingDialog(login.this);
                                        loading.dismissDialog();
                                        new AndExAlertDialog.Builder(login.this)
                                                .setTitle("WARNING !")
                                                .setMessage("Mobile Number Or Email is Already Exists !")
                                                .setPositiveBtnText("Ok")

                                                .setImage(R.drawable.warning, 15)
                                                .setCancelableOnTouchOutside(true)
                                                .OnPositiveClicked(new AndExAlertDialogListener() {
                                                    @Override
                                                    public void OnClick(String input) {
                                                        //Toast.makeText(login.this, "Yes", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .build();


                                        //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
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
                        Log.e("JSON Parser", "Error parsing data " + error.toString());
                        Toast.makeText(login.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        //map.put("namekey",name.getText().toString().trim());
                        map.put("mobilekey", "+91" + phonenumber.getText().toString());
                        map.put("role", spinnerrole.getSelectedItem().toString());
                        //map.put("image",encodeBitmapImage(bitmap));
                        return map;
                    }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                };
                RequestQueue rq = Volley.newRequestQueue(login.this);
                rq.add(sr);

            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get OTP

                /*if (otpcode.getText().toString().isEmpty()) {
                    otpcode.setError("Enter OTP First");
                    return;
                }*/
                if (gotp1.getText().toString().trim().isEmpty()
                        || gotp2.getText().toString().trim().isEmpty()
                        || gotp3.getText().toString().trim().isEmpty()
                        || gotp4.getText().toString().trim().isEmpty()
                        || gotp5.getText().toString().trim().isEmpty()
                        || gotp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(login.this, "Please Enter OTP No.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String enteredotpcode = gotp1.getText().toString().trim()+
                        gotp2.getText().toString().trim()+
                        gotp3.getText().toString().trim()+
                        gotp4.getText().toString().trim()+
                        gotp5.getText().toString().trim()+
                        gotp6.getText().toString().trim();



                loading = new LoadingDialog(login.this);
                Log.e("start", "2");
                loading.startLoadingDialog();
                PhoneAuthCredential crediential = PhoneAuthProvider.getCredential(verificationId, enteredotpcode);
                authenicateUser(crediential);
            }
        });

        callbacks1 = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(login.this, "Hii", Toast.LENGTH_SHORT).show();
                //authenicateUser(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                //Toast.makeText(login.this, "Hii-1", Toast.LENGTH_SHORT).show();
                //Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //Toast.makeText(login.this, "Hii", Toast.LENGTH_SHORT).show();
                //super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                Toast.makeText(login.this, "OTP Send", Toast.LENGTH_SHORT).show();
                //token = forceResendingToken ;

                /*otp1.setVisibility(View.GONE);
                text1.setVisibility(View.GONE);
                mob1.setVisibility(View.GONE);
                cCode.setVisibility(View.GONE);
                phonenumber.setVisibility(View.GONE);
                sendOtp.setVisibility(View.GONE);
                spinnerrole.setVisibility(View.GONE);

                otp2.setVisibility(View.VISIBLE);
                rg.setVisibility(View.GONE);
                text2.setVisibility(View.VISIBLE);
                mob2.setVisibility(View.VISIBLE);
                mob2.setText(userPhoneNumber.toString());
                otpcode.setVisibility(View.VISIBLE);
                verifyOtp.setVisibility(View.VISIBLE);
                resendotp.setVisibility(View.VISIBLE);

                resendotp.setEnabled(false);
                loading.dismissDialog();*/


            }

        };

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(login.this, "Hii", Toast.LENGTH_SHORT).show();
                //authenicateUser(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                //Toast.makeText(login.this, "Hii-1", Toast.LENGTH_SHORT).show();
                Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //Toast.makeText(login.this, "Hii", Toast.LENGTH_SHORT).show();
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                loading.dismissDialog();
                token = forceResendingToken;

               tempImg.setVisibility(View.GONE);
               signupll.setVisibility(View.GONE);

                otp1.setVisibility(View.GONE);
                text1.setVisibility(View.GONE);
                mob1.setVisibility(View.GONE);
                cCode.setVisibility(View.GONE);
                phonenumber.setVisibility(View.GONE);
                sendOtp.setVisibility(View.GONE);
                spinnerrole.setVisibility(View.GONE);
                //resendotp.setEnabled(false);

                otp2.setVisibility(View.VISIBLE);
                rg.setVisibility(View.GONE);
                text2.setVisibility(View.VISIBLE);
                mob2.setVisibility(View.VISIBLE);
                mob2.setText(userPhoneNumber.toString());
                otpll.setVisibility(View.VISIBLE);
                //otpcode.setVisibility(View.VISIBLE);
                verifyOtp.setVisibility(View.VISIBLE);

                resendotp.setVisibility(View.VISIBLE);
                //resendotp.setEnabled(false);


            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                //Toast.makeText(login.this, "Hii", Toast.LENGTH_SHORT).show();
                super.onCodeAutoRetrievalTimeOut(s);
                //resendotp.setEnabled(true);
            }
        };

    }
    }

    /*public void  rbCLick(View v)
    {
        int radiobtnID = rg.getCheckedRadioButtonId();
        rb = findViewById(radiobtnID);
        //Toast.makeText(this, rb.getText(), Toast.LENGTH_SHORT).show();

    }*/

    public void verifyPhoneNumber(String phoneNum){

        //Send OTP

        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks
                );*/


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNum)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();


        PhoneAuthProvider.verifyPhoneNumber(options);


    }
    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 10;
        }
        return false;
    }
    public void authenicateUser(PhoneAuthCredential credential){

        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email",userEmail);
                editor.putString("name",userName);
                editor.putString("society",userSociety);
                editor.putString("city",userCity);
                editor.putString("block",userBlock);
                editor.putString("role",spinnerrole.getSelectedItem().toString());
                editor.putBoolean("isLoggedIn",true);
                editor.putString("phoneno","+91"+phonenumber.getText().toString());
                editor.putBoolean("isSignedIn",false);
                editor.commit();
                Toast.makeText(login.this, "Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(login.this,Dashboard.class);
                startActivity(i);
                finish();
                loading.dismissDialog();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismissDialog();
                Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}