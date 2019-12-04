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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * POI解析Excel
 */
@Test
public class test {
    /**
     * 初始化表单
     */
    private List<List<String>> sheet = getSheet();

    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 地址
     * @throws IOException IO异常
     */
    private List<List<String>> readExcel(String path) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        // return a list contains many list
        List<List<String>> lists = new ArrayList<List<String>>();
        //读取excel文件
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            //获取工作薄
            Workbook wb = null;
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return null;
            }

            //读取第一个工作页sheet
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            System.out.println(sheet);
            System.out.println(row);
            System.out.println(cell);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 获取表单（默认第一个sheet）
     * @return 第一个表单
     */
    private List<List<String>> getSheet(){
        String path = "E:\\WindApi\\src\\main\\java\\com\\wind\\data\\testcases.xls";
        List<List<String>> sheet = readExcel(path);
        assert sheet != null;
        return sheet;
    }

//    /**
//     *
//     *
//     * @return 行数
//     */
//    private int getLines(){
//        int size = sheet.size();
//        System.out.println("sheet size: " + size);
//        return size;
//    }
//
//    /**
//     *
//     * @param rowNum 第几行
//     * @return 行的数据
//     */
//    private List<String> getRow(int rowNum){
//        List<String> row = sheet.get(rowNum);
//        return row;
//    }
//
//    /**
//     *
//     * @param colNum 第几行
//     * @return 列的数据
//     */
//    private List<String> getCol(int colNum){
//        List<String> col = sheet.get(colNum);
//        return col;
//    }
//
//    /**
//     *
//     * @param cellNum 第几个单元格
//     */
//    private void getCell(int cellNum){
//        List<String> row = getRow(cellNum);
//        String cell = row.get(0);
//        System.out.println(cell);
//    }
//
//
//    public void main() {
//        getCell(2);
//
//    }



}