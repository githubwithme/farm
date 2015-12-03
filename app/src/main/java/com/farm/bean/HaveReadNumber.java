package com.farm.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "HaveReadNumber")
public class HaveReadNumber
{
    @Id
    @NoAutoIncrement
    String id;
    String pq_plantnum;
    String pq_jobnum;
    String pq_cmdnum;
    String plantnum;
    String jobnum;
    String asknum;
    String cmdnum;

    public void setCmdnum(String cmdnum)
    {
        this.cmdnum = cmdnum;
    }

    public String getCmdnum()
    {
        return cmdnum;
    }

    public void setPq_cmdnum(String pq_cmdnum)
    {
        this.pq_cmdnum = pq_cmdnum;
    }

    public String getPq_cmdnum()
    {
        return pq_cmdnum;
    }

    public void setPq_jobnum(String pq_jobnum)
    {
        this.pq_jobnum = pq_jobnum;
    }

    public String getPq_jobnum()
    {
        return pq_jobnum;
    }

    public void setPlantnum(String plantnum)
    {
        this.plantnum = plantnum;
    }

    public String getPlantnum()
    {
        return plantnum;
    }

    public void setPq_plantnum(String pq_plantnum)
    {
        this.pq_plantnum = pq_plantnum;
    }

    public String getPq_plantnum()
    {
        return pq_plantnum;
    }

    public void setJobnum(String jobnum)
    {
        this.jobnum = jobnum;
    }

    public String getJobnum()
    {
        return jobnum;
    }

    public void setAsknum(String asknum)
    {
        this.asknum = asknum;
    }

    public String getAsknum()
    {
        return asknum;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

}
