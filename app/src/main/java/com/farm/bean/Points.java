package com.farm.bean;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "Points")
public class Points extends Entity
{
	int id;
	String lat;
	String lon;
	String time;
	String uuid;
	String imagepath;
	String imagedescription;
	Boolean isstartpoint;
	Boolean isendpoint;

	public void setImagedescription(String imagedescription)
	{
		this.imagedescription = imagedescription;
	}

	public String getImagedescription()
	{
		return imagedescription;
	}

	public void setImagepath(String imagepath)
	{
		this.imagepath = imagepath;
	}

	public String getImagepath()
	{
		return imagepath;
	}

	public void setIsendpoint(Boolean isendpoint)
	{
		this.isendpoint = isendpoint;
	}

	public Boolean getIsendpoint()
	{
		return isendpoint;
	}

	public void setIsstartpoint(Boolean isstartpoint)
	{
		this.isstartpoint = isstartpoint;
	}

	public Boolean getIsstartpoint()
	{
		return isstartpoint;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setLat(String lat)
	{
		this.lat = lat;
	}

	public String getLat()
	{
		return lat;
	}

	public void setLon(String lon)
	{
		this.lon = lon;
	}

	public String getLon()
	{
		return lon;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getTime()
	{
		return time;
	}

	@Override
	public String toString()
	{
		return super.toString();
	}
}
