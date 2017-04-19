package moe.chionlab.wechatmomentstat.gui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.chionlab.wechatmomentstat.Model.Fm2Itembean;
import moe.chionlab.wechatmomentstat.Model.Manager21;
import moe.chionlab.wechatmomentstat.Model.Manager22;
import moe.chionlab.wechatmomentstat.Model.Manager23;
import moe.chionlab.wechatmomentstat.Model.Manager24;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/2/20.
 */

public class NewkeywordActivity extends Activity implements View.OnClickListener {
    TextView titleTV;
    EditText keywordET,rightET;
    Button yesBT,cancleBT;
    ImageButton reBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newkeyword);
        initView();
    }

    private void initView()
    {
        titleTV = (TextView) findViewById(R.id.tv_nkw_title);
        keywordET = (EditText) findViewById(R.id.et_nkw_keyword);
        rightET = (EditText) findViewById(R.id.et_nkw_right);
        yesBT = (Button) findViewById(R.id.bt_nkw_yes);
        cancleBT = (Button) findViewById(R.id.bt_nkw_cancle);
        reBT = (ImageButton) findViewById(R.id.bt_nkw_re);

        yesBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);
        reBT.setOnClickListener(this);
        Intent intent = getIntent();
        titleTV.setText(intent.getStringExtra("title"));

        if(intent.getStringExtra("type").equals("edit"))
        {
            keywordET.setText(Share.fm2ItembeanList.get(intent.getIntExtra("No",0)).getTitle());
            rightET.setText(Share.fm2ItembeanList.get(intent.getIntExtra("No",0)).getRight());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_nkw_re:
                finish();
                break;
            case R.id.bt_nkw_cancle:
                finish();
                break;
            case R.id.bt_nkw_yes:
//                if(!keywordET.getText().equals("")&&!rightET.getText().equals(""))
//                {
//                    Intent intent = new Intent();
//                    intent.putExtra("return",new Fm2Itembean(keywordET.getText().toString(),Integer.parseInt(rightET.getText().toString())));
//                    setResult(1,intent);
//                    finish();
//                }
                if(getIntent().getStringExtra("type").equals("add"))
                {
                    if(keywordET.getText().equals(""))
                    {

                    }
                    if(rightET.getText().equals(""))
                    {

                    }
                    else
                    {
                        ProgressBarCycle.setProgressBar(NewkeywordActivity.this,"正在上传...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Manager22 manager22 = new Manager22(NewkeywordActivity.this,handler);
                                Map<String,String> datamap = new HashMap<>();
                                datamap.put("id", NowUser.id);
                                datamap.put("keyword",keywordET.getText().toString());
                                datamap.put("weight",rightET.getText().toString());
                                datamap.put("property","");
                                Map<String,Object> finaldata = new HashMap<>();
                                finaldata.put("code",22);
                                finaldata.put("data",datamap);
                                manager22.upload(finaldata);
                            }
                        }).start();

                    }
                }
                else if(getIntent().getStringExtra("type").equals("edit"))
                {
                    if(keywordET.getText().equals(""))
                    {

                    }
                    if(rightET.getText().equals(""))
                    {

                    }
                    else
                    {
                        ProgressBarCycle.setProgressBar(NewkeywordActivity.this,"正在上传...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<String> oldkeywordList = new ArrayList<String>();
                                oldkeywordList.add(Share.fm2ItembeanList.get(getIntent().getIntExtra("No",0)).getTitle());
                                Manager24 manager24 = new Manager24(NewkeywordActivity.this,temphandler);


                            }
                        }).start();

                    }
                }
                break;
        }
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 22:
                    if(msg.obj.toString().equals("22"))
                    {
                        if(getIntent().getStringExtra("type").equals("add"))
                        {
                            Toast.makeText(NewkeywordActivity.this, "添加关键词成功", Toast.LENGTH_SHORT).show();
                        }
                        else if(getIntent().getStringExtra("type").equals("edit"))
                        {
                            Toast.makeText(NewkeywordActivity.this, "编辑关键词成功", Toast.LENGTH_SHORT).show();
                        }
                        ProgressBarCycle.cancleProgressBar();

                    }
                    else
                    {
                        if(getIntent().getStringExtra("type").equals("add"))
                        {
                            Toast.makeText(NewkeywordActivity.this, "添加关键词失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                        else if(getIntent().getStringExtra("type").equals("edit"))
                        {
                            Toast.makeText(NewkeywordActivity.this, "编辑关键词失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                        ProgressBarCycle.cancleProgressBar();
                    }
                    break;
                case 23:
                    if(msg.obj.toString().equals("23"))
                    {
                        Toast.makeText(NewkeywordActivity.this, "编辑关键词成功", Toast.LENGTH_SHORT).show();
                        Fm2Itembean fm2Itembean = new Fm2Itembean(keywordET.getText().toString(),Integer.parseInt(rightET.getText().toString()));
                        Share.fm2ItembeanList.set(getIntent().getIntExtra("No",0),fm2Itembean);

                        Intent intent = new Intent();
                        setResult(2,intent);
                        ProgressBarCycle.cancleProgressBar();

                        finish();
                    }
                    else
                    {
                        Toast.makeText(NewkeywordActivity.this, "编辑关键词失败，所编辑的关键词已被服务器删除，请重新设置", Toast.LENGTH_SHORT).show();
                        ProgressBarCycle.cancleProgressBar();
                    }

                    break;

            }
        }
    };

    Handler temphandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 24:
                    if(msg.obj.toString().equals("24"))
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Manager23 manager23 = new Manager23(NewkeywordActivity.this,handler);
                                Map<String,String> datamap = new HashMap<>();
                                datamap.put("id", NowUser.id);
                                datamap.put("keyword",keywordET.getText().toString());
                                datamap.put("weight",rightET.getText().toString());
                                datamap.put("property","");
                                Map<String,Object> finaldata = new HashMap<>();
                                finaldata.put("code",23);
                                finaldata.put("data",datamap);
                                manager23.upload(finaldata);
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(NewkeywordActivity.this, "上传失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                        ProgressBarCycle.cancleProgressBar();
                    }

            }
        }
    };

}
