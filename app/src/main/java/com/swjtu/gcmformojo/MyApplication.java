package com.swjtu.gcmformojo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.support.multidex.MultiDex;

import com.huawei.android.hms.agent.HMSAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

/**
 * 用于存储全局变量
 * Created by HeiPi on 2017/2/24.
 */

public class MyApplication extends Application {

    final public static String MYTAG = "GcmForMojo";
    final public static String PREF = "com.swjtu.gcmformojo_preferences";
    final public static String QQ="Mojo-Webqq";
    final public static String WEIXIN="Mojo-Weixin";
    final public static String SYS="Mojo-Sys";
    final public static String KEY_TEXT_REPLY="key_text_reply";


    final public static String mi_APP_ID = "2882303761517557334";
    final public static String mi_APP_KEY = "5631755784334";

    final public static String fm_APP_ID = "110370";
    final public static String fm_APP_KEY = "38b8c46a27c84d3881a41adf8aceb6f8";

    final public static String qqColor="#1296DB";
    final public static String wxColor="#62B900";

    public static SharedPreferences mySettings;
    public static SharedPreferences miSettings;

    public static String deviceGcmToken;
    public static String deviceMiToken;
    public static String deviceHwToken;
    public static String deviceFmToken;

    public static int isQqOnline = 1;
    public static int isWxOnline = 1;

    private final Map<String, List<Spanned>> msgSave = new HashMap<>();
    private final Map<Integer, Integer> msgCountMap = new HashMap<>();
    private final Map<String, Integer> msgIdMap = new HashMap<>();
    private final ArrayList<User> currentUserList = new ArrayList<>();

    private final ArrayList<QqFriend> qqFriendArrayList = new ArrayList<>();
    private final ArrayList<QqFriendGroup> qqFriendGroups= new ArrayList<>();

    private final ArrayList<WechatFriend> WechatFriendArrayList = new ArrayList<>();
    private final ArrayList<WechatFriendGroup> WechatFriendGroups= new ArrayList<>();

    private static MyApplication myApp;

    public static MyApplication getInstance() {
        return myApp;
    }


    public Map<String, List<Spanned>> getMsgSave () {
        return this.msgSave;
    }

    public Map<Integer, Integer> getMsgCountMap () {
        return this.msgCountMap;
    }

    public Map<String, Integer> getMsgIdMap () {
        return this.msgIdMap;
    }

    public ArrayList<User> getCurrentUserList () {
        return this.currentUserList;
    }

    public ArrayList<QqFriend> getQqFriendArrayList () {
        return this.qqFriendArrayList;
    }

    public ArrayList<QqFriendGroup> getQqFriendGroups () {
        return this.qqFriendGroups;
    }

    public ArrayList<WechatFriend> getWechatFriendArrayList () {
        return this.WechatFriendArrayList;
    }

    public ArrayList<WechatFriendGroup> getWechatFriendGroups () {
        return this.WechatFriendGroups;
    }


    public static String getCurTime(){

        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static String getColorMsgTime(String messageType,Boolean isSend){

        String    str    =    "";
        if(!isSend) {
            if(messageType.equals(QQ)){
                str    =    "<font color='"+qqColor+"'><small>"+ getCurTime()+"</small></font><br>";
            }else if(messageType.equals(WEIXIN)){
                str    =    "<font color='"+wxColor+"'><small>"+ getCurTime()+"</small></font><br>";
            }
        }else {
            if(messageType.equals(QQ)){
                str    =    "<font color='"+wxColor+"'><small>"+ getCurTime()+"</small></font><br>";
            }else if(messageType.equals(WEIXIN)){
                str    =    "<font color='"+qqColor+"'><small>"+ getCurTime()+"</small></font><br>";
            }
        }
        return str;
    }

    /**
     *  转换文字格式
     *
     *
     */

    public  static Spanned toSpannedMessage(String message){

        Spanned tmpMsg;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tmpMsg= Html.fromHtml(message,FROM_HTML_MODE_COMPACT);
        } else {
            //noinspection deprecation
            tmpMsg=Html.fromHtml(message);
        }

        return tmpMsg;

    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //初始化全局变量
        myApp = this;
        miSettings = getSharedPreferences("mipush", Context.MODE_PRIVATE);
        mySettings = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        //华为推送初始化
        String pushType=mySettings.getString("push_type","GCM");
        if(pushType.equals("HwPush")){
            HMSAgent.init(this);
        }

    }

    // 提取微信UID中的数字并进行字符数量削减，以兼容notifyID
    // created by Alex Wang at 20180205
    public static int WechatUIDConvert(String UID) {
        String str = UID;
        str=str.trim();
        String str2="";
        if(str != null && !"".equals(str)){
            for(int i=0;i<str.length();i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        //针对系统账号进行优化，以防闪退
        switch (str) {
            case "newsapp":
                str2 = "639727700";
                break;
            case "filehelper":
                str2 = "345343573";
                break;
        }
        return Integer.parseInt(str2.substring(0,9));
    }

    //为Kitkat及更低版本启用multidex支持
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
