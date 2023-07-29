package com.example.ITwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserfeedbackActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;

    private static int niming = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userfeedback);
        textView = findViewById(R.id.Userfeedback);
        CheckBox checkBox = findViewById(R.id.isniming);
        button = findViewById(R.id.optionbutton);
        button.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    niming = 1;
                } else {
                    niming = 0;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (niming == 1) {
                            YonghuFankui.yijianfankui(textView.getText().toString(), "匿名");
                        } else {
                            YonghuFankui.yijianfankui(textView.getText().toString(), MainActivity.mSharedPreferences2.getString("dqyh", ""));
                        }
                    }

                }).start();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Toast.makeText(getApplicationContext(), "提交成功，感谢你宝贵的意见", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textView.getText().toString().trim().equals("")) {
                    button.setEnabled(false);
                } else button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
