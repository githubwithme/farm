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
@Table(name = "parktab")
public class parktab implements Parcelable
{
	public String id;
	public String uId;
	public String regDate;
	public String parkName;
	public String plantnumber;
	public String parkAddress;
	public String parkCover;
	public String depreciationOfFixedAssets;
	public String balanceOfPark;
	public String parkNote;
	public String isDelete;
	public String deleteDate;
	public String x;
	public String y;
	public String Apath;
	public String AYear;
	public String parkWeather;
	// 自定义
	public String workuserid;
	public String realName;
	public String imgurl;
	public String Weather;
	public String Weatherimg;
	public String TMPL;
	public String TMPM;
	public String TMPH;
	public String jobCount;
	public String plantCount;
	public String plantGrowCount;
	public String askCount;
	public String AreajobCount;
	public String AreaplantGrowCount;

	public String jobVideoCount;
	public String plantVideoCount;
	public String plantGrowVideoCount;
	public String askVideoCount;
	public String AreajobVideoCount;
	public String AreaplantGrowVideoCount;

	public String commandCount;
	public String AreacommandCount;
	public String commandVideoCount;
	public String AreacommandVideoCount;
	public List<areatab> areatabList;


	public void setPlantnumber(String plantnumber)
	{
		this.plantnumber = plantnumber;
	}

	public String getPlantnumber()
	{
		return plantnumber;
	}

	public void setAreatabList(List<areatab> areatabList)
	{
		this.areatabList = areatabList;
	}

	public List<areatab> getAreatabList()
	{
		return areatabList;
	}

	public void setCommandVideoCount(String commandVideoCount)
	{
		this.commandVideoCount = commandVideoCount;
	}

	public String getCommandVideoCount()
	{
		return commandVideoCount;
	}

	public void setAreacommandVideoCount(String areacommandVideoCount)
	{
		AreacommandVideoCount = areacommandVideoCount;
	}

	public String getAreacommandVideoCount()
	{
		return AreacommandVideoCount;
	}

	public void setCommandCount(String commandCount)
	{
		this.commandCount = commandCount;
	}

	public String getCommandCount()
	{
		return commandCount;
	}

	public void setAreacommandCount(String areacommandCount)
	{
		AreacommandCount = areacommandCount;
	}

	public String getAreacommandCount()
	{
		return AreacommandCount;
	}

	public void setAreaplantGrowVideoCount(String areaplantGrowVideoCount)
	{
		AreaplantGrowVideoCount = areaplantGrowVideoCount;
	}

	public String getAreaplantGrowVideoCount()
	{
		return AreaplantGrowVideoCount;
	}

	public void setAreajobVideoCount(String areajobVideoCount)
	{
		AreajobVideoCount = areajobVideoCount;
	}

	public String getAreajobVideoCount()
	{
		return AreajobVideoCount;
	}

	public void setAskVideoCount(String askVideoCount)
	{
		this.askVideoCount = askVideoCount;
	}

	public String getAskVideoCount()
	{
		return askVideoCount;
	}

	public void setPlantGrowVideoCount(String plantGrowVideoCount)
	{
		this.plantGrowVideoCount = plantGrowVideoCount;
	}

	public String getPlantGrowVideoCount()
	{
		return plantGrowVideoCount;
	}

	public void setPlantVideoCount(String plantVideoCount)
	{
		this.plantVideoCount = plantVideoCount;
	}

	public String getPlantVideoCount()
	{
		return plantVideoCount;
	}

	public void setJobVideoCount(String jobVideoCount)
	{
		this.jobVideoCount = jobVideoCount;
	}

	public String getJobVideoCount()
	{
		return jobVideoCount;
	}

	public void setAreajobCount(String areajobCount)
	{
		AreajobCount = areajobCount;
	}

	public String getAreajobCount()
	{
		return AreajobCount;
	}

	public void setAreaplantGrowCount(String areaplantGrowCount)
	{
		AreaplantGrowCount = areaplantGrowCount;
	}

	public String getAreaplantGrowCount()
	{
		return AreaplantGrowCount;
	}

	public void setJobCount(String jobCount)
	{
		this.jobCount = jobCount;
	}

	public String getJobCount()
	{
		return jobCount;
	}

	public void setPlantCount(String plantCount)
	{
		this.plantCount = plantCount;
	}

	public String getPlantCount()
	{
		return plantCount;
	}

	public void setPlantGrowCount(String plantGrowCount)
	{
		this.plantGrowCount = plantGrowCount;
	}

	public String getPlantGrowCount()
	{
		return plantGrowCount;
	}

	public void setAskCount(String askCount)
	{
		this.askCount = askCount;
	}

	public String getAskCount()
	{
		return askCount;
	}

	public void setTMPM(String tMPM)
	{
		TMPM = tMPM;
	}

	public String getTMPM()
	{
		return TMPM;
	}

	public void setTMPH(String tMPH)
	{
		TMPH = tMPH;
	}

	public String getTMPH()
	{
		return TMPH;
	}

	public void setTMPL(String tMPL)
	{
		TMPL = tMPL;
	}

	public String getTMPL()
	{
		return TMPL;
	}

	public void setWeatherimg(String weatherimg)
	{
		Weatherimg = weatherimg;
	}

	public String getWeatherimg()
	{
		return Weatherimg;
	}

	public void setWorkuserid(String workuserid)
	{
		this.workuserid = workuserid;
	}

	public String getWorkuserid()
	{
		return workuserid;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setImgurl(String imgurl)
	{
		this.imgurl = imgurl;
	}

	public String getImgurl()
	{
		return imgurl;
	}

	public void setWeather(String weather)
	{
		Weather = weather;
	}

	public String getWeather()
	{
		return Weather;
	}

	public String getid()
	{
		return id;
	}

	public void setid(String id)
	{
		this.id = id;
	}

	public String getuId()
	{
		return uId;
	}

	public void setuId(String uId)
	{
		this.uId = uId;
	}

	public String getregDate()
	{
		return regDate;
	}

	public void setregDate(String regDate)
	{
		this.regDate = regDate;
	}

	public String getparkName()
	{
		return parkName;
	}

	public void setparkName(String parkName)
	{
		this.parkName = parkName;
	}

	public String getparkAddress()
	{
		return parkAddress;
	}

	public void setparkAddress(String parkAddress)
	{
		this.parkAddress = parkAddress;
	}

	public String getparkCover()
	{
		return parkCover;
	}

	public void setparkCover(String parkCover)
	{
		this.parkCover = parkCover;
	}

	public String getdepreciationOfFixedAssets()
	{
		return depreciationOfFixedAssets;
	}

	public void setdepreciationOfFixedAssets(String depreciationOfFixedAssets)
	{
		this.depreciationOfFixedAssets = depreciationOfFixedAssets;
	}

	public String getbalanceOfPark()
	{
		return balanceOfPark;
	}

	public void setbalanceOfPark(String balanceOfPark)
	{
		this.balanceOfPark = balanceOfPark;
	}

	public String getparkNote()
	{
		return parkNote;
	}

	public void setparkNote(String parkNote)
	{
		this.parkNote = parkNote;
	}

	public String getisDelete()
	{
		return isDelete;
	}

	public void setisDelete(String isDelete)
	{
		this.isDelete = isDelete;
	}

	public String getdeleteDate()
	{
		return deleteDate;
	}

	public void setdeleteDate(String deleteDate)
	{
		this.deleteDate = deleteDate;
	}

	public String getx()
	{
		return x;
	}

	public void setx(String x)
	{
		this.x = x;
	}

	public String gety()
	{
		return y;
	}

	public void sety(String y)
	{
		this.y = y;
	}

	public String getApath()
	{
		return Apath;
	}

	public void setApath(String Apath)
	{
		this.Apath = Apath;
	}

	public String getAYear()
	{
		return AYear;
	}

	public void setAYear(String AYear)
	{
		this.AYear = AYear;
	}

	public String getparkWeather()
	{
		return parkWeather;
	}

	public void setparkWeather(String parkWeather)
	{
		this.parkWeather = parkWeather;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Parcelable.Creator<parktab> CREATOR = new Creator()
	{
		@Override
		public parktab createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			parktab p = new parktab();
			p.setid(source.readString());
			p.setuId(source.readString());
			p.setregDate(source.readString());
			p.setparkName(source.readString());
			p.setPlantnumber(source.readString());
			p.setparkAddress(source.readString());
			p.setparkCover(source.readString());
			p.setdepreciationOfFixedAssets(source.readString());
			p.setbalanceOfPark(source.readString());
			p.setparkNote(source.readString());
			p.setisDelete(source.readString());
			p.setdeleteDate(source.readString());
			p.setx(source.readString());
			p.sety(source.readString());
			p.setApath(source.readString());
			p.setAYear(source.readString());
			p.setparkWeather(source.readString());

			p.setWorkuserid(source.readString());
			p.setRealName(source.readString());
			p.setImgurl(source.readString());
			p.setWeather(source.readString());
			p.setWeatherimg(source.readString());
			p.setTMPL(source.readString());
			p.setTMPM(source.readString());
			p.setTMPH(source.readString());
			p.setJobCount(source.readString());
			p.setPlantCount(source.readString());
			p.setPlantGrowCount(source.readString());
			p.setAskCount(source.readString());
			p.setAreajobCount(source.readString());
			p.setAreaplantGrowCount(source.readString());

			p.setCommandCount(source.readString());
			p.setAreacommandCount(source.readString());
			p.setCommandVideoCount(source.readString());
			p.setAreacommandVideoCount(source.readString());
			p.areatabList = source.readArrayList(plantgrowthtab.class.getClassLoader());
			return p;
		}

		@Override
		public parktab[] newArray(int size)
		{
			return new parktab[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(uId);
		p.writeString(regDate);
		p.writeString(parkName);
		p.writeString(plantnumber);
		p.writeString(parkAddress);
		p.writeString(parkCover);
		p.writeString(depreciationOfFixedAssets);
		p.writeString(balanceOfPark);
		p.writeString(parkNote);
		p.writeString(isDelete);
		p.writeString(deleteDate);
		p.writeString(x);
		p.writeString(y);
		p.writeString(Apath);
		p.writeString(AYear);
		p.writeString(parkWeather);

		p.writeString(workuserid);
		p.writeString(realName);
		p.writeString(imgurl);
		p.writeString(Weather);
		p.writeString(Weatherimg);
		p.writeString(TMPL);
		p.writeString(TMPM);
		p.writeString(TMPH);
		p.writeString(jobCount);
		p.writeString(plantCount);
		p.writeString(plantGrowCount);
		p.writeString(askCount);
		p.writeString(AreajobCount);
		p.writeString(AreaplantGrowCount);

		p.writeString(commandCount);
		p.writeString(AreacommandCount);
		p.writeString(commandVideoCount);
		p.writeString(AreacommandVideoCount);
		p.writeList(areatabList);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
