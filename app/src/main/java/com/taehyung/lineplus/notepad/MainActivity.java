package com.taehyung.lineplus.notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taehyung.lineplus.notepad.data.DataConst;
import com.taehyung.lineplus.notepad.data.db.NoteViewModel;
import com.taehyung.lineplus.notepad.data.db.Note;
import com.taehyung.lineplus.notepad.ui.popup.DetailFragment;
import com.taehyung.lineplus.notepad.ui.recyclerview.CustomDividerItemDecoration;
import com.taehyung.lineplus.notepad.ui.recyclerview.main.NoteAdapter;
import com.taehyung.lineplus.notepad.ui.recyclerview.main.NoteViewHolder;
import com.taehyung.lineplus.notepad.utility.Utils;

import java.util.ArrayList;

import static com.taehyung.lineplus.notepad.data.DataConst.NOTE_ACTIVITY_REQUEST_CODE.ADD_NOTE_ACTIVITY_REQUEST_CODE;
import static com.taehyung.lineplus.notepad.data.DataConst.NOTE_ACTIVITY_REQUEST_CODE.UPDATE_NOTE_ACTIVITY_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String ACTIVITY_CALL_TYPE = "ACTIVITY_CALL_TYPE";

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddBtn;
    private FrameLayout mPopupLayout;

    private NoteViewModel mNoteViewModel;

    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mPopupFragmentList;

    private long updateId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        Utils.setDensity(this);

        mRecyclerView = findViewById(R.id.activity_main_recyclerview);
        mAddBtn = findViewById(R.id.activity_main_add_btn);
        mPopupLayout = findViewById(R.id.activity_main_popup_layout);

        // RecyclerView 셋팅
        NoteAdapter adapter = new NoteAdapter(getApplicationContext(), mOnItemClickListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomDividerItemDecoration customDividerItemDecoration = new CustomDividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL);
        customDividerItemDecoration.setDrawable(getDrawable(R.drawable.line_divider));
        mRecyclerView.addItemDecoration(customDividerItemDecoration);

        // model provider
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNote().observe(this, notes -> {
            if (mRecyclerView.getAdapter() instanceof NoteAdapter) {
                NoteAdapter noteAdapter = (NoteAdapter) mRecyclerView.getAdapter();
                noteAdapter.setNotes(notes);
            }
        });

        // Fragment 제어를 위한 Manager
        mFragmentManager = getSupportFragmentManager();
        mPopupFragmentList = new ArrayList<Fragment>();

        mAddBtn.setOnClickListener(mOnClickListener);
    }

    /**
     * FAB clickListener
     */
    private View.OnClickListener mOnClickListener = view -> {
        switch (view.getId()) {
            case R.id.activity_main_add_btn:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(ACTIVITY_CALL_TYPE, DataConst.NOTE_ACTIVITY_TYPE.TYPE_ADD);
                startActivityForResult(intent, ADD_NOTE_ACTIVITY_REQUEST_CODE);
                break;
        }
    };

    /**
     * RecyclerView Item Click Listener
     */
    private NoteAdapter.OnItemClickListener mOnItemClickListener = (view, position) -> {
        long curId = -1L;
        Object tag = view.getTag(R.attr.key_note_holder);
        NoteViewHolder holder = null;

        if (tag instanceof NoteViewHolder) {
            holder = (NoteViewHolder) tag;
            if (holder != null
                    && holder.itemView != null
                    && holder.itemView.getTag(R.attr.key_note_db_id) instanceof Long) {
                curId = (long) holder.itemView.getTag(R.attr.key_note_db_id);
            }
        }

        String failedText = "";
        switch (view.getId()) {
            case R.id.note_item_edit_btn:
                Note curNote = mNoteViewModel.getNote(curId);
                if (curNote != null) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra(ACTIVITY_CALL_TYPE, DataConst.NOTE_ACTIVITY_TYPE.TYPE_UPDATE);
                    intent.putExtra(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA, curNote);
                    startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
                } else {
                    failedText = getString(R.string.note_update_failed);
                    Toast.makeText(getApplicationContext(), failedText, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.note_item_remove_btn:
                // 삭제 처리
                if (curId != -1) {
                    try {
                        // 이미지 파일 삭제 처리
                        Note targetNote = mNoteViewModel.getNote(curId);
                        if (targetNote != null && !Utils.isListEmpty(targetNote.getImages())) {
                            for (String filePath : targetNote.getImages()) {
                                Utils.deleteImgFile(filePath);
                            }
                        }

                        mNoteViewModel.deleteNote(curId);
                    } catch (Exception e) {
                        failedText = getString(R.string.note_delete_failed);
                        Toast.makeText(getApplicationContext(), failedText, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.note_item_root_layout:
                showDetailPopup(curId);
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult() called. " + requestCode + " | " + resultCode + " | " + intent);

        if (resultCode != RESULT_OK || intent == null) {
            return;
        }

        if (requestCode == ADD_NOTE_ACTIVITY_REQUEST_CODE) {
            if (intent.getSerializableExtra(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA) instanceof Note) {
                Note input = (Note) intent.getSerializableExtra(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA);
                try {
                    mNoteViewModel.insertNote(input);
                } catch (Exception e) {
                    String text = getString(R.string.note_save_failed);
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else if (requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE) {
            if (intent.getSerializableExtra(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA) instanceof Note) {
                Note update = (Note) intent.getSerializableExtra(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA);
                try {
                    mNoteViewModel.updateNote(update);
                    updateId = update.getId();
                } catch (Exception e) {
                    String text = getString(R.string.note_save_failed);
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 상세보기 팝업 노출
     * show Detail Popup
     *
     * @param id target DB id
     */
    private void showDetailPopup(long id) {
        Log.d(TAG, "showDetailPopup() called. id: " + id);
        mAddBtn.hide();

        Note curNote = mNoteViewModel.getNote(id);

        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA, curNote);
        detailFragment.setArguments(bundle);

        mPopupFragmentList.add(detailFragment);

        try {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(mPopupLayout.getId(), detailFragment).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            String failedText = getString(R.string.note_detail_failed);
            Toast.makeText(getApplicationContext(), failedText, Toast.LENGTH_SHORT).show();
            mPopupFragmentList.remove(detailFragment);

            mAddBtn.show();
        }
    }

    /**
     * 상세보기 팝업 제거
     * destroy Detail Popup
     */
    public void destroyDetailPopup() {
        Log.d(TAG, "destroyDetailPopup() called.");
        mAddBtn.show();

        // 최상단 popup remove
        Fragment forwardFragment = mPopupFragmentList.get(mPopupFragmentList.size() - 1);

        try {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.remove(forwardFragment).commitAllowingStateLoss();

            mPopupFragmentList.remove(forwardFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called.");
        super.onResume();

        if (updateId != -1
                && mRecyclerView != null
                && mRecyclerView.getAdapter() instanceof NoteAdapter) {
            NoteAdapter adapter = (NoteAdapter) mRecyclerView.getAdapter();

            int position = adapter.getUpdatePosition(updateId);
            adapter.notifyItemChanged(position);

            updateId = -1;
        }
    }
}
