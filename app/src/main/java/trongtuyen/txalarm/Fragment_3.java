package trongtuyen.txalarm;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by trong on 29-Nov-16.
 */


public class Fragment_3 extends Fragment implements View.OnClickListener {

    private Button bMin;
    private Button bSec;
    private Button bStart;
    private Button bStop;
    private TextView tvMin;
    private TextView tvSec;
    private NumberPicker etMin;
    private NumberPicker etSec;
    private ProgressBar proBar;
    private ProgressBar proBar1;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds; // total count down time in milliseconds
    private long timeBlinkInMilliseconds; // start time of start blinking
    private boolean blink; // controls the blinking .. on and off
    private boolean bRunning;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bStart = (Button) view.findViewById(R.id.buttonStartCd);
        bStop = (Button) view.findViewById(R.id.buttonStop);
        tvMin = (TextView) view.findViewById(R.id.tevMin);
        tvSec = (TextView) view.findViewById(R.id.tevSec);
        etMin=(NumberPicker)view.findViewById(R.id.editMin);
        etSec=(NumberPicker)view.findViewById(R.id.editSec);
        proBar=(ProgressBar)view.findViewById(R.id.progressbar_timerview) ;
        proBar1=(ProgressBar)view.findViewById(R.id.progressbar1_timerview) ;
        etMin.setMaxValue(59);
        etMin.setMinValue(0);
        etSec.setMaxValue(59);
        etSec.setMinValue(0);
        bStart.setOnClickListener(this);
        bStop.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStartCd) {
            setTimer();
            //Hides the Keyboard
//            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
//                    Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(editMin.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(editSec.getWindowToken(), 0);
            bStop.setVisibility(View.VISIBLE);
            bStart.setVisibility(View.INVISIBLE);
            etMin.setVisibility(View.GONE);
            etSec.setVisibility(View.GONE);
            proBar.setVisibility(View.INVISIBLE);
            startTimer();
            proBar1.setVisibility(View.VISIBLE);

        } else if (v.getId() == R.id.buttonStop) {
            countDownTimer.cancel();
            bStart.setVisibility(View.VISIBLE);
            bStop.setVisibility(View.INVISIBLE);
            etMin.setVisibility(View.VISIBLE);
            etSec.setVisibility(View.VISIBLE);
            tvMin.setVisibility(View.INVISIBLE);
            tvSec.setVisibility(View.INVISIBLE);
            proBar1.setVisibility(View.GONE);
            proBar.setVisibility(View.VISIBLE);
        }
    }

    private void setTimer() {
        int min = 0;
        int sec = 0;
        min=etMin.getValue();
        sec=etSec.getValue();
        totalTimeCountInMilliseconds = ((60 * min) + sec) * 1000;
        timeBlinkInMilliseconds = 30 * 1000;
        int total = ((60 * min) + sec) * 1000;
        proBar1.setMax(total);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1) {
            // 500 means, onTick function will be called at every 1 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                tvMin.setVisibility(View.VISIBLE);
                tvSec.setVisibility(View.VISIBLE);
//                //Setting the Progress Bar to decrease wih the timer

                proBar1.setProgress((int) (leftTimeInMilliseconds));
                tvMin.setTextColor(getResources().getColor(R.color.pressed_color));
                tvSec.setTextColor(getResources().getColor(R.color.pressed_color));
                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                    tvMin.setTextColor(Color.RED);
                    tvSec.setTextColor(Color.RED);
                }
                if(seconds / 60 <10)
                {
                tvMin.setText("0"+String.valueOf(seconds / 60));}
                else{
                    tvMin.setText(String.valueOf(seconds / 60));
                }
                if(seconds%60 <10) {
                    tvSec.setText("0"+String.valueOf(seconds % 60));
                }else
                {
                    tvSec.setText(String.valueOf(seconds % 60));
                }
                // format the textview to show the easily readable format
                bRunning = TRUE;
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
//                textViewShowTime.setText("Time up!");
                final MediaPlayer player = new MediaPlayer();
                AssetFileDescriptor afd;
                try {
                    afd = getContext().getAssets().openFd("Ringtone02.mp3");
                    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    player.setAudioStreamType(AudioManager.STREAM_ALARM);
                    player.prepare();
                } catch (IOException e) {
                    //Log.e(...)
                }
                player.start();
                bStart.setText("TIME UP !!!!");
                bStart.setBackgroundColor(Color.rgb(255,140,0));
                bStart.setVisibility(View.VISIBLE);
                // wait 5 sec... then stop the player.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        player.stop();
                        bStart.setText("START");
                        bStart.setBackgroundColor(getResources().getColor(R.color.pressed_color));
                    }
                }, 2500);

                tvMin.setVisibility(View.INVISIBLE);
                tvSec.setVisibility(View.INVISIBLE);
                bStart.setVisibility(View.VISIBLE);
                bStop.setVisibility(View.GONE);
                etMin.setVisibility(View.VISIBLE);
                etSec.setVisibility(View.VISIBLE);
                proBar.setVisibility(View.VISIBLE);
                proBar1.setVisibility(View.GONE);
                bRunning=FALSE;
            }
        }.start();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(bRunning)
        {
            tvMin.setVisibility(View.VISIBLE);
            tvSec.setVisibility(View.VISIBLE);
            bStart.setVisibility(View.INVISIBLE);
            bStop.setVisibility(View.VISIBLE);
            etMin.setVisibility(View.INVISIBLE);
            etSec.setVisibility(View.INVISIBLE);
        }
    }
}




