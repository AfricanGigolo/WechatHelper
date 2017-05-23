package moe.chionlab.wechatmomentstat.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import moe.chionlab.wechatmomentstat.R;


import java.util.List;

/**
 * Created by chenjunfan on 2017/2/18.
 */

public class Fm2Adapter extends BaseAdapter {

    private List<Fm2Itembean> itemList;
    private LayoutInflater mInfalter;
    public boolean flag = false;



    public Fm2Adapter(Context context, List<Fm2Itembean> itemList)
    {
        this.itemList = itemList;
        mInfalter =  LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Fm2Adapter.ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInfalter.inflate(R.layout.item_fm2,null);
            viewHolder.checkboxOperateData = (CheckBox) convertView.findViewById(R.id.checkBox_itemfm2_item);
            viewHolder.titleTV = (TextView) convertView.findViewById(R.id.tv_itemfm2_title);
            viewHolder.rightTV = (TextView) convertView.findViewById(R.id.tv_itemfm2_right);
            viewHolder.itemRL = (RelativeLayout) convertView.findViewById(R.id.rl_fm2item);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (Fm2Adapter.ViewHolder) convertView.getTag();
        }
        final Fm2Itembean itembean = itemList.get(i);
        SPlist sPlist = new SPlist();
        final int tempI = i;
        if (itembean != null)
        {
            viewHolder.titleTV.setText(itembean.getTitle());
            viewHolder.rightTV.setText("权值:"+ sPlist.getstr(itembean.getRight()+""));

            if (flag) {
                viewHolder.checkboxOperateData.setVisibility(View.VISIBLE);
            } else {
                viewHolder.checkboxOperateData.setVisibility(View.GONE);
            }
            viewHolder.checkboxOperateData.setChecked(itembean.getIscheck());

            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
//            viewHolder.itemRL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itembean.getIscheck()) {
//                        if(flag) {
//                            itembean.setIscheck(false);
//                            viewHolder.checkboxOperateData.setChecked(flag);
//                            itemList.get(tempI).setIscheck(false);
//                        }
//                    } else {
//                        if(flag) {
//                            itembean.setIscheck(true);
//                            viewHolder.checkboxOperateData.setChecked(true);
//                            itemList.get(tempI).setIscheck(true);
//                        }
//                    }
//                }
//            });

            viewHolder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itembean.getIscheck()) {
                        if(flag) {
                            itembean.setIscheck(false);
                            itemList.get(tempI).setIscheck(false);
                        }
                    } else {
                        if(flag) {
                            itembean.setIscheck(true);
                            itemList.get(tempI).setIscheck(true);
                        }
                    }
                }

            });
        }


        return convertView;
    }




    class ViewHolder{
        public TextView titleTV,rightTV;
        public CheckBox checkboxOperateData;
        public RelativeLayout itemRL;
    }
}
