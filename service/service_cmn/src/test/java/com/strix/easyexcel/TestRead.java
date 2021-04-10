package com.strix.easyexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\STRIX\\Documents\\JAVA项目\\02.xlsx";

        EasyExcel.read(fileName,UserData.class,new ExcelListener()).sheet().doRead();


    }
}
