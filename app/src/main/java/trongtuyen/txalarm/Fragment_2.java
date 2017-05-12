package trongtuyen.txalarm;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by trong on 27-Nov-16.
 */

public class Fragment_2 extends Fragment implements View.OnClickListener {

    Integer[] thumbnails = {R.drawable.argentina0, R.drawable.australia1, R.drawable.belgium2, R.drawable.brazil3, R.drawable.cambodja4, R.drawable.canada5
            , R.drawable.chile6, R.drawable.china7, R.drawable.colombia8, R.drawable.costarica9, R.drawable.croatia10, R.drawable.cuba11
            , R.drawable.denmark12, R.drawable.ecuador13, R.drawable.egypt14, R.drawable.france15, R.drawable.germany16, R.drawable.greece17
            , R.drawable.honduras18, R.drawable.india19, R.drawable.indonezia20, R.drawable.italy21, R.drawable.jamaica22, R.drawable.japan23
            , R.drawable.laos24, R.drawable.mexico25, R.drawable.netherlands26, R.drawable.newzealand27, R.drawable.nigeria28, R.drawable.peru29
            , R.drawable.philippines30, R.drawable.portugal31, R.drawable.qatar32, R.drawable.russianfederation33, R.drawable.singapore34, R.drawable.southkorea35
            , R.drawable.spain36, R.drawable.sweden37, R.drawable.switzerland38, R.drawable.taiwan39, R.drawable.thailand40, R.drawable.turkey41
            , R.drawable.ukraine42, R.drawable.unitedarabemirates43, R.drawable.unitedkingdom44, R.drawable.uruguay45, R.drawable.usa46, R.drawable.uzbekistan47
            , R.drawable.venezuela48, R.drawable.vietnam49};

    private Context context;
    private CustomCityAdapter cityAdapter;
    private List<City> listCity = new ArrayList<>();
    Calendar calendar;
    private Handler customHandler = new Handler();
    private TextView contentViewCurDate;
    private TextView contentViewCurHour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // flag, gmt, name, country
        City Temp = new City(thumbnails[27], -11, "Niue", "New Zealand");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -10, "Hawaii", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -10, "Alaska", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[5], -8, "British Col", "Canada");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -8, "Washington", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -8, "California", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[25], -7, "Chihuahua", "Mexico");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -7, "Kansas", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -7, "Texas/Utah", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[9], -6, "San Jose", "Costa Rica");
        listCity.add(Temp);
        Temp = new City(thumbnails[18], -6, "Tegucigalpa", "Honduras");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -6, "Florida", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -6, "Indiana", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -6, "Michigan", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[3], -5, "Amazonas", "Brazil");
        listCity.add(Temp);
        Temp = new City(thumbnails[8], -5, "Bogota", "Colombia");
        listCity.add(Temp);
        Temp = new City(thumbnails[11], -5, "Havana", "Cuba");
        listCity.add(Temp);
        Temp = new City(thumbnails[13], -5, "Quito", "Ecuador");
        listCity.add(Temp);
        Temp = new City(thumbnails[22], -5, "Kingston", "Jamaica");
        listCity.add(Temp);
        Temp = new City(thumbnails[25], -5, "Quintana Roo", "Mexico");
        listCity.add(Temp);
        Temp = new City(thumbnails[29], -5, "Lima", "Peru");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -5, "New York", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -5, "Ohio", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], -5, "Virginia", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[6], -4, "Santiago", "Chile");
        listCity.add(Temp);
        Temp = new City(thumbnails[12], -4, "Greenland", "Denmark");
        listCity.add(Temp);
        Temp = new City(thumbnails[15], -4, "Martinique", "France");
        listCity.add(Temp);
        Temp = new City(thumbnails[15], -4, "Saint-Martin", "France");
        listCity.add(Temp);
        Temp = new City(thumbnails[26], -4, "Aruba", "Netherlands");
        listCity.add(Temp);
        Temp = new City(thumbnails[26], -4, "Sint Maarten", "Netherlands");
        listCity.add(Temp);
        Temp = new City(thumbnails[48], -4, "Caracas", "Venezuela");
        listCity.add(Temp);
        Temp = new City(thumbnails[0], -3, "Buenos Aires", "Argentina");
        listCity.add(Temp);
        Temp = new City(thumbnails[45], -3, "Montevideo", "Uruguay");
        listCity.add(Temp);
        Temp = new City(thumbnails[44], -2, "South Georgia", "United Kingdom");
        listCity.add(Temp);
        Temp = new City(thumbnails[36], -1, "Azores Islands", "Spain");
        listCity.add(Temp);
        Temp = new City(thumbnails[31], 0, "Madeira", "Portugal");
        listCity.add(Temp);
        Temp = new City(thumbnails[36], 0, "Canary Islands", "Spain");
        listCity.add(Temp);
        Temp = new City(thumbnails[44], 0, "Guernsey", "United Kingdom");
        listCity.add(Temp);
        Temp = new City(thumbnails[44], 0, "Jersey", "United Kingdom");
        listCity.add(Temp);
        Temp = new City(thumbnails[2], 1, "Brussels", "Belgium");
        listCity.add(Temp);
        Temp = new City(thumbnails[10], 1, "Zagreb", "Croatia");
        listCity.add(Temp);
        Temp = new City(thumbnails[12], 1, "Copenhagen", "Denmark");
        listCity.add(Temp);
        Temp = new City(thumbnails[15], 1, "Paris", "France");
        listCity.add(Temp);
        Temp = new City(thumbnails[16], 1, "Berlin", "Germany");
        listCity.add(Temp);
        Temp = new City(thumbnails[21], 1, "Rome", "Italy");
        listCity.add(Temp);
        Temp = new City(thumbnails[26], 1, "Amsterdam", "Netherlands");
        listCity.add(Temp);
        Temp = new City(thumbnails[28], 1, "Abuja", "Nigeria");
        listCity.add(Temp);
        Temp = new City(thumbnails[37], 1, "Stockholm", "Sweden");
        listCity.add(Temp);
        Temp = new City(thumbnails[38], 1, "Bern", "Switzerland");
        listCity.add(Temp);
        Temp = new City(thumbnails[14], 2, "Cairo", "Egypt");
        listCity.add(Temp);
        Temp = new City(thumbnails[17], 2, "Athens", "Greece");
        listCity.add(Temp);
        Temp = new City(thumbnails[42], 2, "Kiev", "Ukraine");
        listCity.add(Temp);
        Temp = new City(thumbnails[32], 3, "Doha", "Qatar");
        listCity.add(Temp);
        Temp = new City(thumbnails[33], 3, "Central", "Rusia");
        listCity.add(Temp);
        Temp = new City(thumbnails[41], 3, "Ankara", "Turkey");
        listCity.add(Temp);
        Temp = new City(thumbnails[42], 3, "Donetsk", "Ukraine");
        listCity.add(Temp);
        Temp = new City(thumbnails[15], 4, "Southen", "France");
        listCity.add(Temp);
        Temp = new City(thumbnails[43], 4, "Abu Dhabi", "United Arab Emirates");
        listCity.add(Temp);
        Temp = new City(thumbnails[1], 5, "Heard Islands", "Australia");
        listCity.add(Temp);
        Temp = new City(thumbnails[1], 5, "McDonald Isl", "Australia");
        listCity.add(Temp);
        Temp = new City(thumbnails[47], 5, "Tashkent", "Uzbekistan");
        listCity.add(Temp);
        Temp = new City(thumbnails[33], 6, "Siberian Federal ", "Rusia");
        listCity.add(Temp);
        Temp = new City(thumbnails[4], 7, "Phnom Penh", "Combodia");
        listCity.add(Temp);
        Temp = new City(thumbnails[24], 7, "Vientiane", "Laos");
        listCity.add(Temp);
        Temp = new City(thumbnails[40], 7, "Bangkok", "Thailand");
        listCity.add(Temp);
        Temp = new City(thumbnails[49], 7, "Ha Noi", "Viet Nam");
        listCity.add(Temp);
        Temp = new City(thumbnails[49], 7, "Ho Chi Minh", "Viet Nam");
        listCity.add(Temp);
        Temp = new City(thumbnails[40], 7, "Bangkok", "Thailand");
        listCity.add(Temp);
        Temp = new City(thumbnails[1], 8, "Western Aus", "Australia");
        listCity.add(Temp);
        Temp = new City(thumbnails[7], 8, "Beijing", "China");
        listCity.add(Temp);
        Temp = new City(thumbnails[20], 8, "Kalimantan", "Indonesia");
        listCity.add(Temp);
        Temp = new City(thumbnails[20], 8, "Sunda Islands", "Indonesia");
        listCity.add(Temp);
        Temp = new City(thumbnails[30], 8, "Manila", "Philippines");
        listCity.add(Temp);
        Temp = new City(thumbnails[34], 8, "Singapore", "Singapore");
        listCity.add(Temp);
        Temp = new City(thumbnails[23], 9, "Tokyo", "Japan");
        listCity.add(Temp);
        Temp = new City(thumbnails[35], 9, "Seoul", "South Korea");
        listCity.add(Temp);
        Temp = new City(thumbnails[33], 9, "Far Eastern", "Rusia");
        listCity.add(Temp);
        Temp = new City(thumbnails[1], 10, "Queenland", "Australia");
        listCity.add(Temp);
        Temp = new City(thumbnails[1], 10, "Victoria", "Australia");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], 10, "Guam", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[46], 10, "Mariana Islands", "United States");
        listCity.add(Temp);
        Temp = new City(thumbnails[1], 11, "Norfolk Island", "Australia");
        listCity.add(Temp);
        Temp = new City(thumbnails[27], 12, "Wellington", "New Zealand");
        listCity.add(Temp);

        context = this.getContext();
        cityAdapter = new CustomCityAdapter(this.getContext(), R.layout.city, listCity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView list1 = (ListView) view.findViewById(R.id.rv_city);
        list1.setAdapter(cityAdapter);

        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/alarmclock.ttf");
        contentViewCurHour = (TextView) view.findViewById(R.id.textViewCurHour);
        contentViewCurDate = (TextView) view.findViewById(R.id.textViewCurDate);
        contentViewCurHour.setTypeface(myTypeface);
        /////// Refresh after 1 second
        customHandler.postDelayed(updateTimerThread, 1000);
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
            contentViewCurDate.setText(sMonth + ", " + String.valueOf(date) + ", " + String.valueOf(year));
            if ( hour>= 10 && min>=10)
            {
                contentViewCurHour.setText(String.valueOf(hour)+":" + String.valueOf(min));
            }
            else
            {
                if(hour< 10 && min>=10 ) {
                    contentViewCurHour.setText("0" + String.valueOf(hour) + ":" + String.valueOf(min));
                }
                if(hour< 10 && min<10 ) {
                    contentViewCurHour.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(min));
                }
                if(hour>= 10 && min<10 ) {
                    contentViewCurHour.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(min));
                }
            }
            customHandler.postDelayed(this, 0);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
