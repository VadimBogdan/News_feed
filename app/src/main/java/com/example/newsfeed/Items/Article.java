package com.example.newsfeed.Items;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Article implements Parcelable {
    public static final String TAG_ARTICLE = "article";

    public static final String TAG_SOURCE = "source";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_NAME = "name";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_CONTENT = "content";
    public static final String TAG_URL = "url";
    public static final String TAG_URL_TO_IMAGE = "urlToImage";
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<Article> CREATOR = new Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    private HashMap<String, String> data = new HashMap<>();

    public Article(HashMap<String, String> data) {
        this.data = data;
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    @SuppressWarnings("unchecked")
    private Article(Parcel in) {
        data = in.readHashMap(HashMap.class.getClassLoader());
    }

    public String get(String tag) {
        return data.get(tag);
    }

    /* everything below here is for implementing Parcelable */
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        Bundle b = new Bundle();
        out.writeMap(data);
    }
}
