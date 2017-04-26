package moe.chionlab.wechatmomentstat.gui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.chionlab.wechatmomentstat.Model.Fm1Adapter;
import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager11;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/2/20.
 */

public class Fm3AddActivity extends Activity implements View.OnClickListener {
    ImageButton reBT;
    Button selectallBT,selectnonBT,yesBT,cancleBT;
    ListView listView;
    Fm1Adapter mAdapter;
    private LinearLayout selectLL,bottomLL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm3add);
        initView();

        mAdapter = new Fm1Adapter(Fm3AddActivity.this, Share.fm3allList);
        listView.setAdapter(mAdapter);
        mAdapter.flag=true;
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (mAdapter.flag!=true)
//                {
//                    mAdapter.flag = true;
//                    selectLL.setVisibility(View.VISIBLE);
//
//                    bottomLL.setVisibility(View.VISIBLE);
//                    mAdapter.notifyDataSetChanged();
//                }
//                return false;
//            }
//        });

    }

    private void initView()
    {
        reBT = (ImageButton) findViewById(R.id.bt_fm3add_re);
        selectallBT = (Button) findViewById(R.id.bt_fm3add_selectall);
        selectnonBT = (Button) findViewById(R.id.bt_fm3add_selectnon);
        yesBT = (Button) findViewById(R.id.bt_fm3add_yes);
        cancleBT = (Button) findViewById(R.id.bt_fm3add_cancle);
        listView = (ListView) findViewById(R.id.lv_fm3add);
        selectLL = (LinearLayout) findViewById(R.id.ll_fm3add_select);
        bottomLL = (LinearLayout) findViewById(R.id.ll_fm3add_bottom);

        reBT.setOnClickListener(this);
        selectnonBT.setOnClickListener(this);
        selectallBT.setOnClickListener(this);
        yesBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);

        for(int i = 0;i<Share.fm3addedList.size();i++)
        {
            for(int j=0;i<Share.fm3allList.size();j++)
            {
                if(Share.fm3addedList.get(i).getId().equals(Share.fm3allList.get(j).getId()))
                {
                    Share.fm3allList.remove(j);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_fm3add_re:
                finish();
                break;
            case R.id.bt_fm3add_selectall:
                for (int i = 0; i < Share.fm3allList.size(); i++) {
                    Share.fm3allList.get(i).setIscheck(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_fm3add_selectnon:
                for (int i = 0; i < Share.fm3allList.size(); i++) {
                    if (Share.fm3allList.get(i).getIscheck()) {
                        Share.fm3allList.get(i).setIscheck(false);
                    } else {
                        Share.fm3allList.get(i).setIscheck(true);
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_fm3add_yes:
                final List<Map<String,Object>> groupList = new ArrayList<>();
                for(int i=0;i<Share.fm3allList.size();i++)
                {
                    if(Share.fm3allList.get(i).getIscheck())
                    {
                        Map<String,Object> group = new HashMap<>();
                        group.put("group",Share.fm3allList.get(i).getId());
                        group.put("name",Share.fm3allList.get(i).getTitle());
                        groupList.add(group);
                    }
                }
                if(groupList.size()!=0)
                {
                    ProgressBarCycle.setProgressBar(Fm3AddActivity.this,"正在添加，请稍候...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Manager11 manager11 = new Manager11(Fm3AddActivity.this,handler);
                            manager11.upload(groupList);
                        }
                    }).start();

                }
                break;
            case R.id.bt_fm3add_cancle:
                finish();
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 11:
                    if(msg.obj.toString().equals("1"))
                    {
                        for(int i=0;i<Share.fm3allList.size();i++)
                        {
                            if(Share.fm3allList.get(i).getIscheck())
                            {
                                Share.fm3addedList.add(Share.fm3allList.get(i));
                                Share.fm3allList.remove(i);
                                i--;
                            }

                        }
                        Intent intent = new Intent();
                        setResult(1,intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Fm3AddActivity.this, "添加失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                    ProgressBarCycle.cancleProgressBar();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(int i=0;i<Share.fm3allList.size();i++)
        {
            Share.fm3allList.get(i).setIscheck(false);
        }
    }
}
