package com.smapley.prints2.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Pos;
import com.smapley.prints2.R;
import com.smapley.prints2.adapter.Main_Viewpage_Adapter;
import com.smapley.prints2.fragment.Chose;
import com.smapley.prints2.fragment.Data;
import com.smapley.prints2.fragment.Print;
import com.smapley.prints2.fragment.Set;
import com.smapley.prints2.util.CustomViewPager;
import com.smapley.prints2.util.HttpUtils;
import com.smapley.prints2.util.MyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends FragmentActivity implements View.OnClickListener, IOCallBack {

    private TextView bottom_item1;
    private TextView bottom_item2;
    private TextView bottom_item3;
    private TextView bottom_item4;

    private CustomViewPager viewPager;
    public View bottom;

    private Main_Viewpage_Adapter pageViewAdapter;
    public Print print;
    public Set set;
    private Data data;
    private Chose chose;
    private List<Fragment> fragmentList;


    private static String TAG = "MainActivity";
    private final int UPDATA = 2;
    private final int UPDATA2 = 1;
    private final int UPDATA3 = 5;
    private final int CONNECTBT = 10;
    private final int CANUPDATA = 11;
    private static final int PRINT = 3;
    public static Dialog dialog;
    private Map map;
    private String allidString;
    private SharedPreferences sp_user;

    private static String title2;
    public static int position = 1;
    public static MainActivity mainActivity;

    ExecutorService es = Executors.newScheduledThreadPool(30);

    Pos mPos = new Pos();
    BTPrinting mBt = new BTPrinting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        position = 1;

        sp_user = getSharedPreferences("user", MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_item1);
        builder.setMessage(R.string.dialog_item2);
        dialog = builder.create();

        initView();
        initViewPage();

        mainActivity = this;

        mPos.Set(mBt);
        mBt.SetCallBack(this);
        initPrint();
    }

    private void initView() {

        bottom_item1 = (TextView) findViewById(R.id.bottom_item1);
        bottom_item2 = (TextView) findViewById(R.id.bottom_item2);
        bottom_item3 = (TextView) findViewById(R.id.bottom_item3);
        bottom_item4 = (TextView) findViewById(R.id.bottom_item4);

        viewPager = (CustomViewPager) findViewById(R.id.fragment);
        bottom = findViewById(R.id.main_bottom);


        bottom_item1.setOnClickListener(this);
        bottom_item2.setOnClickListener(this);
        bottom_item3.setOnClickListener(this);
        bottom_item4.setOnClickListener(this);

    }

    private void initViewPage() {
        fragmentList = new ArrayList<>();
        set = new Set();
        chose = new Chose();
        data = new Data();
        print = new Print();
        fragmentList.add(print);
        fragmentList.add(chose);
        fragmentList.add(data);
        fragmentList.add(set);
        pageViewAdapter = new Main_Viewpage_Adapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pageViewAdapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_item1:
                viewPagerGo(0);
                position = 1;
                break;
            case R.id.bottom_item2:
                viewPagerGo(1);
                position = 2;
                break;
            case R.id.bottom_item3:
                viewPagerGo(2);
                position = 3;
                break;
            case R.id.bottom_item4:
                viewPagerGo(3);
                position = 4;
                break;
        }
    }

    public void viewPagerGo(int num) {
        switch (num) {
            case 0:
                print.getData();
                break;

            case 2:

                break;

            case 1:
                chose.clearn();
                chose.settitle(title2);
                break;
            case 3:
                set.settitle(title2);
                break;
        }

        viewPager.setCurrentItem(num);
    }

    private void changeTitle(String title2) {
        this.title2 = title2;
        chose.settitle(title2);
        set.settitle(title2);
    }

    private void initPrint() {
        // 初始化字符串资源
        if (!mBt.IsOpened()) {
            changeTitle(getString(R.string.print0));
            connectBT();
        } else {
            changeTitle(getString(R.string.print1));
        }
    }

    /**
     * 连接打印机
     */
    public void connectBT() {
        try {
            String address = sp_user.getString("address", "");
            if (address != null && !address.isEmpty()) {
                es.submit(new TaskOpen(mBt, address, mainActivity));
                Toast.makeText(MainActivity.this, "正在连接打印机", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "请先手动连接一次打印机！", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "-------异常----------", Toast.LENGTH_SHORT).show();
        }
    }


    public class TaskOpen implements Runnable {
        BTPrinting bt = null;
        String address = null;
        Context context = null;

        public TaskOpen(BTPrinting bt, String address, Context context) {
            this.bt = bt;
            this.address = address;
            this.context = context;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            bt.Open(address, context);
        }
    }


    private void upData2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = new HashMap();
                map.put("allid", allidString);
                map.put("user1", MyData.UserName);
                mhandler.obtainMessage(UPDATA3, HttpUtils.updata(map, MyData.getURL_updateZt1())).sendToTarget();
            }
        }).start();
    }


    private void upData(boolean isData, final String url) {
        if (isData) {
            dialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap map = new HashMap();
                    map.put("user1", MyData.UserName);
                    mhandler.obtainMessage(UPDATA, HttpUtils.updata(map, url)).sendToTarget();
                }
            }).start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示：");
            builder.setMessage("没有可打印信息！");
            builder.setPositiveButton("确定", null);
            builder.create().show();
        }
    }

    private String qishu = "";
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.dialog_item1);
            try {
                switch (msg.what) {
                    case CANUPDATA:
                        if(mPos.GetIO().IsOpened()) {
                            if (print.dataList.size() > 1) {
                                upData(true, MyData.getUrlDayin());
                            } else {
                                upData(false, MyData.getUrlDayin());
                            }
                        }else{
                            Toast.makeText(mainActivity,"打印机未连接,请连接打印机后重试!",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case CONNECTBT:
                        connectBT();
                        break;
                    case UPDATA3:
                        Map resultmap = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (Integer.parseInt(resultmap.get("newid").toString()) > 0) {
                            print.getData();
                        }
                        break;

                    case UPDATA2:
                        dialog.dismiss();
                        Map result = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (Integer.parseInt(result.get("newid").toString()) > 0) {
                            Toast.makeText(MainActivity.this, "更新数据成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "没有数据需要更新！", Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case UPDATA:
                        dialog.dismiss();
                        map = JSON.parseObject(msg.obj.toString(), new TypeReference<Map>() {
                        });
                        if (Integer.parseInt(map.get("count").toString()) > 0) {
                            qishu = map.get("qishu").toString();
                            mhandler.obtainMessage(PRINT).sendToTarget();
                        } else {
                            builder.setMessage(R.string.dialog_item3);
                            builder.setPositiveButton(R.string.dialog_item7, null);
                            dialog = builder.create();
                            dialog.show();
                        }
                        break;

                    case PRINT:
                        builder.setMessage(R.string.dialog_item4);
                        dialog = builder.create();
                        dialog.show();
                        es.submit(new TaskPrint(mPos,map));
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    if(bPrintResult)
                        upData2();
                    Toast.makeText(mainActivity.getApplicationContext(), bPrintResult ? "打印成功" : "打印失败", Toast.LENGTH_SHORT).show();
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
            printText(pos, riqi);
            printText(pos, name);
            printText(pos, allid);
            printText(pos, dataString);
            printText(pos, lin2);
            pos.POS_SetLineHeight(40);
            printLable(pos, new String[]{"号码","金额","一元赔率"},false);
            for (int i = 0; i < list.size(); i++) {
                printLable(pos, new String[]{list.get(i).get("number").toString(),list.get(i).get("gold").toString(),list.get(i).get("pei").toString()});
            }
            pos.POS_FeedLine();
            pos.POS_SetLineHeight(35);
            printText(pos, allnum);
            printText(pos, lin2);
            pos.POS_S_Align(1);
            printText(pos, qihao);
            pos.POS_FeedLine();
            pos.POS_FeedLine();
            pos.POS_Beep(1, 5);
            return pos.GetIO().IsOpened();
        }
    }

    public void printLable(Pos pos, String[] text, boolean isNum){
        int nWidthTimes=0;
        if(isNum){
            nWidthTimes=1;
        }
        pos.POS_S_TextOut(text[0], 0, nWidthTimes, 0, 0, 0x00);
        pos.POS_S_TextOut(text[1], 140, nWidthTimes, 0, 0, 0x00);
        pos.POS_S_TextOut(text[2]+"\r\n", 256, nWidthTimes, 0, 0, 0x00);
    }

    public void printLable(Pos pos, String[] text) {
        printLable(pos,text,true);
    }

    public void printText(Pos pos, String text) {
        pos.POS_S_TextOut(text + "\r\n", 0, 0, 0, 0, 0x00);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        es.submit(new TaskClose(mBt));
    }

    public class TaskClose implements Runnable {
        BTPrinting bt = null;

        public TaskClose(BTPrinting bt) {
            this.bt = bt;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            bt.Close();
        }

    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                changeTitle(getString(R.string.print1));
                Toast.makeText(mainActivity,getString(R.string.print1),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                changeTitle(getString(R.string.print0));
                Toast.makeText(mainActivity,getString(R.string.print0),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                changeTitle(getString(R.string.print0));
                Toast.makeText(mainActivity,getString(R.string.print0),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            viewPagerGo(0);
            position = 1;
            print.getData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static void stop() {
        mainActivity.finish();
    }
}
