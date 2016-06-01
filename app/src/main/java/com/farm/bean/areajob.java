package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Description: areatab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "areajob")
public class areajob implements Parcelable // 与数据库不一致
{
    public String areaname;
    public String jobid;
    public String jobname;
    public String jobtime;


    public void setJobtime(String jobtime)
    {
        this.jobtime = jobtime;
    }

    public String getJobtime()
    {
        return jobtime;
    }

    public void setJobname(String jobname)
    {
        this.jobname = jobname;
    }

    public String getJobname()
    {
        return jobname;
    }

    public void setJobid(String jobid)
    {
        this.jobid = jobid;
    }

    public String getJobid()
    {
        return jobid;
    }

    public void setAreaname(String areaname)
    {
        this.areaname = areaname;
    }

    public String getAreaname()
    {
        return areaname;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<areajob> CREATOR = new Creator()
    {
        @Override
        public areajob createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            areajob p = new areajob();
            p.setAreaname(source.readString());
            p.setJobid(source.readString());
            p.setJobname(source.readString());
            p.setJobtime(source.readString());

            return p;
        }

        @Override
        public areajob[] newArray(int size)
        {
            return new areajob[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(areaname);
        p.writeString(jobid);
        p.writeString(jobname);
        p.writeString(jobtime);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
