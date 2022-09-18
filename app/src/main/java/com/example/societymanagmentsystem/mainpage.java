package com.example.societymanagmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainpage extends AppCompatActivity {

    Button login,signup,showdata,showimg,temp;
    public static  final String fileName="USER_PROFILE";
    public static  final String fileName1="issignedin";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        //Toast.makeText(mainpage.this, "ahh", Toast.LENGTH_SHORT).show();
        login = (Button) findViewById(R.id.loginbtn);
        signup = (Button)findViewById(R.id.signupbtn);
        showdata = findViewById(R.id.fetchdata);
        showimg = findViewById(R.id.fetchimg);
        temp = findViewById(R.id.temp);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn",false);
        //String phoneno= sharedPreferences.getString("phoneno","");
        //String role = sharedPreferences.getString("role","");
        //String email = sharedPreferences.getString("email","");

        if(isLoggedIn || isSignedIn){
            Intent i = new Intent(mainpage.this,Dashboard.class);
            startActivity(i);
            finish();
        }
        else {

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mainpage.this, "ahh", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mainpage.this, login.class);
                    startActivity(i);
                    finish();
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mainpage.this, "ahh", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mainpage.this, signup.class);
                    startActivity(i);
                    finish();
                }
            });

            showdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mainpage.this, showdata.class);
                    startActivity(i);
                    finish();
                }
            });


            showimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mainpage.this, signup2.class);
                    startActivity(i);
                    finish();
                }
            });

            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(mainpage.this, NavBar.class);
                    startActivity(i);
                /*LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mainpage.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.wait_user, null);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return i == KeyEvent.KEYCODE_BACK;
                    }
                });
                alertDialogBuilderUserInput.setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();
                                System.exit(0);
                            }
                        });
                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();*/
                }
            });
        }
    }
}