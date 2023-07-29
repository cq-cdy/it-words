package com.example.ITwords;

import com.example.ITwords.GetMysql.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

class YonghuFankui {
    static void jubao(String message, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "insert into jubao (message,username)values(?,?);";

            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, message);//第一个通配符
            ps.setObject(2, name);//第二个通配符
            //4.执行
            ps.execute();
        } catch (Exception e) {
            System.out.println("未知错误6");
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, null);
        }
    }

    static void yijianfankui(String messgae, String name) {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "insert into yonghufankui (message,username)values(?,?);";

            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, messgae);//第一个通配符
            ps.setObject(2, name);//第二个通配符
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, null);
        }
    }


}
