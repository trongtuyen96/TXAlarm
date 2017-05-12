package trongtuyen.txalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import trongtuyen.txalarm.database.AlarmDatabase;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by trong on 29-Nov-16.
 */

public class CustomAlarmAdapter extends ArrayAdapter<Alarm> {
    Context context;
    List<Alarm> contentArr;
    AlarmDatabase db;

    ////(XXX)//////////
    AlarmManager alarmManager ;

    Intent myIntent;
    PendingIntent pendingIntent;
    public CustomAlarmAdapter(Context context, int layoutToBeInflated,List<Alarm> contentArr) {
        super(context, R.layout.alarm, contentArr);
        this.context = context;
        this.contentArr = contentArr;
        db = new AlarmDatabase(this.context);
        alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.alarm, null);

        // Set custom Font
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/alarmclock.ttf");
        TextView contentViewHourMin = (TextView) row.findViewById(R.id.textView);
        contentViewHourMin.setTypeface(myTypeface);

//        TextView contentView = (TextView) row.findViewById(R.id.textView);
//        contentView.setTypeface(Typeface.DEFAULT_BOLD);
        final Alarm alarm = contentArr.get(position);
        if ( alarm.getHour()>= 10 && alarm.getMin()>=10)
        {
            contentViewHourMin.setText(String.valueOf(alarm.getHour())+":" + String.valueOf(alarm.getMin()));
        }
        else
        {
            if(alarm.getHour()< 10 && alarm.getMin()>=10 ) {
                contentViewHourMin.setText("0" + String.valueOf(alarm.getHour()) + ":" + String.valueOf(alarm.getMin()));
            }
            if(alarm.getHour()< 10 && alarm.getMin()<10 ) {
                contentViewHourMin.setText("0" + String.valueOf(alarm.getHour()) + ":" + "0" + String.valueOf(alarm.getMin()));
            }
            if(alarm.getHour()>= 10 && alarm.getMin()<10 ) {
                contentViewHourMin.setText(String.valueOf(alarm.getHour()) + ":" + "0" + String.valueOf(alarm.getMin()));
            }
        }


        final Switch btnSwitch = (Switch) row.findViewById(R.id.switch2);
//        MyButton.setChecked(position==PrevID);
//        MyButton.setTag(position);

        // Button Switch
        if(alarm.getStatus() == 0)
        {
            btnSwitch.setChecked(false);
            //Delete an Alarm manager//
            //myIntent = new Intent(context, AlarmReceiver.class);
            //pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
        else
        {
            if(contentArr.get(position).getStatus() == 1)
            {
                btnSwitch.setChecked(true);
            }
        }

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String MyText = "Alarm ON ";
                    //Toast.makeText(context, MyText, Toast.LENGTH_SHORT).show();
                    alarm.setStatus(1);
                    db.updateAlarm(alarm);

                    //Alarm Manager here to invoke device alarm service//
                    myIntent = new Intent(context, AlarmReceiver.class);
                    myIntent.putExtra("ALARM_GAME_ID",alarm.getGametype());
                    myIntent.putExtra("ALARM_ID",alarm.getId());
                    //Toast.makeText(context,"ID " + alarm.getId(),Toast.LENGTH_LONG).show();

                    pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                    Calendar calendar = Calendar.getInstance();

                    int Now_Hour = new Time(System.currentTimeMillis()).getHours();
                    int Now_Min = new Time(System.currentTimeMillis()).getMinutes();
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                    calendar.set(Calendar.MINUTE, alarm.getMin());

                    if(alarm.getHour()<Now_Hour || (alarm.getHour()==Now_Hour && alarm.getMin()<Now_Min))
                    {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+24*60*60*1000, 5*60*1000, pendingIntent);
                    }
                    else{

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*60*1000, pendingIntent);
                    }


                } else {
                    String MyText = "Alarm OFF ";
                    //Toast.makeText(context, MyText, Toast.LENGTH_SHORT).show();
                    alarm.setStatus(0);
                    db.updateAlarm(alarm);

                    //Delete an Alarm manager//
                    //myIntent = new Intent(context, AlarmReceiver.class);
                    //pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);

                }

            }
        });

        ImageButton btnEdit = (ImageButton) row.findViewById(R.id.EditButton);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MyText = "Open Edit ";
                //Toast.makeText(context, MyText, Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(MainActivity,this, CreateAlarmActivity.class);
//                MainActivity.this.startActivity(myIntent);
                Intent intent = new Intent(context, CreateAlarmActivity.class);
                intent.putExtra("key", 1); //Optional parameters
                intent.putExtra(CreateAlarmActivity.ID, alarm.getId());
                context.startActivity(intent);
            }
        });
////            @Override
////            public void onClick(View v) {
////                PrevID = (Integer)v.getTag();
////                String MyText = "Position: " + position;
////                Toast.makeText(context, MyText, Toast.LENGTH_SHORT).show();
////                notifyDataSetChanged();
//                /**else
//                 {
//                 LayoutInflater inflater2 = ((Activity) context).getLayoutInflater();
//                 View row2 = inflater2.inflate(R.layout.custom_layout,null);
//                 RadioButton PrevButton = (RadioButton) row2.findViewById(PrevID);
//                 PrevButton.toggle();
//                 PrevID = R.id.MyCheck;
//                 }*/
//                //v.getVerticalScrollbarPosition();
//            }
//        });
        return (row);
    }
}
