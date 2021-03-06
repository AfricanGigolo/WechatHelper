package moe.chionlab.wechatmomentstat.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import moe.chionlab.wechatmomentstat.R;




import java.util.List;

/**
 * Created by chenjunfan on 2017/2/16.
 */

public class Fm1Adapter extends BaseAdapter{

    private List<Fm1Itembean> itemList;
    private LayoutInflater mInfalter;
    public boolean flag;



    public Fm1Adapter(Context context, List<Fm1Itembean> itemList)
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
    public View getView( int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInfalter.inflate(R.layout.item_fm1,null);
            viewHolder.checkboxOperateData = (CheckBox) convertView.findViewById(R.id.checkBox_itemfm1_item);
            viewHolder.titleTV = (TextView) convertView.findViewById(R.id.tv_itemfm1_title);
            viewHolder.itemLL = (LinearLayout) convertView.findViewById(R.id.ll_fm1item);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Fm1Itembean itembean = itemList.get(i);
        final int tempI = i;
        if (itembean != null)
        {
            viewHolder.titleTV.setText(itembean.getTitle());

            if (flag) {
                viewHolder.checkboxOperateData.setVisibility(View.VISIBLE);
            } else {
                viewHolder.checkboxOperateData.setVisibility(View.GONE);
            }
            viewHolder.checkboxOperateData.setChecked(itembean.getIscheck());


            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
//            viewHolder.itemLL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (itembean.getIscheck()) {
//                        viewHolder.checkboxOperateData.setChecked(false);
//                        itembean.setIscheck(false);
//                        itemList.get(tempI).setIscheck(false);
//
//                    } else {
//                        viewHolder.checkboxOperateData.setChecked(true);
//                        itembean.setIscheck(true);
//                        itemList.get(tempI).setIscheck(true);
//
//                    }
//                }
//            });

            viewHolder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itembean.getIscheck()) {

                            itembean.setIscheck(false);
                            itemList.get(tempI).setIscheck(false);

                    } else {

                            itembean.setIscheck(true);
                            itemList.get(tempI).setIscheck(true);

                    }
                }

            });
        }


        return convertView;
    }




    class ViewHolder{
        public TextView titleTV;
        public CheckBox checkboxOperateData;
        public LinearLayout itemLL;

    }
}
