package com.oortcloud.basemodule.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.oortcloud.basemodule.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @Company: 奥尔特云（深圳）智慧科技有限公司
 * @Author: lukezhang
 * @Date: 2022/10/28 15:51
 */
public class DateTimeDialog {
    private ICal1Back icb;


    public interface ICal1Back {
        void callback(long time);
    }
    public static void SetDateDialog(Context context, Activity activity, ICal1Back icb, String... title) {
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_datetime, null);
                final DatePicker datePicker = view1.findViewById(R.id.dialog_datetime_date_picker);
                final TimePicker timePicker = view1.findViewById(R.id.dialog_datetime_time_picker);
                timePicker.setIs24HourView(true);

                Calendar calendar;
//                String strDate = textView.getText().toString();
                calendar = Calendar.getInstance();
//                calendar = convertDateToCalendar(strDate);
                calendar.get(Calendar.YEAR);
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), null);

                timePicker.setVisibility(View.GONE);
//            datePicker.setCalendarViewShown(false);
                //设置Date布局
                builder.setView(view1);
                if (title != null && title.length > 0) {
                    builder.setTitle(title[0]);
                } else
                    builder.setTitle("选择日期");

                builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //日期格式
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int dayOfMonth = datePicker.getDayOfMonth();

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(year, month, dayOfMonth, 0, 0);
                        if (icb != null){
                            icb.callback(calendar1.getTimeInMillis());
                        }
//                        textView[0] =(String.format(Locale.getDefault(), "%d年%d月%d日 ", year, month, dayOfMonth));
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取 消", (dialog, which) -> dialog.cancel());
                /*builder.setNeutralButton("现 在", (dialog, i) -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.getDefault());// HH:mm:ss
                    //获取当前时间
                    Date date = new Date(System.currentTimeMillis());
                    textView.setText(simpleDateFormat.format(date));
                    dialog.cancel();
                });*/
                builder.create().show();
          //  }
        /*});*/

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void SetDateTimeDialog(Context context, Activity activity, ICal1Back icb, String... title) {
        /*textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {*/
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_datetime, null);
                final DatePicker datePicker = view1.findViewById(R.id.dialog_datetime_date_picker);
                final TimePicker timePicker = view1.findViewById(R.id.dialog_datetime_time_picker);
                timePicker.setIs24HourView(true);
//            datePicker.setCalendarViewShown(false);

                Calendar calendar = Calendar.getInstance(Locale.CHINA);
//                String strDate = textView.getText().toString();
//                calendar = convertDateToCalendar(strDate);
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), null);
                datePicker.setMinDate(System.currentTimeMillis());
                datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    }
                });
                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                    }
                });
                //设置Date布局
                builder.setView(view1);
                if (title != null && title.length > 0) {
                    builder.setTitle(title[0]);
                } else
                    builder.setTitle("选择时间");

                builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //日期格式
                            if (calendar.getTimeInMillis() < System.currentTimeMillis())
                            {
                                Toast.makeText(context, "必须设置将来的时间", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                if (icb != null){
                                    icb.callback(calendar.getTimeInMillis());
                                }
                                dialog.cancel();
                            }

                    }
                });
                builder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                /*builder.setNeutralButton("现 在", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日 HH:mm", Locale.getDefault());// HH:mm:ss
                        //获取当前时间
                        Date date = new Date(System.currentTimeMillis());
                        textView.setText(simpleDateFormat.format(date));
                        dialog.cancel();
                    }
                });*/
                builder.create().show();
       /*     }
        });*/
    }


    private static Calendar convertDateToCalendar(String strDate) {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        Calendar calendar = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }

        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.setTime(date);
        }
//        calendar.add(Calendar.MONTH,1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        if (strDate != null && !strDate.equals("")) {
            if (strDate.contains("：")) {
                strDate = strDate.split("：")[1];
            }


            strDate = strDate.replace("年", "-").replace("月", "-").replace("日", "").replace(".", "").replace(" ", "-").replace(":", "-");
            Log.d("liuwz", "convertDateToCalendar: "+strDate);
            if (strDate.split("-").length >= 3) {
                year = Integer.parseInt(strDate.split("-")[0]);
                month = Integer.parseInt(strDate.split("-")[1]);
                day = Integer.parseInt(strDate.split("-")[2]);
                if (strDate.split("-").length >= 5) {
                    hour = Integer.parseInt(strDate.split("-")[3]);
                    minute = Integer.parseInt(strDate.split("-")[4]);
                }
                calendar.set(year, month, day, hour, minute);
                calendar.add(Calendar.MONTH, -1);
            } else if (strDate.split("-").length >= 2) {
                hour = Integer.parseInt(strDate.split("-")[0]);
                minute = Integer.parseInt(strDate.split("-")[1]);
                calendar.set(year, month, day, hour, minute);
            }
        }

        return calendar;
    }
}
