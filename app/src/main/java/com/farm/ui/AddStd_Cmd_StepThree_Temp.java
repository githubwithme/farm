package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CustomFragmentPagerAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepThree_Temp extends Fragment
{
    List<Fragment> fragments;
    List<goodslisttab> list_goods;
    commembertab commembertab;
    Dictionary dic_park;
    Dictionary dic_area;
    FragmentCallBack fragmentCallBack = null;
    SelectorFragment selectorUi;
    Fragment mContent = new Fragment();
    HashMap<String, List<goodslisttab>> map_goods;
    private List<String> list;
    private TextView[] tvList;
    private View[] views;
    private LayoutInflater inflater;
    private int currentItem = 0;
    private CustomFragmentPagerAdapter customFragmentPagerAdapter;

    @ViewById
    ImageView iv_dowm_tab;
    @ViewById
    Button btn_next;
    @ViewById
    HorizontalScrollView cmd_tools_scrlllview;
    @ViewById
    LinearLayout cmd_tools;

    @Click
    void btn_next()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 2);
        fragmentCallBack.callbackFun2(bundle);
    }


    @AfterViews
    void afterOncreate()
    {
        inflater = LayoutInflater.from(getActivity());
        getArealist();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_three_temp, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }


    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(List<String> data)
    {
        list = data;
        tvList = new TextView[list.size()];
        views = new View[list.size()];

        for (int i = 0; i < list.size(); i++)
        {
            View view = inflater.inflate(R.layout.item_addstd_cmd_area, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(list.get(i));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lp.setMargins(40, 1, 40, 1);
            textView.setLayoutParams(lp);
            cmd_tools.addView(view);
            tvList[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int i = v.getId();
            switchContent(mContent, fragments.get(i));
            if (currentItem != i)
            {
                changeTextColor(i);
                changeTextLocation(i);
            }
            currentItem = i;
        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager()
    {
        fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            Fragment fragment = new Area_Cmd_Fragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            bundle.putParcelableArrayList("GOODS", (ArrayList<? extends Parcelable>) map_goods.get(dic_area.getFirstItemID().get(i)));
            bundle.putString("FN", dic_area.getFirstItemName().get(i));
            bundle.putString("FI", dic_area.getFirstItemID().get(i));
            bundle.putStringArrayList("SI", (ArrayList<String>) dic_area.getSecondItemID().get(i));
            bundle.putStringArrayList("SN", (ArrayList<String>) dic_area.getSecondItemName().get(i));
//            bundle.putStringArray("TI", dic_area.getThirdItemID().get(index).get());
            bundle.putStringArrayList("TN", (ArrayList<String>) dic_area.getThirdItemName().get(0).get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        changeTextColor(0);
        changeTextLocation(0);
        switchContent(mContent, fragments.get(0));
    }



    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id)
    {
        for (int i = 0; i < tvList.length; i++)
        {
            if (i != id)
            {
//                tvList[i].setBackgroundColor(0x00000000);
                tvList[i].setTextColor(0xFF000000);

                TextPaint tp = tvList[i].getPaint();
                tvList[i].setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_7));
                tp.setFakeBoldText(false);
            }
        }
//        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFFF5D5E);

        tvList[id].setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_7));
        TextPaint tp = tvList[id].getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition)
    {
        int x = (views[clickPosition].getTop());
        cmd_tools_scrlllview.smoothScrollTo(0, x);
    }

    private void getArealist()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "getstdPark");
        params.addQueryStringParameter("action", "getDict");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;//0
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic_area = lsitNewData.get(0);
                            showToolsView(dic_area.getFirstItemName());
                        }
                    } else
                    {
                        lsitNewData = new ArrayList<Dictionary>();
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void getGoodsSum()
    {
        String goodsid = "";
        String goodsname = "";
        for (int i = 0; i < list_goods.size(); i++)
        {
            goodsid = goodsid + list_goods.get(i).getId() + ",";
            goodsname = goodsname + list_goods.get(i).getgoodsName() + ",";
        }
        commandtab_single commandtab_single = com.farm.bean.commandtab_single.getInstance();
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        String aa = commandtab_single.getNongziId();
        params.addQueryStringParameter("goodsId", goodsid.substring(0, goodsid.length() - 1));
        params.addQueryStringParameter("action", "getGoodsSum");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                        {
                        String aa = result.getRows().toJSONString();
                        int size = result.getRows().size();
                        map_goods = new HashMap<String, List<goodslisttab>>();
                        for (int i = 0; i < size; i++)
                        {
                            String parkId = result.getRows().getJSONObject(i).getString("parkId");
                            String parkName = result.getRows().getJSONObject(i).getString("parkName");
//                            JSONArray jsonarray = result.getRows().getJSONObject(i).getJSONArray("goodsSum");
                            String jsonarray = result.getRows().getJSONObject(i).getString("goodsSum");
                            String [] listsum=jsonarray.split("[,]");
                            HashMap<String, String> map = new HashMap<String, String>();
                            List<goodslisttab> list = new ArrayList<goodslisttab>();
                            for (int k = 0; k < list_goods.size(); k++)
                            {
                                goodslisttab goodslisttab = new goodslisttab();
                                goodslisttab.setParkId(parkId);
                                goodslisttab.setParkName(parkName);
                                goodslisttab.setdaysBeforeWarning(list_goods.get(k).getdaysBeforeWarning());
                                goodslisttab.setgoodsName(list_goods.get(k).getgoodsName());
                                goodslisttab.setgoodsNote(list_goods.get(k).getgoodsNote());
                                goodslisttab.setgoodsProducer(list_goods.get(k).getgoodsProducer());
                                goodslisttab.setgoodsSpec(list_goods.get(k).getgoodsSpec());
//                                goodslisttab.setGoodsSum(jsonarray.get(k).toString());
                                if (listsum.length!=0)
                                {
                                    if (listsum[k].equals(""))
                                    {
                                        listsum[k] = "0";
                                        goodslisttab.setGoodsSum(listsum[k]);
                                    }else
                                    {
                                        goodslisttab.setGoodsSum(listsum[k]);
                                    }

                                }
                                goodslisttab.setgoodsTypeID(list_goods.get(k).getgoodsTypeID());
                                goodslisttab.setgoodsunit(list_goods.get(k).getgoodsunit());
                                goodslisttab.setId(list_goods.get(k).getId());
                                goodslisttab.setImgurl(list_goods.get(k).getImgurl());
                                goodslisttab.setisDelete(list_goods.get(k).getisDelete());
                                goodslisttab.setlevelOfWarning(list_goods.get(k).getlevelOfWarning());
                                goodslisttab.setregDate(list_goods.get(k).getregDate());
                                goodslisttab.setuID(list_goods.get(k).getuID());
                                goodslisttab.setuserDefTypeID(list_goods.get(k).getuserDefTypeID());
                                //坚修改
                                goodslisttab.setIsExchange(list_goods.get(k).getIsExchange());
                                goodslisttab.setFirs(list_goods.get(k).getFirs());
                                goodslisttab.setSec(list_goods.get(k).getSec());
                                goodslisttab.setThree(list_goods.get(k).getThree());
                                goodslisttab.setThreeNum(list_goods.get(k).getThreeNum());


                                list.add(goodslisttab);
                            }
                            map_goods.put(parkId, list);
                        }
                        initPager();
                    } else
                    {
                        lsitNewData = new ArrayList<Dictionary>();
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    public void update()
    {
        SqliteDb.deleteAllSelectCmdArea(getActivity(), goodslisttab_flsl.class);
        list_goods = SqliteDb.getGoods(getActivity(), goodslisttab.class);
        getGoodsSum();
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().finish();
    }
}
