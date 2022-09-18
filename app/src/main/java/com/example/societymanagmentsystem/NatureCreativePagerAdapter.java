package com.example.societymanagmentsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;



import com.tbuonomo.creativeviewpager.adapter.CreativePagerAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class NatureCreativePagerAdapter implements CreativePagerAdapter, View.OnClickListener {
    Context context;
    ConstraintLayout constll;
    TextView title;
    ImageView image;
    Button btn_worker;
    static int j = 0;
    String city;
    SharedPreferences sharedPreferences;
    //Button Logout;
    public static final String fileName = "USER_PROFILE";

    public NatureCreativePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return WorkerArrayNature.values().length;
    }


    @Override
    public View instantiateContentItem(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {

        View contentRoot = layoutInflater.inflate(R.layout.item_creative_header_profile, viewGroup, false);
        ImageView imageView = (ImageView) contentRoot.findViewById(R.id.itemCreativeImage);
        imageView.setImageDrawable(
                ContextCompat.getDrawable(context, WorkerArrayNature.values()[i].getUserDrawable()));

        return contentRoot;
    }


    @Override
    public View instantiateHeaderItem(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {

        ViewGroup headerRoot = (ViewGroup) layoutInflater.inflate(R.layout.item_creative_content_nature, viewGroup, false);
        constll = headerRoot.findViewById(R.id.constll);
        title = headerRoot.findViewById(R.id.itemCreativeNatureTitle);
        image = headerRoot.findViewById(R.id.itemCreativeNatureImage);
        btn_worker = (Button) headerRoot.findViewById(R.id.btn_open_worker);
        title.setText(WorkerArrayNature.values()[i].getTitle());

        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city", "");


        //Log.e("Sample-1",NatureItem.values().toString());

        btn_worker.setText("View " + WorkerArrayNature.values()[i].getTitle() + " Section");


        /*Log.e("Original",NatureItem.values()[0].getTitle());
        Log.e("Original",NatureItem.values()[1].getTitle());
        Log.e("Original",NatureItem.values()[2].getTitle());
        Log.e("Original",NatureItem.values()[3].getTitle());
        Log.e("Original",NatureItem.values()[4].getTitle());*/
        Log.e("Original", btn_worker.getText().toString());
        image.setImageDrawable(ContextCompat.getDrawable(context, WorkerArrayNature.values()[i].getNatureDrawable()));

        btn_worker.setOnClickListener(this);

        /*btn_worker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("ButtonCLick","H"+btn_worker.getText().toString().trim()+"H");
                Log.e("ButtonCLick","H"+view.toString()+"H");
                if (btn_worker.getText().toString().contains("Food"))
                {
                    Log.e("ButtonCLick","YES1");
                    Toast.makeText(context, "On Food", Toast.LENGTH_SHORT).show();
                    //Intent i = Intent(context,Workers_List :: class.java)
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    //intent.putExtra("workerType","Cook")
                    //context.startActivity(intent)
                }
                else if (btn_worker.getText().toString().contains("Driver"))
                {
                    Log.e("ButtonCLick","YES2");
                    Toast.makeText(context, "On Drivers", Toast.LENGTH_SHORT).show();
                    /*val intent = Intent(context,Workers_List :: class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("workerType","Driver")
                    context.startActivity(intent)*/
                /*}
                else if (btn_worker.getText().toString() .contains("Cooks"))
                {
                    Log.e("ButtonCLick","YES3");
                    Toast.makeText(context, "On Cooks", Toast.LENGTH_SHORT).show();
                    /*val intent = Intent(context,Workers_List :: class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("workerType","Plumber")
                    context.startActivity(intent)*/
        //}
               /* else if (btn_worker.getText().toString().contains("Electrician"))
                {
                    Log.e("ButtonCLick","YES4");
                    Toast.makeText(context, "On Electrician", Toast.LENGTH_SHORT).show();
                    /*val intent = Intent(context,Workers_List :: class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("workerType","Electrician")
                    context.startActivity(intent)*/
        //}
                /*else if (btn_worker.getText().toString().contains("Plumber"))
                {
                    Log.e("ButtonCLick","YES5");
                    Toast.makeText(context, "On Plumber", Toast.LENGTH_SHORT).show();
                    /*val intent = Intent(context,Workers_List :: class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("workerType","Security Guard")
                    context.startActivity(intent)*/
        //}
        //}
        //});*/


        return headerRoot;
    }

    @Override
    public void onClick(View viewi) {
        switch (viewi.getId()) {
            case R.id.btn_open_worker:
                Button btn = viewi.findViewById(R.id.btn_open_worker);
                Log.e("CLickedadsdas", btn.getText().toString());
                if (btn.getText().toString().contains("Food")) {

                    Intent intent = new Intent(context,Workers_List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("workerType","Food");
                    intent.putExtra("city",city);
                    //context(new Intent(this, Workers_List.class));

                    //Intent g = new Intent(context.getApplicationContext(), Workers_List.class);
                    context.startActivity(intent);

                    /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //sharingIntent.setType("text/plain");
                    //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

                    Intent startIntent = Intent.createChooser(sharingIntent, context.getResources().getString(Workers_List.class));
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startIntent);*/
                    //((Activity)context).startActivity(g);

                    Toast.makeText(context, "Food", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Driver")) {
                    Intent intent = new Intent(context,Workers_List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("workerType","Driver");
                    intent.putExtra("city",city);
                    context.startActivity(intent);
                    Toast.makeText(context, "Driver", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Cooks")) {
                    Intent intent = new Intent(context,Workers_List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("workerType","Cook");
                    intent.putExtra("city",city);
                    //context(new Intent(this, Workers_List.class));
                    //Intent g = new Intent(context.getApplicationContext(), Workers_List.class);
                    context.startActivity(intent);

                    Toast.makeText(context, "Cooks", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Electrician")) {
                    Intent intent = new Intent(context,Workers_List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("workerType","Electrician");
                    intent.putExtra("city",city);
                    context.startActivity(intent);
                    Toast.makeText(context, "Electrician", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Plumber")) {
                    Intent intent = new Intent(context,Workers_List.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("workerType","Plumber");
                    intent.putExtra("city",city);
                    context.startActivity(intent);

                    Toast.makeText(context, "Plumber", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(context, "Cab", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean isUpdatingBackgroundColor() {
        return true;
    }


    @Override
    public Bitmap requestBitmapAtPosition(int i) {
        //Toast.makeText(context, BitmapFactory.decodeResource(context.getResources(), NatureItem.values()[i].getNatureDrawable()).toString(), Toast.LENGTH_SHORT).show();
        return BitmapFactory.decodeResource(context.getResources(), WorkerArrayNature.values()[i].getNatureDrawable());
        /*return BitmapFactory.decodeResource(context.resources,
                NatureItem.values()[position].natureDrawable)*/
    }
}
