package com.tx.hsp.pingbobo.bean.users;

/**
 * Created by hsp on 15/7/28.
 */
public class BeanShow {
    public String screen_name; //用户昵称
    public int province; //用户所在省级ID
    public int city; //用户所在城市ID
    public String location; //用户所在地
    public String description; //用户个人描述
    public String profile_image_url; //用户头像地址（中图），50×50像素
    public String gender; //性别，m：男、f：女、n：未知
    public int followers_count;  //粉丝数
    public int friends_count; //关注数
    public int statuses_count; //微博数
    public int favourites_count; //收藏数;
    public String avatar_large;  //用户头像地址（大图），180×180像素
    public String avatar_hd;     //用户头像地址（高清），高清头像原图
}
