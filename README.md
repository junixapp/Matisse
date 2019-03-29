
## Matisse
知乎Matisse的增强版，主要优化了用户体验，功能上集成了[UCrop](https://github.com/Yalantis/uCrop) + [CameraView](https://github.com/CJT2325/CameraView)。


## 功能
- 拍摄照片和视频
- 支持照片裁剪
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
#### Permission
The library requires two permissions:
- `android.permission.READ_EXTERNAL_STORAGE`
- `android.permission.WRITE_EXTERNAL_STORAGE`

So if you are targeting Android 6.0+, you need to handle runtime permission request before next step.

#### Simple usage snippet
------
Start `MatisseActivity` from current `Activity` or `Fragment`:

```java
Matisse.from(MainActivity.this)
        .choose(MimeType.allOf())
        .countable(true)
        .maxSelectable(9)
        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .isCrop(true)//开启裁剪，只有选择单张图片才能裁剪
        .imageEngine(new GlideEngine())
        .forResult(REQUEST_CODE_CHOOSE);
```
直接跳转拍摄界面：
```java
Matisse.from(SampleActivity.this)
        .capture()
        .isCrop(true) //是否对拍照的结果裁剪
        .forResult(REQUEST_CODE_CHOOSE);
```


#### 选择主题
There are two built-in themes you can use to start `MatisseActivity`:
- `R.style.Matisse_Zhihu` (light mode)
- `R.style.Matisse_Dracula` (dark mode)  

And Also you can define your own theme as you wish.

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

        //获取选择图片或者视频的结果路径
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
