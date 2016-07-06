package com.farm.bean;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/6.
 */
public class PlantObserveList
{
    String observetime;
    List<plantgrowthtab> plantgrowthtabList;


    public String getObservetime()
    {
        return observetime;
    }

    public void setObservetime(String observetime)
    {
        this.observetime = observetime;
    }

    public List<plantgrowthtab> getPlantgrowthtabList()
    {
        return plantgrowthtabList;
    }

    public void setPlantgrowthtabList(List<plantgrowthtab> plantgrowthtabList)
    {
        this.plantgrowthtabList = plantgrowthtabList;
    }
}
