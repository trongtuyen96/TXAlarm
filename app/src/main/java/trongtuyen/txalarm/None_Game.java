package trongtuyen.txalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;

import trongtuyen.txalarm.database.AlarmDatabase;

/**
 * Created by USER on 16/12/2016.
 */

public class None_Game extends AppCompatActivity {

    private Context context;

    Button Quit;
    Button Delay;
    TextView Clock;
    private Handler customHandler = new Handler();
    Calendar calendar;

    //For the Alarm pack
    int Alarm_ID;
    int isAlarm;
    AlarmDatabase db;
    Alarm ThisAlarm;
    AssetFileDescriptor afd;
    MediaPlayer player;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.none_game_bg);

        ////// Set only portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;

        Quit = (Button) findViewById(R.id.none_game_quit);
        Delay = (Button) findViewById(R.id.none_game_delay);
        Clock = (TextView) findViewById(R.id.Clock);

        ////Music part
        isAlarm = getIntent().getIntExtra("isAlarm",-1);
        Alarm_ID = getIntent().getIntExtra("ThisAlarmID", -1);


        if(isAlarm == 1) {

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
            Quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

//                    Intent GoBack = new Intent(context,MainActivity.class);
//                    context.startActivity(GoBack);
                    finish();
                }
            });

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


        ////Clock part
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (hour >= 10 && min >= 10) {
            Clock.setText(String.valueOf(hour) + ":" + String.valueOf(min));
        } else {
            if (hour < 10 && min >= 10) {
                Clock.setText("0" + String.valueOf(hour) + ":" + String.valueOf(min));
            }
            if (hour < 10 && min < 10) {
                Clock.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(min));
            }
            if (hour >= 10 && min < 10) {
                Clock.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(min));
            }
        }
        customHandler.postDelayed(updateTimerThread, 1000);


    }
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);


            if (hour >= 10 && min >= 10) {
                Clock.setText(String.valueOf(hour) + ":" + String.valueOf(min));
            } else {
                if (hour < 10 && min >= 10) {
                    Clock.setText("0" + String.valueOf(hour) + ":" + String.valueOf(min));
                }
                if (hour < 10 && min < 10) {
                    Clock.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(min));
                }
                if (hour >= 10 && min < 10) {
                    Clock.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(min));
                }
            }
            customHandler.postDelayed(this, 0);
        }
    };

    //Prevent user press back button
    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
