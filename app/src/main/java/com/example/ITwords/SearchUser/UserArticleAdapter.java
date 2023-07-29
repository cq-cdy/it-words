package com.example.ITwords.SearchUser;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ITwords.Article_Message_Share;
import com.example.ITwords.MainActivity;
import com.example.ITwords.R;

import java.util.ArrayList;

public class UserArticleAdapter extends RecyclerView.Adapter<UserArticleAdapter.UserArticleViewHolder> {
    private ArrayList<User_Article> articleArrayList ;
    private Application application;
    private String searchname;
    private long click = 0;
    private int clicknum = 0;

    public UserArticleAdapter(ArrayList<User_Article> articleArrayList, Application application, String searchname) {
        this.articleArrayList = articleArrayList;
        this.application = application;
        this.searchname = searchname;

    }


    @NonNull
    @Override
    public UserArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_card, parent, false);

        return new UserArticleAdapter.UserArticleViewHolder(itemView);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final UserArticleViewHolder holder, int position) {
        final User_Article user_article = articleArrayList.get(position);
        if (searchname.equals(MainActivity.mSharedPreferences2.getString("dqyh", ""))) {
            holder.deleteMessage.setVisibility(View.VISIBLE);
        }
        holder.textViewEnglish.setText(user_article.getUserarticle());
        holder.user_agree.setText("获赞:" + user_article.getUseragree());
        holder.userdate.setText(user_article.getDate());
        holder.textViewEnglish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(holder.textViewEnglish.getText().toString() +
                        "\n\n      文章来源:MyITwords App\n" + "     " + "     作者:" + user_article.getByuser().toString() +
                        "\n      发布日期:" + user_article.getDate());
                Toast.makeText(application, user_article.getByuser().toString() + "的文章已复制到剪切板", Toast.LENGTH_SHORT).show();
                return false;

            }
        });

        holder.deleteMessage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (clicknum == 4) {
                    //删除成功之后的初始话化
                    click = 0;
                    clicknum = 0;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Article_Message_Share.deleteMessage(holder.textViewEnglish.getText().toString(), holder.userdate.getText().toString());

                        }
                    }).start();
                    Toast toast = Toast.makeText(application, "删除成功", Toast.LENGTH_SHORT);
                    toast.show();
                    holder.textViewEnglish.setText("文章已删除");
                    holder.user_agree.setText("null");
                    holder.deleteMessage.setVisibility(View.GONE);
                    holder.userdate.setText("被吸入了黑洞");
                    return;
                }
                if (click == 0) {
                    click = System.currentTimeMillis();
                    Toast toast = Toast.makeText(application, "连续点击删除该文章", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    if (System.currentTimeMillis() - click < 700) {
                        clicknum++;
                        click = System.currentTimeMillis();
                    } else {
                        click = 0;
                        clicknum = 0;
                    }
                }
            }
        });


        holder.textViewEnglish.setMovementMethod(ScrollingMovementMethod.getInstance());

        holder.textViewEnglish.setOnTouchListener(new View.OnTouchListener() {//使得文本竖直滚动不冲突
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    static class UserArticleViewHolder extends RecyclerView.ViewHolder {
        Button deleteMessage;
        TextView textViewEnglish, user_agree, userdate;

        UserArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            user_agree = itemView.findViewById(R.id.user_agree);
            userdate = itemView.findViewById(R.id.userdate);
            deleteMessage = itemView.findViewById(R.id.deleteMessage);
            deleteMessage.setVisibility(View.INVISIBLE);
        }
    }
}
