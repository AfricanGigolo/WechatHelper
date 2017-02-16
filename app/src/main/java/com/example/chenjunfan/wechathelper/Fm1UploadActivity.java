package com.example.chenjunfan.wechathelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by chenjunfan on 2017/2/16.
 */

public class Fm1UploadActivity extends Activity implements View.OnClickListener {
    TextView longTV,shortTV;
    FrameLayout fl_desc;
    Button moreBT,date1BT,date2BT;
    ImageButton reBT;
    ImageView moreIV;
    boolean isInit = false;
    boolean isShowShortText = true;
    private int year1,year2;
    private int month1,month2;
    private int day1,day2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm1upload);
        initView();
        Calendar mycalendar=Calendar.getInstance();

        year1=mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month1=mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day1=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天


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

        moreBT.setOnClickListener(this);
        date2BT.setOnClickListener(this);
        date1BT.setOnClickListener(this);
        reBT.setOnClickListener(this);

        String str = "新浪科技讯 北京时间7月25日凌晨消息，在今天举行的新产品发布会上，谷歌发布Android 4.3版本，代号仍为\"果冻豆(Jelly Bean)\"。今天发布的新一代Nexus 7将搭载该操作系统，Nexus系列设备今日可收到OTA推送更新。\r\nAndroid 4.3操作系统新增一系列功能。首先是多用户设置功能，包括针对保护儿童的“受限文件(restricted profiles)”特性。用户可以对应用内容进行限制，防止儿童在使用应用时看到不适宜内容，或接触不合适的应用内购买广告。这项功能与微软Windows Phone的\"儿童乐园(Microsoft's Kid's Corner)\"功能类似。\r\n第二项升级是智能蓝牙(Bluetooth Smart)功能，即\"低功耗蓝牙(Bluetooth Low Energy)\"。";
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
                DatePickerDialog dpd=new DatePickerDialog(this,Datelistener1,year1,month1,day1);
                dpd.show();
                break;
            case R.id.bt_fm1up_date2:
                DatePickerDialog dpd2=new DatePickerDialog(this,Datelistener2,year1,month1,day1);
                dpd2.show();
                break;
            case R.id.bt_fm1up_re:
                finish();
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


}
