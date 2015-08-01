package com.tx.hsp.pingbobo.bean;

/**
 * Created by hsp on 15/7/28.
 */
public class BeanComment {
    public String created_at;//string	评论创建时间
    public int id;//int64	评论的ID
    public String text;//string	评论的内容
    public String source;//string	评论的来源
    public BeanUser user;//	object	评论作者的用户信息字段 详细
    public String mid;//string	评论的MID
    public String idstr;//string	字符串型的评论ID
    public BeanStatus status;//object	评论的微博信息字段 详细
    public BeanComment reply_comment;//object	评论来源评论，当本评论属于对另一评论的回复时返回此字段
}
