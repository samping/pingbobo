package com.tx.hsp.pingbobo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.UIUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class MainActivity extends ActionBarActivity {


    String TAG = "hsp";

    /**
     * WeiboSDKDemo 程序的 APP_SECRET。
     * 请注意：请务必妥善保管好自己的 APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    private static final String WEIBO_DEMO_APP_SECRET = "77e5c7b280ee24203110dc6c5bec2570";

    /**
     * 通过 code 获取 Token 的 URL
     */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";

    String Token = "https://api.weibo.com/2/statuses/home_timeline.json";


    /**
     * 获取 Token 成功或失败的消息
     */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED = 2;

    /**
     * 微博 Web 授权接口类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;
    /**
     * 获取到的 Code
     */
    private String mCode;
    /**
     * 获取到的 Token
     */
    private Oauth2AccessToken mAccessToken;


    Button button, button2;
    TextView textView, mTokenText;

    RequestQueue mQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mQueue = Volley.newRequestQueue(this);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           JsonObjectRequest jr= new JsonObjectRequest(Request.Method.GET,Token+"?access_token="+mAccessToken.getToken(),null,
                                                   new Response.Listener<JSONObject>() {
                                                       @Override
                                                       public void onResponse(JSONObject response) {
                                                            Log.v("hsp",response.toString());
                                                       }
                                                   }, new Response.ErrorListener() {
                                               @Override
                                               public void onErrorResponse(VolleyError error) {
                                                   Log.v("hsp", error.getMessage());
                                               }
                                           });
                                           mQueue.add(jr);
                                       }
                                   }
        );

        textView = (TextView) findViewById(R.id.textView);
        mTokenText = (TextView) findViewById(R.id.textView2);

        // 初始化微博对象
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        // 第一步：获取 Code
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboAuth.authorize(new AuthListener(), WeiboAuth.OBTAIN_AUTH_CODE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            if (null == values) {
                Toast.makeText(MainActivity.this,
                        R.string.weibosdk_demo_toast_obtain_code_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            String code = values.getString("code");
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(MainActivity.this,
                        R.string.weibosdk_demo_toast_obtain_code_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            mCode = code;
            textView.setText(code);
//            mAuthCodeButton.setEnabled(true);
//            mTokenText.setText("");
            Toast.makeText(MainActivity.this,
                    R.string.weibosdk_demo_toast_obtain_code_success, Toast.LENGTH_SHORT).show();
            fetchTokenAsync(mCode, WEIBO_DEMO_APP_SECRET);
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UIUtils.showToast(MainActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }


    /**
     * 该 Handler 配合 {@link RequestListener} 对应的回调来更新 UI。
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FETCH_TOKEN_SUCCESS:
                    // 显示 Token
                    String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                            new java.util.Date(mAccessToken.getExpiresTime()));
                    String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                    mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
//                    mAuthCodeButton.setEnabled(false);

                    Toast.makeText(MainActivity.this,
                            R.string.weibosdk_demo_toast_obtain_token_success, Toast.LENGTH_SHORT).show();
                    break;

                case MSG_FETCH_TOKEN_FAILED:
                    Toast.makeText(MainActivity.this,
                            R.string.weibosdk_demo_toast_obtain_token_failed, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

        ;
    };

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
                LogUtil.d(TAG, "Response: " + response);

                // 获取 Token 成功
                Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(response);
                if (token != null && token.isSessionValid()) {
                    LogUtil.d(TAG, "Success! " + token.toString());

                    mAccessToken = token;
                    mHandler.obtainMessage(MSG_FETCH_TOKEN_SUCCESS).sendToTarget();
                } else {
                    LogUtil.d(TAG, "Failed to receive access token");
                }
            }

            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {
                LogUtil.e(TAG, "onComplete4binary...");
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }

            @Override
            public void onIOException(IOException e) {
                LogUtil.e(TAG, "onIOException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }

            @Override
            public void onError(WeiboException e) {
                LogUtil.e(TAG, "WeiboException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
        });
    }
/**
 *
 */
}
