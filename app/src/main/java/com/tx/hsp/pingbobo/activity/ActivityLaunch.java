package com.tx.hsp.pingbobo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.tx.hsp.pingbobo.Constants;
import com.tx.hsp.pingbobo.MyApplication;
import com.tx.hsp.pingbobo.R;
import com.tx.hsp.pingbobo.Utils.L;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ActivityLaunch extends ActionBarActivity {

    /**
     * WeiboSDKDemo 程序的 APP_SECRET。
     * 请注意：请务必妥善保管好自己的 APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    private static final String WEIBO_DEMO_APP_SECRET = "77e5c7b280ee24203110dc6c5bec2570";

    /**
     * 通过 code 获取 Token 的 URL
     */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launch);
        Oauth2AccessToken token = MyApplication.getOauth();
        String uid = MyApplication.getUid();
        L.v("Token : "+token);
        fetchTokenAsync(MyApplication.getCode(), WEIBO_DEMO_APP_SECRET);
        if(token==null||uid=="null"){
            startActivity(new Intent(this,ActivitySignIn.class));
            finish();
        }else{
            startActivity(new Intent(this,ActivityHome.class));
            finish();
        }
    }
    /**
     * 异步获取 Token。
     *
     * @param authCode  授权 Code，该 Code 是一次性的，只能被获取一次 Token
     * @param appSecret 应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
     *                  不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    public void fetchTokenAsync(String authCode, String appSecret) {
        /*
        LinkedHashMap<String, String> requestParams = new LinkedHashMap<String, String>();
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID,     Constants.APP_KEY);
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecretConstantS.APP_SECRET);
        requestParams.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
        requestParams.put(WBConstants.AUTH_PARAMS_CODE,          authCode);
        requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
        */
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID, Constants.APP_KEY);
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
        requestParams.add(WBConstants.AUTH_PARAMS_GRANT_TYPE, "authorization_code");
        requestParams.add(WBConstants.AUTH_PARAMS_CODE, authCode);
        requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL, Constants.REDIRECT_URL);

        /**
         * 请注意：
         * {@link RequestListener} 对应的回调是运行在后台线程中的，
         * 因此，需要使用 Handler 来配合更新 UI。
         */
        AsyncWeiboRunner.request(OAUTH2_ACCESS_TOKEN_URL, requestParams, "POST", new RequestListener() {
            @Override
            public void onComplete(String response) {
                L.v("Response: " + response);

                // 获取 Token 成功
                Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(response);
                MyApplication.setOauth(response);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {
                L.v("onComplete4binary...");

            }

            @Override
            public void onIOException(IOException e) {
                L.v("onIOException： " + e.getMessage());

            }

            @Override
            public void onError(WeiboException e) {
                L.v("WeiboException： " + e.getMessage());

            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(ActivityLaunch.this,ActivityHome.class));
            ActivityLaunch.this.finish();
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_launch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
