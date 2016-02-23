package com.chiemy.app.cashier.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chiemy on 16/2/22.
 */
public class BitmapUtil {
    /**
     * 计算采样比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 取小比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 从图片字节数组中生产指定宽高的图片
     * （保持原图的宽高比，采样率取 原图高度/需要高度 与 原图宽度/需要宽度 中比例较小的一个）
     * @param data 图片字节数组
     * @param reqWidth	需要的宽度
     * @param reqHeight	需要的高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(byte [] data, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        //只获得原始图片的宽度和高度
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length,options);
        options.inPurgeable = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 从指定的路径下，生成指定宽高的图片
     * @param filePath 文件路径
     * @param reqWidth 宽
     * @param reqHeight 高
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(String filePath, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inPurgeable = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.d("-", ">>>insamplesize = " + options.inSampleSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 将图片旋转一定角度
     * @param bitmap 要旋转的图片
     * @param degree 旋转角度（正数为逆时针）
     * @return	旋转后的图片
     */
    public static Bitmap getRotateBitmap(Bitmap bitmap,float degree){
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree,
                (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
        return rotateBitmap;
    }

    /**
     * 截取图片
     * @param bitmap 被截取的图片
     * @param rect 截取范围
     * @return
     */
    public static Bitmap captureBitmap(Bitmap bitmap, RectF rect){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int captrueWidth = (int)(rect.right-rect.left);
        int captrueHeight = (int)(rect.bottom-rect.top);
        captrueWidth = captrueWidth > width ? width : captrueWidth;
        captrueHeight = captrueHeight > height ? height : captrueHeight;
        return Bitmap.createBitmap(bitmap, (int)rect.left, (int)rect.top,captrueWidth, captrueHeight);
    }

    /**
     * 从图片中间截取方形图片
     * @param bitmap 被截取的图片
     * @return
     */
    public static Bitmap captureSquareBitmap(Bitmap bitmap){
        int x = 0;
        int y = 0;
        int width;
        int height;
        if (bitmap.getWidth() > bitmap.getHeight()){
            height = bitmap.getHeight();
            width = height;
            x = (bitmap.getWidth() - bitmap.getHeight())/2;
        }else{
            width = bitmap.getWidth();
            height = width;
            y = (bitmap.getHeight() - bitmap.getWidth())/2;
        }
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, x, y, width, height);
        return bitmap1;
    }

    private int getFileExifRotation(Uri uri) throws IOException {
        ExifInterface exifInterface = new ExifInterface(uri.getPath());
        int orientation = exifInterface
                .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    private Bitmap rotate(Bitmap source,int rotate){
        if(rotate > 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(rotate);
            Bitmap rotateBitmap = Bitmap.createBitmap(source, 0, 0,source.getWidth(), source.getHeight(), matrix, true);
            if(rotateBitmap != null) {
                source.recycle();
                return rotateBitmap;
            }
        }
        return source;
    }

    /**
     * 压缩图片
     * @param path 被压缩图片的路径，压缩完成后替换原图片
     * @param width 宽
     * @param height 高
     * @param quality 质量
     */
    public static void compress(String path, String compressSavePath, int width, int height, int quality){
        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(path, width, height);
        if (bitmap != null){
            saveBitmap(bitmap, compressSavePath, quality);
        }
    }

    /**
     * 保存图片
     * @param filePath
     */
    public static void saveBitmap(Bitmap bitmap, String filePath, int quality){
        File f = new File(filePath);
        if (f.exists()){
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }
}
