package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String str = new JSONObject().put("code", 00).put("data", json1).toString();

        final String urlPath = "http://58.213.141.235:8080/qmjs_FEP/datewalk/createSportTrack.action";
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
            JSONObject retJson = new JSONObject();
            retJson.getJSONObject(responseData);
            Message message = new Message();
            if(retJson.get("code").toString().equals("1"))
            {
                message.what=Integer.parseInt(retJson.get("is_admin").toString());
                message.obj=retJson.get("user_id");
                handler.sendMessage(message);
            }
            else
                handler.sendEmptyMessage(0);



        } catch (Exception e) {
// TODO: handle exception
            throw new RuntimeException(e);
        }
//        Looper.loop();
    }
}

