package com.wind.testcases;

import com.wind.common.InterfaceName;
import com.wind.common.ReadEnv;
import com.wind.common.TestConfig;
import com.wind.common.TestMethod;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author mandy
 * @Create 2019/10/17 9:52
 */


public class LoginTest {
    @BeforeTest(groups = "loginTrue",description = "登录前")
    public void beforeTest(){
        // 为url赋值
        TestConfig.loginUrl = ReadEnv.getUrl(InterfaceName.LOGIN);
        TestConfig.nearSphereUrl = ReadEnv.getUrl(InterfaceName.NEARSPHERE);
        TestConfig.myCardsUrl = ReadEnv.getUrl(InterfaceName.MINECARDS);
        TestConfig.publishCardUrl = ReadEnv.getUrl(InterfaceName.PUBLISHCARDURL);
        TestConfig.modifyCardUrl = ReadEnv.getUrl(InterfaceName.MODIFYCARDURL);
        TestConfig.removeCardUrl = ReadEnv.getUrl(InterfaceName.REMOVECARDURL);
        TestConfig.topicListUrl = ReadEnv.getUrl(InterfaceName.TOPICLISTURL);


        // 实例化client
        TestConfig.defaultHttpClient = new DefaultHttpClient();
    }


    @Test(groups = "loginTrue",description = "登录成功测试用例")
    public void loginTrue() throws Exception {
        // 发送请求
        List<NameValuePair> paramList = new ArrayList<>(0);
        paramList.add(new BasicNameValuePair("phone", ReadEnv.getData("phone")));
        paramList.add(new BasicNameValuePair("phoneCaptcha", ReadEnv.getData("phoneCaptcha")));

        // 以form表单的形式传参
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
        System.out.println(paramList);

        // 接收response
        String result = TestMethod.postWithForm(TestConfig.loginUrl,formEntity);
        System.out.println("[Mylog]------result的内容为:" + result);
        JSONObject resJson = new JSONObject(result);
        System.out.println("[Mylog]------resJson的内容为:" + resJson);

        // 为公共参数token重新赋值
        TestConfig.token = resJson.getJSONObject("data").getString("token");

        // 判断执行结果
        String id = resJson.getJSONObject("data").getString("id");
        int code = resJson.getInt("code");
        System.out.println("[Mylog]------用户id为：" + id);
        Assert.assertEquals(code,200);
        Assert.assertEquals(id,"5d6d2d17cb0e2900707524bc");

        // 执行sql

    }

}


