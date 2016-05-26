package com.farm.common;

/**
 * Created by ${hmj} on 2016/5/25.
 */

import com.farm.bean.ChartEntity;

import java.util.Comparator;

public class SortComparator implements Comparator
{
    @Override
    public int compare(Object lhs, Object rhs)
    {
        ChartEntity a = (ChartEntity) lhs;
        ChartEntity b = (ChartEntity) rhs;

        return (Integer.valueOf(b.getNumber()) - Integer.valueOf(a.getNumber()));
    }
}