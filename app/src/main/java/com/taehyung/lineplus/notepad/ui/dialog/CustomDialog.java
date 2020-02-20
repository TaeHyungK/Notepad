package com.taehyung.lineplus.notepad.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.taehyung.lineplus.notepad.R;

public class CustomDialog extends Dialog {
    private static final String TAG = CustomDialog.class.getSimpleName();

    private Button mDismissBtn;
    private Button mPhotoBtn;
    private Button mGalleryBtn;
    private Button mUrlBtn;

    private View.OnClickListener mOnClickListener;

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public CustomDialog(Context context, View.OnClickListener onClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mOnClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.d_select_image);

        mDismissBtn = findViewById(R.id.d_select_image_dismiss_btn);
        mPhotoBtn = findViewById(R.id.d_select_image_photo);
        mGalleryBtn = findViewById(R.id.d_select_image_gallery);
        mUrlBtn = findViewById(R.id.d_select_image_url);

        if (mOnClickListener != null) {
            mDismissBtn.setOnClickListener(mOnClickListener);
            mPhotoBtn.setOnClickListener(mOnClickListener);
            mGalleryBtn.setOnClickListener(mOnClickListener);
            mUrlBtn.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // wrap_content 사용으로 외부 영역 클릭 이벤트를 알 수 없어 오버라이딩
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            this.dismiss();
        }
        return super.dispatchTouchEvent(ev);
    }
}
