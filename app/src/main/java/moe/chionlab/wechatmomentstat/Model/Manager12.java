package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/4/12.
 */

public class Manager12 {

    private Handler handler;
    private Context context;

    public Manager12(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    public void upload(List<String> groups)
    {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("id", NowUser.id);
        data.put("groups",groups);
        Map<String,Object>finaldata = new HashMap<>();
        finaldata.put("code",12);
        finaldata.put("data",data);
        String str = new Gson().toJson(finaldata);

        Looper.prepare();
        final String urlPath = Share.IP_ADDRESS
                +"/ChatDetection/groupManageServlet";
        Log.d("url", urlPath);
        URL url;
        try {
            url = new URL(urlPath);
/*封装子对象*/

            String content = str;

            System.out.println(content);
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

            //Toast.makeText(context,"收到："+ responseData, Toast.LENGTH_SHORT).show();
            Log.d("responsData", responseData);
            Message msg = new Message();
            msg.what=12;
//            JSONTokener jsonTokener = new JSONTokener(responseData);
//            msg.obj=((JSONObject)jsonTokener.nextValue()).getString("code");
            msg.obj=responseData;
            handler.sendMessage(msg);


        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Log.d("response", "连接网络失败");
            Message msg = new Message();
            msg.what=12;
            msg.obj = "2";
            handler.sendMessage(msg);



        }
        Looper.loop();
    }
}
