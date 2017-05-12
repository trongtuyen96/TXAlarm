package trongtuyen.txalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import trongtuyen.txalarm.database.AlarmDatabase;

/**
 * Created by trong on 27-Nov-16.
 */

public class Fragment_1 extends Fragment implements View.OnClickListener{
    private CustomAlarmAdapter alarmAdapter;
    private List<Alarm> alarmList = new ArrayList<>();
    private Context context;
    private AlarmDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();
        db = new AlarmDatabase(context);
        alarmList.addAll(db.getAlarmList("SELECT * FROM "+AlarmDatabase.TABLE_ALARM));
        alarmAdapter = new CustomAlarmAdapter(context,R.layout.alarm, alarmList);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView list = (ListView) view.findViewById(R.id.rv_note);
        list.setAdapter(alarmAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showAlarm(context, CreateAlarmActivity.NEW_ALARM);

                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        updateAlarmList();
    }

    private void updateAlarmList() {
        // clear old list
        alarmList.clear();
        // add all notes from database, reverse list
        ArrayList<Alarm> ls = db.getAlarmList("SELECT * FROM " + AlarmDatabase.TABLE_ALARM);

        // reverse list
        for (int i = ls.size() - 1; i >= 0; i--) {
            alarmList.add(ls.get(i));
        }

        alarmAdapter.notifyDataSetChanged();
    }

    public static void showAlarm(Context context, long id) {
        Intent intent = new Intent(context, CreateAlarmActivity.class);

        // send id to NoteActivity
        intent.putExtra(CreateAlarmActivity.ID, id);

        context.startActivity(intent);
    }
}
