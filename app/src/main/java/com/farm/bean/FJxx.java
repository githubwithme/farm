package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by user on 2016/4/13.
 */
@Table(name = "FJxx")
public class FJxx implements Parcelable
{
    public String FJID;   //附件id
    public String GLID;   //关联id 事件ID，事件处理结果ID
    public String FJMC;   //附件名称
    public String LSTLJ;  //略缩图路径
    public String FJLJ;   //附件路径
    public String SCSJ;   //上传时间
    public String SCR;    //上传人id
    public String SCRXM;  //上传人姓名
    public String FJLX;    // 附件类型 :1为照片,2为视频,3为录音,4为其他
    public String BZ;      //附件描述
    private String FJBDLJ;

    public String getBZ() {
        return BZ;
    }

    public String getFJBDLJ() {
        return FJBDLJ;
    }

    public void setFJBDLJ(String FJBDLJ) {
        this.FJBDLJ = FJBDLJ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getFJID() {
        return FJID;
    }

    public void setFJID(String FJID) {
        this.FJID = FJID;
    }

    public String getFJLJ() {
        return FJLJ;
    }

    public void setFJLJ(String FJLJ) {
        this.FJLJ = FJLJ;
    }

    public String getFJLX() {
        return FJLX;
    }

    public void setFJLX(String FJLX) {
        this.FJLX = FJLX;
    }

    public String getFJMC() {
        return FJMC;
    }

    public void setFJMC(String FJMC) {
        this.FJMC = FJMC;
    }

    public String getGLID() {
        return GLID;
    }

    public void setGLID(String GLID) {
        this.GLID = GLID;
    }

    public String getLSTLJ() {
        return LSTLJ;
    }

    public void setLSTLJ(String LSTLJ) {
        this.LSTLJ = LSTLJ;
    }

    public String getSCR() {
        return SCR;
    }

    public void setSCR(String SCR) {
        this.SCR = SCR;
    }

    public String getSCRXM() {
        return SCRXM;
    }

    public void setSCRXM(String SCRXM) {
        this.SCRXM = SCRXM;
    }

    public String getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(String SCSJ) {
        this.SCSJ = SCSJ;
    }

    public static final Creator<FJxx> CREATOR = new Creator()
    {
        @Override
        public FJxx createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            FJxx p = new FJxx();
            p.setFJID(source.readString());
            p.setGLID(source.readString());
            p.setFJMC(source.readString());
            p.setLSTLJ(source.readString());
            p.setFJLJ(source.readString());
            p.setSCSJ(source.readString());
            p.setSCR(source.readString());
            p.setSCRXM(source.readString());
            p.setFJLX(source.readString());
            p.setBZ(source.readString());
            p.setFJBDLJ(source.readString());
            return p;
        }

        @Override
        public FJxx[] newArray(int size)
        {
            return new FJxx[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(FJID);
        p.writeString(GLID);
        p.writeString(FJMC);
        p.writeString(LSTLJ);
        p.writeString(FJLJ);
        p.writeString(SCSJ);
        p.writeString(SCR);
        p.writeString(SCRXM);
        p.writeString(FJLX);
        p.writeString(BZ);
        p.writeString(FJBDLJ);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
