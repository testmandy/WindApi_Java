package com.wind.common;

/**
 * @Author mandy
 * @Create 2019/12/9 9:31
 */

import org.apache.poi.ss.usermodel.Cell;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;


public class RunExcel {
    private final int IdColNum = 0;
    private final int ModelNameColNum = 1;
    private final int UrlColNum = 2;
    private final int MethodColNum = 3;
    private final int IsHeaderColNum = 4;
    private final int DependentIdColNum = 5;
    private final int DependentDataColNum = 6;
    private final int DependentKeyColNum = 7;
    private final int RequestDataColNum = 8;
    private final int ExpectDataColNum = 9;
    private final int ActualDataColNum = 10;
    private ExcelReader excelReader = new ExcelReader();
    private TestMethod testMethod = new TestMethod();

    private String getId(int rowNum) {
        return excelReader.getCell(rowNum,IdColNum);
    }
    private String getModelName(int rowNum) {
        return excelReader.getCell(rowNum,ModelNameColNum);
    }
    private String getUrl(int rowNum) {
        return excelReader.getCell(rowNum,UrlColNum);
    }
    private String getMethod(int rowNum) {
        return excelReader.getCell(rowNum,MethodColNum);
    }
    private String getIsHeader(int rowNum) {
        return excelReader.getCell(rowNum,IsHeaderColNum);
    }
    private String getDependentId(int rowNum) {
        return excelReader.getCell(rowNum,DependentIdColNum);
    }
    private String getDependentData(int rowNum) {
        return excelReader.getCell(rowNum,DependentDataColNum);
    }
    private String getDependentKey(int rowNum) {
        return excelReader.getCell(rowNum,DependentKeyColNum);
    }
    public String getRequestData(int rowNum) {
        return excelReader.getCell(rowNum,RequestDataColNum);
    }
    private String getExpectResult(int rowNum) {
        return excelReader.getCell(rowNum,ExpectDataColNum);
    }


    private void getData(int rowNum) throws Exception {
        String id = excelReader.getCell(rowNum,IdColNum);
        String modelName = excelReader.getCell(rowNum,ModelNameColNum);
        String url = excelReader.getCell(rowNum,UrlColNum);
        String method = excelReader.getCell(rowNum,MethodColNum);
        String isHeader = excelReader.getCell(rowNum,IsHeaderColNum);
        String dependentId = excelReader.getCell(rowNum,DependentIdColNum);
        String dependentData = excelReader.getCell(rowNum,DependentDataColNum);
        String dependentKey = excelReader.getCell(rowNum,DependentKeyColNum);
        String requestData = excelReader.getCell(rowNum,RequestDataColNum);
        String expectResult = excelReader.getCell(rowNum,ExpectDataColNum);
    }


    private String runCase(int i) throws Exception {
        // 执行用例
        String actualResult = null;
        if (getDependentId(i) != null){
            String dependentData = runDependentCase(i);
        }else {
            actualResult = testMethod.main(getMethod(i),getUrl(i),getRequestData(i));
            System.out.println(getId(i) + "[" + getModelName(i) + "] 的执行结果为：" + actualResult );
            Assert.assertEquals(actualResult,getExpectResult(i));
        }
        return actualResult;
    }

    private String runDependentCase(int rowNum) throws Exception {
        // 执行用例
        String dependentData = null;
        for (int i = 1; i < excelReader.getLines(); i++) {
            int findRowNum;
            String findId = excelReader.getCell(i, 0);
            if (getDependentId(rowNum).equals(findId)) {
                findRowNum = i;
                String dependentResult = runCase(findRowNum);
                // 获取实际结果
                JSONObject resJson = new JSONObject(dependentResult);
                dependentData = resJson.getJSONObject("data").getString(getDependentKey(rowNum));
                break;
            }
        }
        return dependentData;
    }


    @Test
    public void main() throws Exception {
        for(int i=1;i<excelReader.getLines();i++){
            runCase(i);
        }
    }


}
