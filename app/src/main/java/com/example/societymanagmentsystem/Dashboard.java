package com.example.societymanagmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //private final WeakReference<login> loginActivityWeakRef;

    SharedPreferences sharedPreferences;
    //Button Logout;
    public LoadingDialog loading;
    public static final String fileName = "USER_PROFILE";
    public static final String fileName1 = "issignedin";
    public static final String isLoggedIn = "true";
    String phoneno,role ,name ,email, society, block ,city;

    //private static final String UPLOAD_URL = "http://adrenal-pairs.000webhostapp.com/Admin/IsAccRej.php";
    private static final String UPLOAD_URL = "https://vivek.ninja/Admin/IsAccRej.php";
    private static final String UPLOAD_URL1=  "https://vivek.ninja/childexit/add_child_exit.php";
    private static final String UPLOAD_URL2=  "https://vivek.ninja/SecMsg/add_secmsg.php";
    public static boolean account = false;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    int btn_id = -1;
    CardView membercd1, membercd2, membercd3, membercd4, membercd5, membercd6,membercd7;
    CardView admncd1, admncd2, admncd3,admncd33, admncd4, admncd5, admncd6, admncd7, admncd8, admncd9,admncd10;
    CardView seccd1, seccd2, seccd3, seccd4, seccd5, seccd6;
    TextView textView;
    ImageSlider imageSlider;
    ScrollView scv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);


        //Logout = findViewById(R.id.logout);
        //BInding ELements
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);
        imageSlider = findViewById(R.id.slider);
        scv = findViewById(R.id.VSV1);


        //NavBar Binding
        View hView = navigationView.inflateHeaderView(R.layout.header);
        ImageView imgvw = (ImageView) hView.findViewById(R.id.imageViewnav);
        imgvw.setImageResource(R.drawable.small_society_icon);
        TextView tv = (TextView) hView.findViewById(R.id.id1);
        TextView tv1 = (TextView) hView.findViewById(R.id.id2);
        //tabLayout = findViewById(R.id.cabtabLayout);
        //viewPager = findViewById(R.id.cabviewPager);

        //SharePreferences
        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
         phoneno = sharedPreferences.getString("phoneno", "");
         role = sharedPreferences.getString("role", "");
         name = sharedPreferences.getString("name", "");
         email = sharedPreferences.getString("email", "");

         society = sharedPreferences.getString("society", "");
         block = sharedPreferences.getString("block", "");
         city = sharedPreferences.getString("city", "");

         Log.e("city","c-"+city);

        //Log.e("ANMDNMs",society+"-"+block+"-"+city);

        loading= new LoadingDialog(Dashboard.this);

        TextView tvNavTitle;

        tv.setText(name);
        tv1.setText(email);

        /*View hView =  navigationView.inflateHeaderView(R.layout.header);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageViewnav);
        imgvw .setImageResource(R.drawable.small_society_icon);
        TextView tv = (TextView)hView.findViewById(R.id.id1);
        TextView tv1 = (TextView)hView.findViewById(R.id.id2);
        tv.setText("EEE");
        tv.setText("sdsd");
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu_admin);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Dashboard.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.admin_dashboard_content, null);
        //cd1 = mView.findViewById(R.id.cdv1);
        //cd2 =mView.findViewById(R.id.cdv2);
        scv.addView(mView);


        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://vivek.ninja/images/603dd8d67d936.jpg",null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/603dd93aea93e.jpg",null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/3.jpg",null));
        slideModels.add(new SlideModel("https://vivek.ninja/images/4.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://vivek.ninja/images/5.jpg",ScaleTypes.FIT));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/6.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://vivek.ninja/images/7.jpg",null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/IMG689830285.jpg","Image-4",null));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        setSupportActionBar(toolbar);

        /* TO Hide SOme Buttons
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        */

        /*navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);*/

        LoadImageSlider();
        LoadNavBar(role);


        //Fill Activity Based On User Type Here




        if (isLoggedIn) {

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, phoneno, Toast.LENGTH_SHORT).show();
            /*Logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent i = new Intent(Dashboard.this,mainpage.class);
                    startActivity(i);
                    finish();
                    //Toast.makeText(Dashboard.this, "", Toast.LENGTH_SHORT).show();
                }
            });*/

        } else if (isSignedIn) {

            //Toast.makeText(this, "temp", Toast.LENGTH_SHORT).show();
            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
            View mViewi = layoutInflaterAndroidi.inflate(R.layout.wait_user, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
            alertDialogBuilderUserInput.setView(mViewi);
            alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return i == KeyEvent.KEYCODE_BACK;
                }
            });
            alertDialogBuilderUserInput.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
            final AlertDialog dialog = alertDialogBuilderUserInput.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jobj = new JSONObject(response);
                                String res = jobj.getString("response");

                                //Toast.makeText(Dashboard.this, res, Toast.LENGTH_SHORT).show();

                                if (res.contains("accepted")) {
                                    dialog.dismiss();

                                    //Toast.makeText(Dashboard.this, "Accepted!", Toast.LENGTH_SHORT).show();
                                } else if (res.contains("yes")) {

                                } else if (res.contains("no")) {

                                    dialog.dismiss();

                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_wait_cancel, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                    alertDialogBuilderUserInput.setView(mViewi);
                                    alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                            return i == KeyEvent.KEYCODE_BACK;
                                        }
                                    });
                                    alertDialogBuilderUserInput.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.clear();
                                                    editor.apply();

                                                    finish();
                                                    Intent i = new Intent(Dashboard.this, login.class);
                                                    startActivity(i);
                                                    finish();
                                                    //System.exit(0);
                                                }
                                            });
                                    final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();

                                    Toast.makeText(Dashboard.this, "Sorry", Toast.LENGTH_SHORT).show();
                                } else {
                                    //loading= new LoadingDialog(login.this);

                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_wait_reason, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                    alertDialogBuilderUserInput.setView(mViewi);
                                    TextView reason = (TextView) mViewi.findViewById(R.id.dialogReasn1);
                                    alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                            return i == KeyEvent.KEYCODE_BACK;
                                        }
                                    });

                                    alertDialogBuilderUserInput.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                    System.exit(0);
                                                }
                                            });
                                    reason.setText("Reasn : " + res);
                                    final AlertDialog dialog = alertDialogBuilderUserInput.create();
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();

                                    //Toast.makeText(Dashboard.this, res, Toast.LENGTH_SHORT).show();

                                    //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                }

                                //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();

                    //String path = getPath(filepath);
                    //map.put("namekey",name.getText().toString().trim());
                    map.put("mobilekey", phoneno);
                    //map.put("role",rb.getText().toString());
                    //map.put("image",encodeBitmapImage(bitmap));
                    return map;
                }
            };
            RequestQueue rq = Volley.newRequestQueue(Dashboard.this);
            rq.add(sr);

            //


            /*Logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.clear();
                    editor.apply();
                    Intent i = new Intent(Dashboard.this,mainpage.class);
                    startActivity(i);
                    finish();
                    //Toast.makeText(Dashboard.this, "", Toast.LENGTH_SHORT).show();
                }
            });*/
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Dashboard.this, login.class);
            startActivity(i);
            finish();
        }




        /*if(account) {
            setContentView(R.layout.dashboard_main);
        }
        else{
            Intent i = new Intent(Dashboard.this,mainpage.class);
            startActivity(i);
            //setContentView(R.layout.activity_mainpage);
        }*/
    }

    private void LoadImageSlider() {
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://vivek.ninja/images/603dd8d67d936.jpg", null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/603dd93aea93e.jpg",null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/3.jpg",null));
        slideModels.add(new SlideModel("https://vivek.ninja/images/4.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://vivek.ninja/images/5.jpg", ScaleTypes.FIT));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/6.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://vivek.ninja/images/7.jpg", null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/IMG689830285.jpg","Image-4",null));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void LoadNavBar(String Role) {
        int inflateMenu = -111, dashboardContent = -111;

        switch (Role) {
            case "Admin":
                inflateMenu = R.menu.main_menu_admin;
                dashboardContent = R.layout.admin_dashboard_content;
                //loadAdminCardView(dashboardContent);
                break;
            case "Society Member":
                inflateMenu = R.menu.main_menu_member;
                dashboardContent = R.layout.member_dashboard_content;
                //loadMemberCardView(dashboardContent);
                Log.e("Status", "in Switch case");
                break;
            case "Security":
                inflateMenu = R.menu.main_menu_security;
                dashboardContent = R.layout.security_dashboard_content;
                //loadSecurityCardView(dashboardContent);
                break;
        }

        navigationView.getMenu().clear();
        navigationView.inflateMenu(inflateMenu);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Dashboard.this);
        View mView = layoutInflaterAndroid.inflate(dashboardContent, null);
        loadUserCardView(mView, Role);
        //cd1 = mView.findViewById(R.id.membercdv1);
        //cd2 =mView.findViewById(R.id.membercdv2);
        scv.addView(mView);
        setSupportActionBar(toolbar);

        /* TO Hide SOme Buttons
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        */

        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadUserCardView(View mView, String Role) {
        Log.e("Status", "in member Func");
        //LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Dashboard.this);
        //View mView = layoutInflaterAndroid.inflate(dashboardContent, null);
        switch (Role) {
            case "Admin":
                admncd1 = mView.findViewById(R.id.admncdv1);
                admncd2 = mView.findViewById(R.id.admncdv2);
                admncd3 = mView.findViewById(R.id.admncdv3);
                admncd33 = mView.findViewById(R.id.admncdv33);
                admncd4 = mView.findViewById(R.id.admncdv4);
                admncd5 = mView.findViewById(R.id.admncdv5);
                admncd6 = mView.findViewById(R.id.admncdv6);
                admncd7 = mView.findViewById(R.id.admncdv7);
                admncd8 = mView.findViewById(R.id.admncdv8);
                admncd9 = mView.findViewById(R.id.admncdv9);
                admncd10 = mView.findViewById(R.id.admncdv10);
                admncd1.setOnClickListener(this);
                admncd2.setOnClickListener(this);
                admncd3.setOnClickListener(this);
                admncd33.setOnClickListener(this);
                admncd4.setOnClickListener(this);
                admncd5.setOnClickListener(this);
                admncd6.setOnClickListener(this);
                admncd7.setOnClickListener(this);
                admncd8.setOnClickListener(this);
                admncd9.setOnClickListener(this);
                admncd10.setOnClickListener(this);
                break;

            case "Society Member":
                membercd1 = mView.findViewById(R.id.membercdv1);
                membercd2 = mView.findViewById(R.id.membercdv2);
                membercd3 = mView.findViewById(R.id.membercdv3);
                membercd4 = mView.findViewById(R.id.membercdv4);
                membercd5 = mView.findViewById(R.id.membercdv5);
                membercd6 = mView.findViewById(R.id.membercdv6);
                membercd7 = mView.findViewById(R.id.membercdv7);
                membercd1.setOnClickListener(this);
                membercd2.setOnClickListener(this);
                membercd3.setOnClickListener(this);
                membercd4.setOnClickListener(this);
                membercd5.setOnClickListener(this);
                membercd6.setOnClickListener(this);
                membercd7.setOnClickListener(this);
                break;

            case "Security":
                seccd1 = mView.findViewById(R.id.seccdv1);
                seccd2 = mView.findViewById(R.id.seccdv2);
                seccd3 = mView.findViewById(R.id.seccdv3);
                seccd4 = mView.findViewById(R.id.seccdv4);
                seccd5 = mView.findViewById(R.id.seccdv5);
                seccd6 = mView.findViewById(R.id.seccdv6);
                seccd1.setOnClickListener(this);
                seccd2.setOnClickListener(this);
                seccd3.setOnClickListener(this);
                seccd4.setOnClickListener(this);
                seccd5.setOnClickListener(this);
                seccd6.setOnClickListener(this);
                break;


        }


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Log.e("Status", "in On CLik");
        switch (view.getId()) {
            case R.id.membercdv1:
                Intent ms = new Intent(this,AllowCab.class);
                startActivity(ms);
                //Toast.makeText(this, "Cab", Toast.LENGTH_SHORT).show();
                break;

            case R.id.membercdv2:
                //Toast.makeText(this, "Guest", Toast.LENGTH_SHORT).show();
                break;
            case R.id.membercdv3:
                Intent m42 = new Intent(this,AllowDelievery.class);
                startActivity(m42);
                //Toast.makeText(this, "Delievery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.membercdv4:

                ///
                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_allow_child_exit, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                alertDialogBuilderUserInput.setView(mViewi);
                //
                final EditText parentName = mViewi.findViewById(R.id.ALEParentName);
                final CardView submit = mViewi.findViewById(R.id.ACESubmit);
                final Button selTime = mViewi.findViewById(R.id.ACESelTimeBtn);
                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                selTime.setText("0 Hours");

                selTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //if (loginActivityWeakRef.get() != null && !loginActivityWeakRef.get().isFinishing()) {
                        //Activity activity = (Activity)getApplicationContext();
                        //if (loginActivityWeakRef.get() != null && !loginActivityWeakRef.get().isFinishing()) {

                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                    Dashboard.this,
                                    R.style.BottomThemeDialogTheme
                            );
                /*View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.fragment_valid_for,
                                (CoordinatorLayout) view.findViewById(R.id.ll2));*/
                            View bottomSHeet = LayoutInflater.from(Dashboard.this)
                                    .inflate(R.layout.fragment_valid_for,
                                            (LinearLayout) view.findViewById(R.id.ll2));

                            Button bt1 = bottomSHeet.findViewById(R.id.valid1hr);

                            Button bt2 = bottomSHeet.findViewById(R.id.valid2hr);
                            Button bt4 = bottomSHeet.findViewById(R.id.valid4hr);
                            Button bt8 = bottomSHeet.findViewById(R.id.valid8hr);
                            Button bt12 = bottomSHeet.findViewById(R.id.valid12hr);
                            Button bt24 = bottomSHeet.findViewById(R.id.valid24hr);
                        bt1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 1;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt1.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTime.setText(btn_id+" Hour");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();
                                //bt2.setBackgroundColor(Color.RED);
                                //bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 2;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt2.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt2.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTime.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();
                                //bt2.setBackgroundColor(Color.RED);
                                //bt2.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 4;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt4.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt4.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTime.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();
                                //bt2.setBackgroundColor(Color.RED);
                                //bt4.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 8;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt8.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt8.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTime.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();


                                //bt2.setBackgroundColor(Color.RED);
                                // bt8.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 12;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt12.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt12.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTime.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();


                                //bt2.setBackgroundColor(Color.RED);
                                //bt12.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt24.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 24;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt24.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt24.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTime.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();


                                //bt2.setBackgroundColor(Color.RED);
                                //bt24.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                            bottomSheetDialog.setContentView(bottomSHeet);

                            bottomSheetDialog.setCanceledOnTouchOutside(false);
                            bottomSheetDialog.show();
                        //}
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(parentName.getText().toString().isEmpty() || selTime.getText().toString().contains("0 Hours")){
                            Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(selTime.getText().toString().contains("0 Hours")){
                            Toast.makeText(Dashboard.this, "Select Valid Time Period", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(parentName.getText().toString().isEmpty()){
                            parentName.setError("Please Enter Parent Name !");
                            return;
                            //Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        }

                        loading.startLoadingDialog();
                        ///
                        StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                                            JSONObject js = jsonArray.getJSONObject(0);
                                            //Log.e("ResponseFull",response);
                                            String res = js.getString("repon");
                                            if(!res.contains("no")){
                                                //userEmail= res;

                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Request Send Successfully !");
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
                                                //Toast.makeText(Dashboard.this, "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                                new CountDownTimer(1000, 1000) {

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        // TODO Auto-generated method stub
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        // TODO Auto-generated method stub
                                                        Toast.makeText(Dashboard.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        loading.dismissDialog();
                                                        dialog.dismiss();

                                                    }
                                                }.start();

                                            }
                                            else if(res.contains("no")){
                                                //loading= new LoadingDialog(login.this);
                                                loading.dismissDialog();
                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                               // Toast.makeText(Dashboard.this, "NOooooooo", Toast.LENGTH_SHORT).show();


                                                //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                            }

                                            //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                Log.e("JSON Parser", "Error parsing data " + error.toString());
                                Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();

                                map.put("childnamekey",parentName.getText().toString());
                                map.put("timeper",selTime.getText().toString());
                                map.put("gname",name);
                                map.put("mobilekey",phoneno);
                                map.put("emailkey",email);
                                map.put("citykey",city);
                                map.put("societykey",society);
                                map.put("blockkey",block);

                                return  map;
                            }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                        };
                        RequestQueue rq = Volley.newRequestQueue(Dashboard.this);
                        rq.add(sr);
                        ///

                    }
                });


                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                ///

                //Toast.makeText(this, "child", Toast.LENGTH_SHORT).show();
                break;
            case R.id.membercdv5:
                Intent klmi = new Intent(this,Genpdf.class);
                klmi.putExtra("block",block);
                klmi.putExtra("city",city);
                klmi.putExtra("society",society);

                startActivity(klmi);
                Toast.makeText(this, "Gatepass", Toast.LENGTH_SHORT).show();
                break;
            case R.id.membercdv6:

                ///

                LayoutInflater layoutInflaterMSG = LayoutInflater.from(Dashboard.this);
                View ViewMSG = layoutInflaterMSG.inflate(R.layout.fragment_msg_to_guard, null);
                AlertDialog.Builder MSGalertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                MSGalertDialogBuilderUserInput.setView(ViewMSG);
                //
                final EditText MSG = ViewMSG.findViewById(R.id.MTGmsg);
                final CardView MsgSubmit = ViewMSG.findViewById(R.id.MTGSubmit);
                //final Button selTime = mViewi.findViewById(R.id.ACESelTimeBtn);
                final AlertDialog dialogi = MSGalertDialogBuilderUserInput.create();

                //selTime.setText("0 Hours");

                MsgSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(MSG.getText().toString().isEmpty()){
                            MSG.setError("Please Enter Message  !");
                            return;
                            //Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        }
                        if(MSG.getText().toString().length()>=250){
                            MSG.setError("Message Length must be Max 250 Charecter !");
                            return;
                            //Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        }
                        //.makeText(Dashboard.this, MSG.getText(), Toast.LENGTH_SHORT).show();
                        loading.startLoadingDialog();
                        ///
                        StringRequest sr = new StringRequest(POST, UPLOAD_URL2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                                            JSONObject js = jsonArray.getJSONObject(0);
                                            //Log.e("ResponseFull",response);
                                            String res = js.getString("repon");
                                            if(!res.contains("no")){
                                                //userEmail= res;

                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Message Send Successfully");
                                                reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                                reasonBtn.setText("OK");
                                                reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                /*alertDialogBuilderUserInput.setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                //finish();
                                                                //System.exit(0);
                                                            }
                                                        });*/
                                                //reason.setText("Reasn : " + res);
                                                dialog.getWindow().setBackgroundDrawable(
                                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();


                                                //Toast.makeText(Dashboard.this, "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                                new CountDownTimer(1000, 1000) {

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        // TODO Auto-generated method stub
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        // TODO Auto-generated method stub
                                                        Toast.makeText(Dashboard.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        loading.dismissDialog();
                                                        dialogi.dismiss();

                                                    }
                                                }.start();

                                            }
                                            else if(res.contains("no")){



                                                //loading= new LoadingDialog(login.this);
                                                loading.dismissDialog();
                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Unable To Send Message !");
                                                reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                                reasonBtn.setText("OK");
                                                reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                /*alertDialogBuilderUserInput.setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                //finish();
                                                                //System.exit(0);
                                                            }
                                                        });*/
                                                //reason.setText("Reasn : " + res);
                                                dialog.getWindow().setBackgroundDrawable(
                                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();

                                                //Toast.makeText(Dashboard.this, "NOooooooo", Toast.LENGTH_SHORT).show();


                                                //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                            }

                                            //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Unable To Contact Server ! Try After Some Time !");
                                            reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                            reasonBtn.setText("OK");
                                            reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                            final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            dialog.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismissDialog();
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                Log.e("JSON Parser", "Error parsing data " + error.toString());
                                //Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();

                                Log.e("Name",name);
                                map.put("namekey",name);
                                map.put("mobilekey",phoneno);
                                map.put("emailkey",email);
                                map.put("citykey",city);
                                map.put("societykey",society);
                                map.put("blockkey",block);
                                map.put("msg",MSG.getText().toString());
                                return  map;
                            }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                        };

                        RequestQueue rq = Volley.newRequestQueue(Dashboard.this);
                        rq.add(sr);

                        ///

                    }
                });


                dialogi.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogi.setCanceledOnTouchOutside(false);
                dialogi.show();

                ///
                //Toast.makeText(this, "Msg To Guard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.membercdv7:
                Intent mlmm = new Intent(Dashboard.this, ShowSocietyNotice.class);
                mlmm.putExtra("role",role);
                startActivity(mlmm);
                //Toast.makeText(this, "SOciety Notice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv1:
                Intent m55 = new Intent(this,ManageSoc.class);
                startActivity(m55);
                //Toast.makeText(this, "Society ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.admncdv2:
                Toast.makeText(this, "Security", Toast.LENGTH_SHORT).show();
                break;

            case R.id.admncdv33:
                Intent i = new Intent(Dashboard.this, AddNotice.class);
                i.putExtra("name",name);
                i.putExtra("phoneno",phoneno);
                i.putExtra("city",city);
                i.putExtra("society",society);
                i.putExtra("block",block);
                startActivity(i);
                //Toast.makeText(this, "Mange Notice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv10:
                Intent mlm = new Intent(Dashboard.this, ShowSocietyNotice.class);
                mlm.putExtra("role",role);
                startActivity(mlm);
                //Toast.makeText(this, "View Society Notice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv3:
                Intent ii = new Intent(Dashboard.this, ManageMembers.class);
                startActivity(ii);
                //Toast.makeText(this, "Member", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv4:
                Intent m = new Intent(this,AllowCab.class);
                startActivity(m);

                //Toast.makeText(this, "Cab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv5:
                Toast.makeText(this, "Guest", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv6:
                Intent m4 = new Intent(this,AllowDelievery.class);
                startActivity(m4);
               // Toast.makeText(this, "Delievery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv7:

                LayoutInflater layoutInflaterAndroidii = LayoutInflater.from(Dashboard.this);
                View mViewii = layoutInflaterAndroidii.inflate(R.layout.fragment_allow_child_exit, null);
                AlertDialog.Builder alertDialogBuilderUserInputi = new AlertDialog.Builder(Dashboard.this);
                alertDialogBuilderUserInputi.setView(mViewii);
                //
                final EditText parentNamei = mViewii.findViewById(R.id.ALEParentName);
                final CardView submiti = mViewii.findViewById(R.id.ACESubmit);
                final Button selTimei = mViewii.findViewById(R.id.ACESelTimeBtn);
                final AlertDialog dialoge = alertDialogBuilderUserInputi.create();

                selTimei.setText("0 Hours");

                selTimei.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //if (loginActivityWeakRef.get() != null && !loginActivityWeakRef.get().isFinishing()) {
                        //Activity activity = (Activity)getApplicationContext();
                        //if (loginActivityWeakRef.get() != null && !loginActivityWeakRef.get().isFinishing()) {

                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                    Dashboard.this,
                                    R.style.BottomThemeDialogTheme
                            );
                /*View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.fragment_valid_for,
                                (CoordinatorLayout) view.findViewById(R.id.ll2));*/
                            View bottomSHeet = LayoutInflater.from(Dashboard.this)
                                    .inflate(R.layout.fragment_valid_for,
                                            (LinearLayout) view.findViewById(R.id.ll2));

                            Button bt1 = bottomSHeet.findViewById(R.id.valid1hr);

                            Button bt2 = bottomSHeet.findViewById(R.id.valid2hr);
                            Button bt4 = bottomSHeet.findViewById(R.id.valid4hr);
                            Button bt8 = bottomSHeet.findViewById(R.id.valid8hr);
                            Button bt12 = bottomSHeet.findViewById(R.id.valid12hr);
                            Button bt24 = bottomSHeet.findViewById(R.id.valid24hr);
                        bt1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 1;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt1.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTimei.setText(btn_id+" Hour");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();
                                //bt2.setBackgroundColor(Color.RED);
                                //bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 2;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt2.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt2.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTimei.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();
                                //bt2.setBackgroundColor(Color.RED);
                                //bt2.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 4;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt4.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt4.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTimei.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();
                                //bt2.setBackgroundColor(Color.RED);
                                //bt4.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 8;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt8.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt8.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTimei.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();


                                //bt2.setBackgroundColor(Color.RED);
                                // bt8.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 12;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt12.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt12.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTimei.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();


                                //bt2.setBackgroundColor(Color.RED);
                                //bt12.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                        bt24.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btn_id = 24;
                                Log.e("ButtonId", "Button" + btn_id);
                                bt24.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                                new CountDownTimer(250, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onFinish() {
                                        // TODO Auto-generated method stub
                                        bt24.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                        selTimei.setText(btn_id+" Hours");
                                        bottomSheetDialog.dismiss();
                                    }
                                }.start();


                                //bt2.setBackgroundColor(Color.RED);
                                //bt24.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                            }
                        });
                            bottomSheetDialog.setContentView(bottomSHeet);

                            bottomSheetDialog.setCanceledOnTouchOutside(false);
                            bottomSheetDialog.show();
                        //}
                    }
                });

                submiti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(parentNamei.getText().toString().isEmpty() || selTimei.getText().toString().contains("0 Hours")){
                            Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(selTimei.getText().toString().contains("0 Hours")){
                            Toast.makeText(Dashboard.this, "Select Valid Time Period", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(parentNamei.getText().toString().isEmpty()){
                            parentNamei.setError("Please Enter parent Name !");
                            return;
                            //Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        }

                        loading.startLoadingDialog();
                        ///
                        StringRequest sr = new StringRequest(POST, UPLOAD_URL1,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                                            JSONObject js = jsonArray.getJSONObject(0);
                                            //Log.e("ResponseFull",response);
                                            String res = js.getString("repon");
                                            if(!res.contains("no")){
                                                //userEmail= res;

                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Request Send Successfully !");
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
                                                //Toast.makeText(Dashboard.this, "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                                new CountDownTimer(1000, 1000) {

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        // TODO Auto-generated method stub
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        // TODO Auto-generated method stub
                                                        Toast.makeText(Dashboard.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        loading.dismissDialog();
                                                        dialoge.dismiss();

                                                    }
                                                }.start();

                                            }
                                            else if(res.contains("no")){
                                                //loading= new LoadingDialog(login.this);
                                                loading.dismissDialog();
                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                               // Toast.makeText(Dashboard.this, "NOooooooo", Toast.LENGTH_SHORT).show();


                                                //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                            }

                                            //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                Log.e("JSON Parser", "Error parsing data " + error.toString());
                                Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();

                                map.put("childnamekey",parentNamei.getText().toString());
                                map.put("timeper",selTimei.getText().toString());
                                map.put("gname",name);
                                map.put("mobilekey",phoneno);
                                map.put("emailkey",email);
                                map.put("societykey",society);
                                map.put("blockkey",block);
                                map.put("citykey",city);

                                return  map;
                            }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                        };
                        RequestQueue rq = Volley.newRequestQueue(Dashboard.this);
                        rq.add(sr);
                        ///

                    }
                });


                dialoge.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialoge.setCanceledOnTouchOutside(false);
                dialoge.show();


                /*Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.temp);
                dialog.setCancelable(false);
                final Button tn = ;
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();*/

                //Toast.makeText(this, "child", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv8:
                Intent klm = new Intent(this,Genpdf.class);
                klm.putExtra("block",block);
                klm.putExtra("city",city);
                klm.putExtra("society",society);

                startActivity(klm);
                //Toast.makeText(this, "Gatepass", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admncdv9:
                LayoutInflater layoutInflaterMSGi = LayoutInflater.from(Dashboard.this);
                View ViewMSGi = layoutInflaterMSGi.inflate(R.layout.fragment_msg_to_guard, null);
                AlertDialog.Builder MSGalertDialogBuilderUserInputi = new AlertDialog.Builder(Dashboard.this);
                MSGalertDialogBuilderUserInputi.setView(ViewMSGi);
                //
                final EditText MSGi = ViewMSGi.findViewById(R.id.MTGmsg);
                final CardView MsgSubmiti = ViewMSGi.findViewById(R.id.MTGSubmit);
                //final Button selTime = mViewi.findViewById(R.id.ACESelTimeBtn);
                final AlertDialog dialogie = MSGalertDialogBuilderUserInputi.create();

                //selTime.setText("0 Hours");

                MsgSubmiti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(MSGi.getText().toString().isEmpty()){
                            MSGi.setError("Please Enter Message  !");
                            return;
                            //Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        }
                        if(MSGi.getText().toString().length()>=250){
                            MSGi.setError("Message Length must be Max 250 Charecter !");
                            return;
                            //Toast.makeText(Dashboard.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(Dashboard.this, MSGi.getText(), Toast.LENGTH_SHORT).show();
                        loading.startLoadingDialog();
                        ///
                        StringRequest sr = new StringRequest(POST, UPLOAD_URL2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                                            JSONObject js = jsonArray.getJSONObject(0);
                                            //Log.e("ResponseFull",response);
                                            String res = js.getString("repon");
                                            if(!res.contains("no")){
                                                //userEmail= res;

                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Message Send Successfully");
                                                reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                                reasonBtn.setText("OK");
                                                reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                /*alertDialogBuilderUserInput.setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                //finish();
                                                                //System.exit(0);
                                                            }
                                                        });*/
                                                //reason.setText("Reasn : " + res);
                                                dialog.getWindow().setBackgroundDrawable(
                                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();


                                                //Toast.makeText(Dashboard.this, "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                                new CountDownTimer(1000, 1000) {

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        // TODO Auto-generated method stub
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        // TODO Auto-generated method stub
                                                        Toast.makeText(Dashboard.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        loading.dismissDialog();
                                                        dialogie.dismiss();

                                                    }
                                                }.start();

                                            }
                                            else if(res.contains("no")){



                                                //loading= new LoadingDialog(login.this);
                                                loading.dismissDialog();
                                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                                alertDialogBuilderUserInput.setView(mViewi);
                                                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                                reasonTv.setText("Unable To Send Message !");
                                                reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                                reasonBtn.setText("OK");
                                                reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                /*alertDialogBuilderUserInput.setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                //finish();
                                                                //System.exit(0);
                                                            }
                                                        });*/
                                                //reason.setText("Reasn : " + res);
                                                dialog.getWindow().setBackgroundDrawable(
                                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();

                                                //Toast.makeText(Dashboard.this, "NOooooooo", Toast.LENGTH_SHORT).show();


                                                //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                            }

                                            //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Unable To Contact Server ! Try After Some Time !");
                                            reasonTv.setTextColor(getResources().getColor(R.color.responseNegative));
                                            reasonBtn.setText("OK");
                                            reasonIv.setBackground(getResources().getDrawable(R.drawable.wrong));
                                            final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            dialog.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismissDialog();
                                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
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
                                final AlertDialog dialog = alertDialogBuilderUserInput.create();

                                reasonBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                Log.e("JSON Parser", "Error parsing data " + error.toString());
                                //Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();


                                map.put("namekey",name);
                                map.put("mobilekey",phoneno);
                                map.put("emailkey",email);
                                map.put("citykey",city);
                                map.put("societykey",society);
                                map.put("blockkey",block);
                                map.put("msg",MSGi.getText().toString());
                                return  map;
                            }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                        };

                        RequestQueue rq = Volley.newRequestQueue(Dashboard.this);
                        rq.add(sr);

                        ///

                    }
                });


                dialogie.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogie.setCanceledOnTouchOutside(false);
                dialogie.show();
                //Toast.makeText(this, "Msg To Guard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.seccdv1:
                Intent j = new Intent(this,ManageUserMsg.class);
                j.putExtra("society",society);
                j.putExtra("city",city);
                startActivity(j);
                //Toast.makeText(this, "User Messae", Toast.LENGTH_SHORT).show();
                break;

            case R.id.seccdv2:
                Intent mio = new Intent(this,SecShowGatepass.class);
                startActivity(mio);
                //Toast.makeText(this, "Gatepass", Toast.LENGTH_SHORT).show();
                break;
            case R.id.seccdv3:
                Intent m45= new Intent(this,ManageChildExit.class);
                startActivity(m45);
                //Toast.makeText(this, "Children", Toast.LENGTH_SHORT).show();
                break;
            case R.id.seccdv4:
                Intent mlmi = new Intent(Dashboard.this, ShowSocietyNotice.class);
                mlmi.putExtra("role",role);
                startActivity(mlmi);
                //Toast.makeText(this, "Society Notice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.seccdv5:
                Intent mi = new Intent(this,SecShowCab.class);
                startActivity(mi);

                //Toast.makeText(this, "Allow Cab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.seccdv6:
                Intent m5 = new Intent(this,SecShowDeli.class);
                startActivity(m5);
                //Toast.makeText(this, "Allow Delievery", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent i = new Intent(Dashboard.this,login.class);
        Intent profile = new Intent(Dashboard.this,userprofile.class);
        switch (item.getItemId()){
            case R.id.admn_nav_home:
                //Toast.makeText(this, "About us ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_profile:
                startActivity(profile);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_logout:
                editor.clear();
                editor.apply();
                startActivity(i);
                finish();
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_emgncyno:
                Intent k1 = new Intent(Dashboard.this, ViewEmgNo.class);
                startActivity(k1);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_manageemgncyno:
                Intent kjj = new Intent(Dashboard.this, AddEmgnNoList.class);
                startActivity(kjj);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_vendorno:
                //WorkerFrag fragWorkers = new WorkerFrag();
                Intent k = new Intent(Dashboard.this, viewii.class);
                startActivity(k);
                //loadWorkersFrag(fragWorkers);
                //Toast.makeText(this, "Vendors No ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_managevendor:
                Intent kj = new Intent(Dashboard.this, Add_Worker_List.class);
                startActivity(kj);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.admn_nav_abtus:

                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Dashboard.this);
                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_abtus, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Dashboard.this);
                alertDialogBuilderUserInput.setView(mViewi);
                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mem_nav_home:
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mem_nav_profile:
                startActivity(profile);
                //.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mem_nav_logout:
                editor.clear();
                editor.apply();
                startActivity(i);
                finish();
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mem_nav_emgncyno:
                Intent k12 = new Intent(Dashboard.this, ViewEmgNo.class);
                startActivity(k12);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mem_nav_vendorno:
                Intent k3 = new Intent(Dashboard.this, viewii.class);
                startActivity(k3);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mem_nav_abtus:
                LayoutInflater layoutInflaterAndroidii = LayoutInflater.from(Dashboard.this);
                View mViewii = layoutInflaterAndroidii.inflate(R.layout.fragment_abtus, null);
                AlertDialog.Builder alertDialogBuilderUserInputi = new AlertDialog.Builder(Dashboard.this);
                alertDialogBuilderUserInputi.setView(mViewii);
                final AlertDialog dialogi = alertDialogBuilderUserInputi.create();
                dialogi.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogi.show();
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sec_nav_home:
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sec_nav_profile:
                startActivity(profile);
                //.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sec_nav_logout:
                editor.clear();
                editor.apply();
                startActivity(i);
                finish();
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sec_nav_abtus:
                LayoutInflater layoutInflaterAndroidiii = LayoutInflater.from(Dashboard.this);
                View mViewiii = layoutInflaterAndroidiii.inflate(R.layout.fragment_abtus, null);
                AlertDialog.Builder alertDialogBuilderUserInputii = new AlertDialog.Builder(Dashboard.this);
                alertDialogBuilderUserInputii.setView(mViewiii);
                final AlertDialog dialogii = alertDialogBuilderUserInputii.create();
                dialogii.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogii.show();
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;
            //case R.id.sec_nav_emgncyno:
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
            //    break;
            case R.id.sec_nav_societymember:
                Intent km = new Intent(Dashboard.this, SecShowMembers.class);
                startActivity(km);
                //Toast.makeText(this, "LOGOUT ", Toast.LENGTH_SHORT).show();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadWorkersFrag(WorkerFrag ff) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.id1 , ff);
        //Intent i = new Intent(Dashboard.this,temp.class);
        //startActivity(i);

    }
}