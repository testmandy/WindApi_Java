package com.wind.testcases;

import com.wind.common.ReadEnv;
import com.wind.config.DBUtil;
import com.wind.config.TestConfig;
import com.wind.common.TestMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author mandy
 * @Create 2019/11/6 13:42
 */
public class CardTest{
    private String cardId;
    private String topicId;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
    private String timeStr = df.format(new Date());// new Date()为获取当前系统时间

    @Test
    public void getTopicList() throws Exception {
        // 发请求并接收response
        String result = TestMethod.getMethod(TestConfig.topicListUrl);
        System.out.println("[Mylog]------接口地址为:" + TestConfig.topicListUrl);
        System.out.println("[Mylog]------result的内容为:" + result);
        // 判断执行结果
        JSONObject resJson = new JSONObject(result);
        int code = resJson.getInt("code");
        Assert.assertEquals(code,200);

        // 存储id
        JSONArray list = resJson.getJSONObject("data").getJSONArray("list");
        List<String> topicIds = new ArrayList<>();
        for (int i=0;i<list.length();i++){
            String topicId = list.getJSONObject(i).getString("_id");
            topicIds.add(topicId);
        }
        this.topicId = topicIds.get(0);
        System.out.println("[Mylog]------主题id列表:" + topicIds);
    }


    @Test(enabled = true)
    public List<String> getMyCards() throws Exception {
        // 发请求并接收response
        String result = TestMethod.getMethod(TestConfig.myCardsUrl);
        System.out.println("[Mylog]------接口地址为:" + TestConfig.myCardsUrl);
        System.out.println("[Mylog]------result的内容为:" + result);
        // 判断执行结果
        JSONObject resJson = new JSONObject(result);
        int code = resJson.getInt("code");
        Assert.assertEquals(code,200);

        // 存储id
        JSONArray list = resJson.getJSONObject("data").getJSONArray("list");
        List<String> cardIds = new ArrayList<>();
        for (int i=0;i<list.length();i++){
            String cardId = list.getJSONObject(i).getString("_id");
            cardIds.add(cardId);
        }
        System.out.println("[Mylog]------我的人设卡id列表:" + cardIds);
        return cardIds;
    }


    @Test(enabled = false, dependsOnMethods = "getTopicList")
    public  void publishCard() throws Exception {
        // 发送请求
        List<NameValuePair> paramList = new ArrayList<>(0);
        paramList.add(new BasicNameValuePair("media[url]", ReadEnv.getData("media[url]")));
        paramList.add(new BasicNameValuePair("topicId", this.topicId));
        paramList.add(new BasicNameValuePair("remark", "This is my post at " + timeStr));

        // 以form表单的形式传参
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
        System.out.println(paramList);

        // 接收response
        String result = TestMethod.postWithForm(TestConfig.publishCardUrl,formEntity);
        System.out.println("[Mylog]------result的内容为:" + result);
        JSONObject resJson = new JSONObject(result);
        System.out.println("[Mylog]------resJson的内容为:" + resJson);

        // 判断执行结果
        try {
            String id = resJson.getJSONObject("data").getString("id");
            // 将id赋值给当前类属性
            this.cardId = id;
        } catch (Exception e) {
            System.out.println("[ErrorInfo]------请求错误");
        }
        int code = resJson.getInt("code");
        Assert.assertEquals(code,200);
    }

    @Test(enabled = false, dependsOnMethods = "publishCard")
    public void modifyCard() throws Exception {
        // 发送请求
        List<NameValuePair> paramList = new ArrayList<>(0);
        paramList.add(new BasicNameValuePair("cardId", this.cardId));
        paramList.add(new BasicNameValuePair("media[url]", ReadEnv.getData("media[url]")));
        paramList.add(new BasicNameValuePair("topicId", this.topicId));
        paramList.add(new BasicNameValuePair("remark", "The remark has been modified at " + timeStr));

        // 以form表单的形式传参
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
        System.out.println(paramList);

        // 接收response
        String result = TestMethod.postWithForm(TestConfig.modifyCardUrl,formEntity);
        System.out.println("[Mylog]------result的内容为:" + result);
        JSONObject resJson = new JSONObject(result);
        System.out.println("[Mylog]------resJson的内容为:" + resJson);

        // 判断执行结果
        int code = resJson.getInt("code");
        Assert.assertEquals(code, 200);
    }

    @Test(enabled = false)
    public void removeAllCards() throws Exception {
        // 发送请求
        List cardIds = getMyCards();
        for (Object cardId : cardIds) {
            List<NameValuePair> paramList = new ArrayList<>(0);
            paramList.add(new BasicNameValuePair("cardId", cardId.toString()));

            // 以form表单的形式传参
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
            System.out.println(paramList);

            // 接收response
            String result = TestMethod.postWithForm(TestConfig.removeCardUrl, formEntity);
            System.out.println("[Mylog]------result的内容为:" + result);
            JSONObject resJson = new JSONObject(result);
            System.out.println("[Mylog]------resJson的内容为:" + resJson);

            // 判断执行结果
            int code = resJson.getInt("code");
            Assert.assertEquals(code, 200);
        }
    }

    private List getEmailsFromDB() throws Exception {
        // 执行sql，获取期望结果
        SqlSession session = DBUtil.getSqlsession();
        List emails = session.selectList("getEmail");
        System.out.println("SQL查询到的结果：" + emails);
        return emails;
    }


    @Test(enabled = true, dependsOnMethods = "getTopicList")
    public void publishManyCards() throws Exception {
        // 发送请求
        List emails = getEmailsFromDB();
        for (Object email : emails) {
            // 发送请求
            List<NameValuePair> paramList = new ArrayList<>(0);
            paramList.add(new BasicNameValuePair("media[url]", ReadEnv.getData("media[url]")));
            paramList.add(new BasicNameValuePair("topicId", this.topicId));
            try {
                paramList.add(new BasicNameValuePair("remark", email.toString().substring(0,7)+ " at " + timeStr));
            } catch (Exception e) {
                System.out.println("email不存在或字符长度受限");

            }
            // 以form表单的形式传参
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
            System.out.println(paramList);

            // 接收response
            String result = TestMethod.postWithForm(TestConfig.publishCardUrl,formEntity);
            System.out.println("[Mylog]------result的内容为:" + result);
            JSONObject resJson = new JSONObject(result);
            System.out.println("[Mylog]------resJson的内容为:" + resJson);

            // 判断执行结果
            try {
                String id = resJson.getJSONObject("data").getString("id");
                // 将id赋值给当前类属性
                this.cardId = id;
            } catch (Exception e) {
                System.out.println("[ErrorInfo]------请求错误");
            }
            int code = resJson.getInt("code");
            Assert.assertEquals(code,200);
            Thread.sleep(3000);
        }

    }


}
