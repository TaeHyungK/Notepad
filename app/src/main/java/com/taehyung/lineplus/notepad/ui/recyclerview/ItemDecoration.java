package com.taehyung.lineplus.notepad.ui.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.utility.Utils;

public class ItemDecoration extends DividerItemDecoration {
    private Drawable mDivider;

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public ItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int lastPosition = parent.getAdapter().getItemCount() - 1; // item last position
        int position = parent.getChildAdapterPosition(view); // item position

        if (position == 0) {
            // 최상단일 경우
            outRect.bottom = Utils.changeDP2Pixel(10);
        } else if (position == lastPosition) {
            // 최하단일 경우
            outRect.top = Utils.changeDP2Pixel(10);
        } else {
            // 가운데 아이템일 경우
            outRect.top = Utils.changeDP2Pixel(5);
            outRect.bottom = Utils.changeDP2Pixel(5);

        }
    }
}
