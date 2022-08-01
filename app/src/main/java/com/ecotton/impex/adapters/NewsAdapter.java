package com.ecotton.impex.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.NewsFullScreenActivity;
import com.ecotton.impex.models.NewsList;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    Context mcontext;
    public List<NewsList> newsdatalist;

    public NewsAdapter(Context mcontext, List<NewsList> newsdatalist) {
        this.mcontext = mcontext;
        this.newsdatalist = newsdatalist;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.custome_news_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewsList newsModel = newsdatalist.get(position);
        holder.tv_shortnews.setText(newsModel.getName());
        holder.tv_newstime.setText(newsModel.getDate());

        Glide.with(mcontext).load(newsModel.getImage()).into(holder.iv_news);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, NewsFullScreenActivity.class);
                intent.putExtra("id", newsModel.getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsdatalist.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_shortnews, tv_newstime;
        ImageView iv_news;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_shortnews = itemView.findViewById(R.id.tv_shortnews);
            tv_newstime = itemView.findViewById(R.id.tv_newstime);
            iv_news = itemView.findViewById(R.id.iv_news);
        }
    }
}
