package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by shuwenouwan on 2016/8/3.
 */
public class FilferBean_Sale implements Parcelable
{
    String type;

    protected FilferBean_Sale(Parcel in)
    {
        type = in.readString();
        listdata = in.createStringArrayList();
    }

    public static final Creator<FilferBean_Sale> CREATOR = new Creator<FilferBean_Sale>()
    {
        @Override
        public FilferBean_Sale createFromParcel(Parcel in)
        {
            return new FilferBean_Sale(in);
        }

        @Override
        public FilferBean_Sale[] newArray(int size)
        {
            return new FilferBean_Sale[size];
        }
    };

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<String> getListdata()
    {
        return listdata;
    }

    public void setListdata(List<String> listdata)
    {
        this.listdata = listdata;
    }

    List<String> listdata;

    public FilferBean_Sale()
    {
        super();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(type);
        parcel.writeStringList(listdata);
    }
}
