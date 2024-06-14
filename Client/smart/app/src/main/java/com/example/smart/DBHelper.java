package com.example.smart;


import android.util.Log;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static String diver = "com.mysql.jdbc.Driver";
    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    private static String url = "jdbc:mysql://10.132.125.37:3306/smart?serverTimezone=GMT%2B8";
    private static String user = "root";//用户名
    private static String password = "123";//密码

    public static Connection getConn(){
        Connection conn = null;

        try {
            Class.forName(diver);
            conn = (Connection) DriverManager.getConnection(url,user,password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
