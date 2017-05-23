package moe.chionlab.wechatmomentstat.Model;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

/**
 * Created by chenjunfan on 2017/5/22.
 */

public class SPlist {
    public List rightlist;
    public List propertylist;

    public SPlist() {
        rightlist = new ArrayList<String>();
        propertylist = new ArrayList<String>();
        rightlist.add("敏感词");
        rightlist.add("1级词");
        rightlist.add("2级词");
        rightlist.add("3级词");
        rightlist.add("4级词");
        rightlist.add("5级词");
        rightlist.add("6级词");
        rightlist.add("7级词");
        rightlist.add("8级词");
        rightlist.add("9级词");

        propertylist.add("名词");
        propertylist.add("时间词");
        propertylist.add("处所词");
        propertylist.add("方位词");
        propertylist.add("动词");
        propertylist.add("形容词");
        propertylist.add("区别词");
        propertylist.add("状态词");
        propertylist.add("代词");
        propertylist.add("数词");
        propertylist.add("量词");
        propertylist.add("副词");
        propertylist.add("介词");
        propertylist.add("连词");
        propertylist.add("助词");
        propertylist.add("叹词");         propertylist.add("名词");
        propertylist.add("语气词");         propertylist.add("名词");
        propertylist.add("拟声词");
        propertylist.add("前缀");
        propertylist.add("后缀");
        propertylist.add("字符串");
        propertylist.add("标点符号");
    }

    public String getcode(String str)
    {
        switch (str)
        {
            case "敏感词":
                return "-1";
            case "1级词":
                return "1";
            case "2级词":
                return "2";
            case "3级词":
                return "3";
            case "4级词":
                return "4";
            case "5级词":
                return "5";
            case "6级词":
                return "6";
            case "7级词":
                return "7";
            case "8级词":
                return "8";
            case "9级词":
                return "9";
            case "名词":
                return "n";
            case "时间词":
                return "t";
            case "处所词":
                return "s";
            case "方位词":
                return "f";
            case "动词":
                return "v";
            case "形容词":
                return "a";
            case "区别词":
                return "b";
            case "状态词":
                return "z";
            case "代词":
                return "r";
            case "数词":
                return "m";
            case "量词":
                return "q";
            case "副词":
                return "d";
            case "介词":
                return  "p";
            case "连词":
                return "c";
            case "助词":
                return "u";
            case "叹词":
                return "e";
            case "语气词":
                return "y";
            case "拟声词":
                return "o";
            case "前缀":
                return "h";
            case "后缀":
                return "k";
            case "字符串":
                return "x";
            case "标点符号":
                return "w";
            default:
                return "错误";
        }
    }

    public String getstr(String str) {
        switch (str) {
            case "-1":
                return "敏感词";
            case "1":
                return "1级词";
            case "2":
                return "2级词";
            case "3":
                return "3级词";
            case "4":
                return "4级词";
            case "5":
                return "5级词";
            case "6":
                return "6级词";
            case "7":
                return "7级词";
            case "8":
                return "8级词";
            case "9":
                return "9级词";
            case "n":
                return "名词";
            case "t":
                return "时间词";
            case "s":
                return "处所词";
            case "f":
                return "方位词";
            case "v":
                return "动词";
            case "a":
                return "形容词";
            case "b":
                return "区别词";
            case "z":
                return "状态词";
            case "r":
                return "代词";
            case "m":
                return "数词";
            case "q":
                return "量词";
            case "d":
                return "副词";
            case "p":
                return "介词";
            case "c":
                return "连词";
            case "u":
                return "助词";
            case "e":
                return "叹词";
            case "y":
                return "语气词";
            case "o":
                return "拟声词";
            case "h":
                return "前缀";
            case "k":
                return "后缀";
            case "x":
                return "字符串";
            case "w":
                return "标点符号";
            default:
                return "错误";
        }
    }
        public  void setSpinnerItemSelectedByValue(Spinner spinner, String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
//                spinner.setSelection(i,true);// 默认选中项
                spinner.setSelection(i);// 默认选中项

                break;
            }
        }

    }
    }



