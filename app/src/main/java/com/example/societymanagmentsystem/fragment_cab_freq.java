package com.example.societymanagmentsystem;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

public class fragment_cab_freq extends Fragment {
    Button selDays, selVal, startTime,endTime, submit;
    String Days_Name="",final_Day_Name;
    int day_of_week=0;
    String init_Start_curr_Time = "";

    private static final String UPLOAD_URL=  "https://vivek.ninja/AllowCab/add_cab_freq.php";
    public static final String fileName = "USER_PROFILE";
    String userName,userEmail,userRole,userPhone,userSociety,userCity,userBlock;
    SharedPreferences sharedPreferences;
    //CalendarView calender;

    //String temp;
    String Date;
    private int mDate, mMonth, mYear;
    private int tMin, tHour;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_cab_freq, container, false);

        //bt1 = view.findViewById(R.id.cab_freq_btn);

        //calender = view.findViewById(R.id.calendarVieww);
        //date = (TextView)view.findViewById(R.id.today_date2);

        /*calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date.setText("Date is : " + i2 +" / " + (i1+1) + " / " + i);
            }
        });*/
        /*date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalanderBtm bottomcalan = new CalanderBtm();
                bottomcalan.show(getFragmentManager(),"Hii");

                String temp = bottomcalan.Date;
                date.setText(temp);
                Log.e("MESSGE","Msg"+temp);


            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Freq.", Toast.LENGTH_SHORT).show();
            }
        });*/
        //Log.e("MESSGE", "Msg" + temp);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selDays = view.findViewById(R.id.fselDayBtn);
        selVal = view.findViewById(R.id.fselValBtn);
        startTime = view.findViewById(R.id.fsttimeBtn);
        endTime = view.findViewById(R.id.fentimeBtn);
        submit = view.findViewById(R.id.freqSubmit);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        userPhone= sharedPreferences.getString("phoneno", "");
        userRole = sharedPreferences.getString("role", "");
        userName = sharedPreferences.getString("name", "");
        userEmail = sharedPreferences.getString("email", "");
        userSociety = sharedPreferences.getString("society", "");
        userBlock = sharedPreferences.getString("block", "");
        userCity = sharedPreferences.getString("city", "");


        selDays.setText("0 Days");
        selVal.setText("0 Days Validity");

        DateFormat df = new SimpleDateFormat("hh:mm aa");
        String currTime = df.format(Calendar.getInstance().getTime());

        startTime.setText(currTime);

        //
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateandTime = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 1);
            System.out.println("Time here "+calendar.getTime());
            // Log.e("AddTime",calendar.getTime().toString());

            DateFormat dfi = new SimpleDateFormat("hh:mm aa");
            String currTimeiiii = dfi.format(calendar.getTime());
            endTime.setText(currTimeiiii);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
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
                            startTime.setText(f12Hours.format(date));
                            init_Start_curr_Time=f12Hours.format(date);
                            //Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
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
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
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
                                init_Start_curr_Time = startTime.getText().toString();
                            }
                            String start_Time = init_Start_curr_Time;
                            String end_Time = f12Hours.format(date);

                            LocalTime start = LocalTime.parse(start_Time, timeFormatter);
                            LocalTime end = LocalTime.parse(end_Time, timeFormatter);

                            Duration diff = Duration.between(start, end);
                            Log.e("DASD",diff+" DIff");

                            long hours = diff.toHours();
                            long minutes = diff.minusHours(hours).toMinutes();
                            if(hours<=-1 || minutes<=-1) {
                                Toast.makeText(getActivity(), "End Time Must Be Greater Than Start Time !!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                endTime.setText(f12Hours.format(date));
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





        selVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
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
                                selVal.setText("1 Week");
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
                                selVal.setText("15 Days");
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
                                selVal.setText("1 Month");
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

        selDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
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
                        if(sun.isChecked()){
                            Days_Name+="Sunday,";
                            day_of_week+=1;
                        }
                        if(mon.isChecked()){
                            Days_Name+="Monday,";
                            day_of_week+=1;
                        }
                        if(tue.isChecked()){
                            Days_Name+="Tuesday,";
                            day_of_week+=1;
                        }
                        if(wed.isChecked()){
                            Days_Name+="Wednesday,";
                            day_of_week+=1;
                        }
                        if(thu.isChecked()){
                            Days_Name+="Thursday,";
                            day_of_week+=1;
                        }
                        if(fri.isChecked()){
                            Days_Name+="Friday,";
                            day_of_week+=1;
                        }
                        if(sat.isChecked()){
                            Days_Name+="Saturday,";
                            day_of_week+=1;
                        }
                        Toast.makeText(getActivity(), Days_Name, Toast.LENGTH_SHORT).show();
                        selDays.setText(day_of_week+" Days");
                        bottomSheetDialog.dismiss();
                        day_of_week=0;
                        final_Day_Name = Days_Name;
                        Days_Name="";
                    }
                });

                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selDays.getText().toString()=="0 Days"){
                    Toast.makeText(getActivity(), "Please Select Days !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selVal.getText().toString()=="0 Days Validity"){
                    Toast.makeText(getActivity(), "Please Select Validity Of Days !", Toast.LENGTH_SHORT).show();
                    return;
                }

                ////
                StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    String res = js.getString("repon");
                                    if(!res.contains("no")){
                                        //userEmail= res;
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(getActivity());
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Requset Send Successfully !");
                                        reasonTv.setTextColor(getActivity().getResources().getColor(R.color.responsePositive));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getActivity().getResources().getDrawable(R.drawable.check));
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
                                        //Toast.makeText(getContext(), "YESSSSSSSS", Toast.LENGTH_SHORT).show();

                                    }
                                    else if(res.contains("no")){
                                        //loading= new LoadingDialog(login.this);
                                        //loading.dismissDialog();
                                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(getActivity());
                                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                                        alertDialogBuilderUserInput.setView(mViewi);
                                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                        reasonTv.setText("Error Occured At Database Side ! Try Again Later !");
                                        reasonTv.setTextColor(getActivity().getResources().getColor(R.color.responseNegative));
                                        reasonBtn.setText("OK");
                                        reasonIv.setBackground(getActivity().getResources().getDrawable(R.drawable.wrong));
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
                                    LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(getActivity());
                                    View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilderUserInput.setView(mViewi);
                                    TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                                    ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                                    Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                                    reasonTv.setText(e.getMessage());
                                    reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                            getActivity().getResources().getDimension(R.dimen.result_font));
                                    reasonTv.setTextColor(getActivity().getResources().getColor(R.color.responseNegative));
                                    reasonBtn.setText("OK");
                                    reasonIv.setBackground(getActivity().getResources().getDrawable(R.drawable.wrong));
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
                        //loading.dismissDialog();
                        Log.e("JSON Parser", "Error parsing data " + error.toString());
                        LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(getActivity());
                        View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_reponse_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                        alertDialogBuilderUserInput.setView(mViewi);
                        TextView reasonTv = (TextView) mViewi.findViewById(R.id.response_text);
                        ImageView reasonIv = (ImageView) mViewi.findViewById(R.id.reponse_image);
                        Button reasonBtn = (Button) mViewi.findViewById(R.id.response_button);
                        reasonTv.setText(error.toString());
                        reasonTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                getActivity().getResources().getDimension(R.dimen.result_font));
                        reasonTv.setTextColor(getActivity().getResources().getColor(R.color.responseNegative));
                        reasonBtn.setText("OK");
                        reasonIv.setBackground(getActivity().getResources().getDrawable(R.drawable.wrong));
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

                        map.put("namekey",userName);
                        map.put("mobilekey",userPhone);
                        map.put("emailkey",userEmail);
                        map.put("role",userRole);
                        map.put("citykey",userCity);
                        map.put("societykey",userSociety);
                        map.put("blockkey",userBlock);
                        map.put("dayskey",final_Day_Name);
                        map.put("valdayskey",selVal.getText().toString());
                        map.put("starttimekey",startTime.getText().toString());
                        map.put("endtimekey",endTime.getText().toString());
                        return  map;
                    }

                    /*@Override
                    public byte[] getBody() throws AuthFailureError {
                        return new JSONObject(map).toString().getBytes();
                    }*/
                };
                RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
                rq.add(sr);


                ////


                //Log.e("Details",userName+"-"+userEmail+"-"+userPhone+"-"+userRole+"-"+userCity+"-"+userSociety+"-"+userBlock+"-"+final_Day_Name+"-"+selVal.getText().toString()+"-"+startTime.getText().toString()+"-"+endTime.getText().toString());
                //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();


            }
        });

        //calender = view.findViewById(R.id.calendarView2);
        //date = (TextView) view.findViewById(R.id.today_time);

        /*time = (TextView) view.findViewById(R.id.valid_for);
        validnow = (TextView) view.findViewById(R.id.valid_for1);
        bt1 = view.findViewById(R.id.validfsor);
        bt2 = view.findViewById(R.id.appCompatButton);
        bt3 = view.findViewById(R.id.appCompatButton2);
        submit = view.findViewById(R.id.appCompatButton4);*/



        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (veh1.getText().toString().trim().isEmpty()
                        || veh2.getText().toString().trim().isEmpty()
                        || veh3.getText().toString().trim().isEmpty()
                        || veh4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Empty HEHE", Toast.LENGTH_SHORT).show();
                    return;
                }
                String vehNO = veh1.getText().toString().trim()+
                        veh2.getText().toString().trim()+
                        veh3.getText().toString().trim()+
                        veh4.getText().toString().trim();

                Toast.makeText(getActivity(), vehNO, Toast.LENGTH_SHORT).show();

            }
        });*/


        /*DateFormat df = new SimpleDateFormat("HH:mm aa");
        String dateiii = df.format(Calendar.getInstance().getTime());

        DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
        String date2 = df2.format(Calendar.getInstance().getTime());

        date.setText(date2);
        time.setText(dateiii);*/


       /* bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
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
        });*/

       /* bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.bottom_sheet_timer,
                                (LinearLayout) view.findViewById(R.id.lin1));

                TimePicker dp = bottomSHeet.findViewById(R.id.timePicker);
                ImageView img1 = bottomSHeet.findViewById(R.id.wrongBtn);
                ImageView img2 = bottomSHeet.findViewById(R.id.rightBtn);

                img1.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
                            Toast.makeText(getActivity(), f12Hours.format(date), Toast.LENGTH_SHORT).show();
                            //time.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });








                bottomSHeet.findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(), "Hiiiii", Toast.LENGTH_SHORT).show();
                    }
                });
                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });*/

       /* bt1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.bottom_sheet_calender,
                                (LinearLayout) view.findViewById(R.id.lin));

                CalendarView dp = bottomSHeet.findViewById(R.id.datePicker);
                dp.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                        Toast.makeText(getActivity(), i2 + " " + i1 + " " + i, Toast.LENGTH_SHORT).show();
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
                        date.setText(i2 + " " + (monthname) + " " + i);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSHeet.findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(), "Hiiiii", Toast.LENGTH_SHORT).show();
                    }
                });
                bottomSheetDialog.setContentView(bottomSHeet);
                bottomSheetDialog.show();
            }
        });*/

        /*bt1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                //bottomSheetDialog.setCanceledOnTouchOutside(false);

                CalendarView calender =  bottomSheetDialog.findViewById(R.id.datePicker);

                calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                        Toast.makeText(getActivity(), i2+" "+i1+" "+i, Toast.LENGTH_SHORT).show();

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
                        date.setText(i2+" "+(monthname)+" "+i);
                        bottomSheetDialog.dismiss();

                    }
                });
                bottomSheetDialog.show();
            }
        });*/


        /*validnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroidi = LayoutInflater.from(getActivity());
                View mViewi = layoutInflaterAndroidi.inflate(R.layout.fragment_valid_for, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
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
                                //finish();
                                //System.exit(0);
                            }
                        });
                final AlertDialog dialog = alertDialogBuilderUserInput.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });*/

        /*date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog dp = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int currdate) {
                        String monthname = "";
                        if ((month + 1) == 1) {
                            monthname = "Jan";
                        } else if ((month + 1) == 1) {
                            monthname = "Jan";
                        } else if ((month + 1) == 2) {
                            monthname = "Feb";
                        } else if ((month + 1) == 3) {
                            monthname = "Mar";
                        } else if ((month + 1) == 4) {
                            monthname = "Apr";
                        } else if ((month + 1) == 5) {
                            monthname = "May";
                        } else if ((month + 1) == 6) {
                            monthname = "June";
                        } else if ((month + 1) == 7) {
                            monthname = "July";
                        } else if ((month + 1) == 8) {
                            monthname = "Aug";
                        } else if ((month + 1) == 9) {
                            monthname = "Sep";
                        } else if ((month + 1) == 10) {
                            monthname = "Oct";
                        } else if ((month + 1) == 11) {
                            monthname = "Nov";
                        } else if ((month + 1) == 12) {
                            monthname = "Dec";
                        }


                        date.setText(currdate + " " + (monthname) + " " + year);
                        Log.e("DSDas", "Date is : " + currdate + " / " + (month + 1) + " / " + year);
                    }
                }, mYear, mMonth, mDate);
                dp.show();
            }
        });*/

        /*time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tmp = new TimePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMin = i1;
                                String timee = tHour + ":" + tMin;
                                Log.e("Time", tHour + ":" + tMin);

                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(timee);

                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    time.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );
                tmp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tmp.updateTime(tHour, tMin);
                tmp.show();
            }
        });*/
        //Log.e("MESSGE", "Msg" + temp);
    }
}
