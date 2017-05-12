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
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import trongtuyen.txalarm.database.AlarmDatabase;

/**
 * Created by USER on 9/12/2016.
 */

public class Num_Order_Game extends AppCompatActivity {

    private Context context;
    //Intent Extra Int ID
    public static final int ORDER_GAME = 0;
    public static final int MAXMIN_GAME = 1;
    public static final int CALC_GAME = 2;

    //For buttons
    Button Restart; //Restart the game
    Button Replay; //Play another game
    Button Done;
    Button Quit; //Will be invisible if it is for alarm on ring (appear in try version)

    Button Choice01;
    Button Choice02;
    Button Choice03;
    Button Choice04;

    //TextView
    TextView Instruction;
    TextView GameName;

    //Array and variable
    int[] GameNums = {0,0,0,0};
    int[] UserPush = {0,0,0,0};
    int NumPush = 0;
    int UserChoice; //For min max game
    Integer[] Arrange = {0,0,0,0};
    int InOrder;
    //Run from 0-3
    //if mod(InOrder) = 0 => ascending - find min game
    //if mod(InOrder) = 1 => descending - find max game
    //In range {0..3} is the position of real answer for Calculation Game

    int Operator; //For Calculating game - a simple 2-3 digit calculate
    //0 is +
    //1 is -
    //2 is x
    int RandAnsPos; //Random position for answer in Calculating game
    int FirstNum;
    int SecondNum;
    Random r; //For random picked number
    int Result; //For calc and min max


    //For the Alarm pack
    int Game_ID;
    int Alarm_ID;
    int numGame;
    int numGameDone; //number of game done
    int isAlarm;
    AlarmDatabase db;
    Alarm ThisAlarm;
    AssetFileDescriptor afd;
    MediaPlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_order_game);

        ////// Set only portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        context = this;

        Instruction = (TextView) findViewById(R.id.Instruct);
        GameName = (TextView) findViewById(R.id.ActivityGameName);

        Restart = (Button) findViewById(R.id.GameRestart);
        Replay = (Button) findViewById(R.id.GameReplay);
        Done = (Button) findViewById(R.id.GameDone);

        Quit = (Button) findViewById(R.id.GameQuit);
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Return Create Alarm Activity
                finish();
            }
        });

        Choice01 = (Button) findViewById(R.id.NUM_01);
        Choice02 = (Button) findViewById(R.id.NUM_02);
        Choice03 = (Button) findViewById(R.id.NUM_03);
        Choice04 = (Button) findViewById(R.id.NUM_04);

        //////////////////////(XXX)//////////////////////////////
        Game_ID = getIntent().getIntExtra("GameID",-1);
        isAlarm = getIntent().getIntExtra("isAlarm",-1);

        if(isAlarm == 1)
        {
            Alarm_ID = getIntent().getIntExtra("ThisAlarmID",-1);
            numGame = getIntent().getIntExtra("NumOfGame",-1);
            numGameDone = 0;

            Replay.setVisibility(View.INVISIBLE);
            Quit.setVisibility(View.INVISIBLE);

            db = new AlarmDatabase(context);
            //Toast.makeText(context,"ID"+Alarm_ID,Toast.LENGTH_LONG).show();
            String sql = "SELECT * FROM " + AlarmDatabase.TABLE_ALARM + " WHERE " + AlarmDatabase.ID_ALARM + " = " + Alarm_ID;
            ThisAlarm = db.getAlarm(sql);
//            Toast.makeText(context,"flag"+ThisAlarm.getFlag(),Toast.LENGTH_LONG).show();
            //Play music
            if(ThisAlarm.getFlag() == 1) //Need afd
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
            }
            else
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
                    .setMessage("Play game 3 times to turn off the alarm.");
            builder.create().show();

            //OpenGame
            CreateGamePack();
            if (Game_ID == ORDER_GAME) {
                OrderNumGamePack();
            } else {
                if (Game_ID == MAXMIN_GAME) {
                    MinMaxGamePack();
                } else {
                    if (Game_ID == CALC_GAME) {
                        CalcGamePack();
                    }
                    else{
                        finish();
                    }
                }
            }

        }
        else {

            //Button try game
            CreateGamePack();
            if (Game_ID == ORDER_GAME) {
                OrderNumGamePack();
                Replay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateGamePack();
                        ResetPack();
                        OrderNumGamePack();
                    }
                });
            } else {
                if (Game_ID == MAXMIN_GAME) {
                    MinMaxGamePack();
                    Replay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CreateGamePack();
                            ResetPack();
                            MinMaxGamePack();
                        }
                    });
                } else {
                    if (Game_ID == CALC_GAME) {
                        CalcGamePack();
                        Replay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CreateGamePack();
                                ResetPack();
                                CalcGamePack();
                            }
                        });
                    }
                }
            }
        }
    }

//    public int[] addElement(int[] a, int e) {
//        a  = Arrays.copyOf(a, a.length + 1);
//        a[a.length - 1] = e;
//        return a;
//    }

    public void CreateGamePack()
    {
        Done.setEnabled(false);
        Replay.setEnabled(false);
        Restart.setEnabled(false);
        NumPush = 0;

        for(int i = 0; i<GameNums.length; i++)
        {
            r = new Random();
            GameNums[i] = r.nextInt(100 - 1) + 1;
            Arrange[i] = Integer.valueOf(GameNums[i]);
        }
        r = new Random();
        InOrder = r.nextInt(4 - 0) + 0;

        r = new Random();
        Operator = r.nextInt(3 - 0) + 0; //For Calculating game - a simple 2-3 digit calculate

        if(Operator!=2) {
            r = new Random();
            FirstNum = r.nextInt(100 - 1) + 1;
            r = new Random();
            SecondNum = r.nextInt(100 - 1) + 1;
        }
        else{
            if(Operator==2){
                r = new Random();
                FirstNum = r.nextInt(10 - 1) + 1;
                r = new Random();
                SecondNum = r.nextInt(10 - 1) + 1;
            }
        }

        //Arrange in order
        if( (InOrder%2) == 0)
        {
            //Ascending game
            Arrays.sort(Arrange, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer x, Integer y)
                {
                    return x - y;
                }
            });
        }
        if( (InOrder%2) == 1)
        {
            //Descending game
            Arrays.sort(Arrange, new Comparator<Integer>()
            {
                @Override
                public int compare(Integer x, Integer y)
                {
                    return y - x;
                }
            });
        }
        Result = -1;
        UserChoice = -1;

    }

    public void ResetPack()
    {
        Done.setEnabled(false);
        Replay.setEnabled(false);
        Restart.setEnabled(false);

        Choice01.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg));
        Choice01.setEnabled(true);
        Choice02.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg));
        Choice02.setEnabled(true);
        Choice03.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg));
        Choice03.setEnabled(true);
        Choice04.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg));
        Choice04.setEnabled(true);

        NumPush = 0;
        UserChoice = -1;
        //UserPush = null;
        //UserPush = new int[0];
    }

    public void OrderNumGamePack()
    {
        if((InOrder%2)==0)
        {
            //Ascending
            Instruction.setText("Press numbers in ASCENDING order");
            GameName.setText("Arrange number");

        }
        if((InOrder%2)==1)
        {
            //Descending
            Instruction.setText("Press numbers in DESCENDING order");
            GameName.setText("Arrange number");

        }
        Choice01.setText(Integer.toString(GameNums[0]));
        Choice02.setText(Integer.toString(GameNums[1]));
        Choice03.setText(Integer.toString(GameNums[2]));
        Choice04.setText(Integer.toString(GameNums[3]));

        Choice01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice01.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                UserPush[NumPush] = GameNums[0];
                NumPush = NumPush + 1;
                //addElement(UserPush, GameNums[0]);
                Restart.setEnabled(true);
                if(NumPush == GameNums.length)
                {
                    Done.setEnabled(true);
                }
            }
        });
        Choice02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice02.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice02.setEnabled(false);
                UserPush[NumPush] = GameNums[1];
                NumPush = NumPush + 1;
                //addElement(UserPush, GameNums[1]);
                Restart.setEnabled(true);
                if(NumPush == GameNums.length)
                {
                    Done.setEnabled(true);
                }
            }
        });
        Choice03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice03.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice03.setEnabled(false);
                UserPush[NumPush] = GameNums[2];
                NumPush = NumPush + 1;
                //addElement(UserPush, GameNums[2]);
                Restart.setEnabled(true);
                if(NumPush == GameNums.length)
                {
                    Done.setEnabled(true);
                }
            }
        });
        Choice04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice04.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice04.setEnabled(false);
                UserPush[NumPush] = GameNums[3];
                NumPush = NumPush + 1;
                //addElement(UserPush, GameNums[3]);
                Restart.setEnabled(true);
                if(NumPush == GameNums.length)
                {
                    Done.setEnabled(true);
                }
            }
        });

        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ResetPack();
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Done.setEnabled(false);
                boolean flag_win = true;
                for(int i = 0; i<GameNums.length;i++)
                {
                    if(UserPush[i]!=Arrange[i].intValue())
                    {
                        flag_win = false;
                    }
                }

                if(flag_win==true)
                {
                    if(isAlarm==1)
                    {
                        numGameDone = numGameDone + 1;
                        if(numGameDone==numGame)
                        {
                            if (player == null) {
                                player = new MediaPlayer();
                            }
                            if (player.isPlaying() == true) {
                                player.stop();
                                player.release();
                                player = null;
                            }
                            Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            MyVibrate.vibrate(500);

                            ThisAlarm.setStatus(0);
                            db.updateAlarm(ThisAlarm);

                            AlarmManager alarmManager;
                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent myIntent = new Intent(context, AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ThisAlarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
//                            Intent GoBack = new Intent(context,MainActivity.class);
//                            context.startActivity(GoBack);
                            finish();
                        }
                        else
                        {
                            CreateGamePack();
                            ResetPack();
                            OrderNumGamePack();
                        }
                    }
                    else {
                        Restart.setEnabled(false);
                        Replay.setEnabled(true);
                        Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        MyVibrate.vibrate(500);
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Game Result").setIcon(R.mipmap.ic_launcher)
                                .setMessage("Congratulation! You win!");
                        builder.create().show();
                    }

                }
                else{
                    Restart.setEnabled(true);
                    Replay.setEnabled(false);
                    Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    MyVibrate.vibrate(500);
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Game Result").setIcon(R.mipmap.ic_launcher)
                            .setMessage("Too bad! Wrong answer!");
                    builder.create().show();

                }
            }
        });
    }

    public void MinMaxGamePack()
    {
        if((InOrder%2)==0)
        {
            //Ascending - Min number
            Instruction.setText("Find the MIN number");
            GameName.setText("Find max-min number");
            Result = Arrange[0];

        }
        if((InOrder%2)==1)
        {
            //Descending - Max number
            Instruction.setText("Find the MAX number");
            GameName.setText("Find max-min number");
            Result = Arrange[0];

        }
        Choice01.setText(Integer.toString(GameNums[0]));
        Choice02.setText(Integer.toString(GameNums[1]));
        Choice03.setText(Integer.toString(GameNums[2]));
        Choice04.setText(Integer.toString(GameNums[3]));

        Choice01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice01.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[0];
                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });
        Choice02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice02.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[1];

                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });

        Choice03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice03.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[2];

                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });
        Choice04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice04.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[3];

                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });

        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPack();
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Done.setEnabled(false);

                if(Result==UserChoice)
                {
                    if(isAlarm==1)
                    {
                        numGameDone = numGameDone + 1;
                        if(numGameDone==numGame)
                        {
                            if (player == null) {
                                player = new MediaPlayer();
                            }
                            if (player.isPlaying() == true) {
                                player.stop();
                                player.release();
                                player = null;
                            }
                            Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            MyVibrate.vibrate(500);

                            ThisAlarm.setStatus(0);
                            db.updateAlarm(ThisAlarm);

                            AlarmManager alarmManager;
                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent myIntent = new Intent(context, AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ThisAlarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
//                            Intent GoBack = new Intent(context,MainActivity.class);
//                            context.startActivity(GoBack);
                            finish();
                        }
                        else
                        {
                            CreateGamePack();
                            ResetPack();
                            MinMaxGamePack();
                        }
                    }
                    else
                    {
                        Restart.setEnabled(false);
                        Replay.setEnabled(true);
                        Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        MyVibrate.vibrate(500);
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Game Result").setIcon(R.mipmap.ic_launcher)
                                .setMessage("Congratulation! You win!");
                        builder.create().show();
                    }
                }
                else{
                    Restart.setEnabled(true);
                    Replay.setEnabled(false);
                    Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    MyVibrate.vibrate(500);
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Game Result").setIcon(R.mipmap.ic_launcher)
                            .setMessage("Too bad! Wrong answer!");
                    builder.create().show();
                }
            }
        });

    }

    public void CalcGamePack()
    {
        GameName.setText("Simple Calculating");

        if(Operator==0)
        {
            Result = FirstNum + SecondNum;
            Instruction.setText(FirstNum + " + " + SecondNum + " = ? ");
        }
        if(Operator==1)
        {
            Result = FirstNum - SecondNum;
            Instruction.setText(FirstNum + " - " + SecondNum + " = ? ");
        }
        if(Operator==2)
        {
            Result = FirstNum * SecondNum;
            Instruction.setText(FirstNum + " x " + SecondNum + " = ? ");
        }

        r = new Random();
        RandAnsPos = r.nextInt(4 - 0) + 0;
        GameNums[RandAnsPos] = Result;

        Choice01.setText(Integer.toString(GameNums[0]));
        Choice02.setText(Integer.toString(GameNums[1]));
        Choice03.setText(Integer.toString(GameNums[2]));
        Choice04.setText(Integer.toString(GameNums[3]));

        Choice01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice01.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[0];
                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });

        Choice02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice02.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[1];
                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });

        Choice03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice03.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[2];
                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });

        Choice04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice04.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pressed));
                Choice01.setEnabled(false);
                Choice02.setEnabled(false);
                Choice03.setEnabled(false);
                Choice04.setEnabled(false);

                UserChoice = GameNums[3];
                Restart.setEnabled(true);
                Done.setEnabled(true);

            }
        });

        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPack();
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Done.setEnabled(false);

                if(Result==UserChoice)
                {
                    if(isAlarm==1)
                    {
                        numGameDone = numGameDone + 1;
                        if(numGameDone==numGame)
                        {
                            if (player == null) {
                                player = new MediaPlayer();
                            }
                            if (player.isPlaying() == true) {
                                player.stop();
                                player.release();
                                player = null;
                            }
                            Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            MyVibrate.vibrate(500);

                            ThisAlarm.setStatus(0);
                            db.updateAlarm(ThisAlarm);

                            AlarmManager alarmManager;
                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent myIntent = new Intent(context, AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ThisAlarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
//                            Intent GoBack = new Intent(context,MainActivity.class);
//                            context.startActivity(GoBack);
                            finish();
                        }
                        else
                        {
                            CreateGamePack();
                            ResetPack();
                            CalcGamePack();
                        }
                    }
                    else
                    {
                        Restart.setEnabled(false);
                        Replay.setEnabled(true);
                        Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        MyVibrate.vibrate(500);
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Game Result").setIcon(R.mipmap.ic_launcher)
                                .setMessage("Congratulation! You win!");
                        builder.create().show();
                    }
                }
                else{
                    Restart.setEnabled(true);
                    Replay.setEnabled(false);
                    Vibrator MyVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    MyVibrate.vibrate(500);
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Game Result").setIcon(R.mipmap.ic_launcher)
                            .setMessage("Too bad! Wrong answer!");
                    builder.create().show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
