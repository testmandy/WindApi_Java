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
    private  sheet = getSheet();

    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 地址
     * @throws IOException IO异常
     */
    private Object readExcel(String path) {
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

            System.out.println("sheet内容为：" + sheet);

            //第一行为标题
            // 打印每个单元格
//            for (Row row : sheet) {
//                ArrayList<String> list = new ArrayList<String>();
//                for (Cell cell : row) {
//                    //根据不同类型转化成字符串
//                    cell.setCellType(Cell.CELL_TYPE_STRING);
//                    System.out.println("单元格的内容为" + cell.getStringCellValue());
//                    list.add(cell.getStringCellValue());
//                }
//                lists.add(list);
//            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sheet;
    }



    /**
     * 获取表单（默认第一个sheet）
     * @return 第一个表单
     */
    private void getSheet(){
        String path = "E:\\WindApi\\src\\main\\java\\com\\wind\\data\\testcases.xls";
        Object sheet = readExcel(path);
        assert sheet != null;
    }

    /**
     *
     *
     * @return 行数
     */
    private int getLines(){
        //打印每行
        int nrows = sheet.getLastRowNum();
        System.out.println("sheet总行数为：" + nrows);
        return nrows;
    }

    /**
     *
     * @param rowNum 第几行
     * @return 行的数据
     */
    private Row getRow(sheet, int rowNum){
        Row row = sheet.getRow(rowNum);
        return row;
    }

    /**
     *
     * @param colNum 第几行
     * @return 列的数据
     */
    private int getColLines(int colNum){
        int ncols = 0;
        ncols = getRow(getLines()).getLastCellNum();
        System.out.println("sheet总列数为：" + ncols);
        return ncols;
    }

    /**
     *
     * @param colNum 第几个单元格
     */
    private Cell getCell(int rowNum, int colNum){
        Cell cell = getRow(rowNum).getCell(colNum);
        System.out.println("单元格的内容为：" + cell);
        return cell;
    }


    @Test
    public void main() {
        Object sheet = getSheet();
        getCell(2,1);

    }



}