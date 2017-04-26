package moe.chionlab.wechatmomentstat.gui;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager31;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;

/**
 * Created by chenjunfan on 2017/2/16.
 */

public class Fm1UploadActivity extends Activity implements View.OnClickListener {
    TextView longTV,shortTV;
    FrameLayout fl_desc;
    Button moreBT,date1BT,date2BT,yesBT,cancleBT;
    ImageButton reBT;
    ImageView moreIV;
    boolean isInit = false;
    boolean isShowShortText = true;
    private int year1=2017,year2=2017;
    private int month1=1,month2=1;
    private int day1=1,day2=1;
    private int year,day,month;
    private List<Fm1Itembean> itembeanList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm1upload);
        initView();
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
         year = t.year;
         month = t.month;
         day = t.monthDay;





    }

    private void initView()
    {
        fl_desc = (FrameLayout) findViewById(R.id.fl_desc);
        longTV = (TextView) findViewById(R.id.tv_fm1up_long);
        shortTV = (TextView) findViewById(R.id.tv_fm1up_short);
        moreBT = (Button) findViewById(R.id.bt_fm1up_more);
        moreIV = (ImageView) findViewById(R.id.iv_fm1up_moreline);
        date1BT = (Button) findViewById(R.id.bt_fm1up_date1);
        date2BT = (Button) findViewById(R.id.bt_fm1up_date2);
        reBT = (ImageButton) findViewById(R.id.bt_fm1up_re);
        yesBT = (Button) findViewById(R.id.bt_fm1up_yes);
        cancleBT = (Button) findViewById(R.id.bt_fm1up_cancle);

        moreBT.setOnClickListener(this);
        date2BT.setOnClickListener(this);
        date1BT.setOnClickListener(this);
        reBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);
        yesBT.setOnClickListener(this);

        Intent intent = getIntent();
        itembeanList = intent.getParcelableArrayListExtra("checked");


        //String str = "新浪科技讯 北京时间7月25日凌晨消息，在今天举行的新产品发布会上，谷歌发布Android 4.3版本，代号仍为\"果冻豆(Jelly Bean)\"。今天发布的新一代Nexus 7将搭载该操作系统，Nexus系列设备今日可收到OTA推送更新。\r\nAndroid 4.3操作系统新增一系列功能。首先是多用户设置功能，包括针对保护儿童的“受限文件(restricted profiles)”特性。用户可以对应用内容进行限制，防止儿童在使用应用时看到不适宜内容，或接触不合适的应用内购买广告。这项功能与微软Windows Phone的\"儿童乐园(Microsoft's Kid's Corner)\"功能类似。\r\n第二项升级是智能蓝牙(Bluetooth Smart)功能，即\"低功耗蓝牙(Bluetooth Low Energy)\"。";
        String str = "已选择的群组：";
        for(int i = 0;i<itembeanList.size();i++)
        {
            str = str + "<"+itembeanList.get(i).getTitle()+"> ";
        }
        longTV.setText(str);
        shortTV.setText(str);

        ViewTreeObserver vto = fl_desc.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isInit)
                    return true;
                if (mesureDescription(shortTV, longTV)) {
                    moreIV.setVisibility(View.VISIBLE);
                    moreBT.setVisibility(View.VISIBLE);
                }
                isInit = true;
                return true;
            }
        });


    }

    /**
     * 计算描述信息是否过长
     */
    private boolean mesureDescription(TextView shortView, TextView longView) {
        final int shortHeight = shortView.getHeight();
        final int longHeight = longView.getHeight();
        if (longHeight > shortHeight) {
            shortView.setVisibility(View.VISIBLE);
            longView.setVisibility(View.GONE);
            return true;
        }
        shortView.setVisibility(View.GONE);
        longView.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_fm1up_more:
                if (isShowShortText) {
                    shortTV.setVisibility(View.GONE);
                    longTV.setVisibility(View.VISIBLE);
                } else {
                    shortTV.setVisibility(View.VISIBLE);
                    longTV.setVisibility(View.GONE);
                }
                toogleMoreButton(moreBT);
                isShowShortText = !isShowShortText;
                break;
            case R.id.bt_fm1up_date1:
                DatePickerDialog dpd=new DatePickerDialog(this,Datelistener1,year,month,day);
                dpd.show();
                break;
            case R.id.bt_fm1up_date2:
                DatePickerDialog dpd2=new DatePickerDialog(this,Datelistener2,year,month,day);
                dpd2.show();
                break;
            case R.id.bt_fm1up_re:
                finish();
                break;
            case R.id.bt_fm1up_cancle:
                finish();
                break;
            case R.id.bt_fm1up_yes:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String mintime = null, maxtime = null;
                try {
                    mintime = ""+simpleDateFormat.parse(year1 + "-" + (month1 + 1) + "-" + day1).getTime();
                    maxtime = ""+simpleDateFormat.parse(year2 + "-" + (month2 + 1) + "-" + day2).getTime();
                    Log.d("Fm1UploadActivity", "mintime:" + mintime + " maxtime:" + maxtime);
                } catch (ParseException e) {
                    Log.d("Fm1UploadActivity", "转化日期失败");
                    e.printStackTrace();
                }
                Log.d("Fm1UploadActivity", (year1 + "," + month1 + "," + day1 + ";" + year2 + "," + month2 + "," + day2));
                if(date1BT.getText().equals("选择日期")){
                    Toast.makeText(this, "请选择起始日期", Toast.LENGTH_SHORT).show();
                }
                else if(date2BT.getText().equals("选择日期"))
                {
                    Toast.makeText(this, "请选择终止日期", Toast.LENGTH_SHORT).show();
                }
                else if(Long.parseLong(mintime)>=Long.parseLong(maxtime))
                {
                    Toast.makeText(this, "请选择正确的日期", Toast.LENGTH_SHORT).show();
                }
                else {
                    ProgressBarCycle.setProgressBar(Fm1UploadActivity.this, "正在上传,数据库解析需要时间,请耐心等待...");

                    List<String> checkedlist = new ArrayList<>();
                    for (int i = 0; i < itembeanList.size(); i++) {
                        checkedlist.add(itembeanList.get(i).getId());
                    }
                    final Map<String, Object> checkedMap = new HashMap<>();
                    checkedMap.put("checkedlist", checkedlist);
                    checkedMap.put("maxtime", maxtime);
                    checkedMap.put("mintime", mintime);

                    final Manager31 manager31 = new Manager31(Fm1UploadActivity.this, handler);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            manager31.upload(checkedMap, 1);
                        }
                    }).start();
                }

                break;


        }
    }

    /**
     * 更改按钮【更多】的文本
     */
    private void toogleMoreButton(Button btn) {

        String text = (String) btn.getText();
        String moreText = "展开";
        String lessText = "收起";
        if (moreText.equals(text)) {
            btn.setText(lessText);
        } else {
            btn.setText(moreText);
        }
    }

    private DatePickerDialog.OnDateSetListener Datelistener1=new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {


            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year1 = myyear;
            month1 = monthOfYear;
            day1 = dayOfMonth;
            //更新日期
            date1BT.setText(year1 + "-" + (month1 + 1) + "-" + day1);

        }
    };

    private DatePickerDialog.OnDateSetListener Datelistener2=new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {


            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year2= myyear;
            month2 = monthOfYear;
            day2 = dayOfMonth;
            //更新日期
            date2BT.setText(year2 + "-" + (month2 + 1) + "-" + day2);

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 31:
                    if(msg.obj.toString().equals("1"))
                    {
                        Toast.makeText(Fm1UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        ProgressBarCycle.cancleProgressBar();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Fm1UploadActivity.this, "上传失败,请重试", Toast.LENGTH_SHORT).show();
                        ProgressBarCycle.cancleProgressBar();
                    }
            }
        }
    };


}
