package com.taehyung.lineplus.notepad.ui.popup;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyung.lineplus.notepad.MainActivity;
import com.taehyung.lineplus.notepad.R;
import com.taehyung.lineplus.notepad.data.DataConst;
import com.taehyung.lineplus.notepad.data.db.Note;
import com.taehyung.lineplus.notepad.ui.recyclerview.CustomItemDecoration;
import com.taehyung.lineplus.notepad.ui.recyclerview.edit.ImageAdapter;
import com.taehyung.lineplus.notepad.utility.Utils;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final int MARQUEE_DELAY = 50;

    private MainActivity mMainActivity;

    private ConstraintLayout mMainlayout;
    private TextView mTitle;
    private TextView mDesc;
    private RecyclerView mImageRecyclerView;
    private Button mCancel;

    private ArrayList<String> mImageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.f_detail, container, false);
        initLayout(view);
        setData(getArguments());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Initialize Layout
     *
     * @param parentView View - inflated View
     */
    private void initLayout(View parentView) {
        mMainlayout = parentView.findViewById(R.id.f_detail_main_container);
        mTitle = parentView.findViewById(R.id.f_detail_title_text);
        mDesc = parentView.findViewById(R.id.f_detail_desc_text);
        mImageRecyclerView = parentView.findViewById(R.id.f_detail_image_recyclerview);
        mCancel = parentView.findViewById(R.id.f_detail_cancel_btn);

        mDesc.setMovementMethod(new ScrollingMovementMethod());

        // TODO 확대 보기 지원?
        ImageAdapter adapter = new ImageAdapter(DataConst.NOTE_ACTIVITY_TYPE.TYPE_DETAIL, getActivity(), null);
        mImageRecyclerView.setAdapter(adapter);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        CustomItemDecoration customItemDecoration = new CustomItemDecoration(3);
        mImageRecyclerView.addItemDecoration(customItemDecoration);

        mCancel.setOnClickListener(mOnClickListener);
        mMainlayout.setOnClickListener(mOnClickListener);
    }


    /**
     * Initialize Data
     */
    private void setData(Bundle bundle) {
        if (bundle == null) {
            mMainActivity.destroyDetailPopup();
            Toast.makeText(mMainActivity, getString(R.string.note_detail_failed), Toast.LENGTH_SHORT).show();
        }

        if (bundle.getSerializable(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA) instanceof Note) {
            Note curNote = (Note) bundle.getSerializable(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA);

            mTitle.setText(curNote.getTitle());
            mDesc.setText(curNote.getDesc());

            mImageList = curNote.getImages();
            if (Utils.isListEmpty(mImageList)) {
                mImageList = new ArrayList<>();
            }
            if (mImageRecyclerView.getAdapter() instanceof ImageAdapter) {
                ImageAdapter imageAdapter = (ImageAdapter) mImageRecyclerView.getAdapter();
                imageAdapter.setImages(mImageList);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTitle.setSelected(true);
                }
            }, MARQUEE_DELAY);
        } else {
            mMainActivity.destroyDetailPopup();
            Toast.makeText(mMainActivity, getString(R.string.note_detail_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener mOnClickListener = view -> {
        switch (view.getId()) {
            case R.id.f_detail_main_container:
                // click outside.. do nothing
                break;
            case R.id.f_detail_cancel_btn:
                mMainActivity.destroyDetailPopup();
                break;
        }
    };
}
