package com.farm.bean;

/**
 * @author :sima
 * @version :1.0
 * @createTime：2015-8-5 下午3:04:57
 * @description :实体类
 */
public class SearchEntity
{
    String uuid;
    String title;
    String date;
    String type;


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }
}
