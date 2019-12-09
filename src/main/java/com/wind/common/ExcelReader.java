package com.wind.common;

/**
 * @Author mandy
 * @Create 2019/11/14 14:51
 */
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * POI解析Excel
 */
public class ExcelReader {
    /**
     * 初始化表单
     */
    private Sheet sheet = getSheet();


    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 地址
     * @throws IOException IO异常
     */
    private Workbook readExcel(String path) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        // return a list contains many list
        List<List<String>> lists = new ArrayList<List<String>>();
        //读取excel文件
        InputStream is = null;
        //获取工作薄
        Workbook wb = null;
        try {
            is = new FileInputStream(path);
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wb;
    }



    /**
     * 获取表单（默认第一个sheet）
     * @return 第一个表单
     */
    public Sheet getSheet(){
        String path = "E:\\WindApi\\src\\main\\java\\com\\wind\\data\\testcases.xls";
        Workbook workbook = readExcel(path);
        assert workbook != null;
        Sheet sheet = workbook.getSheetAt(0);
        assert sheet != null;
        return sheet;
    }

    /**
     * 获取总行数
     * @return 行数
     */
    public int getLines(){
        int nrows = sheet.getLastRowNum();
        System.out.println("sheet总行数为：" + nrows);
        return nrows;
    }

    /**
     * 获取一行的内容
     * @param rowNum 第几行
     * @return 行的数据
     */
    public Row getRow(int rowNum){
        //打印每行
        Row row = sheet.getRow(rowNum);
        return row;
    }

    /**
     * 获取总列数
     * @return 列的数据
     */
    public int getColLines(){
        int ncols = getRow(getLines()).getLastCellNum();
        System.out.println("sheet总列数为：" + ncols);
        return ncols;
    }

    /**
     * 获取单元格内容
     * @param rowNum 第几行
     * @param colNum 第几列
     */
    public String getCell(int rowNum, int colNum){
        String cellData = null;
        try {
            Cell cell = getRow(rowNum).getCell(colNum);
            if (cell != null){
                cellData = cell.getStringCellValue();
            }
            System.out.println("单元格的内容为：" + cellData);
        } catch (Exception e) {
            System.out.println("[MyLog]--------单元格获取异常");
        }
        return cellData;
    }


    public void main() {
        getColLines();
        getCell(1,1);
    }

}