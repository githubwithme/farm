package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

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
@Table(name = "areatab")
public class areatab implements Parcelable // 与数据库不一致
{
	public String id;
	public String uId;
	public String regDate;
	public String areaName;
	public String areaCover;
	public String productId;
	public String productName;
	public String parkId;
	public String parkName;
	public String numOfPlant;
	public String numOfPick;
	public String ratioOfParkFee;
	public String ratioOfParkIncome;
	public String areaNote;
	public String isDelete;
	public String deleteDate;
	public String x;
	public String y;
	public String Apath;
	public String AYear;
	// 自定义
	public String jobCount;
	public String plantCount;
	public String plantGrowCount;
	public String realName;
	public String imgurl;
	public String workuserid;
	public String commandCount;
	public String jobVideoCount;
	public String plantGrowVideoCount;
	public String commandVideoCount;
	public List<contractTab> contractTabList;


	public void setContractTabList(List<contractTab> contractTabList)
	{
		this.contractTabList = contractTabList;
	}

	public List<contractTab> getContractTabList()
	{
		return contractTabList;
	}

	public void setJobVideoCount(String jobVideoCount)
	{
		this.jobVideoCount = jobVideoCount;
	}

	public String getJobVideoCount()
	{
		return jobVideoCount;
	}

	public void setPlantGrowVideoCount(String plantGrowVideoCount)
	{
		this.plantGrowVideoCount = plantGrowVideoCount;
	}

	public String getPlantGrowVideoCount()
	{
		return plantGrowVideoCount;
	}

	public void setCommandVideoCount(String commandVideoCount)
	{
		this.commandVideoCount = commandVideoCount;
	}

	public String getCommandVideoCount()
	{
		return commandVideoCount;
	}

	public void setCommandCount(String commandCount)
	{
		this.commandCount = commandCount;
	}

	public String getCommandCount()
	{
		return commandCount;
	}

	public void setWorkuserid(String workuserid)
	{
		this.workuserid = workuserid;
	}

	public String getWorkuserid()
	{
		return workuserid;
	}

	public void setImgurl(String imgurl)
	{
		this.imgurl = imgurl;
	}

	public String getImgurl()
	{
		return imgurl;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setPlantGrowCount(String plantGrowCount)
	{
		this.plantGrowCount = plantGrowCount;
	}

	public String getPlantGrowCount()
	{
		return plantGrowCount;
	}

	public void setPlantCount(String plantCount)
	{
		this.plantCount = plantCount;
	}

	public String getPlantCount()
	{
		return plantCount;
	}

	public void setJobCount(String jobCount)
	{
		this.jobCount = jobCount;
	}

	public String getJobCount()
	{
		return jobCount;
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

	public String getareaName()
	{
		return areaName;
	}

	public void setareaName(String areaName)
	{
		this.areaName = areaName;
	}

	public String getareaCover()
	{
		return areaCover;
	}

	public void setareaCover(String areaCover)
	{
		this.areaCover = areaCover;
	}

	public String getproductId()
	{
		return productId;
	}

	public void setproductId(String productId)
	{
		this.productId = productId;
	}

	public String getproductName()
	{
		return productName;
	}

	public void setproductName(String productName)
	{
		this.productName = productName;
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

	public String getnumOfPlant()
	{
		return numOfPlant;
	}

	public void setnumOfPlant(String numOfPlant)
	{
		this.numOfPlant = numOfPlant;
	}

	public String getnumOfPick()
	{
		return numOfPick;
	}

	public void setnumOfPick(String numOfPick)
	{
		this.numOfPick = numOfPick;
	}

	public String getratioOfParkFee()
	{
		return ratioOfParkFee;
	}

	public void setratioOfParkFee(String ratioOfParkFee)
	{
		this.ratioOfParkFee = ratioOfParkFee;
	}

	public String getratioOfParkIncome()
	{
		return ratioOfParkIncome;
	}

	public void setratioOfParkIncome(String ratioOfParkIncome)
	{
		this.ratioOfParkIncome = ratioOfParkIncome;
	}

	public String getareaNote()
	{
		return areaNote;
	}

	public void setareaNote(String areaNote)
	{
		this.areaNote = areaNote;
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

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Parcelable.Creator<areatab> CREATOR = new Creator()
	{
		@Override
		public areatab createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			areatab p = new areatab();
			p.setid(source.readString());
			p.setuId(source.readString());
			p.setregDate(source.readString());
			p.setareaName(source.readString());
			p.setareaCover(source.readString());
			p.setproductId(source.readString());
			p.setproductName(source.readString());
			p.setparkId(source.readString());
			p.setparkName(source.readString());
			p.setnumOfPlant(source.readString());
			p.setnumOfPick(source.readString());
			p.setratioOfParkFee(source.readString());
			p.setratioOfParkIncome(source.readString());
			p.setareaNote(source.readString());
			p.setisDelete(source.readString());
			p.setdeleteDate(source.readString());
			p.setx(source.readString());
			p.sety(source.readString());
			p.setApath(source.readString());
			p.setAYear(source.readString());
			p.setJobCount(source.readString());
			p.setPlantCount(source.readString());
			p.setPlantGrowCount(source.readString());
			p.setRealName(source.readString());
			p.setImgurl(source.readString());
			p.setWorkuserid(source.readString());
			p.setCommandCount(source.readString());
			p.contractTabList = source.readArrayList(plantgrowthtab.class.getClassLoader());
			return p;
		}

		@Override
		public areatab[] newArray(int size)
		{
			return new areatab[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(uId);
		p.writeString(regDate);
		p.writeString(areaName);
		p.writeString(areaCover);
		p.writeString(productId);
		p.writeString(productName);
		p.writeString(parkId);
		p.writeString(parkName);
		p.writeString(numOfPlant);
		p.writeString(numOfPick);
		p.writeString(ratioOfParkFee);
		p.writeString(ratioOfParkIncome);
		p.writeString(areaNote);
		p.writeString(isDelete);
		p.writeString(deleteDate);
		p.writeString(x);
		p.writeString(y);
		p.writeString(Apath);
		p.writeString(AYear);
		p.writeString(jobCount);
		p.writeString(plantCount);
		p.writeString(plantGrowCount);
		p.writeString(realName);
		p.writeString(imgurl);
		p.writeString(workuserid);
		p.writeString(commandCount);
		p.writeList(contractTabList);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
