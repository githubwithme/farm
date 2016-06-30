package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by hasee on 2016/6/12.
 */
@Table(name = "Purchaser")
public class Purchaser implements Parcelable
{
    public String id;
    public String uid;
    public String name;
    public String telephone;
    public String address;
    public String mailbox;
    public String note;
    public String regDate;
    public String userType;
    public String remark2;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getMailbox()
    {
        return mailbox;
    }

    public void setMailbox(String mailbox)
    {
        this.mailbox = mailbox;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getRemark2()
    {
        return remark2;
    }

    public void setRemark2(String remark2)
    {
        this.remark2 = remark2;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
    public static final Creator<Purchaser> CREATOR = new Creator()
    {
        @Override
        public Purchaser createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Purchaser p = new Purchaser();
            p.setId(source.readString());
            p.setUid(source.readString());
            p.setName(source.readString());
            p.setTelephone(source.readString());
            p.setAddress(source.readString());
            p.setMailbox(source.readString());
            p.setNote(source.readString());
            p.setRegDate(source.readString());
            p.setUserType(source.readString());
            p.setRemark2(source.readString());

            return p;
        }

        @Override
        public Purchaser[] newArray(int size)
        {
            return new Purchaser[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uid);
        p.writeString(name);
        p.writeString(telephone);
        p.writeString(address);
        p.writeString(mailbox);
        p.writeString(note);
        p.writeString(regDate);
        p.writeString(userType);
        p.writeString(remark2);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
