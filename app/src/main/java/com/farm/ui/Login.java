package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.service.LogService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
@EActivity(R.layout.login)
public class Login extends Activity
{

    @ViewById
    LinearLayout login;
    @ViewById
    RelativeLayout rl_down;
    @ViewById
    CheckBox cb_autologin;
    @ViewById
    View line;
    @ViewById
    EditText et_name;
    @ViewById
    EditText et_psw;
    //    @ViewById
//    CircleImageView btn_login;
    @ViewById
    LinearLayout ll_login;
    @ViewById
    ProgressBar pb_logining;
    @ViewById
    ImageView iv_down;
    PopupWindow popupWindow_tab;
    View popupWindowView_tab;
    List<commembertab> list;

    @Click
    void register()
    {
//        Intent intent = new Intent(Login.this, Register_StepOne_.class);
//        startActivity(intent);
    }

    @Click
    void vistor()
    {
//        Intent intent = new Intent(Login.this, NCZ_MainActivity_.class);
//        startActivity(intent);
//        finish();
    }

    @Click
    void btn_forgetpdw()
    {
        Intent intent = new Intent(Login.this, FindPassword_.class);
        startActivity(intent);
    }

    @Click
    void rl_down()
    {
        list = SqliteDb.getUserList(this, commembertab.class);
        if (popupWindow_tab!=null && popupWindow_tab.isShowing())
        {
            Toast.makeText(this, "请不要重复点击", Toast.LENGTH_SHORT).show();
        } else
        {
            showPop_tab();
        }

    }

    @Click
    void login()
    {
        if (!et_name.getText().toString().equals("") && !et_psw.getText().toString().equals(""))
        {
            ll_login.setVisibility(View.GONE);
            pb_logining.setVisibility(View.VISIBLE);
            startLogin(et_name.getText().toString(), et_psw.getText().toString());
        } else
        {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @AfterViews
    void afterOncreate()
    {
        Intent intent_log = new Intent(Login.this, LogService.class);
        startService(intent_log);

        commembertab commembertab = AppContext.getUserInfo(this);
        if (commembertab != null && !commembertab.getuserPwd().equals(""))
        {
            ll_login.setVisibility(View.GONE);
            cb_autologin.setChecked(true);
            pb_logining.setVisibility(View.VISIBLE);
            et_name.setText(commembertab.getuserName());
            et_psw.setText(commembertab.getuserPwd());
            startLogin(commembertab.getuserName(), commembertab.getuserPwd());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    private void startLogin(final String username, final String psw)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("userpwd", psw);
        params.addQueryStringParameter("action", "userLogin");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<commembertab> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), commembertab.class);
                        if (listData == null)
                        {
                            Toast.makeText(Login.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            String level = listData.get(0).getnlevel().toString();
                            if (level.equals("0"))// 农场主
                            {
                                Intent intent = new Intent(Login.this, NCZ_MainActivity_.class);
                                startActivity(intent);
                                finish();
                            } else if (level.equals("-1"))// 技术部门主管
                            {
                                Intent intent = new Intent(Login.this, NCZ_MainActivity_.class);
                                startActivity(intent);
                                finish();
                            } else if (level.equals("1"))// 场长
                            {
                                Intent intent = new Intent(Login.this, CZ_MainActivity_.class);
                                startActivity(intent);
                                finish();
                            } else if (level.equals("2"))// 片管
                            {
                                Intent intent = new Intent(Login.this, PG_MainActivity_.class);
                                startActivity(intent);
                                finish();
                            } else
                            {
                                Toast.makeText(Login.this, "该用户已被锁定，暂不能登陆!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            commembertab commembertab = listData.get(0);
                            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                            Editor editor = sp.edit();
                            editor.putString("userName", commembertab.getuserName());
                            editor.commit();
                            if (cb_autologin.isChecked())
                            {
                                commembertab.setAutoLogin("1");
                                commembertab.setuserPwd(psw);
                                SqliteDb.save(Login.this, commembertab);
                            } else
                            {
                                commembertab.setAutoLogin("0");
                                commembertab.setuserPwd("");
                                SqliteDb.save(Login.this, commembertab);
                            }
                        }
                    } else
                    {
                        Toast.makeText(Login.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
                        ll_login.setVisibility(View.VISIBLE);
                        pb_logining.setVisibility(View.GONE);
                    }
                } else
                {
                    Toast.makeText(Login.this, "连接数据服务器出错了!", Toast.LENGTH_SHORT).show();
                    ll_login.setVisibility(View.VISIBLE);
                    pb_logining.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                Toast.makeText(Login.this, "连接服务器出错了!", Toast.LENGTH_SHORT).show();
                ll_login.setVisibility(View.VISIBLE);
                pb_logining.setVisibility(View.GONE);
            }
        });

    }

    public void showPop_tab()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupWindowView_tab = layoutInflater.inflate(R.layout.popup_userlist, null);// 外层
        popupWindowView_tab.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (popupWindow_tab.isShowing()))
                {
                    popupWindow_tab.dismiss();
                    iv_down.setBackground(getResources().getDrawable(R.drawable.downward));
                    return true;
                }
                return false;
            }
        });
        popupWindowView_tab.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (popupWindow_tab.isShowing())
                {
                    popupWindow_tab.dismiss();
                    iv_down.setBackground(getResources().getDrawable(R.drawable.downward));
                }
                return false;
            }
        });
        popupWindow_tab = new PopupWindow(popupWindowView_tab, et_name.getWidth(), LayoutParams.WRAP_CONTENT, true);
        popupWindow_tab.showAsDropDown(et_name, 0, 0);
        popupWindow_tab.setOutsideTouchable(true);
        ListView listview = (ListView) popupWindowView_tab.findViewById(R.id.lv_yq);
        listview.setAdapter(new yqAdapter(this, list));
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                et_name.setText(list.get(postion).getuserName());
                iv_down.setBackground(getResources().getDrawable(R.drawable.downward));
                popupWindow_tab.dismiss();
            }
        });
    }

    public class yqAdapter extends BaseAdapter
    {
        List<commembertab> listItems;
        private LayoutInflater listContainer;

        class ListItemView
        {
            public TextView tv_yq;
        }

        public yqAdapter(Context context, List<commembertab> list)
        {
            this.listContainer = LayoutInflater.from(context);
            this.listItems = list;
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                convertView = listContainer.inflate(R.layout.userlist_item, null);
                listItemView = new ListItemView();
                listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            listItemView.tv_yq.setText(listItems.get(position).getuserName());
            return convertView;
        }

        @Override
        public int getCount()
        {
            return listItems.size();
        }

        @Override
        public Object getItem(int arg0)
        {
            return null;
        }

        @Override
        public long getItemId(int arg0)
        {
            return 0;
        }
    }
}
