package com.example.ITwords;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ITwords.GetMysql.GetConnection;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    public static SharedPreferences mSharedPreferences;//记录是否登录
    public static SharedPreferences.Editor mEditor;
    public static String currentuser = "";
    public static SharedPreferences mSharedPreferences2;//记录用户名
    public static SharedPreferences.Editor mEditor2;
    public static ActionBar actionBar;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity被创建***********************");
        getURL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                try {
                    conn = GetConnection.getConnection();
                    if (conn == null) {
                        AddFragment.isITpipei = false;
                    }
                } catch (Exception e) {
                    AddFragment.isITpipei = false;
                }
                //5.资源关闭
                finally {
                    GetConnection.closeResource(conn, null, null);
                }
            }
        }).start();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认键盘不弹出
        mSharedPreferences2 = getSharedPreferences("dqyh", MODE_PRIVATE);
        mEditor2 = mSharedPreferences2.edit();
        mSharedPreferences2 = getSharedPreferences("dqyh", 0);
        mSharedPreferences = getSharedPreferences("first", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mSharedPreferences = getSharedPreferences("first", 0);
        System.out.println(MainActivity.mSharedPreferences2.getString("dqyh", ""));

        navController = Navigation.findNavController(findViewById(R.id.fragment));
        NavigationUI.setupActionBarWithNavController(this, navController);
        if (mSharedPreferences.getInt("first", 0) != 0) {//如果处于未登录状态就不创建集合
            //通过独立的构造方法驱动数据工厂来从sqllite中单词集合得到AddFragment.listwords
           new WordRepository(getApplicationContext(), 1);
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("单词集合被毁灭**************************************");

    }

    @Override

    public void onBackPressed() {
        super.onBackPressed();
        navController.navigateUp();
    }


    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }

    private static String getProperty(android.content.res.Resources resources,
                                     String key) {
        Properties properties = getProperties(resources);
        assert properties != null;
        return properties.getProperty(key);
    }

    /**获取配置文件中的信息.
     * @param resources
     * @return
     */
    private static Properties getProperties(
            android.content.res.Resources resources) {
        Properties props = new Properties();
        try {
            props.load(resources.openRawResource(R.raw.url));
        } catch (Resources.NotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return props;
    }
    private boolean getURL(){
        GetConnection.URL=getProperty(getResources(), "URL");
        GetConnection.password = getProperty(getResources(), "password");
        System.out.println(GetConnection.URL+".."+GetConnection.password);
        return !(GetConnection.URL==null || GetConnection.password == null);
    }


}
