package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/3/14.
 */

public class Manager00 {
    Context context;
    Handler handler;

    public Manager00(Context context,Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void upload(String account, String password) throws JSONException {
        JSONObject json1 = new JSONObject();
        json1.put("account", account);
        json1.put("password", password);
        String str = new JSONObject().put("code", "00").put("data", json1).toString();

        final String urlPath = Share.IP_ADDRESS+"/ChatDetection/userManageServlet";
        URL url;
        try {
            url = new URL(urlPath);

/*封装子对象*/

            String content = str;
            Log.d("update", content);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);//设置允许输出
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Charset", "UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(content.getBytes());
            os.close();
/*服务器返回的响应码*/

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            Log.d("Manager00", responseData);
            JSONObject retJson = new JSONObject();
//            retJson.getJSONObject(responseData);
            JSONTokener jsonTokener = new JSONTokener(responseData);
            retJson = (JSONObject) jsonTokener.nextValue();
            Message message = new Message();
            if(retJson.get("code").toString().equals("1"))
            {
                message.what=Integer.parseInt(retJson.get("is_admin").toString());
                message.obj=retJson.get("id").toString();
                handler.sendMessage(message);
            }
            else
                handler.sendEmptyMessage(-1);



        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Log.d("Manager00", "错误");
            Message msg = new Message();
            msg.what = -2;
            handler.sendMessage(msg);
        }
//        Looper.loop();
    }
}

