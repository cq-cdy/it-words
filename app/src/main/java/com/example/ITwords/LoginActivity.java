package com.example.ITwords;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ITwords.GetMysql.GetConnection;
import com.example.ITwords.GetMysql.UserMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity {
    private Button buttonlogin;
    private TextView usertext;
    private TextView passwordtext;
    private View progressBarlogin;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.xz);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_xz);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        imageView.startAnimation(animation);
        TextView findpassword = findViewById(R.id.findpassword);
        findpassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        TextView zhuceText = findViewById(R.id.zhuceText);
        zhuceText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        buttonlogin = findViewById(R.id.buttonlogin);
        usertext = findViewById(R.id.editText);
        passwordtext = findViewById(R.id.editText2);
        progressBarlogin = findViewById(R.id.progressBarlogin);
        progressBarlogin.setVisibility(View.INVISIBLE);
        findpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usertext.setEnabled(false);
                passwordtext.setEnabled(false);
                if (usertext.getText().toString().trim().equals("") && passwordtext.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "输入不允许为空", Toast.LENGTH_SHORT).show();
                    progressBarlogin.setVisibility(View.INVISIBLE);
                    usertext.setEnabled(true);
                    passwordtext.setEnabled(true);
                    buttonlogin.setEnabled(true);
                    buttonlogin.setEnabled(true);
                    return;
                }
                progressBarlogin.setVisibility(View.VISIBLE);
                buttonlogin.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        String user = usertext.getText().toString().trim();
                        String password = passwordtext.getText().toString().trim();
                        if (!AddFragment.isNetworkConnected(getApplication())) {
                            Toast.makeText(getApplication(), "当前无网络连接，请先检查网络", Toast.LENGTH_SHORT).show();

                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarlogin.setVisibility(View.INVISIBLE);
                                            usertext.setEnabled(true);
                                            passwordtext.setEnabled(true);
                                            buttonlogin.setEnabled(true);
                                        }
                                    });
                                }
                            }.start();
                            return;
                        }

                        if (UserMessage.islogin(user, password)) {
                            MainActivity.mEditor2.putString("dqyh", user);
                            MainActivity.mEditor2.commit();
                            MainActivity.currentuser = user;
                            MainActivity.mEditor.putInt("first", 1);
                            MainActivity.mEditor.commit();
                            WordViewModel wordViewModel = new WordViewModel(getApplication());

                            Connection conn = null;
                            PreparedStatement ps = null;
                            ResultSet res = null;
                            try {//从服务器端到读取该用户的单词到本地sqllite
                                conn = GetConnection.getConnection();
                                String sql = "select * from " + MainActivity.mSharedPreferences2.getString("dqyh", "") + "tab " + ";";
                                ps = conn.prepareStatement(sql);
                                res = ps.executeQuery();
                                while (res.next()) {//判断结果集下一条是否有数据，如果有数据，返回true，指针下移，否则结束
                                    String word = res.getString(1);
                                    String mean = res.getString(2);
                                    wordViewModel.insertWords(new Word(word, mean));
                                }
                                ps.execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                GetConnection.closeResource(conn, ps, res);
                            }
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            Toast toast = Toast.makeText(getApplicationContext(), "" + MainActivity.currentuser + ",登录成功", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            finish();
                        } else {
                            if (!GetConnection.isconnectionsuccessful)

                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBarlogin.setVisibility(View.INVISIBLE);
                                                usertext.setEnabled(true);
                                                passwordtext.setEnabled(true);
                                                buttonlogin.setEnabled(true);
                                            }
                                        });
                                    }
                                }.start();

                            else
                                Toast.makeText(getApplication(), "该用户名不存在 or 用户名或密码错误", Toast.LENGTH_SHORT).show();
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarlogin.setVisibility(View.INVISIBLE);
                                            usertext.setEnabled(true);
                                            passwordtext.setEnabled(true);
                                            buttonlogin.setEnabled(true);
                                        }
                                    });
                                }
                            }.start();

                        }
                        progressBarlogin.setVisibility(View.INVISIBLE);
                        Looper.loop();
                        // finish();
                    }
                }).start();
            }
        });

        zhuceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ZhuceActivity.class);
                startActivity(intent);
            }
        });
    }


}
