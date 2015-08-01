package com.tx.hsp.pingbobo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tx.hsp.pingbobo.R;
import com.tx.hsp.pingbobo.Utils.L;
import com.tx.hsp.pingbobo.bean.BeanPicUrl;
import com.tx.hsp.pingbobo.bean.BeanStatus;
import com.tx.hsp.pingbobo.bean.BeanStatuses;
import com.tx.hsp.pingbobo.network.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsp on 15/7/29.
 * AdapterStatus用来render每一条微博的内容
 * 可以用在hometime publictime friendtime等
 */
public class AdapterStatus extends RecyclerView.Adapter<AdapterStatusViewHolder> {


    private BeanStatuses beanStatuses;
    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;

    public AdapterStatus(Context c, BeanStatuses bean) {
        context = c;
        imageLoader = VolleySingleton.getInstance().getImageLoader();
        beanStatuses = bean;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public AdapterStatusViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        L.v("AdapterStatus onCreateViewHolder");
        View view = mInflater.inflate(R.layout.adapter_status, viewGroup, false);
        return new AdapterStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterStatusViewHolder myViewHolder, int i) {
        L.v("AdapterStatus onBindViewHolder");
        //        myViewHolder.photoGrid.setAdapter(new AdapterFragmentHomeGridView(context, beanStatuses.statuses.get(i)));
        myViewHolder.name.setText(beanStatuses.statuses.get(i).user.name);
        myViewHolder.time.setText(beanStatuses.statuses.get(i).created_at);
//        myViewHolder.come.setText(beanStatuses.statuses.get(i).source);
        myViewHolder.come.setText("");
        myViewHolder.text.setText(beanStatuses.statuses.get(i).text);
        String uri = beanStatuses.statuses.get(i).user.profile_image_url;

        /**
         * 加载user icon
         */
        if (uri != null) {
            myViewHolder.icon.setImageUrl(uri, imageLoader);
            myViewHolder.icon.setDefaultImageResId(R.drawable.status_photo_background);
            myViewHolder.icon.setErrorImageResId(R.drawable.status_photo_background);
        }
        /**
         * 加载status photoes
         */
        List<BeanPicUrl> pics = beanStatuses.statuses.get(i).pic_urls;
        if (pics != null) {
            myViewHolder.photoLayout.setVisibility(View.VISIBLE);
            for (int j = 0; j < pics.size(); j++) {
                NetworkImageView imageView = myViewHolder.picList.get(j);
                String url = pics.get(j).thumbnail_pic;
                L.v("thumbnail : " + url);
                imageView.setImageUrl(url, imageLoader);
                imageView.setDefaultImageResId(R.drawable.status_photo_background);
                imageView.setErrorImageResId(R.drawable.status_photo_background);
                imageView.setVisibility(View.VISIBLE);
            }
            /**
             * 去除不显示的photoes
             */
            for(int r=pics.size();r<9;r++){
                myViewHolder.picList.get(r).setVisibility(View.GONE);
            }

        }else{
            myViewHolder.photoLayout.setVisibility(View.GONE);
        }
        /**
         * 加载retweet
         */
        BeanStatus retweet = beanStatuses.statuses.get(i).retweeted_status;
        if(retweet!=null) {
            myViewHolder.retweetLayout.setVisibility(View.VISIBLE);
            myViewHolder.retweetName.setText(retweet.user.name);
            myViewHolder.retweetText.setText(retweet.text);
            List<BeanPicUrl> retweetPics = retweet.pic_urls;
            if (pics != null) {
                for (int j = 0; j < retweetPics.size(); j++) {
                    NetworkImageView imageView = myViewHolder.retweetPicList.get(j);
                    String url = retweetPics.get(j).thumbnail_pic;
                    L.v("retweet thumbnail : " + url);
                    imageView.setImageUrl(url, imageLoader);
                    imageView.setDefaultImageResId(R.drawable.status_photo_background);
                    imageView.setErrorImageResId(R.drawable.status_photo_background);
                    imageView.setVisibility(View.VISIBLE);
                }
                /**
                 * 去除不显示的photoes
                 */
                for (int r = retweetPics.size(); r < 9; r++) {
                    myViewHolder.retweetPicList.get(r).setVisibility(View.GONE);
                }

            }
        }else{
            myViewHolder.retweetLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onViewRecycled(AdapterStatusViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return beanStatuses.statuses.size();
    }
}
