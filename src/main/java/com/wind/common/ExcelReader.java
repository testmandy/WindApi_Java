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
    private List<List<String>> sheet = getSheet();

    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 地址
     * @throws IOException IO异常
     */
    public List<List<String>> readExcel(String path) {
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

//            //第一行为标题
//            for (Row row : sheet) {
//                ArrayList<String> list = new ArrayList<String>();
//                for (Cell cell : row) {
//                    //根据不同类型转化成字符串
//                    cell.setCellType(Cell.CELL_TYPE_STRING);
//                    System.out.println(cell.getStringCellValue());
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
     * 创建Excel.xls
     * @param lists 需要写入xls的数据
     * @param titles 列标题
     * @param name  文件名
     * @return
     * @throws IOException
     */
    public static Workbook creatExcel(List<List<String>> lists, String[] titles, String name) throws IOException {
        System.out.println(lists);
        //创建新的工作薄
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(name);
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<titles.length;i++){
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<titles.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(cs);
        }
        if(lists == null || lists.size() == 0){
            return wb;
        }
        //设置每行每列的值
        for (short i = 1; i <= lists.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short)i);
            for(short j=0;j<titles.length;j++){
                // 在row行上创建一个方格
                Cell cell = row1.createCell(j);
                cell.setCellValue(lists.get(i-1).get(j));
                cell.setCellStyle(cs2);
            }
        }
        return wb;
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

    /**
     *
     *
     * @return 行数
     */
    private int getLines(){
        int size = sheet.size();
        System.out.println("sheet size: " + size);
        return size;
    }

    /**
     *
     * @param rowNum 第几行
     * @return 行的数据
     */
    private List<String> getRow(int rowNum){
        List<String> row = sheet.get(rowNum);
        return row;
    }

    /**
     *
     * @param colNum 第几行
     * @return 列的数据
     */
    private List<String> getCol(int colNum){
        List<String> col = sheet.get(colNum);
        return col;
    }

    /**
     *
     * @param cellNum 第几个单元格
     */
    private void getCell(int cellNum){
        List<String> row = getRow(cellNum);
        String cell = row.get(0);
        System.out.println(cell);
    }


    @Test
    public void main() {
        getCell(2);

    }



}