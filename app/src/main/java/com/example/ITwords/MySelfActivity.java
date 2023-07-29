package com.example.ITwords;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ITwords.GetMysql.UserDemo;
import com.example.ITwords.SearchUser.GetUserArticle;
import com.example.ITwords.SearchUser.UserArticleAdapter;
import com.example.ITwords.SearchUser.User_Article;

import java.util.ArrayList;

public class MySelfActivity extends AppCompatActivity {
    protected static ArrayList<Word> mymidlist;
    private RecyclerView recyclerView;
    private static int myhighscore;
    private UserArticleAdapter myrecycleAdapter;
    private ProgressBar progressBar;
    private TextView myscore;
    private TextView myarticle;
    protected TextView mymidtext;
    protected TextView myhightext;
    ArrayList<User_Article> user_articleArrayList;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_self);
        getSupportActionBar().hide();
        TextView myname = findViewById(R.id.myname);
        TextView myword = findViewById(R.id.myshoucang);
        myscore = findViewById(R.id.myscore);
        myarticle = findViewById(R.id.myatricle);
        Button tuichudenglu = findViewById(R.id.tuichudenglu);
        progressBar = findViewById(R.id.jiazaimy);
        progressBar.setVisibility(View.VISIBLE);
        TextView mylowtext = findViewById(R.id.mylowtext);
        mymidtext = findViewById(R.id.mymidtext);
        myhightext = findViewById(R.id.myhightext);
        ProgressBar mylow = findViewById(R.id.mylow);
        ProgressBar mymid = findViewById(R.id.mymid);
        ProgressBar myhigh = findViewById(R.id.myhigh);
        View myback = findViewById(R.id.myback);
        myback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myname.setText("Hi," + MainActivity.mSharedPreferences2.getString("dqyh", ""));
        myword.setText(" 你的收藏录里有:" + AddFragment.listwords.size() + "条收藏记录");
        ArrayList<Word> mylowlist = new ArrayList<>();
        mymidlist = new ArrayList<>();
        ArrayList<Word> myhighlist = new ArrayList<>();
        int lowbai = 0, midbai = 0, highbai = 0;
        for (Word w : AddFragment.listwords) {
            if (w.getWord().contains(" ") && w.getWord().length() < 15) {
                mymidlist.add(w);
            }
            if (w.getWord().contains(" ") && w.getWord().length() >= 15) {
                myhighlist.add(w);
            }
            if (!w.getWord().contains(" ")) {
                mylowlist.add(w);
            }
        }
        try {
            lowbai = (100 * mylowlist.size() / AddFragment.listwords.size());
            midbai = (100 * mymidlist.size() / AddFragment.listwords.size());
            highbai = (100 * myhighlist.size() / AddFragment.listwords.size());
        } catch (Exception ignored) {

        }
        mylowtext.setText("普通词汇(" + mylowlist.size() + "):");
        mymidtext.setText("进阶短语(" + mymidlist.size() + "):");
        myhightext.setText("专业术语(" + myhighlist.size() + "):");
        mylow.setProgress(lowbai);
        mymid.setProgress(midbai);
        myhigh.setProgress(highbai);

        new Thread(new Runnable() {
            @Override
            public void run() {
                user_articleArrayList = GetUserArticle.getuserArticle(MainActivity.mSharedPreferences2.getString("dqyh", ""));
                System.out.println(user_articleArrayList.size() + "]]]");
                recyclerView = findViewById(R.id.myrecycleView);
                myhighscore = UserDemo.getMyhighscore();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                myrecycleAdapter = new UserArticleAdapter(user_articleArrayList, getApplication(), MainActivity.mSharedPreferences2.getString("dqyh", ""));
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(myrecycleAdapter);
                                progressBar.setVisibility(View.INVISIBLE);
                                myscore.setText("      你在180秒挑战中的最好成绩:" + myhighscore);
                                myarticle.setText(" 共发布帖子" + user_articleArrayList.size() + "篇");
                            }
                        });
                    }
                }.start();

            }
        }).start();

        tuichudenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MySelfActivity.this);
                builder.setTitle("确定要退出登录吗？");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WordViewModel wordViewModel;
                        wordViewModel = AddFragment.getWordViewModel();
                        try {
                            wordViewModel.deleteAllWords();
                            MainActivity.mEditor.putInt("first", 0);
                            MainActivity.mEditor.commit();
                            AddFragment.getT().setText("未登录");
                        } catch (Exception ignored) {

                        }
                        finish();
                    }
                });
                builder.create();
                builder.show();

            }
        });
    }
}
