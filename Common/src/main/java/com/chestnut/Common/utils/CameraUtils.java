package com.chestnut.Common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月5日
 *     desc  : 相机相关工具类
 *     thanks To:
 *     dependent on:
 *          EmptyUtils
 *          StringUtils
 *          ImageUtils
 * </pre>
 */
public class CameraUtils {

    private CameraUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取打开照程序界面的Intent
     *@param photoSavePath 保存的路径
     *@return 返回Intent
     */
    public static Intent getOpenCameraIntent(String photoSavePath) {
        if (EmptyUtils.isEmpty(photoSavePath))
            photoSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        CameraPhotoSavePath = photoSavePath;
        Uri uri = Uri.fromFile(new File(photoSavePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 获取跳转至相册选择界面的Intent
     * @return 返回Intent
     */
    public static Intent getImagePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        return intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    }

    public static Intent getCropImageIntent(int outputX, int outputY, Uri fromFileURI,
                                            Uri saveFileURI) {
        return getCropImageIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI);
    }

    public static Intent getCropImageIntent(int outputX, int outputY, String fromFileString,
                                            String saveFileString) {
        return getCropImageIntent(1, 1, outputX, outputY, true, fromFileString, saveFileString);
    }

    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, Uri fromFileURI,
                                            Uri saveFileURI) {
        return getCropImageIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI);
    }

    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, String fromFileString,
                                            String saveFileString) {
        return getCropImageIntent(aspectX, aspectY, outputX, outputY, true, fromFileString, saveFileString);
    }

    /**
     * 获取[跳转至裁剪界面,默认可缩放]的Intent
     * @param aspectX     裁剪框尺寸比例X
     * @param aspectY     裁剪框尺寸比例Y
     * @param outputX     输出尺寸宽度
     * @param outputY     输出尺寸高度
     * @param canScale    是否可缩放
     * @param fromFileURI 文件来源路径URI
     * @param saveFileURI 输出文件路径URI
     * @return Intent
     */
    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
                                            Uri fromFileURI, Uri saveFileURI) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fromFileURI, "image/*");
        intent.putExtra("crop", "true");
        // X方向上的比例
        intent.putExtra("aspectX", aspectX <= 0 ? 1 : aspectX);
        // Y方向上的比例
        intent.putExtra("aspectY", aspectY <= 0 ? 1 : aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", canScale);
        // 图片剪裁不足黑边解决
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", true);
        // 需要将读取的文件路径和裁剪写入的路径区分，否则会造成文件0byte
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
        // true-->返回数据类型可以设置为Bitmap，但是不能传输太大，截大图用URI，小图用Bitmap或者全部使用URI
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

    /**
     * 获取[跳转至裁剪界面,默认可缩放]的Intent
     * @param aspectX     裁剪框尺寸比例X
     * @param aspectY     裁剪框尺寸比例Y
     * @param outputX     输出尺寸宽度
     * @param outputY     输出尺寸高度
     * @param canScale    是否可缩放
     * @param fromFileString 文件来源路径String
     * @param saveFileString 输出文件路径String
     * @return Intent
     */
    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
                                            String fromFileString, String saveFileString) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(fromFileString)), "image/*");
        intent.putExtra("crop", "true");
        // X方向上的比例
        intent.putExtra("aspectX", aspectX <= 0 ? 1 : aspectX);
        // Y方向上的比例
        intent.putExtra("aspectY", aspectY <= 0 ? 1 : aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", canScale);
        // 图片剪裁不足黑边解决
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        // 需要将读取的文件路径和裁剪写入的路径区分，否则会造成文件0byte
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(saveFileString)));
        // true-->返回数据类型可以设置为Bitmap，但是不能传输太大，截大图用URI，小图用Bitmap或者全部使用URI
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

    /**
     * 获得选中相册的图片
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     *                要先选择图片，startActivityForResult(CameraUtils.getImagePickerIntent(),request);
     * @return bitmap
     */
    public static Bitmap getChosenImageBitmap(Activity context, Intent data) {
        if (data == null) return null;
        Bitmap bm = null;
        ContentResolver cr = context.getContentResolver();
        Uri originalUri = data.getData();
        try {
            bm = MediaStore.Images.Media.getBitmap(cr, originalUri);
        } catch (IOException e) {
            ExceptionCatchUtils.catchE(e,"CameraUtils");
        }
        return bm;
    }

    /**
     * 获得选中相册的图片路径
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     *                要先选择图片，startActivityForResult(CameraUtils.getImagePickerIntent(),request);
     * @return String
     */
    public static String getChosenImagePath(Activity context, Intent data) {
        if (data == null) {
            return null;
        }
        String path = "";
        ContentResolver resolver = context.getContentResolver();
        Uri originalUri = data.getData();
        if (null == originalUri) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = resolver.query(originalUri, projection, null, null, null);
        if (null != cursor) {
            try {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            } catch (IllegalArgumentException e) {
                ExceptionCatchUtils.catchE(e,"CameraUtils");
            }finally {
                try {
                    if (!cursor.isClosed()) {
                        cursor.close();
                    }
                } catch (Exception e) {
                    ExceptionCatchUtils.catchE(e,"CameraUtils");
                }
            }
        }
        return StringUtils.isEmpty(path) ? originalUri.getPath() : path;
    }

    /**
     *  返回相机拍摄的照片的存储地址
     *  建议在，onActivityResult 中获取
     * @return
     */
    public static String getCameraPhotoSavePath() {
        return CameraPhotoSavePath;
    }

    /**
     *  获取相机拍摄的照片的 Bitmap
     *  建议在，onActivityResult 中获取
     * @return
     */
    public static Bitmap getCameraPhotoBitmap() {
        return EmptyUtils.isEmpty(CameraPhotoSavePath) ? null : ImageUtils.getBitmap(new File(CameraPhotoSavePath));
    }


    private static final int RESULT_OK = -1;
    public static final int PHOTO_REQUEST_TAKE_PHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 裁剪结果
    public static String CameraPhotoSavePath = null;    //存储相机的照片地址

    /**
     *      对本 Utils 的进一步封装，
     *      选择图片，裁剪。
     *      1.  在 onActivityResult 中调：
     *          Bitmap bitmap = CameraUtils.getBitmapFromCG(this,requestCode,0,data,1,1,300,300,true,this.getCacheDir()+"/cutHeadPhotoTemp.jpg");
     *      2.  在任意地方调用：    getHeadCropPhotoFromCamera（）
     *
     * @param activity  调用者
     * @param cameraTackSavePath 保存的地方
     */
    public static void getHeadCropPhotoFromCamera(Activity activity, String cameraTackSavePath) {
        activity.startActivityForResult(getOpenCameraIntent(cameraTackSavePath), PHOTO_REQUEST_TAKE_PHOTO);
    }

    /**
     *     对本 Utils 的进一步封装。
     *     1.  在 onActivityResult 中调：
     *          Bitmap bitmap = CameraUtils.getBitmapFromCG(this,requestCode,0,data,1,1,300,300,true,this.getCacheDir()+"/cutHeadPhotoTemp.jpg");
     *      2.  在任意地方调用：    getHeadCropPhotoFromGallery（）
     *
     * @param activity  调用者
     */
    public static void getHeadCropPhotoFromGallery(Activity activity) {
        activity.startActivityForResult(getImagePickerIntent(), PHOTO_REQUEST_GALLERY);
    }

    /**
     *      onActivityResult 中返回结果
     */
    public static Bitmap getBitmapFromCG(
            Activity activity, int requestCode, int resultCode, Intent data,
            int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
            String corpPhotoSaveFileString) {
        Bitmap bitmap = null;
        switch (requestCode) {
            // 如果是拍照
            case PHOTO_REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 没有指定特定存储路径的时候，data不为null
                    if (data != null) {
                        if (data.getData() != null) {
                            startPhotoZoom(activity,aspectX,aspectY,outputX,outputY,canScale,data.getData(),Uri.fromFile(new File(corpPhotoSaveFileString)));
                        }
                    }
                    else {
                        startPhotoZoom(activity,aspectX,aspectY,outputX,outputY,canScale,Uri.fromFile(new File(CameraPhotoSavePath)),Uri.fromFile(new File(corpPhotoSaveFileString)));
                    }
                }
                break;
            // 如果是从相册选取
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    if (data.getData() != null) {
                        startPhotoZoom(activity,aspectX,aspectY,outputX,outputY,canScale,data.getData(),Uri.fromFile(new File(corpPhotoSaveFileString)));
                    }
                }
                break;
            //如果是裁剪完成
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = bundle.getParcelable("data");
                    }
                }
                break;
        }
        return bitmap;
    }

    /**
     *      开始裁剪
     */
    private static void startPhotoZoom(Activity activity, int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
                                       Uri fromFileURI, Uri saveFileURI) {
        activity.startActivityForResult(getCropImageIntent(
                aspectX,
                aspectY,
                outputX,
                outputY,
                canScale,
                fromFileURI,
                saveFileURI
        ), PHOTO_REQUEST_CUT);
    }

}
