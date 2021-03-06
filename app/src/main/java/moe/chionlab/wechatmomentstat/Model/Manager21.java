package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/4/12.
 */

public class Manager21 {
    private Context context;
    private Handler handler;

    public Manager21(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void upload()
    {
        Map<String,Object> data = new HashMap<>();
        data.put("id", NowUser.id);
        Map<String,Object> finaldata = new HashMap<>();
        finaldata.put("data",data);
        finaldata.put("code",21);
        String str = new Gson().toJson(finaldata);

        Looper.prepare();
        final String urlPath = Share.IP_ADDRESS
                +"/ChatDetection/keywordManageServlet";
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
            msg.what=21;
            JSONTokener jsonTokener = new JSONTokener(responseData);
            JSONObject obj=((JSONObject)jsonTokener.nextValue()).getJSONObject("data");
            JSONArray jsonArray = obj.getJSONArray("keywords");
            List<Map<String,Object>> keywordsList = new ArrayList<>();
            Gson gson = new Gson();
            keywordsList = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            for(int i=0;i<keywordsList.size();i++)
            {
                Map<String,Object> datamap=new HashMap<>();
                datamap=keywordsList.get(i);
                Log.d("Manager21", datamap.get("weight").toString() + datamap.get("keyword").toString()+datamap.get("property").toString());
            }
            msg.obj=keywordsList;
            handler.sendMessage(msg);


        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Log.d("response", "连接网络失败");
            Message msg = new Message();
            msg.what=21;
            msg.obj = null;
            handler.sendMessage(msg);



        }
        Looper.loop();
    }

}
