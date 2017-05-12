package trongtuyen.txalarm;

import java.io.Serializable;

/**
 * Created by trong on 27-Nov-16.
 */

public class Alarm implements Serializable {
    private int id;
    private String content;
    private int hour;
    private int minute;
    private int gametype;
    private int status;
    private int days[];
    private String soundName; //Name of ringtone
    private int PreVol; //int data for seekbar
    private float Volume; //float data to set media player volume
    private int flag_afd; //for Ringtone type raw in assets file


    public int getId(){return id;}
    public int getHour(){return hour;}
    public int getMin(){return minute;}
    public int getGametype(){return gametype;}
    public int getStatus(){return status;}
    public int[] getDays(){return days;}
    public String getSound(){return soundName;}
    public int getPreVol(){return PreVol;}
    public float getVol(){return Volume;}
    public int getFlag(){return flag_afd;}


    public Alarm setId(int id){
        this.id = id;
        return this;
    }

    public Alarm setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public Alarm setMin(int min) {
        this.minute = min;
        return this;
    }

    public Alarm setGameType(int type) {
        this.gametype = type;
        return this;
    }

    public Alarm setStatus(int status){
        this.status=status;
    return this;
    }

    public Alarm setDays(int[] days){
        this.days=days;
        return this;
    }

    public Alarm setSound(String name) {
        this.soundName = name;
        return this;
    }

    public Alarm setPreVol(int Vol) {
        this.PreVol = Vol;
        return this;
    }

    public Alarm setVolume(float Volume) {
        this.Volume = Volume;
        return this;
    }

    public Alarm setFlag(int Flag) {
        this.flag_afd = Flag;
        return this;
    }


}
