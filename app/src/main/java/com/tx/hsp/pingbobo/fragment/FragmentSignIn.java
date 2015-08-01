package com.tx.hsp.pingbobo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.tx.hsp.pingbobo.Constants;
import com.tx.hsp.pingbobo.MyApplication;
import com.tx.hsp.pingbobo.R;
import com.tx.hsp.pingbobo.Utils.L;
import com.tx.hsp.pingbobo.activity.ActivityHome;
import com.tx.hsp.pingbobo.activity.ActivityUser;
import com.tx.hsp.pingbobo.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSignIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignIn extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageButton imageButton;

    /**
     * 调用这个fragment的activity
     */
    private  Activity activity;

    /**
     * WeiboSDKDemo 程序的 APP_SECRET。
     * 请注意：请务必妥善保管好自己的 APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    private static final String WEIBO_DEMO_APP_SECRET = "77e5c7b280ee24203110dc6c5bec2570";

    /**
     * 通过 code 获取 Token 的 URL
     */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";

    /**
     * 通过 Token 获取 UID 的 URL
     */
    private static final String USER_UID = "https://api.weibo.com/2/account/get_uid.json";

    String Token = "https://api.weibo.com/2/statuses/home_timeline.json";


    /**
     * 获取 Token 成功或失败的消息
     */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED = 2;
    /**
     * 获取 UID 成功或失败的消息
     */
    private static final int MSG_GET_UID_SUCCESS = 3;
    private static final int MSG_GET_UID_FAILED = 4;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSignIn newInstance(String param1, String param2) {
        FragmentSignIn fragment = new FragmentSignIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSignIn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        // 初始化微博对象

        imageButton = (ImageButton) view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboAuth.authorize(new AuthListener(), WeiboAuth.OBTAIN_AUTH_CODE);
            }
        });
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
        mWeiboAuth = new WeiboAuth(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
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
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            if (null == values) {
                Toast.makeText(MyApplication.getAppContext(),
                        R.string.weibosdk_demo_toast_obtain_code_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            String code = values.getString("code");
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(MyApplication.getAppContext(),
                        R.string.weibosdk_demo_toast_obtain_code_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            mCode = code;
//            textView.setText(code);
            MyApplication.setCode(code);
//            mAuthCodeButton.setEnabled(true);
//            mTokenText.setText("");
            Toast.makeText(MyApplication.getAppContext(),
                    R.string.weibosdk_demo_toast_obtain_code_success, Toast.LENGTH_SHORT).show();
            fetchTokenAsync(mCode, WEIBO_DEMO_APP_SECRET);
        }

        @Override
        public void onCancel() {
            Toast.makeText(MyApplication.getAppContext(),
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UIUtils.showToast(MyApplication.getAppContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
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
                if (token != null && token.isSessionValid()) {
                    L.v("Success! " + token.toString());

                    mAccessToken = token;
                    mHandler.obtainMessage(MSG_FETCH_TOKEN_SUCCESS).sendToTarget();
                } else {
                    L.v("Failed to receive access token");
                }
            }

            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {
                L.v("onComplete4binary...");
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }

            @Override
            public void onIOException(IOException e) {
                L.v("onIOException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }

            @Override
            public void onError(WeiboException e) {
                L.v("WeiboException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
        });
    }
/**
 *
 */

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
//                    mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
//                    mAuthCodeButton.setEnabled(false);
                    MyApplication.setToken(mAccessToken.getToken());
                    MyApplication.setUid(mAccessToken.getUid());
                    Toast.makeText(MyApplication.getAppContext(),
                            R.string.weibosdk_demo_toast_obtain_token_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, ActivityHome.class));
                    activity.finish();
                    break;

                case MSG_FETCH_TOKEN_FAILED:
                    Toast.makeText(MyApplication.getAppContext(),
                            R.string.weibosdk_demo_toast_obtain_token_failed, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_GET_UID_SUCCESS:
                    startActivity(new Intent(activity, ActivityUser.class));
                    break;
                case MSG_GET_UID_FAILED:
                    break;
                default:
                    break;
            }
        }

        ;
    };


}
