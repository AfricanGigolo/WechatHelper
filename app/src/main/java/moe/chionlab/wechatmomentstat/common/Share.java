package moe.chionlab.wechatmomentstat.common;


import java.util.ArrayList;
import java.util.List;

import moe.chionlab.wechatmomentstat.Model.Fm1Itembean;
import moe.chionlab.wechatmomentstat.Model.Fm2Itembean;
import moe.chionlab.wechatmomentstat.SnsStat;

/**
 * Created by chiontang on 3/23/16.
 */
public class Share {
    static public SnsStat snsData = null;
    static public String databasePassword=null;
    static public String IP_ADDRESS = "http://192.168.1.116:8080";
    static public List<Fm1Itembean> fm1ItembeanList = new ArrayList<>();
    static public List<Fm2Itembean> fm2ItembeanList = new ArrayList<>();
    static public List<Fm1Itembean> fm3allList = new ArrayList<>();
    static public List<Fm1Itembean> fm3addedList = new ArrayList<>();
}
