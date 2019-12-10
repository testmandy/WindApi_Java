package com.wind.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * POI解析Excel
 */
public class ExcelReader {
    /**
     * 初始化表单E:\WindApi\src\main\java\com\wind\data\testcases.xls
     */
    private String path = System.getProperty("user.dir") + "/src/main/java/com/wind/data/testcases.xlsx";
    private Workbook wb = getWorkbook();
    private Sheet sheet = wb.getSheetAt(0);


    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 地址
     */
    private Workbook readExcel(String path) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        // return a list contains many list
        List<List<String>> lists = new ArrayList<>();
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
     * 获取workbook
     * @return workbook
     */
    private Workbook getWorkbook(){
        assert path != null;
        Workbook workbook = readExcel(path);
        assert workbook != null;
        return workbook;
    }


    /**
     * 获取表单（默认第一个sheet）
     * @return 第一个表单
     */
    public Sheet getSheet(){
        Sheet sheet = getWorkbook().getSheetAt(0);
        assert sheet != null;
        return sheet;
    }

    /**
     * 获取总行数
     * @return 行数
     */
    public int getLines(){
        int nrows = sheet.getLastRowNum();
        System.out.println("[MyLog]--------sheet总行数为：" + nrows);
        return nrows;
    }

    /**
     * 获取一行的内容
     * @param rowNum 第几行
     * @return 行的数据
     */
    private Row getRow(int rowNum){
        //打印每行
        return sheet.getRow(rowNum);
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
            }else {
                cellData = null;
            }
        } catch (Exception e) {
            System.out.println("[MyLog]--------单元格获取异常");
        }
        return cellData;
    }


    public void main() {
        getCell(1,1);
    }


    /**
     * 写入数据到excel单元格
     * @param rowNum 第几行
     * @param colNum 第几列
     * @param data 数据
     */
    public void writeCell(int rowNum, int colNum, String data) {
        try {
            Cell cell = getRow(rowNum).getCell(colNum);
            if (cell != null){
                System.out.println("原单元格内容为： " + cell.getStringCellValue());
            }else {
                cell = getRow(rowNum).createCell(colNum);
            }
            cell.setCellValue(data);
            System.out.println("修改后单元格内容为： " + cell.getStringCellValue());
            // 定义单元格样式1：pass的用例
            CellStyle cellStyle1 = wb.createCellStyle();
            cellStyle1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex()); // 前景色
            cellStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
            // 定义单元格样式1：fail的用例
            CellStyle cellStyle2 = wb.createCellStyle();
            cellStyle2.setFillForegroundColor(IndexedColors.RED.getIndex()); // 前景色
            cellStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cellStyle2.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
            // 运行成功或失败，分别设置不同单元格样式
            if (data.equals("fail")){
                cell.setCellStyle(cellStyle2);
            }else if (!data.equals("")){
                cell.setCellStyle(cellStyle1);
            }
            FileOutputStream os = new FileOutputStream(path);
            wb.write(os);//一定要写这句代码，否则无法将数据写入excel文档中
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试代码
//    @Test
    public void test() {
        writeCell(1, 10, "fail");

    }


}