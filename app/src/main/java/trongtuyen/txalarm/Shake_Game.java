package trongtuyen.txalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;

import trongtuyen.txalarm.database.AlarmDatabase;

/**
 * Created by trong on 21-Dec-16.
 */

public class Shake_Game extends AppCompatActivity {
    private Context context;
    public int iCount = 0;
    private ProgressBar proBarVer;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private TextView tvShake;

    Button Delay;
    Button Quit;

    //For the Alarm pack
    int Alarm_ID;
    int isAlarm;
    AlarmDatabase db;
    Alarm ThisAlarm;
    AssetFileDescriptor afd;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_game);

        ////// Set only portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;

        Quit = (Button) findViewById(R.id.shake_quit);
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Delay = (Button) findViewById(R.id.shake_delay);

        ////Music part
        isAlarm = getIntent().getIntExtra("isAlarm",-1);
        Alarm_ID = getIntent().getIntExtra("ThisAlarmID", -1);


        if(isAlarm == 1) {

            Quit.setVisibility(View.INVISIBLE);

            db = new AlarmDatabase(context);
            String sql = "SELECT * FROM " + AlarmDatabase.TABLE_ALARM + " WHERE " + AlarmDatabase.ID_ALARM + " = " + Alarm_ID;
            ThisAlarm = db.getAlarm(sql);
//            Toast.makeText(context,"flag"+ThisAlarm.getFlag(),Toast.LENGTH_LONG).show();
            //Play music
            if (ThisAlarm.getFlag() == 1) //Need afd
            {

                if (player == null) {
                    player = new MediaPlayer();
                }
                if (player.isPlaying() == true) {
                    player.stop();
                    player.release();
                    player = null;
                    player = new MediaPlayer();
                }
                try {
                    afd = getAssets().openFd(ThisAlarm.getSound());
                    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    player.prepare();
                    player.setLooping(true);
                    player.setVolume(ThisAlarm.getVol(), ThisAlarm.getVol());
                    player.start();

                } catch (IOException e) {
                    //Log.e(...)
                }
            } else {
                if (player == null) {
                    player = new MediaPlayer();
                }
                if (player.isPlaying() == true) {
                    player.stop();
                    player.release();
                    player = null;
                    player = new MediaPlayer();
                }
                try {
                    player.setDataSource(ThisAlarm.getSound());
                    player.prepare();
                    player.setLooping(true);
                    player.setVolume(ThisAlarm.getVol(), ThisAlarm.getVol());
                    player.start();
                } catch (IOException e) {
                    //Log.e(...)
                }
            }

            //Show instruction
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            String CurrentHour, CurrentMin;
            if(new Time(System.currentTimeMillis()).getHours()<10)
            {
                CurrentHour = "0" + new Time(System.currentTimeMillis()).getHours();
            }
            else
            {
                CurrentHour = "" + new Time(System.currentTimeMillis()).getHours();
            }
            if(new Time(System.currentTimeMillis()).getMinutes()<10)
            {
                CurrentMin = "0" + new Time(System.currentTimeMillis()).getMinutes();
            }
            else
            {
                CurrentMin = "" + new Time(System.currentTimeMillis()).getMinutes();
            }
            builder.setTitle("ALARM: "+  CurrentHour + " : " +  CurrentMin).setIcon(R.drawable.ic_clock)
                    .setMessage("Shake your phone to turn off the alarm.");
            builder.create().show();

            Delay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Delay this alarm").setIcon(R.drawable.ic_clock)
                            .setMessage("Delay this alarm for 5 minutes?");
                    builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    });
                    builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (player == null) {
                                player = new MediaPlayer();
                            }
                            if (player.isPlaying() == true) {
                                player.stop();
                                player.release();
                                player = null;
                            }
                            finish();
                        }
                    });
                    builder.create().show();
                }
            });
        }
        else{
            Delay.setVisibility(View.INVISIBLE);
        }




        proBarVer = (ProgressBar)findViewById(R.id.shake_vertical_bar);
        tvShake=(TextView)findViewById(R.id.textViewShake);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                iCount++;
                proBarVer.setProgress(iCount);
                tvShake.setText(iCount*2+"%");
                if(iCount*2>=100)
                {
                    Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    MyVibrate.vibrate(500);
                    Toast.makeText(context, "Game finished", Toast.LENGTH_SHORT).show();
                    if(isAlarm == 1){
                        if (player == null) {
                            player = new MediaPlayer();
                        }
                        if (player.isPlaying() == true) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                        ThisAlarm.setStatus(0);
                        db.updateAlarm(ThisAlarm);

                        AlarmManager alarmManager;
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent = new Intent(context, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ThisAlarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);

//                        Intent GoBack = new Intent(context,MainActivity.class);
//                        context.startActivity(GoBack);
                        finish();
                    }
                    else {
                        finish();
                    }
                }
//                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
