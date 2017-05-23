package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.Share;


/**
 * Created by chenjunfan on 2017/3/14.
 * 获取已设置为自动上传的群组模块
 */

public class Manager10 {
    Context context;
    Handler handler;

    public Manager10(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void upload(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("code","10");
            jsonObject.put("data",new JSONObject().put("id", Integer.parseInt(NowUser.id)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = jsonObject.toString();

        final String urlPath = Share.IP_ADDRESS+"/ChatDetection/groupManageServlet";
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
            Log.d("Manager10", responseData);
            JSONTokener jsonTokener = new JSONTokener(responseData);
            List<Map<String, Object>> oldgrouplist = null;

            try {
                jsonObject = (JSONObject) jsonTokener.nextValue();
                JSONObject jsdata = jsonObject.getJSONObject("data");
                JSONArray jsgroups = jsdata.getJSONArray("groups");
                Gson gson = new Gson();
                oldgrouplist = gson.fromJson(jsgroups.toString(), new TypeToken<List<Map<String, Object>>>() {
                }.getType());


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Manager30", "解析json失败");

            }
            for(int i=0;i<oldgrouplist.size();i++)
            {
                Fm1Itembean fm1Itembean = new Fm1Itembean();
                fm1Itembean.setIscheck(false);
                fm1Itembean.setTitle(oldgrouplist.get(i).get("name").toString());
                fm1Itembean.setId(oldgrouplist.get(i).get("group").toString());
                Share.fm3addedList.add(fm1Itembean);
            }

            Message message = new Message();
            message.what = 10;
            message.obj="1";
            handler.sendMessage(message);



        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Message message = new Message();
            message.what = 10;
            message.obj="2";
            handler.sendMessage(message);


        }

    }





}
