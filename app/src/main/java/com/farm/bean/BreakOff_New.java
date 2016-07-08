package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 *
 * Description: breakofftab 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0
 */
@Table(name="BreakOff_New")
public class BreakOff_New implements Parcelable
{
	public String id;
	@Id
	@NoAutoIncrement
	public String uuid;
	public String uid;
	public String breakofftime;
	public String parkid;
	public String parkname;
	public String areaid;
	public String areaname;
	public String contractid;
	public String contractname; //承包区名字
	public String lat;
	public String lng;
	public String latlngsize ;
	public String numberofbreakoff; //数量
	public String regdate;
	public String weight;
	public String status;//断蕾情况：0未断蕾 1已经断蕾
	public String batchTime  ;//批次时间
	public String batchColor  ;//批次颜色
	public String xxzt;
	public String Year;
	public String allnumber;
	public List<CoordinatesBean> coordinatesBeanList;

	public String getAllnumber()
	{
		return allnumber;
	}

	public void setAllnumber(String allnumber)
	{
		this.allnumber = allnumber;
	}

	public void setCoordinatesBeanList(List<CoordinatesBean> coordinatesBeanList)
	{
		this.coordinatesBeanList = coordinatesBeanList;
	}

	public List<CoordinatesBean> getCoordinatesBeanList()
	{
		return coordinatesBeanList;
	}

	public void setBatchColor(String batchColor)
	{
		this.batchColor = batchColor;
	}

	public String getBatchColor()
	{
		return batchColor;
	}

	public void setYear(String year)
	{
		Year = year;
	}

	public String getYear()
	{
		return Year;
	}

	public void setXxzt(String xxzt)
	{
		this.xxzt = xxzt;
	}

	public String getXxzt()
	{
		return xxzt;
	}

	;


	public void setBatchTime(String batchTime)
	{
		this.batchTime = batchTime;
	}

	public String getBatchTime()
	{
		return batchTime;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return status;
	}

	public void setWeight(String weight)
	{
		this.weight = weight;
	}

	public String getWeight()
	{
		return weight;
	}

	public void setLatlngsize(String latlngsize)
	{
		this.latlngsize = latlngsize;
	}

	public String getLatlngsize()
	{
		return latlngsize;
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

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setBreakofftime(String breakofftime)
	{
		this.breakofftime = breakofftime;
	}

	public String getBreakofftime()
	{
		return breakofftime;
	}

	public String getid()
	{
		return id;
	}

	public void setid(String id)
	{
		this.id = id;
	}

	public String getuid()
	{
		return uid;
	}

	public void setuid(String uid)
	{
		this.uid = uid;
	}



	public String getparkid()
	{
		return parkid;
	}

	public void setparkid(String parkid)
	{
		this.parkid = parkid;
	}

	public String getparkname()
	{
		return parkname;
	}

	public void setparkname(String parkname)
	{
		this.parkname = parkname;
	}

	public String getareaid()
	{
		return areaid;
	}

	public void setareaid(String areaid)
	{
		this.areaid = areaid;
	}

	public String getareaname()
	{
		return areaname;
	}

	public void setareaname(String areaname)
	{
		this.areaname = areaname;
	}

	public String getcontractid()
	{
		return contractid;
	}

	public void setcontractid(String contractid)
	{
		this.contractid = contractid;
	}

	public String getcontractname()
	{
		return contractname;
	}

	public void setcontractname(String contractname)
	{
		this.contractname = contractname;
	}

	public String getnumberofbreakoff()
	{
		return numberofbreakoff;
	}

	public void setnumberofbreakoff(String numberofbreakoff)
	{
		this.numberofbreakoff = numberofbreakoff;
	}

	public String getregdate()
	{
		return regdate;
	}

	public void setregdate(String regdate)
	{
		this.regdate = regdate;
	}



	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<BreakOff_New> CREATOR = new Creator()
	{
		@Override
		public BreakOff_New createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			BreakOff_New p = new BreakOff_New();
			p.setid(source.readString());
			p.setUuid(source.readString());
			p.setuid(source.readString());
			p.setBreakofftime(source.readString());
			p.setparkid(source.readString());
			p.setparkname(source.readString());
			p.setareaid(source.readString());
			p.setareaname(source.readString());
			p.setcontractid(source.readString());
			p.setcontractname(source.readString());
			p.setLat(source.readString());
			p.setLng(source.readString());
			p.setLatlngsize(source.readString());
			p.setnumberofbreakoff(source.readString());
			p.setregdate(source.readString());
			p.setWeight(source.readString());
			p.setStatus(source.readString());
			p.setBatchTime(source.readString());
			p.setBatchColor(source.readString());
			p.setXxzt(source.readString());
			p.setYear(source.readString());
			p.setAllnumber(source.readString());
			p.coordinatesBeanList = source.readArrayList(sellOrderDetailTab.class.getClassLoader());
			return p;
		}

		@Override
		public BreakOff_New[] newArray(int size)
		{
			return new BreakOff_New[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(uuid);
		p.writeString(uid);
		p.writeString(breakofftime);
		p.writeString(parkid);
		p.writeString(parkname);
		p.writeString(areaid);
		p.writeString(areaname);
		p.writeString(contractid);
		p.writeString(contractname);
		p.writeString(lat);
		p.writeString(lng);
		p.writeString(latlngsize);
		p.writeString(numberofbreakoff);
		p.writeString(regdate);
		p.writeString(weight);
		p.writeString(status);
		p.writeString(batchTime);
		p.writeString(batchColor);
		p.writeString(xxzt);
		p.writeString(Year);
		p.writeString(allnumber);
		p.writeList(coordinatesBeanList);
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
}
