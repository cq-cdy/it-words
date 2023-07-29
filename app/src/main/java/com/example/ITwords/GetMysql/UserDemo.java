package com.example.ITwords.GetMysql;

import com.example.ITwords.MainActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class UserDemo {
    private static String usertab;
    static boolean islogin(final String user, final String password) {

        boolean islog = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select user,password from user ;";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                String nowuser = res.getString(1);
                String nowpassword = res.getString(2);
                if ((nowuser.equals(user) && nowpassword.equals(password))) {
                    MainActivity.currentuser = user;//锁定登录成功当前用户的表
                    islog = true;
                    break;
                }
            }
        } catch (Exception ignored) {
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return islog;
    }

    static void delAllword(final String user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = GetConnection.getConnection();
                    //2.预编译sql语句，返回PrepareStatement的实例
                    String sql = "delete from " + MainActivity.mSharedPreferences2.getString("dqyh", "") + "tab;";
                    ps = conn.prepareStatement(sql);
                    //4.执行
                    ps.execute();
                } catch (Exception e) {
                    System.out.println("未知错误");
                }
                //5.资源关闭
                finally {
                    GetConnection.closeResource(conn, ps, null);
                }
            }
        }).start();
    }

    static void addUser(final String user, final String password, final String email, final String tel) throws Exception {
        String userID = "";
        Connection conn = null;
        PreparedStatement ps = null;
        conn = GetConnection.getConnection();
        String sql = "insert into user(userID,user,password,email,tel)values(?,?,?,?,?)";
        ps = conn.prepareStatement(sql);
        char c = (char) (new Random().nextInt(25) + 65);
        userID += c;
        for (int i = 0; i < 6; i++) {
            userID += new Random().nextInt(9);//生成随即userID
        }
        ps.setObject(1, userID);//第一个通配符
        ps.setObject(2, user);//第二个通配符
        ps.setObject(3, password);//第三个通配符
        ps.setObject(4, email);
        ps.setObject(5, tel);
        UserDemo.newusertab(user);
        newusertab(user);
        ps.execute();
        GetConnection.closeResource(conn, ps, null);
    }


    static boolean isExit(final String username) {
        boolean flag = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select user from user;";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                String oldusername = res.getString(1);
                if (username.toLowerCase().equals(oldusername.toLowerCase())) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeResource(conn, ps, null);
        }
        return flag;
    }

    private static void newusertab(String user) {
        usertab = user + "tab";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "CREATE table " + usertab + "( word varchar(100),	mean varchar(100));";
            ps = conn.prepareStatement(sql);
            ps.execute();
            copyword(user);
            score(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            GetConnection.closeResource(conn, ps, null);
        }
    }

    private static void copyword(String user) {
        usertab = user + "tab";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select * from word;";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                copywordadd("project", "项目");
                copywordadd("mainfest", "清单");
                copywordadd("database", "数据库");
                copywordadd("character", "字符");
                copywordadd("binay", "二进制");
                copywordadd("abstract", "抽象");
                break;
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }

    private static void copywordadd(String word, String mean) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "insert into " + usertab + "(word,mean)values(?,?);";

            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, word);//第一个通配符
            ps.setObject(2, mean);//第二个通配符
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

    private static void score(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();

            String sql = "insert into english_test_score (username,highscore)values(?,?);";

            ps = conn.prepareStatement(sql);
            ps.setObject(1, username);
            ps.setObject(2, 0);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, null);
        }

    }

    static ArrayList<User_Score> bestscoreuser() {
        ArrayList<User_Score> arrayList = new ArrayList<User_Score>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            int i = 0;
            conn = GetConnection.getConnection();
            String sql = "select * from english_test_score ORDER BY highscore DESC;;";
            ps = conn.prepareStatement(sql);

            res = ps.executeQuery();
            while (res.next()) {
                i += 1;
                String username = res.getString(1);
                int score = res.getInt(2);
                User_Score user_score = new User_Score();
                user_score.setUsername(username);
                user_score.setScore(score);
                arrayList.add(user_score);
                if (i == 5) {
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
        return arrayList;
    }

    public static int getMyhighscore() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int myhighscore = 0;
        try {
            conn = GetConnection.getConnection();
            String sql = "select highscore from english_test_score where username= '" + MainActivity.mSharedPreferences2.getString("dqyh", "") + "'";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                myhighscore = res.getInt(1);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return myhighscore;

    }

    static void changescore(int tempscore) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            conn = GetConnection.getConnection();
            String sql = "UPDATE english_test_score set highscore=? where username=?";

            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, tempscore);//第一个通配符
            ps.setObject(2, MainActivity.mSharedPreferences2.getString("dqyh", ""));//第二个通配符
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
