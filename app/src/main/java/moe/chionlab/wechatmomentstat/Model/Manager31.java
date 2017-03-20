package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.json.JSONObject;

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
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.Task;
import moe.chionlab.wechatmomentstat.common.CommonList;
import moe.chionlab.wechatmomentstat.common.NowUser;
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

    private String getJsonStr(List<Map<String, Object>> recordList) {
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
            db = SQLiteDatabase.openOrCreateDatabase(Config.EXT_DIR + "/EnMicroMsg.db",Share.databasePassword, null, hook);


            for (int i = 0; i < recordList.size(); i++) {
                Map<String, Object> map = recordList.get(i);

                if (map.get("sender").toString().contains("chatroom")) {
                    map.put("chatroomid", map.get("sender"));
                    map.remove("sender");
                    String temp = map.get("content").toString();
                    int end = temp.indexOf(':');

                    if(end!=-1&&end<=20)
                    {
                        temp = temp.substring(0,end);
                    }
                    else {
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
                        if(end!=-1&&end<=20)
                        {
                            map.put("chatroomname", map.get("name"));
                            map.remove("name");
                            map.put("name", c.getString(c.getColumnIndex("nickname")));
                        }


                    }
                    else
                    {
                        map.put("chatroomname",map.get("name"));
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

            for(int i =0;i<recordList.size();i++)
            {
                Map<String, Object> map = recordList.get(i);
                //Log.d("record"+i," "+map.get("chatroomid")+" "+map.get("chatroomname"));

            }
            Log.d("record","替换chatroom信息完成" );

            //将所有recordList中的chatroom名字列出
            ArrayList<String> chatroomIdList = new ArrayList<String>();
            boolean flag = true;
            for(int i=0;i<recordList.size();i++)
            {

                Map<String,Object> map = recordList.get(i);
                flag =false;
                for(int j=0;j<chatroomIdList.size();j++)
                {
                    if(map.get("chatroomid").toString().equals(chatroomIdList.get(j)))
                    {
                        flag = true;
                        break;
                    }
                }
                if(flag == false)
                {
                    chatroomIdList.add(map.get("chatroomid").toString());
                }
            }

//            for(int i = 0;i<chatroomIdList.size();i++)
//            {
//                Log.d("chatroomIdList", chatroomIdList.get(i));
//
//            }
            Log.d("record","得到chatroomNameList完成" );


            //针对每个名字形成新的dataList，并将当前的dataList和nameList的当前值元素封装成GroupList;

            ArrayList<Map<String,Object>> groupList2 = new ArrayList<Map<String, Object>>();
            for(int i=0;i<chatroomIdList.size();i++)
            {

                ArrayList<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
                String id = chatroomIdList.get(i);
//                Log.d("i", i+"");
                for(int j =0;j<recordList.size();j++)
                {
//                    System.out.println(i+" "+j+"   "+recordList.get(j).get("chatroomid").toString());
                    if(recordList.get(j).get("chatroomid").toString().equals(id))
                    {


                        dataList.add(recordList.get(j));

                    }
                }

//                Log.d("go", "1");
                Map<String,Object> groupmap = new HashMap<String,Object>();
//                Log.d("groupmap"+i,""+dataList.get(0).get("chatroomid"));
//                Log.d("groupmap"+i,""+dataList.get(0).get("chatroomname"));

                for(int j=0;j<dataList.size();j++)
                {

                        groupmap.put("group",dataList.get(j).get("chatroomid"));
                        groupmap.put("name",dataList.get(j).get("chatroomname"));


                }

//                Log.d("go", "2");

                for(int k=0;k<dataList.size();k++)
                {
//                    Log.d("k", "" + k);
                    Map<String,Object> map = dataList.get(k);
//                    Log.d("map"+k, map.get("chatroomid").toString()+" "+map.get("chatroomname"));
                    Map<String,Object> map2 = new HashMap<String,Object>();

//                    map.remove("chatroomid");
//                    map.remove("chatroomname");
                    map2.put("code",map.get("code"));
                    map2.put("sender",map.get("sender"));
                    map2.put("name",map.get("name"));
                    map2.put("timestamp",map.get("timestamp"));
                    map2.put("order",map.get("order"));
                    map2.put("data",map.get("data"));

                    dataList.set(k,map2);
                }
//                Log.d("go", "\n3");

                groupmap.put("chat_records",dataList);
                groupList2.add(groupmap);

                Log.d("groupmap"+i,groupmap.get("group")+" "+groupmap.get("name"));
//                Log.d("go", "4");

            }


            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id", NowUser.id);
            data.put("groups",groupList2);
            Map<String,Object> jsonmap = new HashMap<>();
            jsonmap.put("code",31);
            jsonmap.put("data",data);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("code","31");
//            jsonObject.put("data",data);

            String retvalue =new Gson().toJson(jsonmap);
            Log.d("Manager31.togson", retvalue);
            return retvalue;

        } catch (Exception e) {

        }
        return "";

    }








    public void upload() {

// TODO Auto-generated method stub
        Looper.prepare();
        final String urlPath ="http://192.168.1.116:8080"
        +"/ChatDetection/uploadServlet";
        Log.d("url", urlPath);
        URL url;
        try {
            url = new URL(urlPath);
/*封装子对象*/

            String content = getJsonStr(new ReadDatabase(context).readDatabaseText());

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

            Message msg = new Message();
            msg.what=31;
            msg.obj = new JSONObject().getJSONObject(responseData).get("code");
            handler.sendMessage(msg);


        } catch (Exception e) {
// TODO: handle exception
            Log.d("response", "连接网络失败");
            Message msg = new Message();
            msg.what=31;
            msg.obj = "2";
            handler.sendMessage(msg);




        }
        Looper.loop();
    }



    public void getGroupList()
    {
        Task task = new Task(context);
        task.testRoot();
        try {
            task.copyShared_prefs();
            task.copyEnDb();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        String uin = null;
        String password = null;
        Log.d("MymainActivity", "imei:" + imei);
        File sharepreFile = new File(Config.EXT_DIR + "/system_config_prefs.xml");
        try {
            FileInputStream is = new FileInputStream(sharepreFile);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bfr = new BufferedReader(isr);
            String in = "";

            while ((in = bfr.readLine()) != null) {
                Log.d("MymainActivity", in);
                if (in.contains("default_uin")) {
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(in);
                    uin = m.replaceAll("").trim();
                    Log.d("uin", uin);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("MymainActivity", "打开system_config_prefs.xml失败");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (uin != null) {


            password = Md5.getMd5(imei + uin).substring(0, 7);
            Log.d("password", password);


        }


        SQLiteDatabase.loadLibs(context);


        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");
            }
        };
        try {

            db = SQLiteDatabase.openOrCreateDatabase(Config.EXT_DIR + "/EnMicroMsg.db", password, null, hook);

            c = db.query("chatroom",new String[]{"chatroomname"}, null, null, null, null, null);
            groupList = new ArrayList<>();

                while (c.moveToNext()) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    String content = c.getString(c.getColumnIndex("chatroomname"));
                    Log.d("group", content);
                    map.put("chatroomname",content);
                    groupList.add(map);
                }
                c.close();



            for (int i = 0; i < groupList.size(); i++) {
                Map<String, Object> map = groupList.get(i);
                c = db.query("rcontact", new String[]{"username", "nickname"}, "username=?", new String[]{map.get("chatroomname").toString()}, null, null, null);

                if (c.moveToNext()) {
                    //System.out.println("\n find \n");
                    map.put("name", c.getString(c.getColumnIndex("nickname")));
                    groupList.set(i, map);
                }

            }
            c.close();
            db.close();
            CommonList.groupList = groupList;
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);





            for(int i=0;i<groupList.size();i++)
            {
                Map<String, Object> map = groupList.get(i);
                Log.d("chatroom",  map.get("chatroomname").toString()+map.get("name").toString());
            }


        }
         catch (Exception e) {
            e.printStackTrace();
        }


    }


}
