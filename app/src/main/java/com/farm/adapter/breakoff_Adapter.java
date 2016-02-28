package com.farm.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BreakOffTab;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.bean.goodslisttab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.ui.Common_JobDetail_Show_;
import com.farm.widget.CustomDialog_EditDLInfor;
import com.farm.widget.CustomDialog_ListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.swipelistview.SimpleSwipeListener;
import com.swipelistview.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/17.
 * 适配肥料选择界面的左边导航栏
 */
public class breakoff_Adapter extends BaseExpandableListAdapter
{
    Button btn_sure;
    EditText et_numberofbreakoff;
    EditText et_note;
    TextView tv_time;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    SwipeLayout swipeLayout;
    int currentgroupPosition = 0;
    int currentchildPosition = 0;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();
    ExpandableListView mainlistview;
    Dictionary tempDic = new Dictionary();
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<contractTab> listData;
    ListView list;

    public breakoff_Adapter(Context context, List<contractTab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getBreakOffTabList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getBreakOffTabList().get(childPosition);
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    HashMap<Integer, HashMap<Integer, View>> lmap = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map = new HashMap<>();
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView tv_note;
        public TextView tv_time;
        public TextView tv_numberofbreakoff;
    }

    private void getListData(String jobid)
    {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("jobid", jobid);
        params.addQueryStringParameter("userid", "15");
        params.addQueryStringParameter("uid", "12");
        params.addQueryStringParameter("username", "戴泉");
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "");
        params.addQueryStringParameter("page_size", "10");
        params.addQueryStringParameter("page_index", "10");
        params.addQueryStringParameter("action", "tjobGetByID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BreakOffTab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), BreakOffTab.class);
                        Intent intent = new Intent(context, Common_JobDetail_Show_.class);
                        intent.putExtra("bean", listNewData.get(0));
                        context.startActivity(intent);
                    } else
                    {
                        listNewData = new ArrayList<BreakOffTab>();
                    }
                } else
                {
                    AppContext.makeToast(context, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });
    }

    //设置子item的组件
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<BreakOffTab> childData = listData.get(groupPosition).getBreakOffTabList();
        final BreakOffTab BreakOffTab = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_breakoff, null);
            listItemView = new ListItemView();
            listItemView.tv_numberofbreakoff = (TextView) convertView.findViewById(R.id.tv_numberofbreakoff);
            listItemView.tv_note = (TextView) convertView.findViewById(R.id.tv_note);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(listItemView);
            convertView.setTag(R.id.tag_bean, BreakOffTab);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BreakOffTab BreakOffTab = (com.farm.bean.BreakOffTab) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, Common_JobDetail_Show_.class);
                    intent.putExtra("bean", BreakOffTab);
                    context.startActivity(intent);

                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            if (!BreakOffTab.getNote().equals(""))
            {
                listItemView.tv_note.setText(BreakOffTab.getNote());
            }

                listItemView.tv_time.setText(BreakOffTab.getDateofbreakoff());
                listItemView.tv_numberofbreakoff.setText("断蕾"+BreakOffTab.getNumberofbreakoff()+"株");
        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
//        for (int i = 0, cnt = getGroupCount(); i < cnt; i++)
//        {
//            if (groupPosition != i && mainlistview.isGroupExpanded(i))
//            {
//                mainlistview.collapseGroup(i);
//            }
//        }
//        for (int i = 0; i < firstItemName.size(); i++)
//        {
//            mainlistview.expandGroup(i);
//        }
//        mainlistview.expandGroup(groupPosition);
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getBreakOffTabList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getBreakOffTabList().size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return listData.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return listData.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    //设置父item组件
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_parent_breakoff, null);
        }
        TextView tv_call = (TextView) convertView.findViewById(R.id.tv_call);
        TextView tv_output = (TextView) convertView.findViewById(R.id.tv_output);
        TextView tv_numberofbreakoff = (TextView) convertView.findViewById(R.id.tv_numberofbreakoff);
        TextView tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
        Button btn_addBreakOffInfo = (Button) convertView.findViewById(R.id.btn_addBreakOffInfo);
        swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        // 当隐藏的删除menu被打开的时候的回调函数
        swipeLayout.addSwipeListener(new SimpleSwipeListener()
        {
            @Override
            public void onOpen(SwipeLayout layout)
            {
//                Toast.makeText(context, "Open", Toast.LENGTH_SHORT).show();
            }
        });
        // 双击的回调函数
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener()
        {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface)
            {
//                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        // 添加删除布局的点击事件
        convertView.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
//                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                // 点击完成之后，关闭删除menu
                swipeLayout.close();
            }
        });


        tv_call.setTag(groupPosition);
        tv_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phone = listData.get(Integer.valueOf(v.getTag().toString())).getAreaId();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        tv_contractname.setTag(groupPosition);
        tv_contractname.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                mainlistview.expandGroup(Integer.valueOf(v.getTag().toString()));
//                TextView textView = (TextView) v;
            }
        });
        btn_addBreakOffInfo.setTag(groupPosition);
        btn_addBreakOffInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_AddBreakOffInfo(listData.get(Integer.valueOf(v.getTag().toString())).getid(),listData.get(Integer.valueOf(v.getTag().toString())).getContractNum());
            }
        });
        tv_contractname.setText(listData.get(groupPosition).getContractNum());
        int numberOfBreakOff=0;
        List<BreakOffTab> list_breakoff=listData.get(groupPosition).getBreakOffTabList();
        for (int i = 0; i <list_breakoff.size() ; i++)
        {
            numberOfBreakOff=numberOfBreakOff+Integer.valueOf(list_breakoff.get(i).getNumberofbreakoff());
        }
        tv_numberofbreakoff.setText("共断蕾"+numberOfBreakOff+"株");
        tv_output.setText("产量:"+listData.get(groupPosition).getPlantnumber()+"株");

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
    public void showDialog_AddBreakOffInfo(final String contractid,final String contractname)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_addbreakoffinfo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(context, R.style.MyDialog, dialog_layout);
        et_numberofbreakoff = (EditText) dialog_layout.findViewById(R.id.et_numberofbreakoff);
        tv_time = (TextView) dialog_layout.findViewById(R.id.tv_time);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        tv_time.setText(utils.getTime());
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String uuid = java.util.UUID.randomUUID().toString();
                commembertab commembertab=AppContext.getUserInfo(context);
                BreakOffTab breakOffTab=new BreakOffTab();
                breakOffTab.setid(uuid);
                breakOffTab.setContractId(contractid);
                breakOffTab.setContractNum(contractname);
                breakOffTab.setparkId(commembertab.getparkId());
                breakOffTab.setparkName(commembertab.getparkName());
                breakOffTab.setAreaId(commembertab.getareaId());
                breakOffTab.setareaName(commembertab.getareaName());
                breakOffTab.setregDate(utils.getToday());
                breakOffTab.setregDate(utils.getToday());
                breakOffTab.setregDate(utils.getToday());
                breakOffTab.setNote(et_note.getText().toString());
                breakOffTab.setNumberofbreakoff(et_numberofbreakoff.getText().toString());
                breakOffTab.setDateofbreakoff(utils.getToday());
                SqliteDb.save(context, breakOffTab);

                Intent intent = new Intent();
                intent.setAction(AppContext.BROADCAST_UPDATEBREAKOFFINFO);
                context.sendBroadcast(intent);

                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }
    public void showDialog(List<String> list)
    {
        View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(context, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                tempDic.getThirdItemID().get(currentgroupPosition).get(currentchildPosition).clear();
                tempDic.getThirdItemID().get(currentgroupPosition).get(currentchildPosition).add(bundle.getString("name"));
                currentTextView.setText(bundle.getString("name"));
                currentTextView.setTextColor(context.getResources().getColor(R.color.bg_yellow));
                TextPaint tp = currentTextView.getPaint();
                tp.setFakeBoldText(true);
            }
        });
        customDialog_listView.show();
    }

    public Dictionary getDictionary()
    {
        return tempDic;
    }
}
