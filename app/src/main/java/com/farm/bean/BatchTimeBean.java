package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * Description: areatab 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "BatchTimeBean")
public class BatchTimeBean implements Parcelable // 与数据库不一致
{
	public String id;
	public String uid;
	public String parkid;
	public String parkname;
	public String areaid;
	public String areaname;
	public String contractid;
	public String contractname;
	public String type;
	public String batchtime;
	public String isexist;


	public void setBatchtime(String batchtime)
	{
		this.batchtime = batchtime;
	}

	public String getBatchtime()
	{
		return batchtime;
	}

	public void setIsexist(String isexist)
	{
		this.isexist = isexist;
	}

	public String getIsexist()
	{
		return isexist;
	}

	public void setContractname(String contractname)
	{
		this.contractname = contractname;
	}

	public String getContractname()
	{
		return contractname;
	}

	public void setAreaname(String areaname)
	{
		this.areaname = areaname;
	}

	public String getAreaname()
	{
		return areaname;
	}

	public void setParkname(String parkname)
	{
		this.parkname = parkname;
	}

	public String getParkname()
	{
		return parkname;
	}

	public void setContractid(String contractid)
	{
		this.contractid = contractid;
	}

	public String getContractid()
	{
		return contractid;
	}

	public void setAreaid(String areaid)
	{
		this.areaid = areaid;
	}

	public String getAreaid()
	{
		return areaid;
	}

	public void setParkid(String parkid)
	{
		this.parkid = parkid;
	}

	public String getParkid()
	{
		return parkid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getUid()
	{
		return uid;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}



	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<BatchTimeBean> CREATOR = new Creator()
	{
		@Override
		public BatchTimeBean createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			BatchTimeBean p = new BatchTimeBean();
			p.setId(source.readString());
			p.setUid(source.readString());
			p.setParkid(source.readString());
			p.setParkname(source.readString());
			p.setAreaid(source.readString());
			p.setAreaname(source.readString());
			p.setContractid(source.readString());
			p.setContractname(source.readString());
			p.setType(source.readString());
			p.setBatchtime(source.readString());
			p.setIsexist(source.readString());
			return p;
		}

		@Override
		public BatchTimeBean[] newArray(int size)
		{
			return new BatchTimeBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(uid);
		p.writeString(parkid);
		p.writeString(parkname);
		p.writeString(areaid);
		p.writeString(areaname);
		p.writeString(contractid);
		p.writeString(contractname);
		p.writeString(type);
		p.writeString(batchtime);
		p.writeString(isexist);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
