package moe.chionlab.wechatmomentstat.gui;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import moe.chionlab.wechatmomentstat.Model.Fm1Adapter;
import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager12;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class ThirdFragment extends Fragment implements View.OnClickListener {

    private Button selectallBT,selectnonBT,yesBT,cancleBT;
    ImageButton plusBT;

    private ListView listView;


    private Fm1Adapter mAdapter;
    LinearLayout selectLL,bottomLL;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_3, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        initView(rootView);

        


        mAdapter = new Fm1Adapter(getContext(),Share.fm3addedList);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mAdapter.flag!=true)
                {
                    mAdapter.flag = true;
                    selectLL.setVisibility(View.VISIBLE);
                    bottomLL.setVisibility(View.VISIBLE);
                    plusBT.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        //写内容

        return rootView;
    }
    private void initView(View view)
    {
        selectallBT = (Button) view.findViewById(R.id.bt_fm3_selectall);
        selectnonBT = (Button) view.findViewById(R.id.bt_fm3_selectnon);
        yesBT = (Button) view.findViewById(R.id.bt_fm3_yes);
        cancleBT = (Button) view.findViewById(R.id.bt_fm3_cancle);
        listView = (ListView) view.findViewById(R.id.lv_fm3);
        selectLL = (LinearLayout) view.findViewById(R.id.ll_fm3_select);
        bottomLL = (LinearLayout) view.findViewById(R.id.ll_fm3_bottom);
        plusBT = (ImageButton) view.findViewById(R.id.bt_fm3_plus);

        selectallBT.setOnClickListener(this);
        selectnonBT.setOnClickListener(this);
        yesBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);
        plusBT.setOnClickListener(this);

        yesBT.setText("删除");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_fm3_cancle:
                mAdapter.flag = false;
                selectLL.setVisibility(View.GONE);
                bottomLL.setVisibility(View.GONE);
                plusBT.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_fm3_plus:
                Intent intent = new Intent(getActivity(),Fm3AddActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.bt_fm3_selectall:
                for (int i = 0; i < Share.fm3addedList.size(); i++) {
                    Share.fm3addedList.get(i).setIscheck(true);
                }
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm3_selectnon:
                for (int i = 0; i < Share.fm3addedList.size(); i++) {
                    if (Share.fm3addedList.get(i).getIscheck()) {
                        Share.fm3addedList.get(i).setIscheck(false);
                    } else {
                        Share.fm3addedList.get(i).setIscheck(true);
                    }
                }
                break;
            case R.id.bt_fm3_yes:
                ProgressBarCycle.setProgressBar(getContext(),"正在删除群组，请稍候...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Manager12 manager12 = new Manager12(handler,getContext());
                        List<String> grouplist = new ArrayList<String>();
                        for(int i=0;i<Share.fm3addedList.size();i++)
                        {
                            Fm1Itembean fm1Itembean = Share.fm3addedList.get(i);
                            if(fm1Itembean.getIscheck())
                            {
                                grouplist.add(fm1Itembean.getId());
                            }
                        }
                        if(grouplist.size()!=0)
                        {
                            manager12.upload(grouplist);
                        }
                    }
                }).start();

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1)
        {
            if(resultCode ==1)
            {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 12:
                    if(msg.obj.toString().equals("1"))
                    {
                        for(int i=0;i<Share.fm3addedList.size();i++)
                        {
                            Fm1Itembean fm1Itembean = Share.fm3addedList.get(i);
                            if(fm1Itembean.getIscheck())
                            {
                                Share.fm3allList.add(fm1Itembean);
                                Share.fm3addedList.remove(i);
                                i--;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        ProgressBarCycle.cancleProgressBar();
                        Toast.makeText(getContext(), "删除群组成功", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ProgressBarCycle.cancleProgressBar();
                        Toast.makeText(getContext(), "删除群组失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
