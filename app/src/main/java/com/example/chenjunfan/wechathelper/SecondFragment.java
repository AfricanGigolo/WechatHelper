package com.example.chenjunfan.wechathelper;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import function.Fm1Itembean;
import function.Fm2Adapter;
import function.Fm2Itembean;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class SecondFragment extends Fragment implements View.OnClickListener {
    int REQUEST_CODE = 1;
    int RESULT_OK = 1;
    int RESULT_NO = 0;

    private View rootView;

    private Button selectallBT,selectnonBT,deleteBT,cancleBT;
    private ImageButton plusBT;

    private ListView listView;
    RelativeLayout selectRL;
    LinearLayout bottomLL;

    private List<Fm2Itembean> itembeanList;
    private Fm2Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_2, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);
        itembeanList = new ArrayList<>();
        for(int i=0;i<20;i++)
        {
            itembeanList.add(new Fm2Itembean("关键词"+(i+1),i+1));
        }

        mAdapter = new Fm2Adapter(getContext(),itembeanList);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(mAdapter!=null)
               {
                   if (mAdapter.flag == false)
                   {
                       mAdapter.flag = true;
                       plusBT.setVisibility(View.GONE);
                       selectRL.setVisibility(View.VISIBLE);
                       bottomLL.setVisibility(View.VISIBLE);
                       mAdapter.notifyDataSetChanged();
                   }
               }
               return true;
           }
       });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),NewkeywordActivity.class);
                intent.putExtra("title","编辑关键词");
                startActivity(intent);
            }
        });

        //写内容

        return rootView;
    }
    private void initView(View view)
    {
        selectallBT = (Button) view.findViewById(R.id.bt_fm2_selectall);
        selectnonBT = (Button) view.findViewById(R.id.bt_fm2_selectnon);
        deleteBT = (Button) view.findViewById(R.id.bt_fm2_delete);
        cancleBT = (Button) view.findViewById(R.id.bt_fm2_cancle);
        plusBT = (ImageButton) view.findViewById(R.id.bt_fm2_plus);
        listView = (ListView) view.findViewById(R.id.lv_fm2);
        bottomLL = (LinearLayout) view.findViewById(R.id.ll_fm2_bottom);
        selectRL = (RelativeLayout) view.findViewById(R.id.rl_fm2_select);

        selectallBT.setOnClickListener(this);
        selectnonBT.setOnClickListener(this);
        deleteBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);
        plusBT.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_fm2_cancle:
                selectRL.setVisibility(View.GONE);
                bottomLL.setVisibility(View.GONE);
                plusBT.setVisibility(View.VISIBLE);
                mAdapter.flag =false;
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_fm2_selectall:
                for (int i = 0; i < itembeanList.size(); i++) {
                    itembeanList.get(i).setIscheck(true);
                }
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm2_selectnon:
                for (int i = 0; i < itembeanList.size(); i++) {
                    if (itembeanList.get(i).getIscheck()) {
                        itembeanList.get(i).setIscheck(false);
                    } else {
                        itembeanList.get(i).setIscheck(true);
                    }
                }

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_fm2_plus:
                Intent intent = new Intent(getActivity(),NewkeywordActivity.class);
                intent.putExtra("title","添加关键词");
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.bt_fm2_delete:
                for(int i=0;i<itembeanList.size();i++)
                {
                    if (itembeanList.get(i).getIscheck())
                    {
                        Log.d("SecondFragment", "i:" + i);
                        itembeanList.remove(i);
                        i--;
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Fm2Itembean itembean = data.getParcelableExtra("return");
                itembeanList.add(0,itembean);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


}
