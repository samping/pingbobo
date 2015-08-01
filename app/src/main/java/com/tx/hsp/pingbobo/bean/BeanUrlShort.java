package com.tx.hsp.pingbobo.bean;

/**
 * Created by hsp on 15/7/28.
 */
public class BeanUrlShort {
    public String url_short;//string	短链接
    public String url_long;//string	原始长链接
    public int type;//int	链接的类型，0：普通网页、1：视频、2：音乐、3：活动、5、投票
    public boolean result;//boolean	短链的可用状态，true：可用、false：不可用。
}
