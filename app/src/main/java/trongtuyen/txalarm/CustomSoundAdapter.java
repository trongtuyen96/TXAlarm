package trongtuyen.txalarm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by USER on 28/11/2016.
 */

public class CustomSoundAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items;
    int PrevID = 100;
    //SharedPreferences prefs;

    public CustomSoundAdapter(Context context, int layoutToBeInflated, String[] items){
        super(context,R.layout.ringtone_custom_list,items);
        this.context = context;
        this.items = items;
        //prefs = context.getSharedPreferences("Position", MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.ringtone_custom_list,null);

        TextView label = (TextView) row.findViewById(R.id.SoundName);
        label.setText(items[position]);

        //RadioButton MyCheck = (RadioButton) row.findViewById(R.id.SoundCheck);
        //MyCheck.setChecked(position==PrevID);
//        MyCheck.setTag(position);
//        MyCheck.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                PrevID = (Integer)v.getTag();
//                //Toast.makeText(context,"Pos: " + position,Toast.LENGTH_LONG).show();
//
////                Intent intent = new Intent(context,CreateAlarmActivity.class);
////                intent.putExtra("Choosen Position", position);
////                context.startActivity(intent);
//
//                notifyDataSetChanged();
//
//            }
//        });
        //edit.putInt("pos",position);
        //edit.commit();
        return (row);
    }

}
