package moe.chionlab.wechatmomentstat.gui;


import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import moe.chionlab.wechatmomentstat.Model.Fm1Adapter;
import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.R;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class ThirdFragment extends Fragment implements View.OnClickListener {

    private Button selectallBT,selectnonBT,yesBT,cancleBT;
    ImageButton plusBT;

    private ListView listView;

    private List<Fm1Itembean> itembeanList;
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

        itembeanList = new ArrayList<Fm1Itembean>();

        for(int i=0;i<20;i++)
        {
            itembeanList.add(new Fm1Itembean("群组"+(i+32)));
        }
        mAdapter = new Fm1Adapter(getContext(),itembeanList);
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
                startActivity(intent);
                break;
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
        }
    }
}
