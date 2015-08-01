package com.tx.hsp.pingbobo.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tx.hsp.pingbobo.MyApplication;
import com.tx.hsp.pingbobo.R;
import com.tx.hsp.pingbobo.bean.users.BeanShow;
import com.tx.hsp.pingbobo.network.VolleySingleton;

import org.json.JSONObject;

import java.util.BitSet;


public class ActivityUser extends ActionBarActivity {

    private ImageView userHead;
    private TextView userName;

    /**
     * 获取用户信息的URL
     */
    private String URL = "https://api.weibo.com/2/users/show.json";

    /**
     * 网络反馈handler msg
     */
    private final int MSG_GET_JSON_SUCCESS = 1;
    private final int MSG_GET_JSON_FAILED = 2;
    private final int MSG_GET_IMAGE_SUCCESS = 3;
    private final int MSG_GET_IMAGE_FAILED = 4;

    /**
     * 创建一个bean用来装载反馈回来的数据
     *
     */
    private BeanShow beanShow;

    /**
     * 创建一个gson工具用来解析response数据
     * 打包进beanshow容器
     */
    private Gson gson;

    /**
     *  创建volley引用
     *  指向单例
     */
    private VolleySingleton volley;

    /**
     * 创建用户图片引用
     */
    private Bitmap userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userHead = (ImageView)findViewById(R.id.imageView);
        userName = (TextView)findViewById(R.id.useName);
        gson = new Gson();

        volley = VolleySingleton.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_user, menu);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        volley.getRequestQueue().add(request);

    }


    /**
     *  创建handler
     *  用于接收msg及更新ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_GET_IMAGE_FAILED:
                    break;
                case MSG_GET_IMAGE_SUCCESS:
                    userHead.setImageBitmap(userImage);
                    break;
                case MSG_GET_JSON_FAILED:
                    break;
                case MSG_GET_JSON_SUCCESS:
                    userName.setText(beanShow.screen_name);
                    volley.getImageLoader().get(beanShow.avatar_large, imageListener);
                    break;
            }
        }
    };

    /**
     * 创建请求用户数据的request
     */
    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + "?access_token=" + MyApplication.getToken() + "&uid=" + MyApplication.getUid(), null
            , new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
                beanShow = gson.fromJson(response.toString(),BeanShow.class);
                handler.sendEmptyMessage(MSG_GET_JSON_SUCCESS);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                handler.sendEmptyMessage(MSG_GET_JSON_FAILED);
        }
    });

    /**
     * 创建请求用户头像的listener
     */
    ImageLoader.ImageListener imageListener= new ImageLoader.ImageListener() {
        @Override
        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
            userImage = response.getBitmap();
            handler.sendEmptyMessage(MSG_GET_IMAGE_SUCCESS);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            handler.sendEmptyMessage(MSG_GET_IMAGE_FAILED);
        }
    };
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
