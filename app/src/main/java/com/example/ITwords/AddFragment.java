package com.example.ITwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.ITwords.GetMysql.GetConnection;
import com.example.ITwords.GetMysql.WordManage;
import com.example.ITwords.Internet.TransApi;
import com.example.ITwords.Internet.TranslateResult;
import com.google.gson.Gson;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    private int index = 1;
    private final static String FLUME_SOCKET_IP="192.168.230.100";
    private final static int FLUME_SOCKET_PORT=4444;
    public static ArrayList<String> tuijianlist = new ArrayList<>();
    private static String flumeword = "itwords";
    private final StringBuffer sb = new StringBuffer();
    private final static String TAG = "OCR";
    private static boolean isinternet = false;
    private static WordViewModel wordViewModel2;
    private TextView tuijiantxt;
    private TextView tuijianmean;
    static List<Word> listwords;

    static void setList(List<Word> list) {
        listwords = list;
    }

    static boolean isITpipei = true;
    private Button agreebutton, disagreebutton, refresh_message;
    private TextToSpeech mSpeech2;
    private RadioGroup radioGroup;
    private TextView ing;
    private TextView tiptext;
    private TextView nousetip;
    private TextView message;
    private TextView by_user;
    private TextView biaoti;
    private TextView showagree;
    private TextView date;
    private Button trsbutton;
    private View progressBar;
    private View messagegress;
    private View progressBar3;
    private EditText editTextEnglish, editTextChinese;
    private WordViewModel wordViewModel;
    static boolean sk=true;
    private static com.example.ITwords.Message tempmessage;
    //Handler handler = new Handler();
    @SuppressLint("StaticFieldLeak")
    private static TextView T;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private String handlerText(Intent intent) {
        return intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    static WordViewModel getWordViewModel() {
        return wordViewModel2;
    }

    static TextView getT() {
        return T;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mSpeech2 != null) {
            mSpeech2.stop();
            mSpeech2.shutdown();
        }
        System.out.println("AddFragment被毁灭");
        if (listwords != null) {
            listwords.clear();
        }
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("视图创建*****************************");
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @SuppressLint({"SetTextI18n", "CutPasteId", "ClickableViewAccessibility"})
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();
        MainActivity.actionBar.hide();
        System.out.println("AddFragment创建***********************************");

        ImageView imageView = activity.findViewById(R.id.imageView8);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.img_xz);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        imageView.startAnimation(animation);
        @SuppressLint("CutPasteId") TextView usertext;
        usertext = activity.findViewById(R.id.textView);
        if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
            usertext.setText("未登录");
        } else {
            usertext.setText(MainActivity.mSharedPreferences2.getString("dqyh", "") + "");
        }
        tuijiantxt = activity.findViewById(R.id.tuijiantxt);


        tuijianmean = activity.findViewById(R.id.tuijianmean);
        View right = activity.findViewById(R.id.right);
        View left = activity.findViewById(R.id.left);
        Button getword = activity.findViewById(R.id.getbutton);
        TextView takephototext = activity.findViewById(R.id.takephototext);
        TextView myshoucangtext = activity.findViewById(R.id.myshoucangtext);
        TextView mytesttext = activity.findViewById(R.id.mytesttext);
        TextView searchusertext = activity.findViewById(R.id.searchusertext);
        TextView contactustext = activity.findViewById(R.id.contactustext);
        TextView yijiantext = activity.findViewById(R.id.yijiantext);
        View jubao = activity.findViewById(R.id.jubao);
        radioGroup = activity.findViewById(R.id.radiogroup);
        Button myword = activity.findViewById(R.id.myword_180);
        wordViewModel = ViewModelProviders.of(activity).get(WordViewModel.class);//这里实例化了WordViewModel通过构造方法
        wordViewModel2 = wordViewModel;
        Button buttonSubmit = activity.findViewById(R.id.buttonSubmit);
        trsbutton = activity.findViewById(R.id.trsbutton);
        editTextEnglish = activity.findViewById(R.id.editTextEnglish);
        editTextChinese = activity.findViewById(R.id.editTextChinese);
        View englishdel = activity.findViewById(R.id.englishdel);
        View chinesedel = activity.findViewById(R.id.chinesedel);
        View xuanzhuan = activity.findViewById(R.id.imageView8);
        final TextView biaoti = activity.findViewById(R.id.textView3);
        progressBar = activity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar3 = activity.findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.INVISIBLE);
        ing = activity.findViewById(R.id.ing);
        ing.setVisibility(View.INVISIBLE);
        tiptext = activity.findViewById(R.id.tiptext);
        tiptext.setVisibility(View.INVISIBLE);
        tiptext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        nousetip = activity.findViewById(R.id.nousetip);
        nousetip.setVisibility(View.INVISIBLE);
        final TextView ITnews = activity.findViewById(R.id.ITnews);
        ITnews.setText("   服务器连接异常~~~    服务器连接异常~~~   服务器连接异常~~~   服务器连接异常~~~");
        ITnews.setSelected(true);
        View readenglish = activity.findViewById(R.id.readenglish);
        message = activity.findViewById(R.id.message);

        message.setMovementMethod(ScrollingMovementMethod.getInstance());

        final Thread t = new Thread() {
            @Override
            public void run() {
                WordManage.word_tuijian();
            }
        };

        t.start();


        //这个线程是为了避免连接失败白屏
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    t.join();//等待推荐更新
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if(tuijianlist.size()!=0){

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] split = tuijianlist.get(0).split("#");
                                tuijiantxt.setText(split[0]);
                                tuijianmean.setText(split[1]);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        if (tuijianlist.size() != 0) {
            String[] split = tuijianlist.get(index).split("#");
            tuijiantxt.setText(split[0]);
            tuijianmean.setText(split[1]);
        }
        System.err.println("\n\n\n\n"+"卡住zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        //获取标题
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetConnection.setTitle(biaoti);
                GetConnection.setGongGao(ITnews);
            }
        }).start();
        T = activity.findViewById(R.id.textView);
        // editTextEnglish.requestFocus();//获取光标

        message.setOnTouchListener(new View.OnTouchListener() {//使得文本竖直滚动不冲突
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        editTextEnglish.setOnTouchListener(new View.OnTouchListener() {//使得文本竖直滚动不冲突
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        editTextChinese.setOnTouchListener(new View.OnTouchListener() {//使得文本竖直滚动不冲突
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        OCR.getInstance(getContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象

            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                Log.e(TAG, error.toString());
            }
        }, getContext());


        //文本分享到此APP
        Intent intent = requireActivity().getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();
        //设置接收类型为文本
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                editTextEnglish.setText(handlerText(intent));
            }
        }
        jubao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("是否要举报该文章以及用户");
                builder.setNegativeButton("我点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确认举报", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                YonghuFankui.jubao(message.getText().toString(), by_user.getText().toString());
                            }
                        }).start();
                        Toast toast = Toast.makeText(getActivity(), "举报成功，感谢您的反馈", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }).show();
            }
        });
        //意见反馈
        yijiantext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Toast toast;
                    toast = Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(requireActivity(), UserfeedbackActivity.class);
                startActivity(intent);
            }
        });
        //联系我们
        contactustext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(requireActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        mSpeech2 = new TextToSpeech(getContext(), new OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = mSpeech2.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    } else {
                        mSpeech2.speak("", TextToSpeech.QUEUE_FLUSH,
                                null);
                    }
                }
            }
        });
        mSpeech2.setPitch(0.8f);
        // 设置语速
        mSpeech2.setSpeechRate(0.8f);
        readenglish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editTextEnglish.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "英文栏为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                mSpeech2.speak(editTextEnglish.getText().toString(),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        xuanzhuan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.ecjtu.jx.cn/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                requireContext().startActivity(intent);

            }
        });
        TextView tougao = activity.findViewById(R.id.tougao);
        tougao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Toast toast = Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(requireContext(), TouGaoActivity.class);
                startActivity(intent);
            }
        });
        //单词检测
        mytesttext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Toast toast = Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(requireContext(), WordTestActivity.class);
                startActivity(intent);

            }
        });

        //自动弹出键盘
        // InputMethodManager imm=(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(editTextEnglish,0);//光标定位
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String english = editTextEnglish.getText().toString().trim();
                String chinese = editTextChinese.getText().toString().trim();
                // buttonSubmit.setEnabled(!english.isEmpty() && !chinese.isEmpty());
                if (!english.isEmpty() || !chinese.isEmpty()) {
                    trsbutton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        //输入框添加监听
        editTextEnglish.addTextChangedListener(textWatcher);
        editTextChinese.addTextChangedListener(textWatcher);
        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Toast toast = Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (!AddFragment.isNetworkConnected(getActivity())) {
                    Toast.makeText(getContext(), "当前无网络连接，请先检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isITpipei) {
                    Toast.makeText(getContext(), "服务器连接异常，无法添加到收藏", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextEnglish.getText().toString().trim().isEmpty()
                        || editTextChinese.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "英文或注释为空，无法添加", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String english = editTextEnglish.getText().toString().trim();
                final String chinese = editTextChinese.getText().toString().trim();
                final Word word = new Word(english, chinese);
                for (int i = 0; i < listwords.size(); i++) {
                    if (english.toLowerCase().equals(listwords.get(i).getWord().toLowerCase())) {
                        Toast.makeText(getContext(), "你可以去笔记本里搜索这个单词，请勿重复添加", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet res = null;
                        String flword = editTextEnglish.getText().toString();
                        String flmean = editTextChinese.getText().toString();
                        try {
                            conn = GetConnection.getConnection();
                            String sql = "select word,mean from word ;";
                            ps = conn.prepareStatement(sql);
                            //执行并返回结果集
                            res = ps.executeQuery();
                            //处理结果集
                            while (res.next()) {
                                String inerword = res.getString(1);
                                if (inerword.toLowerCase().equals(english.toLowerCase())) {
                                    return;
                                }
                            }
                            try {
                                flumeword = flword + "#" + flmean + "#" + MainActivity.mSharedPreferences2.getString("dqyh", "");
                                Socket sock = new Socket(FLUME_SOCKET_IP, FLUME_SOCKET_PORT);
                                // 创建一个写线程
                                new TelnetWriter(sock.getOutputStream()).start();
                                // 创建一个读线程

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            WordManage.wordComplement(english, chinese);
                        } catch (Exception ignored) {
                        } finally {
                            GetConnection.closeResource(conn, ps, res);
                        }
                    }
                }).start();
                wordViewModel.insertWords(word);

                WordManage.addword(word.getWord(), word.getChineseMeaning());
                NavController navController = Navigation.findNavController(v);
                navController.navigateUp();
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Toast.makeText(getContext(), "单词添加成功", Toast.LENGTH_SHORT).show();
            }
        });

        chinesedel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextChinese.setText("");
            }
        });
        englishdel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEnglish.setText("");
            }
        });
        trsbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected(getActivity())) {
                    Toast.makeText(getContext(), "当前无网络连接,无法提供在线翻译服务", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                ing.setVisibility(View.VISIBLE);
                final String query = editTextEnglish.getText().toString();
                String english = editTextEnglish.getText().toString().trim();
                String chinese = editTextChinese.getText().toString().trim();
                if (english.isEmpty() && chinese.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    ing.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "请输入要翻译的中文或英文", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String english = editTextEnglish.getText().toString().trim();
                        String chinese = editTextChinese.getText().toString().trim();
                        if (english.isEmpty() && !chinese.isEmpty()) {
                            final String query2 = editTextChinese.getText().toString();
                            String resultJson = new TransApi().getTransResult(query2, "auto", "en");
                            //拿到结果，对结果进行解析。
                            Gson gson = new Gson();
                            TranslateResult translateResult = gson.fromJson(resultJson, TranslateResult.class);
                            final List<TranslateResult.TransResultBean> trans_result = translateResult.getTrans_result();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder dst = new StringBuilder();
                                    for (TranslateResult.TransResultBean s : trans_result
                                    ) {
                                        dst.append(s.getDst());
                                    }
                                    editTextEnglish.setText(dst.toString());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    ing.setVisibility(View.INVISIBLE);
                                    trsbutton.setEnabled(true);
                                }
                            });
                        } else {
                            String resultJson = new TransApi().getTransResult(query, "auto", "zh");
                            //拿到结果，对结果进行解析。
                            Gson gson = new Gson();
                            TranslateResult translateResult = gson.fromJson(resultJson, TranslateResult.class);
                            final List<TranslateResult.TransResultBean> trans_result = translateResult.getTrans_result();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    StringBuilder dst = new StringBuilder();
                                    System.out.println(trans_result);
                                    for (TranslateResult.TransResultBean s : trans_result
                                    ) {
                                        dst.append(s.getDst());
                                    }

                                    editTextChinese.setText(dst.toString());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    ing.setVisibility(View.INVISIBLE);
                                    trsbutton.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        //拍照翻译
        takephototext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CameraActivity.class);

                // 设置临时存储
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(requireContext()).getAbsolutePath());

                // 调用除银行卡，身份证等识别的activity
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);

                startActivityForResult(intent, 111);
            }
        });
        myword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Intent intent = new Intent();
                    intent.setClass(requireActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(requireContext(), MySelfActivity.class);
                startActivity(intent);
                /*NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_addFragment_to_wordsFragment);*/

            }
        });
        myshoucangtext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Intent intent = new Intent();
                    intent.setClass(requireActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_addFragment_to_wordsFragment);
            }
        });

        getword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuijianlist.size() != 0) {

                    editTextEnglish.setText(tuijiantxt.getText().toString());
                    editTextChinese.setText(tuijianmean.getText().toString());
                    editTextEnglish.clearFocus();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "暂无推荐", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


            }
        });
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("dangqianchangdu"+tuijianlist.size()+"\n"+index);
                if (tuijianlist.size() != 0) {
                    String[] split = tuijianlist.get(++index % tuijianlist.size()).split("#");
                    tuijiantxt.setText(split[0]);
                    tuijianmean.setText(split[1]);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "暂无推荐", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


            }
        });
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuijianlist.size() >= 1) {

                    String[] split =
                            tuijianlist.
                                    get((index+=(tuijianlist.size()-1))% tuijianlist.size()).split("#");
                    tuijiantxt.setText(split[0]);
                    tuijianmean.setText(split[1]);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "暂无推荐", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        searchusertext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Toast toast = Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(requireContext(), SearchUserActivity.class);
                startActivity(intent);
            }
        });
        radioGroup.check(R.id.radioButton1);//默认先选中1
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    isinternet = false;
                    tiptext.setText("");
                    nousetip.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "切换成功：普通翻译模式", Toast.LENGTH_SHORT).show();
                } else {
                    isinternet = true;
                    Toast.makeText(getContext(), "切换成功：IT单词匹配模式", Toast.LENGTH_SHORT).show();
                    tiptext.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editTextEnglish.setText(tiptext.getText().toString().trim());
                            editTextEnglish.setSelection(editTextEnglish.getText().toString().length());
                        }
                    });
                    editTextEnglish.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            if (isinternet) {
                                if (!isNetworkConnected(getContext())) {
                                    Toast.makeText(getContext(), "无网络连接暂时无法搜索", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                nousetip.setVisibility(View.INVISIBLE);
                                tiptext.setVisibility(View.INVISIBLE);
                                progressBar3.setVisibility(View.VISIBLE);
                                if (editTextEnglish.getText().toString().isEmpty()) {
                                    nousetip.setVisibility(View.INVISIBLE);
                                    tiptext.setVisibility(View.INVISIBLE);
                                    progressBar3.setVisibility(View.INVISIBLE);
                                    return;
                                }

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Connection conn = null;
                                        PreparedStatement ps = null;
                                        ResultSet res = null;
                                        try {
                                            conn = GetConnection.getConnection();
                                            isITpipei = conn != null;
                                            String sql = "select * from word;";
                                            assert conn != null;
                                            ps = conn.prepareStatement(sql);
                                            res = ps.executeQuery();
                                            while (res.next()) {
                                                final String word = res.getString(1).toLowerCase();
                                                final String mean = res.getString(2);
                                                if (editTextEnglish.getText().toString().isEmpty()) {
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    tiptext.setText(word);
                                                                    nousetip.setVisibility(View.INVISIBLE);
                                                                    tiptext.setVisibility(View.INVISIBLE);
                                                                    editTextChinese.setText("");
                                                                }
                                                            });
                                                        }
                                                    }.start();
                                                    return;
                                                }
                                                if (word.contains(editTextEnglish.getText().toString().toLowerCase())) {
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    tiptext.setText(word);
                                                                    nousetip.setVisibility(View.VISIBLE);
                                                                    tiptext.setVisibility(View.VISIBLE);
                                                                    editTextChinese.setText(mean);
                                                                }
                                                            });
                                                        }
                                                    }.start();
                                                    break;
                                                } else {
                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            super.run();
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    editTextChinese.setText("");
                                                                }
                                                            });
                                                        }
                                                    }.start();
                                                }
                                            }
                                            ps.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            radioGroup.check(R.id.radioButton1);
                                                            Toast toast = Toast.makeText(getActivity(), "服务器连接异常，自动切换到普通模式", Toast.LENGTH_SHORT);
                                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                                            toast.show();
                                                        }
                                                    });
                                                }
                                            }.start();
                                        } finally {
                                            GetConnection.closeResource(conn, ps, res);
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressBar3.setVisibility(View.GONE);
                                                        }
                                                    });
                                                }
                                            }.start();
                                        }
                                    }
                                }).start();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });
        messagegress = activity.findViewById(R.id.messagegress);
        by_user = activity.findViewById(R.id.by_user);
        refresh_message = activity.findViewById(R.id.refresh_message);
        showagree = activity.findViewById(R.id.showagree);
        date = activity.findViewById(R.id.date);
        agreebutton = activity.findViewById(R.id.agreebutton);
        disagreebutton = activity.findViewById(R.id.disagreebutton);
        agreebutton.setText("👍");
        disagreebutton.setText("👎");
        agreebutton.setEnabled(false);
        disagreebutton.setEnabled(false);
        by_user.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                char[] c = by_user.getText().toString().toCharArray();
                StringBuilder s = new StringBuilder();
                for (int i = 5; i < c.length; i++) {
                    s.append(c[i]);
                }
                assert cm != null;
                cm.setText(s.toString());
                Toast.makeText(getContext(), "用户名已复制", Toast.LENGTH_SHORT).show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Article_Message_Share.getOneMessage();
                    tempmessage = Article_Message_Share.getMessagearray.get(0);//记录此时的临时信息
                    new Thread() {//更新视图
                        @Override
                        public void run() {
                            super.run();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    agreebutton.setVisibility(View.VISIBLE);
                                    agreebutton.setEnabled(true);
                                    disagreebutton.setVisibility(View.VISIBLE);
                                    disagreebutton.setEnabled(true);
                                    by_user.setText("来自用户:" + Article_Message_Share.getMessagearray.get(0).getByuser());
                                    message.setText(Article_Message_Share.getMessagearray.get(0).getMessage());
                                    showagree.setText("当前获赞:" + Article_Message_Share.getMessagearray.get(0).getAgree());
                                    date.setText(Article_Message_Share.getMessagearray.get(0).getDate());
                                    messagegress.setVisibility(View.INVISIBLE);
                                    refresh_message.setEnabled(true);
                                }
                            });
                        }
                    }.start();

                } catch (Exception ignored) {
                }
            }
        }).start();
        refresh_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                messagegress.setVisibility(View.VISIBLE);
                refresh_message.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Article_Message_Share.getOneMessage();
                            tempmessage = Article_Message_Share.getMessagearray.get(0);//记录此时的临时信息

                            new Thread() {//更新视图
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            message.scrollTo(0, 0);//回到顶部
                                            agreebutton.setText("👍");
                                            disagreebutton.setText("👎");
                                            agreebutton.setVisibility(View.VISIBLE);
                                            agreebutton.setEnabled(true);
                                            disagreebutton.setVisibility(View.VISIBLE);
                                            disagreebutton.setEnabled(true);
                                            by_user.setText("来自用户:" + Article_Message_Share.getMessagearray.get(0).getByuser());
                                            message.setText(Article_Message_Share.getMessagearray.get(0).getMessage());
                                            showagree.setText("当前获赞:" + Article_Message_Share.getMessagearray.get(0).getAgree());
                                            date.setText(Article_Message_Share.getMessagearray.get(0).getDate());
                                            messagegress.setVisibility(View.INVISIBLE);
                                            refresh_message.setEnabled(true);

                                        }
                                    });
                                }
                            }.start();

                        } catch (Exception ignored) {
                        }
                    }
                }).start();

            }
        });
        agreebutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disagreebutton.setVisibility(View.INVISIBLE);
                agreebutton.setText("你赞了TA");
                agreebutton.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Article_Message_Share.agree(tempmessage);
                    }
                }).start();
                showagree.setText("当前获赞:" + (tempmessage.getAgree() + 1));
            }
        });
        disagreebutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                agreebutton.setVisibility(View.INVISIBLE);
                disagreebutton.setText("你踩了TA");
                disagreebutton.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Article_Message_Share.disagree(tempmessage);
                    }
                }).start();
                showagree.setText("当前获赞:" + (tempmessage.getAgree() - 1));
            }
        });
        message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (message.getText().toString().isEmpty()) {
                    return true;
                }
                if (MainActivity.mSharedPreferences.getInt("first", 0) == 0) {
                    Toast toast = Toast.makeText(getActivity(), "登录后可复制", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                ClipboardManager cm = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                assert cm != null;
                cm.setText(message.getText().toString() + "\n\n      文章来源:MyITwords App\n" + "     " + by_user.getText().toString()
                        + "\n      发布日期:" + date.getText().toString());
                Toast.makeText(getContext(), by_user.getText().toString() + "的文章已复制到剪切板", Toast.LENGTH_SHORT).show();
                return false;

            }
        });

    }

    public void onResume() {//在此fragment中对返回键进行监听
        super.onResume();
        System.out.println("AddfrmgentOnresume状态*******************************");
        if(sk) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("作者没钱续费服务器了，仅支持在线翻译等功能（0.0）");
            builder.setNegativeButton("此次不再显示", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sk=false;
                }
            });
//            builder.setNegativeButton("下次一定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
            builder.setPositiveButton("⭐打赏作者⭐", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), Shoukuanma.class);
                    startActivity(intent);

                }
            }).show();
        }
    }

    static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert mConnectivityManager != null;
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                //这种方法也可以
                //return mNetworkInfo .getState()== NetworkInfo.State.CONNECTED
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("ADD-onActivityResult");
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            // 获取调用参数
            assert data != null;
            final String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
            System.out.println(contentType+"3-2");
            // 通过临时文件获取拍摄的图片
            String filePath = FileUtil.getSaveFile(requireContext()).getAbsolutePath();
            OCRManager.recognizeAccurateBasic(getContext(), filePath, new OCRManager.OCRCallBack<GeneralResult>() {
                @Override
                public void succeed(GeneralResult data) {
                    // 调用成功，返回GeneralResult对象
                    //  String content = OCRManager.getResult(data);
                    for (WordSimple wordSimple : data.getWordList()) {                    // wordSimple不包含位置信息
                        sb.append(wordSimple.getWords());
                    }
                    if (sb.toString().equals("")) {
                        Toast toast = Toast.makeText(getActivity(), "没有检测到任何文字", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    editTextEnglish.setText(sb.toString().trim());
                    sb.delete(0, sb.length());
                    //Log.e(TAG,content + "");
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("是否要举报该文章以及用户");
                    builder.setNegativeButton("我点错了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                }
                @Override
                public void failed(OCRError error) {
                    // 调用失败，返回OCRError对象
                    Log.e(TAG, "错误信息：" + error.getMessage());
                }
            });
        }
    }

    private static class TelnetWriter extends Thread {
        private PrintStream out;
        TelnetWriter(OutputStream outputStream) {
            this.out = new PrintStream(outputStream);
        }
        public void run() {
            try {
                out.println(flumeword);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

}


