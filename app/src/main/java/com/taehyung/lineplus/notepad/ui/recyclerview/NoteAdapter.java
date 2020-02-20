package com.taehyung.lineplus.notepad.ui.recyclerview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.R;
import com.taehyung.lineplus.notepad.data.db.Note;
import com.taehyung.lineplus.notepad.utility.Utils;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private static final String TAG = NoteAdapter.class.getSimpleName();

    private List<Note> mNotes;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public NoteAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        NoteViewHolder holder = new NoteViewHolder(view, mOnItemClickListener);

        Log.d(TAG, "onCreateViewHolder() " + holder.mTitle + " | " + holder.mDesc);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called. position: " + position);
        if (!Utils.isListEmpty(mNotes)) {
            Note curNote = mNotes.get(position);
            Log.d(TAG, "onBindViewHolder() curNote: " + curNote.getTitle() + " | " + curNote.getDesc());
            // TODO 썸네일 이미지 바인드 처리
//            holder.mImageView.setImageResource();
            holder.mTitle.setText(curNote.getTitle());
            holder.mDesc.setText(curNote.getDesc());

            holder.mEditBtn.setTag(R.attr.key_note_holder, holder);
            holder.mRemoveBtn.setTag(R.attr.key_note_holder, holder);
            holder.mRootLayout.setTag(R.attr.key_note_holder, holder);

            holder.itemView.setTag(R.attr.key_note_db_id, curNote.mId);
        } else {
            Toast.makeText(mContext, "Note is Empty!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}