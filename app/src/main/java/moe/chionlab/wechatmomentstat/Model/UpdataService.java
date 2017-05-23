package moe.chionlab.wechatmomentstat.Model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
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

/**
 * Created by chenjunfan on 2017/3/1.
 */

public class UpdataService extends Service {




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
        builder.setContentTitle("微信助手").setContentText("自动上传服务正在后台运行").setSmallIcon(R.drawable.tx);
        Intent notifyIntent = new Intent(this,UpdataService.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);
        Notification notification = builder.build();
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(1,notification);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        class RunningTask extends AsyncTask<Void, Void, Void> {




            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }

            @Override
            protected void onPostExecute(Void voidParam) {
                super.onPostExecute(voidParam);


                Log.d("RunningTask", "前台服务运行中");
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }


}
