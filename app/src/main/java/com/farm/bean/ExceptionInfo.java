package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 *
 * Description: breakofftab 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0
 */
@Table(name="ExceptionInfo")
public class ExceptionInfo implements Parcelable
{
	public String id;
	@Id
	@NoAutoIncrement
	public String exceptionid;
	public String uuid;
	public String exceptionInfo;
	public String regtime;
	public String userid;
	public String username;
	public String isSolve;


	public void setExceptionid(String exceptionid)
	{
		this.exceptionid = exceptionid;
	}

	public String getExceptionid()
	{
		return exceptionid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setIsSolve(String isSolve)
	{
		this.isSolve = isSolve;
	}

	public String getIsSolve()
	{
		return isSolve;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setRegtime(String regtime)
	{
		this.regtime = regtime;
	}

	public String getRegtime()
	{
		return regtime;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setExceptionInfo(String exceptionInfo)
	{
		this.exceptionInfo = exceptionInfo;
	}

	public String getExceptionInfo()
	{
		return exceptionInfo;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<ExceptionInfo> CREATOR = new Creator()
	{
		@Override
		public ExceptionInfo createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			ExceptionInfo p = new ExceptionInfo();
			p.setId(source.readString());
			p.setExceptionid(source.readString());
			p.setUuid(source.readString());
			p.setExceptionInfo(source.readString());
			p.setRegtime(source.readString());
			p.setUserid(source.readString());
			p.setUsername(source.readString());
			p.setIsSolve(source.readString());

			return p;
		}

		@Override
		public ExceptionInfo[] newArray(int size)
		{
			return new ExceptionInfo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(exceptionid);
		p.writeString(uuid);
		p.writeString(exceptionInfo);
		p.writeString(regtime);
		p.writeString(userid);
		p.writeString(username);
		p.writeString(isSolve);

	}
	@Override
	public int describeContents()
	{
		return 0;
	}
}
