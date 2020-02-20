package com.taehyung.lineplus.notepad.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    /********************************************************************************************************
     * DENSITY
     ********************************************************************************************************/
    private static float mDensity;

    public static void setDensity(Context context) {
        mDensity = context.getResources().getDisplayMetrics().density;
        Log.d(TAG, "setDensity() density: " + mDensity);
    }

    public static int changeDP2Pixel(int d) {
        return (int) (d * mDensity + 0.5f);
    }

    public static int changeDP2Pixel(float d) {
        return (int) (d * mDensity + 0.5f);
    }

    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            Log.d(TAG, "bitmapToByteArray() bitmap is null. do nothing.");
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos) ;
        byte[] byteArray = baos.toByteArray() ;
        return byteArray ;
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        if (byteArray == null) {
            Log.d(TAG, "byteArrayToBitmap() byteArray is null. do nothing.");
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }
}
