package com.example.ITwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ITwords.GetMysql.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TouGaoActivity extends AppCompatActivity {
    private EditText editTextmessage;
    private Button tougao_button;
    private TextView tipinput;
    private String temp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tou_gao);
        final String NAME = MainActivity.mSharedPreferences2.getString("dqyh", "");
        Objects.requireNonNull(getSupportActionBar()).hide();
        tipinput = findViewById(R.id.tipinput);
        tipinput.setText("发布后所有用户都可以看到你的文章");
        editTextmessage = findViewById(R.id.tougaomessage);
        EditText editTextusername = findViewById(R.id.tougao_usernmae);
        tougao_button = findViewById(R.id.tougao_button);
        tougao_button.setEnabled(false);
        editTextusername.setText("作者:" + NAME);
        editTextmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTextmessage.getText().toString().trim().isEmpty()) {
                    tougao_button.setEnabled(true);
                } else tougao_button.setEnabled(false);
                tipinput.setText("你还可以输入" + (1500 - editTextmessage.getText().toString().length()) + "个字");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tougao_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1500 - editTextmessage.getText().toString().length() < 0) {
                    Toast.makeText(getApplicationContext(), "你的文章字数太多了，删减一些吧", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            fabuwenzhang(editTextmessage.getText().toString(), NAME);
                        }
                    }).start();
                }
                if (AddFragment.isITpipei) {
                    Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                    //收起键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "网络波动，发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("SimpleDateFormat")
    private void fabuwenzhang(String message, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = GetConnection.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "insert into article_by_user (article_message,by_user,agree,date)values(?,?,?,?);";

            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, message);//第一个通配符
            ps.setObject(2, name);
            ps.setObject(3, 0);//第二个通配符
            ps.setObject(4, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeResource(conn, ps, null);
        }
    }

}
