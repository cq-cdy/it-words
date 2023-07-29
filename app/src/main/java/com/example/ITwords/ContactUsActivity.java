package com.example.ITwords;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUsActivity extends AppCompatActivity {
    TextView QQNUM, myqq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().hide();
        QQNUM = findViewById(R.id.QQNUM);
        QQNUM.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        QQNUM.setOnClickListener(new View.OnClickListener() {
            String key = "GCBg0qTx6uMSOc8oGQ1w-kEUJ6l5j1hO";

            @Override
            public void onClick(View v) {
                joinQQGroup(key);
            }
        });
        myqq = findViewById(R.id.Myqq);
        myqq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        myqq.setOnClickListener(new View.OnClickListener() {
            private String qqnumber = "1044779679";

            @Override
            public void onClick(View v) {
                try {
                    String qqUrl = "mqqwpa://im/chat?chat_type=wpa&version=1&uin=" + qqnumber;//是发送过去的qq号码
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "跳转至腾讯qq", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "未检测到手机上的腾讯QQ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }
            }
        });

    }

    /****************
     *
     * 发起添加群流程。群号：大学生Java大数据方向(651685861) 的 key 为： GCBg0qTx6uMSOc8oGQ1w-kEUJ6l5j1hO
     * 调用 joinQQGroup(GCBg0qTx6uMSOc8oGQ1w-kEUJ6l5j1hO) 即可发起手Q客户端申请加群 大学生Java大数据方向(651685861)
     *
     * @param key 由官网生成的key

     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/

    public boolean joinQQGroup(String key) {

        Intent intent = new Intent();

        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {

            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(getApplicationContext(), "未检测到手机上的腾讯QQ", Toast.LENGTH_SHORT).show();
            return false;

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
