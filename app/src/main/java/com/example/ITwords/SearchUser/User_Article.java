package com.example.ITwords.SearchUser;

public class User_Article {
    private String userarticle,byuser;
    private int useragree;
    private String date;

    String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    String getUserarticle() {
        return userarticle;
    }

    void setUserarticle(String userarticle) {
        this.userarticle = userarticle;
    }

    String getByuser() {
        return byuser;
    }

    void setByuser(String byuser) {
        this.byuser = byuser;
    }

    int getUseragree() {
        return useragree;
    }

    void setUseragree(int useragree) {
        this.useragree = useragree;
    }
}
