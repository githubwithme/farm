package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by user on 2016/4/11.
 */
@Table(name = "ReportedBean")
public class ReportedBean  implements Parcelable
{
    @Id
    @NoAutoIncrement
    public String eventId;
    public String reportorId;
    public String reportor;
    public String uid;
    public String reporTime ;
    public String eventType;
    public String eventContent;
    public String state ;
    public String X="0";
    public String Y="0";

    public String thumbImageUrl;//缩略图
    public String solveId;
    public String solveName;
    public String result;
    public String IsUpload;
    public String imageUrl;     //原图
    public List<FJxx> fjxx;


    public List<FJxx> getFjxx() {
        return fjxx;
    }

    public void setFjxx(List<FJxx> fjxx) {
        this.fjxx = fjxx;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }



    public String getIsUpload() {
        return IsUpload;
    }

    public void setIsUpload(String isUpload) {
        IsUpload = isUpload;
    }

    public String getReportorId() {
        return reportorId;
    }

    public void setReportorId(String reportorId) {
        this.reportorId = reportorId;
    }

    public String getReporTime() {
        return reporTime;
    }

    public void setReporTime(String reporTime) {
        this.reporTime = reporTime;
    }

    public String getReportor() {
        return reportor;
    }

    public void setReportor(String reportor) {
        this.reportor = reportor;
    }

    public String getSolveId() {
        return solveId;
    }

    public void setSolveId(String solveId) {
        this.solveId = solveId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSolveName() {
        return solveName;
    }

    public void setSolveName(String solveName) {
        this.solveName = solveName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Parcelable.Creator<ReportedBean> CREATOR = new Creator()
    {
        @Override
        public ReportedBean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ReportedBean p = new ReportedBean();
            p.setEventId(source.readString());
            p.setReportorId(source.readString());
            p.setReportor(source.readString());
            p.setUid(source.readString());
            p.setReporTime(source.readString());
            p.setEventType(source.readString());
            p.setEventContent(source.readString());
            p.setState(source.readString());
            p.setX(source.readString());
            p.setY(source.readString());
            p.setThumbImageUrl(source.readString());
            p.setSolveId(source.readString());
            p.setSolveName(source.readString());
            p.setResult(source.readString());
            p.setIsUpload(source.readString());
            p.setImageUrl(source.readString());

            p.fjxx = source.readArrayList(plantgrowthtab.class.getClassLoader());
      /*      p.imgUrl = source.readArrayList(List.class.getClassLoader());
            p.thumbImageUrl = source.readArrayList(List.class.getClassLoader());*/
            return p;
        }

        @Override
        public ReportedBean[] newArray(int size)
        {
            return new ReportedBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(eventId);
        p.writeString(reportorId);
        p.writeString(reportor);
        p.writeString(uid);
        p.writeString(reporTime );
        p.writeString(eventType);
        p.writeString(eventContent);
        p.writeString(state );
        p.writeString(X);
        p.writeString(Y);
        p.writeString(thumbImageUrl);
        p.writeString(solveId);
        p.writeString(solveName);
        p.writeString(result);
        p.writeString(IsUpload);
        p.writeString(imageUrl);
        p.writeList(fjxx);
    /*    p.writeList(imgUrl);
        p.writeList(thumbImageUrl);*/
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
