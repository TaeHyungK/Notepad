package com.taehyung.lineplus.notepad.ui.recyclerview.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.R;
import com.taehyung.lineplus.notepad.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();

    private List<String> mImages;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public ImageAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        ImageViewHolder holder = new ImageViewHolder(view, mOnItemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (!Utils.isListEmpty(mImages)) {
            String curImagePath = mImages.get(position);

            Bitmap bitmap = Utils.getImgFile(mContext, curImagePath);
            if (bitmap != null) {
                holder.mImageView.setImageBitmap(bitmap);
            }

            holder.mRemoveBtn.setTag(R.attr.key_image_path, curImagePath);
        }
    }

    public void setImages(List<String> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mImages != null ? mImages.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
