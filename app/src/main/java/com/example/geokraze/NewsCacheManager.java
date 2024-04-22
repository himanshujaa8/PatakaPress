package com.example.geokraze;

import android.content.Context;

import com.kwabenaberko.newsapilib.models.Article;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NewsCacheManager {
    private static final String CACHE_FILE_NAME = "news_cache";

    public static void saveNewsArticles(Context context, List<Article> articles) {
        try {
            FileOutputStream fos = context.openFileOutput(CACHE_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(articles);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Article> loadNewsArticles(Context context) {
        List<Article> articles = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(CACHE_FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            articles = (List<Article>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return articles;
    }
}