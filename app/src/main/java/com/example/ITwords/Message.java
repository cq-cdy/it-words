package com.example.ITwords;
class Message{
    private String message,byuser,date;
    private int agree;

    String getDate() {
        return date;
    }
    void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String getByuser() {
        return byuser;
    }

    void setByuser(String byuser) {
        this.byuser = byuser;
    }

    int getAgree() {
        return agree;
    }

    void setAgree(int agree) {
        this.agree = agree;
    }
}
