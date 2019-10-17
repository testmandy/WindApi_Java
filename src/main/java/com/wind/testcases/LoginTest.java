package com.wind.testcases;

import com.wind.common.InterfaceName;
import com.wind.common.ReadFile;
import com.wind.common.TestConfig;
import com.wind.common.TestMethod;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @Author mandy
 * @Create 2019/10/17 9:52
 */


public class LoginTest {
    @BeforeTest(groups = "loginTrue",description = "登录前")
    public void beforeTest(){
        // 为url赋值
        TestConfig.loginUrl = ReadFile.getUrl(InterfaceName.LOGIN);
        TestConfig.nearSphereUrl = ReadFile.getUrl(InterfaceName.NEARSPHERE);

        // 实例化client
        TestConfig.defaultHttpClient = new DefaultHttpClient();

    }


    @Test(groups = "loginTrue",description = "登录成功测试用例")
    public void loginTrue() throws Exception {
        // 发送请求
        JSONObject params = new JSONObject();
        params.put("phone",ReadFile.getData("phone"));
        params.put("phoneCaptcha",ReadFile.getData("phoneCaptcha"));
        System.out.println(params);
        String result = TestMethod.postWithHeaders(TestConfig.loginUrl,params);
        System.out.println(result);

        // 执行sql

    }

}


