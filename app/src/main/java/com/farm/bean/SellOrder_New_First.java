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
    public String total ;//总件数
    public String qualityTotalWeight;//正品总净重
    public String defectTotalWeight;//次品重净重
    public String TotalWeight;//总净重
    public String packFee;//总包装费
    public String carryFee;//总搬运费
    public String totalFee;//总合计金额
    public String personNote;//搬运说明
    public String actualMoney;//实际金额

    public String getActualMoney()
    {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney)
    {
        this.actualMoney = actualMoney;
    }

    public String getPersonNote()
    {
        return personNote;
    }

    public void setPersonNote(String personNote)
    {
        this.personNote = personNote;
    }

    public String getQualityTotalWeight()
    {
        return qualityTotalWeight;
    }

    public void setQualityTotalWeight(String qualityTotalWeight)
    {
        this.qualityTotalWeight = qualityTotalWeight;
    }

    public String getDefectTotalWeight()
    {
        return defectTotalWeight;
    }

    public void setDefectTotalWeight(String defectTotalWeight)
    {
        this.defectTotalWeight = defectTotalWeight;
    }

    public String getTotalWeight()
    {
        return TotalWeight;
    }

    public void setTotalWeight(String totalWeight)
    {
        TotalWeight = totalWeight;
    }

    public String getPackFee()
    {
        return packFee;
    }

    public void setPackFee(String packFee)
    {
        this.packFee = packFee;
    }

    public String getCarryFee()
    {
        return carryFee;
    }

    public void setCarryFee(String carryFee)
    {
        this.carryFee = carryFee;
    }

    public String getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(String totalFee)
    {
        this.totalFee = totalFee;
    }

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

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
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
            p.setTotal(source.readString());
            p.setQualityTotalWeight(source.readString());
            p.setDefectTotalWeight(source.readString());
            p.setTotalWeight(source.readString());
            p.setPackFee(source.readString());
            p.setCarryFee(source.readString());
            p.setTotalFee(source.readString());
            p.setPersonNote(source.readString());
            p.setActualMoney(source.readString());


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
        p.writeString(total);
        p.writeString(qualityTotalWeight);
        p.writeString(defectTotalWeight);
        p.writeString(TotalWeight);
        p.writeString(packFee);
        p.writeString(carryFee);
        p.writeString(totalFee);
        p.writeString(personNote);
        p.writeString(actualMoney);


    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
