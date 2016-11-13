package com.smapley.prints2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lvrenyang.io.Pos;
import com.smapley.prints2.R;
import com.smapley.prints2.adapter.DetailAdapter;
import com.smapley.prints2.adapter.DetailAdapter2;
import com.smapley.prints2.listview.SwipeMenu;
import com.smapley.prints2.listview.SwipeMenuCreator;
import com.smapley.prints2.listview.SwipeMenuItem;
import com.smapley.prints2.listview.SwipeMenuListView;
import com.smapley.prints2.util.HttpUtils;
import com.smapley.prints2.util.MyData;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hao on 2015/11/8.
 */
@ContentView(R.layout.detail)
public class Detail extends Activity {

    private static final int PRINT = 5;
    private static final int GETDATA3 = 3;
    private static final int ERROR = 7;
    private static final int DELECTS = 8;
    private SwipeMenuListView listView;

    private TextView re_print;

    private DetailAdapter adapter1;
    private DetailAdapter2 adapter2;

    private final int GETDATA1 = 1;
    private final int GETDATA2 = 2;
    private final int UPDATA = 4;

    private static List<Map<String, String>> list1;
    private static List<Map<String, String>> list1_now = new ArrayList<>();
    private List<Map<String, String>> list2;
    private List<Map<String, String>> list2_now = new ArrayList<>();


    private TextView item1;
    private TextView item2;
    private TextView item3;

    private TextView page_up;
    private TextView page_num;
    private TextView page_down;

    private int now_item = 1;
    private int page_num1 = 1;
    private int page_num2 = 1;

    private LinearLayout layout1;
    private LinearLayout layout2;

    public static Dialog dialog;
    private Map map;
    private String allidString;
    private ListView listView3;
    private SimpleAdapter adapter3;

    private String qishu = "";

    private static CheckBox checkBox;
    private LinearLayout lin2;
    private LinearLayout lin2s;

    private TextView delect;

    private Detail mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        x.view().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_item1);
        builder.setMessage(R.string.dialog_item2);
        dialog = builder.create();

        initView();
        getData(GETDATA1);
        getData(GETDATA2);
        getData(GETDATA3);
    }

    private void upData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("提示：");
        builder.setMessage("是否重新打印上一张？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                dialogs.dismiss();
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap map = new HashMap();
                        map.put("user1", MyData.UserName);
                        mhandler.obtainMessage(UPDATA, HttpUtils.updata(map, MyData.getUrlGetjilu2())).sendToTarget();
                    }
                }).start();
            }
        });
        builder.create().show();


    }


    public static void check(boolean b) {
        if (b) {
            for (Map map : list1_now) {
                if (!map.get("zt").equals("已退码")) {
                    map.put("check", "1");
                }
            }
        } else {
            for (Map map : list1_now) {
                map.put("check", "0");
            }
        }
    }


    private void initView() {

        adapter1=new DetailAdapter(this,list1_now);
        adapter2=new DetailAdapter2(this,list2_now);

        checkBox = (CheckBox) findViewById(R.id.details_check);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        lin2s = (LinearLayout) findViewById(R.id.lin2s);
        delect = (TextView) findViewById(R.id.detail_delect);

        delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                builder.setTitle("提示：");
                builder.setMessage("是否批量删除？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                HashMap map = new HashMap();
                                List<Map> datalist = new ArrayList<Map>();
                                for (Map map1 : list1_now) {
                                    if (!map1.get("zt").equals("已退码") && map1.get("check").equals("1")) {
                                        datalist.add(map1);
                                    }
                                }
                                if (datalist.isEmpty()) {
                                    mhandler.obtainMessage(ERROR).sendToTarget();
                                } else {
                                    String data = "";
                                    for (Map map1 : datalist) {
                                        data = data + map1.get("id").toString() + "," + map1.get("biaoshi").toString() + ",";
                                    }
                                    map.put("tuima", data.substring(0, data.length() - 1));
                                    map.put("user1", MyData.UserName);
                                    map.put("mi", MyData.PassWord);
                                    mhandler.obtainMessage(DELECTS, HttpUtils.updata(map, MyData.getUrlTuima())).sendToTarget();
                                }

                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check(b);
                adapter1.notifyDataSetChanged();
            }
        });

        re_print = (TextView) findViewById(R.id.re_print);
        re_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mainActivity.mPos.GetIO().IsOpened()) {
                    upData();
                } else {
                    Toast.makeText(Detail.this, "未连接到打印机！", Toast.LENGTH_SHORT).show();

                }

            }
        });

        listView3 = (ListView) findViewById(R.id.detail_list3);
        adapter3 = new SimpleAdapter(Detail.this, list3, R.layout.detail_zhangdan_item
                , new String[]{"qishu", "zjine", "zhuishui", "zzhongjiang", "yingkui1"}
                , new int[]{R.id.zhangdan_tv_item0, R.id.zhangdan_tv_item1, R.id.zhangdan_tv_item2, R.id.zhangdan_tv_item3, R.id.zhangdan_tv_item4});
        listView3.setAdapter(adapter3);

        listView = (SwipeMenuListView) findViewById(R.id.detail_list);
        listView.setDivider(null);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(Detail.this);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                Map map = null;
                if (now_item == 1) {
                    map = list1_now.get(position);
                    final String id = map.get("id").toString();
                    final String biaoshi = map.get("biaoshi").toString();
                    switch (index) {
                        case 0:
                            AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                            builder.setTitle("提示：");
                            builder.setMessage("确定退码？");
                            builder.setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HashMap map = new HashMap();
                                            map.put("tuima", id + "," + biaoshi);
                                            map.put("user1", MyData.UserName);
                                            map.put("mi", MyData.PassWord);
                                            mhandler.obtainMessage(DELECTS, HttpUtils.updata(map, MyData.getUrlTuima())).sendToTarget();
                                        }
                                    }).start();
                                }
                            });
                            builder.create().show();


                            break;
                    }
                }


                return false;
            }
        });

        item1 = (TextView) findViewById(R.id.detail_item1);
        item2 = (TextView) findViewById(R.id.detail_item2);
        item3 = (TextView) findViewById(R.id.detail_item3);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        page_down = (TextView) findViewById(R.id.page_down);
        page_num = (TextView) findViewById(R.id.page_num);
        page_up = (TextView) findViewById(R.id.page_up);
        page_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(true);
                checkBox.setChecked(false);
                if (now_item == 1) {
                    if (page_num1 * PageSize < list1.size()) {
                        if (page_num1 * PageSize + PageSize < list1.size()) {
                            list1_now.clear();
                            list1_now.addAll(list1.subList(page_num1 * PageSize, page_num1 * PageSize + PageSize));
                        } else {
                            list1_now.clear();
                            list1_now.addAll(list1.subList(page_num1 * PageSize, list1.size()));
                        }
                        page_num1++;
                        page_num.setText(page_num1 + "");
                        adapter1.notifyDataSetChanged();
                        listView.setAdapter(adapter1);
                    }
                } else if (now_item == 2) {
                    if (page_num2 * PageSize < list2.size()) {
                        if (page_num2 * PageSize + PageSize < list2.size()) {
                            list2_now.clear();
                            list2_now.addAll(list2.subList(page_num2 * PageSize, page_num2 * PageSize + PageSize));
                        } else {
                            list2_now.clear();
                            list2_now.addAll(list2.subList(page_num2 * PageSize, list2.size()));
                        }
                        page_num2++;
                        page_num.setText(page_num2 + "");
                        adapter2.notifyDataSetChanged();
                        listView.setAdapter(adapter2);
                    }
                }
            }
        });

        page_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(true);
                checkBox.setChecked(false);
                if (now_item == 1) {
                    if (page_num1 > 1) {
                        page_num1--;
                        list1_now.clear();
                        list1_now.addAll(list1.subList(page_num1 * PageSize - PageSize, page_num1 * PageSize));
                        page_num.setText(page_num1 + "");
                        adapter1.notifyDataSetChanged();
                        listView.setAdapter(adapter1);
                    }
                } else if (now_item == 2) {
                    if (page_num2 > 1) {
                        page_num2--;
                        list2_now.clear();
                        list2_now.addAll(list2.subList(page_num2 * PageSize - PageSize, page_num2 * PageSize));
                        page_num.setText(page_num2 + "");
                        adapter2.notifyDataSetChanged();
                        listView.setAdapter(adapter2);
                    }
                }
            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now_item = 1;
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                lin2.setVisibility(View.VISIBLE);
                lin2s.setVisibility(View.GONE);
                listView.setAdapter(adapter1);
                item1.setTextColor(getResources().getColor(R.color.blue));
                item2.setTextColor(getResources().getColor(R.color.black));
                item3.setTextColor(getResources().getColor(R.color.black));
                page_num.setText(page_num1 + "");
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now_item = 2;
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                lin2.setVisibility(View.GONE);
                lin2s.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter2);
                item1.setTextColor(getResources().getColor(R.color.black));
                item2.setTextColor(getResources().getColor(R.color.blue));
                item3.setTextColor(getResources().getColor(R.color.black));
                page_num.setText(page_num2 + "");
            }
        });
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now_item = 3;
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);
                item1.setTextColor(getResources().getColor(R.color.black));
                item2.setTextColor(getResources().getColor(R.color.black));
                item3.setTextColor(getResources().getColor(R.color.blue));
            }
        });

    }

    private void getData(final int num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("user1", MyData.UserName);
                String url = null;
                if (num == GETDATA1) {
                    url = MyData.getUrlGetmingxi();
                } else if (num == GETDATA2) {
                    url = MyData.getUrlGetjiang();
                } else if (num == GETDATA3) {
                    url = MyData.getUrlGetzhangdan();
                }
                mhandler.obtainMessage(num, HttpUtils.updata(map, url)).sendToTarget();
            }
        }).start();

    }

    private int PageSize = 40;
    private List<Map<String, Object>> list3 = new ArrayList<>();
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case GETDATA1:
                        list1 = JSON.parseObject(msg.obj.toString(), new TypeReference<List<Map<String, String>>>() {
                        });
                        for (int i = 0; i < list1.size(); i++) {
                            list1.get(i).put("check", "0");
                            switch (Integer.parseInt(list1.get(i).get("zt").toString())) {
                                case 0:
                                    list1.get(i).put("zt", "未打印");

                                    break;
                                case 1:
                                    list1.get(i).put("zt", "已打印");
                                    break;
                                case 9:
                                    list1.get(i).put("zt", "已退码");
                                    break;
                            }
                        }
                        if (list1.size() > PageSize) {
                            list1_now.clear();
                            list1_now.addAll(list1.subList(0, PageSize));
                        } else {
                            list1_now.clear();
                            list1_now.addAll(list1);
                        }
                        adapter1.notifyDataSetChanged();
                        listView.setAdapter(adapter1);

                        break;
                    case GETDATA2:
                        list2 = JSON.parseObject(msg.obj.toString(), new TypeReference<List<Map<String, String>>>() {
                        });

                        if (list2.size() > PageSize) {
                            list2_now.clear();
                            list2_now.addAll(list2.subList(0, PageSize));
                        } else {
                            list2_now.clear();
                            list2_now.addAll(list2);
                        }
                        adapter2.notifyDataSetChanged();

                        break;

                    case GETDATA3:
                        list3.clear();
                        list3.addAll(JSON.parseObject(msg.obj.toString(), new TypeReference<List<Map<String, Object>>>() {
                        }));
                        Log.e("result", msg.obj.toString());

                        adapter3.notifyDataSetChanged();
                        break;


                    case UPDATA:
                        dialog.dismiss();

                        map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (Integer.parseInt(map.get("count").toString()) > 0) {
                            qishu = map.get("qishu").toString();
                            mhandler.obtainMessage(PRINT).sendToTarget();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                            builder.setTitle(R.string.dialog_item1);
                            builder.setMessage(R.string.dialog_item3);
                            builder.setPositiveButton(R.string.dialog_item7, null);
                            dialog = builder.create();
                            dialog.show();
                        }
                        break;

                    case PRINT:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
                        builder.setTitle(R.string.dialog_item1);
                        builder.setMessage(R.string.dialog_item4);
                        dialog = builder.create();
                        dialog.show();
                        MainActivity.mainActivity.es.submit(new TaskPrint( MainActivity.mainActivity.mPos,map));
                        break;
                    case DELECTS:
                        checkBox.setChecked(true);
                        checkBox.setChecked(false);
                        String result2 = JSON.parseObject(msg.obj.toString(), new TypeReference<String>() {
                        });
                        getData(GETDATA1);
                        Toast.makeText(Detail.this, result2, Toast.LENGTH_SHORT).show();

                        break;
                    case ERROR:
                        checkBox.setChecked(true);
                        checkBox.setChecked(false);
                        adapter1.notifyDataSetChanged();
                        Toast.makeText(Detail.this, "没有码可以退！", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Detail.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.mainActivity.print.getData();
    }

    public class TaskPrint implements Runnable {
        Pos pos = null;
        Map map =null;
        public TaskPrint(Pos pos, Map map ) {
            this.pos = pos;
            this.map = map;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final boolean bPrintResult = PrintTicket(map);

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    Toast.makeText(mActivity.getApplicationContext(), bPrintResult ? "打印成功" : "打印失败", Toast.LENGTH_SHORT).show();
                }
            });

        }

        public boolean PrintTicket(Map map) {
            allidString = map.get("allid").toString();
            String allid = "编号：" + allidString;
            String riqi = "日期：" + map.get("riqi").toString();
            String name = "会员：" + map.get("ming").toString();
            String qihao =  qishu ;
            String allnum = " 笔数 " + map.get("count") + "  总金额 " + map.get("allgold") + "元";
            String lin2 = "————————————————————————————————";
            String dataString = map.get("beizhu").toString();
            List<Map<String, String>> list = JSON.parseObject(map.get("result").toString(), new TypeReference<List<Map<String, String>>>() {
            });
            pos.POS_S_Align(0);
            pos.POS_SetLineHeight(35);
            MainActivity.mainActivity.printText(pos, riqi);
            MainActivity.mainActivity. printText(pos, name);
            MainActivity.mainActivity.printText(pos, allid);
            MainActivity.mainActivity.printText(pos, dataString);
            MainActivity.mainActivity.printText(pos, lin2);
            pos.POS_SetLineHeight(40);
            MainActivity.mainActivity.printLable(pos, new String[]{"号码","金额","一元赔率"},false);
            for (int i = 0; i < list.size(); i++) {
                MainActivity.mainActivity.printLable(pos, new String[]{list.get(i).get("number").toString(),list.get(i).get("gold").toString(),list.get(i).get("pei").toString()});
            }
            pos.POS_FeedLine();
            pos.POS_SetLineHeight(35);
            MainActivity.mainActivity.printText(pos, allnum);
            MainActivity.mainActivity.printText(pos, lin2);
            pos.POS_S_Align(1);
            MainActivity.mainActivity. printText(pos, qihao);
            pos.POS_FeedLine();
            pos.POS_FeedLine();
            pos.POS_Beep(1, 5);
            return pos.GetIO().IsOpened();
        }
    }
}
