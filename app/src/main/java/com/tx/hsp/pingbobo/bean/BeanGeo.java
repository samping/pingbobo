package com.tx.hsp.pingbobo.bean;

/**
 * Created by hsp on 15/7/28.
 */
public class BeanGeo {
    public String longitude;//string	经度坐标
    public String latitude;//string	维度坐标
    public String city;//string	所在城市的城市代码
    public String province;//	string	所在省份的省份代码
    public String city_name;//string	所在城市的城市名称
    public String province_name;//	string	所在省份的省份名称
    public String address;//string	所在的实际地址，可以为空
    public String pinyin;//string	地址的汉语拼音，不是所有情况都会返回该字段
    public String more;//string	更多信息，不是所有情况都会返回该字段
}
