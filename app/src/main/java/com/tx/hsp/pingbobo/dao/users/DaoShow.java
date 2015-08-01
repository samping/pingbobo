package com.tx.hsp.pingbobo.dao.users;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.tx.hsp.pingbobo.MyApplication;
import com.tx.hsp.pingbobo.bean.users.BeanShow;
import com.tx.hsp.pingbobo.network.VolleySingleton;

import org.json.JSONObject;

/**
 * Created by hsp on 15/7/28.
 */
public class DaoShow {
    /**
     * 获取用户信息的URL
     */
    private String URL = "https://api.weibo.com/2/users/show.json";

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

    private Listener listener;

    /**
     *
     * @param l 为调用者invoke
     */
    public DaoShow(Listener l){
        gson = new Gson();
        this.listener = l;
        volley = VolleySingleton.getInstance();
    }


    /**
     * 用于执行request
     */
    public void excute(){
        volley.getRequestQueue().add(request);
    }
    /**
     * 创建请求用户数据的request
     */
    private JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + "?access_token=" + MyApplication.getToken() + "&uid=" + MyApplication.getUid(), null
            , new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            beanShow = gson.fromJson(response.toString(),BeanShow.class);
            listener.onResponse(beanShow);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    });


    public interface Listener{
        public void onResponse(BeanShow beanShow);
    }
}
