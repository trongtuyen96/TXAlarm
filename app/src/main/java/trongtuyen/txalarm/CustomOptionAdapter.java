package trongtuyen.txalarm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by USER on 29/11/2016.
 */

public class CustomOptionAdapter extends ArrayAdapter<String> {
    Context context;
    String[] options;
    String[] optName;


    public CustomOptionAdapter(Context context, int layoutToBeInflated, String[] options, String[] optName){
        super(context,R.layout.option_custom_list,options);
        this.context = context;
        this.options = options;
        this.optName = optName;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.option_custom_list,null);

        TextView label = (TextView) row.findViewById(R.id.staticText);
        TextView option = (TextView) row.findViewById(R.id.optionName);

        label.setText(options[position]);
        option.setText(optName[position]);

        return (row);
    }
}
