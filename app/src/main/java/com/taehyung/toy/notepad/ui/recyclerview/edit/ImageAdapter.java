package com.taehyung.toy.notepad.ui.recyclerview.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.toy.notepad.R;
import com.taehyung.toy.notepad.data.DataConst;
import com.taehyung.toy.notepad.utility.Utils;

import java.util.ArrayList;
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
        Log.d(TAG, "onBindViewHolder() called. position: " + position);
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

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addImage(String image) {
        if (mImages == null) {
            mImages = new ArrayList<>();
        }

        mImages.add(0, image);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages != null ? mImages.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
