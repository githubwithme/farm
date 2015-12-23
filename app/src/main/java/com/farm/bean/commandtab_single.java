package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * Description: commandtab 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "commandtab")
public class commandtab_single implements Parcelable
{
	public String Id;
	public String uId;
	public String regDate;
	public String stdJobType;
	public String stdJobTypeName;
	public String stdJobId;
	public String stdJobName;
	public String nongziName;
	public String amount;
	public String commNote;
	public String execLevel;
	public String commFromID;
	public String commFromName;
	public String commFromVPath;
	public String commDays;
	public String commComDate;
	public String isDelete;
	public String DeleteDate;
	public String AYear;
	public String importance;
	// 自定义
	public String statusid;
	public String parkId;
	public String parkName;
	public String areaId;
	public String areaName;
	public String feedbackuserId;
	public String feedbackuserName;
	public String feedbackNote;
	public String feedbackaudioPath;
	public String feedbackDate;
	public String commStatus;
	public String confirmDate;
	public String iCount;
	public String comvidioCount;
	public String vidioCount;


	private static commandtab_single singleton = new commandtab_single();

	private commandtab_single()
	{
	}

	public static commandtab_single getInstance()
	{
		return singleton;
	}

	public void setVidioCount(String vidioCount)
	{
		this.vidioCount = vidioCount;
	}
	public String getVidioCount()
	{
		return vidioCount;
	}
	public void setComvidioCount(String comvidioCount)
	{
		this.comvidioCount = comvidioCount;
	}

	public String getComvidioCount()
	{
		return comvidioCount;
	}

	public void setiCount(String iCount)
	{
		this.iCount = iCount;
	}

	public String getiCount()
	{
		return iCount;
	}

	public void setStatusid(String statusid)
	{
		this.statusid = statusid;
	}

	public String getStatusid()
	{
		return statusid;
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

	public String getareaId()
	{
		return areaId;
	}

	public void setareaId(String areaId)
	{
		this.areaId = areaId;
	}

	public String getareaName()
	{
		return areaName;
	}

	public void setareaName(String areaName)
	{
		this.areaName = areaName;
	}

	public String getfeedbackuserId()
	{
		return feedbackuserId;
	}

	public void setfeedbackuserId(String feedbackuserId)
	{
		this.feedbackuserId = feedbackuserId;
	}

	public String getfeedbackuserName()
	{
		return feedbackuserName;
	}

	public void setfeedbackuserName(String feedbackuserName)
	{
		this.feedbackuserName = feedbackuserName;
	}

	public String getfeedbackNote()
	{
		return feedbackNote;
	}

	public void setfeedbackNote(String feedbackNote)
	{
		this.feedbackNote = feedbackNote;
	}

	public String getfeedbackaudioPath()
	{
		return feedbackaudioPath;
	}

	public void setfeedbackaudioPath(String feedbackaudioPath)
	{
		this.feedbackaudioPath = feedbackaudioPath;
	}

	public String getfeedbackDate()
	{
		return feedbackDate;
	}

	public void setfeedbackDate(String feedbackDate)
	{
		this.feedbackDate = feedbackDate;
	}

	public String getcommStatus()
	{
		return commStatus;
	}

	public void setcommStatus(String commStatus)
	{
		this.commStatus = commStatus;
	}

	public String getconfirmDate()
	{
		return confirmDate;
	}

	public void setconfirmDate(String confirmDate)
	{
		this.confirmDate = confirmDate;
	}

	public String getId()
	{
		return Id;
	}

	public void setId(String Id)
	{
		this.Id = Id;
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

	public String getstdJobType()
	{
		return stdJobType;
	}

	public void setstdJobType(String stdJobType)
	{
		this.stdJobType = stdJobType;
	}

	public String getstdJobTypeName()
	{
		return stdJobTypeName;
	}

	public void setstdJobTypeName(String stdJobTypeName)
	{
		this.stdJobTypeName = stdJobTypeName;
	}

	public String getstdJobId()
	{
		return stdJobId;
	}

	public void setstdJobId(String stdJobId)
	{
		this.stdJobId = stdJobId;
	}

	public String getstdJobName()
	{
		return stdJobName;
	}

	public void setstdJobName(String stdJobName)
	{
		this.stdJobName = stdJobName;
	}

	public String getnongziName()
	{
		return nongziName;
	}

	public void setnongziName(String nongziName)
	{
		this.nongziName = nongziName;
	}

	public String getamount()
	{
		return amount;
	}

	public void setamount(String amount)
	{
		this.amount = amount;
	}

	public String getcommNote()
	{
		return commNote;
	}

	public void setcommNote(String commNote)
	{
		this.commNote = commNote;
	}

	public String getexecLevel()
	{
		return execLevel;
	}

	public void setexecLevel(String execLevel)
	{
		this.execLevel = execLevel;
	}

	public String getcommFromID()
	{
		return commFromID;
	}

	public void setcommFromID(String commFromID)
	{
		this.commFromID = commFromID;
	}

	public String getcommFromName()
	{
		return commFromName;
	}

	public void setcommFromName(String commFromName)
	{
		this.commFromName = commFromName;
	}

	public String getcommFromVPath()
	{
		return commFromVPath;
	}

	public void setcommFromVPath(String commFromVPath)
	{
		this.commFromVPath = commFromVPath;
	}

	public String getcommDays()
	{
		return commDays;
	}

	public void setcommDays(String commDays)
	{
		this.commDays = commDays;
	}

	public String getcommComDate()
	{
		return commComDate;
	}

	public void setcommComDate(String commComDate)
	{
		this.commComDate = commComDate;
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

	public String getimportance()
	{
		return importance;
	}

	public void setimportance(String importance)
	{
		this.importance = importance;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<commandtab_single> CREATOR = new Creator()
	{
		@Override
		public commandtab_single createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			commandtab_single p = new commandtab_single();
			p.setId(source.readString());
			p.setuId(source.readString());
			p.setregDate(source.readString());
			p.setstdJobType(source.readString());
			p.setstdJobTypeName(source.readString());
			p.setstdJobId(source.readString());
			p.setstdJobName(source.readString());
			p.setnongziName(source.readString());
			p.setamount(source.readString());
			p.setcommNote(source.readString());
			p.setexecLevel(source.readString());
			p.setcommFromID(source.readString());
			p.setcommFromName(source.readString());
			p.setcommFromVPath(source.readString());
			p.setcommDays(source.readString());
			p.setcommComDate(source.readString());
			p.setisDelete(source.readString());
			p.setDeleteDate(source.readString());
			p.setAYear(source.readString());
			p.setimportance(source.readString());
			p.setStatusid(source.readString());
			p.setparkId(source.readString());
			p.setparkName(source.readString());
			p.setareaId(source.readString());
			p.setareaName(source.readString());
			p.setfeedbackuserId(source.readString());
			p.setfeedbackuserName(source.readString());
			p.setfeedbackNote(source.readString());
			p.setfeedbackaudioPath(source.readString());
			p.setfeedbackDate(source.readString());
			p.setcommStatus(source.readString());
			p.setconfirmDate(source.readString());
			p.setiCount(source.readString());
			p.setComvidioCount(source.readString());
			p.setVidioCount(source.readString());
			return p;
		}

		@Override
		public commandtab_single[] newArray(int size)
		{
			return new commandtab_single[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(Id);
		p.writeString(uId);
		p.writeString(regDate);
		p.writeString(stdJobType);
		p.writeString(stdJobTypeName);
		p.writeString(stdJobId);
		p.writeString(stdJobName);
		p.writeString(nongziName);
		p.writeString(amount);
		p.writeString(commNote);
		p.writeString(execLevel);
		p.writeString(commFromID);
		p.writeString(commFromName);
		p.writeString(commFromVPath);
		p.writeString(commDays);
		p.writeString(commComDate);
		p.writeString(isDelete);
		p.writeString(DeleteDate);
		p.writeString(AYear);
		p.writeString(importance);
		p.writeString(statusid);
		p.writeString(parkId);
		p.writeString(parkName);
		p.writeString(areaId);
		p.writeString(areaName);
		p.writeString(feedbackuserId);
		p.writeString(feedbackuserName);
		p.writeString(feedbackNote);
		p.writeString(feedbackaudioPath);
		p.writeString(feedbackDate);
		p.writeString(commStatus);
		p.writeString(confirmDate);
		p.writeString(iCount);
		p.writeString(comvidioCount);
		p.writeString(vidioCount);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	public void clearAll()
	{
		singleton = new commandtab_single();
	}
}
