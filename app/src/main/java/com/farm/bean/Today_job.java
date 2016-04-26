package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
@Table(name = "Today_job")
public class Today_job implements Parcelable {

    String regisdate;
    public List<jobtab> joblist;

    public List<jobtab> getJoblist() {
        return joblist;
    }

    public void setJoblist(List<jobtab> joblist) {
        this.joblist = joblist;
    }

    public String getRegisdate() {
        return regisdate;
    }

    public void setRegisdate(String regisdate) {
        this.regisdate = regisdate;
    }


    public static final Parcelable.Creator<Today_job> CREATOR = new Creator()
    {
        @Override
        public Today_job createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Today_job p = new Today_job();

            p.joblist = source.readArrayList(plantgrowthtab.class.getClassLoader());
            return p;
        }

        @Override
        public Today_job[] newArray(int size)
        {
            return new Today_job[size];
        }
    };
    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(regisdate);

        p.writeList(joblist);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
