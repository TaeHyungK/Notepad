package com.taehyung.lineplus.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.taehyung.lineplus.notepad.data.DataConst;
import com.taehyung.lineplus.notepad.data.db.Note;
import com.taehyung.lineplus.notepad.ui.dialog.CustomDialog;
import com.taehyung.lineplus.notepad.ui.recyclerview.CustomDividerItemDecoration;
import com.taehyung.lineplus.notepad.ui.recyclerview.CustomItemDecoration;
import com.taehyung.lineplus.notepad.ui.recyclerview.edit.ImageAdapter;
import com.taehyung.lineplus.notepad.utility.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Add & Edit Activity
 */
public class EditActivity extends AppCompatActivity {
    private static final String TAG = EditActivity.class.getSimpleName();

    private String mType;
    private Note mCurNote;
    private ArrayList<String> mImageList;

    private EditText mTitleEdit;
    private EditText mDescEdit;
    private RecyclerView mImageRecyclerView;
    private Button mAddImgBtn;

    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mTitleEdit = findViewById(R.id.activity_edit_title_edit);
        mDescEdit = findViewById(R.id.activity_edit_desc_edit);
        mImageRecyclerView = findViewById(R.id.activity_edit_image_recyclerview);
        mAddImgBtn = findViewById(R.id.activity_edit_image_add_btn);

        initLayout(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션바 정의
        getMenuInflater().inflate(R.menu.edit_actionbar, menu);
        return true;
    }

    private void initLayout(Intent intent) {
        // 이미지 추가 Button 셋팅
        mAddImgBtn.setTag(R.attr.key_add_image, DataConst.DIALOG_SELECT_IMAGE_TYPE.ADD);
        mAddImgBtn.setOnClickListener(view -> {
            if (mDialog == null) {
                mDialog = new CustomDialog(EditActivity.this, mDialogClickListener);
            }
            mDialog.show();
        });

        // 첨부된 이미지 RecyclerView 셋팅
        ImageAdapter adapter = new ImageAdapter(DataConst.NOTE_ACTIVITY_TYPE.TYPE_ADD, getApplicationContext(), mOnItemClickListener);
        mImageRecyclerView.setAdapter(adapter);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        CustomItemDecoration customItemDecoration = new CustomItemDecoration(5);
        mImageRecyclerView.addItemDecoration(customItemDecoration);

        // 수정인 경우 기존 데이터 셋팅
        if (intent == null) {
            Log.d(TAG, "initLayout() intent is null. early return.");
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // type 을 넣어주지 않은 경우 ADD type 으로 설정(default type is ADD)
            mType = bundle.getString(MainActivity.ACTIVITY_CALL_TYPE, DataConst.NOTE_ACTIVITY_TYPE.TYPE_ADD);
            switch (mType) {
                case DataConst.NOTE_ACTIVITY_TYPE.TYPE_ADD:
                    if (Utils.isListEmpty(mImageList)) {
                        mImageList = new ArrayList<>();
                    }
                    break;
                case DataConst.NOTE_ACTIVITY_TYPE.TYPE_UPDATE:
                    if (bundle.getSerializable(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA) instanceof Note) {
                        mCurNote = (Note) bundle.getSerializable(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA);
                        mTitleEdit.setText(mCurNote.getTitle());
                        mDescEdit.setText(mCurNote.getDesc());
                        mImageList = mCurNote.getImages();
                        if (Utils.isListEmpty(mImageList)) {
                            mImageList = new ArrayList<>();
                        }
                        initImageLayout(mImageList);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_save_btn:
                boolean isResult = saveBtn();
                if (isResult) {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * save 버튼 선택 시 동작
     * save Btn process
     *
     * @return true - save 가능, false - save 불가 (누락된 내용 있을 경우)
     */
    private boolean saveBtn() {
        Log.d(TAG, "saveBtn() called. ");
        String guide = null;
        if (TextUtils.isEmpty(mTitleEdit.getText())) {
            guide = getString(R.string.note_add_cant_save_no_title_guide);
        } else if (TextUtils.isEmpty(mDescEdit.getText())) {
            guide = getString(R.string.note_add_cant_save_no_desc_guide);
        }

        if (guide != null) {
            Toast.makeText(getApplicationContext(), guide, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (DataConst.NOTE_ACTIVITY_TYPE.TYPE_ADD.equals(mType)) {
            mCurNote = new Note(mTitleEdit.getText().toString(), mDescEdit.getText().toString());
            mCurNote.setImages(mImageList);
        } else if (DataConst.NOTE_ACTIVITY_TYPE.TYPE_UPDATE.equals(mType)) {
            mCurNote.setTitle(mTitleEdit.getText().toString());
            mCurNote.setDesc(mDescEdit.getText().toString());
            mCurNote.setImages(mImageList);
        }

        Intent intent = new Intent();
        intent.putExtra(DataConst.NOTE_EXTRA.EXTRA_NOTE_DATA, mCurNote);
        setResult(RESULT_OK, intent);
        return true;
    }

    /**
     * 이미지 유무에 따른 이미지 영역 Layout 셋팅
     *
     * @param images ArrayList<String>
     */
    private void initImageLayout(ArrayList<String> images) {
        boolean hasImages = !Utils.isListEmpty(images);
        if (hasImages) {
            if (mImageRecyclerView.getAdapter() instanceof ImageAdapter) {
                ImageAdapter imageAdapter = (ImageAdapter) mImageRecyclerView.getAdapter();
                imageAdapter.setImages(images);
            }
        }
    }

    private View.OnClickListener mDialogClickListener = view -> {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.d_select_image_dismiss_btn:
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                break;
            case R.id.d_select_image_photo:
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, DataConst.DIALOG_SELECT_IMAGE_TYPE.PHOTO);
                break;
            case R.id.d_select_image_gallery:
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, DataConst.DIALOG_SELECT_IMAGE_TYPE.GALLERY);
                break;
            case R.id.d_select_image_url:
                mDialog.switchUrlEdit(!mDialog.isUrlEditShowing());
                break;
            case R.id.d_select_image_url_edit_confirm:
                if (mDialog.isUrlEditShowing()) {
                    String url = mDialog.getUrlString();

                    Log.d(TAG, "mDialogClickListener URL_CONFIRM_BTN url: " + url);

                    if (url != null) {
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(url)
                                .listener(new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        e.printStackTrace();

                                        String guide = getString(R.string.dialog_select_image_failed);
                                        Toast.makeText(getApplicationContext(), guide, Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                        if (resource != null) {
                                            String imagePath = Utils.createImgFile(getApplicationContext(), resource);
                                            if (imagePath != null) {
                                                mImageList.add(imagePath);
                                                if (mImageRecyclerView.getAdapter() instanceof ImageAdapter) {
                                                    ImageAdapter imageAdapter = (ImageAdapter) mImageRecyclerView.getAdapter();
                                                    imageAdapter.setImages(mImageList);
                                                }
                                            } else {
                                                String guide = getString(R.string.dialog_select_image_failed);
                                                Toast.makeText(getApplicationContext(), guide, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        return false;
                                    }
                                })
                                .submit();
                    } else {
                        String guide = getString(R.string.dialog_cant_load_url_image);
                        Toast.makeText(getApplicationContext(), guide, Toast.LENGTH_SHORT).show();
                    }

                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called. " + requestCode + " | " + resultCode + " | " + data);

        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DataConst.DIALOG_SELECT_IMAGE_TYPE.PHOTO:
                    try {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            String filePath = Utils.createImgFile(getApplicationContext(), bitmap);
                            if (filePath != null) {
                                mImageList.add(filePath);
                                if (mImageRecyclerView.getAdapter() instanceof ImageAdapter) {
                                    ImageAdapter imageAdapter = (ImageAdapter) mImageRecyclerView.getAdapter();
                                    imageAdapter.setImages(mImageList);
                                }
                            } else {
                                String guide = getString(R.string.dialog_select_image_failed);
                                Toast.makeText(getApplicationContext(), guide, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case DataConst.DIALOG_SELECT_IMAGE_TYPE.GALLERY:
                    try {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                        bitmap = ImageDecoder.decodeBitmap(source);
                        if (bitmap != null) {
                            String filePath = Utils.createImgFile(getApplicationContext(), bitmap);
                            if (filePath != null) {
                                mImageList.add(filePath);
                                if (mImageRecyclerView.getAdapter() instanceof ImageAdapter) {
                                    ImageAdapter imageAdapter = (ImageAdapter) mImageRecyclerView.getAdapter();
                                    imageAdapter.setImages(mImageList);
                                }
                            } else {
                                String guide = getString(R.string.dialog_select_image_failed);
                                Toast.makeText(getApplicationContext(), guide, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * Image RecyclerView item click listener
     */
    private ImageAdapter.OnItemClickListener mOnItemClickListener = (view, position) -> {
        Object tag = view.getTag(R.attr.key_image_path);
        String imagePath = null;

        if (tag instanceof String) {
            imagePath = (String) tag;
        }

        String failedText = "";
        switch (view.getId()) {
            case R.id.image_item_remove_btn:
                if (imagePath != null) {
                    Utils.deleteImgFile(imagePath);
                    mImageList.remove(imagePath);
                    if (mImageRecyclerView.getAdapter() instanceof ImageAdapter) {
                        ImageAdapter imageAdapter = (ImageAdapter) mImageRecyclerView.getAdapter();
                        imageAdapter.setImages(mImageList);
                    }
                } else {
                    failedText = getString(R.string.note_add_image_remove_failed);
                    Toast.makeText(getApplicationContext(), failedText, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    };
}
