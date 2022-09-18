package com.example.societymanagmentsystem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class Genpdf extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button pdfGen, OselDate, OselTime, OselVal, FselDays, Fselval, FselStime, FselEtime;
    Bitmap bmp, scaledbmp;
    EditText userName, userPhone;
    Date dateObj;
    DateFormat dateFormat;
    int btn_id = -1;
    ConstraintLayout ll1, ll2;
    String userBlock, gatepassno, userCity, userSociety;
    String init_Start_curr_Time = "";
    String Days_Name = "", final_Day_Name;
    int day_of_week = 0;
    private static final String UPLOAD_URL = "https://vivek.ninja/Gatepass/get_gatepeassno.php";
    String UPLOAD_URL_add;
    private static final String UPLOAD_URL_once = "https://vivek.ninja/Gatepass/add_gatepassonce.php";
    private static final String UPLOAD_URL_freq = "https://vivek.ninja/Gatepass/add_gatepassfreq.php";
    public LoadingDialog loading;


    Spinner spinner_type_worker;
    ArrayAdapter<CharSequence> workerTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genpdf);

        Intent j = getIntent();
        userBlock = j.getStringExtra("block");
        userCity = j.getStringExtra("city");
        userSociety = j.getStringExtra("society");
        //Toast.makeText(this, userBlock, Toast.LENGTH_SHORT).show();


        pdfGen = findViewById(R.id.pdfgen);
        loading = new LoadingDialog(Genpdf.this);


        spinner_type_worker = findViewById(R.id.gatepasstypespinner);

        userName = findViewById(R.id.GPUname);
        userPhone = findViewById(R.id.GPUphone);

        OselDate = findViewById(R.id.GPOseldate);
        OselTime = findViewById(R.id.GPOseltime);
        OselVal = findViewById(R.id.GPOselval);

        FselDays = findViewById(R.id.GPFseldays);
        Fselval = findViewById(R.id.GPFselval);
        FselStime = findViewById(R.id.GPFselstarttime);
        FselEtime = findViewById(R.id.GPFselendtime);


        ll1 = findViewById(R.id.frame1);
        ll2 = findViewById(R.id.frame2);

        workerTypeAdapter = ArrayAdapter.createFromResource(this, R.array.gatepasstype, android.R.layout.simple_spinner_item);
        workerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type_worker.setAdapter(workerTypeAdapter);

        spinner_type_worker.setOnItemSelectedListener(this);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pdfimage);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 1200, 560, false);

        ///

        generatGatepass();

        ///

        DateFormat df = new SimpleDateFormat("hh:mm aa");
        String currTime = df.format(Calendar.getInstance().getTime());

        DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
        String currDate = df2.format(Calendar.getInstance().getTime());

        OselDate.setText(currDate);
        OselTime.setText(currTime);
        OselVal.setText("1 Hour");

        FselDays.setText("0 Days");
        Fselval.setText("0 Days Validity");

        String currTimei = df.format(Calendar.getInstance().getTime());
        FselStime.setText(currTimei);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateandTime = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 1);
            System.out.println("Time here " + calendar.getTime());
            // Log.e("AddTime",calendar.getTime().toString());

            DateFormat dfi = new SimpleDateFormat("hh:mm aa");
            String currTimeiiii = dfi.format(calendar.getTime());
            FselEtime.setText(currTimeiiii);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        OselVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                /*View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.fragment_valid_for,
                                (CoordinatorLayout) view.findViewById(R.id.ll2));*/
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
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
                                OselVal.setText(btn_id + " Hour");
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
                                OselVal.setText(btn_id + " Hours");
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
                                OselVal.setText(btn_id + " Hours");
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
                                OselVal.setText(btn_id + " Hours");
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
                                OselVal.setText(btn_id + " Hours");
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
                                OselVal.setText(btn_id + " Hours");
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


            }
        });

        OselTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
                        .inflate(R.layout.bottom_sheet_timer,
                                (LinearLayout) view.findViewById(R.id.lin1));

                TimePicker dp = bottomSHeet.findViewById(R.id.timePicker);
                ImageView img1 = bottomSHeet.findViewById(R.id.wrongBtn);
                ImageView img2 = bottomSHeet.findViewById(R.id.rightBtn);

                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*int hour = dp.getHour();
                        int min = dp.getMinute();
                        String timee = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(timee);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
                        bottomSheetDialog.dismiss();
                    }
                });
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour = dp.getHour();
                        int min = dp.getMinute();
                        String timee = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(timee);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            OselTime.setText(f12Hours.format(date));
                            //Toast.makeText(Genpdf.this, f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        bottomSheetDialog.dismiss();
                    }
                });

                /*bottomSHeet.findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(), "Hiiiii", Toast.LENGTH_SHORT).show();
                    }
                });*/
                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });

        OselDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
                        .inflate(R.layout.bottom_sheet_calender,
                                (LinearLayout) view.findViewById(R.id.lin));

                CalendarView dp = bottomSHeet.findViewById(R.id.datePicker);
                dp.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                        //Toast.makeText(Genpdf.this, i2 + " " + i1 + " " + i, Toast.LENGTH_SHORT).show();
                        String monthname = "";
                        if ((i1 + 1) == 1) {
                            monthname = "Jan";
                        } else if ((i1 + 1) == 1) {
                            monthname = "Jan";
                        } else if ((i1 + 1) == 2) {
                            monthname = "Feb";
                        } else if ((i1 + 1) == 3) {
                            monthname = "Mar";
                        } else if ((i1 + 1) == 4) {
                            monthname = "Apr";
                        } else if ((i1 + 1) == 5) {
                            monthname = "May";
                        } else if ((i1 + 1) == 6) {
                            monthname = "June";
                        } else if ((i1 + 1) == 7) {
                            monthname = "July";
                        } else if ((i1 + 1) == 8) {
                            monthname = "Aug";
                        } else if ((i1 + 1) == 9) {
                            monthname = "Sep";
                        } else if ((i1 + 1) == 10) {
                            monthname = "Oct";
                        } else if ((i1 + 1) == 11) {
                            monthname = "Nov";
                        } else if ((i1 + 1) == 12) {
                            monthname = "Dec";
                        }
                        OselDate.setText(i2 + " " + (monthname) + " " + i);
                        bottomSheetDialog.dismiss();
                    }
                });


                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });


        FselStime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
                        .inflate(R.layout.bottom_sheet_timer,
                                (LinearLayout) view.findViewById(R.id.lin1));

                TimePicker dp = bottomSHeet.findViewById(R.id.timePicker);
                ImageView img1 = bottomSHeet.findViewById(R.id.wrongBtn);
                ImageView img2 = bottomSHeet.findViewById(R.id.rightBtn);

                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*int hour = dp.getHour();
                        int min = dp.getMinute();
                        String timee = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(timee);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
                        bottomSheetDialog.dismiss();
                    }
                });
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour = dp.getHour();
                        int min = dp.getMinute();
                        String timee = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(timee);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            FselStime.setText(f12Hours.format(date));
                            init_Start_curr_Time = f12Hours.format(date);
                            //Toast.makeText(Genpdf.this, f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        bottomSheetDialog.dismiss();
                    }
                });

                /*bottomSHeet.findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(), "Hiiiii", Toast.LENGTH_SHORT).show();
                    }
                });*/
                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });


        FselEtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
                        .inflate(R.layout.bottom_sheet_timer,
                                (LinearLayout) view.findViewById(R.id.lin1));

                TimePicker dp = bottomSHeet.findViewById(R.id.timePicker);
                ImageView img1 = bottomSHeet.findViewById(R.id.wrongBtn);
                ImageView img2 = bottomSHeet.findViewById(R.id.rightBtn);

                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*int hour = dp.getHour();
                        int min = dp.getMinute();
                        String timee = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(timee);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
                        bottomSheetDialog.dismiss();
                    }
                });
                img2.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        int hour = dp.getHour();
                        int min = dp.getMinute();
                        String timee = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(timee);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );

                            //Comparison Here
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

                            if (init_Start_curr_Time == "") {
                                init_Start_curr_Time = FselStime.getText().toString();
                            }
                            String start_Time = init_Start_curr_Time;
                            String end_Time = f12Hours.format(date);

                            LocalTime start = LocalTime.parse(start_Time, timeFormatter);
                            LocalTime end = LocalTime.parse(end_Time, timeFormatter);

                            Duration diff = Duration.between(start, end);
                            Log.e("DASD", diff + " DIff");

                            long hours = diff.toHours();
                            long minutes = diff.minusHours(hours).toMinutes();
                            if (hours <= -1 || minutes <= -1) {
                                Toast.makeText(Genpdf.this, "End Time Must Be Greater Than Start Time !!", Toast.LENGTH_SHORT).show();
                            } else {
                                FselEtime.setText(f12Hours.format(date));
                                bottomSheetDialog.dismiss();
                            }
                            //String totalTimeString = String.format("%02d:%02d", hours, minutes);
                            //Log.e("TIME",totalTimeString);
                            //System.out.println("TotalTime in Hours and Mins Format is " + totalTimeString);


                            //endTime.setText(f12Hours.format(date));
                            //Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                });

                /*bottomSHeet.findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(), "Hiiiii", Toast.LENGTH_SHORT).show();
                    }
                });*/
                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });

        Fselval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
                        .inflate(R.layout.fragment_validity_days,
                                (LinearLayout) view.findViewById(R.id.fll2));

                Button week1 = bottomSHeet.findViewById(R.id.fvalid1wk);
                Button Day15 = bottomSHeet.findViewById(R.id.fvalid15day);
                Button Month1 = bottomSHeet.findViewById(R.id.fvalid1mnt);

                week1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        week1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                        new CountDownTimer(250, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub
                                week1.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                Fselval.setText("1 Week");
                                bottomSheetDialog.dismiss();
                            }
                        }.start();
                        //bt2.setBackgroundColor(Color.RED);
                        //bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                    }
                });
                Day15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Day15.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                        new CountDownTimer(250, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub
                                Day15.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                Fselval.setText("15 Days");
                                bottomSheetDialog.dismiss();
                            }
                        }.start();
                        //bt2.setBackgroundColor(Color.RED);
                        //bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                    }
                });
                Month1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Month1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                        new CountDownTimer(250, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub
                                Month1.setBackground(getResources().getDrawable(R.drawable.valid_btn_theme));
                                Fselval.setText("1 Month");
                                bottomSheetDialog.dismiss();
                            }
                        }.start();
                        //bt2.setBackgroundColor(Color.RED);
                        //bt1.setBackground(getResources().getDrawable(R.drawable.selected_valid_btn_theme));
                    }
                });
                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });


        FselDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        Genpdf.this,
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(Genpdf.this)
                        .inflate(R.layout.fragment_sel_days,
                                (LinearLayout) view.findViewById(R.id.fll1));

                CheckBox sun = bottomSHeet.findViewById(R.id.fsun);
                CheckBox mon = bottomSHeet.findViewById(R.id.fmon);
                CheckBox tue = bottomSHeet.findViewById(R.id.ftue);
                CheckBox wed = bottomSHeet.findViewById(R.id.fwed);
                CheckBox thu = bottomSHeet.findViewById(R.id.fthu);
                CheckBox fri = bottomSHeet.findViewById(R.id.ffri);
                CheckBox sat = bottomSHeet.findViewById(R.id.fsat);
                ImageView submit = bottomSHeet.findViewById(R.id.fsubmit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sun.isChecked()) {
                            Days_Name += "Sunday,";
                            day_of_week += 1;
                        }
                        if (mon.isChecked()) {
                            Days_Name += "Monday,";
                            day_of_week += 1;
                        }
                        if (tue.isChecked()) {
                            Days_Name += "Tuesday,";
                            day_of_week += 1;
                        }
                        if (wed.isChecked()) {
                            Days_Name += "Wednesday,";
                            day_of_week += 1;
                        }
                        if (thu.isChecked()) {
                            Days_Name += "Thursday,";
                            day_of_week += 1;
                        }
                        if (fri.isChecked()) {
                            Days_Name += "Friday,";
                            day_of_week += 1;
                        }
                        if (sat.isChecked()) {
                            Days_Name += "Saturday,";
                            day_of_week += 1;
                        }
                        Toast.makeText(Genpdf.this, Days_Name, Toast.LENGTH_SHORT).show();
                        FselDays.setText(day_of_week + " Days");
                        bottomSheetDialog.dismiss();
                        day_of_week = 0;
                        final_Day_Name = Days_Name;
                        Days_Name = "";
                    }
                });

                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();
    }

    private void generatGatepass(){
        StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject js = jsonArray.getJSONObject(0);
                            String res = js.getString("repon");
                            gatepassno = js.getString("gatepass");
                            //Toast.makeText(Genpdf.this, "Gatepass : "+gatepassno, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {

                            e.printStackTrace();
                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
                            alertDialogBuilderUserInput.setView(mViewi);
                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                            reasonTv.setText("Currently Server is Down ! Please Try After Some Time !");
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

                Log.e("JSON Parser", "Error parsing data " + error.toString());
                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
                alertDialogBuilderUserInput.setView(mViewi);
                TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                reasonTv.setText("Currently Server is Down ! Please Try After Some Time !");
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
                //Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(Genpdf.this);
        rq.add(sr);

    }

    private void createPDF() {
        pdfGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userPhone.getText().toString().isEmpty() ||
                        userPhone.getText().toString().isEmpty() ||
                        spinner_type_worker.getSelectedItemPosition() == 0) {
                    Toast.makeText(Genpdf.this, "Enter All Fields !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner_type_worker.getSelectedItemPosition() == 2) {
                    if (FselDays.getText().toString() == "0 Days") {
                        Toast.makeText(Genpdf.this, "Please Select Days !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Fselval.getText().toString() == "0 Days Validity") {
                        Toast.makeText(Genpdf.this, "Please Select Validity Of Days !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                ///
                Log.e("Request", "1st");
                /*StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    String res = js.getString("repon");
                                    gatepassno = js.getString("gatepass");
                                    Toast.makeText(Genpdf.this, "Gatepass : "+gatepassno, Toast.LENGTH_SHORT).show();


                                        //userEmail= res;
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Gatepass Generated Successfully ! \n Your Gate pass No is :  "+gatepassno);
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

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
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

                        Log.e("JSON Parser", "Error parsing data " + error.toString());
                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
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
                        //Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue rq = Volley.newRequestQueue(Genpdf.this);
                rq.add(sr);*/

                ///


                dateObj = new Date();
                PdfDocument myPdf = new PdfDocument();
                Paint myPaint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();

                PdfDocument.Page myPage1 = myPdf.startPage(myPageInfo);

                Canvas canvas = myPage1.getCanvas();

                canvas.drawBitmap(scaledbmp, 0, 0, myPaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC));
                titlePaint.setTextSize(70);
                canvas.drawText("Gate Pass", 600, 620, titlePaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Gate Pass No  :  "+gatepassno, 20, 700, myPaint);
                Log.e("Gatepass","Hii"+gatepassno);

                dateFormat = new SimpleDateFormat("dd/MM/yy");
                canvas.drawText("Date : " + dateFormat.format(dateObj), 20, 750, myPaint);

                dateFormat = new SimpleDateFormat("HH:mm:ss");
                canvas.drawText("Time : " + dateFormat.format(dateObj), 20, 800, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Member Name ", 250, 900, myPaint);
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("=>", 600, 900, myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText(userName.getText().toString(), 750, 900, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Block/Wing No ", 250, 1000, myPaint);
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("=>", 600, 1000, myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText(userBlock.toString(), 750, 1000, myPaint);


                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Member Phone No ", 250, 950, myPaint);
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("=>", 600, 950, myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText(userPhone.getText().toString(), 750, 950, myPaint);

                myPaint.setColor(Color.RED);
                canvas.drawLine(0, 1050, 1200, 1050, myPaint);


                if (spinner_type_worker.getSelectedItemPosition() == 1) {
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Date  ", 250, 1100, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1100, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(OselDate.getText().toString(), 750, 1100, myPaint);


                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Time  ", 250, 1150, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1150, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(OselTime.getText().toString(), 750, 1150, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Time Validity  ", 250, 1200, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1200, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(OselVal.getText().toString(), 750, 1200, myPaint);
                } else if (spinner_type_worker.getSelectedItemPosition() == 2) {
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(" Valid Days ", 250, 1100, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1100, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(Fselval.getText().toString(), 750, 1100, myPaint);


                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Start Time ", 250, 1150, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1150, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(FselStime.getText().toString(), 750, 1150, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("End Time ", 250, 1200, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1200, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText(FselEtime.getText().toString(), 750, 1200, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Valid Days  ", 250, 1250, myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("=>", 600, 1250, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    String arr[] = final_Day_Name.split(",");
                    int j = 1250;
                    for (int i = 0; i < arr.length; i++) {
                        Log.e("Temp", arr[i]);
                        canvas.drawText(arr[i], 750, j, myPaint);
                        j += 50;
                    }
                }

                /*myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Date  ",250,1100,myPaint);
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("=>",600,1100,myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("12th August 2021",750,1100,myPaint);


                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Time  ",250,1150,myPaint);
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("=>",600,1150,myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("08:12 PM",750,1150,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Valid Days  ",250,1200,myPaint);
                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("=>",600,1200,myPaint);
                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                String arr[] = {"Saturday","Monday","Thirsday"};
                int j = 1200;
                for(int i=0;i<arr.length;i++) {
                    canvas.drawText(arr[i],750,j,myPaint);
                    j+=50;
                }*/


                myPdf.finishPage(myPage1);

                String time = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault())
                        .format(System.currentTimeMillis());
                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath+"/Society App");
                dir.mkdirs();
                String fileName = "SOCGATEPASS"+time+".pdf";


                File file = new File(dir, "/"+fileName);


                try {
                    ///

                    myPdf.writeTo(new FileOutputStream(file));

                    if (spinner_type_worker.getSelectedItemPosition() == 1) {
                        UPLOAD_URL_add = "https://vivek.ninja/Gatepass/add_gatepassonce.php";
                    } else if (spinner_type_worker.getSelectedItemPosition() == 2) {
                        UPLOAD_URL_add = "https://vivek.ninja/Gatepass/add_gatepassfreq.php";
                    }
                    Log.e("Request", "2nd");

                    /////







                    /*new CountDownTimer(5000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            loading.dismissDialog();

                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
                            alertDialogBuilderUserInput.setView(mViewi);
                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                            reasonTv.setText("Gate pass is Stored at : "+Environment.getExternalStorageDirectory()+"/Gatepass.pdf");
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

                        }
                    }.start();*/


                    Log.e("Url",UPLOAD_URL_add);
                    StringRequest sr = new StringRequest(POST, UPLOAD_URL_add,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.e("Response",response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                                        JSONObject js = jsonArray.getJSONObject(0);
                                        String res = js.getString("repon");
                                        if(!res.contains("no")){
                                            //userEmail= res;
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
                                            alertDialogBuilderUserInput.setView(mViewi);
                                            TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                            ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                            Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                            reasonTv.setText("Your Gate pass No is :  " + gatepassno+".Gate pass PDF is stored at "+dir);
                                            reasonTv.setTextColor(getResources().getColor(R.color.responsePositive));
                                            reasonBtn.setText("OK");
                                            reasonIv.setBackground(getResources().getDrawable(R.drawable.check));
                                            final AlertDialog dialogi = alertDialogBuilderUserInput.create();

                                            reasonBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    dialogi.dismiss();
                                                    //loading.startLoadingDialog();


                                                }
                                            });

                                            dialogi.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialogi.setCanceledOnTouchOutside(false);
                                            dialogi.show();

                                            generatGatepass();



                                            //Toast.makeText(getContext(), "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                        }
                                        else if(res.contains("no")){
                                            //loading= new LoadingDialog(login.this);
                                            //loading.dismissDialog();
                                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
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


                                            //Toast.makeText(getContext(), "NOooooooo", Toast.LENGTH_SHORT).show();


                                            //Toast.makeText(login.this, "Sorry !", Toast.LENGTH_SHORT).show();
                                        }

                                        //Toast.makeText(login.this, res1, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        //loading.dismissDialog();
                                        e.printStackTrace();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
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
                            //loading.dismissDialog();
                            Log.e("JSON Parser", "Error parsing data " + error.toString());
                            LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(Genpdf.this);
                            View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Genpdf.this);
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
                            //Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();

                            map.put("gatepassno", gatepassno);
                            map.put("namekey", userName.getText().toString());
                            map.put("mobilekey", userPhone.getText().toString());
                            map.put("citykey", userCity);
                            map.put("societykey", userSociety);
                            map.put("blockkey", userBlock);
                            if(spinner_type_worker.getSelectedItemPosition()==1) {
                                map.put("datekey", OselDate.getText().toString());
                                map.put("timekey", OselTime.getText().toString());
                                map.put("valkey", OselVal.getText().toString());
                            }
                            else if(spinner_type_worker.getSelectedItemPosition()==2){
                                map.put("stimekey", FselStime.getText().toString());
                                map.put("etimekey", FselEtime.getText().toString());
                                map.put("dayskey", final_Day_Name);
                                map.put("valkey", Fselval.getText().toString());
                            }
                            return  map;
                        }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                    };
                    RequestQueue rq = Volley.newRequestQueue(Genpdf.this);
                    rq.add(sr);

                    ////


                    ///


                    ///


                    ///

                } catch (IOException e) {
                    e.printStackTrace();
                }

                myPdf.close();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.gatepasstypespinner) {
            if (spinner_type_worker.getSelectedItemPosition() == 1) {
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);

            } else if (spinner_type_worker.getSelectedItemPosition() == 2) {
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            } else if (spinner_type_worker.getSelectedItemPosition() == 0) {
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}