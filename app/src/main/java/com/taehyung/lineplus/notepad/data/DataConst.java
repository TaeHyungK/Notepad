package com.taehyung.lineplus.notepad.data;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataConst {
    @StringDef({
            NOTE_EXTRA.EXTRA_TITLE_DATA,
            NOTE_EXTRA.EXTRA_DESC_DATA,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface NOTE_EXTRA {
        String EXTRA_TITLE_DATA = "EXTRA_TITLE_DATA";
        String EXTRA_DESC_DATA = "EXTRA_DESC_DATA";
        String EXTRA_NOTE_DATA = "EXTRA_NOTE_ID";
        String EXTRA_NOTE_ID = "EXTRA_NOTE_DATA";
    }

    @IntDef({
            NOTE_ACTIVITY_REQUEST_CODE.ADD_NOTE_ACTIVITY_REQUEST_CODE,
            NOTE_ACTIVITY_REQUEST_CODE.UPDATE_NOTE_ACTIVITY_REQUEST_CODE,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface NOTE_ACTIVITY_REQUEST_CODE {
        int ADD_NOTE_ACTIVITY_REQUEST_CODE = 1000;
        int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 1001;
    }

    @StringDef({
            NOTE_ACTIVITY_TYPE.TYPE_ADD,
            NOTE_ACTIVITY_TYPE.TYPE_UPDATE,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface NOTE_ACTIVITY_TYPE {
        String TYPE_ADD = "TYPE_ADD";
        String TYPE_UPDATE = "TYPE_UPDATE";
    }

    @IntDef({
            DIALOG_SELECT_IMAGE_TYPE.ADD,
            DIALOG_SELECT_IMAGE_TYPE.PHOTO,
            DIALOG_SELECT_IMAGE_TYPE.GALLERY,
            DIALOG_SELECT_IMAGE_TYPE.URL,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface DIALOG_SELECT_IMAGE_TYPE {
        int ADD = 10000;
        int PHOTO = 10001;
        int GALLERY = 10002;
        int URL = 10003;
    }
}
