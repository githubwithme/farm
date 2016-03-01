package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * 
 * Description: parktab 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "BoundaryBean")
public class BoundaryBean implements Parcelable
{
	public String id;
	public String uid;
	public String parkid;
	public String parkname;
	public List<Points> pointsList;

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getUid()
	{
		return uid;
	}

	public void setParkname(String parkname)
	{
		this.parkname = parkname;
	}

	public String getParkname()
	{
		return parkname;
	}

	public void setParkid(String parkid)
	{
		this.parkid = parkid;
	}

	public String getParkid()
	{
		return parkid;
	}

	public void setPointsList(List<Points> pointsList)
	{
		this.pointsList = pointsList;
	}

	public List<Points> getPointsList()
	{
		return pointsList;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<BoundaryBean> CREATOR = new Creator()
	{
		@Override
		public BoundaryBean createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			BoundaryBean p = new BoundaryBean();
			p.setId(source.readString());
			p.setUid(source.readString());
			p.setParkid(source.readString());
			p.setParkname(source.readString());
			p.pointsList = source.readArrayList(plantgrowthtab.class.getClassLoader());
			return p;
		}

		@Override
		public BoundaryBean[] newArray(int size)
		{
			return new BoundaryBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(uid);
		p.writeString(parkid);
		p.writeString(parkname);
		p.writeList(pointsList);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
