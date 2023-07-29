package com.example.ITwords.SearchUser;

import com.example.ITwords.GetMysql.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetUserName {
    public static String getusername(String searchName) {
        boolean isfind = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select user from user;";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {//判断结果集下一条是否有数据，如果有数据，返回true，指针下移，否则结束
                String getName = res.getString(1);
                if (getName.equals(searchName)) {
                    isfind = true;
                    break;
                }
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return isfind ? searchName : null;
    }

    public static ArrayList<UserWord> getuserwordslist(String searchName) {
        ArrayList<UserWord> userWordArrayList = new ArrayList<UserWord>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select * from " + searchName + "tab ORDER BY word" + ";";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {//判断结果集下一条是否有数据，如果有数据，返回true，指针下移，否则结束
                UserWord userWord = new UserWord();
                userWord.setWord(res.getString(1));
                userWord.setMean(res.getString(2));
                userWordArrayList.add(userWord);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return userWordArrayList;
    }

    public static int getuser_high_score(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int s = 0;
        try {
            conn = GetConnection.getConnection();
            String sql = "select highscore from english_test_score where username='" + username + "';";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {//判断结果集下一条是否有数据，如果有数据，返回true，指针下移，否则结束
                s = res.getInt(1);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return s;
    }

}
