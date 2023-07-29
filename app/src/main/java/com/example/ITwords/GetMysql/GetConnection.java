package com.example.ITwords.GetMysql;


import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetConnection {
    public static boolean isconnectionsuccessful = true;
    public static String URL =null;
    public static String password = null;

    public static Connection getConnection() {
        Connection conn = null;
        isconnectionsuccessful = true;
        try {
            Class.forName("com.mysql.jdbc.Driver"); //加载驱动
            conn = DriverManager.getConnection(
                    URL,
                    "root", password);

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isconnectionsuccessful = false;
        } catch (Exception e) {
            isconnectionsuccessful = false;
        } finally {
            isconnectionsuccessful = conn != null;
        }
        return conn;
    }


    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet res) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void setTitle(TextView textView){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select biaoti1 from biaoti ;";
            ps = conn.prepareStatement(sql);
            //执行并返回结果集
            res = ps.executeQuery();
            //处理结果集
            while (res.next()) {
                String inerword = res.getString(1);
                textView.setText(inerword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }
    public static void setGongGao(TextView textView){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select gonggao from biaoti ;";
            ps = conn.prepareStatement(sql);
            //执行并返回结果集
            res = ps.executeQuery();
            //处理结果集
            while (res.next()) {
                String inerword = res.getString(1);
                textView.setText(inerword);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }


}
