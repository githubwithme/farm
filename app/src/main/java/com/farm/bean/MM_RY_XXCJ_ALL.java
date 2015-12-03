

package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 *
 * Description: MM_RY_XXCJ_ALL 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0 时间 2015-3-3
 */
@Table(name="MM_RY_XXCJ_ALL")
public class MM_RY_XXCJ_ALL implements Parcelable 
{
  public static final Parcelable.Creator<MM_RY_XXCJ_ALL> CREATOR = new Creator()
  {  
      @Override  
      public MM_RY_XXCJ_ALL createFromParcel(Parcel source)
      {  
         // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
    	 MM_RY_XXCJ_ALL p = new MM_RY_XXCJ_ALL();
         p.setId(source.readInt());
         p.setSCZT(source.readInt());
         p.setID(source.readString());
         p.setUserID(source.readString());
         p.setIntMonth(source.readString());
         p.setRWS(source.readString());
         p.setHYS(source.readString());
         p.setSJSBS(source.readString());
         p.setSJCLS(source.readString());
         p.setRKS(source.readString());
         p.setCZRKS(source.readString());
         p.setLDRKS(source.readString());
         p.setWGRS(source.readString());
         p.setLDS(source.readString());
         p.setFJS(source.readString());
         p.setQYS(source.readString());
         return p;  
      }  

      @Override  
      public MM_RY_XXCJ_ALL[] newArray(int size) 
      {  
          return new MM_RY_XXCJ_ALL[size];  
      }  
  }; 

	

    int id;
    int SCZT;
	/** identifier field */

	private String ID;
    
	/** identifier field */

	private String UserID;
    
	/** identifier field */

	private String IntMonth;
    
	/** identifier field */

	private String RWS;
    
	/** identifier field */

	private String HYS;
    
	/** identifier field */

	private String SJSBS;
    
	/** identifier field */

	private String SJCLS;
    
	/** identifier field */

	private String RKS;
    
	/** identifier field */

	private String CZRKS;
    
	/** identifier field */

	private String LDRKS;
    
	/** identifier field */

	private String WGRS;
    
	/** identifier field */

	private String LDS;
    
	/** identifier field */

	private String FJS;
    
	/** identifier field */

	private String QYS;
    


	public void setId(int id) 
    {
		this.id = id;
	}
	public int getId() 
    {
		return id;
	}
	public void setSCZT(int sCZT) 
	{
		SCZT = sCZT;
	}
	public int getSCZT() 
	{
		return SCZT;
	}
	/**
	 * @return 返回 
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param ID 要设置的  
	 */
	public void setID(String ID) {
		this.ID = ID;
	}
	/**
	 * @return 返回 
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * @param UserID 要设置的  
	 */
	public void setUserID(String UserID) {
		this.UserID = UserID;
	}
	/**
	 * @return 返回 
	 */
	public String getIntMonth() {
		return IntMonth;
	}

	/**
	 * @param IntMonth 要设置的  
	 */
	public void setIntMonth(String IntMonth) {
		this.IntMonth = IntMonth;
	}
	/**
	 * @return 返回 任务数
	 */
	public String getRWS() {
		return RWS;
	}

	/**
	 * @param RWS 要设置的  任务数
	 */
	public void setRWS(String RWS) {
		this.RWS = RWS;
	}
	/**
	 * @return 返回 会议数
	 */
	public String getHYS() {
		return HYS;
	}

	/**
	 * @param HYS 要设置的  会议数
	 */
	public void setHYS(String HYS) {
		this.HYS = HYS;
	}
	/**
	 * @return 返回 事件上报数
	 */
	public String getSJSBS() {
		return SJSBS;
	}

	/**
	 * @param SJSBS 要设置的  事件上报数
	 */
	public void setSJSBS(String SJSBS) {
		this.SJSBS = SJSBS;
	}
	/**
	 * @return 返回 事件处理数
	 */
	public String getSJCLS() {
		return SJCLS;
	}

	/**
	 * @param SJCLS 要设置的  事件处理数
	 */
	public void setSJCLS(String SJCLS) {
		this.SJCLS = SJCLS;
	}
	/**
	 * @return 返回 人口
	 */
	public String getRKS() {
		return RKS;
	}

	/**
	 * @param RKS 要设置的  人口
	 */
	public void setRKS(String RKS) {
		this.RKS = RKS;
	}
	/**
	 * @return 返回 常住人口数
	 */
	public String getCZRKS() {
		return CZRKS;
	}

	/**
	 * @param CZRKS 要设置的  常住人口数
	 */
	public void setCZRKS(String CZRKS) {
		this.CZRKS = CZRKS;
	}
	/**
	 * @return 返回 流动人口数
	 */
	public String getLDRKS() {
		return LDRKS;
	}

	/**
	 * @param LDRKS 要设置的  流动人口数
	 */
	public void setLDRKS(String LDRKS) {
		this.LDRKS = LDRKS;
	}
	/**
	 * @return 返回 外国人数
	 */
	public String getWGRS() {
		return WGRS;
	}

	/**
	 * @param WGRS 要设置的  外国人数
	 */
	public void setWGRS(String WGRS) {
		this.WGRS = WGRS;
	}
	/**
	 * @return 返回 楼栋数
	 */
	public String getLDS() {
		return LDS;
	}

	/**
	 * @param LDS 要设置的  楼栋数
	 */
	public void setLDS(String LDS) {
		this.LDS = LDS;
	}
	/**
	 * @return 返回 房间数
	 */
	public String getFJS() {
		return FJS;
	}

	/**
	 * @param FJS 要设置的  房间数
	 */
	public void setFJS(String FJS) {
		this.FJS = FJS;
	}
	/**
	 * @return 返回 企业数
	 */
	public String getQYS() {
		return QYS;
	}

	/**
	 * @param QYS 要设置的  企业数
	 */
	public void setQYS(String QYS) {
		this.QYS = QYS;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
    	@Override
	public int describeContents()
    {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) 
    {
	
	}
}
