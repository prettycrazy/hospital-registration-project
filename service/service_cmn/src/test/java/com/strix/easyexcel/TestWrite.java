package com.strix.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {

        List<UserData> list = new ArrayList<>();
        for(int i = 0 ; i <10 ; i++){
            UserData data = new UserData();
            data.setUid(i);
            data.setName("lucy" + i);
            list.add(data);
        }


        String fileName = "C:\\Users\\STRIX\\Documents\\JAVA项目\\02.xlsx";

        EasyExcel.write(fileName, UserData.class).sheet("用户信息")
                .doWrite(list);



    }
}
