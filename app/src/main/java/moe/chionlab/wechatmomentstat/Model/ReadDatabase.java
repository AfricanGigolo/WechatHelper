package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.chionlab.wechatmomentstat.Config;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.SnsStat;
import moe.chionlab.wechatmomentstat.Task;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/3/15.
 */

public class ReadDatabase {
    private Context context;
    private SQLiteDatabase db = null;
    private Cursor c = null;

    private List<Map<String, Object>> recordList;

    public ReadDatabase(Context context) {
        this.context = context;
    }

    public List<Map<String, Object>> readDatabaseText() {

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
            Share.databasePassword = password;
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

                    map.put("content", content);
                    map.put("timestamp", createTime);
                    map.put("order", _id + "");
//                    if (status.equals("2")) {
//                        map.put("name", "我");
//
//                    } else {
                        map.put("name", "");
//                    }
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


//        for (int j = 0; j < recordList.size(); j++) {
//            Map<String, Object> map = recordList.get(j);
//
//            Log.d(j + " ", map.get("order") + " " + map.get("sender") + " " + map.get("name") + " " + map.get("content"));
//
//        }
        Log.d("MymainActivity", "读取数据库完");

       return recordList;
    }

    public List<Map<String, Object>> readSnsDatabaseText()
    {
        Task task = null;
        SnsStat snsStat = null;

        task = new Task(context.getApplicationContext());


        try {
            task.copySnsDB();
            task.initSnsReader();
            task.snsReader.run();
            snsStat = new SnsStat(task.snsReader.getSnsList());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Toast.makeText(context, "设备没有root权限，请获取root权限再试", Toast.LENGTH_LONG).show();
            Log.e("wechatmomentstat", "exception");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        //String str = "";
        try {
            for(int i=0;i<snsStat.snsList.size();i++) {
                Map<String,Object> map = new HashMap<>();
                SnsInfo snsInfo = snsStat.snsList.get(i);

                String str =  "第" + (i + 1) + "条:\n"
                        + "用户名:" + snsInfo.authorId
                        + "\n昵称:" + snsInfo.authorName
                        + "\n时间:" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date(snsInfo.timestamp * 1000)) +
                        "\n内容:" + snsInfo.content + "\n\n";

                Log.d("ReadDatabase"+i, str);
                map.put("code",0);
                map.put("sender",snsInfo.authorId);
                map.put("name",snsInfo.authorName);
                map.put("timestamp",snsInfo.timestamp);
                map.put("order",0);
                Map<String,Object> textmap = new HashMap<>();
                textmap.put("text",snsInfo.content);
                map.put("data",textmap);
                dataList.add(map);
        }



        }
        catch (Exception e)
        {

        }
        //Log.d("Snsdata", str);
        return dataList;
    }

}
