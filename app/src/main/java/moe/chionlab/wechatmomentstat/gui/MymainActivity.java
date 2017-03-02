package moe.chionlab.wechatmomentstat.gui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import moe.chionlab.wechatmomentstat.Config;
import moe.chionlab.wechatmomentstat.Model.UpdataService;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.SnsStat;
import moe.chionlab.wechatmomentstat.Task;

/**
 * Created by chenjunfan on 2017/3/1.
 */

public class MymainActivity extends Activity {
    Task task = null;
    SnsStat snsStat = null;
    public TextView mainTV = null;
    Button button;
    UpdataService updataService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        task = new Task(this.getApplicationContext());
        task.testRoot();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readWeChatDatabase();
            }
        }).start();


        setContentView(R.layout.actitvity_mymain);
        mainTV = (TextView) findViewById(R.id.tv_mymain_tv1);
        button = (Button) findViewById(R.id.bt_mymain_run);
        button.setText("正在获取root");
        button.setEnabled(false);


        button.setText("run");
        button.setEnabled(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updataService = new UpdataService(task, snsStat, mainTV);
                Intent startUpdataIntent = new Intent(MymainActivity.this, UpdataService.class);
                startService(startUpdataIntent);

                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");

                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getBaseContext(), "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();


                }
            }
        });


//        ((Button) findViewById(R.id.bt_mymain_run)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((Button) findViewById(R.id.bt_mymain_run)).setText(R.string.exporting_sns);
//                ((Button) findViewById(R.id.bt_mymain_run)).setEnabled(false);
//                new MymainActivity.RunningTask().execute();
//            }
//        });
//
////        TextView descriptionHtmlTextView = (TextView) findViewById(R.id.description_html_textview);
////        descriptionHtmlTextView.setMovementMethod(LinkMovementMethod.getInstance());
////        descriptionHtmlTextView.setText(Html.fromHtml(getResources().getString(R.string.description_html)));
//
//    }
//
//    class RunningTask extends AsyncTask<Void, Void, Void> {
//
//        Throwable error = null;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                task.copySnsDB();
//                task.initSnsReader();
//                task.snsReader.run();
//                snsStat = new SnsStat(task.snsReader.getSnsList());
//            } catch (Throwable e) {
//                this.error = e;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void voidParam) {
//            super.onPostExecute(voidParam);
//            ((Button) findViewById(R.id.bt_mymain_run)).setText(R.string.launch);
//            ((Button) findViewById(R.id.bt_mymain_run)).setEnabled(true);
//            if (this.error != null) {
//                Toast.makeText(MymainActivity.this, R.string.not_rooted, Toast.LENGTH_LONG).show();
//                Log.e("wechatmomentstat", "exception", this.error);
//
//                try {
//                    ((TextView) findViewById(R.id.description_textview_2)).setText("Error: " + this.error.getMessage());
//                } catch (Throwable e) {
//                    Log.e("wechatmomentstat", "exception", e);
//                }
//
//                return;
//            }
//            Share.snsData = snsStat;
//            String str = "";
//            for(int i=0;i<snsStat.snsList.size();i++)
//            {
//                SnsInfo snsInfo = snsStat.snsList.get(i);
//                str=str+"第"+(i+1)+"条:\n"
//                        +"用户名:"+snsInfo.authorId
//                        +"\n昵称:"+snsInfo.authorName
//                +"\n时间:"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date(snsInfo.timestamp * 1000))+
//                "\n内容:"+snsInfo.content+"\n\n";
//            }
//            mainTV.setText(str);
//        }
//    }
    }

    public void readWeChatDatabase( ) {

        Task task = new Task(getBaseContext());
        task.testRoot();
        try {
            task.copyEnDb();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Log.d("MymainActivity", "已复制EnDB");
        }


        SQLiteDatabase.loadLibs(this);


        String password = "XXXXXXX";
        File databaseFile = getDatabasePath(Config.EXT_DIR+"/EnMicroMsg.db");
        //File databaseFile = getDatabasePath("EnMicroMsg.db");
        //eventsData = new myDataHelper(this);

//        if (databaseFile != null) {
//            Log.d("MymainActivity", "dbFile open!");
//        }


        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Config.EXT_DIR+"/EnMicroMsg.db", "04619f4", null, hook);
            Cursor c = db.query("message", null, null, null, null, null, null);

            while (c.moveToNext()) {
                int _id = c.getInt(c.getColumnIndex("msgId"));
                String name = c.getString(c.getColumnIndex("content"));
                Log.i("db", "_id=>" + _id + ", content=>" + name);
            }
            c.close();
            db.close();
        } catch (Exception e) {

        }
    }




}

