package com.chiemy.app.cashier.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

/**
 * Created by chiemy on 16/2/20.
 */
public class RoundDrawableUtil {
    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param radius
     * @param imageView
     */
    public static void loadRoundImage(Context context, final String url, final int radius, final ImageView imageView){
        final Context con = context.getApplicationContext();
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(con).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    Bitmap bitmap1 = BitmapUtil.captureSquareBitmap(bitmap);
                    Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, radius, radius, true);

                    final RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(con.getResources(), bitmap2);
                    drawable.setCircular(true);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(drawable);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
