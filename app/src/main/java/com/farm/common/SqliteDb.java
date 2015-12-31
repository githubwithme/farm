package com.farm.common;

import android.content.Context;

import com.farm.bean.HaveReadRecord;
import com.farm.bean.commembertab;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class SqliteDb
{
    public static boolean save(Context context, Object obj)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.replace(obj);
        } catch (DbException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static <T> boolean saveAll(Context context, List<T> list)
    {
        DbUtils db = DbUtils.create(context);
        db.configAllowTransaction(true);
        try
        {
            for (int i = 0; i < list.size(); i++)
            {
                db.replace(list.get(i));
            }
        } catch (DbException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            db.configAllowTransaction(false);
        }
        return true;
    }

    public static <T> boolean deleteRecordtemp(Context context, Class<T> c, String BELONG, String firsttype, String secondType)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(c, WhereBuilder.b("BELONG", "=", BELONG).and("firsttype", "=", firsttype).and("secondtype", "=", secondType));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }

    public static <T> boolean deleteSelectCmdArea(Context context, Class<T> c, String firsttype, String secondType)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(c, WhereBuilder.b("firsttype", "=", firsttype).and("secondtype", "=", secondType));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }

    public static <T> boolean deleteGoods(Context context, Class<T> c, String Id)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(c, WhereBuilder.b("Id", "=", Id));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }

    public static <T> boolean deleteAllSelectCmdArea(Context context, Class<T> c)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.deleteAll(c);
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }

    public static <T> boolean deleteAllRecordtemp(Context context, Class<T> c, String BELONG)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(c, WhereBuilder.b("BELONG", "=", BELONG));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }

    public static <T> List<T> getSelectRecordTemp(Context context, Class<T> c, String BELONG, String firsttype)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c).where("BELONG", "=", BELONG).and("firsttype", "=", firsttype));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null == list || list.isEmpty())
        {
            list = new ArrayList<T>();
        }
        return list;
    }

    public static <T> List<T> getSelectRecordByFirstTypetemp(Context context, Class<T> c, String BELONG)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c).where("BELONG", "=", BELONG));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null == list || list.isEmpty())
        {
            list = new ArrayList<T>();
        }
        return list;
    }

    public static <T> List<T> getGoods(Context context, Class<T> c)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null == list || list.isEmpty())
        {
            list = new ArrayList<T>();
        }
        return list;
    }

    public static <T> List<T> getSelectCmdArea(Context context, Class<T> c)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null == list || list.isEmpty())
        {
            list = new ArrayList<T>();
        }
        return list;
    }

    public static <T> List<T> getSelectItem(Context context, Class<T> c, String firsttype, String secondType)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c).where("firsttype", "=", firsttype).and("secondType", "=", secondType));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null == list || list.isEmpty())
        {
            list = new ArrayList<T>();
        }
        return list;
    }

    public static <T> List<T> getUserList(Context context, Class<T> c)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null == list || list.isEmpty())
        {
            list = new ArrayList<T>();
        }
        return list;
    }

    public static <T> Object getCurrentUser(Context context, Class<T> c, String userName)
    {
        DbUtils db = DbUtils.create(context);
        Object obj = null;
        try
        {
            obj = db.findFirst(Selector.from(c).where("userName", "=", userName));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> Object getHaveReadData(Context context, Class<T> c, String id)
    {
        DbUtils db = DbUtils.create(context);
        Object obj = null;
        try
        {
            obj = db.findFirst(Selector.from(c).where("id", "=", id));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

//    public static <T> Object getAllGoodsNumber(Context context, Class<T> c, String id)
//    {
//        DbUtils db = DbUtils.create(context);
//        Object obj = null;
//        try
//        {
//            obj = db.count(Selector.from(c).select(""));
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//        return obj;
//    }

    public static <T> void updateHaveReadData(Context context, Object c, String id, String columnname)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.update(c, id, columnname);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    public static <T> Object getAutoLoginUser(Context context, Class<T> c)
    {
        DbUtils db = DbUtils.create(context);
        Object obj = null;
        try
        {
            obj = db.findFirst(Selector.from(c).where("autoLogin", "=", "1"));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public static void existSystem(Context context, commembertab commembertab)// 这个方式可以
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.update(commembertab, "userPwd", "isLogin");
        } catch (DbException e)
        {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    public static void saveHaveReadRecord(Context context, String id, String num)
    {
        HaveReadRecord HaveReadRecord = new HaveReadRecord();
        HaveReadRecord.setId(id);
        HaveReadRecord.setNum(num);
        save(context, HaveReadRecord);
    }

    public static HaveReadRecord getHaveReadRecord(Context context, String id)
    {
        HaveReadRecord HaveReadRecord = (HaveReadRecord) SqliteDb.getHaveReadData(context, HaveReadRecord.class, id);
        return HaveReadRecord;
    }

    public static void updateHaveReadRecord(Context context, String id, String num)
    {
        HaveReadRecord haveReadRecord = new HaveReadRecord();
        haveReadRecord.setId(id);
        haveReadRecord.setNum(num);
        DbUtils db = DbUtils.create(context);
        try
        {
            db.update(haveReadRecord, "id", "num");
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

}
