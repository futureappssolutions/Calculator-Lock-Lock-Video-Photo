package com.calculator.vaultlocker.common;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private final Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        SurfaceHolder mSurfaceHolder = getHolder();
        this.mCamera = camera;
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(3);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            this.mCamera.setPreviewDisplay(surfaceHolder);
            Camera.Parameters parameters = this.mCamera.getParameters();
            List<Camera.Size> supportedPreviewSizes = this.mCamera.getParameters().getSupportedPreviewSizes();
            Camera.Size size = supportedPreviewSizes.get(0);
            for (int i = 1; i < supportedPreviewSizes.size(); i++) {
                if (supportedPreviewSizes.get(i).width * supportedPreviewSizes.get(i).height > size.width * size.height) {
                    size = supportedPreviewSizes.get(i);
                }
            }
            for (Integer num : parameters.getSupportedPreviewFormats()) {
                if (num == 842094169) {
                    parameters.setPreviewFormat(num);
                }
            }
            parameters.setPreviewSize(size.width, size.height);
            parameters.setPictureSize(size.width, size.height);
            this.mCamera.setParameters(parameters);
            this.mCamera.startPreview();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            if (this.mCamera != null) {
                getHolder().removeCallback(this);
                this.mCamera.stopPreview();
                this.mCamera.release();
            }
        } catch (Exception e) {
            Log.v("The Exception is:", e.toString());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        try {
            this.mCamera.setPreviewDisplay(surfaceHolder);
            this.mCamera.startPreview();
        } catch (Exception ignored) {
        }
    }
}
