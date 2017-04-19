package moe.chionlab.wechatmomentstat.gui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.Model.Fm2Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager10;
import moe.chionlab.wechatmomentstat.Model.Manager21;
import moe.chionlab.wechatmomentstat.Model.Manager31;
import moe.chionlab.wechatmomentstat.Model.ReadDatabase;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.Task;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class MainActivity extends FragmentActivity {
    private int count=0;private long exitTime = 0;

    private boolean nonetwork = false;
    Task task = null;
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {FirstFragment.class,SecondFragment.class,ThirdFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn,R.drawable.tab_home_btn,R.drawable.tab_home_btn};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"上传记录","关键词管理", "自动上传"};



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = new Task(this.getApplicationContext());
        task.testRoot();





        ProgressBarCycle.setProgressBar(MainActivity.this,"正在初始化数据，因为数据库解密比较费时，请耐心等待...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Manager31 manager31 = new Manager31(MainActivity.this,new Handler());
                ReadDatabase readDatabase = new ReadDatabase(MainActivity.this);
                Log.d("FirstFragment", "正在读取数据：");
                ArrayList<Map<String,Object>> groupslist = manager31.getLocalGroups(readDatabase.readDatabaseText());
                List<Fm1Itembean> itembeanList = new ArrayList<Fm1Itembean>();
                if(groupslist!=null) {
                    for (int i = 0; i < groupslist.size(); i++) {
                        Fm1Itembean fm1Itembean = new Fm1Itembean();
                        Map groupmap = groupslist.get(i);
                        fm1Itembean.setId(groupmap.get("group").toString());
                        fm1Itembean.setTitle(groupmap.get("name").toString());
                        fm1Itembean.setIscheck(false);
                        itembeanList.add(fm1Itembean);

                    }
                }
                Share.fm1ItembeanList=itembeanList;
                Share.fm3allList=itembeanList;
                handler.sendEmptyMessage(31);
                Looper.loop();

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                Manager21 manager21 = new Manager21(MainActivity.this,handler);
                manager21.upload();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager10 manager10 = new Manager10(MainActivity.this,handler);
                manager10.upload();
            }
        }).start();


    }





    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 31:
                    count++;
                   // Log.d("MainActivity"+msg.what, "count:" + count);
                    break;
                case 21:
                    if(msg.obj!=null)
                    {
                        List<Map<String,Object>> keywordlist = (List<Map<String, Object>>) msg.obj;
                        List<Fm2Itembean> fm2ItembeanList = new ArrayList<>();
                        for(int i = 0 ;i<keywordlist.size();i++)
                        {
                            Map<String,Object> keywordmap = keywordlist.get(i);
                            Fm2Itembean fm2Itembean = new Fm2Itembean(keywordmap.get("keyword").toString(),Integer.parseInt(keywordmap.get("weight").toString()));
                            fm2ItembeanList.add(fm2Itembean);

                        }
                        Share.fm2ItembeanList = fm2ItembeanList;
                    }
                    else
                        nonetwork =true;
                    count++;
                   // Log.d("MainActivity", "count:" + count);
                    break;
                case 10:
                   // Log.d("MainActivity", msg.what + "," + msg.obj.toString());
                    if(msg.obj.toString().equals("10"))
                        count++;
                    else
                    {
                        nonetwork = true;
                        count++;

                    }
                    Log.d("MainActivity", "count:" + count);
                    break;



            }



            if(count==3)
            {
                if(nonetwork == true)
                {
                    Toast.makeText(MainActivity.this, "网络不畅通，请检查网络设置", Toast.LENGTH_SHORT).show();
                }
                ProgressBarCycle.cancleProgressBar();
                initView();
            }

        }
    };

    /**
     * 初始化组件
     */
    private void initView(){
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkReadPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR);
//            int checkWritePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR);
//            if (checkReadPermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
//            }
//            if (checkWritePermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALENDAR}, 2);
//            }
//        }

        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

        mTabHost.setCurrentTab(0);

    }






    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次程序将被后台", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

