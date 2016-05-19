

package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 *
 * Description: SellOrderDetail 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0 
 */
@Table(name="SellOrderDetail_New")
public class SellOrderDetail_New implements Parcelable
{
	public String id;
	@Id
	@NoAutoIncrement
	public String uuid;
	public String saleid;
	public String batchTime;
	public String uid;
	public String parkid;
	public String parkname;
	public String areaid;
	public String areaname;
	public String contractid;
	public String contractname;
	public String planprice;
	public String actualprice;
	public String planlat;
	public String planlatlngsize;
	public String actuallatlngsize;
	public String planlng;
	public String actuallat;
	public String actuallng;
	public String plannumber;
	public String planweight;
	public String actualnumber;
	public String actualweight;
	public String plannote;
	public String actualnote;
	public String reg;
	public String status;
	public String isSoldOut;
	public String xxzt;
	public String type;
	public String year;
	public List<CoordinatesBean> coordinatesBeanList;


	public void setYear(String year)
	{
		this.year = year;
	}

	public String getYear()
	{
		return year;
	}

	public void setCoordinatesBeanList(List<CoordinatesBean> coordinatesBeanList)
	{
		this.coordinatesBeanList = coordinatesBeanList;
	}

	public List<CoordinatesBean> getCoordinatesBeanList()
	{
		return coordinatesBeanList;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setBatchTime(String batchTime)
	{
		this.batchTime = batchTime;
	}

	public String getBatchTime()
	{
		return batchTime;
	}

	public void setXxzt(String xxzt)
	{
		this.xxzt = xxzt;
	}

	public String getXxzt()
	{
		return xxzt;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getUuid()
	{
		return uuid;
	}

	public String getid()
    {
		return id;
	}

	public void setid(String id) 
    {
		this.id = id;
	}

	public String getsaleid() 
    {
		return saleid;
	}

	public void setsaleid(String saleid) 
    {
		this.saleid = saleid;
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

	public String getplanprice() 
    {
		return planprice;
	}

	public void setplanprice(String planprice) 
    {
		this.planprice = planprice;
	}

	public String getactualprice() 
    {
		return actualprice;
	}

	public void setactualprice(String actualprice) 
    {
		this.actualprice = actualprice;
	}


	public void setPlanlat(String planlat)
	{
		this.planlat = planlat;
	}

	public String getPlanlat()
	{
		return planlat;
	}

	public String getplanlatlngsize()
    {
		return planlatlngsize;
	}

	public void setplanlatlngsize(String planlatlngsize) 
    {
		this.planlatlngsize = planlatlngsize;
	}

	public String getactuallatlngsize() 
    {
		return actuallatlngsize;
	}

	public void setactuallatlngsize(String actuallatlngsize) 
    {
		this.actuallatlngsize = actuallatlngsize;
	}

	public String getplanlng() 
    {
		return planlng;
	}

	public void setplanlng(String planlng) 
    {
		this.planlng = planlng;
	}

	public String getactuallat() 
    {
		return actuallat;
	}

	public void setactuallat(String actuallat) 
    {
		this.actuallat = actuallat;
	}

	public String getactuallng() 
    {
		return actuallng;
	}

	public void setactuallng(String actuallng) 
    {
		this.actuallng = actuallng;
	}

	public String getplannumber() 
    {
		return plannumber;
	}

	public void setplannumber(String plannumber) 
    {
		this.plannumber = plannumber;
	}

	public String getplanweight() 
    {
		return planweight;
	}

	public void setplanweight(String planweight) 
    {
		this.planweight = planweight;
	}

	public String getactualnumber() 
    {
		return actualnumber;
	}

	public void setactualnumber(String actualnumber) 
    {
		this.actualnumber = actualnumber;
	}

	public String getactualweight() 
    {
		return actualweight;
	}

	public void setactualweight(String actualweight) 
    {
		this.actualweight = actualweight;
	}

	public String getplannote() 
    {
		return plannote;
	}

	public void setplannote(String plannote) 
    {
		this.plannote = plannote;
	}

	public String getactualnote() 
    {
		return actualnote;
	}

	public void setactualnote(String actualnote) 
    {
		this.actualnote = actualnote;
	}

	public String getreg() 
    {
		return reg;
	}

	public void setreg(String reg) 
    {
		this.reg = reg;
	}

	public String getstatus() 
    {
		return status;
	}

	public void setstatus(String status) 
    {
		this.status = status;
	}

	public String getisSoldOut() 
    {
		return isSoldOut;
	}

	public void setisSoldOut(String isSoldOut) 
    {
		this.isSoldOut = isSoldOut;
	}
	
	public boolean equals(Object o) 
    {
		return false;
	}

	public int hashCode()
    {
		return 0;
	}
    
   public static final Creator<SellOrderDetail_New> CREATOR = new Creator()
   {  
      @Override  
      public SellOrderDetail_New createFromParcel(Parcel source)
      {  
         // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
    	 SellOrderDetail_New p = new SellOrderDetail_New();
         p.setid(source.readString());
         p.setUuid(source.readString());
         p.setsaleid(source.readString());
         p.setBatchTime(source.readString());
         p.setuid(source.readString());
         p.setparkid(source.readString());
         p.setparkname(source.readString());
         p.setareaid(source.readString());
         p.setareaname(source.readString());
         p.setcontractid(source.readString());
         p.setcontractname(source.readString());
         p.setplanprice(source.readString());
         p.setactualprice(source.readString());
         p.setPlanlat(source.readString());
         p.setplanlatlngsize(source.readString());
         p.setactuallatlngsize(source.readString());
         p.setplanlng(source.readString());
         p.setactuallat(source.readString());
         p.setactuallng(source.readString());
         p.setplannumber(source.readString());
         p.setplanweight(source.readString());
         p.setactualnumber(source.readString());
         p.setactualweight(source.readString());
         p.setplannote(source.readString());
         p.setactualnote(source.readString());
         p.setreg(source.readString());
         p.setstatus(source.readString());
         p.setisSoldOut(source.readString());
         p.setXxzt(source.readString());
         p.setType(source.readString());
         p.setYear(source.readString());
		  p.coordinatesBeanList = source.readArrayList(sellOrderDetailTab.class.getClassLoader());
         return p;
      }  

      @Override  
      public SellOrderDetail_New[] newArray(int size)
      {  
          return new SellOrderDetail_New[size];
      }  
   }; 
   
   	@Override
	public void writeToParcel(Parcel p, int arg1) 
    {
         p.writeString(id);
         p.writeString(uuid);
         p.writeString(saleid);
         p.writeString(batchTime);
         p.writeString(uid);
         p.writeString(parkid);
         p.writeString(parkname);
         p.writeString(areaid);
         p.writeString(areaname);
         p.writeString(contractid);
         p.writeString(contractname);
         p.writeString(planprice);
         p.writeString(actualprice);
         p.writeString(planlat);
         p.writeString(planlatlngsize);
         p.writeString(actuallatlngsize);
         p.writeString(planlng);
         p.writeString(actuallat);
         p.writeString(actuallng);
         p.writeString(plannumber);
         p.writeString(planweight);
         p.writeString(actualnumber);
         p.writeString(actualweight);
         p.writeString(plannote);
         p.writeString(actualnote);
         p.writeString(reg);
         p.writeString(status);
         p.writeString(isSoldOut);
         p.writeString(xxzt);
         p.writeString(type);
         p.writeString(year);
		p.writeList(coordinatesBeanList);
	}
    @Override
	public int describeContents()
	{
		return 0;
	}
}