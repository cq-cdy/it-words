package com.example.ITwords.GetMysql;

public class User_Score {
    private String username;
    private int score;

    void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    void setUsername(String username) {
        this.username = username;
    }
}
