package com.chiemy.app.cashier.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

/**
 * Created by chiemy on 16/2/20.
 */
public class RoundDrawableUtil {
    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param width
     * @param height
     * @param imageView
     */
    public static void loadRoundImage(Context context, final String url, final int width, final int height, final ImageView imageView){
        final Context con = context.getApplicationContext();
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(con).load(url).asBitmap().into(width, height).get();
                    final RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(con.getResources(), bitmap);
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
