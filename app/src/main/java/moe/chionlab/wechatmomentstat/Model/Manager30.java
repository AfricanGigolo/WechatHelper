package moe.chionlab.wechatmomentstat.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.Share;

/**
 * Created by chenjunfan on 2017/3/22.
 */

public class Manager30 {
    private Context context;

    public Manager30(Context context) {
        this.context = context;
    }


    public ArrayList<Map<String, Object>> getAlreadyRecord(ArrayList<Map<String, Object>> oldGroupList,Map<String,Object> checkedMap,int type)
    //type = 1时表示手动上传群组,type = 2时表示自动上传群组；
    {
        LogUtil.e("oldgrouplist1", new Gson().toJson(oldGroupList));
        List<Map<String, Object>> groups = new ArrayList<>();
        for (int i = 0; i < oldGroupList.size(); i++) {
            Map<String, Object> oldmap = oldGroupList.get(i);
            Map<String, Object> newmap = new HashMap<>();
            newmap.put("group", oldmap.get("group"));
            newmap.put("name", oldmap.get("name"));
            groups.add(newmap);

        }
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("groups", groups);
        datamap.put("id", NowUser.id);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", 30);
        jsonMap.put("data", datamap);
        String str = new Gson().toJson(jsonMap);
        final String urlPath = Share.IP_ADDRESS
                + "/ChatDetection/uploadServlet";
        Log.d("url", urlPath);
        URL url;
        String responseData = "";
        try {
            url = new URL(urlPath);
/*封装子对象*/

            String content = str;

            if (content.equals("")) {
                Log.d("错误", "Manager30转换json错误");
                return null;
            }
            LogUtil.e("", content);
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

            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            LogUtil.e("responseData30", responseData);


        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            Log.d("response30", "连接网络失败");
            return null;
        }

        if (responseData.equals("")) {
            return null;
        }

        JSONTokener jsonTokener = new JSONTokener(responseData);
        List<Map<String, Object>> timestampList = null;

        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONObject jsdata = jsonObject.getJSONObject("data");
            JSONArray jsgroups = jsdata.getJSONArray("groups");
            Gson gson = new Gson();
            timestampList = gson.fromJson(jsgroups.toString(), new TypeToken<List<Map<String, Object>>>() {
            }.getType());


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Manager30", "解析json失败");
            return null;
        }


        if (timestampList == null) {
            return null;
        }

        for (int i = 0; i < oldGroupList.size(); i++) {
            Map<String, Object> oldgroupmap = oldGroupList.get(i);
            for (int j = 0; j < timestampList.size(); j++) {
                Map<String, Object> newgroupmap = timestampList.get(j);
                if (!(newgroupmap.get("group").toString().equals(oldgroupmap.get("group").toString())))
                    continue;
                //Log.d("find"+j,newgroupmap.get("group").toString()+" "+oldgroupmap.get("group"));
                List<Map<String, Object>> oldRecordList = (List<Map<String, Object>>) oldgroupmap.get("chat_records");
                Long maxtimestamp = Long.parseLong(newgroupmap.get("timestamp").toString());
                for (int k = 0; k < oldRecordList.size(); k++) {
                    Map<String, Object> oldmap = oldRecordList.get(k);
//                    int flag=0;
//                    for(int l = 0;l<newTimestampList.size();l++)
//                    {
//                       // Log.d("timestamps",newTimestampList.get(l).toString());
//                        if(oldmap.get("timestamp").toString().equals(newTimestampList.get(l).toString()))
//                        {
//                            flag =1;
//                            oldRecordList.remove(k);
//                            Log.d("oldRecordList"+k, "remove");
//                            k--;
//                            break;
//                        }
//                        else
//                            continue;
//                    }
//                    if(flag==0)
//                    {
//                        Log.d("notfind",k+"");
//                    }
                    if (Long.parseLong(oldmap.get("timestamp").toString()) <= maxtimestamp) {
                        oldRecordList.remove(k);
                        k--;
                    }
                }
                oldgroupmap.put("chat_records", oldRecordList);
            }
            oldGroupList.set(i, oldgroupmap);
            Log.d("oldGroupList", "set");


            LogUtil.e("oldgrouplist2", new Gson().toJson(oldGroupList));




        }
        //手动上传：
        if(type == 1)
        {
            List<String> checkedlist = (List<String>) checkedMap.get("checkedlist");
            Long maxtime = Long.parseLong(checkedMap.get("maxtime").toString());
            Long mintime = Long.parseLong(checkedMap.get("mintime").toString());

            for(int i =0;i<oldGroupList.size();i++)//丢弃不在选中群组列表中的群组；
            {
                Map<String,Object> groupmap = oldGroupList.get(i);
                boolean flag = false;
                for(int j = 0;j<checkedlist.size();j++)
                {
                    String groupid = checkedlist.get(j);
                    if(groupmap.get("group").equals(groupid))
                    {
                        flag = true;
                        break;
                    }
                }
                if(flag == false)
                {
                    oldGroupList.remove(i);
                    i--;
                }
            }

            for(int i = 0;i<oldGroupList.size();i++)//丢弃不在选中范围内的聊天记录
            {
                Map<String,Object> groupmap = oldGroupList.get(i);
                List<Map<String,Object>> recordlist = (List<Map<String, Object>>) groupmap.get("chat_records");
                for(int j = 0;j<recordlist.size();j++)
                {
                    Map<String,Object> recordmap = recordlist.get(j);
                    Long curttentime = Long.parseLong(recordmap.get("timestamp").toString());
                    if(curttentime<mintime||curttentime>maxtime)
                    {
                        recordlist.remove(j);
                        j--;
                    }
                }
                groupmap.put("chat_records",recordlist);
                oldGroupList.set(i,groupmap);

            }
        }
        return oldGroupList;
    }
}

