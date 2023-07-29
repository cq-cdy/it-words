package com.example.ITwords;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private LiveData<List<Word>> allWordsLive;
    private WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        allWordsLive = wordDao.getAllWordsLive();
    }

    WordRepository(Context context, int a) {//这个构造方法只用来 创建一个单词集合，只用一次
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        allWordsLive = wordDao.getAllWordsLive();
        new Thread() {//开多线程 进行设置
            @Override
            public void run() {
                super.run();
                setWordList();//开新线程来执行操作
                System.out.println("创建了单词集合");
            }
        }.start();
    }


    public void insertWords(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    void updateWords(Word... words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    void deleteWords(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    void deleteAllWords(Word... words) {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    private void setWordList() {
        new GetListAsyncTask(wordDao).execute();
    }

    LiveData<List<Word>> getAllWordsLive() {

        return allWordsLive;
    }

    LiveData<List<Word>> findWordsWithPatten(String patten) {
        if (WordsFragment.x == 1) {
            return wordDao.findWordsWithPatten(patten + "%");
        } else
            return wordDao.findWordsWithPatten("%" + patten + "%");
    }

    static class GetListAsyncTask extends AsyncTask<Word, Void, Void> {

        GetListAsyncTask(WordDao wordDao) {
            AddFragment.setList(wordDao.WORD_LIST());
            wordDao.WORD_LIST().clear();
        }

        @Override
        protected Void doInBackground(Word... words) {
            return null;
        }
    }

    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }

    }

    static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }

    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }

    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }

    }
}
