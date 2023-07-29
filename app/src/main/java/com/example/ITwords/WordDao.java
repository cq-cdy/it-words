package com.example.ITwords;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWords(Word... words);

    @Update
    void updateWords(Word... words);

    @Delete
    void deleteWords(Word... words);

    @Query("DELETE FROM WORD")
    void deleteAllWords();

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    LiveData<List<Word>>getAllWordsLive();

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    List<Word> WORD_LIST();


    @Query("SELECT * FROM WORD WHERE english_word || chinese_meaning LIKE :patten ORDER BY ID DESC")
    LiveData<List<Word>> findWordsWithPatten(String patten);


}
