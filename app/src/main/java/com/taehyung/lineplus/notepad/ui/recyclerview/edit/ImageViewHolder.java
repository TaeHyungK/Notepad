package com.taehyung.lineplus.notepad.ui.recyclerview.edit;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ImageViewHolder.class.getSimpleName();

    protected ImageView mImageView;
    protected Button mRemoveBtn;

    public ImageViewHolder(@NonNull View itemView, ImageAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_item_image);
        mRemoveBtn = itemView.findViewById(R.id.image_item_remove_btn);

        mRemoveBtn.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, pos);
                }
            }
        });
    }
}
