package com.taehyung.lineplus.notepad.data.db;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private static final String TAG = NoteRepository.class.getSimpleName();

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        AppDatabase appDb = AppDatabase.getInstance(application);
        // Database 에 있는 Dao를 가져온다.
        mNoteDao = appDb.noteDao();
        // Dao의 쿼리를 이용하여 저장되어 있는 모든 데이터를 가져온다.
        mAllNotes = mNoteDao.getAll();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    /**
     * 노트 조회 처리
     * get Note
     *
     * @param id long - target mId
     */
    public Note getNote(long id) {
        if (id == -1) {
            return null;
        }

        Note curNote = null;
        if (mAllNotes != null && mAllNotes.getValue() != null
                && mAllNotes.getValue().size() > 0) {
            List<Note> notes = mAllNotes.getValue();
            for (Note note : notes) {
                if (id == note.mId) {
                    curNote = note;
                    break;
                }
            }
        }

        return curNote;
    }

    /**
     * 메모 추가 처리
     * add Note
     *
     * @param input Note
     */
    public void insertNote(Note input) {
        if (input == null) {
            return;
        }

        new AsyncTask<Note, Void, Long>() {
            @Override
            protected Long doInBackground(Note... notes) {
                if (mNoteDao == null) {
                    return -1L;
                }
                Log.d(TAG, "doInBackground() notes[0]: " + notes[0].getId() + " | " + notes[0].getTitle() + " | " + notes[0].getDesc());
                return mNoteDao.insertNote(notes[0]);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d(TAG, "insert: " + aLong);
            }
        }.execute(input);
    }

    /**
     * 메모 삭제 처리
     * delete Note
     *
     * @param id long - target mId
     */
    public void deleteNote(long id) {
        if (id == -1) {
            return;
        }

        new AsyncTask<Long, Void, Integer>() {
            @Override
            protected Integer doInBackground(Long... longs) {
                if (mNoteDao == null) {
                    return -1;
                }
                return mNoteDao.deleteNote(longs[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "deleteNote: " + integer);
            }
        }.execute(id);
    }

    public void updateNote(Note note) {
        new AsyncTask<Note, Void, Integer>() {
            @Override
            protected Integer doInBackground(Note... notes) {
                if (mNoteDao == null) {
                    return -1;
                }
                return mNoteDao.update(notes[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d(TAG, "notes : " + integer);
            }
        }.execute(note);
    }
}
