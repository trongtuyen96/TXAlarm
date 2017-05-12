package trongtuyen.txalarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by USER on 15/12/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
//        MainActivity.getTextView2().setText("Enough Rest. Do Work Now!");
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
//        ringtone.play();

        int GameID = intent.getIntExtra("ALARM_GAME_ID", -1);
        int AlarmID = intent.getIntExtra("ALARM_ID",-1);
        //Toast.makeText(context,"Get in Alarm Receiver ID: " + AlarmID,Toast.LENGTH_LONG).show();
        if(GameID!=4 && GameID!=3 && GameID!=-1) {
            Intent CallGame = new Intent(context, Num_Order_Game.class);
            CallGame.putExtra("GameID", GameID);
            CallGame.putExtra("ThisAlarmID", AlarmID);
            CallGame.putExtra("NumOfGame", 3);
            CallGame.putExtra("isAlarm", 1);
            CallGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(CallGame);
        }
        else{
            //Toast.makeText(context,"Get in Alarm Receiver ID: " + AlarmID,Toast.LENGTH_LONG).show();
            if(GameID!=-1 && GameID!=3) {
                Intent CallGame2 = new Intent(context, None_Game.class);
                CallGame2.putExtra("ThisAlarmID", AlarmID);
                CallGame2.putExtra("isAlarm", 1);
                CallGame2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(CallGame2);
            }
            else{
                Intent CallGame3 = new Intent(context, Shake_Game.class);
                CallGame3.putExtra("ThisAlarmID", AlarmID);
                CallGame3.putExtra("isAlarm", 1);
                CallGame3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(CallGame3);
            }
        }
    }
}
