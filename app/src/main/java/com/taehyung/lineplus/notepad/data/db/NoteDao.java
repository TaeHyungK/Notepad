package com.taehyung.lineplus.notepad.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteDao {
    // DAO 클래스로 Query 작성

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAll();

    // 동일한 primary Key가 있을 경우 덮어쓴다.
    @Insert(onConflict = REPLACE)
    long insertNote(Note note);

    @Query("SELECT * FROM note WHERE mId = (:id)")
    Note getNote(int id);

    @Query("DELETE FROM note")
    int deleteAll();

    @Query("DELETE FROM note WHERE mId = (:id)")
    int deleteNote(long id);

    @Update
    int update(Note note);
}
