package com.farm.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * Description: type.java 实体类</p>
 * 
 * Copyright: Copyright (c) 2015
 * 
 * Company: 广州海川信息科技有限公司
 * 
 * @version 1.0
 */
@Table(name = "SelectItem")
public class SelectItem extends Entity
{
	@Id
	int id;
	String firsttype;
	String secondtype;
	String thirdtype;

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setFirsttype(String firsttype)
	{
		this.firsttype = firsttype;
	}

	public String getFirsttype()
	{
		return firsttype;
	}

	public void setSecondtype(String secondtype)
	{
		this.secondtype = secondtype;
	}

	public String getSecondtype()
	{
		return secondtype;
	}

	public void setThirdtype(String thirdtype)
	{
		this.thirdtype = thirdtype;
	}

	public String getThirdtype()
	{
		return thirdtype;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

}
