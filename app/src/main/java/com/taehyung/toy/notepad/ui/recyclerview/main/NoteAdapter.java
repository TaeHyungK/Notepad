package com.taehyung.toy.notepad.ui.recyclerview.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.toy.notepad.R;
import com.taehyung.toy.notepad.data.db.Note;
import com.taehyung.toy.notepad.utility.Utils;

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

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called. position: " + position);
        if (!Utils.isListEmpty(mNotes)) {
            Note curNote = mNotes.get(position);
            Log.d(TAG, "onBindViewHolder() curNote: " + curNote.getTitle()
                    + " | " + curNote.getDesc()
                    + " | " + curNote.getImages());

            if (!Utils.isListEmpty(curNote.getImages())) {
                String filePath = curNote.getImages().get(0);

                Bitmap bitmap = Utils.getImgFile(mContext, filePath);
                if (bitmap != null) {
                    holder.mImageView.setImageBitmap(bitmap);
                } else {
                    holder.mImageView.setImageDrawable(mContext.getDrawable(R.drawable.default_no_image));
                }
            }
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

    public int getUpdatePosition(long id) {
        int position = -1;

        for (int i = 0; i < mNotes.size(); i++) {
            Note curNote = mNotes.get(i);
            if (curNote.getId() == id) {
                position = i;
                break;
            }
        }

        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
