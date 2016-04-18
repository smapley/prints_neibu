package com.smapley.prints2.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.smapley.prints2.R;
import com.smapley.prints2.activity.MainActivity;
import com.smapley.prints2.util.HttpUtils;
import com.smapley.prints2.util.MyData;
import com.smapley.prints2.util.ThreadSleep;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hao on 2015/11/7.
 */
@ContentView(R.layout.date)
public class Data extends Fragment {

    @ViewInject(R.id.title_item2)
    private TextView tv_title2;
    @ViewInject(R.id.title_item3)
    private TextView tv_title3;

    private final int GETDATA = 1;
    private final int UPDATA = 2;


    @ViewInject(R.id.data_ev_item13_layout)
    private RelativeLayout item13_layout;
    @ViewInject(R.id.data_ev_item23_layout)
    private RelativeLayout item23_layout;
    @ViewInject(R.id.data_ev_item33_layout)
    private RelativeLayout item33_layout;
    @ViewInject(R.id.data_ev_item43_layout)
    private RelativeLayout item43_layout;
    @ViewInject(R.id.data_ev_item53_layout)
    private RelativeLayout item53_layout;
    @ViewInject(R.id.data_ev_item63_layout)
    private RelativeLayout item63_layout;


    @ViewInject(R.id.data_tv_item11)
    private TextView item11;
    @ViewInject(R.id.data_tv_item21)
    private TextView item21;
    @ViewInject(R.id.data_tv_item31)
    private TextView item31;
    @ViewInject(R.id.data_tv_item41)
    private TextView item41;
    @ViewInject(R.id.data_tv_item51)
    private TextView item51;
    @ViewInject(R.id.data_tv_item61)
    private TextView item61;

    @ViewInject(R.id.data_tv_item12)
    private TextView item12;
    @ViewInject(R.id.data_tv_item22)
    private TextView item22;
    @ViewInject(R.id.data_tv_item32)
    private TextView item32;
    @ViewInject(R.id.data_tv_item42)
    private TextView item42;
    @ViewInject(R.id.data_tv_item52)
    private TextView item52;
    @ViewInject(R.id.data_tv_item62)
    private TextView item62;


    @ViewInject(R.id.data_ev_item13)
    private TextView item13;
    @ViewInject(R.id.data_ev_item23)
    private TextView item23;
    @ViewInject(R.id.data_ev_item33)
    private TextView item33;
    @ViewInject(R.id.data_ev_item43)
    private TextView item43;
    @ViewInject(R.id.data_ev_item53)
    private TextView item53;
    @ViewInject(R.id.data_ev_item63)
    private TextView item63;

    @ViewInject(R.id.data_ev_item13_ico)
    private TextView item13_ico;
    @ViewInject(R.id.data_ev_item23_ico)
    private TextView item23_ico;
    @ViewInject(R.id.data_ev_item33_ico)
    private TextView item33_ico;
    @ViewInject(R.id.data_ev_item43_ico)
    private TextView item43_ico;
    @ViewInject(R.id.data_ev_item53_ico)
    private TextView item53_ico;
    @ViewInject(R.id.data_ev_item63_ico)
    private TextView item63_ico;

    @ViewInject(R.id.print_keybord)
    private View print_keybord;

    private List<TextView> itemList = new ArrayList<>();
    private List<TextView> itemIcoList = new ArrayList<>();
    private List<View> itemLayoutList = new ArrayList<>();

    @ViewInject(R.id.data_tv_item1)
    private TextView item1;
    @ViewInject(R.id.data_tv_item2)
    private TextView item2;
    @ViewInject(R.id.data_tv_item3)
    private TextView item3;
    @ViewInject(R.id.data_tv_item4)
    private TextView item4;

    @ViewInject(R.id.edu)
    private TextView edu;


    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;
    private ProgressDialog dialog;
    private int NowPostion = 0;
    private TextView jin_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = x.view().inject(this, inflater, container);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在上传信息。。。");

        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {

        tv_title3.setText("保存");

        itemLayoutList.add(item13_layout);
        itemLayoutList.add(item23_layout);
        itemLayoutList.add(item33_layout);
        itemLayoutList.add(item43_layout);
        itemLayoutList.add(item53_layout);
        itemLayoutList.add(item63_layout);

        itemList.add(item13);
        itemList.add(item23);
        itemList.add(item33);
        itemList.add(item43);
        itemList.add(item53);
        itemList.add(item63);

        itemIcoList.add(item13_ico);
        itemIcoList.add(item23_ico);
        itemIcoList.add(item33_ico);
        itemIcoList.add(item43_ico);
        itemIcoList.add(item53_ico);
        itemIcoList.add(item63_ico);
    }

    public void upData() {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("user1", MyData.UserName);
                map.put("erd", item13.getText().toString());
                map.put("sand", item23.getText().toString());
                map.put("sid", item33.getText().toString());
                map.put("erx", item43.getText().toString());
                map.put("sanx", item53.getText().toString());
                map.put("six", item63.getText().toString());
                mhandler.obtainMessage(UPDATA, HttpUtils.updata(map, MyData.getUrlUpdataziliao())).sendToTarget();
            }
        }).start();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("user1", MyData.UserName);
                mhandler.obtainMessage(GETDATA, HttpUtils.updata(map, MyData.getUrlGetziliao())).sendToTarget();
            }
        }).start();
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case GETDATA:
                        // item11.setText(JSON.parseObject(msg.obj.toString(),new TypeReference<String>(){}));

                        Map<String, Object> result1 = JSON.parseObject(msg.obj.toString(), new TypeReference<Map<String, Object>>() {
                        });
                        edu.setText(result1.get("edu").toString());
                        item1.setText(result1.get("yyed2").toString());
                        item2.setText(result1.get("shui").toString());
                        item3.setText(result1.get("jiang").toString());
                        item4.setText(result1.get("yk").toString());
                        tv_title2.setText(result1.get("murl").toString());
                        Map<String, List<String>> result = JSON.parseObject(result1.get("new").toString(), new TypeReference<Map<String, List<String>>>() {
                        });
                        item11.setText(result.get("2").get(0) == null ? "" : result.get("2").get(0));
                        item12.setText(result.get("2").get(1) == null ? "" : result.get("2").get(1));
                        item13.setText(result.get("2").get(2) == null ? "" : result.get("2").get(2));
                        item21.setText(result.get("3").get(0) == null ? "" : result.get("3").get(0));
                        item22.setText(result.get("3").get(1) == null ? "" : result.get("3").get(1));
                        item23.setText(result.get("3").get(2) == null ? "" : result.get("3").get(2));
                        item31.setText(result.get("4").get(0) == null ? "" : result.get("4").get(0));
                        item32.setText(result.get("4").get(1) == null ? "" : result.get("4").get(1));
                        item33.setText(result.get("4").get(2) == null ? "" : result.get("4").get(2));
                        item41.setText(result.get("5").get(0) == null ? "" : result.get("5").get(0));
                        item42.setText(result.get("5").get(1) == null ? "" : result.get("5").get(1));
                        item43.setText(result.get("5").get(2) == null ? "" : result.get("5").get(2));
                        item51.setText(result.get("6").get(0) == null ? "" : result.get("6").get(0));
                        item52.setText(result.get("6").get(1) == null ? "" : result.get("6").get(1));
                        item53.setText(result.get("6").get(2) == null ? "" : result.get("6").get(2));
                        item61.setText(result.get("7").get(0) == null ? "" : result.get("7").get(0));
                        item62.setText(result.get("7").get(1) == null ? "" : result.get("7").get(1));
                        item63.setText(result.get("7").get(2) == null ? "" : result.get("7").get(2));

                        break;
                    case UPDATA:
                        dialog.dismiss();
                        Integer result2 = JSON.parseObject(msg.obj.toString(), new TypeReference<Integer>() {
                        });
                        if (null != result2 && result2 >= 0) {
                            getData();
                            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();

                        }
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    @Event({R.id.back, R.id.title_item3, R.id.data_ev_item13_layout, R.id.data_ev_item23_layout, R.id.data_ev_item33_layout, R.id.data_ev_item43_layout, R.id.data_ev_item53_layout, R.id.data_ev_item63_layout,
            R.id.key_item1, R.id.key_item2, R.id.key_item3, R.id.key_item5, R.id.key_item6, R.id.key_item7, R.id.key_item9,
            R.id.key_item10, R.id.key_item11, R.id.key_item8, R.id.key_item12, R.id.key_item13, R.id.key_item15})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_item3:
                upData();
                break;
            case R.id.back:
                print_keybord.setVisibility(View.GONE);
                ((MainActivity) getActivity()).bottom.setVisibility(View.VISIBLE);
                break;
            case R.id.data_ev_item13_layout:
                editItem(0);
                break;
            case R.id.data_ev_item23_layout:
                editItem(1);
                break;
            case R.id.data_ev_item33_layout:
                editItem(2);
                break;
            case R.id.data_ev_item43_layout:
                editItem(3);
                break;
            case R.id.data_ev_item53_layout:
                editItem(4);
                break;
            case R.id.data_ev_item63_layout:
                editItem(5);
                break;
            case R.id.key_item8:

                break;
            case R.id.key_item12:
                if (NowPostion <= 4)
                    editItem(NowPostion + 1);
                break;

            case R.id.key_item15:
                String data = jin_text.getText().toString();
                if (data != null && !data.equals("")) {
                    if (data.length() > 0 || data.equals("0")) {
                        if (data.indexOf(".") == -1) {
                            jin_text.setText(jin_text.getText() + ((TextView) view).getText().toString());
                        }
                    }
                }
                break;

            default:
                if (jin_text.getText().toString().equals("0")) {
                    jin_text.setText(((TextView) view).getText().toString());
                } else {
                    jin_text.setText(jin_text.getText() + ((TextView) view).getText().toString());
                }
                break;
        }
    }

    private void editItem(int position) {
        scrollView.scrollTo(0, 150 + 50 * position);
        for (int i = 0; i < 6; i++) {
            if (position == i) {
                itemIcoList.get(i).setVisibility(View.VISIBLE);
                jin_text = itemList.get(i);
                jin_text.setText("");
                NowPostion = i;
            } else {
                itemIcoList.get(i).setVisibility(View.GONE);

            }
        }
        print_keybord.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).bottom.setVisibility(View.GONE);

    }
}
