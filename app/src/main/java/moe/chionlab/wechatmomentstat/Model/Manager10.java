package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Map;

import moe.chionlab.wechatmomentstat.common.NowUser;


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


    public void upload() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","10");
        jsonObject.put("data",new JSONObject().put("id", NowUser.id));
        String str = jsonObject.toString();

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
            Map<String,Object> datamap= (Map<String, Object>) retJson.get("data");
            ArrayList<Group> groups = (ArrayList<Group>) datamap.get("groups");
            Message message = new Message();
            handler.sendEmptyMessage(-1);



        } catch (Exception e) {
// TODO: handle exception
            throw new RuntimeException(e);
        }

    }





}
