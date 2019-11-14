package com.wind.common;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @Author mandy
 * @Create 2019/10/17 11:55
 */
public class TestConfig {
    // 定义公用的url
    public static String loginUrl;
    public static String nearSphereUrl;
    public static String myCardsUrl;
    public static String publishCardUrl;
    public static String modifyCardUrl;
    public static String removeCardUrl;
    public static String topicListUrl;


    // 定义公用的请求
    public static DefaultHttpClient defaultHttpClient;

    // 定义公用的参数
    private static String sign = "Linshen100";
    public static String token = "anonymous_token";

    public static String getSign() {
        return sign;
    }

}
