package com.farm.com.custominterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by ${hmj} on 2015/12/16.
 */
public interface FragmentCallBack_AddPlantObservation
{
    public void callbackFun1(Bundle arg);

    public void callbackFun2(Bundle arg);

    public void callbackFun_setText(Bundle arg);

    public void stepTwo_setHeadText(Bundle arg);

    public Fragment getFragment(Bundle arg);

    public String getGcdId();


}