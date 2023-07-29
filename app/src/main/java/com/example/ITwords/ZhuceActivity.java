package com.example.ITwords;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.ITwords.GetMysql.UserMessage;

public class ZhuceActivity extends AppCompatActivity {
    private TextView newuser, newpassword, newemail, newtel, newusertip, newusertip2;
    private Button surezhuce;
    private View zhucequanquan2;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onResume();
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
        setContentView(R.layout.activity_zhuce);
        getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.imageView19);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_xz);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        imageView.startAnimation(animation);
        newuser = findViewById(R.id.newuser);
        newpassword = findViewById(R.id.newpassword);
        newemail = findViewById(R.id.newemail);
        newtel = findViewById(R.id.newtel);
        surezhuce = findViewById(R.id.surezhuce);
        zhucequanquan2 = findViewById(R.id.progressBar2);
        zhucequanquan2.setVisibility(View.INVISIBLE);
        newusertip = findViewById(R.id.newusertip);
        newusertip.setVisibility(View.INVISIBLE);
        newusertip2 = findViewById(R.id.newusertip2);
        newusertip2.setVisibility(View.INVISIBLE);
        newuser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = newuser.getText().toString();
                if (temp.contains(".") || temp.contains("?") || temp.contains("-") || temp.contains("$") || temp.contains(" ") ||
                        temp.contains("@") || temp.contains("'\'") || temp.contains("/")||temp.contains(",")||temp.contains("，")) {
                    newusertip2.setVisibility(View.VISIBLE);
                    surezhuce.setEnabled(false);
                    return;
                }
                if (temp.length() > 14) {
                    newusertip.setVisibility(View.VISIBLE);
                    surezhuce.setEnabled(false);
                    return;
                } else surezhuce.setEnabled(true);
                newusertip.setVisibility(View.INVISIBLE);
                newusertip2.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        surezhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surezhuce.setVisibility(View.GONE);
                zhucequanquan2.setVisibility(View.VISIBLE);
                newuser .setEnabled(false);
                newpassword.setEnabled(false);
                newemail .setEnabled(false);
                newtel .setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        String user = newuser.getText().toString().trim();
                        String password = newpassword.getText().toString().trim();
                        String email = newemail.getText().toString().trim();
                        String tel = newtel.getText().toString().trim();
                        if (!AddFragment.isNetworkConnected(getApplication())) {
                            Toast.makeText(getApplication(), "当前无网络连接，请先检查网络", Toast.LENGTH_SHORT).show();
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            surezhuce.setVisibility(View.VISIBLE);
                                            newuser .setEnabled(true);
                                            newpassword.setEnabled(true);
                                            newemail .setEnabled(true);
                                            newtel .setEnabled(true);
                                        }
                                    });
                                }
                            }.start();
                            zhucequanquan2.setVisibility(View.INVISIBLE);
                            return;
                        }

                        if (user.equals("") || password.equals("")) {
                            Toast.makeText(getApplication(), "用户名,密码输入不能为空", Toast.LENGTH_SHORT).show();
                            zhucequanquan2.setVisibility(View.INVISIBLE);
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            surezhuce.setVisibility(View.VISIBLE);
                                            newuser .setEnabled(true);
                                            newpassword.setEnabled(true);
                                            newemail .setEnabled(true);
                                            newtel .setEnabled(true);
                                        }
                                    });
                                }
                            }.start();
                            zhucequanquan2.setVisibility(View.INVISIBLE);
                            return;
                        }

                        try {
                            if (UserMessage.addUser(user, password, email, tel)) {
                                Toast.makeText(getApplication(), "注册成功,请登录", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                newuser.setText("");
                                Toast toast2 = Toast.makeText(getApplicationContext(), "注册失败，该用户名已存在", Toast.LENGTH_SHORT);
                                toast2.setGravity(Gravity.CENTER, 0, 0);
                                toast2.show();
                                zhucequanquan2.setVisibility(View.INVISIBLE);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                surezhuce.setVisibility(View.VISIBLE);
                                                newuser .setEnabled(true);
                                                newpassword.setEnabled(true);
                                                newemail .setEnabled(true);
                                                newtel .setEnabled(true);
                                            }
                                        });
                                    }
                                }.start();
                                zhucequanquan2.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "访问不到服务器，注册失败", Toast.LENGTH_SHORT).show();
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            surezhuce.setVisibility(View.VISIBLE);
                                            newuser .setEnabled(true);
                                            newpassword.setEnabled(true);
                                            newemail .setEnabled(true);
                                            newtel .setEnabled(true);
                                        }
                                    });
                                }
                            }.start();
                            zhucequanquan2.setVisibility(View.INVISIBLE);
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        surezhuce.setVisibility(View.VISIBLE);
                                        newuser .setEnabled(true);
                                        newpassword.setEnabled(true);
                                        newemail .setEnabled(true);
                                        newtel .setEnabled(true);
                                    }
                                });
                            }
                        }.start();
                        zhucequanquan2.setVisibility(View.INVISIBLE);
                        Looper.loop();

                    }
                }).start();

            }
        });
    }
}
