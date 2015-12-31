package com.farm.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: type.java 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "DataSet")
public class DataSet extends Entity
{
    @Id
    int id;
    String firstid;
    String firsttype;
    String secondid;
    String secondtype;
    String thirdid;
    String thirdtype;


    public void setFirstid(String firstid)
    {
        this.firstid = firstid;
    }

    public String getFirstid()
    {
        return firstid;
    }

    public void setSecondid(String secondid)
    {
        this.secondid = secondid;
    }

    public String getSecondid()
    {
        return secondid;
    }

    public void setThirdid(String thirdid)
    {
        this.thirdid = thirdid;
    }

    public String getThirdid()
    {
        return thirdid;
    }

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
