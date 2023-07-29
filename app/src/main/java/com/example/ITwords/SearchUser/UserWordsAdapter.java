package com.example.ITwords.SearchUser;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ITwords.GetMysql.WordManage;
import com.example.ITwords.R;
import com.example.ITwords.Word;
import com.example.ITwords.WordRepository;

import java.util.ArrayList;

public class UserWordsAdapter extends RecyclerView.Adapter<UserWordsAdapter.UserWordViewHolder> {
    private ArrayList<UserWord> userWordArrayList ;
    private Application application;
    private int ispublic;

    public UserWordsAdapter(ArrayList<UserWord> userWordArrayList, Application application, int ispublic) {
        this.userWordArrayList = userWordArrayList;
        this.application = application;
        this.ispublic = ispublic;
    }


    @NonNull
    @Override
    public UserWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_normal, parent, false);
        return new UserWordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserWordViewHolder holder, int position) {
        if (ispublic == -1) {
            Toast.makeText(application, "部分信息获取失败", Toast.LENGTH_SHORT).show();
        } else if (ispublic == 0) {
            holder.textViewEnglish.setText("单词获取失败");
            holder.textViewChinese.setText("该用户未公开收藏列表");
            holder.to_my.setVisibility(View.GONE);
            return;
        }
        UserWord userWord = userWordArrayList.get(position);
        holder.textViewEnglish.setText(userWord.getWord());
        holder.textViewChinese.setText(userWord.getMean());
        holder.to_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = holder.textViewEnglish.getText().toString();
                String mean = holder.textViewChinese.getText().toString();
                new WordRepository(application).insertWords(new Word(word, mean));
                try {
                    WordManage.addword(word, mean);
                    Toast.makeText(application, "偷取成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(application, "偷取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(holder.textViewEnglish.getText().toString());
                Toast.makeText(application, holder.textViewEnglish.getText() + "已复制到剪切板", Toast.LENGTH_SHORT).show();
                return false;

            }
        });
    }

    @Override
    public int getItemCount() {
        return userWordArrayList.size();
    }

    static class UserWordViewHolder extends RecyclerView.ViewHolder {
        View to_my;
        TextView textViewEnglish, textViewChinese;

        UserWordViewHolder(@NonNull View itemView) {
            super(itemView);
            to_my = itemView.findViewById(R.id.to_my);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
        }
    }

}
