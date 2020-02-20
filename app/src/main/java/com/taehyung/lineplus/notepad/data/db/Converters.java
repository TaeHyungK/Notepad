package com.taehyung.lineplus.notepad.data.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
//    @TypeConverter
//    public static ArrayList<ImageWrapper> fromImageWrapper(String value) {
//        Type listType = new TypeToken<ArrayList<ImageWrapper>>() {}.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromArrayList(ArrayList<ImageWrapper> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }

    @TypeConverter
    public static ArrayList<byte[]> fromByteArray(String value) {
        Type listType = new TypeToken<ArrayList<byte[]>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<byte[]> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}