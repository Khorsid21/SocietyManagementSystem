package com.example.societymanagmentsystem;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static com.android.volley.Request.Method.POST;

public class fragment_cab_once extends Fragment {
    Button selDate,selTime,selVal,Submit;
    EditText veh1, veh2, veh3, veh4;
    int btn_id = -1;
    private static final String UPLOAD_URL=  "https://vivek.ninja/AllowCab/add_cab_once.php";
    String userName,userEmail,userRole,userPhone,userSociety,userCity,userBlock;
    SharedPreferences sharedPreferences;
    public static final String fileName = "USER_PROFILE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_cab_once,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selDate = view.findViewById(R.id.oSelDateBtn);
        selTime = view.findViewById(R.id.oSelTimeBtn);
        selVal = view.findViewById(R.id.oValBtn);
        Submit = view.findViewById(R.id.oSubmit);
        veh1 = view.findViewById(R.id.inputVehNo1);
        veh2 = view.findViewById(R.id.inputVehNo2);
        veh3 = view.findViewById(R.id.inputVehNo3);
        veh4 = view.findViewById(R.id.inputVehNo4);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        userPhone= sharedPreferences.getString("phoneno", "");
        userRole = sharedPreferences.getString("role", "");
        userName = sharedPreferences.getString("name", "");
        userEmail = sharedPreferences.getString("email", "");
        userSociety = sharedPreferences.getString("society", "");
        userBlock = sharedPreferences.getString("block", "");
        userCity = sharedPreferences.getString("city", "");



        veh1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    veh2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        veh2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    veh3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        veh3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    veh4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        DateFormat df = new SimpleDateFormat("hh:mm aa");
        String currTime = df.format(Calendar.getInstance().getTime());

        DateFormat df2 = new SimpleDateFormat("d MMM yyyy");
        String currDate = df2.format(Calendar.getInstance().getTime());

        selDate.setText(currDate);
        selTime.setText(currTime);
        selVal.setText("1 Hour");


        selVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(),
                        R.style.BottomThemeDialogTheme
                );
                /*View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.fragment_valid_for,
                                (CoordinatorLayout) view.findViewById(R.id.ll2));*/
                View bottomSHeet = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.fragment_valid_for,
                                (LinearLayout)view.findViewById(R.id.ll2));

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
                                selVal.setText(btn_id+" Hour");
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
                                selVal.setText(btn_id+" Hours");
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
                                selVal.setText(btn_id+" Hours");
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
                                selVal.setText(btn_id+" Hours");
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
                                selVal.setText(btn_id+" Hours");
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
                                selVal.setText(btn_id+" Hours");
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

        selTime.setOnClickListener(new View.OnClickListener() {
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
                            selTime.setText(f12Hours.format(date));
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

        selDate.setOnClickListener(new View.OnClickListener() {
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
                        //Toast.makeText(getActivity(), i2 + " " + i1 + " " + i, Toast.LENGTH_SHORT).show();
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
                        selDate.setText(i2 + " " + (monthname) + " " + i);
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

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (veh1.getText().toString().trim().isEmpty()
                        || veh2.getText().toString().trim().isEmpty()
                        || veh3.getText().toString().trim().isEmpty()
                        || veh4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Vehicle No.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String vehNO = veh1.getText().toString().trim()+
                        veh2.getText().toString().trim()+
                        veh3.getText().toString().trim()+
                        veh4.getText().toString().trim();


                ////

                StringRequest sr = new StringRequest(POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject js = jsonArray.getJSONObject(0);
                                    Log.e("ResponseFull",response);
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
                        map.put("datekey",selDate.getText().toString());
                        map.put("timekey",selTime.getText().toString());
                        map.put("valkey",selVal.getText().toString());
                        map.put("vehkey",vehNO);
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


                //Log.e("Details",userName+"-"+userEmail+"-"+userPhone+"-"+userRole+"-"+userCity+"-"+userSociety+"-"+userBlock+"-"+vehNO+"-"+selVal.getText().toString()+"-"+selDate.getText().toString()+"-"+selTime.getText().toString());
                //Toast.makeText(getActivity(), vehNO, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
