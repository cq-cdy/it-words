package com.example.ITwords;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ITwords.GetMysql.GetConnection;
import com.example.ITwords.GetMysql.UserMessage;
import com.example.ITwords.GetMysql.User_Score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllWordTestFragment extends Fragment {
    private static ArrayList<Word_180> word_180ArrayList = new ArrayList<>();
    private TextView high_score_180;
    private TextView time_180;
    private TextView temp;
    private TextView no1;
    private TextView no2;
    private TextView no3;
    private TextView no4;
    private TextView no5;
    private TextView no_1_sc;
    private TextView no_2_sc;
    private TextView no_3_sc;
    private TextView no_4_sc;
    private TextView no_5_sc;
    private TextView currentScore;
    private TextView newSore;
    private TextView targetWord;
    private TextView targetMean;
    private TextView questionWord;
    private TextView questionMean;
    private EditText editText;
    private Button begin_180, go_on;
    private int temp_score = 0;
    private ProgressBar progressBar4;
    private StringBuilder stringBuilder = new StringBuilder();
    private static ArrayList<User_Score> user_scoreArrayList;

    public AllWordTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_word_test, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        high_score_180 = requireActivity().findViewById(R.id.high_score_180);
        Thread a = new Thread() {
            @Override
            public void run() {
                super.run();
                getHighScore();
                getTenWords();
            }
        };
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        high_score_180.setText("最高纪录:" + high_score);

        temp = requireActivity().findViewById(R.id.temp);
        temp.setVisibility(View.GONE);
        currentScore = requireActivity().findViewById(R.id.currentScore);
        currentScore.setVisibility(View.INVISIBLE);
        TextView daangongbu = requireActivity().findViewById(R.id.daangongbu);
        daangongbu.setVisibility(View.INVISIBLE);
        TextView currentName = requireActivity().findViewById(R.id.currentName);
        newSore = requireActivity().findViewById(R.id.newScore_180);
        newSore.setVisibility(View.INVISIBLE);
        targetWord = requireActivity().findViewById(R.id.targetWord);
        targetWord.setVisibility(View.INVISIBLE);
        targetMean = requireActivity().findViewById(R.id.targetMean);
        targetMean.setVisibility(View.INVISIBLE);
        questionWord = requireActivity().findViewById(R.id.questionWord);
        questionWord.setVisibility(View.INVISIBLE);
        questionMean = requireActivity().findViewById(R.id.questionMean);
        questionMean.setVisibility(View.INVISIBLE);
        editText = requireActivity().findViewById(R.id.editText3);
        editText.setEnabled(false);
        go_on = requireActivity().findViewById(R.id.go_on);
        go_on.setEnabled(false);

        TextView textView = requireView().findViewById(R.id.mytesttext_1);
        currentName = requireActivity().findViewById(R.id.currentName);
        time_180 = requireActivity().findViewById(R.id.time_180);
        begin_180 = requireActivity().findViewById(R.id.begin_180);
        currentName.setText(MainActivity.mSharedPreferences2.getString("dqyh", ""));
        final CountDownTimer jishiqi = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String value = String.valueOf((int) (millisUntilFinished / 1000));
                time_180.setText("倒计时:" + value + "s");
            }

            @Override
            public void onFinish() {
                if (temp_score > high_score) {
                    high_score = temp_score;
                    high_score_180.setText("最高纪录:" + high_score);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserMessage.changescore(high_score);
                        }
                    }).start();
                }
                targetWord.setVisibility(View.INVISIBLE);
                targetMean.setVisibility(View.INVISIBLE);
                questionWord.setVisibility(View.INVISIBLE);
                questionMean.setVisibility(View.INVISIBLE);
                editText.setEnabled(false);
                go_on.setEnabled(false);
                begin_180.setVisibility(View.VISIBLE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("时间到，你在180秒内得到了" + temp_score + "分");
                builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

            }
        };
        begin_180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word_180 word_180;
                temp_score = 0;
                currentScore.setText("当前得分:0");
                newSore.setVisibility(View.INVISIBLE);
                try {
                    word_180 = word_180ArrayList.remove(0);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getActivity(), "无法开始，暂时访问不到服务器", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                begin_180.setVisibility(View.INVISIBLE);
                currentScore.setVisibility(View.VISIBLE);
                targetWord.setVisibility(View.VISIBLE);
                targetMean.setVisibility(View.VISIBLE);
                questionWord.setVisibility(View.VISIBLE);
                questionMean.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                go_on.setEnabled(true);
                stringBuilder.append(word_180.word_180);
                temp.setText(stringBuilder);
                stringBuilder = toWord_(stringBuilder.toString());
                questionWord.setText(stringBuilder);
                questionMean.setText(word_180.getMean_180());
                System.out.println(stringBuilder);
                stringBuilder.setLength(0);
                jishiqi.start();
                editText.setText("");

            }
        });

        go_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().toLowerCase().equals(temp.getText().toString().toLowerCase())) {
                    temp_score++;
                    currentScore.setText("当前得分:" + temp_score);
                    Word_180 word_180 = word_180ArrayList.remove(0);
                    stringBuilder.append(word_180.word_180);
                    temp.setText(stringBuilder);
                    stringBuilder = toWord_(stringBuilder.toString());
                    questionWord.setText(stringBuilder);
                    questionMean.setText(word_180.getMean_180());
                    stringBuilder.setLength(0);
                    editText.setText("");
                } else {
                    if (high_score < temp_score) {
                        high_score = temp_score;
                        high_score_180.setText("最高纪录:" + high_score);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UserMessage.changescore(high_score);
                            }
                        }).start();
                    }
                    if (word_180ArrayList.size() < 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getTenWords();
                            }
                        }).start();
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle(currentScore.getText().toString() + ",正确答案：" + temp.getText().toString() + "," + questionMean.getText().toString() + ",再接再厉");
                    builder.setPositiveButton("继续答题", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create();
                    builder.show();
                    temp_score = 0;
                    newSore.setVisibility(View.INVISIBLE);
                    begin_180.setVisibility(View.VISIBLE);
                    currentScore.setVisibility(View.INVISIBLE);
                    targetWord.setVisibility(View.INVISIBLE);
                    targetMean.setVisibility(View.INVISIBLE);
                    questionWord.setVisibility(View.INVISIBLE);
                    questionMean.setVisibility(View.INVISIBLE);
                    editText.setEnabled(false);
                    go_on.setEnabled(false);
                    jishiqi.cancel();

                }
                if (temp_score > high_score) {
                    newSore.setVisibility(View.VISIBLE);
                }
                System.out.println("动态增长：" + word_180ArrayList.size());
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        no1 = requireActivity().findViewById(R.id.no1_180);
        no2 = requireActivity().findViewById(R.id.no2_180);
        no3 = requireActivity().findViewById(R.id.no3_180);
        no4 = requireActivity().findViewById(R.id.no4_180);
        no5 = requireActivity().findViewById(R.id.no5_180);
        no_1_sc = requireActivity().findViewById(R.id.no_1_sc);
        no_2_sc = requireActivity().findViewById(R.id.no_2_sc);
        no_3_sc = requireActivity().findViewById(R.id.no_3_sc);
        no_4_sc = requireActivity().findViewById(R.id.no_4_sc);
        no_5_sc = requireActivity().findViewById(R.id.no_5_sc);
        progressBar4 = requireActivity().findViewById(R.id.progressBar4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    user_scoreArrayList = UserMessage.bestscoreuser();//加载排行；
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getActivity(), "服务器访问异常", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        handler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                try {
                                    no1.setText( user_scoreArrayList.get(0).getUsername() );no_1_sc.setText(String.valueOf(user_scoreArrayList.get(0).getScore()));
                                    no2.setText(user_scoreArrayList.get(1).getUsername());no_2_sc.setText(String.valueOf(user_scoreArrayList.get(1).getScore()));
                                    no3.setText(user_scoreArrayList.get(2).getUsername());no_3_sc.setText(String.valueOf(user_scoreArrayList.get(2).getScore()));
                                    no4.setText (user_scoreArrayList.get(3).getUsername());no_4_sc.setText(String.valueOf(user_scoreArrayList.get(3).getScore()));
                                    no5.setText( user_scoreArrayList.get(4).getUsername());no_5_sc.setText(String.valueOf(user_scoreArrayList.get(4).getScore()));
                                } catch (Exception e) {
                                    Toast toast = Toast.makeText(getActivity(), "服务器访问异常", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                progressBar4.setVisibility(View.GONE);
                            }
                        });
                    }
                }.start();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("第二个被毁灭");
    }

    private static void getTenWords() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select * from word order by rand( ) limit 10;";
            ps = conn.prepareStatement(sql);
            //执行并返回结果集
            res = ps.executeQuery();
            //处理结果集
            while (res.next()) {//判断结果集下一条是否有数据，如果有数据，返回true，指针下移，否则结束
                String word_180 = res.getString(1);
                String mean_180 = res.getString(2);
                word_180ArrayList.add(new Word_180(word_180, mean_180));
            }
        } catch (Exception e) {
        } finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }

    private static int high_score;

    private static void getHighScore() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = GetConnection.getConnection();
            String sql = "select highscore from english_test_score where username= '" + MainActivity.mSharedPreferences2.getString("dqyh", "") + "'";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            while (res.next()) {
                high_score = res.getInt(1);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源关闭
        finally {
            GetConnection.closeResource(conn, ps, res);
        }
    }

    private static StringBuilder toWord_(String word) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] c;
        c = word.toCharArray();
        stringBuilder.append(c[0]);
        if (c.length < 3) {
            for (int i = 0; i < c.length; i++) {
                c[i] = '_';
                stringBuilder.append(c[i]);
            }
            return stringBuilder;
        } else {
            for (int i = 1; i < c.length - 1; i++) {
                if (c[i] == ' ' || c[i] == '(' || c[i] == '（') {
                    stringBuilder.append(c[i]);
                    i += 1;
                    stringBuilder.append(c[i]);
                } else if (c[i] == '(' || c[i] == ')' || c[i] == '（' || c[i] == '）' || c[i] == '-' || c[i] == '—') {
                    stringBuilder.append(c[i]);
                } else {
                    c[i] = '_';
                    stringBuilder.append(c[i]);
                }
            }
            stringBuilder.append(c[c.length - 1]);
        }
        return stringBuilder;
    }
}

class Word_180 {
    String word_180, mean_180;

    public Word_180(String word_180, String mean_180) {
        this.word_180 = word_180;
        this.mean_180 = mean_180;
    }

    public String getWord_180() {
        return word_180;
    }

    public void setWord_180(String word_180) {
        this.word_180 = word_180;
    }

    public String getMean_180() {
        return mean_180;
    }

    public void setMean_180(String mean_180) {
        this.mean_180 = mean_180;
    }
}
