/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lxj.matisse.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.matisse.CaptureMode;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.PicassoEngine;
import com.lxj.matisse.filter.Filter;
import java.io.File;
import java.util.List;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 23;

    private UriAdapter mAdapter;
    TextView captureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.zhihu).setOnClickListener(this);
        findViewById(R.id.dracula).setOnClickListener(this);
        findViewById(R.id.jumpCapture).setOnClickListener(this);
        captureText = findViewById(R.id.captureText);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new UriAdapter());
    }

    @Override
    public void onClick(final View v) {
        captureText.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.zhihu:
                Matisse.from(SampleActivity.this)
                        .choose(MimeType.ofAll()) //显示所有文件类型，比如图片和视频，
//                        .capture(true)//是否显示拍摄按钮，默认不显示
                        .capture(true, CaptureMode.All)//是否显示拍摄按钮，默认不显示
//                        .maxSelectable(9) //默认9张
                        //添加图片过滤器，比如过滤掉小于10K的图片
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        //设置选中图片的监听器
//                        .setOnSelectedListener(new OnSelectedListener() {
//                            @Override
//                            public void onSelected(
//                                    @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
//                                // DO SOMETHING IMMEDIATELY HERE
//                                Log.e("onSelected", "onSelected: pathList=" + pathList);
//
//                            }
//                        })
//                        .originalEnable(true)//是否显示原图，默认显示
                        //设置原图选中和取消选中的监听器
//                        .setOnCheckedListener(new OnCheckedListener() {
//                            @Override
//                            public void onCheck(boolean isChecked) {
//                                // DO SOMETHING IMMEDIATELY HERE
//                                Log.e("isChecked", "onCheck: isChecked=" + isChecked);
//                            }
//                        })
//                        .imageEngine(new GlideEngine()) // 默认是Glide4.x版本的加载器，如果你用的是Glide4.x，则无需设置
                        //.imageEngine(new PicassoEngine())//如果你用的是Picasso
//                        .imageEngine(new Glide3Engine())//如果你用的是Glide3.x版本，Glide3Engine这个类在demo中
                        .forResult(REQUEST_CODE_CHOOSE); //请求码
                break;
            case R.id.dracula:
                Matisse.from(SampleActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Dracula)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .originalEnable(true)
                        .maxSelectable(1)
                        .isCrop(true)
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            case R.id.jumpCapture:
                Matisse.from(SampleActivity.this)
                        .jumpCapture()//直接跳拍摄，默认可以同时拍摄照片和视频
//                        .jumpCapture(CaptureMode.Image)//只拍照片
//                        .jumpCapture(CaptureMode.Video)//只拍视频
                        .isCrop(true) //开启裁剪
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
        }
        mAdapter.setData(null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            String capturePath = null;
            String videoPath = null;
            String cropPath = null;
            if((videoPath = Matisse.obtainCaptureVideoResult(data))!=null){
                //录制的视频
                capturePath = Matisse.obtainCaptureImageResult(data);
                captureText.setVisibility(View.VISIBLE);
                captureText.setText("视频路径："+videoPath
                +" \n 第一帧图片："+capturePath);
            }else if((capturePath = Matisse.obtainCaptureImageResult(data))!=null){
                captureText.setVisibility(View.VISIBLE);
                captureText.setText("拍照路径："+capturePath);
            }else if((cropPath = Matisse.obtainCropResult(data))!=null){
                captureText.setVisibility(View.VISIBLE);
                String s = "裁剪的路径："+cropPath;
                s += "\n 原图路径：" + Matisse.obtainSelectPathResult(data).get(0);
                captureText.setText(s);
            }else {
                mAdapter.setData(Matisse.obtainSelectUriResult(data), Matisse.obtainSelectPathResult(data));
                Log.e("OnActivityResult", "originalState: "+String.valueOf(Matisse.obtainOriginalState(data)));
            }
        }
    }

    private static class UriAdapter extends RecyclerView.Adapter<UriAdapter.UriViewHolder> {

        private List<Uri> mUris;
        private List<String> mPaths;

        void setData(List<Uri> uris, List<String> paths) {
            mUris = uris;
            mPaths = paths;
            notifyDataSetChanged();
        }

        @Override
        public UriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UriViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.uri_item, parent, false));
        }

        @Override
        public void onBindViewHolder(UriViewHolder holder, int position) {
            holder.mUri.setText(mUris.get(position).toString());
            String sz = Formatter.formatFileSize(holder.itemView.getContext(), new File(mPaths.get(position)).length());
            holder.mPath.setText(mPaths.get(position) + " \n 大小："+sz);

            holder.mUri.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
            holder.mPath.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
            Glide.with(holder.image.getContext()).load(mUris.get(position)).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return mUris == null ? 0 : mUris.size();
        }

        static class UriViewHolder extends RecyclerView.ViewHolder {

            private TextView mUri;
            private TextView mPath;
            private ImageView image;

            UriViewHolder(View contentView) {
                super(contentView);
                mUri = (TextView) contentView.findViewById(R.id.uri);
                mPath = (TextView) contentView.findViewById(R.id.path);
                image =  contentView.findViewById(R.id.image);
            }
        }
    }

}
