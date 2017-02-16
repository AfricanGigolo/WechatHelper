package com.example.chenjunfan.wechathelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import function.Fm1Adapter;
import function.Fm1Itembean;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class FirstFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button selectallBT,selectnonBT,yesBT,cancleBT;

    private ListView listView;

    private List<Fm1Itembean> itembeanList;
    private Fm1Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_1, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);

        //写内容
        itembeanList = new ArrayList<Fm1Itembean>();

        for(int i=0;i<20;i++)
        {
            itembeanList.add(new Fm1Itembean("群组"+(i+1)));
        }
        mAdapter = new Fm1Adapter(getContext(),itembeanList);
        listView.setAdapter(mAdapter);

        return rootView;
    }
    private void initView(View view)
    {
        selectallBT = (Button) view.findViewById(R.id.bt_fm1_selectall);
        selectnonBT = (Button) view.findViewById(R.id.bt_fm1_selectnon);
        //reBT = (ImageButton) view.findViewById(R.id.bt_fm1_re);
        yesBT = (Button) view.findViewById(R.id.bt_fm1_yes);
        cancleBT = (Button) view.findViewById(R.id.bt_fm1_cancle);
        listView = (ListView) view.findViewById(R.id.lv_fm1);

        selectnonBT.setOnClickListener(this);
        selectallBT.setOnClickListener(this);
       // reBT.setOnClickListener(this);
        yesBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_fm1_selectall:
                for (int i = 0; i < itembeanList.size(); i++) {
                    itembeanList.get(i).setIscheck(true);
                }
                    mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm1_selectnon:
                for (int i = 0; i < itembeanList.size(); i++) {
                    if (itembeanList.get(i).getIscheck()) {
                        itembeanList.get(i).setIscheck(false);
                    } else {
                        itembeanList.get(i).setIscheck(true);
                    }
                }

                mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm1_yes:
                break;

            case R.id.bt_fm1_cancle:
                break;
        }

    }
}
