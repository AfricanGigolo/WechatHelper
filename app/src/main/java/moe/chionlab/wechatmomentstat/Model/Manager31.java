package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.chionlab.wechatmomentstat.Config;
import moe.chionlab.wechatmomentstat.Task;
import moe.chionlab.wechatmomentstat.common.CommonList;
import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/3/13.
 */

public class Manager31 {

    private Context context;
    private SQLiteDatabase db = null;
    private Cursor c = null;
    Handler handler;


    List<Map<String,Object>> groupList;

    public Manager31(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    private String getJsonStr(List<Map<String, Object>> recordList,Map<String,Object> checkedmap,int type) {
        try
        {
            ArrayList<Map<String,Object>> groupList2;
            groupList2 = getLocalGroups(recordList);
            groupList2 = new Manager30(context).getAlreadyRecord(groupList2,checkedmap,type);
            Log.d("Manager31_grouplist2", groupList2.toString());
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id", NowUser.id);
            data.put("groups",groupList2);
            Map<String,Object> jsonmap = new HashMap<>();
            jsonmap.put("code",31);
            jsonmap.put("data",data);

            String retvalue =new Gson().toJson(jsonmap);
            Log.d("Manager31.togson", retvalue);
            return retvalue;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";

    }

    public ArrayList<Map<String,Object>> getLocalGroups(List<Map<String, Object>> recordList)
    {
        SQLiteDatabase.loadLibs(context);


        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");
            }
        };
        try {
            Log.d("Manager31", "开始替换chatroom信息");
            db = SQLiteDatabase.openOrCreateDatabase(Config.EXT_DIR + "/EnMicroMsg.db", Share.databasePassword, null, hook);


            for (int i = 0; i < recordList.size(); i++) {
                Map<String, Object> map = recordList.get(i);

                if (map.get("sender").toString().contains("chatroom")) {
                    map.put("chatroomid", map.get("sender"));
                    map.remove("sender");
                    String temp = map.get("content").toString();
                    int end = temp.indexOf(':');

                    if (end != -1 && end <= 20) {
                        temp = temp.substring(0, end);
                    } else {
                        temp = NowUser.id; //账户用户名
                    }
                    map.put("sender", temp);
                    String tempContent = map.get("content").toString();
                    tempContent = tempContent.replace(map.get("content").toString().substring(0, (map.get("content").toString().indexOf(":") + 1)), "");
                    map.remove("content");
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("text", tempContent);
                    map.put("data", data);

                    c = db.query("rcontact", new String[]{"username", "nickname"}, "username=?", new String[]{map.get("sender").toString()}, null, null, null);

                    if (c.moveToNext()) {
                        if (end != -1 && end <= 20) {
                            map.put("chatroomname", map.get("name"));
                            map.remove("name");
                            map.put("name", c.getString(c.getColumnIndex("nickname")));
                        }


                    } else {
                        map.put("chatroomname", map.get("name"));
                        map.remove("name");
                        map.put("name", NowUser.id);
                    }
                    c.close();
                } else {
                    recordList.remove(i);
                    i--;
                }
            }

            db.close();

            for (int i = 0; i < recordList.size(); i++) {
                Map<String, Object> map = recordList.get(i);
                //Log.d("record"+i," "+map.get("chatroomid")+" "+map.get("chatroomname"));

            }
            Log.d("record", "替换chatroom信息完成");

            //将所有recordList中的chatroom名字列出
            ArrayList<String> chatroomIdList = new ArrayList<String>();
            boolean flag = true;
            for (int i = 0; i < recordList.size(); i++) {

                Map<String, Object> map = recordList.get(i);
                flag = false;
                for (int j = 0; j < chatroomIdList.size(); j++) {
                    if (map.get("chatroomid").toString().equals(chatroomIdList.get(j))) {
                        flag = true;
                        break;
                    }
                }
                if (flag == false) {
                    chatroomIdList.add(map.get("chatroomid").toString());
                }
            }

//            for(int i = 0;i<chatroomIdList.size();i++)
//            {
//                Log.d("chatroomIdList", chatroomIdList.get(i));
//
//            }
            Log.d("record", "得到chatroomNameList完成");


            //针对每个名字形成新的dataList，并将当前的dataList和nameList的当前值元素封装成GroupList;

            ArrayList<Map<String, Object>> groupList2 = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < chatroomIdList.size(); i++) {

                ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                String id = chatroomIdList.get(i);
//                Log.d("i", i+"");
                for (int j = 0; j < recordList.size(); j++) {
//                    System.out.println(i+" "+j+"   "+recordList.get(j).get("chatroomid").toString());
                    if (recordList.get(j).get("chatroomid").toString().equals(id)) {


                        dataList.add(recordList.get(j));

                    }
                }

//                Log.d("go", "1");
                Map<String, Object> groupmap = new HashMap<String, Object>();
//                Log.d("groupmap"+i,""+dataList.get(0).get("chatroomid"));
//                Log.d("groupmap"+i,""+dataList.get(0).get("chatroomname"));

                for (int j = 0; j < dataList.size(); j++) {

                    groupmap.put("group", dataList.get(j).get("chatroomid"));
                    groupmap.put("name", dataList.get(j).get("chatroomname"));


                }

//                Log.d("go", "2");

                for (int k = 0; k < dataList.size(); k++) {
//                    Log.d("k", "" + k);
                    Map<String, Object> map = dataList.get(k);
//                    Log.d("map"+k, map.get("chatroomid").toString()+" "+map.get("chatroomname"));
                    Map<String, Object> map2 = new HashMap<String, Object>();

//                    map.remove("chatroomid");
//                    map.remove("chatroomname");
                    map2.put("code", map.get("code"));
                    map2.put("sender", map.get("sender"));
                    map2.put("name", map.get("name"));
                    map2.put("timestamp", map.get("timestamp"));
                    map2.put("order", map.get("order"));
                    map2.put("data", map.get("data"));

                    dataList.set(k, map2);
                }
//                Log.d("go", "\n3");

                groupmap.put("chat_records", dataList);
                groupList2.add(groupmap);

                Log.d("groupmap" + i, groupmap.get("group") + " " + groupmap.get("name"));
//                Log.d("go", "4");


            }
            return groupList2;
        }
        catch (Exception e)
        {
            ProgressBarCycle.cancleProgressBar();
            e.printStackTrace();
        }
        return null;


    }








    public void upload(Map<String,Object> checkedmap,int type) //type = 1时表示手动上传群组,type = 2时表示自动上传群组；
    {

// TODO Auto-generated method stub
        //Looper.prepare();
        final String urlPath =Share.IP_ADDRESS
        +"/ChatDetection/uploadServlet";
        Log.d("31url", urlPath);
        URL url;
        try {
            url = new URL(urlPath);
/*封装子对象*/

            String content = getJsonStr(new ReadDatabase(context).readDatabaseText(),checkedmap,type);

            if(content.equals(""))
            {
                Log.d("错误", "Manager31转换json错误");
                return;
            }
            LogUtil.e("",content);
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
            Log.d("responseData", responseData);

            Message msg = new Message();
            msg.what=31;
            JSONTokener jsonTokener = new JSONTokener(responseData);
            msg.obj=((JSONObject)jsonTokener.nextValue()).getString("code");

            handler.sendMessage(msg);
            return;


        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Log.d("response", "连接网络失败");
            Message msg = new Message();
            msg.what=31;
            msg.obj = "2";
            handler.sendMessage(msg);




        }
        Looper.loop();
    }


}
