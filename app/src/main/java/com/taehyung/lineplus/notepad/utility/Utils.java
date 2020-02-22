package com.taehyung.lineplus.notepad.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.taehyung.lineplus.notepad.data.DataConst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public static String createImgFile(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            Log.d(TAG, "createImgFile() bitmap is null. do nothing.");
            return null;
        }
        String filePath = null;
        String dirPath = context.getCacheDir() + File.separator + "notepad_img";
        String filename = DataConst.FILE_PREFIX + System.currentTimeMillis() + DataConst.FILE_SUFFIX;

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File imageFile = new File(dir, filename);
        FileOutputStream fos = null;
        try {
            imageFile.createNewFile();
            fos = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            filePath = imageFile.getPath();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        Log.d(TAG, "createImgFile() filePath: " + filePath);
        return filePath;
    }

    public static void deleteImgFile(String filePath) {
        File targetFile = new File(filePath);

        if (targetFile.exists()) {
            targetFile.delete();
        }
    }

    public static Bitmap getImgFile(Context context, String filePath) {
        Log.d(TAG, "getImgFile() called. filePath: " + filePath);
        File targetFile = new  File(filePath);

        Bitmap bitmap = null;

        if(targetFile.exists()) {
            bitmap = BitmapFactory.decodeFile(targetFile.getAbsolutePath());
        }

        return bitmap;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            Log.d(TAG, "bitmapToByteArray() bitmap is null. do nothing.");
            return null;
        }

        byte[] byteArray = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byteArray = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArray ;
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        if (byteArray == null) {
            Log.d(TAG, "byteArrayToBitmap() byteArray is null. do nothing.");
            return null;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
