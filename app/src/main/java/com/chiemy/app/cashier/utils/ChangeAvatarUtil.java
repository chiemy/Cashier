package com.chiemy.app.cashier.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
     */
    public static void takePhoto(Activity activity) {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getAvatarImagePath())));
        activity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 获取拍照后图片保存的路径（包括文件名）
     * @return
     */
    public static String getAvatarImagePath(){
        return Environment.getExternalStorageDirectory() + File.separator + "avatar.jpg";
    }
}
