package com.lxj.matisse.sample;

/**
 * Description:
 * Create by dance, at 2019/5/19
 */
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.lxj.matisse.engine.ImageEngine;

///**
// * 如果你的Glide是3.x版本的，请使用这个类
// */
//
//public class Glide3Engine implements ImageEngine {
//
//    @Override
//    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()  // some .jpeg files are actually gif
//                .placeholder(placeholder)
//                .override(resize, resize)
//                .centerCrop()
//                .into(imageView);
//    }
//
//    @Override
//    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
//                                 Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .placeholder(placeholder)
//                .override(resize, resize)
//                .centerCrop()
//                .into(imageView);
//    }
//
//    @Override
//    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .override(resizeX, resizeY)
//                .priority(Priority.HIGH)
//                .fitCenter()
//                .into(imageView);
//    }
//
//    @Override
//    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .asGif()
//                .override(resizeX, resizeY)
//                .priority(Priority.HIGH)
//                .into(imageView);
//    }
//
//    @Override
//    public boolean supportAnimatedGif() {
//        return true;
//    }
//
//}