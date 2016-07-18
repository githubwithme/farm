package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Description: areatab 实体类</p>
 * <p/>
 * Copyright: Copyright (c) 2015
 * <p/>
 * Company: 广州海川信息科技有限公司
 *
 * @version 1.0
 */
@Table(name = "SellOrder_New")
public class SellOrder_New implements Parcelable // 与数据库不一致
{
    public String id;
    public String uid;
    public String infoId;
    @Id
    @NoAutoIncrement
    public String uuid;
    public String batchTime;
    public String selltype;
    public String status;
    public String buyers;   //采购商id//0
    public String address;  //产品发往城市
    public String email;
    public String phone;
    public String price;
    public String number;
    public String weight;
    public String sumvalues;
    public String actualprice = "";
    public String actualweight = "";
    public String actualnumber = "";
    public String actualsumvalues = "";
    public String reg;
    public String saletime;
    public String year;
    public String note;
    public String feedbacknote;
    public String deposit;
    public String xxzt;
    public String producer;
    public String finalpayment;
    public String flashStr;
    public List<SellOrderDetail_New> sellOrderDetailList;
    public List<SellOrderDetail_New> DetailSecLists;

   public  String pactId;
    public String plateNumber; // 车牌号
    public String mainPepole;  // 负责人id
    public String mainPepName;  // 负责人
    public String contractorId ; // 包工头
    public String pickId ;      //  挑工头
    public String carryPrice ;  // 搬运单价
    public String packPrice ;   // 包装单价
    public String defectPrice;   //次品单价
    public String defectNum;   // 次品数量
    public String packPec;   // 包装规格
    public String waitDeposit ;   // 要付订金

    public String buyersId;   // 采购商id
    public String buyersName;   // 采购商名
    public String contractorName;   //  包工头名
    public String pickName ;   //  搬运工名
    public String purchaName ;   // 片管获取采购商名字
    public String purchaTel ;   //采购商电话
    public String purchaMail ;   // 采购商mail
    public String isNeedAudit ;   // 单价，订金的修改
    public String freeDeposit ;   // 免付订金
    public String freeFinalPay ;   // 免付尾款

    public String oldPrice;        //旧的单价
    public String oldCarryPrice;   //旧的搬运价格
    public String oldPackPrice;   //旧的包装价格
    public String oldnumber;     //旧的数量
    public String oldsaletime;     //旧的数量
    public String creatorid;    //创建者id
    public String goodsname;     //物资名字
    //另外一个表
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


    public List<SellOrderDetail_New> getDetailSecLists()
    {
        return DetailSecLists;
    }

    public void setDetailSecLists(List<SellOrderDetail_New> detailSecLists)
    {
        DetailSecLists = detailSecLists;
    }

    public String getInfoId()
    {
        return infoId;
    }

    public void setInfoId(String infoId)
    {
        this.infoId = infoId;
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

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
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

    public String getPersonNote()
    {
        return personNote;
    }

    public void setPersonNote(String personNote)
    {
        this.personNote = personNote;
    }

    public String getActualMoney()
    {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney)
    {
        this.actualMoney = actualMoney;
    }

    public String getPurchaName()
    {
        return purchaName;
    }

    public void setPurchaName(String purchaName)
    {
        this.purchaName = purchaName;
    }

    public String getOldsaletime()
    {
        return oldsaletime;
    }

    public void setOldsaletime(String oldsaletime)
    {
        this.oldsaletime = oldsaletime;
    }

    public String getOldPrice()
    {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice)
    {
        this.oldPrice = oldPrice;
    }

    public String getOldCarryPrice()
    {
        return oldCarryPrice;
    }

    public void setOldCarryPrice(String oldCarryPrice)
    {
        this.oldCarryPrice = oldCarryPrice;
    }

    public String getOldPackPrice()
    {
        return oldPackPrice;
    }

    public void setOldPackPrice(String oldPackPrice)
    {
        this.oldPackPrice = oldPackPrice;
    }

    public String getOldnumber()
    {
        return oldnumber;
    }

    public void setOldnumber(String oldnumber)
    {
        this.oldnumber = oldnumber;
    }

    public String getCreatorid()
    {
        return creatorid;
    }

    public void setCreatorid(String creatorid)
    {
        this.creatorid = creatorid;
    }

    public String getGoodsname()
    {
        return goodsname;
    }

    public void setGoodsname(String goodsname)
    {
        this.goodsname = goodsname;
    }

    public String getFreeFinalPay()
    {
        return freeFinalPay;
    }

    public void setFreeFinalPay(String freeFinalPay)
    {
        this.freeFinalPay = freeFinalPay;
    }

    public String getFreeDeposit()
    {
        return freeDeposit;
    }

    public void setFreeDeposit(String freeDeposit)
    {
        this.freeDeposit = freeDeposit;
    }

    public String getIsNeedAudit()
    {
        return isNeedAudit;
    }

    public void setIsNeedAudit(String isNeedAudit)
    {
        this.isNeedAudit = isNeedAudit;
    }

    public String getPactId()
    {
        return pactId;
    }

    public void setPactId(String pactId)
    {
        this.pactId = pactId;
    }

    public String getPurchaTel()
    {
        return purchaTel;
    }

    public void setPurchaTel(String purchaTel)
    {
        this.purchaTel = purchaTel;
    }

    public String getPurchaMail()
    {
        return purchaMail;
    }

    public void setPurchaMail(String purchaMail)
    {
        this.purchaMail = purchaMail;
    }

    public String getMainPepName()
    {
        return mainPepName;
    }

    public void setMainPepName(String mainPepName)
    {
        this.mainPepName = mainPepName;
    }

    public String getBuyersId()
    {
        return buyersId;
    }

    public void setBuyersId(String buyersId)
    {
        this.buyersId = buyersId;
    }

    public String getBuyersName()
    {
        return buyersName;
    }

    public void setBuyersName(String buyersName)
    {
        this.buyersName = buyersName;
    }

    public String getContractorName()
    {
        return contractorName;
    }

    public void setContractorName(String contractorName)
    {
        this.contractorName = contractorName;
    }

    public String getPickName()
    {
        return pickName;
    }

    public void setPickName(String pickName)
    {
        this.pickName = pickName;
    }

/*    buyersId      //采购商id
            buyersName  // 采购商名
    contractorId   //包公头id
            contractorName // 包工头名
    pickId     //搬运工id
            pickName  //搬运工名*/

    public String getPackPec()
    {
        return packPec;
    }

    public void setPackPec(String packPec)
    {
        this.packPec = packPec;
    }

    public String getWaitDeposit()
    {
        return waitDeposit;
    }

    public void setWaitDeposit(String waitDeposit)
    {
        this.waitDeposit = waitDeposit;
    }

    public String getContractorId()
    {
        return contractorId;
    }

    public void setContractorId(String contractorId)
    {
        this.contractorId = contractorId;
    }

    public String getPickId()
    {
        return pickId;
    }

    public void setPickId(String pickId)
    {
        this.pickId = pickId;
    }

    public String getCarryPrice()
    {
        return carryPrice;
    }

    public void setCarryPrice(String carryPrice)
    {
        this.carryPrice = carryPrice;
    }

    public String getPackPrice()
    {
        return packPrice;
    }

    public void setPackPrice(String packPrice)
    {
        this.packPrice = packPrice;
    }

    public String getDefectPrice()
    {
        return defectPrice;
    }

    public void setDefectPrice(String defectPrice)
    {
        this.defectPrice = defectPrice;
    }

    public String getDefectNum()
    {
        return defectNum;
    }

    public void setDefectNum(String defectNum)
    {
        this.defectNum = defectNum;
    }

    public String getPlateNumber()
    {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber)
    {
        this.plateNumber = plateNumber;
    }

    public String getMainPepole()
    {
        return mainPepole;
    }

    public void setMainPepole(String mainPepole)
    {
        this.mainPepole = mainPepole;
    }

    public String getFlashStr()
    {
        return flashStr;
    }

    public void setFlashStr(String flashStr)
    {
        this.flashStr = flashStr;
    }

    public void setFinalpayment(String finalpayment)
    {
        this.finalpayment = finalpayment;
    }

    public String getFinalpayment()
    {
        return finalpayment;
    }

    public void setProducer(String producer)
    {
        this.producer = producer;
    }

    public String getProducer()
    {
        return producer;
    }

    public void setXxzt(String xxzt)
    {
        this.xxzt = xxzt;
    }

    public String getXxzt()
    {
        return xxzt;
    }

    public void setFeedbacknote(String feedbacknote)
    {
        this.feedbacknote = feedbacknote;
    }

    public String getFeedbacknote()
    {
        return feedbacknote;
    }

    public void setActualprice(String actualprice)
    {
        this.actualprice = actualprice;
    }

    public String getActualprice()
    {
        return actualprice;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setDeposit(String deposit)
    {
        this.deposit = deposit;
    }

    public String getDeposit()
    {
        return deposit;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getYear()
    {
        return year;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setBatchTime(String batchTime)
    {
        this.batchTime = batchTime;
    }

    public String getBatchTime()
    {
        return batchTime;
    }

    public void setSellOrderDetailList(List<SellOrderDetail_New> sellOrderDetailList)
    {
        this.sellOrderDetailList = sellOrderDetailList;
    }

    public List<SellOrderDetail_New> getSellOrderDetailList()
    {
        return sellOrderDetailList;
    }

    public void setSaletime(String saletime)
    {
        this.saletime = saletime;
    }

    public String getSaletime()
    {
        return saletime;
    }

    public void setReg(String reg)
    {
        this.reg = reg;
    }

    public String getReg()
    {
        return reg;
    }

    public void setActualsumvalues(String actualsumvalues)
    {
        this.actualsumvalues = actualsumvalues;
    }

    public String getActualsumvalues()
    {
        return actualsumvalues;
    }

    public void setActualnumber(String actualnumber)
    {
        this.actualnumber = actualnumber;
    }

    public String getActualnumber()
    {
        return actualnumber;
    }

    public void setActualweight(String actualweight)
    {
        this.actualweight = actualweight;
    }

    public String getActualweight()
    {
        return actualweight;
    }


    public void setSumvalues(String sumvalues)
    {
        this.sumvalues = sumvalues;
    }

    public String getSumvalues()
    {
        return sumvalues;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getNumber()
    {
        return number;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPrice()
    {
        return price;
    }

    public void setBuyers(String buyers)
    {
        this.buyers = buyers;
    }

    public String getBuyers()
    {
        return buyers;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setSelltype(String selltype)
    {
        this.selltype = selltype;
    }

    public String getSelltype()
    {
        return selltype;
    }


    public String getid()
    {
        return id;
    }

    public void setid(String id)
    {
        this.id = id;
    }


    public boolean equals(Object o)
    {
        return false;
    }

    public int hashCode()
    {
        return 0;
    }

    public static final Creator<SellOrder_New> CREATOR = new Creator()
    {
        @Override
        public SellOrder_New createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            SellOrder_New p = new SellOrder_New();
            p.setid(source.readString());
            p.setUid(source.readString());
            p.setInfoId(source.readString());
            p.setUuid(source.readString());
            p.setBatchTime(source.readString());
            p.setSelltype(source.readString());
            p.setStatus(source.readString());
            p.setBuyers(source.readString());
            p.setAddress(source.readString());
            p.setEmail(source.readString());
            p.setPhone(source.readString());
            p.setPrice(source.readString());
            p.setNumber(source.readString());
            p.setWeight(source.readString());
            p.setSumvalues(source.readString());
            p.setActualprice(source.readString());
            p.setActualweight(source.readString());
            p.setActualnumber(source.readString());
            p.setActualsumvalues(source.readString());
            p.setReg(source.readString());
            p.setSaletime(source.readString());
            p.setYear(source.readString());
            p.setNote(source.readString());
            p.setFeedbacknote(source.readString());
            p.setDeposit(source.readString());
            p.setXxzt(source.readString());
            p.setProducer(source.readString());
            p.setFinalpayment(source.readString());
            p.setFlashStr(source.readString());
            p.sellOrderDetailList = source.readArrayList(sellOrderDetailTab.class.getClassLoader());
            p.DetailSecLists = source.readArrayList(sellOrderDetailTab.class.getClassLoader());


            p.setPactId(source.readString());
            p.setPlateNumber(source.readString());
            p.setMainPepole(source.readString());
            p.setMainPepName(source.readString());
            p.setContractorId(source.readString());
            p.setPickId(source.readString());
            p.setCarryPrice(source.readString());
            p.setPackPrice(source.readString());
            p.setDefectPrice(source.readString());
            p.setDefectNum(source.readString());


            p.setPackPec(source.readString());
            p.setWaitDeposit(source.readString());
            p.setBuyersId(source.readString());
            p.setBuyersName(source.readString());
            p.setContractorName(source.readString());
            p.setPickName(source.readString());
            p.setPurchaName(source.readString());
            p.setPurchaTel(source.readString());
            p.setPurchaMail(source.readString());
            p.setIsNeedAudit(source.readString());
            p.setFreeDeposit(source.readString());
            p.setFreeFinalPay(source.readString());
            p.setOldPrice(source.readString());
            p.setOldCarryPrice(source.readString());
            p.setOldPackPrice(source.readString());
            p.setOldnumber(source.readString());
            p.setOldsaletime(source.readString());
            p.setCreatorid(source.readString());
            p.setGoodsname(source.readString());

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
            return p;


        }

        @Override
        public SellOrder_New[] newArray(int size)
        {
            return new SellOrder_New[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(id);
        p.writeString(uid);
        p.writeString(infoId);
        p.writeString(uuid);
        p.writeString(batchTime);
        p.writeString(selltype);
        p.writeString(status);
        p.writeString(buyers);
        p.writeString(address);
        p.writeString(email);
        p.writeString(phone);
        p.writeString(price);
        p.writeString(number);
        p.writeString(weight);
        p.writeString(sumvalues);
        p.writeString(actualprice);
        p.writeString(actualweight);
        p.writeString(actualnumber);
        p.writeString(actualsumvalues);
        p.writeString(reg);
        p.writeString(saletime);
        p.writeString(year);
        p.writeString(note);
        p.writeString(feedbacknote);
        p.writeString(deposit);
        p.writeString(xxzt);
        p.writeString(producer);
        p.writeString(finalpayment);
        p.writeString(flashStr);
        p.writeList(sellOrderDetailList);
        p.writeList(DetailSecLists);//子表

        p.writeString(pactId);
        p.writeString(plateNumber);
        p.writeString(mainPepole);
        p.writeString(mainPepName);
        p.writeString(contractorId);
        p.writeString(pickId);
        p.writeString(carryPrice);
        p.writeString(packPrice);
        p.writeString(defectPrice);
        p.writeString(defectNum);
        p.writeString(packPec);
        p.writeString(waitDeposit);
        p.writeString(buyersId);
        p.writeString(buyersName);
        p.writeString(contractorName);
        p.writeString(pickName);
        p.writeString(purchaName);
        p.writeString(purchaTel);
        p.writeString(purchaMail);
        p.writeString(isNeedAudit);
        p.writeString(freeDeposit);
        p.writeString(freeFinalPay);
        p.writeString(oldPrice);
        p.writeString(oldCarryPrice);
        p.writeString(oldPackPrice);
        p.writeString(oldnumber);
        p.writeString(oldsaletime);
        p.writeString(creatorid);
        p.writeString(goodsname);

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
