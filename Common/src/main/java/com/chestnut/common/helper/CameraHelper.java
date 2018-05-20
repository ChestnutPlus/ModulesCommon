package com.chestnut.common.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/2/22 14:22
 *     desc  :  对系统相机进行封装
 *     thanks To:
 *              1.  http://blog.csdn.net/qiguangyaolove/article/details/53018504
 *              2.  http://blog.csdn.net/shimo_1011/article/details/56673760
 *              3.  http://blog.csdn.net/super_zq/article/details/52637064
 *     dependent on:
 *     update log:
 *              2018年5月20日
 *                  1. 对自动对焦crash进行处理
 *                  2. 增加多线程锁
 * </pre>
 */
public class CameraHelper {

    private Camera camera;
    private boolean isStartPreview = false;
    private Disposable disposableTimer;

    /**
     * 初始化
     * @param surfaceView surfaceView
     * @param orientation 方向
     * @param cameraId 摄像头Id
     */
    public void init(SurfaceView surfaceView, int orientation, int cameraId) {
        Observable.just(orientation)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(integer -> {
                    //锁住，避免并发初始化带来不可预知的错误
                    synchronized (CameraHelper.this) {
                        SurfaceHolder surfaceHolder = surfaceView.getHolder();
                        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                                try {
                                    if (cameraId!=-1) {
                                        try {
                                            camera = Camera.open(cameraId);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            camera = null;
                                        }
                                    }
                                    if (camera==null)
                                        camera = Camera.open(0);
                                    if (camera == null)
                                        camera = Camera.open(1);
                                    camera.setPreviewDisplay(surfaceHolder);
                                } catch (Exception e) {
                                    if (null != camera) {
                                        camera.release();
                                        camera = null;
                                    }
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                                initCamera(orientation,surfaceView);
                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                                if (null != camera) {
                                    camera.stopPreview();
                                    camera.release();
                                    camera = null;
                                }
                            }
                        });
                    }
                    return integer;
                }).subscribe();
    }

    public void init(SurfaceView surfaceView) {
        init(surfaceView,-1,-1);
    }

    public void init(SurfaceView surfaceView, int orientation) {
        init(surfaceView,orientation,-1);
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        synchronized (CameraHelper.this) {
            isStartPreview = true;
            if (camera!=null)
                camera.startPreview();
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        synchronized (CameraHelper.this) {
            isStartPreview = false;
            if (camera != null)
                camera.stopPreview();
        }
    }

    /**
     * 主动设置对焦
     * 只对焦一次
     */
    public void setAutoFocus() {
        synchronized (CameraHelper.this) {
            if (camera != null && disposableTimer != null && !disposableTimer.isDisposed()) {
                camera.autoFocus((b, camera) -> {
                    if (b) {
                        Log.i("CameraHelper", "setAutoFocusMoveCallback: success...");
                    } else {
                        Log.i("CameraHelper", "setAutoFocusMoveCallback: fail...");
                    }
                });
            }
        }
    }

    /**
     * 主动设置对焦
     *  每隔 timeMs 毫秒
     * @param timeMs 毫秒
     */
    public void setAutoFocus(int timeMs) {
        if (disposableTimer!=null && !disposableTimer.isDisposed())
            disposableTimer.dispose();
        disposableTimer = Observable.interval(timeMs, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(aLong -> setAutoFocus(), throwable -> {
                    Log.i("CameraHelper", "setAutoFocus, err: "+throwable.getMessage());
                });
    }

    /**
     * 拍照并保存
     */
    public void tackPic(String filePath) {
        if (camera!=null)
            camera.takePicture(null, null, (data, camera) -> {
                //将字节数组
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                //输出流保存数据
                try {
                    FileOutputStream fileOutputStream=new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
    }

    public Observable<byte[]> tackPicGetBytes() {
        if (camera!=null)
            return Observable.create(e -> {
                camera.takePicture(null, null, (data, camera) -> {
                    e.onNext(data);
                    e.onComplete();
                });
            });
        else
            return Observable.error(new Throwable("camera null"));
    }

    public Observable<Bitmap> tackPicGetBitmap() {
        if (camera!=null)
            return Observable.create(e -> {
                camera.takePicture(null, null, (data, camera) -> {
                    e.onNext(BitmapFactory.decodeByteArray(data,0,data.length));
                    e.onComplete();
                });
            });
        else
            return Observable.error(new Throwable("camera null"));
    }

    /**
     * 释放资源
     */
    public void release() {
        synchronized (CameraHelper.this) {
            if (disposableTimer!=null && !disposableTimer.isDisposed())
                disposableTimer.dispose();
            disposableTimer = null;
            if (null != camera) {
                camera.setFaceDetectionListener(null);
                camera.setZoomChangeListener(null);
                camera.setAutoFocusMoveCallback(null);
                camera.setErrorCallback(null);
                camera.setPreviewCallbackWithBuffer(null);
                camera.setOneShotPreviewCallback(null);
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    }

    /**
     * 初始化摄像头
     * @param orientation 方向
     */
    private void initCamera(int orientation, SurfaceView surfaceView) {
        if (camera!=null) {
            camera.setParameters(setOptimalPreviewSize(camera, surfaceView));
            if (orientation!=-1)
                camera.setDisplayOrientation(orientation);//将预览旋转90度
            if (isStartPreview)
                startPreview();
        }
    }

    /**
     * 获取设备摄像头最合适的大小
     * @param camera 摄像头
     * @param surfaceView surfaceView
     * @return size
     */
    private Camera.Parameters setOptimalPreviewSize(Camera camera, SurfaceView surfaceView) {
        Camera.Parameters parameters = camera.getParameters();//获取camera的parameter实例
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
        int w = surfaceView.getWidth();
        int h = surfaceView.getHeight();

        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }

        if (optimalSize!=null) {
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);//把camera.size赋值到parameters
            camera.setParameters(parameters);//把parameters设置给camera
        }

        return parameters;
    }
}
