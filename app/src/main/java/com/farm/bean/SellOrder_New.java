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

    public String pactId;
    public String plateNumber; // 车牌号
    public String mainPepole;  // 负责人id
    public String mainPepName;  // 负责人
    public String contractorId; // 包工头
    public String pickId;      //  挑工头
    public String carryPrice;  // 搬运单价
    public String packPrice;   // 包装单价
    public String defectPrice;   //次品单价
    public String defectNum;   // 次品数量
    public String packPec;   // 包装规格
    public String waitDeposit;   // 要付订金

    public String buyersId;   // 采购商id
    public String buyersName;   // 采购商名
    public String contractorName;   //  包工头名
    public String pickName;   //  搬运工名
    public String purchaName;   // 片管获取采购商名字
    public String purchaTel;   //采购商电话
    public String purchaMail;   // 采购商mail
    public String isNeedAudit;   // 单价，订金的修改
    public String freeDeposit;   // 免付订金
    public String freeFinalPay;   // 免付尾款

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
    public String total;//总件数
    public String qualityTotalWeight;//正品总净重
    public String defectTotalWeight;//次品重净重
    public String TotalWeight;//总净重
    public String packFee;//总包装费
    public String carryFee;//总搬运费
    public String totalFee;//总合计金额
    public String personNote;//搬运说明
    public String actualMoney;//实际金额
    public String prepareStatus;//准备状态
    public String product;//产品名称
    public String carNumber;//车辆数
    public String mainPeople;//负责人名称
    public String mainPeoplePhone;//负责人电话
    public String parkname;//采收园区名称
    public String isReady;//是否准备就绪
    public String settlementNumber;//结算订单数
    public String notPaySettlementNumber;//未结算金额订单数
    public String paidSettlementNumber;//已结算金额订单数
    public String paid;//已结算金额
    public String unpaid;//未结算金额
    public String buyersPhone;//采购商电话

    public String getBuyersPhone()
    {
        return buyersPhone;
    }

    public void setBuyersPhone(String buyersPhone)
    {
        this.buyersPhone = buyersPhone;
    }

    public SellOrder_New()
    {
        super();
    }

    protected SellOrder_New(Parcel in)
    {
        id = in.readString();
        uid = in.readString();
        infoId = in.readString();
        uuid = in.readString();
        batchTime = in.readString();
        selltype = in.readString();
        status = in.readString();
        buyers = in.readString();
        address = in.readString();
        email = in.readString();
        phone = in.readString();
        price = in.readString();
        number = in.readString();
        weight = in.readString();
        sumvalues = in.readString();
        actualprice = in.readString();
        actualweight = in.readString();
        actualnumber = in.readString();
        actualsumvalues = in.readString();
        reg = in.readString();
        saletime = in.readString();
        year = in.readString();
        note = in.readString();
        feedbacknote = in.readString();
        deposit = in.readString();
        xxzt = in.readString();
        producer = in.readString();
        finalpayment = in.readString();
        flashStr = in.readString();
        sellOrderDetailList = in.createTypedArrayList(SellOrderDetail_New.CREATOR);
        DetailSecLists = in.createTypedArrayList(SellOrderDetail_New.CREATOR);
        pactId = in.readString();
        plateNumber = in.readString();
        mainPepole = in.readString();
        mainPepName = in.readString();
        contractorId = in.readString();
        pickId = in.readString();
        carryPrice = in.readString();
        packPrice = in.readString();
        defectPrice = in.readString();
        defectNum = in.readString();
        packPec = in.readString();
        waitDeposit = in.readString();
        buyersId = in.readString();
        buyersName = in.readString();
        contractorName = in.readString();
        pickName = in.readString();
        purchaName = in.readString();
        purchaTel = in.readString();
        purchaMail = in.readString();
        isNeedAudit = in.readString();
        freeDeposit = in.readString();
        freeFinalPay = in.readString();
        oldPrice = in.readString();
        oldCarryPrice = in.readString();
        oldPackPrice = in.readString();
        oldnumber = in.readString();
        oldsaletime = in.readString();
        creatorid = in.readString();
        goodsname = in.readString();
        qualityWaterWeight = in.readString();
        qualityNetWeight = in.readString();
        qualityBalance = in.readString();
        defectWaterWeight = in.readString();
        defectNetWeight = in.readString();
        defectBalance = in.readString();
        total = in.readString();
        qualityTotalWeight = in.readString();
        defectTotalWeight = in.readString();
        TotalWeight = in.readString();
        packFee = in.readString();
        carryFee = in.readString();
        totalFee = in.readString();
        personNote = in.readString();
        actualMoney = in.readString();
        prepareStatus = in.readString();
        product = in.readString();
        carNumber = in.readString();
        mainPeople = in.readString();
        mainPeoplePhone = in.readString();
        parkname = in.readString();
        isReady = in.readString();
        settlementNumber = in.readString();
        notPaySettlementNumber = in.readString();
        paidSettlementNumber = in.readString();
        paid = in.readString();
        unpaid = in.readString();
        buyersPhone = in.readString();
    }

    public static final Creator<SellOrder_New> CREATOR = new Creator<SellOrder_New>()
    {
        @Override
        public SellOrder_New createFromParcel(Parcel in)
        {
            return new SellOrder_New(in);
        }

        @Override
        public SellOrder_New[] newArray(int size)
        {
            return new SellOrder_New[size];
        }
    };

    public String getSettlementNumber()
    {
        return settlementNumber;
    }

    public void setSettlementNumber(String settlementNumber)
    {
        this.settlementNumber = settlementNumber;
    }

    public String getNotPaySettlementNumber()
    {
        return notPaySettlementNumber;
    }

    public void setNotPaySettlementNumber(String notPaySettlementNumber)
    {
        this.notPaySettlementNumber = notPaySettlementNumber;
    }

    public String getPaidSettlementNumber()
    {
        return paidSettlementNumber;
    }

    public void setPaidSettlementNumber(String paidSettlementNumber)
    {
        this.paidSettlementNumber = paidSettlementNumber;
    }

    public String getPaid()
    {
        return paid;
    }

    public void setPaid(String paid)
    {
        this.paid = paid;
    }

    public String getUnpaid()
    {
        return unpaid;
    }

    public void setUnpaid(String unpaid)
    {
        this.unpaid = unpaid;
    }

    public String getIsReady()
    {
        return isReady;
    }

    public void setIsReady(String isReady)
    {
        this.isReady = isReady;
    }

    public String getParkname()
    {
        return parkname;
    }

    public void setParkname(String parkname)
    {
        this.parkname = parkname;
    }

    public String getMainPeople()
    {
        return mainPeople;
    }

    public void setMainPeople(String mainPeople)
    {
        this.mainPeople = mainPeople;
    }

    public String getMainPeoplePhone()
    {
        return mainPeoplePhone;
    }

    public void setMainPeoplePhone(String mainPeoplePhone)
    {
        this.mainPeoplePhone = mainPeoplePhone;
    }

    public String getCarNumber()
    {
        return carNumber;
    }

    public void setCarNumber(String carNumber)
    {
        this.carNumber = carNumber;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getPrepareStatus()
    {
        return prepareStatus;
    }

    public void setPrepareStatus(String prepareStatus)
    {
        this.prepareStatus = prepareStatus;
    }


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

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(infoId);
        dest.writeString(uuid);
        dest.writeString(batchTime);
        dest.writeString(selltype);
        dest.writeString(status);
        dest.writeString(buyers);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(price);
        dest.writeString(number);
        dest.writeString(weight);
        dest.writeString(sumvalues);
        dest.writeString(actualprice);
        dest.writeString(actualweight);
        dest.writeString(actualnumber);
        dest.writeString(actualsumvalues);
        dest.writeString(reg);
        dest.writeString(saletime);
        dest.writeString(year);
        dest.writeString(note);
        dest.writeString(feedbacknote);
        dest.writeString(deposit);
        dest.writeString(xxzt);
        dest.writeString(producer);
        dest.writeString(finalpayment);
        dest.writeString(flashStr);
        dest.writeTypedList(sellOrderDetailList);
        dest.writeTypedList(DetailSecLists);
        dest.writeString(pactId);
        dest.writeString(plateNumber);
        dest.writeString(mainPepole);
        dest.writeString(mainPepName);
        dest.writeString(contractorId);
        dest.writeString(pickId);
        dest.writeString(carryPrice);
        dest.writeString(packPrice);
        dest.writeString(defectPrice);
        dest.writeString(defectNum);
        dest.writeString(packPec);
        dest.writeString(waitDeposit);
        dest.writeString(buyersId);
        dest.writeString(buyersName);
        dest.writeString(contractorName);
        dest.writeString(pickName);
        dest.writeString(purchaName);
        dest.writeString(purchaTel);
        dest.writeString(purchaMail);
        dest.writeString(isNeedAudit);
        dest.writeString(freeDeposit);
        dest.writeString(freeFinalPay);
        dest.writeString(oldPrice);
        dest.writeString(oldCarryPrice);
        dest.writeString(oldPackPrice);
        dest.writeString(oldnumber);
        dest.writeString(oldsaletime);
        dest.writeString(creatorid);
        dest.writeString(goodsname);
        dest.writeString(qualityWaterWeight);
        dest.writeString(qualityNetWeight);
        dest.writeString(qualityBalance);
        dest.writeString(defectWaterWeight);
        dest.writeString(defectNetWeight);
        dest.writeString(defectBalance);
        dest.writeString(total);
        dest.writeString(qualityTotalWeight);
        dest.writeString(defectTotalWeight);
        dest.writeString(TotalWeight);
        dest.writeString(packFee);
        dest.writeString(carryFee);
        dest.writeString(totalFee);
        dest.writeString(personNote);
        dest.writeString(actualMoney);
        dest.writeString(prepareStatus);
        dest.writeString(product);
        dest.writeString(carNumber);
        dest.writeString(mainPeople);
        dest.writeString(mainPeoplePhone);
        dest.writeString(parkname);
        dest.writeString(isReady);
        dest.writeString(settlementNumber);
        dest.writeString(notPaySettlementNumber);
        dest.writeString(paidSettlementNumber);
        dest.writeString(paid);
        dest.writeString(unpaid);
        dest.writeString(buyersPhone);
    }
}
