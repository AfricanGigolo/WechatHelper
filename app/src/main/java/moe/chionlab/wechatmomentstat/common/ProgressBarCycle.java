package moe.chionlab.wechatmomentstat.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.widget.ProgressBar;

/**
 * Created by chenjunfan on 2017/3/22.
 */

public class ProgressBarCycle {
    private static ProgressDialog progressBar;


    public static void setProgressBar(Context context,String str)
    {
        progressBar = null;
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage(str);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    public static void cancleProgressBar()
    {
        if(progressBar!=null)
        {
            progressBar.cancel();
        }
    }

}
