package com.example.ITwords.GetMysql;

import com.example.ITwords.AddFragment;
import com.example.ITwords.MainActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class WordDemo {
    static void addWord(final String word, final String mean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = GetConnection.getConnection();
                    //2.预编译sql语句，返回PrepareStatement的实例
                    String sql = "insert into " + MainActivity.mSharedPreferences2.getString("dqyh", "") + "tab" + " (word,mean)values(?,?);";

                    ps = conn.prepareStatement(sql);
                    //3.填充占位符
                    ps.setObject(1, word);//第一个通配符
                    ps.setObject(2, mean);//第二个通配符
                    //4.执行
                    ps.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    GetConnection.closeResource(conn, ps, null);
                }
            }
        }).start();
    }

    static void delWords(final String word) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    //1.获取数据库连接
                    conn = GetConnection.getConnection();
                    //2.预编译sql语句，返回PrepareStatement的实例
                    String sql = "delete from " + MainActivity.mSharedPreferences2.getString("dqyh", "") + "tab" + " where word=?;";
                    ps = conn.prepareStatement(sql);
                    //3.填充占位符
                    ps.setObject(1, word);//第一个通配符
                    //4.执行
                    ps.execute();
                } catch (Exception e) {
                    System.out.println("未知错误");
                } finally {
                    GetConnection.closeResource(conn, ps, null);
                }
            }
        }).start();
    }

    static void wordComplement(String word, String mean) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "insert into userwordfeedback (word,mean)values(?,?);";

            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, word);//第一个通配符
            ps.setObject(2, mean);//第二个通配符
            //4.执行
            ps.execute();
        } catch (Exception e) {
            System.out.println("未知错误3");
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, null);
        }
    }

    static void word_tuijian(){
        if(AddFragment.tuijianlist.size()!=0){
            return;
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select word from yesterday_words order by count DESC limit 10 ;";
            ps = conn.prepareStatement(sql);
            //执行并返回结果集
            res = ps.executeQuery();
            //处理结果集z
            while (res.next()) {//判断结果集下一条是否有数据，如果有数据，返回true，指针下移，否则结束
                String word = res.getString(1);
                if(!word.isEmpty()){
                    AddFragment.tuijianlist.add(word);
                }
            }
        } catch (Exception ignored) {
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }

}
