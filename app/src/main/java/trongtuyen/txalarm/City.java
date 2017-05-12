package trongtuyen.txalarm;

import java.io.Serializable;

/**
 * Created by trong on 08-Dec-16.
 */

public class City implements Serializable {
    private int idFlag;
    private int GMT;
    private String name;
    private String country;

    public City(int flag, int gmt, String namecity, String namecountry)
    {
        this.idFlag=flag;
        this.GMT=gmt;
        this.name=namecity;
        this.country=namecountry;
    }

    public int getIdFlag(){return idFlag;}
    public int getGMT(){return GMT;}
    public String getName(){return name;}
    public String getCountry(){return country;}

    public City setIdFlag(int idFlag){
        this.idFlag = idFlag;
        return this;
    }
    public City setGMT(int GMT){
        this.GMT = GMT;
        return this;
    }
    public City setName(String name){
        this.name = name;
        return this;
    }
    public City setCountry(String country){
        this.country = country;
        return this;
    }
}
