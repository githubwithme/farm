package com.farm.bean;

import android.graphics.drawable.Drawable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-18 下午5:23:49
 * @description :
 */
@Table(name = "FJ_SCFJ")
public class FJ_SCFJ extends Entity
{

	Drawable drawable;
	private boolean HASUPLOAD;
	private String FJBDLJ;
	private String SCLJ;
	int id;
	Boolean Change;

	public void setChange(Boolean change)
	{
		Change = change;
	}

	public Boolean getChange()
	{
		return Change;
	}

	/** identifier field */

	@Id
	private String FJID;

	/** identifier field */

	private String GLID;

	/** identifier field */

	private String GLBM;

	/** identifier field */

	private String FJMC;

	/** identifier field */

	private String FJLJ;

	/** identifier field */

	private String SCSJ;

	/** identifier field */

	private String SCR;

	/** identifier field */

	private String SCRXM;

	/** identifier field */

	private String FJLX;

	/** identifier field */

	private Boolean SFSC;

	/** identifier field */

	private Boolean SCZT;

	public void setDrawable(Drawable drawable)
	{
		this.drawable = drawable;
	}

	public Drawable getDrawable()
	{
		return drawable;
	}

	/**
	 * 包含文件类型如.png
	 * @return
	 */
	public String FJBDLJToFJMC(String FJLJ)
	{
		String fileName = FJLJ.substring(FJLJ.lastIndexOf("/") + 1, FJLJ.length());
		return fileName;
	}

	/**
	 * 服务器数据库填写的路径
	 * 
	 * @param FJBDLJ
	 * @return
	 */
	public String FJBDLJToFJLJ(String FJBDLJ)
	{
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String PATH = "upload/XXCJFJ/" + date + "/" + FJBDLJToFJMC(FJBDLJ);
		return PATH;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setSCLJ(String sCLJ)
	{
		SCLJ = sCLJ;
	}

	public String getSCLJ()
	{
		return SCLJ;
	}

	public void setHASUPLOAD(boolean hASUPLOAD)
	{
		HASUPLOAD = hASUPLOAD;
	}

	public boolean getHASUPLOAD()
	{
		return HASUPLOAD;
	}

	public void setFJBDLJ(String fJBDLJ)
	{
		FJBDLJ = fJBDLJ;
	}

	public String getFJBDLJ()
	{
		return FJBDLJ;
	}

	/**
	 * @return 返回 FJID。
	 */
	public String getFJID()
	{
		return FJID;
	}

	/**
	 * @param FJID
	 *            要设置的 FJID。
	 */
	public void setFJID(String FJID)
	{
		this.FJID = FJID;
	}

	/**
	 * @return 返回 GLID。
	 */
	public String getGLID()
	{
		return GLID;
	}

	/**
	 * @param GLID
	 *            要设置的 GLID。
	 */
	public void setGLID(String GLID)
	{
		this.GLID = GLID;
	}

	/**
	 * @return 返回 GLBM。
	 */
	public String getGLBM()
	{
		return GLBM;
	}

	/**
	 * @param GLBM
	 *            要设置的 GLBM。
	 */
	public void setGLBM(String GLBM)
	{
		this.GLBM = GLBM;
	}

	/**
	 * @return 返回 FJMC。
	 */
	public String getFJMC()
	{
		return FJMC;
	}

	/**
	 * @param FJMC
	 *            要设置的 FJMC。
	 */
	public void setFJMC(String FJMC)
	{
		this.FJMC = FJMC;
	}

	/**
	 * @return 返回 FJLJ。
	 */
	public String getFJLJ()
	{
		return FJLJ;
	}

	/**
	 * @param FJLJ
	 *            要设置的 FJLJ。
	 */
	public void setFJLJ(String FJLJ)
	{
		this.FJLJ = FJLJ;
	}

	/**
	 * @return 返回 SCSJ。
	 */
	public String getSCSJ()
	{
		return SCSJ;
	}

	/**
	 * @param SCSJ
	 *            要设置的 SCSJ。
	 */
	public void setSCSJ(String SCSJ)
	{
		this.SCSJ = SCSJ;
	}

	/**
	 * @return 返回 SCR。
	 */
	public String getSCR()
	{
		return SCR;
	}

	/**
	 * @param SCR
	 *            要设置的 SCR。
	 */
	public void setSCR(String SCR)
	{
		this.SCR = SCR;
	}

	/**
	 * @return 返回 SCRXM。
	 */
	public String getSCRXM()
	{
		return SCRXM;
	}

	/**
	 * @param SCRXM
	 *            要设置的 SCRXM。
	 */
	public void setSCRXM(String SCRXM)
	{
		this.SCRXM = SCRXM;
	}

	/**
	 * @return 返回 FJLX。
	 */
	public String getFJLX()
	{
		return FJLX;
	}

	/**
	 * @param FJLX
	 *            要设置的 FJLX。
	 */
	public void setFJLX(String FJLX)
	{
		this.FJLX = FJLX;
	}

	public void setSFSC(Boolean sFSC)
	{
		SFSC = sFSC;
	}

	public Boolean getSFSC()
	{
		return SFSC;
	}

	public void setSCZT(Boolean sCZT)
	{
		SCZT = sCZT;
	}

	public Boolean getSCZT()
	{
		return SCZT;
	}

	public boolean equals(Object o)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
