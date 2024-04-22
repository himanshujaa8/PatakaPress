package com.example.geokraze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private final List<Article> articleList;
    private final Context context;

    public NewsRecyclerAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.baseline_hide_image_24)
                .placeholder(R.drawable.baseline_hide_image_24)
                .into(holder.imageView);

        holder.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, article.getUrl());
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        holder.saveButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("SavedArticles", Context.MODE_PRIVATE);
            Set<String> savedArticles = sharedPreferences.getStringSet("articles", new HashSet<>());
            savedArticles.add(article.getUrl());
            sharedPreferences.edit().putStringSet("articles", savedArticles).apply();


            Toast.makeText(v.getContext(), "Article saved!", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), NewsfullActivity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView;
        ImageView imageView;
        Button shareButton, saveButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);
            shareButton = itemView.findViewById(R.id.share_button);
            saveButton = itemView.findViewById(R.id.save_button);
        }
    }
}
