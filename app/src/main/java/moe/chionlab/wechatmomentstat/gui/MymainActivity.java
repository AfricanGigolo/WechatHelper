package moe.chionlab.wechatmomentstat.gui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import moe.chionlab.wechatmomentstat.Model.Manager31;
import moe.chionlab.wechatmomentstat.Model.Manager32;
import moe.chionlab.wechatmomentstat.Model.ReadDatabase;
import moe.chionlab.wechatmomentstat.Model.UpdataService;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.SnsStat;
import moe.chionlab.wechatmomentstat.Task;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;

/**
 * Created by chenjunfan on 2017/3/1.
 */

public class MymainActivity extends Activity implements View.OnClickListener {
    Task task = null;
    SnsStat snsStat = null;
    public TextView mainTV = null;
    Button button,buttonsns;
    Handler handler;
    // ProgressBar progressBar;
    UpdataService updataService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        task = new Task(this.getApplicationContext());
        task.testRoot();


        setContentView(R.layout.actitvity_mymain);
        button = (Button) findViewById(R.id.bt_mymain_run);
        buttonsns = (Button) findViewById(R.id.bt_mymain_runsns);

        button.setOnClickListener(this);
        buttonsns.setOnClickListener(this);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar_mymain);


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updataService = new UpdataService(task, snsStat, mainTV);
//                Intent startUpdataIntent = new Intent(MymainActivity.this, UpdataService.class);
//                startService(startUpdataIntent);
//
//                try {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setComponent(cmp);
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(getBaseContext(), "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
//
//
//                }
//            }
//        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //progressBar.setVisibility(View.GONE);
                switch (msg.what) {
                    case 31:
                        ProgressBarCycle.cancleProgressBar();
                        if (msg.obj.equals("0")) {
                            Toast.makeText(MymainActivity.this, "上传文字聊天记录失败！", Toast.LENGTH_SHORT).show();
                        } else if (msg.obj.equals("1")) {
                            Toast.makeText(MymainActivity.this, "上传文字聊天记录成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MymainActivity.this, "上传文字聊天记录:无法连接网络", Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case 32:
                        ProgressBarCycle.cancleProgressBar();
                        if (msg.obj.equals("0")) {
                            Toast.makeText(MymainActivity.this, "上传朋友圈记录失败！", Toast.LENGTH_SHORT).show();
                        } else if (msg.obj.equals("1")) {
                            Toast.makeText(MymainActivity.this, "上传朋友圈记录成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MymainActivity.this, "上传朋友圈记录:无法连接网络！", Toast.LENGTH_SHORT).show();

                        }

                        break;
                }
            }
        };




//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//               // progressBar.setVisibility(View.VISIBLE);
//                Manager32 manager32 = new Manager32(MymainActivity.this,handler);
//                manager32.upload();
//            }
//        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.bt_mymain_run:
                ProgressBarCycle.setProgressBar(MymainActivity.this,"正在上传聊天记录,聊天记录数据库较大,请耐心等待");
                //Toast.makeText(this, "正在上传聊天记录,请耐心等待", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Manager31 manager31 = new Manager31(MymainActivity.this, handler);
                        manager31.upload(new HashMap<String,Object>(),2);


                    }
                }).start();
                break;
            case R.id.bt_mymain_runsns:
                ProgressBarCycle.setProgressBar(MymainActivity.this,"正在上传朋友圈记录,请耐心等待");
                //Toast.makeText(updataService, "正在上传朋友圈记录,请耐心等待", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
            @Override
            public void run() {
               // progressBar.setVisibility(View.VISIBLE);

                Manager32 manager32 = new Manager32(MymainActivity.this,handler);
                manager32.upload();

            }
        }).start();
                break;
        }
    }

    //password:04619f4


//    }
}

