package com.farm.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "HaveReadRecord")
public class HaveReadRecord
{
    @Id
    @NoAutoIncrement
    String id;
    String num;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public String getNum()
    {
        return num;
    }
}
