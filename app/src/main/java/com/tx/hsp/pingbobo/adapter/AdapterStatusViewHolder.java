package com.tx.hsp.pingbobo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tx.hsp.pingbobo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsp on 15/7/31.
 */
public class AdapterStatusViewHolder extends RecyclerView.ViewHolder {
    NetworkImageView icon, photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9;
    List<NetworkImageView> picList = new ArrayList<NetworkImageView>();
    TextView name, time, come, text, comment, send, good;
    View photoLayout;
    /**
     * 定义转发status引用
     */
    TextView retweetName,retweetText;
    NetworkImageView retweetPhoto1, retweetPhoto2, retweetPhoto3, retweetPhoto4, retweetPhoto5,
            retweetPhoto6, retweetPhoto7, retweetPhoto8, retweetPhoto9;
    List<NetworkImageView> retweetPicList = new ArrayList<NetworkImageView>();
    View retweetLayout;

    public AdapterStatusViewHolder(View itemView) {
        super(itemView);
        icon = (NetworkImageView) itemView.findViewById(R.id.status_icon);
        photo1 = (NetworkImageView) itemView.findViewById(R.id.status_photo1);
        picList.add(photo1);
        photo2 = (NetworkImageView) itemView.findViewById(R.id.status_photo2);
        picList.add(photo2);
        photo3 = (NetworkImageView) itemView.findViewById(R.id.status_photo3);
        picList.add(photo3);
        photo4 = (NetworkImageView) itemView.findViewById(R.id.status_photo4);
        picList.add(photo4);
        photo5 = (NetworkImageView) itemView.findViewById(R.id.status_photo5);
        picList.add(photo5);
        photo6 = (NetworkImageView) itemView.findViewById(R.id.status_photo6);
        picList.add(photo6);
        photo7 = (NetworkImageView) itemView.findViewById(R.id.status_photo7);
        picList.add(photo7);
        photo8 = (NetworkImageView) itemView.findViewById(R.id.status_photo8);
        picList.add(photo8);
        photo9 = (NetworkImageView) itemView.findViewById(R.id.status_photo9);
        picList.add(photo9);
        photoLayout = (View)itemView.findViewById(R.id.status_photo_layout);



        name = (TextView) itemView.findViewById(R.id.status_name);
        time = (TextView) itemView.findViewById(R.id.status_time);
        come = (TextView) itemView.findViewById(R.id.status_come);
        text = (TextView) itemView.findViewById(R.id.fragment_home_adapter_text);
        comment = (TextView) itemView.findViewById(R.id.status_comment);
        send = (TextView) itemView.findViewById(R.id.status_send);
        good = (TextView) itemView.findViewById(R.id.status_good);

        /**
         * 转发
         */
        retweetName = (TextView)itemView.findViewById(R.id.status_retweeted_name);
        retweetText = (TextView)itemView.findViewById(R.id.status_retweeted_text);
        retweetPhoto1 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo1);
        retweetPicList.add(retweetPhoto1);
        retweetPhoto2 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo2);
        retweetPicList.add(retweetPhoto2);
        retweetPhoto3 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo3);
        retweetPicList.add(retweetPhoto3);
        retweetPhoto4 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo4);
        retweetPicList.add(retweetPhoto4);
        retweetPhoto5 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo5);
        retweetPicList.add(retweetPhoto5);
        retweetPhoto6 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo6);
        retweetPicList.add(retweetPhoto6);
        retweetPhoto7 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo7);
        retweetPicList.add(retweetPhoto7);
        retweetPhoto8 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo8);
        retweetPicList.add(retweetPhoto8);
        retweetPhoto9 = (NetworkImageView) itemView.findViewById(R.id.status_retweeted_photo9);
        retweetPicList.add(retweetPhoto9);
        retweetLayout = (View)itemView.findViewById(R.id.status_retweeted_layout);
    }
}
