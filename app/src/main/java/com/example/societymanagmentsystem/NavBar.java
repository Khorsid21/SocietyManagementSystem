package com.example.societymanagmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class NavBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    CardView cd1,cd2;
    TextView textView;
    ImageSlider imageSlider;
    ScrollView scv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);
        toolbar=findViewById(R.id.toolbar);
        imageSlider = findViewById(R.id.slider);
        scv = findViewById(R.id.VSV1);

        //cd1 = findViewById(R.id.cdv1);
        //cd2 = findViewById(R.id.cdv2);



        View hView =  navigationView.inflateHeaderView(R.layout.header);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageViewnav);
        imgvw .setImageResource(R.drawable.small_society_icon);
        TextView tv = (TextView)hView.findViewById(R.id.id1);
        TextView tv1 = (TextView)hView.findViewById(R.id.id2);
        tv.setText("EEE");
        tv.setText("sdsd");
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu_admin);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(NavBar.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.admin_dashboard_content, null);
        //cd1 = mView.findViewById(R.id.cdv1);
        //cd2 =mView.findViewById(R.id.cdv2);
        scv.addView(mView);


        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://vivek.ninja/images/603dd8d67d936.jpg",null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/603dd93aea93e.jpg",null));
        //slideModels.add(new SlideModel("https://adrenal-pairs.000webhostapp.com/images/3.jpg",null));
        slideModels.add(new SlideModel("https://vivek.ninja/images/4.jpg",ScaleTypes.FIT));
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

        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.setCheckedItem(R.id.nav_home);


        //cd1.setOnClickListener(this);
        //cd2.setOnClickListener(this);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_abtus:
                Toast.makeText(this, "About us ", Toast.LENGTH_SHORT).show();
        }*/
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()){
            case R.id.cdv1 :
                Toast.makeText(this, "Taxyyyyyy", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cdv2:
                Toast.makeText(this, "chidl", Toast.LENGTH_SHORT).show();
                break;
        } */
    }
}