package trongtuyen.txalarm;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import static trongtuyen.txalarm.R.id.textView2;
import static trongtuyen.txalarm.R.id.textView3;

/**
 * Created by trong on 11-Dec-16.
 */

public class SleepingClock extends AppCompatActivity {

    private int background_por[] = {R.drawable.back_1, R.drawable.back_2, R.drawable.back_3, R.drawable.back_4, R.drawable.back_5, R.drawable.back_6};
    private int background_lan[] = {R.drawable.back_1_l, R.drawable.back_2_l, R.drawable.back_3_l, R.drawable.back_4_l, R.drawable.back_5_l, R.drawable.back_6_l};
    Calendar calendar;
    private Handler customHandler = new Handler();
    private TextView tv3;
    private TextView tv2;
    private Button buttonBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.sleeping_clock);

        // Really hard to find out why
        LayoutInflater mInflater = LayoutInflater.from(this);
        View contentView = mInflater.inflate(R.layout.sleeping_clock, null);
        RelativeLayout layoutCus = (RelativeLayout) contentView.findViewById(R.id.sleeping_clock);// mContainerIconExtension in your case
        int iLandMode = 0;
        if (getIntent().getIntExtra("Landscape mode", -1) == 1) {
            iLandMode = 1;
            ////// Set only landscape
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            iLandMode = 0;
            ////// Set only portrait
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(getIntent().getIntExtra("Type",-1)==0) {
            switch (getIntent().getIntExtra("BackgroundID", -1)) {
                case 0:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_1);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_1_m);
                    break;
                case 1:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_2);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_2_m);
                    break;
                case 2:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_3);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_3_m);
                    break;
                case 3:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_4);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_4_m);
                    break;
                case 4:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_5);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_5_m);
                    break;
                case 5:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_6);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_6_m);
                    break;
                default:
                    if (iLandMode == 0) {
                        layoutCus.setBackgroundResource(R.drawable.back_4);
                    } else
                        layoutCus.setBackgroundResource(R.drawable.back_4_m);
                    break;
            }
        }
        else
        {
            int iRed = getIntent().getIntExtra("intRed",-1);
            int iBlue = getIntent().getIntExtra("intBlue",-1);
            int iGreen = getIntent().getIntExtra("intGreen",-1);
            layoutCus.setBackgroundColor(Color.rgb(iRed,iGreen,iBlue));
        }
        // Set brightness
        if (getIntent().getIntExtra("Brightness", -1) <= 20)
        {
            screenBrightness(20);
        }
        else{
            screenBrightness(getIntent().getIntExtra("Brightness", -1));
        }
        setContentView(layoutCus);


        tv3 = (TextView) layoutCus.findViewById(textView3);
        tv2 = (TextView) layoutCus.findViewById(textView2);
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String sMonth = "";
        switch (month) {
            case 1:
                sMonth = "January";
                break;
            case 2:
                sMonth = "February";
                break;
            case 3:
                sMonth = "March";
                break;
            case 4:
                sMonth = "April";
                break;
            case 5:
                sMonth = "May";
                break;
            case 6:
                sMonth = "June";
                break;
            case 7:
                sMonth = "July";
                break;
            case 8:
                sMonth = "August";
                break;
            case 9:
                sMonth = "September";
                break;
            case 10:
                sMonth = "October";
                break;
            case 11:
                sMonth = "November";
                break;
            case 12:
                sMonth = "December";
                break;

        }
        tv2.setText(sMonth + ", " + String.valueOf(date) + ", " + String.valueOf(year));
        if (hour >= 10 && min >= 10) {
            tv3.setText(String.valueOf(hour) + ":" + String.valueOf(min));
        } else {
            if (hour < 10 && min >= 10) {
                tv3.setText("0" + String.valueOf(hour) + ":" + String.valueOf(min));
            }
            if (hour < 10 && min < 10) {
                tv3.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(min));
            }
            if (hour >= 10 && min < 10) {
                tv3.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(min));
            }
        }
        customHandler.postDelayed(updateTimerThread, 1000);

        buttonBack = (Button) layoutCus.findViewById(R.id.button3);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String sMonth = "";
            switch (month) {
                case 1:
                    sMonth = "January";
                    break;
                case 2:
                    sMonth = "February";
                    break;
                case 3:
                    sMonth = "March";
                    break;
                case 4:
                    sMonth = "April";
                    break;
                case 5:
                    sMonth = "May";
                    break;
                case 6:
                    sMonth = "June";
                    break;
                case 7:
                    sMonth = "July";
                    break;
                case 8:
                    sMonth = "August";
                    break;
                case 9:
                    sMonth = "September";
                    break;
                case 10:
                    sMonth = "October";
                    break;
                case 11:
                    sMonth = "November";
                    break;
                case 12:
                    sMonth = "December";
                    break;

            }
            tv2.setText(sMonth + ", " + String.valueOf(date) + ", " + String.valueOf(year));
            if (hour >= 10 && min >= 10) {
                tv3.setText(String.valueOf(hour) + ":" + String.valueOf(min));
            } else {
                if (hour < 10 && min >= 10) {
                    tv3.setText("0" + String.valueOf(hour) + ":" + String.valueOf(min));
                }
                if (hour < 10 && min < 10) {
                    tv3.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(min));
                }
                if (hour >= 10 && min < 10) {
                    tv3.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(min));
                }
            }
            customHandler.postDelayed(this, 0);
        }
    };


    private void screenBrightness(double newBrightnessValue) {
    /*
     * WindowManager.LayoutParams settings = getWindow().getAttributes();
     * settings.screenBrightness = newBrightnessValue;
     * getWindow().setAttributes(settings);
     */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        float newBrightness = (float) newBrightnessValue;
        lp.screenBrightness = newBrightness / (float) 255;
        getWindow().setAttributes(lp);
    }
}
