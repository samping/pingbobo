package com.tx.hsp.pingbobo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsp on 15/7/28.
 */
public class BeanStatus {
    public String created_at;    //string	微博创建时间
    public long id;    //int64	微博ID
    public long mid;//int64	微博MID
    public String idstr;//string	字符串型的微博ID
    public String text;    //string	微博信息内容
    public String source;    //string	微博来源
    public boolean favorited;    //boolean	是否已收藏，true：是，false：否
    public boolean truncated;    //boolean	是否被截断，true：是，false：否
    // public String in_reply_to_status_id;    //string	（暂未支持）回复ID
    //public String in_reply_to_user_id;    //string	（暂未支持）回复人UID
    //public String in_reply_to_screen_name;    //string	（暂未支持）回复人昵称
    public String thumbnail_pic;//string	缩略图片地址，没有时不返回此字段
    public String bmiddle_pic;    //string	中等尺寸图片地址，没有时不返回此字段
    public String original_pic;    //string	原始图片地址，没有时不返回此字段
    public BeanGeo geo;//	object	地理信息字段 详细
    public BeanUser user;//	object	微博作者的用户信息字段 详细
    public BeanStatus retweeted_status;//	object	被转发的原微博信息字段，当该微博为转发微博时返回 详细
    public int reposts_count;    //int	转发数
    public int comments_count;    //int	评论数
    public int attitudes_count;    //int	表态数
    public int mlevel;    //int	暂未支持
    //visible	object	微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
    public List<BeanPicUrl> pic_urls = new ArrayList<BeanPicUrl>();//	object	微博配图ID。多图时返回多图ID，用来拼接图片url。用返回字段thumbnail_pic的地址配上该返回字段的图片ID，即可得到多个图片url。
    //ad	object array	微博流内的推广微博ID
}
