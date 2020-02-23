package com.taehyung.lineplus.notepad.ui.recyclerview.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.R;
import com.taehyung.lineplus.notepad.data.DataConst;
import com.taehyung.lineplus.notepad.utility.Utils;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();

    private @DataConst.NOTE_ACTIVITY_TYPE String mType;
    private List<String> mImages;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public ImageAdapter(@DataConst.NOTE_ACTIVITY_TYPE String type, Context context, OnItemClickListener onItemClickListener) {
        mType = type;
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
        switch (mType) {
            case DataConst.NOTE_ACTIVITY_TYPE.TYPE_ADD:
            case DataConst.NOTE_ACTIVITY_TYPE.TYPE_UPDATE:
                holder.mRemoveBtn.setVisibility(View.VISIBLE);
                break;
            case DataConst.NOTE_ACTIVITY_TYPE.TYPE_DETAIL:
                break;
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
