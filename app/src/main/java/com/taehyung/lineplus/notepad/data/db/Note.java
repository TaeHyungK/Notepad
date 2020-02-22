package com.taehyung.lineplus.notepad.data.db;


import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Note implements Serializable {
    // Auto Increase 으로 자동 생성
    // FIXME 앱을 오래 사용하며 long 값을 벗어나도록 id가 생성되었을 경우를 위해 새로운 Note 생성 시 빈 id를 가지도록 처리 필요.
    @PrimaryKey(autoGenerate = true)
    public long mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "desc")
    private String mDesc;

    @ColumnInfo(name = "images")
    private ArrayList<String> mImages;

    public Note(String title, String desc) {
        mTitle = title;
        mDesc = desc;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public void setImages(ArrayList<String> mImages) {
        this.mImages = mImages;
    }
}
