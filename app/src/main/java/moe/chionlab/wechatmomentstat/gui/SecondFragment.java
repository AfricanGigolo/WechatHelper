package moe.chionlab.wechatmomentstat.gui;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import moe.chionlab.wechatmomentstat.Model.Fm2Adapter;
import moe.chionlab.wechatmomentstat.Model.Fm2Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager24;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class SecondFragment extends Fragment implements View.OnClickListener {
    int REQUEST_CODE = 1;
    int RESULT_OK = 1;
    int RESULT_NO = 0;
    int REFRESH = 2;

    private View rootView;

    private Button selectallBT,selectnonBT,deleteBT,cancleBT;
    private ImageButton plusBT;

    private ListView listView;
    RelativeLayout selectRL;
    LinearLayout bottomLL;
    
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


        mAdapter = new Fm2Adapter(getContext(),Share.fm2ItembeanList);
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
                intent.putExtra("type","edit");
                intent.putExtra("No",i);
                startActivityForResult(intent,REQUEST_CODE);
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
                for (int i = 0; i < Share.fm2ItembeanList.size(); i++) {
                    Share.fm2ItembeanList.get(i).setIscheck(true);
                }
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm2_selectnon:
                for (int i = 0; i < Share.fm2ItembeanList.size(); i++) {
                    if (Share.fm2ItembeanList.get(i).getIscheck()) {
                        Share.fm2ItembeanList.get(i).setIscheck(false);
                    } else {
                        Share.fm2ItembeanList.get(i).setIscheck(true);
                    }
                }

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_fm2_plus:
                Intent intent = new Intent(getActivity(),NewkeywordActivity.class);
                intent.putExtra("title","添加关键词");
                intent.putExtra("type","add");
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.bt_fm2_delete:
                final List<String> checkedList = new ArrayList<>();
                for(int i=0;i<Share.fm2ItembeanList.size();i++)
                {
                    if(Share.fm2ItembeanList.get(i).getIscheck())
                    {
                        checkedList.add(Share.fm2ItembeanList.get(i).getTitle());
                    }
                }
                if(checkedList.size()!=0)
                {
                    ProgressBarCycle.setProgressBar(getContext(),"正在删除关键词...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Manager24 manager24 = new Manager24(getContext(),handler);
                            manager24.upload(checkedList);
                        }
                    }).start();

                }


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
                Share.fm2ItembeanList.add(itembean);
                mAdapter.notifyDataSetChanged();

            }
            else if(resultCode == REFRESH)
            {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 24:
                    if(msg.obj.toString().equals("1"))
                    {
                        for(int i=0;i<Share.fm2ItembeanList.size();i++)
                        {
                            if (Share.fm2ItembeanList.get(i).getIscheck())
                            {
                                Log.d("SecondFragment", "i:" + i);
                                Share.fm2ItembeanList.remove(i);
                                i--;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "删除关键词成功", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "删除关键词失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                    ProgressBarCycle.cancleProgressBar();
                    break;
            }
        }
    };


}
