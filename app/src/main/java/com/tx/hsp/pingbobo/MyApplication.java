package com.tx.hsp.pingbobo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tx.hsp.pingbobo.Utils.L;

/**
 * Created by hsp on 15/7/25.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

//    private static String Token;

    private static final String FILE_NAME = "User";
    private static final String TOKEN = "Token";
    private static final String CODE = "Code";
    private static final String UID = "Uid";
    private static final String ICON_URL="Icon_url";

    private static final String Oauth = "";


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        L.v("on application create");
    }



    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();

    }

    public static String getToken(){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(TOKEN, "null");
    }

    public static void setToken(String t){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN,t);
        editor.commit();
    }

    public static String getCode(){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(CODE,"null");
    }

    public static void setCode(String c){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(CODE,c);
        editor.commit();
    }

    public static String getUid(){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(UID,"null");
    }

    public static void setUid(String u){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(UID,u);
        editor.commit();
    }

    public static String getIconUrl(){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(ICON_URL,"null");
    }

    public static void setIconUrl(String u){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ICON_URL,u);
        editor.commit();
    }

    public static void setOauth(String t){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Oauth,t);
        editor.commit();
    }
    public static Oauth2AccessToken getOauth(){
        SharedPreferences sharedPref = sInstance.getApplicationContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        // 获取 Token 成功
        Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(sharedPref.getString(Oauth,"null"));
        return token;
    }
}
