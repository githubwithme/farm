package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by user on 2016/4/12.
 */
@Table(name = "HandleBean")
public class HandleBean implements Parcelable
{
    @Id
    @NoAutoIncrement
    public String resultId;
    public String eventId;
    public String solveId;
    public String solveName;
    public String uid;
    public String registime;
    public String result;
    public String state ;
    public String thumbImageUrl;//缩略图
    public String imageUrl;     //原图
    public List<FJxx> fjxx;
    public String isflashStr;
    public String resultflashStr;

    public String getResultflashStr() {
        return resultflashStr;
    }

    public void setResultflashStr(String resultflashStr) {
        this.resultflashStr = resultflashStr;
    }

    public String getIsflashStr() {
        return isflashStr;
    }

    public void setIsflashStr(String isflashStr) {
        this.isflashStr = isflashStr;
    }

    public List<FJxx> getFjxx() {
        return fjxx;
    }

    public void setFjxx(List<FJxx> fjxx) {
        this.fjxx = fjxx;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getRegistime() {
        return registime;
    }

    public void setRegistime(String registime) {
        this.registime = registime;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSolveId() {
        return solveId;
    }

    public void setSolveId(String solveId) {
        this.solveId = solveId;
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

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public static final Parcelable.Creator<HandleBean> CREATOR = new Creator()
    {
        @Override
        public HandleBean createFromParcel(Parcel source)
        {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            HandleBean p = new HandleBean();
            p.setResultId(source.readString());
            p.setEventId(source.readString());
            p.setSolveId(source.readString());
            p.setSolveName(source.readString());
            p.setUid(source.readString());
            p.setRegistime(source.readString());
            p.setResult(source.readString());
            p.setState(source.readString());
            p.setThumbImageUrl(source.readString());
            p.setImageUrl(source.readString());
            p.fjxx = source.readArrayList(plantgrowthtab.class.getClassLoader());
            p.setIsflashStr(source.readString());
            p.setResultflashStr(source.readString());
            return p;
        }

        @Override
        public HandleBean[] newArray(int size)
        {
            return new HandleBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel p, int arg1)
    {
        p.writeString(resultId);
        p.writeString(eventId);
        p.writeString(solveId);
        p.writeString(solveName);
        p.writeString(uid);
        p.writeString(registime);
        p.writeString(result);
        p.writeString(state );
        p.writeString(thumbImageUrl);

        p.writeString(imageUrl);
        p.writeList(fjxx);
        p.writeString(isflashStr);
        p.writeString(resultflashStr);

    /*    p.writeList(imgUrl);
        p.writeList(thumbImageUrl);*/
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

}
