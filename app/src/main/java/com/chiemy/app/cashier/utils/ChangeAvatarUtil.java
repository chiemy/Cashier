package com.chiemy.app.cashier.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by chiemy on 16/2/22.
 */
public class ChangeAvatarUtil {

    public static final int REQUEST_SELECT_PIC = 11;

    /**
     * 调用系统图库，选择图片
     * @param activity 需要重写activity的onActivityResult方法<br>
     *                 拍照结束后，获取图片地址
     *<pre>{@code
     * if (resultCode == RESULT_OK) {
     *      if (requestCode == ChangeAvatarUtil.REQUEST_SELECT_PIC) {
     *          Uri selectedImage = data.getData();
     *          String path = SelectPicUtil.getPath(this, selectedImage);
     *      }
     * }}</pre>
     */
    public static void selectPicture(Activity activity) {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
        getImageIntent.setType("image/*");
        activity.startActivityForResult(getImageIntent, REQUEST_SELECT_PIC);
    }

    /**
     * 拍照请求
     */
    public static final int REQUEST_TAKE_PHOTO = 12;

    /**
     * 调用系统相机拍照
     * @param activity 需要重写activity的onActivityResult方法<br>
     *                 拍照结束后，获取图片
     *<pre>{@code
     * if (resultCode == RESULT_OK) {
     *      if (requestCode == ChangeAvatarUtil.REQUEST_TAKE_PHOTO) {
     *          Bitmap bitmap = (Bitmap) data.getExtras().get("data");
     *      }
     * }
     * }</pre>
     * 图片默认保存路径：Environment.getExternalStorageDirectory()下avatar.jpg
     *
     * @param saveFilePath 拍照后保存的路径，包括文件名
     */
    public static void takePhoto(Activity activity, String saveFilePath) {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(saveFilePath)));
        activity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 获取拍照后图片保存的路径（包括文件名）
     * @return
     */
    public static String getAvatarImagePath(String userId){
        return Environment.getExternalStorageDirectory() + File.separator + userId + ".jpg";
    }

    /**
     * 压缩图片
     * @param path 图片路径
     * @param size 图片尺寸（方形）
     * @param quality 图片质量
     */
    public static void compressAvatar(final String path, final String userId, final int size, final int quality, final CompressListener listener){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String compressSavePath = getAvatarImagePath(userId);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null){
                    bitmap = BitmapUtil.captureSquareBitmap(bitmap);
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    BitmapUtil.saveBitmap(bitmap1, compressSavePath, quality);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null){
                            listener.onCompressFinish(compressSavePath);
                        }
                    }
                });
            }
        }).start();
    }

    public interface CompressListener{
        void onCompressFinish(String path);
    }
}
