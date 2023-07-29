package com.example.ITwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ITwords.GetMysql.GetConnection;
import com.example.ITwords.SearchUser.GetUserArticle;
import com.example.ITwords.SearchUser.GetUserName;
import com.example.ITwords.SearchUser.UserArticleAdapter;
import com.example.ITwords.SearchUser.UserWord;
import com.example.ITwords.SearchUser.UserWordsAdapter;
import com.example.ITwords.SearchUser.User_Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {
    RecyclerView userwordsrecycle, userarticlerecycle;
    UserWordsAdapter userWordsAdapter;
    UserArticleAdapter userArticleAdapter;
    private TextView searchEditText, userlowtext, usermidtext, userhightext, userhighscore;
    private Button searchbutton;
    private TextView usernametext, userwordscount;
    private ProgressBar lowBar, midBar, highBar, userzhuanquanquan1, zhuanquanquan2;
    static int lowbai, midbai, highbai;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private ScrollView scrollView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        getSupportActionBar().hide();
        final ArrayList<UserWord> lowdif = new ArrayList<UserWord>();
        final ArrayList<UserWord> middif = new ArrayList<UserWord>();
        final ArrayList<UserWord> highdif = new ArrayList<UserWord>();
        searchEditText = findViewById(R.id.searchEditText);
        searchbutton = findViewById(R.id.searchbutton);
        usernametext = findViewById(R.id.usernametext);
        userwordscount = findViewById(R.id.userwordscount);
        userlowtext = findViewById(R.id.userlowtext);
        usermidtext = findViewById(R.id.usermidtext);
        userhightext = findViewById(R.id.userhightext);
        userhighscore = findViewById(R.id.user_high_score);
        lowBar = findViewById(R.id.lowBar);
        midBar = findViewById(R.id.midBar);
        highBar = findViewById(R.id.highBar);
        userwordsrecycle = findViewById(R.id.userwordsrecycle);
        userarticlerecycle = findViewById(R.id.userarticlerecycle);


        userzhuanquanquan1 = findViewById(R.id.userzhuanquanquan1);
        userzhuanquanquan1.setVisibility(View.INVISIBLE);
        zhuanquanquan2 = findViewById(R.id.usershoucanggress);
        zhuanquanquan2.setVisibility(View.INVISIBLE);
        scrollView3 = findViewById(R.id.scrollView3);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setEnabled(false);
                searchbutton.setEnabled(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                scrollView3.scrollTo(0, 0);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                userzhuanquanquan1.setVisibility(View.VISIBLE);
                zhuanquanquan2.setVisibility(View.VISIBLE);
                if (searchEditText.getText().toString().trim().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    userzhuanquanquan1.setVisibility(View.INVISIBLE);
                    zhuanquanquan2.setVisibility(View.INVISIBLE);
                    searchEditText.setEnabled(true);
                    searchbutton.setEnabled(true);
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (GetUserName.getusername(searchEditText.getText().toString()) == null) {
                            Looper.prepare();
                            Toast toast = Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            userzhuanquanquan1.setVisibility(View.INVISIBLE);
                            zhuanquanquan2.setVisibility(View.INVISIBLE);
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            searchEditText.setEnabled(true);
                                            searchbutton.setEnabled(true);
                                        }
                                    });
                                }
                            }.start();
                            Looper.loop();
                        } else {
                            //基本信息代码
                            ArrayList<UserWord> userWordArrayList;
                            userWordArrayList = GetUserName.getuserwordslist(searchEditText.getText().toString());
                            final int highscore = GetUserName.getuser_high_score(searchEditText.getText().toString());
                            for (UserWord w : userWordArrayList) {
                                if (w.getWord().contains(" ") && w.getWord().length() < 15) {
                                    middif.add(w);
                                }
                                if (w.getWord().contains(" ") && w.getWord().length() >= 15) {
                                    highdif.add(w);
                                }
                                if (!w.getWord().contains(" ")) {
                                    lowdif.add(w);
                                }
                            }
                            try {
                                lowbai = (100 * lowdif.size() / userWordArrayList.size());
                                midbai = (100 * middif.size() / userWordArrayList.size());
                                highbai = (100 * highdif.size() / userWordArrayList.size());
                            } catch (Exception e) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Ta似乎清理了词库", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }.start();
                                //
                            }
                            final ArrayList<UserWord> finalUserWordArrayList = userWordArrayList;//因为上面需要更新集合，所以在这里定义final
                            //更新视图代码
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void run() {
                                            //基本信息视图更新代码👇
                                            usernametext.setText("" + searchEditText.getText().toString() + "");
                                            userwordscount.setText("共收藏:" + finalUserWordArrayList.size());
                                            userlowtext.setText("普通词汇:(" + lowdif.size() + ")");
                                            usermidtext.setText("进阶短语:(" + middif.size() + ")");
                                            userhightext.setText("专业术语:(" + highdif.size() + ")");
                                            userhighscore.setText("180秒挑战最高记录:" + highscore);
                                            lowBar.setProgress(lowbai);
                                            midBar.setProgress(midbai);
                                            highBar.setProgress(highbai);
                                            finalUserWordArrayList.clear();
                                            lowdif.clear();
                                            middif.clear();
                                            highdif.clear();
                                            userzhuanquanquan1.setVisibility(View.INVISIBLE);
                                            //用户发帖代码视图更新👇
                                        }
                                    });
                                }
                            }.start();
                        }

                    }
                }).start();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (GetUserName.getusername(searchEditText.getText().toString()) == null) {
                            Looper.prepare();
                            Looper.loop();
                        } else {
                            ArrayList<UserWord> userWordArrayList;
                            ArrayList<User_Article> user_articleArrayList;
                            userWordArrayList = GetUserName.getuserwordslist(searchEditText.getText().toString());
                            user_articleArrayList = GetUserArticle.getuserArticle(searchEditText.getText().toString());
                            userWordsAdapter = new UserWordsAdapter(userWordArrayList, getApplication(), show(searchEditText.getText().toString()));
                            userArticleAdapter = new UserArticleAdapter(user_articleArrayList, getApplication(), searchEditText.getText().toString());
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            userwordsrecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                            userwordsrecycle.setAdapter(userWordsAdapter);
                                            userarticlerecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                            userarticlerecycle.setAdapter(userArticleAdapter);
                                            zhuanquanquan2.setVisibility(View.INVISIBLE);
                                            searchEditText.setEnabled(true);
                                            searchbutton.setEnabled(true);
                                        }
                                    });
                                }
                            }.start();
                        }

                    }
                }).start();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static int show(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {

            conn = GetConnection.getConnection();
            String sql = "select ispublic from user where user='" + name + "'";
            ps = conn.prepareStatement(sql);

            res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
        return -1;
    }

}
