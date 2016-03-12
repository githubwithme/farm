package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

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
@Table(name = "DepartmentBean")
public class DepartmentBean implements Parcelable // 与数据库不一致
{
	public String id;
	public String type;
	public String name;

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<DepartmentBean> CREATOR = new Creator()
	{
		@Override
		public DepartmentBean createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			DepartmentBean p = new DepartmentBean();
			p.setId(source.readString());
			p.setType(source.readString());
			p.setName(source.readString());
			return p;
		}

		@Override
		public DepartmentBean[] newArray(int size)
		{
			return new DepartmentBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(type);
		p.writeString(name);

	}

	@Override
	public int describeContents()
	{
		return 0;
	}
}
