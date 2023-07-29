package com.example.ITwords.GetMysql;

import java.util.ArrayList;

public class UserMessage {
    public static boolean addUser(String user, String password, String email, String tel) throws Exception {
        if (UserDemo.isExit(user)) {
            return false;
        } else {
            UserDemo.addUser(user, password, email, tel);
            return true;
        }
    }

    public static void delAllword(String user) {
        UserDemo.delAllword(user);
    }

    public static boolean islogin(String user, String password) {
        return UserDemo.islogin(user, password);
    }


    private static ArrayList<User_Score> arrayList;

    public static ArrayList<User_Score> bestscoreuser() {
        arrayList = UserDemo.bestscoreuser();
        return arrayList;
    }



    public static void changescore(final int score) {
        UserDemo.changescore(score);
    }


}
