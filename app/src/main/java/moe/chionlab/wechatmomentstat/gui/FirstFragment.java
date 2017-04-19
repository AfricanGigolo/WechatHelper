package moe.chionlab.wechatmomentstat.gui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Map;


import moe.chionlab.wechatmomentstat.Model.Fm1Adapter;
import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager31;
import moe.chionlab.wechatmomentstat.Model.ReadDatabase;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/2/14.
 */

public class FirstFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button selectallBT,selectnonBT,yesBT,cancleBT;

    private ListView listView;


    private Fm1Adapter mAdapter;
    private LinearLayout selectLL,bottomLL;

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


        




        mAdapter = new Fm1Adapter(getContext(),Share.fm1ItembeanList);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mAdapter.flag!=true)
                {
                    mAdapter.flag = true;
                    selectLL.setVisibility(View.VISIBLE);

                    bottomLL.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
//        final Handler handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what)
//                {
//                    case 11:
//                        mAdapter.notifyDataSetChanged();
//                        ProgressBarCycle.cancleProgressBar();
//                        break;
//
//                }
//            }
//        };



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
        selectLL = (LinearLayout) view.findViewById(R.id.ll_fm1_select);
        bottomLL = (LinearLayout) view.findViewById(R.id.ll_fm1_bottom);

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
                for (int i = 0; i < Share.fm1ItembeanList.size(); i++) {
                    Share.fm1ItembeanList.get(i).setIscheck(true);
                }
                    mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm1_selectnon:
                for (int i = 0; i < Share.fm1ItembeanList.size(); i++) {
                    if (Share.fm1ItembeanList.get(i).getIscheck()) {
                        Share.fm1ItembeanList.get(i).setIscheck(false);
                    } else {
                        Share.fm1ItembeanList.get(i).setIscheck(true);
                    }
                }

                mAdapter.notifyDataSetChanged();
                break;

            case R.id.bt_fm1_yes:
                Intent intent = new Intent(getActivity(),Fm1UploadActivity.class);
                List<Fm1Itembean> itembeanListTemp = new ArrayList<>();
                for(int i=0;i<Share.fm1ItembeanList.size();i++)
                {
                    if(Share.fm1ItembeanList.get(i).getIscheck())
                    {
                        itembeanListTemp.add(Share.fm1ItembeanList.get(i));
                    }
                }
                intent.putParcelableArrayListExtra("checked", (ArrayList<? extends Parcelable>) itembeanListTemp);
                startActivity(intent);
                break;

            case R.id.bt_fm1_cancle:
                mAdapter.flag = false;
                selectLL.setVisibility(View.GONE);
                bottomLL.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();

                break;
        }

    }
}
