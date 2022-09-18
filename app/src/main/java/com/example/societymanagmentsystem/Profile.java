package com.example.societymanagmentsystem;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    TextView tv;
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        tv  = findViewById(R.id.profilename);
        im = findViewById(R.id.imageholder);
        String phoneno= "xyz";
        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            phoneno = extra.getString("phoneno");
        }
        tv.setText(phoneno);


    }
}
