package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * 
 * Description: jobtab 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "jobtab")
public class jobtab implements Parcelable
{
	public String Id;
	public String uId;
	public String regDate;
	public String commandID;
	public String nongziName;
	public String amount;
	public String duration;
	public String jobNote;
	public String jobNature;
	public String jobFromId;
	public String jobFromName;
	public String jobStatus;
	public String audioJobExecPath;
	public String parkId;
	public String parkName;
	public String areaId;
	public String areaName;
	public String assessuserId;
	public String assessuserName;
	public String assessDate;
	public String assessScore;
	public String assessNote;
	public String audioJobAssessPath;
	public String isDelete;
	public String DeleteDate;
	public String AYear;
	public String stdJobType;
	public String stdJobTypeName;
	public String stdJobId;
	public String stdJobName;
	public String commFromVPath;
	// 自定义
	public String importance;
	public String jobvidioCount;
	public String jobCount;
	public List<String> PF;
	public String percent;


	public String getPercent()
	{
		return percent;
	}

	public void setPercent(String percent)
	{
		this.percent = percent;
	}

	public List<String> getPF()
	{
		return PF;
	}

	public void setPF(List<String> PF)
	{
		this.PF = PF;
	}

	public String getJobCount()
	{
		return jobCount;
	}

	public void setJobCount(String jobCount)
	{
		this.jobCount = jobCount;
	}

	public void setJobvidioCount(String jobvidioCount)
	{
		this.jobvidioCount = jobvidioCount;
	}

	public String getJobvidioCount()
	{
		return jobvidioCount;
	}

	public void setImportance(String importance)
	{
		this.importance = importance;
	}

	public String getImportance()
	{
		return importance;
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

	public String getcommandID()
	{
		return commandID;
	}

	public void setcommandID(String commandID)
	{
		this.commandID = commandID;
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

	public String getduration()
	{
		return duration;
	}

	public void setduration(String duration)
	{
		this.duration = duration;
	}

	public String getjobNote()
	{
		return jobNote;
	}

	public void setjobNote(String jobNote)
	{
		this.jobNote = jobNote;
	}

	public String getjobNature()
	{
		return jobNature;
	}

	public void setjobNature(String jobNature)
	{
		this.jobNature = jobNature;
	}

	public String getjobFromId()
	{
		return jobFromId;
	}

	public void setjobFromId(String jobFromId)
	{
		this.jobFromId = jobFromId;
	}

	public String getjobFromName()
	{
		return jobFromName;
	}

	public void setjobFromName(String jobFromName)
	{
		this.jobFromName = jobFromName;
	}

	public String getjobStatus()
	{
		return jobStatus;
	}

	public void setjobStatus(String jobStatus)
	{
		this.jobStatus = jobStatus;
	}

	public String getaudioJobExecPath()
	{
		return audioJobExecPath;
	}

	public void setaudioJobExecPath(String audioJobExecPath)
	{
		this.audioJobExecPath = audioJobExecPath;
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

	public String getassessuserId()
	{
		return assessuserId;
	}

	public void setassessuserId(String assessuserId)
	{
		this.assessuserId = assessuserId;
	}

	public String getassessuserName()
	{
		return assessuserName;
	}

	public void setassessuserName(String assessuserName)
	{
		this.assessuserName = assessuserName;
	}

	public String getassessDate()
	{
		return assessDate;
	}

	public void setassessDate(String assessDate)
	{
		this.assessDate = assessDate;
	}

	public String getassessScore()
	{
		return assessScore;
	}

	public void setassessScore(String assessScore)
	{
		this.assessScore = assessScore;
	}

	public String getassessNote()
	{
		return assessNote;
	}

	public void setassessNote(String assessNote)
	{
		this.assessNote = assessNote;
	}

	public String getaudioJobAssessPath()
	{
		return audioJobAssessPath;
	}

	public void setaudioJobAssessPath(String audioJobAssessPath)
	{
		this.audioJobAssessPath = audioJobAssessPath;
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

	public String getcommFromVPath()
	{
		return commFromVPath;
	}

	public void setcommFromVPath(String commFromVPath)
	{
		this.commFromVPath = commFromVPath;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Parcelable.Creator<jobtab> CREATOR = new Creator()
	{
		@Override
		public jobtab createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			jobtab p = new jobtab();
			p.setId(source.readString());
			p.setuId(source.readString());
			p.setregDate(source.readString());
			p.setcommandID(source.readString());
			p.setnongziName(source.readString());
			p.setamount(source.readString());
			p.setduration(source.readString());
			p.setjobNote(source.readString());
			p.setjobNature(source.readString());
			p.setjobFromId(source.readString());
			p.setjobFromName(source.readString());
			p.setjobStatus(source.readString());
			p.setaudioJobExecPath(source.readString());
			p.setparkId(source.readString());
			p.setparkName(source.readString());
			p.setareaId(source.readString());
			p.setareaName(source.readString());
			p.setassessuserId(source.readString());
			p.setassessuserName(source.readString());
			p.setassessDate(source.readString());
			p.setassessScore(source.readString());
			p.setassessNote(source.readString());
			p.setaudioJobAssessPath(source.readString());
			p.setisDelete(source.readString());
			p.setDeleteDate(source.readString());
			p.setAYear(source.readString());
			p.setstdJobType(source.readString());
			p.setstdJobTypeName(source.readString());
			p.setstdJobId(source.readString());
			p.setstdJobName(source.readString());
			p.setcommFromVPath(source.readString());
			p.setImportance(source.readString());
			p.setJobvidioCount(source.readString());
			p.setJobCount(source.readString());
			p.PF = source.readArrayList(List.class.getClassLoader());
			p.setPercent(source.readString());
			return p;
		}

		@Override
		public jobtab[] newArray(int size)
		{
			return new jobtab[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(Id);
		p.writeString(uId);
		p.writeString(regDate);
		p.writeString(commandID);
		p.writeString(nongziName);
		p.writeString(amount);
		p.writeString(duration);
		p.writeString(jobNote);
		p.writeString(jobNature);
		p.writeString(jobFromId);
		p.writeString(jobFromName);
		p.writeString(jobStatus);
		p.writeString(audioJobExecPath);
		p.writeString(parkId);
		p.writeString(parkName);
		p.writeString(areaId);
		p.writeString(areaName);
		p.writeString(assessuserId);
		p.writeString(assessuserName);
		p.writeString(assessDate);
		p.writeString(assessScore);
		p.writeString(assessNote);
		p.writeString(audioJobAssessPath);
		p.writeString(isDelete);
		p.writeString(DeleteDate);
		p.writeString(AYear);
		p.writeString(stdJobType);
		p.writeString(stdJobTypeName);
		p.writeString(stdJobId);
		p.writeString(stdJobName);
		p.writeString(commFromVPath);
		p.writeString(importance);
		p.writeString(jobvidioCount);
		p.writeString(jobCount);
		p.writeList(PF);
		p.writeString(percent);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
