package com.example.societymanagmentsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class signup2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnercity,spinnersociety,spinnerblock;
    ArrayList<String> citylist= new ArrayList<>();
    ArrayList<String> blocklist= new ArrayList<>();
    ArrayList<String> societylist= new ArrayList<>();
    ArrayAdapter<String> cityAdapter ,societyAdapter,blockAdapter;
    private JSONArray result;
    Button submit;

    TextView tv3,tv4;
    public  LoadingDialog loading;
    public String name,email,phoneno,image,fwdRole;
    RadioGroup rg;
    SharedPreferences sharedPreferences;
    RadioButton rb;

    //public static String DATA_URL = "https://adrenal-pairs.000webhostapp.com/signup/city_fetch.php";
    public static String DATA_URL = "https://vivek.ninja/signup/city_fetch.php";
    //public static String TEMP_URL = "https://adrenal-pairs.000webhostapp.com/signup/signupfinal.php";
    public static String TEMP_URL = "https://vivek.ninja/signup/signupfinal.php";
    public static String UPLOAD_URL = "https://adrenal-pairs.000webhostapp.com/signup/image2.php";
    RequestQueue requestQueue;

    public static  final String fileName="USER_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        Intent intent = getIntent();

        tv3=findViewById(R.id.textView3);
        tv4=findViewById(R.id.textView4);
        name = intent.getStringExtra("namekey");
        email = intent.getStringExtra("emailkey");
        phoneno = intent.getStringExtra("phoneno");
        image =intent.getStringExtra("image");

        //Bitmap temp =
        fwdRole = intent.getStringExtra("role");
        Toast.makeText(this, fwdRole, Toast.LENGTH_SHORT).show();

        //String lName = intent.getStringExtra("lastName");
        Toast.makeText(this,phoneno, Toast.LENGTH_SHORT).show();
        spinnercity = (Spinner) findViewById(R.id.spinnercity);
        spinnersociety = (Spinner)findViewById(R.id.spinnersociety);
        spinnerblock = (Spinner)findViewById(R.id.spinnerblock);
        requestQueue = Volley.newRequestQueue(this);
        submit = (Button)findViewById(R.id.buttonsubmit);
        rg = findViewById(R.id.radioGroup);

        //spinnercity.setEnabled(false);
        spinnersociety.setEnabled(false);
        spinnerblock.setEnabled(false);



        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        /*if(sharedPreferences.contains(isSignedIn)){
            Intent i = new Intent(signup2.this,Dashboard.class);
            startActivity(i);
        }*/
        fill();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(spinnercity.getSelectedItemPosition() ==0 ||
                        spinnersociety.getSelectedItemPosition() ==0 ||
                        spinnerblock.getSelectedItemPosition() == 0 ||
                        rg.getCheckedRadioButtonId() == -1
                ){
                    Toast.makeText(signup2.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(fwdRole.contains("Security")){
                    if(spinnercity.getSelectedItemPosition() ==0 || spinnersociety.getSelectedItemPosition() ==0){
                        Toast.makeText(signup2.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(!fwdRole.contains("Security")){
                    if(spinnercity.getSelectedItemPosition() ==0 || spinnersociety.getSelectedItemPosition() ==0 || spinnerblock.getSelectedItemPosition() == 0 || rg.getCheckedRadioButtonId() == -1){
                        Toast.makeText(signup2.this, "Please Fill Details First !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                loading= new LoadingDialog(signup2.this);
                loading.startLoadingDialog();
                StringRequest sr = new StringRequest(POST, TEMP_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject jobj = new JSONObject(response);
                                    String res = jobj.getString("response");

                                    if(res.contains("Yes")){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email",email);
                                        editor.putString("role",fwdRole);
                                        editor.putString("name",name);

                                        editor.putString("society",spinnersociety.getSelectedItem().toString());
                                        editor.putString("block",spinnerblock.getSelectedItem().toString());
                                        editor.putString("city",spinnercity.getSelectedItem().toString());
                                        editor.putBoolean("isSignedIn",true);
                                        editor.putBoolean("isLoggedIn",false);
                                        editor.putString("phoneno",phoneno);
                                        editor.commit();
                                        //Toast.makeText(signup2.this, "Successful", Toast.LENGTH_SHORT).show();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(signup2.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(signup2.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Registeration Done !");
                                        reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
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

                                        Intent i = new Intent(signup2.this,Dashboard.class);
                                        startActivity(i);
                                        finish();
                                        loading.dismissDialog();
                                    }
                                    else if(res.contains("No")){
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(signup2.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(signup2.this);
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
                                            }
                                        });

                                        dialogi.getWindow().setBackgroundDrawable(
                                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialogi.setCanceledOnTouchOutside(false);
                                        dialogi.show();
                                        Toast.makeText(signup2.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                    }

                                    //Toast.makeText(signup2.this, res, Toast.LENGTH_SHORT).show();

                                    //Toast.makeText(signup.this, res, Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(getApplicationContext(),signup2.class));
                                    //finish();
                                } catch (JSONException e) {
                                    loading.dismissDialog();
                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(signup2.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(signup2.this);
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
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismissDialog();
                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(signup2.this);
                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(signup2.this);
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
                        Toast.makeText(signup2.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();

                        //String path = getPath(filepath);
                        map.put("namekey",name);
                        map.put("mobilekey",phoneno);
                        map.put("emailkey",email);
                        map.put("image",image);
                        map.put("role",fwdRole);
                        //map.put("phonekey",phoneno);
                        map.put("citykey",spinnercity.getSelectedItem().toString());
                        map.put("societykey",spinnersociety.getSelectedItem().toString());
                        if(fwdRole.contains("Security")){
                            map.put("blockkey", "--");
                            map.put("ownership","--");
                        }
                        else{
                            map.put("blockkey",spinnerblock.getSelectedItem().toString());
                            map.put("ownership",rb.getText().toString());
                        }

                        //map.put("blockkey",spinnerblock.getSelectedItem().toString());
                        //map.put("ownership",rb.getText().toString());
                        return  map;
                    }
                };

                RequestQueue rq = Volley.newRequestQueue(signup2.this);
                rq.add(sr);
            }
        });
/*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                DATA_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                spinnercity.setEnabled(true);
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String countryName = jsonObject.optString("city");
                        citylist.add(countryName);
                        cityAdapter = new ArrayAdapter<>(signup2.this,android.R.layout.simple_spinner_item,citylist);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnercity.setAdapter(cityAdapter);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
        spinnercity.setOnItemSelectedListener(this); */

    }

    public void fill(){
        if(!fwdRole.contains("Security")){
            //spinnersociety.setOnItemSelectedListener(this);
        }
        else if (fwdRole.contains("Security")){
            spinnerblock.setVisibility(View.GONE);
            rg.setVisibility(View.GONE);
            tv4.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
            //rb.setVisibility(View.GONE);

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST,
            DATA_URL, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            spinnercity.setEnabled(true);

            try {
                JSONArray jsonArray = response.getJSONArray("result");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String countryName = jsonObject.optString("city");
                    citylist.add(countryName);
                    cityAdapter = new ArrayAdapter<>(signup2.this,android.R.layout.simple_spinner_item,citylist);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnercity.setAdapter(cityAdapter);
                }
                citylist.add(0,"Choose City");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });

        requestQueue.add(jsonObjectRequest);
        spinnercity.setOnItemSelectedListener(this);
}

    public void  rbCLick(View v)
    {
        int radiobtnID = rg.getCheckedRadioButtonId();
        rb = findViewById(radiobtnID);
        Toast.makeText(this, rb.getText(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinnercity) {
            if (spinnercity.getSelectedItemPosition() != 0) {

                societylist.clear();


                String selectedCity = adapterView.getSelectedItem().toString();
                String DATA_URL1 = "https://vivek.ninja/signup/society_fetch.php?city=" + selectedCity;
                requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST,
                        DATA_URL1, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spinnersociety.setEnabled(true);
                        try {
                            JSONArray jsonArray = response.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String societyName = jsonObject.optString("s_name");
                                //Log.e("Society",societyName);
                                societylist.add(societyName);
                                societyAdapter = new ArrayAdapter<>(signup2.this, android.R.layout.simple_spinner_item, societylist);
                                societyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnersociety.setAdapter(societyAdapter);
                            }
                            societylist.add(0, "Choose Society Name ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(jsonObjectRequest);

                if(!fwdRole.contains("Security")){
                    spinnersociety.setOnItemSelectedListener(this);
                }
                else if (fwdRole.contains("Security")){
                    spinnerblock.setVisibility(View.GONE);
                    rg.setVisibility(View.GONE);
                    tv4.setVisibility(View.GONE);
                    tv3.setVisibility(View.GONE);
                    //rb.setVisibility(View.GONE);

                }




            }
            else {
                ((TextView)spinnercity.getSelectedView()).setError("Error message");
                //Toast.makeText(this, "Please Select Valid City !", Toast.LENGTH_SHORT).show();
            }
        }

        if (adapterView.getId() == R.id.spinnersociety) {
            if (spinnersociety.getSelectedItemPosition() != 0) {
            blocklist.clear();
            spinnerblock.setEnabled(true);
            String selectedCity = spinnercity.getSelectedItem().toString();
            String selectedsociety = spinnersociety.getSelectedItem().toString();
            String DATA_URL2 = "https://vivek.ninja/signup/block_fetch.php?city=" + selectedCity + "&s_name=" + selectedsociety;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST,
                    DATA_URL2, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");
                        JSONObject jobj = new JSONObject(String.valueOf(response));
                        String res = jobj.getString("result");
                        if (res.contains("NO")) {
                            Toast.makeText(signup2.this, "Sorry", Toast.LENGTH_SHORT).show();
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String blockName = jsonObject.optString("block");
                                Log.e("Society", blockName);
                                blocklist.add(blockName);
                                blockAdapter = new ArrayAdapter<>(signup2.this, android.R.layout.simple_spinner_item, blocklist);
                                blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerblock.setAdapter(blockAdapter);
                            }
                            blocklist.add(0,"Choose Block/Wing No.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
            spinnerblock.setOnItemSelectedListener(this);


            //Toast.makeText(signup2.this,rb.getText(), Toast.LENGTH_SHORT).show();

        }
            else {
                ((TextView)spinnersociety.getSelectedView()).setError("Error message");

            }
    }

        if (adapterView.getId() == R.id.spinnerblock){
            if (spinnerblock.getSelectedItemPosition() == 0) {
                ((TextView)spinnerblock.getSelectedView()).setError("Error message");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

