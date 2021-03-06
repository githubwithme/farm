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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.bean.jobtab;
import com.farm.ui.Common_JobDetail_Show_;
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
public class CommandExecute_Adapter extends BaseExpandableListAdapter
{
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
    List<commandtab> listData;
    ListView list;

    public CommandExecute_Adapter(Context context, List<commandtab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getJobList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getJobList().get(childPosition);
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
        public TextView tv_time;
        public TextView tv_note;
        public TextView tv_pf;
        public TextView tv_jd;
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
                List<jobtab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
                        Intent intent = new Intent(context, Common_JobDetail_Show_.class);
                        intent.putExtra("bean", listNewData.get(0));
                        context.startActivity(intent);
                    } else
                    {
                        listNewData = new ArrayList<jobtab>();
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

        List<jobtab> childData = listData.get(groupPosition).getJobList();
        final jobtab jobtab = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_commandexecute, null);
            listItemView = new ListItemView();
            listItemView.tv_jd = (TextView) convertView.findViewById(R.id.tv_jd);
            listItemView.tv_pf = (TextView) convertView.findViewById(R.id.tv_pf);
            listItemView.tv_note = (TextView) convertView.findViewById(R.id.tv_note);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(listItemView);
            convertView.setTag(R.id.tag_bean, jobtab);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    jobtab jobtab = (com.farm.bean.jobtab) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, Common_JobDetail_Show_.class);
                    intent.putExtra("bean", jobtab);
                    context.startActivity(intent);

                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            if (jobtab.getPercent().equals(""))
            {
                listItemView.tv_time.setText(jobtab.getregDate().substring(5, jobtab.getregDate().lastIndexOf(" ")));
                listItemView.tv_jd.setText("进行中...");
                listItemView.tv_pf.setText("");
                listItemView.tv_note.setText("");
            } else
            {
                listItemView.tv_time.setText(jobtab.getregDate().substring(5, jobtab.getregDate().lastIndexOf(" ")));
                listItemView.tv_jd.setText("完成" + jobtab.getPercent() + "%");
                listItemView.tv_pf.setText(jobtab.getaudioJobExecPath() + "分");
                listItemView.tv_note.setText(jobtab.getassessNote());
            }

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
        if (listData.get(groupPosition).getJobList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getJobList().size();
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
            convertView = inflater.inflate(R.layout.layout_parent_commandexecute, null);
        }
        TextView tv_call = (TextView) convertView.findViewById(R.id.tv_call);
        TextView parent_textview = (TextView) convertView.findViewById(R.id.parent_textview);
        TextView tv_pq = (TextView) convertView.findViewById(R.id.tv_pq);
        TextView tv_sd = (TextView) convertView.findViewById(R.id.tv_sd);
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
                String phone = listData.get(Integer.valueOf(v.getTag().toString())).getPhone();
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
        parent_textview.setTag(groupPosition);
        parent_textview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                mainlistview.expandGroup(Integer.valueOf(v.getTag().toString()));
//                TextView textView = (TextView) v;
            }
        });
        parent_textview.setText(listData.get(groupPosition).getparkName());
        tv_pq.setText(listData.get(groupPosition).getareaName());
        String getcommStatus = listData.get(groupPosition).getcommStatus();
        if (getcommStatus.equals("0"))
        {
            tv_sd.setText("待反馈");
        } else if (getcommStatus.equals("1"))
        {
            tv_sd.setText("已反馈");
        } else if (getcommStatus.equals("2"))
        {
            tv_sd.setText("已收到");
        }

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
