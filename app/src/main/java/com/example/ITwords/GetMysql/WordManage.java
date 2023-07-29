package com.example.ITwords.GetMysql;


public class WordManage {

    public static void addword(String word, String mean) {
        WordDemo.addWord(word, mean);
    }

    public static void delWords(String word) {
        WordDemo.delWords(word);
    }

    public static void wordComplement(String word, String mean) {
        WordDemo.wordComplement(word, mean);
    }
   public  static void word_tuijian(){ WordDemo.word_tuijian(); }
}
