package com.example.ITwords;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWordTestFragment extends Fragment {
    private static ArrayList<Word> tempwordlist = new ArrayList<>();
    private TextView my_easy;
    private TextView my_common;
    private TextView my_difficult;
    private TextView tiaozhuantext;
    private TextView righttext;
    private TextView my_mean, answer1, answer2, answer3, answer4, right_score, errscore;
    private ProgressBar tiaozhuanBar5;
    private int num_right = 0;
    private int num_err = 0;
    private static ArrayList<String> daanList;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("第一个被创建");
    }

    public MyWordTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_word_test, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        righttext = requireActivity().findViewById(R.id.righttext);
        righttext.setVisibility(View.GONE);
        my_mean = requireActivity().findViewById(R.id.my_mean);
        right_score = requireActivity().findViewById(R.id.rightscore);
        errscore = requireActivity().findViewById(R.id.errscore);
        answer1 = requireActivity().findViewById(R.id.answer1);
        answer2 = requireActivity().findViewById(R.id.answer2);
        answer3 = requireActivity().findViewById(R.id.answer3);
        answer4 = requireActivity().findViewById(R.id.answer4);
        final Button my_next = requireActivity().findViewById(R.id.my_next_button);
        tiaozhuantext = requireActivity().findViewById(R.id.tiaozhuantext);
        tiaozhuantext.setVisibility(View.INVISIBLE);
        tiaozhuanBar5 = requireActivity().findViewById(R.id.tiaozhuanBar5);
        tiaozhuanBar5.setVisibility(View.INVISIBLE);
        TextView selectDIF = requireActivity().findViewById(R.id.selectDIF);
        selectDIF.setText("难度选择:");
        CheckBox checkBox1 = requireActivity().findViewById(R.id.checkBox);
        CheckBox checkBox2 = requireActivity().findViewById(R.id.checkBox2);
        CheckBox checkBox3 = requireActivity().findViewById(R.id.checkBox3);
        checkBox1.setChecked(true);
        my_easy = requireActivity().findViewById(R.id.my_easy);
        my_common = requireActivity().findViewById(R.id.my_common);
        my_difficult = requireActivity().findViewById(R.id.my_difficult);
        right_score.setText("正确:" + num_right);
        errscore.setText("错误:" + num_err);
        final List<Word> lowdif = new ArrayList<>();
        final List<Word> middif = new ArrayList<>();
        final List<Word> highdif = new ArrayList<>();
        if (tempwordlist.size() != 0) {
            tempwordlist.clear();
        }
        try {
            for (Word w : AddFragment.listwords) {
                if (w.getWord().contains(" ") && w.getWord().length() < 15) {
                    middif.add(w);

                }
                if (w.getWord().contains(" ") && w.getWord().length() >= 15) {
                    highdif.add(w);
                }
                if (!w.getWord().contains(" ")) {
                    lowdif.add(w);
                    tempwordlist.add(w);
                }
            }
        } catch (Exception ignored) {

        }
        answer1.setEnabled(true);
        answer2.setEnabled(true);
        answer3.setEnabled(true);
        answer4.setEnabled(true);
        System.out.println("最开始" + tempwordlist.size());
        TextView textView = requireView().findViewById(R.id.test180_1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiaozhuanBar5.setVisibility(View.VISIBLE);
                tiaozhuantext.setVisibility(View.VISIBLE);
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_myWordTestFragment_to_allWordTestFragment);
                onDestroy();
            }
        });
        daanList = new ArrayList<>();
        Random random = new Random();
        try {
            Word word = tempwordlist.get(random.nextInt(tempwordlist.size()));
            my_mean.setText(word.getChineseMeaning());
            String[] errwords = ansewer(word.getWord());
            String rightword = word.getWord();
            righttext.setText(rightword);
            daanList.add(rightword);
            daanList.addAll(Arrays.asList(errwords));
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity(), "你的收藏夹为空，练习无法继续进行", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;

        }
        answer1.setText(daanList.remove(random.nextInt(daanList.size())));
        answer2.setText(daanList.remove(random.nextInt(daanList.size())));
        answer3.setText(daanList.remove(random.nextInt(daanList.size())));
        answer4.setText(daanList.remove(random.nextInt(daanList.size())));
        my_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempwordlist.size() == 0) {
                    Toast toast = Toast.makeText(getActivity(), "当前难度下你的收藏词汇为空，请选择合适的难度", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                answer1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                answer2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                answer3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                answer4.setBackgroundColor(Color.parseColor("#FFFFFF"));
                answer1.setEnabled(true);
                answer2.setEnabled(true);
                answer3.setEnabled(true);
                answer4.setEnabled(true);

                daanList = new ArrayList<>();
                Random random = new Random();
                Word word = tempwordlist.get(random.nextInt(tempwordlist.size()));
                my_mean.setText(word.getChineseMeaning());
                String[] errwords = ansewer(word.getWord());
                String rightword = word.getWord();
                righttext.setText(rightword);
                daanList.add(rightword);
                daanList.addAll(Arrays.asList(errwords));
                answer1.setText(daanList.remove(random.nextInt(daanList.size())));
                answer2.setText(daanList.remove(random.nextInt(daanList.size())));
                answer3.setText(daanList.remove(random.nextInt(daanList.size())));
                answer4.setText(daanList.remove(random.nextInt(daanList.size())));
            }
        });
        answer1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (answer1.getText().toString().equals(righttext.getText().toString())) {
                    answer1.setBackgroundColor(Color.parseColor("#43FF4A"));
                    right_score.setText("正确:" + (++num_right));
                   my_next.callOnClick();

                } else {
                    answer1.setBackgroundColor(Color.parseColor("#FF0000"));
                    errscore.setText("错误:" + (--num_err));
                }
                answer1.setEnabled(false);
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (answer2.getText().toString().equals(righttext.getText().toString())) {
                    answer2.setBackgroundColor(Color.parseColor("#43FF4A"));
                    right_score.setText("正确:" + (++num_right));
                } else {
                    answer2.setBackgroundColor(Color.parseColor("#FF0000"));
                    errscore.setText("错误:" + (--num_err));
                }
                answer2.setEnabled(false);
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (answer3.getText().toString().equals(righttext.getText().toString())) {
                    right_score.setText("正确:" + (++num_right));
                    answer3.setBackgroundColor(Color.parseColor("#43FF4A"));
                } else {
                    answer3.setBackgroundColor(Color.parseColor("#FF0000"));
                    errscore.setText("错误:" + (--num_err));
                }
                answer3.setEnabled(false);
            }

        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (answer4.getText().toString().equals(righttext.getText().toString())) {
                    answer4.setBackgroundColor(Color.parseColor("#43FF4A"));
                    right_score.setText("正确:" + (++num_right));
                } else {
                    answer4.setBackgroundColor(Color.parseColor("#FF0000"));
                    errscore.setText("错误:" + (--num_err));
                }
                answer4.setEnabled(false);
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    my_easy.setTextColor(Color.parseColor("#D81B60"));
                    try {
                        tempwordlist.addAll(lowdif);
                    } catch (Exception ignored) {

                    }

                } else {
                    my_easy.setTextColor(Color.parseColor("#818080"));
                    try {
                        for (Word word : lowdif) {
                            tempwordlist.remove(word);
                        }
                    } catch (Exception ignored) {

                    }

                }
                System.out.println((tempwordlist.size()));
            }

        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    my_common.setTextColor(Color.parseColor("#D81B60"));
                    tempwordlist.addAll(middif);
                } else {
                    my_common.setTextColor(Color.parseColor("#818080"));
                    try {
                        for (Word word : middif) {
                            tempwordlist.remove(word);
                        }
                    } catch (Exception ignored) {

                    }
                }

                System.out.println((tempwordlist.size()));
            }

        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    my_difficult.setTextColor(Color.parseColor("#D81B60"));
                    try {
                        tempwordlist.addAll(highdif);
                    } catch (Exception ignored) {

                    }
                } else {
                    my_difficult.setTextColor(Color.parseColor("#818080"));
                    try {
                        for (Word word : highdif) {
                            tempwordlist.remove(word);
                        }
                    } catch (Exception ignored) {

                    }
                }

                System.out.println((tempwordlist.size()));
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tiaozhuanBar5.setVisibility(View.INVISIBLE);
        tiaozhuantext.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("第1个被毁灭");
    }

    private static String[] ansewer(String word) {
        String[] strings = new String[3];
        //获取三个容易混淆的错误答案
        strings[0] = swapTwochar(word);
        strings[1] = addChar(word);
        strings[2] = changeChar(word);
        return strings;
    }




    /**
     * 改变答案：随机交换该单词两个非首字母的字母从而生成一个错误答案
     * @param word
     * @return
     */
    private static String swapTwochar(String word) {
        StringBuilder after = new StringBuilder();
        char temp;
        char[] c = word.toCharArray();
        Random random = new Random();
        char t1 = (char) (random.nextInt(26) + 97);
        if (word.length() < 7) {
            for (char u : c) {
                after.append(u);
            }
            after.append(t1);
        } else {
            while (true) {
                int i1 = random.nextInt(c.length - 1) + 1;
                int i2 = random.nextInt(c.length - 1) + 1;
                if (i1 != i2) {
                    if (c[i1] != c[i2]) {
                        temp = c[i1];
                        c[i1] = c[i2];
                        c[i2] = temp;
                        break;
                    }
                }
            }
            for (char u : c) {
                after.append(u);
            }
        }
        return after.toString();
    }

    /**
     * 在字符串内添加一个字符
     *
     * @param word 单词
     * @return 随机添加字符后的错误答案字符串
     */
    private static String addChar(String word) {
        StringBuilder after = new StringBuilder();
        char[] t = word.toCharArray();
        Random random = new Random();
        char t1 = (char) (random.nextInt(26) + 97);
        char[] c = new char[word.length() + 1];

        System.arraycopy(t, 0, c, 0, t.length);
        int index = random.nextInt(word.length());

        if (c.length - 1 - index >= 0)
            System.arraycopy(c, index, c, index + 1, c.length - 1 - index);
        c[index] = t1;

        for (char u : c) {
            after.append(u);
        }
        return after.toString();
    }

    //随机改变两个字符
    private static String changeChar(String word) {
        Random random = new Random();
        StringBuilder after = new StringBuilder();
        char t1 = (char) (random.nextInt(26) + 97);
        char t2 = (char) (random.nextInt(26) + 97);
        char[] c = word.toCharArray();
        if (word.length() <= 5) {
            while (true) {
                int index = random.nextInt(c.length);
                if (c[index] != t1) {
                    c[index] = t1;
                    break;
                }
            }
        } else {
            while (true) {
                int index = random.nextInt(c.length);
                int index2;

                do {
                    index2 = random.nextInt(c.length);
                } while (index == index2);
                if (c[index] != t1 || c[index2] != t2) {
                    c[index] = t1;
                    c[index2] = t2;
                    break;
                }
            }
        }
        for (char s : c) {
            after.append(s);
        }
        return after.toString();
    }


}
