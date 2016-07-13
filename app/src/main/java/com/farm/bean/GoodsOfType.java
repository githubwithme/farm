package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by hasee on 2016/7/12.
 */
@Table(name = "GoodsOfType")
public class GoodsOfType implements Parcelable
{


    public String Id;
    public String uId;
    public String goodsTypeName;
    public String goodsTypeNote ;
    public String regDate;


    public  String goodsType;
    public  String typeName;
    public  String typeNote;


    public String getGoodsType()
    {
        return goodsType;
    }

    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeNote()
    {
        return typeNote;
    }

    public void setTypeNote(String typeNote)
    {
        this.typeNote = typeNote;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getGoodsTypeName()
    {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName)
    {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGoodsTypeNote()
    {
        return goodsTypeNote;
    }

    public void setGoodsTypeNote(String goodsTypeNote)
    {
        this.goodsTypeNote = goodsTypeNote;
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public static final Creator<GoodsOfType> CREATOR = new Creator()
    {
        @Override
        public GoodsOfType createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            GoodsOfType p = new GoodsOfType();
            p.setId(source.readString());
            p.setuId(source.readString());
            p.setGoodsTypeName(source.readString());
            p.setGoodsTypeNote(source.readString());
            p.setRegDate(source.readString());
            p.setGoodsType(source.readString());
            p.setGoodsTypeName(source.readString());
            p.setGoodsTypeNote(source.readString());

            return  p;
        }

        @Override
        public GoodsOfType[] newArray(int size)
        {
            return new GoodsOfType[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {

        p.writeString(Id);
        p.writeString(uId);
        p.writeString(goodsTypeName);
        p.writeString(goodsTypeNote);
        p.writeString(regDate);
        p.writeString(goodsType);
        p.writeString(typeName);
        p.writeString(typeNote);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
