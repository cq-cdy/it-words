package com.example.ITwords;

import com.example.ITwords.GetMysql.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Article_Message_Share {
    static ArrayList<Message> getMessagearray = new ArrayList<>();
    private static Message oneme = new Message();

    static void getOneMessage() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select * from article_by_user order by rand( ) limit 1;";
            ps = conn.prepareStatement(sql);
            //执行并返回结果集
            res = ps.executeQuery();
            getMessagearray.clear();//清空集合，使得集合每次之存储一条
            //处理结果集
            while (res.next()) {
                String message = res.getString(1);
                String by_user = res.getString(2);
                int agree = res.getInt(3);
                String date = res.getString(4);
                oneme.setMessage(message);
                oneme.setByuser(by_user);
                oneme.setAgree(agree);
                oneme.setDate(date);
                getMessagearray.add(oneme);
            }
        } catch (Exception ignored) {
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }

    }

    static void agree(final Message tempmessage) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "UPDATE article_by_user set agree="
                    + (tempmessage.getAgree() + 1)
                    + " WHERE by_user='" + tempmessage.getByuser() + "'" +
                    "and article_message='" + tempmessage.getMessage() + "'";
            ps = conn.prepareStatement(sql);

            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }

    static void disagree(final Message tempmessage) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "UPDATE article_by_user set agree=" + (tempmessage.getAgree() - 1)
                    + " WHERE by_user='" + tempmessage.getByuser() + "'" +
                    "and article_message='" + tempmessage.getMessage() + "'";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }

    public static void deleteMessage(String message, String date) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "delete from article_by_user where article_message='" + message + "' and date='" + date + "'";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }


}
