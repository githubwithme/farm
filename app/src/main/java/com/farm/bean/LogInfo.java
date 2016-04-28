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
@Table(name="LogInfo")
public class LogInfo implements Parcelable
{
	public String id;
	@Id
	@NoAutoIncrement
	public String logid;
	public String logInfo;
	public String logday;
	public String regtime;
	public String deviceuuid;
	public String isUpload;

	public void setLogday(String logday)
	{
		this.logday = logday;
	}

	public String getLogday()
	{
		return logday;
	}

	public void setIsUpload(String isUpload)
	{
		this.isUpload = isUpload;
	}

	public String getIsUpload()
	{
		return isUpload;
	}

	public void setLogid(String logid)
	{
		this.logid = logid;
	}

	public String getLogid()
	{
		return logid;
	}

	public void setLogInfo(String logInfo)
	{
		this.logInfo = logInfo;
	}

	public String getLogInfo()
	{
		return logInfo;
	}

	public void setDeviceuuid(String deviceuuid)
	{
		this.deviceuuid = deviceuuid;
	}

	public String getDeviceuuid()
	{
		return deviceuuid;
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



	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<LogInfo> CREATOR = new Creator()
	{
		@Override
		public LogInfo createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			LogInfo p = new LogInfo();
			p.setId(source.readString());
			p.setLogid(source.readString());
			p.setLogInfo(source.readString());
			p.setLogday(source.readString());
			p.setRegtime(source.readString());
			p.setDeviceuuid(source.readString());
			p.setIsUpload(source.readString());

			return p;
		}

		@Override
		public LogInfo[] newArray(int size)
		{
			return new LogInfo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(logid);
		p.writeString(logInfo);
		p.writeString(logday);
		p.writeString(regtime);
		p.writeString(deviceuuid);
		p.writeString(isUpload);

	}
	@Override
	public int describeContents()
	{
		return 0;
	}
}
