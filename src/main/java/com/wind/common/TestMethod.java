package com.wind.common;

import com.alibaba.fastjson.JSONObject;
import com.wind.config.TestConfig;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author mandy
 * @Create 2019/10/17 14:59
 */
public class TestMethod {
    /**
     * 定义添加参数的方法
     * @return 参数列表
     */
    public List<NameValuePair> addParam(String name,String value){
        List<NameValuePair> paramList = new ArrayList<>(0);
        paramList.add(new BasicNameValuePair(name,value));
        return paramList;
    }

    /**
     * get请求方法
     * @param url api地址
     * @return response
     */
    public static String getMethod(String url) throws Exception {
        String result;
        // 定义get
        HttpGet get = new HttpGet(url);

        // 设置request headers
        get.setHeader("x-sign", TestConfig.getSign());
        get.setHeader("x-token", TestConfig.token);
        System.out.println("[Mylog]------token为：" + TestConfig.token);

        // 接收response
        HttpResponse response = TestConfig.defaultHttpClient.execute(get);
        result = EntityUtils.toString(response.getEntity());
        return result;
    }


    /**
     * post请求方法（参数以字符串形式）
     * @param url api地址
     * @param data 请求体
     * @return response
     */
    public static String postWithString(String url, String data) throws Exception {
        String result;

        // 定义post
        HttpPost post = new HttpPost(url);

        // 设置request headers
        post.setHeader("x-sign", TestConfig.getSign());
        post.setHeader("x-token", TestConfig.token);
        post.setHeader("content-type", "application/json");

        // 设置request body
        StringEntity entity = new StringEntity(data,"utf-8");
        System.out.println(data);
        post.setEntity(entity);

        // 接收response
        System.out.println("start to post");
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity());
        return result;
    }


    /**
     * post请求方法（参数以form表单形式）
     * @param url api地址
     * @param formEntity 请求体
     * @return response
     */
    public static String postWithForm(String url, UrlEncodedFormEntity formEntity) throws Exception {
        String result;

        // 定义post
        HttpPost post = new HttpPost(url);

        // 设置request headers
        post.setHeader("x-sign", TestConfig.getSign());
        post.setHeader("x-token", TestConfig.token);

        // 设置request body
        post.setEntity(formEntity);

        // 接收response
        System.out.println("start to post");
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        int code = response.getStatusLine().getStatusCode();
        result = EntityUtils.toString(response.getEntity());
        return result;
    }

    public String main(String method,String url,String data) throws Exception {
        String testUrl = ReadEnv.getData("base.url") + url;
//        String json = (JSONObject.toJSONString(data));
//        System.out.println("json的内容为：" + json);
        System.out.println(testUrl);
        String result = null;
        if (method.equals("get")){
            result = getMethod(testUrl);
        } else {
            result = postWithString(testUrl,data);
        }
        System.out.println("请求执行结果为：" + result);
        return result;

    }
}
