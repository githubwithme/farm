package com.farm.common;

import android.database.sqlite.SQLiteDatabase;

import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * Created by ${hmj} on 2016/3/16.
 */
public class CustomDbUpgradeListener implements DbUtils.DbUpgradeListener
{

    @Override
    public void onUpgrade(DbUtils dbUtils, int i, int i1)
    {
        boolean tableexist = false;
        boolean columnexist = false;
        SQLiteDatabase sqLiteDatabase = dbUtils.getDatabase();
        try
        {
            tableexist = dbUtils.tableIsExist(goodslisttab.class);
            if (tableexist)
            {
                columnexist = SqliteDb.checkColumnExist1(sqLiteDatabase, "goodslisttab", "DW");
                if (!columnexist)
                {
                    String sql = "alter table  goodslisttab  add column DW NVARCHAR(10)";
                    sqLiteDatabase.execSQL(sql);
                }
            }
            tableexist = dbUtils.tableIsExist(goodslisttab.class);
            if (tableexist)
            {
                columnexist = SqliteDb.checkColumnExist1(sqLiteDatabase, "goodslisttab", "GoodsStatistical");
                if (!columnexist)
                {
                    String sql = "alter table  goodslisttab  add column GoodsStatistical NVARCHAR(20)";
                    sqLiteDatabase.execSQL(sql);
                }
            }
            tableexist = dbUtils.tableIsExist(goodslisttab_flsl.class);
            if (tableexist)
            {
                columnexist = SqliteDb.checkColumnExist1(sqLiteDatabase, "goodslisttab_flsl", "DW");
                if (!columnexist)
                {
                    String sql = "alter table  goodslisttab_flsl  add column DW NVARCHAR(10)";
                    sqLiteDatabase.execSQL(sql);
                }
            }

        } catch (DbException e)
        {
            e.printStackTrace();
            return;
        }

    }
}
