package com.taehyung.toy.notepad.data.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;
    private LiveData<List<Note>> mAllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNote() {
        return mAllNotes;
    }

    public Note getNote(long id) {
        return mRepository.getNote(id);
    }

    public void insertNote(Note note) {
        mRepository.insertNote(note);
    }

    public void deleteNote(long id) {
        mRepository.deleteNote(id);
    }

    public void updateNote(Note note) {
        mRepository.updateNote(note);
    }
}
