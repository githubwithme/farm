package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/5/23.
 */
@Table(name = "PGBean")
public class PGBean implements Parcelable
{
    @Id
    @NoAutoIncrement
    public String userId="";
    public String uid="";

    public String parkId="";
    public String isConfirm="";


    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static final Creator<PGBean> CREATOR = new Creator()
    {
        @Override
        public PGBean createFromParcel(Parcel source)
        {
            PGBean p = new PGBean();
            p.setUserId(source.readString());
            p.setUid(source.readString());
            p.setParkId(source.readString());
            p.setIsConfirm(source.readString());

            return p;


        }

        @Override
        public PGBean[] newArray(int size)
        {
            return new PGBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(userId);
        p.writeString(uid);
        p.writeString(parkId);
        p.writeString(isConfirm);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
