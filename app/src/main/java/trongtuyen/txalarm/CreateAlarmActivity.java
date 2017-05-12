package trongtuyen.txalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import trongtuyen.txalarm.database.AlarmDatabase;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static trongtuyen.txalarm.R.id.btnDel;

public class CreateAlarmActivity extends AppCompatActivity {
    // Database working
    public static final int NEW_ALARM = -1;

    public static final String ID = "ID";
    public static final String EXTRA_ALARM = "ALARM";

    private AlarmDatabase db;
    private Context context;
    private List<Alarm> alarmList2Check = new ArrayList<>();
    private boolean bDuplicateTime = FALSE;

    //////////////////////////////////(XXX)///////////////////////////

    //class Alarm
    Alarm newAlarm;
    int id; //Alarm ID

    //For TimePicker
    private TimePicker timePicker1;
    private int hour;
    private int minute;
    private int MyStatus;

    //For list option
    String[] options = {"Game :", "Ringtone :"};
    String[] optName = {"None", "Ringtone01"};
    ListView OptionList;
    TextView ChooseGameName;
    TextView ChooseSoundName;
    CustomOptionAdapter adapter;

    //For Ringtone List
    static String[] ToneOptions = {"Ringtone01.mp3", "Ringtone02.mp3", "Ringtone03.mp3", "Ringtone04.mp3", "Use your list"};
    ListView SoundList;
    String FinalTone;
    int ToneID;
    int Flag; //Need afd for raw file in asset: 1 is need, 0 is no

    //For GameList
    static String[] GameName = {"Arrange number", "Find max-min number", "Simple Calculating", "Shaking Game","None"};
    ListView GameList;

    //For Volumn bar
    SeekBar alarm;
    AudioManager audioManager;
    final int DEFAULT_SET_VOL = 3;
    int DefaultVol;
    CheckBox DefaultCheck;
    float volume;

    //For buttons
    Button OK;
    Button Cancel;

    Button Delete;


    static final int TIME_DIALOG_ID = 999;

    //Request Code
    private static final int PlaylistRQCode = 111;
    //////////////////REMINDER////////////
//    FinalTone = "Ringtone01.mp3"; //Default ringtone name (for device playlist - can't trigger tone only by ID)
//    ToneID = 0; //Default ringtone01 - get id
//    Flag = 1; //Need afd for raw file in asset
//    DefaultVol = 3; //For the seekbar

    ///////////////////TUYEN/////////////////////////////
    private void getDataAlarm() {
        id = getIntent().getIntExtra(ID, NEW_ALARM);

        // not new alarm then find alarm from database by id of alarm
        if (id != NEW_ALARM) {
            String sql = "SELECT * FROM " + AlarmDatabase.TABLE_ALARM + " WHERE " + AlarmDatabase.ID_ALARM + " = " + id;

            newAlarm = db.getAlarm(sql);
        }

        if (newAlarm != null) {
            timePicker1.setCurrentHour((int) newAlarm.getHour());
            timePicker1.setCurrentMinute((int) newAlarm.getMin());
            //MyStatus = newAlarm.getStatus();
            MyStatus = 0;
            FinalTone = newAlarm.getSound();
            Flag = newAlarm.getFlag();
            DefaultVol = newAlarm.getPreVol();
            GameID = newAlarm.getGametype();
            optName[0] = GameName[GameID];
            optName[1] = newAlarm.getSound();
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            timePicker1.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker1.setCurrentMinute(calendar.get(Calendar.MINUTE));
            FinalTone = "Ringtone01.mp3"; //Default ringtone name (for device playlist - can't trigger tone only by ID)
            ToneID = 0; //Default ringtone01 - get id
            Flag = 1; //Need afd for raw file in asset
            DefaultVol = DEFAULT_SET_VOL; //For the seekbar
            MyStatus = 0; //Alarm off
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        ////// Set only portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView activityName = (TextView) findViewById(R.id.ActivityName);
        Button btnDelete = (Button) findViewById(R.id.btnDel);
        if (getIntent().getIntExtra("key", 2) == 0) {
            activityName.setText("CREATE ALARM");
            btnDelete.setVisibility(View.INVISIBLE);
        } else {
            activityName.setText("EDIT ALARM");
        }

        context = this;

        db = new AlarmDatabase(context);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);
        timePicker1.setCurrentHour(0);
        timePicker1.setCurrentMinute(0);

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
            }
        });

        ///////////////////////////////
        getDataAlarm();

        alarm = (SeekBar) findViewById(R.id.seekBar);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        alarm.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, DefaultVol, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, DefaultVol, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, DefaultVol*2, 0);
        alarm.setProgress(DefaultVol);
        DefaultCheck = (CheckBox) findViewById(R.id.DefaultCheck);
        volume = (float) (1 - (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) - DefaultVol) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM))));

        //When this is new Alarm not edited one
        if(DefaultVol==DEFAULT_SET_VOL)
        {
            DefaultCheck.setChecked(true);
        }
        DefaultCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //if(isChecked==true)
                //{
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, DEFAULT_SET_VOL, 0);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, DEFAULT_SET_VOL, 0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, DEFAULT_SET_VOL*2, 0);
                alarm.setProgress(DEFAULT_SET_VOL);
                volume = (float) (1 - (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) - DEFAULT_SET_VOL) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM))));
                //}
                // else {

                // }
            }
        });
        alarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i, AudioManager.FLAG_PLAY_SOUND);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, i, 0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i*2, 0);
                DefaultCheck.setChecked(false);
                volume = (float) (1 - (Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) - i) / Math.log(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM))));
                DefaultVol = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        OptionList = (ListView) findViewById(R.id.OptionList);
        adapter = new CustomOptionAdapter(this, R.layout.option_custom_list, options, optName);
        OptionList.setAdapter(adapter);
        OptionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                switch (position) {
                    case 0:
                        ShowGameDlg(v);
                        break;
                    case 1:
                        ShowRingtoneDlg(v);
                        break;
                    default:
                        adapter.notifyDataSetChanged();
                }


            }
        });
        Delete = (Button) findViewById(btnDel);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //////////////// TUYEN ///////////////////
                delete();
                /////////////(XXX)////////////////////////
                AlarmManager alarmManager;
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, newAlarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
        });

        OK = (Button) findViewById(R.id.btnOK);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if time is duplicated
                alarmList2Check.addAll(db.getAlarmList("SELECT * FROM "+AlarmDatabase.TABLE_ALARM));
                int hour = timePicker1.getCurrentHour();
                int minute = timePicker1.getCurrentMinute();
                bDuplicateTime = FALSE;
                for(int i=0;i<alarmList2Check.size();i++)
                {
                    if(hour == alarmList2Check.get(i).getHour() && minute == alarmList2Check.get(i).getMin() && id!=alarmList2Check.get(i).getId())
                    {
                        bDuplicateTime =TRUE;
                    }
                }

                if ( bDuplicateTime==FALSE) {
                    //Save bundle
                    Toast.makeText(CreateAlarmActivity.this, "Time set: " + hour + " : " + minute, Toast.LENGTH_LONG).show();
                    save();
                    finish();
                }
                else
                {
                    Toast.makeText(CreateAlarmActivity.this, "Time is duplicated. Please set your time again", Toast.LENGTH_LONG).show();
                }
            }
        });
        Cancel = (Button) findViewById(R.id.btnCancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Not save bundle
                //Toast.makeText(CreateAlarmActivity.this,"Time set: " + hour + " : " + minute,Toast.LENGTH_LONG).show();
//                Intent QuitAlarm = new Intent(CreateAlarmActivity.this,MainActivity.class);
//                startActivity(QuitAlarm);
                finish();
            }
        });

    }

    AssetFileDescriptor afd;
    MediaPlayer player;
    static int PrevPressPos;

    private void ShowRingtoneDlg(final View MyView) {

        final Dialog dialog = new Dialog(this);

        dialog.setTitle("Choose Ringtone");
        dialog.getWindow().setTitleColor(getResources().getColor(R.color.pressed_color));
        View view = getLayoutInflater().inflate(R.layout.sound_dialog, null);
        Button SoundOK = (Button) view.findViewById(R.id.SoundOK);
        Button SoundCancel = (Button) view.findViewById(R.id.SoundCancel);
        SoundList = (ListView) view.findViewById(R.id.SoundList);
        SoundList.setBackgroundColor(Color.WHITE);

        CustomSoundAdapter adapter2 = new CustomSoundAdapter(this, R.layout.ringtone_custom_list, ToneOptions);

        SoundList.setAdapter(adapter2);
        SoundList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //Open music
                try {
                    if (player == null) {
                        player = new MediaPlayer();
                    }
                    if (player.isPlaying() == true) {
                        player.stop();
                        player.release();
                        player = null;
                        player = new MediaPlayer();
                    }
                    if (position == (ToneOptions.length - 1)) {
                        Intent openPlaylist = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/external/audio/media"));
                        startActivityForResult(openPlaylist, PlaylistRQCode);
                    } else {
                        afd = getAssets().openFd(ToneOptions[position]);
                        player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        player.prepare();
                        player.setLooping(true);
                        player.setVolume(volume, volume);
                        player.start();
                        ToneID = position;
	                    FinalTone = ToneOptions[position];
                        Flag = 1;
                    }
                } catch (IOException e) {
                    //Log.e(...)
                }

                //Color check
                SoundList.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.default_color));

                if (position != PrevPressPos) {
                    //if(PrevPressPos!=-1) {
                        SoundList.getChildAt(PrevPressPos).setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    //}
                }
                PrevPressPos = position;
                //Toast.makeText(CreateAlarmActivity.this,"position"+ position,Toast.LENGTH_LONG).show();
            }
        });

        dialog.setContentView(view);
        dialog.show();

        SoundOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save sound
                optName[1] = ToneOptions[ToneID];
                ChooseSoundName = (TextView) MyView.findViewById(R.id.optionName);
                ChooseSoundName.setText(optName[1]);
                if (player == null) {
                    player = new MediaPlayer();
                }
                if (player.isPlaying() == true) {
                    player.stop();
                    player.release();
                    player = null;
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
                //Toast.makeText(CreateAlarmActivity.this,"Pos: " + pos,Toast.LENGTH_LONG).show();
            }
        });

        SoundCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Not save sound
                if (player == null) {
                    player = new MediaPlayer();
                }
                if (player.isPlaying() == true) {
                    player.stop();
                    player.release();
                    player = null;
                }
                dialog.dismiss();
            }

        });
    }

    int GameID = GameName.length - 1;
    static int PrevPressGameID;

    private void ShowGameDlg(final View MyView) {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Choose Game");
        dialog.getWindow().setTitleColor(getResources().getColor(R.color.pressed_color));

        View view = getLayoutInflater().inflate(R.layout.game_dialog, null);
        Button GameOK = (Button) view.findViewById(R.id.GameOK);
        Button GameCancel = (Button) view.findViewById(R.id.GameCancel);
        GameList = (ListView) view.findViewById(R.id.GameList);
        GameList.setBackgroundColor(Color.WHITE);

        CustomGameAdapter adapter3 = new CustomGameAdapter(this, R.layout.game_custom_list, GameName);

        GameList.setAdapter(adapter3);
        GameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //Game chosen
                GameID = position;
                GameList.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.default_color));

                if (position != PrevPressGameID) {
                    GameList.getChildAt(PrevPressGameID).setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                PrevPressGameID = position;
                //Toast.makeText(CreateAlarmActivity.this,"position"+ position,Toast.LENGTH_LONG).show();
            }
        });
        dialog.setContentView(view);
        dialog.show();

        GameOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save game type
                optName[0] = GameName[GameID];
                ChooseGameName = (TextView) MyView.findViewById(R.id.optionName);
                ChooseGameName.setText(optName[0]);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
                //Toast.makeText(CreateAlarmActivity.this,"Pos: " + pos,Toast.LENGTH_LONG).show();
            }
        });

        GameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Not save sound
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case (PlaylistRQCode): {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data == null) {
                            //Do something
                        } else {
                            //final String ringTonePath = data.getData().getEncodedPath();
                            Uri selectedSoundURI = data.getData();
                            if (player == null) {
                                player = new MediaPlayer();
                            }
                            if (player.isPlaying() == true) {
                                player.stop();
                                player.release();
                                player = null;
                                player = new MediaPlayer();
                            }
                            ToneID = ToneOptions.length - 1;
	                        FinalTone = getRealPathFromURI(selectedSoundURI);
                            player.setDataSource(FinalTone);
                            player.prepare();
                            player.setLooping(true);
                            player.setVolume(volume, volume);
                            player.start();
	                        Flag = 0;
                        }
                    } else {
//                        if(PrevPressPos==-1)
//                        {
//                            PrevPressPos = 0;
//                        }
                        //ToneID = PrevPressPos;
                        Toast.makeText(CreateAlarmActivity.this, "No sound selected! Return to last chosen Ringtone...", Toast.LENGTH_LONG).show();
                        SoundList.getChildAt(ToneID).setBackgroundColor(getResources().getColor(R.color.default_color));

                        if (ToneID != PrevPressPos) {
                            SoundList.getChildAt(PrevPressPos).setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        }
                        PrevPressPos = ToneID;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            //Log.e(...)
        }
    }// onActivityResult

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = 0;
            idx = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    ////////////////////////////////////// Tuyen //////////////////////////
    private void save() {

        int hour = timePicker1.getCurrentHour();
        int minute = timePicker1.getCurrentMinute();

        String noti = null;

//        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
        if (hour < 0 || hour > 24 || minute < 0 || minute > 59) {
            noti = "Nothing to save";
        } else {
            // new Alarm
            if (newAlarm == null) {
                Alarm newAlarm = new Alarm();
                newAlarm.setId(db.TotalLine("SELECT * FROM "+AlarmDatabase.TABLE_ALARM)).setHour(hour).setMin(minute).setStatus(MyStatus).setSound(FinalTone).setGameType(GameID).setPreVol(DefaultVol).setVolume(volume).setFlag(Flag);
                if (db.insertAlarm(newAlarm) > 0) {
                    noti = "Added";
                } else {
                    noti = "Add Failed";
                }
            } else { // update note
                newAlarm.setId(id).setHour(hour).setMin(minute).setStatus(MyStatus).setSound(FinalTone).setGameType(GameID).setPreVol(DefaultVol).setVolume(volume).setFlag(Flag);
                if (db.updateAlarm(newAlarm)) {
                    noti = "Updated";
                } else {
                    noti = "Update Failed";
                }
                AlarmManager alarmManager;
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, newAlarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
        }

        Toast.makeText(context, noti, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void delete() {
        int hour = timePicker1.getCurrentHour();
        int minute = timePicker1.getCurrentMinute();

//        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
//            finish();
        if (hour < 0 || hour > 24 || minute < 0 || minute > 59) {
            finish();
        } else {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Delete This Alarm").setIcon(R.drawable.ic_clock)
                    .setMessage("Delete ?");
            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAlarm();
                }
            });
            builder.create().show();
        }
    }

    private void deleteAlarm() {
        if (newAlarm != null) {
            String where = AlarmDatabase.ID_ALARM + " = " + newAlarm.getId();
            String noti = "Deleted";

            if (!db.deleteAlarm(where)) {
                noti = "Delete Failed";
            }
            Toast.makeText(context, noti, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
