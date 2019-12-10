package com.wind.testcases;

import com.wind.common.ExcelReader;
import com.wind.common.TestMethod;
import com.wind.config.TestConfig;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @Author mandy
 * @Create 2019/12/9 9:31
 */

public class RunExcel {

    private TestMethod testMethod = new TestMethod();
    private TestConfig config = new TestConfig();

    /**
     * 获取每个单元格数据
     */
    private ExcelReader excelReader = new ExcelReader();
    private String getId(int rowNum) { return excelReader.getCell(rowNum, config.IdColNum); }
    private String getModelName(int rowNum) { return excelReader.getCell(rowNum, config.ModelNameColNum); }
    private String getUrl(int rowNum) { return excelReader.getCell(rowNum, config.UrlColNum); }
    private String getMethod(int rowNum) { return excelReader.getCell(rowNum, config.MethodColNum); }
    private String getIsRun(int rowNum) { return excelReader.getCell(rowNum, config.IsHeaderColNum); }
    private String getDependentId(int rowNum) { return excelReader.getCell(rowNum, config.DependentIdColNum);}
    private String getDependentKey(int rowNum) { return excelReader.getCell(rowNum,config.DependentKeyColNum); }
    private String getResponseKey(int rowNum) { return excelReader.getCell(rowNum, config.ResponseKeyColNum); }
    String getRequestData(int rowNum) { return excelReader.getCell(rowNum, config.RequestDataColNum); }
    private String getExpectResult(int rowNum) { return excelReader.getCell(rowNum, config.ExpectDataColNum); }
    private String getActualResult(int rowNum) { return excelReader.getCell(rowNum, config.ActualDataColNum); }


    /**
     * 定义DefaultHttpClient
     */
    @BeforeTest
    public void beforeTest() {
        // 实例化client
        TestConfig.defaultHttpClient = new DefaultHttpClient();
    }

    /**
     * 执行case方法
     * @return response
     */
    private String runCase(int rowNum, String isRun) throws Exception {
        // 执行用例
        String actualResult = null;
        String requestData = getRequestData(rowNum);
        // 如果isRun为true，则运行
        if (isRun.equals("yes")){
            // 如果有依赖，先执行依赖case，再把取到的依赖值赋值给本case
            if (getDependentId(rowNum) != null && !getDependentId(rowNum).equals("")) {
                System.out.println("[MyLog]--------getDependentId为" + getDependentId(rowNum));
                String dependentValue = runDependentCase(rowNum);
                System.out.println("[MyLog]--------执行依赖case获取到的依赖数据为: " + dependentValue);
                String newData;
                String dependentKey = getDependentKey(rowNum);

                // 如果data是键值对，使用key-value的形式替换json请求字符串中的值
                if (requestData.startsWith("{")) {
                    JSONObject reqestJson = new JSONObject(requestData);
                    JSONObject newJson = reqestJson.put(dependentKey, dependentValue);
                    newData = newJson.toString();
                }
                // 如果data是raw，使用拼接的方式追加请求字符串的值
                else {
                    newData = requestData + "&" + dependentKey + "=" + dependentValue;
                }

                System.out.println("[MyLog]--------新的请求数据为" + newData);
                actualResult = testMethod.main(getMethod(rowNum), getUrl(rowNum), newData);

            } else {
                actualResult = testMethod.main(getMethod(rowNum), getUrl(rowNum), getRequestData(rowNum));
            }
            System.out.printf("[MyLog]--------%s[%s]的执行结果为：%s \n",getId(rowNum),getModelName(rowNum),actualResult);
            JSONObject resJson = new JSONObject(actualResult);
            Object code = resJson.get("code");

            // 如果是登录接口，为公共参数token重新赋值
            if(getUrl(rowNum).contains("register")){
                TestConfig.token = resJson.getJSONObject("data").getString("token");
            }

            // 把执行结果写入excel
            if (code.equals(200)){
                excelReader.writeCell(rowNum,config.ActualDataColNum,actualResult);
            }else {
                excelReader.writeCell(rowNum,config.ActualDataColNum,"fail");
            }

            // 断言
//            Assert.assertEquals(msg, getExpectResult(rowNum));
        }
        return actualResult;

    }

    /**
     * 执行依赖的case并获取依赖值
     * @return dependentValue
     */
    private String runDependentCase(int rowNum) throws Exception {
        // 执行用例
        String dependentValue = null;
        for (int i = 1; i < excelReader.getLines(); i++) {
            int findRowNum;
            String findId = excelReader.getCell(i, 0);
            if (getDependentId(rowNum).equals(findId)) {
                findRowNum = i;
                String dependentResult = runCase(findRowNum,"yes");
                // 获取实际结果
                JSONObject resJson = new JSONObject(dependentResult);
                // 分割依赖数据
                if (getResponseKey(rowNum).contains(":")){
                    String[] dependentKeys = getResponseKey(rowNum).split(":", 2);
                    // 存储id
                    JSONArray list = resJson.getJSONObject("data").getJSONArray(dependentKeys[0]);
                    dependentValue = list.getJSONObject(0).getString(dependentKeys[1]);
                }else{
                    dependentValue = resJson.getJSONObject("data").getString(getResponseKey(rowNum));
                }
                break;
            }
        }
        return dependentValue;
    }

    /**
     * 遍历执行每条用例
     */
    @Test
    public void main() throws Exception {
        resetResult();
        for (int i=1;i<=excelReader.getLines();i++) {
            System.out.printf("[MyLog]----------------开始执行第 %s 条case---------------- \n",String.valueOf(i));
            runCase(i,getIsRun(i));
        }

    }


    /**
     * 执行用例前把结果置空
     */
    private void resetResult() {
        System.out.println("[MyLog]--------开始重置结果列--------");
        for (int i=1;i<=excelReader.getLines();i++) {
            excelReader.writeCell(i,config.ActualDataColNum,"");
        }

    }

}