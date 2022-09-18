package com.example.societymanagmentsystem;

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

public class NatureCreativePagerEmgnNoAdapter implements CreativePagerAdapter, View.OnClickListener {

    Context context;
    ConstraintLayout constll;
    TextView title;
    ImageView image;
    Button btn_worker;
    String city;
    static int j = 0;

    SharedPreferences sharedPreferences;
    //Button Logout;

    public static final String fileName = "USER_PROFILE";

    public NatureCreativePagerEmgnNoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View viewi) {
        switch (viewi.getId()) {
            case R.id.btn_open_worker:
                Button btn = viewi.findViewById(R.id.btn_open_worker);
                //Log.e("CLickedadsdas", btn.getText().toString());
                if (btn.getText().toString().contains("Ambulance")) {
                    Intent intent = new Intent(context,Emgn_no_list.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Type","Ambulance");
                    intent.putExtra("city",city);
                    context.startActivity(intent);
                    //Toast.makeText(context, "Ambulance", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(context,Workers_List.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("workerType","Food");
                    //context(new Intent(this, Workers_List.class));

                    //Intent g = new Intent(context.getApplicationContext(), Workers_List.class);
                    //context.startActivity(intent);

                    /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //sharingIntent.setType("text/plain");
                    //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

                    Intent startIntent = Intent.createChooser(sharingIntent, context.getResources().getString(Workers_List.class));
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startIntent);*/
                    //((Activity)context).startActivity(g);

                    Toast.makeText(context, "Ambulance", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Police")) {
                    Intent intent = new Intent(context,Emgn_no_list.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Type","Police");
                    intent.putExtra("city",city);
                    context.startActivity(intent);
                    Toast.makeText(context, "Police", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Blood")) {
                    Intent intent = new Intent(context,Emgn_no_list.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Type","Blood");
                    intent.putExtra("city",city);
                    context.startActivity(intent);
                    Toast.makeText(context, "Blood", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(context,Workers_List.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("workerType","Cook");
                    //context(new Intent(this, Workers_List.class));
                    //Intent g = new Intent(context.getApplicationContext(), Workers_List.class);
                    //context.startActivity(intent);

                    Toast.makeText(context, "Blood", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Fire")) {
                    Intent intent = new Intent(context,Emgn_no_list.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("city",city);
                    intent.putExtra("Type","Fire");
                    context.startActivity(intent);
                    Toast.makeText(context, "Fire", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(context,Workers_List.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("workerType","Electrician");
                    //context.startActivity(intent);
                    //Toast.makeText(context, "Fire", Toast.LENGTH_SHORT).show();
                } else if (btn.getText().toString().contains("Woman Safety")) {

                    Intent intent = new Intent(context,Emgn_no_list.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Type","Woman Safety");
                    intent.putExtra("city",city);

                    context.startActivity(intent);
                    Toast.makeText(context, "Woman Safety", Toast.LENGTH_SHORT).show();

                    //Intent intent = new Intent(context,Workers_List.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("workerType","Plumber");
                    //context.startActivity(intent);

                    //.makeText(context, "Woman Safety", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(context, "Cab", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public int getCount() {
        return EmgnArrayNature.values().length;
    }

    @NotNull
    @Override
    public View instantiateContentItem(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup, int i) {
        View contentRoot = layoutInflater.inflate(R.layout.item_creative_header_profile, viewGroup, false);
        ImageView imageView = (ImageView) contentRoot.findViewById(R.id.itemCreativeImage);
        imageView.setImageDrawable(
                ContextCompat.getDrawable(context, EmgnArrayNature.values()[i].getUserDrawable()));

        return contentRoot;
    }

    @NotNull
    @Override
    public View instantiateHeaderItem(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup, int i) {
        ViewGroup headerRoot = (ViewGroup) layoutInflater.inflate(R.layout.item_creative_content_nature, viewGroup, false);
        constll = headerRoot.findViewById(R.id.constll);
        title = headerRoot.findViewById(R.id.itemCreativeNatureTitle);
        image = headerRoot.findViewById(R.id.itemCreativeNatureImage);
        btn_worker = (Button) headerRoot.findViewById(R.id.btn_open_worker);
        title.setText(EmgnArrayNature.values()[i].getTitle());

        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        city = sharedPreferences.getString("city", "");

        //Log.e("Sample-1",NatureItemEmgnNo.values().toString());

        btn_worker.setText("View " + EmgnArrayNature.values()[i].getTitle() + " Section");

        /*Log.e("Original",NatureItemEmgnNo.values()[0].getTitle());
        Log.e("Original",NatureItemEmgnNo.values()[1].getTitle());
        Log.e("Original",NatureItemEmgnNo.values()[2].getTitle());
        Log.e("Original",NatureItemEmgnNo.values()[3].getTitle());
        Log.e("Original",NatureItemEmgnNo.values()[4].getTitle());
        Log.e("Original", btn_worker.getText().toString());*/
        image.setImageDrawable(ContextCompat.getDrawable(context, EmgnArrayNature.values()[i].getNatureDrawable()));

        btn_worker.setOnClickListener(this);

        return headerRoot;
    }

    @Override
    public boolean isUpdatingBackgroundColor() {
        return true;
    }

    @Nullable
    @Override
    public Bitmap requestBitmapAtPosition(int i) {
        return BitmapFactory.decodeResource(context.getResources(), EmgnArrayNature.values()[i].getNatureDrawable());
    }
}
