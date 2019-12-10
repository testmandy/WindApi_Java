package com.wind.testcases;

/**
 * @Author mandy
 * @Create 2019/12/9 9:31
 */

import com.wind.common.ExcelReader;
import com.wind.common.TestMethod;
import com.wind.config.TestConfig;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class RunExcel {

    private TestMethod testMethod = new TestMethod();
    TestConfig config = new TestConfig();

    /**
     * 获取每个单元格数据
     * @return 单元格数据
     */
    private ExcelReader excelReader = new ExcelReader();
    private String getId(int rowNum) { return excelReader.getCell(rowNum, config.IdColNum); }
    private String getModelName(int rowNum) { return excelReader.getCell(rowNum, config.ModelNameColNum); }
    private String getUrl(int rowNum) { return excelReader.getCell(rowNum, config.UrlColNum); }
    private String getMethod(int rowNum) { return excelReader.getCell(rowNum, config.MethodColNum); }
    private String getIsRun(int rowNum) { return excelReader.getCell(rowNum, config.IsHeaderColNum); }
    private String getDependentId(int rowNum) { return excelReader.getCell(rowNum, config.DependentIdColNum);}
    private String getDependentData(int rowNum) { return excelReader.getCell(rowNum,config.DependentDataColNum); }
    private String getDependentKey(int rowNum) { return excelReader.getCell(rowNum, config.DependentKeyColNum); }
    public String getRequestData(int rowNum) { return excelReader.getCell(rowNum, config.RequestDataColNum); }
    private String getExpectResult(int rowNum) { return excelReader.getCell(rowNum, config.ExpectDataColNum); }
    private String getActualResult(int rowNum) { return excelReader.getCell(rowNum, config.ActualDataColNum); }


    /**
     * 定义DefaultHttpClient
     */
    @BeforeTest(groups = "beforeTest", description = "登录前")
    public void beforeTest() {
        // 实例化client
        TestConfig.defaultHttpClient = new DefaultHttpClient();
    }

    /**
     * 执行case方法
     * @return response
     */
    private String runCase(int rowNum) throws Exception {
        // 执行用例
        String actualResult = null;
        // 如果isRun为true，则运行
        if (getIsRun(rowNum).equals("yes")){
            if (getDependentId(rowNum) != null && !getDependentId(rowNum).equals("")) {
                System.out.println("[MyLog]--------getDependentId为" + getDependentId(rowNum));
                String dependentValue = runDependentCase(rowNum);
                System.out.println("[MyLog]--------执行依赖case获取到的依赖数据为: " + dependentValue);
                JSONObject reqestJson = new JSONObject(getRequestData(rowNum));
                JSONObject newJson = reqestJson.put(getDependentData(rowNum), dependentValue);
                String newData = newJson.toString();
                System.out.println("[MyLog]--------新的请求数据为" + newData);
                actualResult = testMethod.main(getMethod(rowNum), getUrl(rowNum), newData);

            } else {
                actualResult = testMethod.main(getMethod(rowNum), getUrl(rowNum), getRequestData(rowNum));
            }
            System.out.printf("[MyLog]--------%s[%s]的执行结果为：%s \n",getId(rowNum),getModelName(rowNum),actualResult);
            JSONObject resJson = new JSONObject(actualResult);
            String msg = resJson.getString("msg");
            //        int code = resJson.getInt("code");

            // 如果是登录接口，为公共参数token重新赋值
            if(getUrl(rowNum).contains("register")){
                TestConfig.token = resJson.getJSONObject("data").getString("token");
            }

            // 把执行结果写入excel
            if (msg.equals("success")){
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
                String dependentResult = runCase(findRowNum);
                // 获取实际结果
                JSONObject resJson = new JSONObject(dependentResult);
                if (getDependentKey(rowNum).contains(":")){
                    String[] dependentKeys = getDependentKey(rowNum).split(":", 2);
                    // 存储id
                    JSONArray list = resJson.getJSONObject("data").getJSONArray(dependentKeys[0]);
                    dependentValue = list.getJSONObject(0).getString(dependentKeys[1]);
                }else{
                    dependentValue = resJson.getJSONObject("data").getString(getDependentKey(rowNum));
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
            System.out.printf("[MyLog]--------开始执行第 %s 条case-------- \n",String.valueOf(i));
            runCase(i);
        }

    }


    /**
     * 执行用例前把结果置空
     */
    public void resetResult() {
        for (int i=1;i<=excelReader.getLines();i++) {
            System.out.println("[MyLog]--------开始重置结果列--------");
            excelReader.writeCell(i,config.ActualDataColNum,"");
        }

    }

}