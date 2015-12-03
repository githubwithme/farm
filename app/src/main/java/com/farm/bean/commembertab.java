package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * Description: commembertab 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "commembertab")
public class commembertab implements Parcelable
{
	@Id
	@NoAutoIncrement
	public String Id;
	public String uId;
	public String userName;
	public String userPwd;
	public String realName;
	public String idNumber;
	public String userCell;
	public String userEmail;
	public String userQQ;
	public String userWX;
	public String userlevel;
	public String parkId;
	public String parkName;
	public String areaId;
	public String areaName;
	public String contractid;
	public String contractName;
	public String userlevelName;
	public String nlevel;
	public String imgurl;
	public String userNote;
	public String regDate;
	public String isDelete;
	public String DeleteDate;
	public String AYear;
	public String autoLogin;


	public void setAutoLogin(String autoLogin)
	{
		this.autoLogin = autoLogin;
	}

	public String getAutoLogin()
	{
		return autoLogin;
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

	public String getuserName()
	{
		return userName;
	}

	public void setuserName(String userName)
	{
		this.userName = userName;
	}

	public String getuserPwd()
	{
		return userPwd;
	}

	public void setuserPwd(String userPwd)
	{
		this.userPwd = userPwd;
	}

	public String getrealName()
	{
		return realName;
	}

	public void setrealName(String realName)
	{
		this.realName = realName;
	}

	public String getidNumber()
	{
		return idNumber;
	}

	public void setidNumber(String idNumber)
	{
		this.idNumber = idNumber;
	}

	public String getuserCell()
	{
		return userCell;
	}

	public void setuserCell(String userCell)
	{
		this.userCell = userCell;
	}

	public String getuserEmail()
	{
		return userEmail;
	}

	public void setuserEmail(String userEmail)
	{
		this.userEmail = userEmail;
	}

	public String getuserQQ()
	{
		return userQQ;
	}

	public void setuserQQ(String userQQ)
	{
		this.userQQ = userQQ;
	}

	public String getuserWX()
	{
		return userWX;
	}

	public void setuserWX(String userWX)
	{
		this.userWX = userWX;
	}

	public String getuserlevel()
	{
		return userlevel;
	}

	public void setuserlevel(String userlevel)
	{
		this.userlevel = userlevel;
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

	public String getcontractid()
	{
		return contractid;
	}

	public void setcontractid(String contractid)
	{
		this.contractid = contractid;
	}

	public String getcontractName()
	{
		return contractName;
	}

	public void setcontractName(String contractName)
	{
		this.contractName = contractName;
	}

	public String getuserlevelName()
	{
		return userlevelName;
	}

	public void setuserlevelName(String userlevelName)
	{
		this.userlevelName = userlevelName;
	}

	public String getnlevel()
	{
		return nlevel;
	}

	public void setnlevel(String nlevel)
	{
		this.nlevel = nlevel;
	}

	public String getimgurl()
	{
		return imgurl;
	}

	public void setimgurl(String imgurl)
	{
		this.imgurl = imgurl;
	}

	public String getuserNote()
	{
		return userNote;
	}

	public void setuserNote(String userNote)
	{
		this.userNote = userNote;
	}

	public String getregDate()
	{
		return regDate;
	}

	public void setregDate(String regDate)
	{
		this.regDate = regDate;
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

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Parcelable.Creator<commembertab> CREATOR = new Creator()
	{
		@Override
		public commembertab createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			commembertab p = new commembertab();
			p.setId(source.readString());
			p.setuId(source.readString());
			p.setuserName(source.readString());
			p.setuserPwd(source.readString());
			p.setrealName(source.readString());
			p.setidNumber(source.readString());
			p.setuserCell(source.readString());
			p.setuserEmail(source.readString());
			p.setuserQQ(source.readString());
			p.setuserWX(source.readString());
			p.setuserlevel(source.readString());
			p.setparkId(source.readString());
			p.setparkName(source.readString());
			p.setareaId(source.readString());
			p.setareaName(source.readString());
			p.setcontractid(source.readString());
			p.setcontractName(source.readString());
			p.setuserlevelName(source.readString());
			p.setnlevel(source.readString());
			p.setimgurl(source.readString());
			p.setuserNote(source.readString());
			p.setregDate(source.readString());
			p.setisDelete(source.readString());
			p.setDeleteDate(source.readString());
			p.setAYear(source.readString());
			p.setAutoLogin(source.readString());
			return p;
		}

		@Override
		public commembertab[] newArray(int size)
		{
			return new commembertab[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(Id);
		p.writeString(uId);
		p.writeString(userName);
		p.writeString(userPwd);
		p.writeString(realName);
		p.writeString(idNumber);
		p.writeString(userCell);
		p.writeString(userEmail);
		p.writeString(userQQ);
		p.writeString(userWX);
		p.writeString(userlevel);
		p.writeString(parkId);
		p.writeString(parkName);
		p.writeString(areaId);
		p.writeString(areaName);
		p.writeString(contractid);
		p.writeString(contractName);
		p.writeString(userlevelName);
		p.writeString(nlevel);
		p.writeString(imgurl);
		p.writeString(userNote);
		p.writeString(regDate);
		p.writeString(isDelete);
		p.writeString(DeleteDate);
		p.writeString(AYear);
		p.writeString(autoLogin);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
