
## Matisse
知乎Matisse的增强版，简化了使用，优化了用户体验，功能上集成了[UCrop](https://github.com/Yalantis/uCrop) + [CameraView](https://github.com/CJT2325/CameraView)。


## 功能
- 拍摄照片和视频，或者只拍摄照片，或者只拍视频
- 支持照片裁剪
- 自动申请所需所有权限，无需额外操作
- Matisse本身所有功能

# 预览
![Image](/screenshot/1.gif)
![Image](/screenshot/2.gif)
![Image](/screenshot/3.gif)


## 使用
![Download](https://api.bintray.com/packages/li-xiaojun/jrepo/matisse/images/download.svg)
```groovy
implementation 'com.lxj:matisse:最新版本'
```

## 如何使用
最简单一行代码即可调用：
```java
Matisse.from(SampleActivity.this)
    .choose(MimeType.ofAll()) //显示所有文件类型，比如图片和视频，
    .capture(true)//是否显示拍摄按钮，默认不显示
    //.capture(true, CaptureMode.All)//是否显示拍摄按钮，可以同时拍视频和图片
    .isCrop(true)//开启裁剪，默认不开启
    //.imageEngine(new GlideEngine()) //默认是Glide4.x版本的加载器，如果你用的是Glide4.x，则无需设置
    //.imageEngine(new Glide3Engine())//如果你用的是Glide3.x版本，请打开这个，Glide3Engine这个类在demo中
    .forResult(REQUEST_CODE_CHOOSE); //请求码
```
直接跳转拍摄界面：
```java
Matisse.from(SampleActivity.this)
    .jumpCapture()//直接跳拍摄，默认可以同时拍摄照片和视频
    //.jumpCapture(CaptureMode.Image)//只拍照片
    //.jumpCapture(CaptureMode.Video)//只拍视频
    .isCrop(true) //开启裁剪
    .forResult(REQUEST_CODE_CHOOSE);
```
详细设置：
```java
Matisse.from(SampleActivity.this)
    .choose(MimeType.ofAll())
    .capture(true) //默认只能拍照片
    //.capture(true, CaptureMode.All)//通过CaptureMode控制拍照照片还是视频，或者都拍
    .maxSelectable(9) //默认最大选中9张，设置为1就是单选
    .theme(R.style.Matisse_Dracula)//暗色主题
    //添加图片过滤器，比如过滤掉小于10K的图片
    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
    //设置选中图片的监听器
    .setOnSelectedListener(new OnSelectedListener() {
        @Override
        public void onSelected(
                @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
            // DO SOMETHING IMMEDIATELY HERE
            Log.e("onSelected", "onSelected: pathList=" + pathList);

        }
    })
    .originalEnable(true)//是否显示原图，默认显示
    //设置原图选中和取消选中的监听器
    .setOnCheckedListener(new OnCheckedListener() {
        @Override
        public void onCheck(boolean isChecked) {
            // DO SOMETHING IMMEDIATELY HERE
            Log.e("isChecked", "onCheck: isChecked=" + isChecked);
        }
    })
    //.imageEngine(new GlideEngine()) // 默认是Glide4.x版本的加载器，如果你用的是Glide4.x，则无需设置
    //.imageEngine(new Glide3Engine())//如果你用的是Glide3.x版本，Glide3Engine这个类在demo中
    //.imageEngine(new PicassoEngine())//如果你用的是Picasso
    .forResult(REQUEST_CODE_CHOOSE); //请求码
```

#### 接收结果

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
        //获取拍摄的图片路径，如果是录制视频则是视频的第一帧图片路径
        String captureImagePath = Matisse.obtainCaptureImageResult(data);

        //获取拍摄的视频路径
        String captureVideoPath = Matisse.obtainCaptureVideoResult(data);

        //获取裁剪结果的路径，不管是选择照片裁剪还是拍摄照片裁剪，结果都从这里取
        String cropPath = Matisse.obtainCropResult(data);

        //获取选择图片或者视频的结果路径，如果开启裁剪的话，获取的是原图的地址
        Matisse.obtainSelectUriResult(data);//uri形式的路径
        Matisse.obtainSelectPathResult(data)//文件形式路径
    }
}
```

## 混淆
如果你使用Picasso：
```pro
-dontwarn com.squareup.picasso.**
```

如果你使用Glide：
```pro
-dontwarn com.bumptech.glide.**
```
