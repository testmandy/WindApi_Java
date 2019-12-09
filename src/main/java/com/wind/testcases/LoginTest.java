package com.wind.testcases;

import com.wind.common.*;
import com.wind.config.DBUtil;
import com.wind.config.TestConfig;
import com.wind.model.LoginModel;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
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
        // 获取实际结果
        String actureId = resJson.getJSONObject("data").getString("id");
        int code = resJson.getInt("code");
        System.out.println("[Mylog]------用户expectId为：" + actureId);

        // 为公共参数token重新赋值
        TestConfig.token = resJson.getJSONObject("data").getString("token");

        // 执行sql，获取期望结果
        SqlSession session = DBUtil.getSqlsession();
        LoginModel loginModel = session.selectOne("loginCase",2);
        String expectId = loginModel.getId();
        System.out.println("SQL查询到的结果：" + loginModel);

        // 断言结果
        Assert.assertEquals(code,200);
        Assert.assertEquals(actureId,expectId);
    }


    @Test(groups = "loginTrue",description = "登录成功测试用例")
    public void loginWithString() throws Exception {
        RunExcel runExcel = new RunExcel();

        // 以String形式传参
        String data = runExcel.getRequestData(1);

        // 接收response
        String result = TestMethod.postWithString(TestConfig.loginUrl,data);
        System.out.println("[Mylog]------result的内容为:" + result);
        JSONObject resJson = new JSONObject(result);
        System.out.println("[Mylog]------resJson的内容为:" + resJson);
        // 获取实际结果
        String actureId = resJson.getJSONObject("data").getString("id");
        int code = resJson.getInt("code");
        System.out.println("[Mylog]------用户expectId为：" + actureId);

        // 为公共参数token重新赋值
        TestConfig.token = resJson.getJSONObject("data").getString("token");

    }

}


