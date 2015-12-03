

package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;
/**
 *
 * Description: parkweatherhourtab 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0 
 */
@Table(name="parkweatherhourtab")
public class parkweatherhourtab implements Parcelable 
{
	public String id;
	public String uId;
	public String regDate;
	public String parkId;
	public String tempL;
	public String tempH;
	public String tempM;
	public String weather;
	public String ADate;
	public String AHour;
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

	public String getADate() 
    {
		return ADate;
	}

	public void setADate(String ADate) 
    {
		this.ADate = ADate;
	}

	public String getAHour() 
    {
		return AHour;
	}

	public void setAHour(String AHour) 
    {
		this.AHour = AHour;
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
    
   public static final Parcelable.Creator<parkweatherhourtab> CREATOR = new Creator()
   {  
      @Override  
      public parkweatherhourtab createFromParcel(Parcel source)
      {  
         // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
    	 parkweatherhourtab p = new parkweatherhourtab();
         p.setid(source.readString());
         p.setuId(source.readString());
         p.setregDate(source.readString());
         p.setparkId(source.readString());
         p.settempL(source.readString());
         p.settempH(source.readString());
         p.settempM(source.readString());
         p.setweather(source.readString());
         p.setADate(source.readString());
         p.setAHour(source.readString());
         p.setdimg(source.readString());
         p.setnimg(source.readString());
         return p;  
      }  

      @Override  
      public parkweatherhourtab[] newArray(int size) 
      {  
          return new parkweatherhourtab[size];  
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
         p.writeString(ADate);
         p.writeString(AHour);
         p.writeString(dimg);
         p.writeString(nimg);
	}
    @Override
	public int describeContents()
	{
		return 0;
	}
}
