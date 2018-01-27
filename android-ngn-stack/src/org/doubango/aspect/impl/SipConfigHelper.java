package org.doubango.aspect.impl;

import android.content.Context;

import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.utils.SharedPreferenceUtil;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by lenovo on 2017/9/6.
 */

public class SipConfigHelper {

    private static final String TAG = SipConfigHelper.class.getCanonicalName();
    public static final String USE_SPEAKER = "USER_SPEAKER." + TAG;
    public static final String CURRENT_GROUP = "CURRENT_GROUP." + TAG;
    public static final boolean DEFAULT_USE_SPEAKER = false;
    public static final String DEFAULT_CURRENT_GROUP = "";

    public static String IDENTITY_IMPU="";
    public static String IDENTITY_DISPLAY_NAME="";
    public  static String IDENTITY_CAMERA_GROUP="";
    public  static  final String IDENTITY_PASSWORD= "123";
    public  static  final String NETWORK_REALM= "doubango.org";
        public  static   String NETWORK_PCSCF_HOST="192.168.1.240";
        public  static   int NETWORK_PCSCF_PORT= 5060;
//    public  static   String NETWORK_PCSCF_HOST="183.3.136.86";
//    public  static   int NETWORK_PCSCF_PORT= 9960;


    public  static   String NETWORK_PCSCF_HOST_L="192.168.1.240";
    public  static   int NETWORK_PCSCF_PORT_L= 5060;

    public  static   String NETWORK_PCSCF_HOST_N="183.3.136.86";
    public  static   int NETWORK_PCSCF_PORT_N= 9960;


    public  static  final String SIPSERVER_IMPU= "pttservice"; //服务器接收消息的账号

    public static final String KEY_CAMERA_GROUP = "camera_group";

    public static void initConfig(INgnConfigurationService goal, Context context, String username, String ipphone) {
        IDENTITY_IMPU = ipphone == null ? IDENTITY_IMPU : ipphone;
        IDENTITY_DISPLAY_NAME= username == null ? IDENTITY_IMPU : username;
        IDENTITY_CAMERA_GROUP = ipphone == null ? IDENTITY_CAMERA_GROUP : ipphone.replaceFirst("1","8");
        goal.putString(
                IDENTITY_DISPLAY_NAME, SharedPreferenceUtil.getString(context, IDENTITY_DISPLAY_NAME, username));
        goal.putString(NgnConfigurationEntry.IDENTITY_IMPU,
                String.format("sip:%s@%s",IDENTITY_IMPU,NETWORK_REALM));
        goal.putString(NgnConfigurationEntry.IDENTITY_IMPI,
                SharedPreferenceUtil.getString(context, NgnConfigurationEntry.IDENTITY_IMPI, IDENTITY_IMPU));
        goal.putString(NgnConfigurationEntry.IDENTITY_PASSWORD,
                SharedPreferenceUtil.getString(context, NgnConfigurationEntry.IDENTITY_PASSWORD,IDENTITY_PASSWORD));
        goal.putString(NgnConfigurationEntry.NETWORK_REALM,
                SharedPreferenceUtil.getString(context, NgnConfigurationEntry.NETWORK_REALM, NETWORK_REALM));
        goal.putBoolean(NgnConfigurationEntry.NETWORK_USE_EARLY_IMS,
                SharedPreferenceUtil.getBoolean(context, NgnConfigurationEntry.NETWORK_USE_EARLY_IMS, true));
        goal.putBoolean(NgnConfigurationEntry.GENERAL_FULL_SCREEN_VIDEO, false);
        goal.putString(NgnConfigurationEntry.NETWORK_PCSCF_HOST,
                SharedPreferenceUtil.getString(context, NgnConfigurationEntry.NETWORK_PCSCF_HOST,NETWORK_PCSCF_HOST));

        goal.putInt(NgnConfigurationEntry.NETWORK_PCSCF_PORT,
                SharedPreferenceUtil.getInt(context, NgnConfigurationEntry.NETWORK_PCSCF_PORT, NETWORK_PCSCF_PORT));
        goal.putBoolean(NgnConfigurationEntry.NETWORK_USE_WIFI,
                SharedPreferenceUtil.getBoolean(context, NgnConfigurationEntry.NETWORK_USE_WIFI, true));

        goal.putBoolean(NgnConfigurationEntry.NETWORK_USE_3G,
                SharedPreferenceUtil.getBoolean(context, NgnConfigurationEntry.NETWORK_USE_3G, true));

        goal.putBoolean(USE_SPEAKER,DEFAULT_USE_SPEAKER);
        // 固定值？
        goal.putInt(NgnConfigurationEntry.MEDIA_CODECS,
                NgnConfigurationEntry.DEFAULT_MEDIA_CODECS);

        goal.commit();

    }

    public static void updateConfig(INgnConfigurationService goal, String key, String value) {
        goal.putString(key, value);
        goal.commit();
    }

    public static void updateConfig(INgnConfigurationService goal, String key, int value) {
        goal.putInt(key, value);
        goal.commit();
    }

    /**
     * 判断TCP是否连接
     * @param ip
     * @param port
     * @return
     */
    public static boolean isConnTcp(String ip,int port){
        boolean isConn = false;
        try {
            Socket socket  = new Socket();
            SocketAddress socAddress = new InetSocketAddress(ip, port);
            socket.connect(socAddress, 1000);//超时2秒
            isConn=socket.isConnected();
            socket.close();
        }  catch (Exception e) {
            isConn=false;
        }
        return isConn;
    }


}
