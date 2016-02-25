package com.farm.bean;

/**
 * Created by ${shuwenouwan} on 2015/12/1115:31.
 * email：1655815015@qq.com
 * description:
 */
public class Gps
{
    private double wgLat;
    private double wgLon;

    public Gps(double wgLat, double wgLon)
    {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    public double getWgLat()
    {
        return wgLat;
    }

    public void setWgLat(double wgLat)
    {
        this.wgLat = wgLat;
    }

    public double getWgLon()
    {
        return wgLon;
    }

    public void setWgLon(double wgLon)
    {
        this.wgLon = wgLon;
    }

    @Override
    public String toString()
    {
        return wgLat + "," + wgLon;
    }
}
