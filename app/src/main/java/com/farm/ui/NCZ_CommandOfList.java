package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by user on 2016/5/19.
 */
@EFragment
public class NCZ_CommandOfList extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.commandlist, container, false);
        return rootView;
    }
}
