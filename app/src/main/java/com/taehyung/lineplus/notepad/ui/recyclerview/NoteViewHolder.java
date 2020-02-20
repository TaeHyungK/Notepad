package com.taehyung.lineplus.notepad.ui.recyclerview;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = NoteViewHolder.class.getSimpleName();

    protected ConstraintLayout mRootLayout;
    protected ImageView mImageView;
    protected TextView mTitle;
    protected TextView mDesc;
    protected Button mEditBtn;
    protected Button mRemoveBtn;

    public NoteViewHolder(@NonNull View itemView, NoteAdapter.OnItemClickListener mOnItemClickListener) {
        super(itemView);
        mRootLayout = itemView.findViewById(R.id.note_item_root_layout);
        mImageView = itemView.findViewById(R.id.note_item_thumbnail);
        mTitle = itemView.findViewById(R.id.note_item_title);
        mDesc = itemView.findViewById(R.id.note_item_desc);
        mEditBtn = itemView.findViewById(R.id.note_item_edit_btn);
        mRemoveBtn = itemView.findViewById(R.id.note_item_remove_btn);

        mRootLayout.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, pos) ;
                }
            }
        });

        mEditBtn.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, pos) ;
                }
            }
        });

        mRemoveBtn.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, pos) ;
                }
            }
        });
    }
}
