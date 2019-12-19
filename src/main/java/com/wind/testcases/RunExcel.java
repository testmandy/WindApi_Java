package com.wind.testcases;

import com.wind.common.ExcelReader;
import com.wind.common.TestMethod;
import com.wind.config.TestConfig;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
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
    private ExcelReader excelReader = new ExcelReader();
    private int colSum = excelReader.getLines();


    /**
     * 获取每个单元格数据
     */
    private String getId(int rowNum) { return excelReader.getCell(rowNum, config.IdColNum); }
    private String getModelName(int rowNum) { return excelReader.getCell(rowNum, config.ModelNameColNum); }
    private String getApiName(int rowNum) { return excelReader.getCell(rowNum, config.ApiNameColNum); }
    private String getUrl(int rowNum) { return excelReader.getCell(rowNum, config.UrlColNum); }
    private String getMethod(int rowNum) { return excelReader.getCell(rowNum, config.MethodColNum); }
    private String getIsRun(int rowNum) { return excelReader.getCell(rowNum, config.IsHeaderColNum); }
    private String getDependentCase(int rowNum) { return excelReader.getCell(rowNum, config.DependentIdColNum);}
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
            if (getDependentCase(rowNum) != null && !getDependentCase(rowNum).equals("")) {
                System.out.println("[MyLog]--------Run dependent case at first:" + getDependentCase(rowNum));
                String dependentValue = runDependentCase(rowNum);
                System.out.println("[MyLog]--------The dependent value is: " + dependentValue);
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

                System.out.println("[MyLog]--------The new request data is: " + newData);
                actualResult = testMethod.main(getMethod(rowNum), getUrl(rowNum), newData);

            } else {
                actualResult = testMethod.main(getMethod(rowNum), getUrl(rowNum), getRequestData(rowNum));
            }
            System.out.printf("[MyLog]--------%s[%s] running result is: %s \n",getId(rowNum),getApiName(rowNum),actualResult);
            JSONObject resJson;
            Object code;
            if (actualResult.startsWith("{")) {
                resJson = new JSONObject(actualResult);
                code = resJson.get("code");
                // 如果是登录接口，为公共参数token重新赋值
                if(getUrl(rowNum).contains("register")){
                    TestConfig.token = resJson.getJSONObject("data").getString("token");
                }
                // 把执行结果写入excel
                if (code.equals(200)){
                    excelReader.writeCell(rowNum,config.ActualDataColNum,actualResult);
                }else {
                    excelReader.writeCell(rowNum,config.ActualDataColNum,"failed: "+actualResult);
                }
            } else {
                System.out.println("[ErrorInfo]--------Running failed!");
            }
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
        for (int i=1;i<colSum;i++) {
            int findRowNum;
            String findModelName = excelReader.getCell(i, config.ApiNameColNum);
            if (getDependentCase(rowNum).equals(findModelName)) {
                findRowNum = i;
                String dependentResult = runCase(findRowNum,"yes");
                if (dependentResult.startsWith("{")) {
                    // 获取实际结果list
                    JSONObject resJson = new JSONObject(dependentResult);
                    String dependentKey = getResponseKey(rowNum);
                    // 如果有多个id，分割依赖数据
                    if (dependentKey.contains(":")){
                        String[] dependentKeys = dependentKey.split(":", 3);
                        // 存储id
                        JSONArray list;
                        try {
                            // 获取list中的第一个json对象
                            list = resJson.getJSONObject("data").getJSONArray(dependentKeys[0]);
                        } catch (JSONException e) {
                            System.out.println("[ErrorInfo]--------The dependent case run failed!");
                            continue;
                        }
                        if (dependentKeys.length==2) {
                            try {
                                dependentValue = list.getJSONObject(0).getString(dependentKeys[1]);
                            } catch (JSONException e) {
                                System.out.println("[ErrorInfo]--------The dependent case run failed!");
                                continue;                            }
                        }else {
                            JSONObject targetJson = list.getJSONObject(0).getJSONObject(dependentKeys[1]);
                            dependentValue = targetJson.getString(dependentKeys[2]);
                        }
                    }else if(dependentKey.contains(",")){
                        String[] dependentKeys = dependentKey.split(",", 2);
                        try {
                            dependentValue = resJson.getJSONObject("data").getJSONObject(dependentKeys[0]).getString(dependentKeys[1]);
                            System.out.println("[1111111111111111111111111]"+dependentValue);
                        } catch (JSONException e) {
                            System.out.println("[ErrorInfo]--------The dependent case run failed!");
                            continue;
                        }
                    }else{
                        try {
                            dependentValue = resJson.getJSONObject("data").getString(dependentKey);
                        } catch (JSONException e) {
                            System.out.println("[ErrorInfo]--------The dependent case run failed!");
                            continue;
                        }
                    }
                    break;
                } else {
                    System.out.println("[ErrorInfo]--------The dependent case run failed!");
                }
            }
        }
        return dependentValue;
    }


    /**
     * 执行用例前把结果置空
     */
    private void resetResult() {
        System.out.println("[MyLog]--------Start to reset the actual cell value--------");
        for (int i=1;i<=colSum;i++) {
            excelReader.writeCell(i,config.ActualDataColNum,"");
        }
        System.out.println("[MyLog]--------Reset finished--------");
    }

    /**
     * 遍历执行每条用例
     */
    @Test
    public void main() throws Exception {
        resetResult();
        for (int i=1;i<=colSum;i++) {
            System.out.printf("[MyLog]----------------Start to run THE NO.%s case---------------- \n",String.valueOf(i));
            runCase(i,getIsRun(i));
        }

    }


}