package com.tx.hsp.pingbobo.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;
import com.tx.hsp.pingbobo.R;
import com.tx.hsp.pingbobo.Utils.L;
import com.tx.hsp.pingbobo.adapter.AdapterStatus;
import com.tx.hsp.pingbobo.bean.BeanStatuses;
import com.tx.hsp.pingbobo.network.VolleySingleton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentHome.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TOKEN = "token";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String token;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * 创建recyclerView及相关类
     */
    private AdapterStatus adapterFragmentHome;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private Activity activity;

    private final int MSG_REFRESH_TRUE = 1;
    private final int MSG_REFRESH_FALSE = 2;

    private final String HOME_TIME_URL = "https://api.weibo.com/2/statuses/home_timeline.json";

    /**
     * 创建网络请求
     */
    private JsonObjectRequest request;

    /**
     * 创建网络数据载体
     */
    private BeanStatuses beanStatusList;

    /**
     * 创建网络数据载体解析工具
     */
    private Gson gson;

    /**
     * 创建上下拉layout引用
     */
    private SwipeRefreshLayout refreshLayout;

    /**
     *
     */
    private Oauth2AccessToken token;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(Oauth2AccessToken token) {
        FragmentHome fragment = new FragmentHome(token);
//        Bundle args = new Bundle();
//        args.putString(TOKEN, "");
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    public FragmentHome(Oauth2AccessToken token) {
        this.token = token;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();
        beanStatusList = new BeanStatuses();
//        if (getArguments() != null) {
////            token = getArguments().getString(TOKEN);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//
//
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        L.v("FramentHome onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(onScrollListener);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_home_swipe_refreshlayout);
        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setColorSchemeColors(R.color.refresh);

        addRequest();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        /**
         *  设置recyclerView相关
         */
        layoutManager = new LinearLayoutManager(this.activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 检测下拉
     */
    @Override
    public void onRefresh() {
        addRequest();
    }

    /**
     * 检测上拉
     */
    private RecyclerView.OnScrollListener  onScrollListener = new OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(beanStatusList!=null) {
                if (layoutManager.findLastVisibleItemPosition() == beanStatusList.statuses.size() - 1) {
                    L.v("onScrolled end");
//                dataset[dataset.length-1] = "正在加载...";
//                adapter.notifyItemChanged(dataset.length-1);
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    };
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    /**
     * 创建一个网络请求
     */
    private void addRequest() {
        /**
         * 实例一个网络请求
         */
//        request = new JsonObjectRequest(Request.Method.GET, HOME_TIME_URL + "?access_token=" + token, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        String rsp = response.toString();
//                        L.v(rsp);
//                        beanStatusList = gson.fromJson(rsp,BeanStatuses.class);
//                        L.v("parse success :" +beanStatusList.statuses.get(0).created_at);
//                        /**
//                         * 装载数据
//                         */
//                        adapterFragmentHome = new AdapterStatus(activity,beanStatusList);
//                        refreshLayout.setRefreshing(false);
//                        recyclerView.setAdapter(adapterFragmentHome);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        refreshLayout.setRefreshing(true);
//        VolleySingleton.getInstance().getRequestQueue().add(request);
        StatusesAPI statuesAPI = new StatusesAPI(token);
        UIHandler.sendEmptyMessage(MSG_REFRESH_TRUE);
        statuesAPI.homeTimeline(0, 0, 10, 1, false, WeiboAPI.FEATURE.ALL, false, new RequestListener() {

            @Override
            public void onComplete(String response) {

                L.v(response);
                beanStatusList = gson.fromJson(response, BeanStatuses.class);
                L.v("parse success :" + beanStatusList.statuses.get(0).created_at);
                /**
                 * 装载数据
                 */
                adapterFragmentHome = new AdapterStatus(activity, beanStatusList);
                UIHandler.sendEmptyMessage(MSG_REFRESH_FALSE);

            }

            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {

            }

            @Override
            public void onIOException(IOException e) {

            }

            @Override
            public void onError(WeiboException e) {

            }
        });
    }
    private Handler UIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_REFRESH_TRUE:
                    refreshLayout.setRefreshing(true);
                    break;
                case MSG_REFRESH_FALSE:
                    refreshLayout.setRefreshing(false);
                    recyclerView.setAdapter(adapterFragmentHome);
                    break;
            }
        }


    };
}
