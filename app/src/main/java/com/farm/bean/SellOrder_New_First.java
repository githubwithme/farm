package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by hasee on 2016/7/1.
 */
@Table(name = "SellOrder_New_First")
public class SellOrder_New_First implements Parcelable
{
    public String id;
    public String sellOrderId;
    public String qualityWaterWeight;     //正品带水重
    public String qualityNetWeight;//正品净重
    public String qualityBalance;// 正品结算金额
    public String defectWaterWeight;//次品带水重
    public String defectNetWeight;//次品净重
    public String defectBalance;//次品结算金额

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSellOrderId()
    {
        return sellOrderId;
    }

    public void setSellOrderId(String sellOrderId)
    {
        this.sellOrderId = sellOrderId;
    }

    public String getQualityWaterWeight()
    {
        return qualityWaterWeight;
    }

    public void setQualityWaterWeight(String qualityWaterWeight)
    {
        this.qualityWaterWeight = qualityWaterWeight;
    }

    public String getQualityNetWeight()
    {
        return qualityNetWeight;
    }

    public void setQualityNetWeight(String qualityNetWeight)
    {
        this.qualityNetWeight = qualityNetWeight;
    }

    public String getQualityBalance()
    {
        return qualityBalance;
    }

    public void setQualityBalance(String qualityBalance)
    {
        this.qualityBalance = qualityBalance;
    }

    public String getDefectWaterWeight()
    {
        return defectWaterWeight;
    }

    public void setDefectWaterWeight(String defectWaterWeight)
    {
        this.defectWaterWeight = defectWaterWeight;
    }

    public String getDefectNetWeight()
    {
        return defectNetWeight;
    }

    public void setDefectNetWeight(String defectNetWeight)
    {
        this.defectNetWeight = defectNetWeight;
    }

    public String getDefectBalance()
    {
        return defectBalance;
    }

    public void setDefectBalance(String defectBalance)
    {
        this.defectBalance = defectBalance;
    }


    public static final Creator<SellOrder_New_First> CREATOR = new Creator()
    {
        @Override
        public SellOrder_New_First createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            SellOrder_New_First p = new SellOrder_New_First();

            p.setId(source.readString());//仓库物资
            p.setSellOrderId(source.readString());
            p.setQualityWaterWeight(source.readString());
            p.setQualityNetWeight(source.readString());
            p.setQualityBalance(source.readString());
            p.setDefectWaterWeight(source.readString());
            p.setDefectNetWeight(source.readString());
            p.setDefectBalance(source.readString());


            return  p;
        }

        @Override
        public SellOrder_New_First[] newArray(int size)
        {
            return new SellOrder_New_First[size];
        }
    };
    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(sellOrderId);
        p.writeString(qualityWaterWeight);
        p.writeString(qualityNetWeight);
        p.writeString(qualityBalance);
        p.writeString(defectWaterWeight);
        p.writeString(defectNetWeight);
        p.writeString(defectBalance);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
