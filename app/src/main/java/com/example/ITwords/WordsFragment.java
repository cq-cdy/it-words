package com.example.ITwords;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ITwords.GetMysql.GetConnection;
import com.example.ITwords.GetMysql.UserMessage;
import com.example.ITwords.GetMysql.WordManage;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends Fragment {
    private List<Word> lowdif = new ArrayList<>();
    private List<Word> middif = new ArrayList<>();
    private List<Word> highdif = new ArrayList<>();
    private Switch aSwitch;
    private static int show = 0;
    private TextView ispublic;
    private TextView zhuangtaitip;
    private WordViewModel wordViewModel;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter1, myAdapter2;
    private LiveData<List<Word>> filteredWords;//过滤之后的词汇
    private List<Word> allWords;
    private ProgressBar jiazaipublic;
    private int lowbai, midbai, highbai;
    static byte x = 2;
    static TextToSpeech mSpeech;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    private ConstraintLayout consabout;

    public WordsFragment() {
        setHasOptionsMenu(true);//显示菜单，因为默认是false
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearData:
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("确定要清空所有词汇吗？");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!AddFragment.isNetworkConnected(getActivity())) {
                            Toast.makeText(getContext(), "当前无网络连接，无法操作", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            wordViewModel.deleteAllWords();
                            UserMessage.delAllword(MainActivity.currentuser);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "当前没有单词", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.exit:
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(requireActivity());
                builder2.setTitle("是否退出登录");
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            wordViewModel.deleteAllWords();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MainActivity.mEditor.putInt("first", 0);
                        MainActivity.mEditor.commit();
                        getActivity().onBackPressed();

                    }
                });
                builder2.create();
                builder2.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String patten = newText.trim();
                filteredWords.removeObservers(getViewLifecycleOwner());//因为下面语句有观察，所以先移除，否则会出现闪屏现象
                filteredWords = wordViewModel.findWordsWithPatten(patten);
                filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = myAdapter1.getItemCount();
                        allWords = words;
                        if (temp != words.size()) {
                            myAdapter1.submitList(words);
                            myAdapter2.submitList(words);
                        }
                    }
                });
                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity.actionBar.show();
        jiazaipublic = requireActivity().findViewById(R.id.jiazaipublic);
        zhuangtaitip = requireActivity().findViewById(R.id.zhuangtaitip);
        ispublic = requireActivity().findViewById(R.id.ispublictext);
        aSwitch = requireActivity().findViewById(R.id.switch1);
        aSwitch.setChecked(true);
        zhuangtaitip.setVisibility(View.GONE);
        ispublic.setVisibility(View.GONE);
        aSwitch.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet res = null;
                try {
                    int i = 0;
                    conn = GetConnection.getConnection();
                    String sql = "select ispublic from user where user='" + MainActivity.mSharedPreferences2.getString("dqyh", "") + "'";
                    ps = conn.prepareStatement(sql);

                    res = ps.executeQuery();
                    while (res.next()) {
                        if (res.getInt(1) == 1) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            aSwitch.setChecked(true);
                                        }
                                    });
                                }
                            }.start();
                        } else {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            aSwitch.setChecked(false);
                                        }
                                    });
                                }
                            }.start();
                        }
                    }
                    ps.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //5.资源关闭
                finally {
                    GetConnection.closeResource(conn, ps, res);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                jiazaipublic.setVisibility(View.GONE);
                                ispublic.setVisibility(View.VISIBLE);
                                aSwitch.setVisibility(View.VISIBLE);
                                zhuangtaitip.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }.start();
            }
        }).start();


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showpublic(1);
                    ispublic.setText("公开");
                    ispublic.setTextColor(Color.GREEN);
                } else {
                    showpublic(0);
                    ispublic.setText("私密");
                    Toast toast = Toast.makeText(getActivity(), "词库已私密化", Toast.LENGTH_SHORT);
                    toast.show();
                    ispublic.setTextColor(Color.GRAY);
                }
            }
        });
        System.out.println("WordFragment创建*****");
        System.out.println("单词集合长度：" + AddFragment.listwords.size());
        if (!AddFragment.isITpipei) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("服务器连接异常，此时对单词操作无效，下次登录时将恢复到当前状态");
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create();
            builder.show();
        }
        mSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {//让mSpeech的实例化放在MyAdapter实例化之前，避免出现空指针
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = mSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    } else {
                        mSpeech.speak("", TextToSpeech.QUEUE_FLUSH,
                                null);
                    }
                }
            }
        });

        for (Word w : AddFragment.listwords) {
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
            lowbai = (100 * lowdif.size() / AddFragment.listwords.size());
            midbai = (100 * middif.size() / AddFragment.listwords.size());
            highbai = (100 * highdif.size() / AddFragment.listwords.size());
        } catch (Exception e) {
            Toast toast = Toast.makeText(getContext(), "空空如也，快去添加单词吧~", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        RadioGroup group = requireActivity().findViewById(R.id.Radio);
        group.check(R.id.rr2);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rr1) {
                    Toast.makeText(getContext(), "首字母顺序查询", Toast.LENGTH_SHORT).show();
                    x = 1;
                } else {
                    x = 2;
                    Toast.makeText(getContext(), "英汉模糊匹配", Toast.LENGTH_SHORT).show();
                }
            }
        });
        consabout = requireActivity().findViewById(R.id.consabout);
        consabout.setVisibility(View.GONE);
        context = getContext();

        TextView showword = requireActivity().findViewById(R.id.showwords);
        showword.setText("共收藏有：" + AddFragment.listwords.size());

        Button about = requireActivity().findViewById(R.id.about);

        ProgressBar lowgress = requireActivity().findViewById(R.id.lowgress);
        lowgress.setProgress(lowbai);

        ProgressBar midgress = requireActivity().findViewById(R.id.midgress);
        midgress.setProgress(midbai);

        ProgressBar highgress = requireActivity().findViewById(R.id.difgress);
        highgress.setProgress(highbai);

        TextView lowtext = requireActivity().findViewById(R.id.lowtext);
        TextView midtext = requireActivity().findViewById(R.id.midtext);
        TextView hightext = requireActivity().findViewById(R.id.hightext);
        lowtext.setText("普通单词:" + lowdif.size());
        midtext.setText("进阶短语:" + middif.size());
        hightext.setText("专用术语:" + highdif.size());
        wordViewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        recyclerView = requireActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        myAdapter1 = new MyAdapter(false, wordViewModel);
        myAdapter2 = new MyAdapter(true, wordViewModel);
        recyclerView.setAdapter(myAdapter2);
        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstPostion = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastPostion = linearLayoutManager.findLastVisibleItemPosition();
                    for (int i = firstPostion; i <= lastPostion; i++) {

                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder != null) {
                            holder.textViewNumber.setText(String.valueOf(i + 1));
                        }
                    }
                }

            }
        });

        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                consabout.setVisibility(View.VISIBLE);
                if (show == 0) {
                    recyclerView.smoothScrollBy(0, 700);
                }
                show = 1;
            }
        });
        View close_about = requireActivity().findViewById(R.id.close_about);
        close_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = 0;
                consabout.setVisibility(View.GONE);
                recyclerView.smoothScrollBy(0, -700);
            }
        });


        filteredWords = wordViewModel.getAllWordsLive();//初始化不过滤获取所有单词
        wordViewModel.getAllWordsLive().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter1.getItemCount();//当前屏幕上所存在的个数
                allWords = words;
                if (temp != words.size()) {
                    recyclerView.smoothScrollBy(0, -200);//设置平滑向下滚动200像素
                    myAdapter1.submitList(words);
                    myAdapter2.submitList(words);
                }
            }
        });
        //滑动删除的工具
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,//不支持拖动
                ItemTouchHelper.START | ItemTouchHelper.END) {//支持滑动
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {//滑动效果
                if (!AddFragment.isNetworkConnected(getContext())) {
                    Toast.makeText(getContext(), "当前无网络连接，暂时无法删除", Toast.LENGTH_SHORT).show();
                    myAdapter1.notifyDataSetChanged();
                    myAdapter2.notifyDataSetChanged();
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                final Word woedDelete = allWords.get(viewHolder.getAdapterPosition());
                builder.setTitle("确定删除该单词吗？");
                builder.setNegativeButton("我手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAdapter1.notifyDataSetChanged();//刷新当前页面单词，让刚刚被滑动删除的单词重新显示在页面
                        myAdapter2.notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.deleteWords(woedDelete);
                        WordManage.delWords(woedDelete.getWord());
                        Snackbar.make(requireActivity().findViewById(R.id.wordsFragmentView), "你删除了一个单词", Snackbar.LENGTH_LONG)
                                .setAction("点我撤回删除", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        wordViewModel.insertWords(woedDelete);//将刚刚删除的单词重新添加
                                        WordManage.addword(woedDelete.getWord(), woedDelete.getChineseMeaning());

                                    }
                                }).show();
                    }
                });
                myAdapter1.notifyDataSetChanged();//刷新当前页面单词
                myAdapter2.notifyDataSetChanged();
                builder.create();
                builder.show();
            }
        }).attachToRecyclerView(recyclerView);

        Button refresh = requireActivity().findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);

            }
        });

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mSpeech != null) {
            mSpeech.stop();
            mSpeech.shutdown();
        }
        System.out.println("WordFragment被毁灭********************************************");
        super.onDestroy();

    }

    @Override//点击回标返回时 隐藏键盘
    public void onResume() {
        System.out.println("WordFragment的Resume状态***********");
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        super.onResume();
     /*   assert imm != null;
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Toast.makeText(getContext(), "左上角返回主页", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
    }

    private static void showpublic(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    //1.获取数据库连接
                    conn = GetConnection.getConnection();
                    //2.预编译sql语句，返回PrepareStatement的实例
                    String sql = "UPDATE user SET ispublic=? where user= '"
                            + MainActivity.mSharedPreferences2.getString("dqyh", "") + "'";
                    ps = conn.prepareStatement(sql);
                    //3.填充占位符
                    ps.setObject(1, i);//第一个通配符
                    //4.执行
                    ps.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    GetConnection.closeResource(conn, ps, null);
                }
            }
        }).start();
    }


}
