package com.nhpm.DocCamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

//import com.crashlytics.android.Crashlytics;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = CameraPreview.class.getName();
    private SurfaceHolder mHolder;
    private Camera mCamera;
  //  private List<Camera.Size> previewSize = parameters.getSupportedPreviewSizes();

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(CameraPreview.TAG,"checking camera onSurfaceCreated");
        try {
            mCamera = Camera.open();
        } catch (RuntimeException e) {
           // Crashlytics.log(1,getClass().getName(),"Error in CameraPreview");
          //  Crashlytics.logException(e);
        }

        Camera.Size previewSize ;
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        previewSize = list.get(0);
        Log.d("list",(String.valueOf(list.size())) + previewSize.width +" "+previewSize.height);
        parameters.setPreviewFrameRate(20);
        parameters.setPreviewSize(previewSize.width,previewSize.height);
        if(mCamera != null) {
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch(Exception e) {
          //  Crashlytics.log(1,getClass().getName(),"Error in CameraPreview");
          //  Crashlytics.logException(e);
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(CameraPreview.TAG,"checking camera onSurfaceChanged");
        if(mHolder.getSurface() == null) {
            return;
        }
        if(mCamera != null) {
            mCamera.stopPreview();
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e ) {
          //  Crashlytics.log(1,getClass().getName(),"Error in CameraPreview");
           // Crashlytics.logException(e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(CameraPreview.TAG,"checking camera onSurfaceDestroyed");
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }
}
