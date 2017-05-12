package trongtuyen.txalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by USER on 28/11/2016.
 */

public class CustomGameAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items;
    int PrevID = 100;

    public CustomGameAdapter(Context context, int layoutToBeInflated, String[] items){
        super(context, R.layout.game_custom_list,items);
        this.context = context;
        this.items = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.game_custom_list,null);

        TextView label = (TextView) row.findViewById(R.id.GameName);
        label.setText(items[position]);

        Button TryGame = (Button) row.findViewById(R.id.TryGame);
        //TryGame.setBackgroundResource(R.color.default_color);
        //TryGame.setBackgroundColor(Color.WHITE);
        if(position==getCount()-1)
        {
            TryGame.setVisibility(View.INVISIBLE);
        }
        else{
            TryGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                v.setBackgroundResource(R.color.default_color);
//                if(position!=PrevID){
//                    //v.setBackgroundResource(R.color.default_color);
//                    v.setBackgroundColor(Color.WHITE);
//                }
//                PrevID = position;
                    if(position==getCount()-2) {
                        Intent openGame = new Intent(context, Shake_Game.class);
                        //openGame.putExtra("GameID", position);
                        context.startActivity(openGame);
                    }
                    else {
                        Intent openGame = new Intent(context, trongtuyen.txalarm.Num_Order_Game.class);
                        openGame.putExtra("GameID", position);
                        context.startActivity(openGame);
                    }

                    //Toast.makeText(context,"Display game Activity",Toast.LENGTH_LONG).show();
                }
            });
        }

//        RadioButton MyCheck = (RadioButton) row.findViewById(R.id.GameCheck);
//        MyCheck.setChecked(position==PrevID);
//        MyCheck.setTag(position);
//        MyCheck.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                PrevID = (Integer)v.getTag();
//                notifyDataSetChanged();
//            }
//        });
        return (row);
    }
}
