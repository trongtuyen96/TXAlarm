package trongtuyen.txalarm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by trong on 08-Dec-16.
 */

public class CustomCityAdapter extends ArrayAdapter<City>{
    Context context;
    List<City> listCity;
    int PrevID = 100;
    TimerTask tmTask;
    Calendar calendar;
    private Handler customHandler = new Handler();
    private int iPos;
    private TextView tvHour;


    public CustomCityAdapter(Context context, int layoutToBeInflated,List<City> listCity){
        super(context,R.layout.city, listCity);
        this.context = context;
        this.listCity = listCity;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.city,null);

        ///// Fonts
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/alarmclock.ttf");

        TextView tvName = (TextView) row.findViewById(R.id.cityName);
        TextView tvCountry = (TextView) row.findViewById(R.id.textCountry);
        TextView tvGMT = (TextView)row.findViewById(R.id.textViewGMT);
        TextView tvDay = (TextView)row.findViewById(R.id.textViewDay);
        ImageView icon = (ImageView) row.findViewById(R.id.imageIcon);
        tvHour = (TextView) row.findViewById(R.id.cityHour);

        tvName.setText(listCity.get(position).getName());
        tvCountry.setText(listCity.get(position).getCountry());
        tvName.setText(listCity.get(position).getName());
        if (listCity.get(position).getGMT() > 0 ) {
            tvGMT.setText("GMT +" + listCity.get(position).getGMT());
        }else{
            tvGMT.setText("GMT " + listCity.get(position).getGMT());
        }
        icon.setImageResource(listCity.get(position).getIdFlag());

        // can use calender get Timezone to advanced function
            calendar = Calendar.getInstance();
            int iNowHour = calendar.get(Calendar.HOUR_OF_DAY);
            int iNowMin = calendar.get(Calendar.MINUTE);
            int tmpHour;
            if ( listCity.get(position).getGMT() > 7) {
                tmpHour = listCity.get(position).getGMT() - 7 + iNowHour;
                tvDay.setText("Same Day");
                if(tmpHour >= 24)
                {   tmpHour = tmpHour-24;
                    tvDay.setText("Next Day");
                }
            }
            else
            {
                tmpHour =  24 + iNowHour - (7 - listCity.get(position).getGMT());
                tvDay.setText("Same Day");
                if(tmpHour >= 24)
                    tmpHour = tmpHour-24;
                if((iNowHour - (7 - listCity.get(position).getGMT()))<0)
                {
                    tvDay.setText("Previous Day");
                }
            }
        tvHour.setTypeface(myTypeface);
        if ( tmpHour>= 10 && iNowMin>=10)
        {
            tvHour.setText(String.valueOf(tmpHour)+":" + String.valueOf(iNowMin));
        }
        else
        {
            if(tmpHour< 10 && iNowMin>=10 ) {
                tvHour.setText("0" + String.valueOf(tmpHour) + ":" + String.valueOf(iNowMin));
            }
            if(tmpHour< 10 && iNowMin<10 ) {
                tvHour.setText("0" + String.valueOf(tmpHour) + ":" + "0" + String.valueOf(iNowMin));
            }
            if(tmpHour>= 10 && iNowMin<10 ) {
                tvHour.setText(String.valueOf(tmpHour) + ":" + "0" + String.valueOf(iNowMin));
            }
        }

//      iPos = position;
        /////// Refresh after 1 second
//        customHandler.postDelayed(updateTimerThread, 1000);

        return (row);
    }

    /// Too many threads make app run very slow
//    private Runnable updateTimerThread = new Runnable() {
//        public void run() {
//            ///////// need Timer
//            // can use calender get Timezone to advanced function
//            calendar = Calendar.getInstance();
//            int iNowHour = calendar.get(Calendar.HOUR);
//            int iNowMin = calendar.get(Calendar.MINUTE);
//            int tmpHour;
//            if ( listCity.get(iPos).getGMT() > 7) {
//                tmpHour = listCity.get(iPos).getGMT() - 7 + iNowHour;
//            }
//            else
//            {
//                tmpHour =  iNowHour - (7 - listCity.get(iPos).getGMT());
//            }
//            ///// Fonts
//            Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/alarmclock.ttf");
//
//            tvHour.setTypeface(myTypeface);
//            tvHour.setText(String.valueOf(tmpHour) + ":"+ String.valueOf(iNowMin));
//
//            customHandler.postDelayed(this, 0);
//        }
//    };
}
