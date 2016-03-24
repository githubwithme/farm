package com.farm.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.farm.bean.CoordinatesBean;
import com.farm.bean.DepartmentBean;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.PolygonBean;
import com.farm.bean.areatab;
import com.farm.bean.breakofftab;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.bean.parktab;
import com.farm.bean.sellOrderDetailTab;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class SqliteDb
{
    public static List<parktab> getBreakOffListByParkID(Context context)
    {
        List<parktab> list_park = FileHelper.getAssetsData(context, "parktab", parktab.class);
        DbUtils db = DbUtils.create(context);
        for (int i = 0; i < list_park.size(); i++)
        {
            List<areatab> list_area = list_park.get(i).getAreatabList();
            for (int j = 0; j < list_area.size(); j++)
            {
                List<contractTab> list_contractTab = list_park.get(i).getAreatabList().get(j).getContractTabList();
                if (list_contractTab == null)
                {
                    List<contractTab> list_contractTab_temp = new ArrayList<>();
                    list_park.get(i).getAreatabList().get(j).setContractTabList(list_contractTab_temp);
                } else
                {
                    for (int k = 0; k < list_contractTab.size(); k++)
                    {

                        List<breakofftab> list = null;
                        try
                        {
                            list = db.findAll(Selector.from(breakofftab.class).where("contractId", "=", list_contractTab.get(k).getid()));
                            if (list != null)
                            {
                                list_park.get(i).getAreatabList().get(j).getContractTabList().get(k).setbreakofftabList(list);
                            }

                        } catch (DbException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }

        return list_park;
    }

    public static <T> List<T> getBoundary_area(Context context, Class<T> c, String parkid, String uid)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c).where("uid", "=", uid).and("areaid", "=", parkid).and("type", "=", "boundary_area"));
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
    //    }


    public static void updateCoordinatesOrder(Context context, CoordinatesBean coordinatesBean)// 这个方式可以
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.update(coordinatesBean, "areaId", "areaName");
        } catch (DbException e)
        {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
    public static <T> boolean deleteplanPolygon(Context context,String uuid)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(PolygonBean.class, WhereBuilder.b("uuid", "=", uuid));
            db.delete(PolygonBean.class, WhereBuilder.b("otherhalf", "=", uuid));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }
    public static <T> boolean deletePolygon(Context context,String uuid)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(PolygonBean.class, WhereBuilder.b("uuid", "=", uuid));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
        }
        return true;
    }
    public static <T> List<T> getTemp1(Context context)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(PolygonBean.class));
            db.deleteAll(list);
            list = db.findAll(Selector.from(CoordinatesBean.class));
//            list = db.findAll(Selector.from(PolygonBean.class));
//            db.updateAll(list, "note", "");
            db.deleteAll(list);
//            list = db.findAll(Selector.from(CoordinatesBean.class));
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

    //    public static String getNeedPlanBoundary(Context context, String uid, String farm_boundary)
//    {
//        StringBuffer build = new StringBuffer();
//        build.append("{\"ResultCode\":1,\"Exception\":\"\",\"AffectedRows\":\"3\",\"Rows\":[");
//        DbUtils db = DbUtils.create(context);
//        try
//        {
//            List<parktab> list_parktab = db.findAll(Selector.from(parktab.class).where("uid", "=", uid));
//            if (list_parktab.size() != 0)
//            {
//                for (int i = 0; i < list_parktab.size(); i++)//每个园区
//                {
//                    List<areatab> list_areatab_temp = db.findAll(Selector.from(areatab.class).where("parkid", "=", list_parktab.get(i).getid()));
//                    if (list_areatab_temp.size() != 0)
//                    {
//                        for (int j = 0; j < list_areatab_temp.size(); j++)//检查每个片区是否已经开始规划
//                        {
//                            List<CoordinatesBean> list_CoordinatesBean_area_temp = db.findAll(Selector.from(CoordinatesBean.class).where("areaid", "=", list_areatab_temp.get(j).getid()).and("contractid", "=", "").and("type", "=", "farm_boundary"));
//                            if (list_CoordinatesBean_area_temp!=null && list_CoordinatesBean_area_temp.size()>0)
//                            {
//                                build.append("[");
//                                String order_last = "0";
//                                List<DbModel> dbModels = db.findDbModelAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", "farm_boundary_free").and("areaid", "=", "").groupBy("orders").select("orders", "count(orders)").orderBy("orders", true));
//                                if (dbModels.size() != 0)
//                                {
//                                    order_last = dbModels.get(0).getString("orders");
//                                }
//                                List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", "").and("orders", "=", order_last));
//                                if (list_CoordinatesBean_park.size() != 0)
//                                {
//                                    for (int h = 0; h < list_CoordinatesBean_park.size(); h++)
//                                    {
//                                        build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h).getLng() + "\"" + "}" + ",");
//                                    }
//                                    build.replace(build.length() - 1, build.length(), "");
//                                    build.append("],");
//                                } else
//                                {
//                                    build.append("],");
//                                }
//                                break;
//                            }
//                            if (j == list_areatab_temp.size()-1)//该园区还未开始规划
//                            {
//                                build.append("[");
//                                List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", ""));
//                                if (list_CoordinatesBean_park.size() != 0)
//                                {
//                                    for (int h1 = 0; h1 < list_CoordinatesBean_park.size(); h1++)
//                                    {
//                                        build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getLng() + "\"" + "}" + ",");
//                                    }
//                                    build.replace(build.length() - 1, build.length(), "");
//                                    build.append("],");
//                                } else
//                                {
//                                    build.append("],");
//                                }
//                            }
//                        }
//                    }else
//                    {
//                        build.append("[");
//                        List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", ""));
//                        if (list_CoordinatesBean_park.size() != 0)
//                        {
//                            for (int h1 = 0; h1 < list_CoordinatesBean_park.size(); h1++)
//                            {
//                                build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(h1).getLng() + "\"" + "}" + ",");
//                            }
//                            build.replace(build.length() - 1, build.length(), "");
//                            build.append("],");
//                        } else
//                        {
//                            build.append("],");
//                        }
//                    }
//
////                    build.append("[");
////                    List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", ""));
////                    if (list_CoordinatesBean_park.size() != 0)
////                    {
////                        for (int j = 0; j < list_CoordinatesBean_park.size(); j++)
////                        {
////                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLng() + "\"" + "}" + ",");
////                        }
////                        build.replace(build.length() - 1, build.length(), "");
////                        build.append("],");
////                    } else
////                    {
////                        build.append("],");
////                    }
//
//                    //片区
//                    List<areatab> list_areatab = db.findAll(Selector.from(areatab.class).where("parkid", "=", list_parktab.get(i).getid()));
//                    if (list_areatab.size() != 0)
//                    {
//                        for (int j = 0; j < list_areatab.size(); j++)//该园区每个片区
//                        {
//                            List<contractTab> list_contractTab_temp = db.findAll(Selector.from(contractTab.class).where("areaid", "=", list_areatab.get(j).getid()));
//                            if (list_contractTab_temp.size() != 0)
//                            {
//                                for (int m = 0; m < list_contractTab_temp.size(); m++)//该园区该片区每个承包区
//                                {
//                                    build.append("[");
//                                    List<CoordinatesBean> list_CoordinatesBean_contractTab = db.findAll(Selector.from(CoordinatesBean.class).where("contractid", "=", list_contractTab_temp.get(m).getid()).and("type", "=", ""));
//                                    if (list_CoordinatesBean_contractTab.size() != 0)
//                                    {
//                                        for (int k = 0; k < list_CoordinatesBean_contractTab.size(); k++)
//                                        {
//                                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLng() + "\"" + "}" + ",");
//                                        }
//                                        build.replace(build.length() - 1, build.length(), "");
//                                        build.append("],");
//                                    } else
//                                    {
//                                        build.append("],");
//                                    }
//                                }
//                            }else
//                            {
//
//                            }
//
//
//
//
//
//                            build.append("[");
//                            List<CoordinatesBean> list_CoordinatesBean_area = db.findAll(Selector.from(CoordinatesBean.class).where("areaid", "=", list_areatab.get(j).getid()).and("contractid", "=", "").and("type", "=", farm_boundary));
//                            if (list_CoordinatesBean_area.size() != 0)
//                            {
//                                for (int k = 0; k < list_CoordinatesBean_area.size(); k++)
//                                {
//                                    build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLng() + "\"" + "}" + ",");
//                                }
//                                build.replace(build.length() - 1, build.length(), "");
//                                build.append("],");
//
//                            } else
//                            {
//                                build.append("],");
//                            }
////承包区
//                            List<contractTab> list_contractTab = db.findAll(Selector.from(contractTab.class).where("areaid", "=", list_areatab.get(j).getid()));
//                            if (list_contractTab.size() != 0)
//                            {
//                                for (int m = 0; m < list_contractTab.size(); m++)//该园区该片区每个承包区
//                                {
//                                    build.append("[");
//                                    List<CoordinatesBean> list_CoordinatesBean_contractTab = db.findAll(Selector.from(CoordinatesBean.class).where("contractid", "=", list_contractTab.get(m).getid()).and("type", "=", farm_boundary));
//                                    if (list_CoordinatesBean_contractTab.size() != 0)
//                                    {
//                                        for (int k = 0; k < list_CoordinatesBean_contractTab.size(); k++)
//                                        {
//                                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLng() + "\"" + "}" + ",");
//                                        }
//                                        build.replace(build.length() - 1, build.length(), "");
//                                        build.append("],");
//                                    } else
//                                    {
//                                        build.append("],");
//                                    }
//
//
//                                }
//                            }
//
//
//                        }
//                    }
//                }
//            }
//
//            build.replace(build.length() - 1, build.length(), "");
//            build.append("]}");
//            build.toString();
//            build.toString();
//
//
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//        return build.toString();
//    }
    public static PolygonBean getNeedPlanlayer(Context context, String uid, String parkid, String areaid, String contractid)
    {
        DbUtils db = DbUtils.create(context);
        PolygonBean polygonBean = null;
        try
        {
            if (contractid.equals(""))//要规划片区，则获取园区未规划图层或者整个园区图层
            {
                String order_last = "0";
                List<DbModel> dbModels = db.findDbModelAll(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", "").groupBy("orders").select("orders", "count(orders)").orderBy("orders", true));
                if (dbModels.size() != 0)//该园区已经开始规划
                {
                    order_last = dbModels.get(0).getString("orders");
                    polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", "").and("contractid", "=", "").and("orders", "=", order_last).and("xxzt", "=", "0"));
                } else//该园区还没开始规划
                {
                    polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", "").and("contractid", "=", "").and("xxzt", "=", "0"));
                }
            } else//要规划承包区，则获取片区未规划图层或者整个片区
            {
                String order_last = "0";
                List<DbModel> dbModels = db.findDbModelAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", areaid).and("contractid", "=", "").groupBy("orders").select("orders", "count(orders)").orderBy("orders", true));
                if (dbModels.size() != 0)//该片区已经开始规划
                {
                    order_last = dbModels.get(0).getString("orders");
                    polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", areaid).and("contractid", "=", "").and("orders", "=", order_last).and("xxzt", "=", "0"));
                } else//该片区还没开始规划
                {
                    polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", areaid).and("contractid", "=", contractid).and("xxzt", "=", "0"));
                }
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return polygonBean;
    }
    public static List<CoordinatesBean> getBoundaryByID(Context context, String uid, String parkid, String areaid, String contractid)
    {
        DbUtils db = DbUtils.create(context);
        List<CoordinatesBean> list_CoordinatesBean = null;
        try
        {
            if (contractid.equals(""))//获取园区未规划图层或者整个园区图层
            {
                String order_last = "0";
                List<DbModel> dbModels = db.findDbModelAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", "").groupBy("orders").select("orders", "count(orders)").orderBy("orders", true));
                if (dbModels.size() != 0)//该园区已经开始规划
                {
                    order_last = dbModels.get(0).getString("orders");
                    list_CoordinatesBean = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", "").and("orders", "=", order_last));
                } else//该园区还没开始规划
                {
                    list_CoordinatesBean = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", ""));
                }
            } else//获取片区未规划图层或者整个片区
            {
                String order_last = "0";
                List<DbModel> dbModels = db.findDbModelAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", areaid).and("contractid", "=", "").groupBy("orders").select("orders", "count(orders)").orderBy("orders", true));
                if (dbModels.size() != 0)//该片区已经开始规划
                {
                    order_last = dbModels.get(0).getString("orders");
                    list_CoordinatesBean = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary_free").and("areaid", "=", areaid).and("contractid", "=", "").and("orders", "=", order_last));
                } else//该片区还没开始规划
                {
                    list_CoordinatesBean = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", areaid).and("contractid", "=", ""));
                }
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list_CoordinatesBean;
    }

    public static List<CoordinatesBean> getPoints(Context context, String uuid)
    {
        DbUtils db = DbUtils.create(context);
        List<CoordinatesBean> list = null;
        try
        {
            list = db.findAll(Selector.from(CoordinatesBean.class).where("uuid", "=", uuid));
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public static List<PolygonBean> getMoreLayer_point(Context context, String uid)
    {
        DbUtils db = DbUtils.create(context);
        List<PolygonBean> list = null;
        try
        {
            list= db.findAll(Selector.from(PolygonBean.class).where("uid", "=", uid).and("type", "=", "D").and("xxzt", "=", "0"));
            if (list == null)
            {
                list = new ArrayList<>();
            }

        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public static List<PolygonBean> getMoreLayer_line(Context context, String uid)
    {
        DbUtils db = DbUtils.create(context);
        List<PolygonBean> list = null;
        try
        {
            list = db.findAll(Selector.from(PolygonBean.class).where("uid", "=", uid).and("type", "=", "X").and("xxzt", "=", "0"));
            if (list == null)
            {
                list = new ArrayList<>();
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list;
    }
    public static PolygonBean getLayer_park(Context context, String parkid)
    {
        DbUtils db = DbUtils.create(context);
        PolygonBean polygonBean_park = null;
        try
        {
            polygonBean_park = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", "").and("xxzt", "=", "0"));

        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return polygonBean_park;
    }
    public static PolygonBean getLayer_area(Context context, String parkid,String areaid)
    {
        DbUtils db = DbUtils.create(context);
        PolygonBean polygonBean = null;
        try
        {
            polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", areaid).and("contractid", "=", "").and("xxzt", "=", "0"));
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return polygonBean;
    }
    public static PolygonBean getLayer_contract(Context context, String parkid,String areaid,String contractid)
    {
        DbUtils db = DbUtils.create(context);
        PolygonBean polygonBean = null;
        try
        {
            polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", areaid).and("contractid", "=", contractid).and("xxzt", "=", "0"));
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return polygonBean;
    }
    public static PolygonBean getLayerbyuuid(Context context, String uuid)
    {
        DbUtils db = DbUtils.create(context);
        PolygonBean polygonBean = null;
        try
        {
            polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("uuid", "=", uuid).and("xxzt", "=", "0"));
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return polygonBean;
    }
    public static PolygonBean getLayer(Context context, String parkid,String areaid,String contractid)
    {
        DbUtils db = DbUtils.create(context);
        PolygonBean polygonBean = null;
        try
        {
            polygonBean = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", parkid).and("type", "=", "farm_boundary").and("areaid", "=", areaid).and("contractid", "=", contractid).and("xxzt", "=", "0"));
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return polygonBean;
    }
    public static List<parktab> getparktab(Context context, String uid)
    {
        DbUtils db = DbUtils.create(context);
        List<parktab> list = null;
        try
        {
            list = db.findAll(Selector.from(parktab.class).where("uid", "=", uid));
            if (list == null)
            {
                list = new ArrayList<>();
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list;
    }
    public static List<areatab> getareatab(Context context, String parkid)
    {
        DbUtils db = DbUtils.create(context);
        List<areatab> list = null;
        try
        {
            list = db.findAll(Selector.from(areatab.class).where("parkid", "=", parkid));
            if (list == null)
            {
                list = new ArrayList<>();
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list;
    }
    public static List<contractTab> getcontracttab(Context context, String areaid)
    {
        DbUtils db = DbUtils.create(context);
        List<contractTab> list = null;
        try
        {
            list = db.findAll(Selector.from(contractTab.class).where("areaid", "=", areaid));
            if (list == null)
            {
                list = new ArrayList<>();
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return list;
    }
    public static String getPlanMap(Context context, String uid, String farm_boundary)
    {
        StringBuffer build = new StringBuffer();
        build.append("{\"ResultCode\":1,\"Exception\":\"\",\"AffectedRows\":\"3\",\"Rows\":[");
        DbUtils db = DbUtils.create(context);
        try
        {
            List<parktab> list_parktab = db.findAll(Selector.from(parktab.class).where("uid", "=", uid));
            if (list_parktab.size() != 0)
            {
                for (int i = 0; i < list_parktab.size(); i++)//每个园区
                {
                    build.append("[");
                    PolygonBean polygonBean_park = db.findFirst(Selector.from(PolygonBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", "").and("xxzt", "=", "0"));

                    if (polygonBean_park != null)
                    {
                        List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("uuid", "=", polygonBean_park.getUuid()));
                        for (int j = 0; j < list_CoordinatesBean_park.size(); j++)
                        {
                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLng() + "\"" + "}" + ",");
                        }
                        build.replace(build.length() - 1, build.length(), "");
                        build.append("],");
                    } else
                    {
                        build.append("],");
                    }

                    //片区
                    List<areatab> list_areatab = db.findAll(Selector.from(areatab.class).where("parkid", "=", list_parktab.get(i).getid()));
                    if (list_areatab.size() != 0)
                    {
                        for (int j = 0; j < list_areatab.size(); j++)//该园区每个片区
                        {
                            build.append("[");
                            PolygonBean polygonBean_area = db.findFirst(Selector.from(PolygonBean.class).where("areaid", "=", list_areatab.get(j).getid()).and("contractid", "=", "").and("type", "=", farm_boundary).and("xxzt", "=", "0"));

                            if (polygonBean_area != null)
                            {
                                List<CoordinatesBean> list_CoordinatesBean_area = db.findAll(Selector.from(CoordinatesBean.class).where("uuid", "=", polygonBean_area.getUuid()));
                                for (int k = 0; k < list_CoordinatesBean_area.size(); k++)
                                {
                                    build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLng() + "\"" + "}" + ",");
                                }
                                build.replace(build.length() - 1, build.length(), "");
                                build.append("],");

                            } else
                            {
                                build.append("],");
                            }
//承包区
                            List<contractTab> list_contractTab = db.findAll(Selector.from(contractTab.class).where("areaid", "=", list_areatab.get(j).getid()));
                            if (list_contractTab.size() != 0)
                            {
                                for (int m = 0; m < list_contractTab.size(); m++)//该园区该片区每个承包区
                                {
                                    build.append("[");
                                    PolygonBean list_PolygonBean_contractTab = db.findFirst(Selector.from(PolygonBean.class).where("contractid", "=", list_contractTab.get(m).getid()).and("type", "=", farm_boundary).and("xxzt", "=", "0"));

                                    if (list_PolygonBean_contractTab != null)
                                    {
                                        List<CoordinatesBean> list_CoordinatesBean_contractTab = db.findAll(Selector.from(CoordinatesBean.class).where("uuid", "=", list_PolygonBean_contractTab.getUuid()));
                                        for (int k = 0; k < list_CoordinatesBean_contractTab.size(); k++)
                                        {
                                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLng() + "\"" + "}" + ",");
                                        }
                                        build.replace(build.length() - 1, build.length(), "");
                                        build.append("],");
                                    } else
                                    {
                                        build.append("],");
                                    }


                                }
                            }


                        }
                    }
                }
            }

            build.replace(build.length() - 1, build.length(), "");
            build.append("]}");
            build.toString();
            build.toString();


        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return build.toString();
    }

    public static String getBoundary_farm(Context context, String uid, String farm_boundary)
    {
        StringBuffer build = new StringBuffer();
        build.append("{\"ResultCode\":1,\"Exception\":\"\",\"AffectedRows\":\"3\",\"Rows\":[");
        DbUtils db = DbUtils.create(context);
        try
        {
            List<parktab> list_parktab = db.findAll(Selector.from(parktab.class).where("uid", "=", uid));
            if (list_parktab.size() != 0)
            {
                for (int i = 0; i < list_parktab.size(); i++)//每个园区
                {
                    build.append("[");
                    List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", ""));
                    if (list_CoordinatesBean_park.size() != 0)
                    {
                        for (int j = 0; j < list_CoordinatesBean_park.size(); j++)
                        {
                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLng() + "\"" + "}" + ",");
                        }
                        build.replace(build.length() - 1, build.length(), "");
                        build.append("],");
                    } else
                    {
                        build.append("],");
                    }

                    //片区
                    List<areatab> list_areatab = db.findAll(Selector.from(areatab.class).where("parkid", "=", list_parktab.get(i).getid()));
                    if (list_areatab.size() != 0)
                    {
                        for (int j = 0; j < list_areatab.size(); j++)//该园区每个片区
                        {
                            build.append("[");
                            List<CoordinatesBean> list_CoordinatesBean_area = db.findAll(Selector.from(CoordinatesBean.class).where("areaid", "=", list_areatab.get(j).getid()).and("contractid", "=", "").and("type", "=", farm_boundary));
                            if (list_CoordinatesBean_area.size() != 0)
                            {
                                for (int k = 0; k < list_CoordinatesBean_area.size(); k++)
                                {
                                    build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLng() + "\"" + "}" + ",");
                                }
                                build.replace(build.length() - 1, build.length(), "");
                                build.append("],");

                            } else
                            {
                                build.append("],");
                            }
//承包区
                            List<contractTab> list_contractTab = db.findAll(Selector.from(contractTab.class).where("areaid", "=", list_areatab.get(j).getid()));
                            if (list_contractTab.size() != 0)
                            {
                                for (int m = 0; m < list_contractTab.size(); m++)//该园区该片区每个承包区
                                {
                                    build.append("[");
                                    List<CoordinatesBean> list_CoordinatesBean_contractTab = db.findAll(Selector.from(CoordinatesBean.class).where("contractid", "=", list_contractTab.get(m).getid()).and("type", "=", farm_boundary));
                                    if (list_CoordinatesBean_contractTab.size() != 0)
                                    {
                                        for (int k = 0; k < list_CoordinatesBean_contractTab.size(); k++)
                                        {
                                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLng() + "\"" + "}" + ",");
                                        }
                                        build.replace(build.length() - 1, build.length(), "");
                                        build.append("],");
                                    } else
                                    {
                                        build.append("],");
                                    }


                                }
                            }


                        }
                    }
                }
            }

            build.replace(build.length() - 1, build.length(), "");
            build.append("]}");
            build.toString();
            build.toString();


        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return build.toString();
    }

    public static String getBoundary_farm_free(Context context, String uid, String farm_boundary)
    {
        StringBuffer build = new StringBuffer();
        build.append("{\"ResultCode\":1,\"Exception\":\"\",\"AffectedRows\":\"3\",\"Rows\":[");
        DbUtils db = DbUtils.create(context);
        try
        {
            List<parktab> list_parktab = db.findAll(Selector.from(parktab.class).where("uid", "=", uid));
            if (list_parktab.size() != 0)
            {
                for (int i = 0; i < list_parktab.size(); i++)//每个园区
                {
                    build.append("[");
                    String order_last = "0";
                    List<DbModel> dbModels = db.findDbModelAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", "").groupBy("orders").select("orders", "count(orders)").orderBy("orders", true));
                    if (dbModels.size() != 0)
                    {
                        order_last = dbModels.get(0).getString("orders");
                    }
                    List<CoordinatesBean> list_CoordinatesBean_park = db.findAll(Selector.from(CoordinatesBean.class).where("parkid", "=", list_parktab.get(i).getid()).and("type", "=", farm_boundary).and("areaid", "=", "").and("orders", "=", order_last));
                    if (list_CoordinatesBean_park.size() != 0)
                    {
                        for (int j = 0; j < list_CoordinatesBean_park.size(); j++)
                        {
                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_park.get(j).getLng() + "\"" + "}" + ",");
                        }
                        build.replace(build.length() - 1, build.length(), "");
                        build.append("],");
                    } else
                    {
                        build.append("],");
                    }

//片区
                    List<areatab> list_areatab = db.findAll(Selector.from(areatab.class).where("parkid", "=", list_parktab.get(i).getid()));
                    if (list_areatab.size() != 0)
                    {
                        for (int j = 0; j < list_areatab.size(); j++)//该园区每个片区
                        {
                            build.append("[");
                            List<CoordinatesBean> list_CoordinatesBean_area = db.findAll(Selector.from(CoordinatesBean.class).where("areaid", "=", list_areatab.get(j).getid()).and("contractid", "=", "").and("type", "=", farm_boundary));
                            if (list_CoordinatesBean_area.size() != 0)
                            {
                                for (int k = 0; k < list_CoordinatesBean_area.size(); k++)
                                {
                                    build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_area.get(k).getLng() + "\"" + "}" + ",");
                                }
                                build.replace(build.length() - 1, build.length(), "");
                                build.append("],");

                            } else
                            {
                                build.append("],");
                            }


//承包区
                            List<contractTab> list_contractTab = db.findAll(Selector.from(contractTab.class).where("areaid", "=", list_areatab.get(j).getid()));
                            if (list_contractTab.size() != 0)
                            {
                                for (int m = 0; m < list_contractTab.size(); m++)//该园区该片区每个承包区
                                {
                                    build.append("[");
                                    List<CoordinatesBean> list_CoordinatesBean_contractTab = db.findAll(Selector.from(CoordinatesBean.class).where("contractid", "=", list_contractTab.get(m).getid()).and("type", "=", farm_boundary));
                                    if (list_CoordinatesBean_contractTab.size() != 0)
                                    {
                                        for (int k = 0; k < list_CoordinatesBean_contractTab.size(); k++)
                                        {
                                            build.append("{" + "\"" + "id" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getId() + "\"" + "," + "\"" + "uid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUid() + "\"" + "," + "\"" + "parkid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkId() + "\"" + "," + "\"" + "areaid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getAreaId() + "\"" + "," + "\"" + "contractid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractid() + "\"" + "," + "\"" + "type" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getType() + "\"" + "," + "\"" + "batchid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getBatchid() + "\"" + "," + "\"" + "numofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getNumofplant() + "\"" + "," + "\"" + "saleid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getSaleid() + "\"" + "," + "\"" + "weightofplant" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getWeightofplant() + "\"" + "," + "\"" + "uuid" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getUuid() + "\"" + "," + "\"" + "parkname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getparkName() + "\"" + "," + "\"" + "areaname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getareaName() + "\"" + "," + "\"" + "contractname" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getContractname() + "\"" + "," + "\"" + "orders" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getOrders() + "\"" + "," + "\"" + "coordinatestime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getCoordinatestime() + "\"" + "," + "\"" + "registime" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getRegistime() + "\"" + "," + "\"" + "lat" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLat() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + list_CoordinatesBean_contractTab.get(k).getLng() + "\"" + "}" + ",");
                                        }
                                        build.replace(build.length() - 1, build.length(), "");
                                        build.append("],");
                                    } else
                                    {
                                        build.append("],");
                                    }


                                }
                            }

                        }
                    }
                }
            }

            build.replace(build.length() - 1, build.length(), "");
            build.append("]}");
            build.toString();
            build.toString();


        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return build.toString();
    }

    public static List<contractTab> getBreakOffListByAreaID(Context context)
    {
        List<contractTab> listdata = FileHelper.getAssetsData(context, "contractTab", contractTab.class);
        DbUtils db = DbUtils.create(context);
        for (int i = 0; i < listdata.size(); i++)
        {

            List<breakofftab> list = null;
            try
            {
                list = db.findAll(Selector.from(breakofftab.class).where("contractId", "=", listdata.get(i).getid()));
                if (list != null)
                {
                    listdata.get(i).setbreakofftabList(list);
                } else
                {
                    list = new ArrayList<>();
                    listdata.get(i).setbreakofftabList(list);
                }

            } catch (DbException e)
            {
                e.printStackTrace();
            }
        }

        return listdata;
    }

    /**
     * 方法1：检查某表列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    public static boolean checkColumnExist1(SQLiteDatabase db, String tableName, String columnName)
    {
        boolean result = false;
        Cursor cursor = null;
        try
        {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e)
        {
//            Log.e(TAG, "checkColumnExists1..." + e.getMessage());
        } finally
        {
            if (null != cursor && !cursor.isClosed())
            {
                cursor.close();
            }
        }

        return result;
    }

    public static boolean alterTable(Context context)
    {

        String[] str = new String[]{"isInnerPoint", "isCenterPoint", "note"};
        DbUtils db = DbUtils.create(context);
        SQLiteDatabase sqLiteDatabase = db.getDatabase();

        for (int i = 0; i < str.length; i++)
        {
            boolean isexist = SqliteDb.checkColumnExist1(sqLiteDatabase, "CoordinatesBean", str[i]);
            if (!isexist)
            {
                String sql = "alter table  CoordinatesBean  add column " + str[i] + " NVARCHAR(10)";
                sqLiteDatabase.execSQL(sql);
            }
        }
        return true;
    }

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

    public static <T> boolean deletetemp(Context context)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(CoordinatesBean.class, WhereBuilder.b("areaid", "!=", ""));
        } catch (DbException e)
        {
            e.printStackTrace();
            String a = e.getMessage();
            return false;
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

    public static <T> boolean deletesellOrderDetailTab(Context context, Class<T> c, String productBatch, String contractId)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(c, WhereBuilder.b("productBatch", "=", productBatch).and("contractId", "=", contractId));
        } catch (DbException e)
        {
            e.printStackTrace();
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
            db.delete(c, WhereBuilder.b("id", "=", Id));
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

    public static <T> boolean deleteCoordinates(Context context)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.delete(CoordinatesBean.class, WhereBuilder.b("areaid", "=", "13"));
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

    public static List<DepartmentBean> getDepartment(Context context)
    {
        DbUtils db = DbUtils.create(context);
        List<DepartmentBean> list = new ArrayList<>();
        try
        {
            List<parktab> list_parktab = db.findAll(Selector.from(parktab.class));
            if (list_parktab != null)
            {
                for (int i = 0; i < list_parktab.size(); i++)
                {
                    DepartmentBean department = new DepartmentBean();
                    department.setId(list_parktab.get(i).getid());
                    department.setType("园区");
                    department.setName(list_parktab.get(i).getparkName());
                    list.add(department);//添加
                    List<areatab> list_areatab = db.findAll(Selector.from(areatab.class).where("parkid", "=", list_parktab.get(i).getid()));
                    if (list_areatab != null)
                    {
                        for (int j = 0; j < list_areatab.size(); j++)
                        {
                            DepartmentBean department1 = new DepartmentBean();
                            department1.setId(list_areatab.get(i).getid());
                            department1.setType("片区");
                            department1.setName(list_parktab.get(i).getparkName() + "-" + list_areatab.get(j).getareaName());
                            list.add(department1);//添加
                            List<contractTab> list_contractTab = db.findAll(Selector.from(contractTab.class));
                            if (list_contractTab != null)
                            {
                                for (int k = 0; k < list_contractTab.size(); k++)
                                {
                                    DepartmentBean department2 = new DepartmentBean();
                                    department2.setId(list_contractTab.get(i).getid());
                                    department2.setType("承包区");
                                    department2.setName(list_parktab.get(i).getparkName() + "-" + list_areatab.get(j).getareaName() + "-" + list_contractTab.get(k).getContractNum());
                                    list.add(department2);//添加
                                }
                            }
                        }

                    }
                }
            }
        } catch (DbException e)
        {
            e.printStackTrace();
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

    public static <T> List<T> getZS(Context context, Class<T> c, String areaid)
    {
        DbUtils db = DbUtils.create(context);
        List<T> list = null;
        try
        {
            list = db.findAll(Selector.from(c).where("areaid", "=", areaid));
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

    public static void updatesellOrderDetailTab(Context context, sellOrderDetailTab sellOrderDetailTab)
    {
        DbUtils db = DbUtils.create(context);
        try
        {
            db.update(sellOrderDetailTab, "orderid");
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    public static <T> List<T> getAllsellOrderDetailTab(Context context, Class<T> c)
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

    public static void initPark(Context context)
    {

        parktab parktab = new parktab();
        parktab.setuId("60");
        parktab.setid("15");
        parktab.setparkName("一号园区");
        save(context, parktab);

        parktab = new parktab();
        parktab.setuId("60");
        parktab.setid("16");
        parktab.setparkName("二号园区");
        save(context, parktab);

        parktab = new parktab();
        parktab.setuId("60");
        parktab.setid("17");
        parktab.setparkName("三号园区");
        save(context, parktab);

    }

    public static void initArea(Context context)
    {

        areatab areatab = new areatab();
        areatab.setuId("60");
        areatab.setid("10");
        areatab.setareaName("片区一号");
        areatab.setparkId("15");
        areatab.setparkName("一号园区");
        save(context, areatab);

        areatab = new areatab();
        areatab.setuId("60");
        areatab.setid("13");
        areatab.setareaName("片区二号");
        areatab.setparkId("15");
        areatab.setparkName("一号园区");
        save(context, areatab);

        areatab = new areatab();
        areatab.setuId("60");
        areatab.setid("14");
        areatab.setareaName("片区三号");
        areatab.setparkId("15");
        areatab.setparkName("一号园区");
        save(context, areatab);

        areatab = new areatab();
        areatab.setuId("60");
        areatab.setid("15");
        areatab.setareaName("片区四号");
        areatab.setparkId("15");
        areatab.setparkName("一号园区");
        save(context, areatab);
    }

    public static void initContract(Context context)
    {
//园区一号片区一号
        contractTab contractTab = new contractTab();
        contractTab.setid("7");
        contractTab.setContractNum("承包区一");
        contractTab.setAreaId("10");
        contractTab.setareaName("片区一号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("10");
        contractTab.setContractNum("承包区二");
        contractTab.setAreaId("10");
        contractTab.setareaName("片区一号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("11");
        contractTab.setContractNum("承包区三");
        contractTab.setAreaId("10");
        contractTab.setareaName("片区一号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        //园区一号片区二号
        contractTab = new contractTab();
        contractTab.setid("12");
        contractTab.setContractNum("承包区一");
        contractTab.setAreaId("13");
        contractTab.setareaName("片区二号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("13");
        contractTab.setContractNum("承包区二");
        contractTab.setAreaId("13");
        contractTab.setareaName("片区二号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("14");
        contractTab.setContractNum("承包区三");
        contractTab.setAreaId("13");
        contractTab.setareaName("片区二号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        //园区一号片区三号
        contractTab = new contractTab();
        contractTab.setid("15");
        contractTab.setContractNum("承包区一");
        contractTab.setAreaId("14");
        contractTab.setareaName("片区三号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("16");
        contractTab.setContractNum("承包区二");
        contractTab.setAreaId("14");
        contractTab.setareaName("片区三号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        //园区一号片区四号
        contractTab = new contractTab();
        contractTab.setid("17");
        contractTab.setContractNum("承包区一");
        contractTab.setAreaId("15");
        contractTab.setareaName("片区四号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("18");
        contractTab.setContractNum("承包区二");
        contractTab.setAreaId("15");
        contractTab.setareaName("片区四号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

        contractTab = new contractTab();
        contractTab.setid("19");
        contractTab.setContractNum("承包区三");
        contractTab.setAreaId("15");
        contractTab.setareaName("片区四号");
        contractTab.setparkId("15");
        contractTab.setparkName("一号园区");
        contractTab.setuId("60");
        save(context, contractTab);

    }
}
