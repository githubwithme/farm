package com.farm.com.custominterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by ${hmj} on 2015/12/16.
 */
public interface FragmentCallBack_AddPlantObservation
{
    void callbackFun1(Bundle arg);

    void callbackFun2(Bundle arg);

    void callbackFun_setText(Bundle arg);

    void stepTwo_setHeadText(Bundle arg);

    Fragment getFragment(Bundle arg);

    String getGcdId();


}