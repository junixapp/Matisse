package com.lxj.matisse;

/**
 * Description:
 * Create by dance, at 2019/3/27
 */
public interface MatisseConst {
    int REQUEST_CODE_PREVIEW = 23; //预览
    int REQUEST_CODE_CAPTURE = 24; //拍照或录制

    int REQUEST_CAPTURE_PERMISSION = 100; //拍照权限请求

    String EXTRA_RESULT_SELECTION = "extra_result_selection";
    String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";
    String EXTRA_RESULT_CAPTURE_IMAGE_PATH = "extra_result_capture_image_path";//拍照或者录制的第一帧
    String EXTRA_RESULT_CAPTURE_VIDEO_PATH = "extra_result_capture_video_path";//视频路径
    String EXTRA_RESULT_CROP_PATH = "extra_result_capture_crop_path";//裁剪结果的路径
    String EXTRA_RESULT_ORIGINAL_ENABLE = "extra_result_original_enable";
}
