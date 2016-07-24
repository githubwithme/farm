package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/5/24.
 */
@Table(name = "ContractGoodsUsed")
public class ContractGoodsUsed implements Parcelable
{
    public String commandId;//指令id
    public String uuid;///物资使用记录唯一标识
    public String uId;//农场id
    public String parkId;//园区id
    public String parkName;//园区id
    public String areaId;//片区id
    public String areaName;//片区id
    public String contractId;//承包区id
    public String contractName;//承包区id
    public String registerId;//登记者id
    public String registerName;//登记者姓名
    public String goodsUsedDate;//物资使用时间
    public String storehouseId;//仓库id
    public String storeName;//仓库名称
    public String batchNumber;//物资批次id
    public String batchName;//物资批次名称
    public String goodsId;//物资id
    public String goodsName;//物资名称
    public String firs;//物资一级单位
    public String firsNum;//物资一级数量
    public String sec;
    public String secNum;
    public String three;
    public String threeNum;
    public String goodsValues;//物资价值

    public ContractGoodsUsed()
    {
        super();
    }

    protected ContractGoodsUsed(Parcel in)
    {
        commandId = in.readString();
        uuid = in.readString();
        uId = in.readString();
        parkId = in.readString();
        parkName = in.readString();
        areaId = in.readString();
        areaName = in.readString();
        contractId = in.readString();
        contractName = in.readString();
        registerId = in.readString();
        registerName = in.readString();
        goodsUsedDate = in.readString();
        storehouseId = in.readString();
        storeName = in.readString();
        batchNumber = in.readString();
        batchName = in.readString();
        goodsId = in.readString();
        goodsName = in.readString();
        firs = in.readString();
        firsNum = in.readString();
        sec = in.readString();
        secNum = in.readString();
        three = in.readString();
        threeNum = in.readString();
        goodsValues = in.readString();
    }

    public static final Creator<ContractGoodsUsed> CREATOR = new Creator<ContractGoodsUsed>()
    {
        @Override
        public ContractGoodsUsed createFromParcel(Parcel in)
        {
            return new ContractGoodsUsed(in);
        }

        @Override
        public ContractGoodsUsed[] newArray(int size)
        {
            return new ContractGoodsUsed[size];
        }
    };

    public String getCommandId()
    {
        return commandId;
    }

    public void setCommandId(String commandId)
    {
        this.commandId = commandId;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getParkId()
    {
        return parkId;
    }

    public void setParkId(String parkId)
    {
        this.parkId = parkId;
    }

    public String getParkName()
    {
        return parkName;
    }

    public void setParkName(String parkName)
    {
        this.parkName = parkName;
    }

    public String getAreaId()
    {
        return areaId;
    }

    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getContractId()
    {
        return contractId;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getContractName()
    {
        return contractName;
    }

    public void setContractName(String contractName)
    {
        this.contractName = contractName;
    }

    public String getRegisterId()
    {
        return registerId;
    }

    public void setRegisterId(String registerId)
    {
        this.registerId = registerId;
    }

    public String getRegisterName()
    {
        return registerName;
    }

    public void setRegisterName(String registerName)
    {
        this.registerName = registerName;
    }

    public String getGoodsUsedDate()
    {
        return goodsUsedDate;
    }

    public void setGoodsUsedDate(String goodsUsedDate)
    {
        this.goodsUsedDate = goodsUsedDate;
    }

    public String getStorehouseId()
    {
        return storehouseId;
    }

    public void setStorehouseId(String storehouseId)
    {
        this.storehouseId = storehouseId;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    public String getBatchNumber()
    {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber)
    {
        this.batchNumber = batchNumber;
    }

    public String getBatchName()
    {
        return batchName;
    }

    public void setBatchName(String batchName)
    {
        this.batchName = batchName;
    }

    public String getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(String goodsId)
    {
        this.goodsId = goodsId;
    }

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getFirs()
    {
        return firs;
    }

    public void setFirs(String firs)
    {
        this.firs = firs;
    }

    public String getFirsNum()
    {
        return firsNum;
    }

    public void setFirsNum(String firsNum)
    {
        this.firsNum = firsNum;
    }

    public String getSec()
    {
        return sec;
    }

    public void setSec(String sec)
    {
        this.sec = sec;
    }

    public String getSecNum()
    {
        return secNum;
    }

    public void setSecNum(String secNum)
    {
        this.secNum = secNum;
    }

    public String getThree()
    {
        return three;
    }

    public void setThree(String three)
    {
        this.three = three;
    }

    public String getThreeNum()
    {
        return threeNum;
    }

    public void setThreeNum(String threeNum)
    {
        this.threeNum = threeNum;
    }

    public String getGoodsValues()
    {
        return goodsValues;
    }

    public void setGoodsValues(String goodsValues)
    {
        this.goodsValues = goodsValues;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(commandId);
        dest.writeString(uuid);
        dest.writeString(uId);
        dest.writeString(parkId);
        dest.writeString(parkName);
        dest.writeString(areaId);
        dest.writeString(areaName);
        dest.writeString(contractId);
        dest.writeString(contractName);
        dest.writeString(registerId);
        dest.writeString(registerName);
        dest.writeString(goodsUsedDate);
        dest.writeString(storehouseId);
        dest.writeString(storeName);
        dest.writeString(batchNumber);
        dest.writeString(batchName);
        dest.writeString(goodsId);
        dest.writeString(goodsName);
        dest.writeString(firs);
        dest.writeString(firsNum);
        dest.writeString(sec);
        dest.writeString(secNum);
        dest.writeString(three);
        dest.writeString(threeNum);
        dest.writeString(goodsValues);
    }
}
