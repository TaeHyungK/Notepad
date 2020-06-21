package com.taehyung.toy.notepad.ui.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.toy.notepad.utility.Utils;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private float spacing;

    public CustomItemDecoration(float spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int lastPosition = parent.getAdapter().getItemCount() - 1; //item last position
        int position = parent.getChildAdapterPosition(view); //item position

        if (position == 0) {
            outRect.right = Utils.changeDP2Pixel(spacing);
        } else if (position == lastPosition) {
            outRect.left = Utils.changeDP2Pixel(spacing);
        } else {
            outRect.left = Utils.changeDP2Pixel(spacing / 2);
            outRect.right = Utils.changeDP2Pixel(spacing / 2);
        }
    }
}
