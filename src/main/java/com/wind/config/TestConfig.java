package com.wind.config;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 公共测试配置
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

    // 定义testcase在excel中的列数
    public final int IdColNum = 0;
    public final int ModelNameColNum = 1;
    public final int ApiNameColNum = 2;
    public final int UrlColNum = 3;
    public final int MethodColNum = 4;
    public final int IsHeaderColNum = 5;
    public final int DependentIdColNum = 6;
    public final int DependentKeyColNum = 7;
    public final int ResponseKeyColNum = 8;
    public final int RequestDataColNum = 9;
    public final int ExpectDataColNum = 10;
    public final int ActualDataColNum = 11;

}
