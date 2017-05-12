package trongtuyen.txalarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.support.annotation.Nullable;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by trong on 29-Nov-16.
 */

public class Fragment_4 extends Fragment implements View.OnClickListener{
    private SeekBar seekBrightness;
    private int iPercent;
    private int iLandscape = 0;
    private Handler customHandler2 = new Handler();
    private Intent myIntent;
    private ViewPager mViewPager;
    private Button okColor;
    // selected color
    private int selectedColorR;
    private int selectedColorG;
    private int selectedColorB;
    private ImageView ivColoBack;
    private Switch btnSwitchType;

    int[] mResources = {
            R.drawable.back_1_m,
            R.drawable.back_2_m,
            R.drawable.back_3_m,
            R.drawable.back_4_m,
            R.drawable.back_5_m,
            R.drawable.back_6_m
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Color picker
        // default color = black
        int defaultColorR=0;
        int defaultColorG=0;
        int defaultColorB=0;
        final ColorPicker cp = new ColorPicker(getActivity(), defaultColorR, defaultColorG, defaultColorB);
        Button btnSleepClock = (Button) view.findViewById(R.id.btnSleepClock);
        final CheckBox check = (CheckBox)view.findViewById(R.id.checkBox);
        ivColoBack=(ImageView)view.findViewById(R.id.imageViewColorBack);

        // Intent to open sleep clock
        myIntent= new Intent(getActivity(), SleepingClock.class);

        // default
        check.setChecked(TRUE);
        myIntent.putExtra("Brightness", 20);
        myIntent.putExtra("Type",0);
        myIntent.putExtra("BackgroundID", 0);
        myIntent.putExtra("Landscape mode",1);

        btnSleepClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myIntent.putExtra("background",position);
                if( check.isChecked())
                {
                    iLandscape = 1;
                }
                else{
                    iLandscape = 0;}
                myIntent.putExtra("Landscape mode",iLandscape);
                startActivity(myIntent);
            }
        }

        );

        // Set from automatic brightness to mannual
        try {
            int brightnessMode = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Exception e) {
            // do something useful

        }

        final TextView tvPercent= (TextView)view.findViewById(R.id.textViewPercentage);


        seekBrightness= (SeekBar)view.findViewById(R.id.seekBar2);
        tvPercent.setText(String.valueOf(seekBrightness.getProgress()*100/255)+"%");
        seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<=20)
                {
                    myIntent.putExtra("Brightness", 20);
                    iPercent=20*100/255;
                    tvPercent.setText(String.valueOf(iPercent)+"%");
//                    screenBrightness(20);
                }
                else{
                    myIntent.putExtra("Brightness", progress);
                    iPercent=progress*100/255;
                    tvPercent.setText(String.valueOf(iPercent)+"%");
//                    screenBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnSwitchType = (Switch)view.findViewById(R.id.switchPC);
        btnSwitchType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mViewPager.setVisibility(View.INVISIBLE);
                    cp.show();
                    // On Click listener for the dialog, when the user select the color
                    okColor = (Button) cp.findViewById(R.id.okColorButton);
                    ivColoBack.setVisibility(View.VISIBLE);

                    okColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Get single channel (value 0-255)
                            selectedColorR = cp.getRed();
                            selectedColorG = cp.getGreen();
                            selectedColorB = cp.getBlue();
                            myIntent.putExtra("intRed", selectedColorR);
                            myIntent.putExtra("intGreen", selectedColorG);
                            myIntent.putExtra("intBlue", selectedColorB);
                            ivColoBack.setBackgroundColor(Color.rgb(selectedColorR,selectedColorG,selectedColorB));

//        // Or the android RGB Color (see the android Color class reference)
//                selectedColorRGB = cp.getColor();
                            cp.dismiss();
                        }
                    });

                    myIntent.putExtra("Type",1);
                }
                else
                {
                    mViewPager.setVisibility(View.VISIBLE);
                    cp.hide();
                    ivColoBack.setVisibility(View.INVISIBLE);
                    myIntent.putExtra("Type",0);
                }
            }
        });

        // Pager Background Images
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this.getContext(),mResources);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        customHandler2.postDelayed(updateTimerThread, 300);

    }


    @Override
    public void onClick(View v) {

    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            myIntent.putExtra("BackgroundID", mViewPager.getCurrentItem());
            customHandler2.postDelayed(this, 0);
        }
    };

    // When user leave this tab, turn switch off
    @Override
    public void onPause() {
        super.onPause();
        btnSwitchType.setChecked(FALSE);
    }
}