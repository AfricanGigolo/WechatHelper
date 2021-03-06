package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/3/20.
 */

public class Manager32 {
    Context context;
    Handler handler;

    public Manager32(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    private String getJsonStr(List<Map<String, Object>> dataList)
    {
        Log.d("getJsonSta","开始"+dataList.size());
        Map<String,Object> map =new HashMap<String,Object>();
//        for (int i=0;i<dataList.size();i++)
//        {
////            Log.d("in", "in"+i);
////            Log.d("snsdata:", "第" + (i + 1) + "条:\n"
////                    + "用户名:" + dataList.get(i).get("sender")
////                    + "\n昵称:" + dataList.get(i).get("name")
////                    + "\n时间:" + dataList.get(i).get("timestamp") +
////                    "\n内容:" + ((HashMap)dataList.get(i).get("data")).get("text") + "\n\n");
//        }
        map.put("chat_records",dataList);
        map.put("id", NowUser.id);
        Map<String,Object> map2 = new HashMap<>();

        map2.put("data",map);
        map2.put("code",32);

        Gson gson = new Gson();
        String str = gson.toJson(map2);
        LogUtil.e("",str);
        return str;
    }

    public void upload()
    {


// TODO Auto-generated method stub

        final String urlPath = Share.IP_ADDRESS
                +"/ChatDetection/uploadServlet";
        Log.d("url", urlPath);
        URL url;
        try {
            url = new URL(urlPath);
/*封装子对象*/

            String content = getJsonStr(new ReadDatabase(context).readSnsDatabaseText());

            if(content.equals(""))
            {
                Log.d("错误", "Manager32转换json错误");
                return;
            }
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
            msg.what=32;
            JSONTokener jsonTokener = new JSONTokener(responseData);
            msg.obj=((JSONObject)jsonTokener.nextValue()).getString("code");
            handler.sendMessage(msg);
            return;


        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Log.d("response", "连接网络失败");
            Message msg = new Message();
            msg.what=32;
            msg.obj = "2";
            handler.sendMessage(msg);



        }
        Looper.loop();
    }

}
