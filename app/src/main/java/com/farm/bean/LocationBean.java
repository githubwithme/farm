package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * Description: commembertab 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "LocationBean")
public class LocationBean implements Parcelable
{
	@Id
	@NoAutoIncrement
	public String Id;
	public String uId;
	public String userId;
	public String userName;
	public String userlevel;
	public String parkId;
	public String parkName;
	public String areaId;
	public String areaName;
	public String userlevelName;
	public String nlevel;
	public String imgurl;
	public String lat;
	public String lng;
	public String regtime;


	public void setRegtime(String regtime)
	{
		this.regtime = regtime;
	}

	public String getRegtime()
	{
		return regtime;
	}

	public void setLat(String lat)
	{
		this.lat = lat;
	}

	public String getLat()
	{
		return lat;
	}

	public void setLng(String lng)
	{
		this.lng = lng;
	}

	public String getLng()
	{
		return lng;
	}



	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserId()
	{
		return userId;
	}



	public String getId()
	{
		return Id;
	}

	public void setId(String Id)
	{
		this.Id = Id;
	}

	public String getuId()
	{
		return uId;
	}

	public void setuId(String uId)
	{
		this.uId = uId;
	}

	public String getuserName()
	{
		return userName;
	}

	public void setuserName(String userName)
	{
		this.userName = userName;
	}



	public String getuserlevel()
	{
		return userlevel;
	}

	public void setuserlevel(String userlevel)
	{
		this.userlevel = userlevel;
	}

	public String getparkId()
	{
		return parkId;
	}

	public void setparkId(String parkId)
	{
		this.parkId = parkId;
	}

	public String getparkName()
	{
		return parkName;
	}

	public void setparkName(String parkName)
	{
		this.parkName = parkName;
	}

	public String getareaId()
	{
		return areaId;
	}

	public void setareaId(String areaId)
	{
		this.areaId = areaId;
	}

	public String getareaName()
	{
		return areaName;
	}

	public void setareaName(String areaName)
	{
		this.areaName = areaName;
	}



	public String getuserlevelName()
	{
		return userlevelName;
	}

	public void setuserlevelName(String userlevelName)
	{
		this.userlevelName = userlevelName;
	}

	public String getnlevel()
	{
		return nlevel;
	}

	public void setnlevel(String nlevel)
	{
		this.nlevel = nlevel;
	}

	public String getimgurl()
	{
		return imgurl;
	}

	public void setimgurl(String imgurl)
	{
		this.imgurl = imgurl;
	}



	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<LocationBean> CREATOR = new Creator()
	{
		@Override
		public LocationBean createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			LocationBean p = new LocationBean();
			p.setId(source.readString());
			p.setuId(source.readString());
			p.setUserId(source.readString());
			p.setuserName(source.readString());
			p.setuserlevel(source.readString());
			p.setparkId(source.readString());
			p.setparkName(source.readString());
			p.setareaId(source.readString());
			p.setareaName(source.readString());
			p.setuserlevelName(source.readString());
			p.setnlevel(source.readString());
			p.setimgurl(source.readString());
			p.setLat(source.readString());
			p.setLng(source.readString());
			p.setRegtime(source.readString());
			return p;
		}

		@Override
		public LocationBean[] newArray(int size)
		{
			return new LocationBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(Id);
		p.writeString(uId);
		p.writeString(userId);
		p.writeString(userName);
		p.writeString(userlevel);
		p.writeString(parkId);
		p.writeString(parkName);
		p.writeString(areaId);
		p.writeString(areaName);
		p.writeString(userlevelName);
		p.writeString(nlevel);
		p.writeString(imgurl);
		p.writeString(lat);
		p.writeString(lng);
		p.writeString(regtime);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
