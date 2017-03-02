package moe.chionlab.wechatmomentstat.Model;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.SnsStat;
import moe.chionlab.wechatmomentstat.Task;
import moe.chionlab.wechatmomentstat.gui.MainActivity;
import moe.chionlab.wechatmomentstat.gui.MymainActivity;

import static android.R.id.content;

/**
 * Created by chenjunfan on 2017/3/1.
 */

public class UpdataService extends Service {
    Task task = null;
    SnsStat snsStat = null;
    TextView mainTV;

    public UpdataService(Task task, SnsStat snsStat, TextView mainTV) {
        this.task = task;
        this.snsStat = snsStat;
        this.mainTV = mainTV;

    }

    public UpdataService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("微信助手").setContentText("正在后台获取朋友圈数据").setSmallIcon(R.mipmap.ic_launcher);
        Intent notifyIntent = new Intent(this,UpdataService.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);
        Notification notification = builder.build();
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        task = new Task(getApplicationContext());
        startForeground(1,notification);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        class RunningTask extends AsyncTask<Void, Void, Void> {

            Throwable error = null;


            @Override
            protected Void doInBackground(Void... params) {
                try {
                    task.copySnsDB();
                    task.initSnsReader();
                    task.snsReader.run();
                    snsStat = new SnsStat(task.snsReader.getSnsList());
                } catch (Throwable e) {
                    this.error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void voidParam) {
                super.onPostExecute(voidParam);

                if (this.error != null) {
                    Toast.makeText(UpdataService.this, R.string.not_rooted, Toast.LENGTH_LONG).show();
                    Log.e("wechatmomentstat", "exception", this.error);

//                try {
//                    ((TextView) findViewById(R.id.description_textview_2)).setText("Error: " + this.error.getMessage());
//                } catch (Throwable e) {
//                    Log.e("wechatmomentstat", "exception", e);
//                }

                    return;
                }
//            Share.snsData = snsStat;
                String str = "";
                for(int i=0;i<snsStat.snsList.size();i++) {
                    SnsInfo snsInfo = snsStat.snsList.get(i);
                    str = str + "第" + (i + 1) + "条:\n"
                            + "用户名:" + snsInfo.authorId
                            + "\n昵称:" + snsInfo.authorName
                            + "\n时间:" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date(snsInfo.timestamp * 1000)) +
                            "\n内容:" + snsInfo.content + "\n\n";
                }
                Log.d("RunningTask", str);
            }
        }

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {

                        new RunningTask().execute();
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();





//            }
//        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }


}
