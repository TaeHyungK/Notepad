package com.taehyung.toy.notepad.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.taehyung.toy.notepad.R;

public class CustomDialog extends Dialog {
    private static final String TAG = CustomDialog.class.getSimpleName();

    private Button mDismissBtn;
    private Button mPhotoBtn;
    private Button mGalleryBtn;
    private Button mUrlBtn;
    private EditText mUrlEdit;
    private Button mUrlEditConfirmBtn;

    private View.OnClickListener mOnClickListener;

    private boolean isEditShowing;

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
        mUrlEdit = findViewById(R.id.d_select_image_url_edit);
        mUrlEditConfirmBtn = findViewById(R.id.d_select_image_url_edit_confirm);

        if (mOnClickListener != null) {
            mDismissBtn.setOnClickListener(mOnClickListener);
            mPhotoBtn.setOnClickListener(mOnClickListener);
            mGalleryBtn.setOnClickListener(mOnClickListener);
            mUrlBtn.setOnClickListener(mOnClickListener);
            mUrlEditConfirmBtn.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public void show() {
        isEditShowing = false;
        if (mUrlEdit != null) {
            mUrlEdit.setVisibility(View.GONE);
        }
        super.show();
    }

    /**
     * URL edit layout show/hide
     *
     * @param isShow true - show / false - hide
     */
    public void switchUrlEdit(boolean isShow) {
        Log.d(TAG, "switchUrlEdit() called. isShow: " + isShow);
        if (isShow) {
            mUrlEdit.setVisibility(View.VISIBLE);
            mUrlEditConfirmBtn.setVisibility(View.VISIBLE);
        } else {
            mUrlEdit.setVisibility(View.GONE);
            mUrlEditConfirmBtn.setVisibility(View.GONE);
        }

        isEditShowing = isShow;
    }

    public boolean isUrlEditShowing() {
        return isEditShowing;
    }

    /**
     * get UrlEdit Text
     *
     * @return String
     */
    public String getUrlString() {
        String result = null;

        if (mUrlEdit != null) {
            result = mUrlEdit.getText().toString();
        }

        return result;
    }
}
