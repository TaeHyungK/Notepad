package com.taehyung.toy.notepad.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(version = 1, entities = {Note.class})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    // DB와 연결되는 DAO
    // DAO는 abstract로 getter()를 제공
    public abstract NoteDao noteDao();

    // Database Singleton 생성
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // DB 생성
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class, "note_db").build();

                }
            }
        }
        return INSTANCE;
    }
}
