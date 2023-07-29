package com.example.ITwords.SearchUser;

import com.example.ITwords.GetMysql.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetUserArticle {
    public static ArrayList<User_Article> getuserArticle(String searchName) {
        ArrayList<User_Article> articleArrayList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select article_message,by_user,agree,date from article_by_user where by_user='" + searchName + "' ORDER BY date DESC";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                User_Article user_article = new User_Article();
                user_article.setUserarticle(res.getString(1));
                user_article.setByuser(res.getNString(2));
                user_article.setUseragree(res.getInt(3));
                user_article.setDate(res.getString(4));
                articleArrayList.add(user_article);
            }
            ps.execute();
            GetConnection.closeResource(conn, ps, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return articleArrayList;
    }
}
