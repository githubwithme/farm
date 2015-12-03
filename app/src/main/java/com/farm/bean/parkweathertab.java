

package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;
/**
 *
 * Description: parkweathertab 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0 
 */
@Table(name="parkweathertab")
public class parkweathertab implements Parcelable 
{
	public String id;
	public String uId;
	public String regDate;
	public String parkId;
	public String tempL;
	public String tempH;
	public String tempM;
	public String weather;
	public String bManMod;
	public String parkweatherNote;
	public String isDelete;
	public String DeleteDate;
	public String AYear;
	public String cjUserID;
	public String cjUserName;
	public String cjDate;
	public String dimg;
	public String nimg;
    

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

	public String getparkId() 
    {
		return parkId;
	}

	public void setparkId(String parkId) 
    {
		this.parkId = parkId;
	}

	public String gettempL() 
    {
		return tempL;
	}

	public void settempL(String tempL) 
    {
		this.tempL = tempL;
	}

	public String gettempH() 
    {
		return tempH;
	}

	public void settempH(String tempH) 
    {
		this.tempH = tempH;
	}

	public String gettempM() 
    {
		return tempM;
	}

	public void settempM(String tempM) 
    {
		this.tempM = tempM;
	}

	public String getweather() 
    {
		return weather;
	}

	public void setweather(String weather) 
    {
		this.weather = weather;
	}

	public String getbManMod() 
    {
		return bManMod;
	}

	public void setbManMod(String bManMod) 
    {
		this.bManMod = bManMod;
	}

	public String getparkweatherNote() 
    {
		return parkweatherNote;
	}

	public void setparkweatherNote(String parkweatherNote) 
    {
		this.parkweatherNote = parkweatherNote;
	}

	public String getisDelete() 
    {
		return isDelete;
	}

	public void setisDelete(String isDelete) 
    {
		this.isDelete = isDelete;
	}

	public String getDeleteDate() 
    {
		return DeleteDate;
	}

	public void setDeleteDate(String DeleteDate) 
    {
		this.DeleteDate = DeleteDate;
	}

	public String getAYear() 
    {
		return AYear;
	}

	public void setAYear(String AYear) 
    {
		this.AYear = AYear;
	}

	public String getcjUserID() 
    {
		return cjUserID;
	}

	public void setcjUserID(String cjUserID) 
    {
		this.cjUserID = cjUserID;
	}

	public String getcjUserName() 
    {
		return cjUserName;
	}

	public void setcjUserName(String cjUserName) 
    {
		this.cjUserName = cjUserName;
	}

	public String getcjDate() 
    {
		return cjDate;
	}

	public void setcjDate(String cjDate) 
    {
		this.cjDate = cjDate;
	}

	public String getdimg() 
    {
		return dimg;
	}

	public void setdimg(String dimg) 
    {
		this.dimg = dimg;
	}

	public String getnimg() 
    {
		return nimg;
	}

	public void setnimg(String nimg) 
    {
		this.nimg = nimg;
	}
	
	public boolean equals(Object o) 
    {
		return false;
	}

	public int hashCode()
    {
		return 0;
	}
    
   public static final Parcelable.Creator<parkweathertab> CREATOR = new Creator()
   {  
      @Override  
      public parkweathertab createFromParcel(Parcel source)
      {  
         // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
    	 parkweathertab p = new parkweathertab();
         p.setid(source.readString());
         p.setuId(source.readString());
         p.setregDate(source.readString());
         p.setparkId(source.readString());
         p.settempL(source.readString());
         p.settempH(source.readString());
         p.settempM(source.readString());
         p.setweather(source.readString());
         p.setbManMod(source.readString());
         p.setparkweatherNote(source.readString());
         p.setisDelete(source.readString());
         p.setDeleteDate(source.readString());
         p.setAYear(source.readString());
         p.setcjUserID(source.readString());
         p.setcjUserName(source.readString());
         p.setcjDate(source.readString());
         p.setdimg(source.readString());
         p.setnimg(source.readString());
         return p;  
      }  

      @Override  
      public parkweathertab[] newArray(int size) 
      {  
          return new parkweathertab[size];  
      }  
   }; 
   
   	@Override
	public void writeToParcel(Parcel p, int arg1) 
    {
         p.writeString(id);
         p.writeString(uId);
         p.writeString(regDate);
         p.writeString(parkId);
         p.writeString(tempL);
         p.writeString(tempH);
         p.writeString(tempM);
         p.writeString(weather);
         p.writeString(bManMod);
         p.writeString(parkweatherNote);
         p.writeString(isDelete);
         p.writeString(DeleteDate);
         p.writeString(AYear);
         p.writeString(cjUserID);
         p.writeString(cjUserName);
         p.writeString(cjDate);
         p.writeString(dimg);
         p.writeString(nimg);
	}
    @Override
	public int describeContents()
	{
		return 0;
	}
}
