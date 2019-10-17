package com.wind.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * @Author mandy
 * @Create 2019/10/17 14:59
 */
public class TestMethod {

    public static String postWithHeaders(String url,JSONObject params) throws Exception {
        String result;

        // 定义post
        HttpPost post = new HttpPost(url);

        // 设置request headers
        post.addHeader("x-version", "1");
        post.addHeader("x-clientid", "8dff6abf9c8829cd2f8c473cd6ab8386");
        post.addHeader("x-timestamp", "1567314497080");
        post.addHeader("x-nonce", "94535");
        post.addHeader("x-sign", "Linshen100");
        post.addHeader("x-token", "anonymous_token");


        // 设置request body
        StringEntity entity = new StringEntity(params.toString());
        post.setEntity(entity);

        // 接收response
        System.out.println("start to post");
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity());
        return result;

    }
}
