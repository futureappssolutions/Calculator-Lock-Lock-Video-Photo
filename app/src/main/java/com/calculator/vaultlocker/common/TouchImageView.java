package com.calculator.vaultlocker.common;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class TouchImageView extends AppCompatImageView {
    protected float origHeight;
    protected float origWidth;
    Context context;
    float[] m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    int oldMeasuredHeight;
    int oldMeasuredWidth;
    int viewHeight;
    int viewWidth;
    int IsClick = 0;
    PointF last = new PointF();
    float maxScale = 3.0f;
    float minScale = 1.0f;
    int mode = 0;
    float mzoomScaleFactor = 0.0f;
    float saveScale = 1.0f;
    PointF start = new PointF();

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sharedConstructing(context);
    }

    public float getFixDragTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            return 0.0f;
        }
        return f;
    }

    public float getFixTrans(float f, float f2, float f3) {
        float f4;
        float f5;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        Matrix matrix = new Matrix();
        this.matrix = matrix;
        this.m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ImageView.ScaleType.MATRIX);

        setOnClickListener(view -> {
            float f = 0;
            TouchImageView.this.IsClick++;
            if (TouchImageView.this.IsClick == 2) {
                TouchImageView touchImageView = TouchImageView.this;
                touchImageView.IsClick = 0;
                touchImageView.mode = 2;
                if (touchImageView.mzoomScaleFactor <= 6.0f) {
                    TouchImageView.this.mzoomScaleFactor = 7.0f;
                } else {
                    TouchImageView.this.mzoomScaleFactor = 0.1f;
                }
                float f2 = TouchImageView.this.mzoomScaleFactor;
                float f3 = TouchImageView.this.saveScale;
                TouchImageView.this.saveScale *= f2;
                if (TouchImageView.this.saveScale > TouchImageView.this.maxScale) {
                    TouchImageView touchImageView2 = TouchImageView.this;
                    touchImageView2.saveScale = touchImageView2.maxScale;
                    f = TouchImageView.this.maxScale;
                } else {
                    if (TouchImageView.this.saveScale < TouchImageView.this.minScale) {
                        TouchImageView touchImageView3 = TouchImageView.this;
                        touchImageView3.saveScale = touchImageView3.minScale;
                        f = TouchImageView.this.minScale;
                    }
                    if (TouchImageView.this.origWidth * TouchImageView.this.saveScale > ((float) TouchImageView.this.viewWidth) || TouchImageView.this.origHeight * TouchImageView.this.saveScale <= ((float) TouchImageView.this.viewHeight)) {
                        TouchImageView.this.matrix.postScale(f2, f2, (float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
                    } else {
                        TouchImageView.this.matrix.postScale(f2, f2, TouchImageView.this.last.x, TouchImageView.this.last.y);
                    }
                    TouchImageView.this.fixTrans();
                }
                f2 = f / f3;
                if (TouchImageView.this.origWidth * TouchImageView.this.saveScale > ((float) TouchImageView.this.viewWidth)) {
                }
                TouchImageView.this.matrix.postScale(f2, f2, (float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
                TouchImageView.this.fixTrans();
            }
        });

        setOnTouchListener((view, motionEvent) -> {
            TouchImageView.this.mScaleDetector.onTouchEvent(motionEvent);
            PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
            int action = motionEvent.getAction();
            if (action == 6) {
                TouchImageView.this.mode = 0;
            } else if (action == 0) {
                TouchImageView.this.last.set(pointF);
                TouchImageView.this.start.set(TouchImageView.this.last);
                TouchImageView.this.mode = 1;
            } else if (action != 1) {
                if (action != 2) {
                    if (action == 3) {
                        TouchImageView.this.mode = 0;
                        int abs = (int) Math.abs(pointF.x - TouchImageView.this.start.x);
                        int abs2 = (int) Math.abs(pointF.y - TouchImageView.this.start.y);
                        if (abs < 3 && abs2 < 3) {
                            TouchImageView.this.performClick();
                        }
                    }
                }
                if (TouchImageView.this.mode == 1) {
                    float f = pointF.x - TouchImageView.this.last.x;
                    float f2 = pointF.y - TouchImageView.this.last.y;
                    TouchImageView touchImageView = TouchImageView.this;
                    float fixDragTrans = touchImageView.getFixDragTrans(f, (float) touchImageView.viewWidth, TouchImageView.this.origWidth * TouchImageView.this.saveScale);
                    TouchImageView touchImageView2 = TouchImageView.this;
                    touchImageView2.matrix.postTranslate(fixDragTrans, touchImageView2.getFixDragTrans(f2, (float) touchImageView2.viewHeight, TouchImageView.this.origHeight * TouchImageView.this.saveScale));
                    TouchImageView.this.fixTrans();
                    TouchImageView.this.last.set(pointF.x, pointF.y);
                }
            } else {
                TouchImageView.this.mode = 0;
                int abs3 = (int) Math.abs(pointF.x - TouchImageView.this.start.x);
                int abs4 = (int) Math.abs(pointF.y - TouchImageView.this.start.y);
                if (abs3 < 3 && abs4 < 3) {
                    TouchImageView.this.performClick();
                }
            }
            TouchImageView touchImageView3 = TouchImageView.this;
            touchImageView3.setImageMatrix(touchImageView3.matrix);
            TouchImageView.this.invalidate();
            return true;
        });
    }

    private float getImageWidth() {
        return ((float) this.oldMeasuredWidth) * this.saveScale;
    }

    private float getImageHeight() {
        return ((float) this.oldMeasuredHeight) * this.saveScale;
    }

    public boolean canScrollHorizontallyFroyo(int i) {
        return canScrollHorizontally(i);
    }

    @Override
    public boolean canScrollHorizontally(int i) {
        this.matrix.getValues(this.m);
        float f = this.m[2];
        if (getImageWidth() < ((float) this.viewWidth)) {
            return false;
        }
        if (f >= -1.0f && i < 0) {
            return false;
        }
        return Math.abs(f) + ((float) this.viewWidth) + 1.0f < getImageWidth() || i <= 0;
    }

    public void setMaxZoom(float f) {
        this.maxScale = f;
    }

    public void fixTrans() {
        this.matrix.getValues(this.m);
        float[] fArr = this.m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, this.origWidth * this.saveScale);
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, this.origHeight * this.saveScale);
        if (fixTrans != 0.0f || fixTrans2 != 0.0f) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
    }

    @Override
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        super.onMeasure(i, i2);
        this.viewWidth = View.MeasureSpec.getSize(i);
        int size = View.MeasureSpec.getSize(i2);
        this.viewHeight = size;
        int i5 = this.oldMeasuredHeight;
        if ((i5 != this.viewWidth || i5 != size) && (i3 = this.viewWidth) != 0 && (i4 = this.viewHeight) != 0) {
            this.oldMeasuredHeight = i4;
            this.oldMeasuredWidth = i3;
            if (this.saveScale == 1.0f) {
                Drawable drawable = getDrawable();
                if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
                    int intrinsicWidth = drawable.getIntrinsicWidth();
                    int intrinsicHeight = drawable.getIntrinsicHeight();
                    Log.d("bmSize", "bmWidth: " + intrinsicWidth + " bmHeight : " + intrinsicHeight);
                    float f = (float) intrinsicWidth;
                    float f2 = (float) intrinsicHeight;
                    float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / f2);
                    this.matrix.setScale(min, min);
                    float f3 = (((float) this.viewHeight) - (f2 * min)) / 2.0f;
                    float f4 = (((float) this.viewWidth) - (min * f)) / 2.0f;
                    this.matrix.postTranslate(f4, f3);
                    this.origWidth = ((float) this.viewWidth) - (f4 * 2.0f);
                    this.origHeight = ((float) this.viewHeight) - (f3 * 2.0f);
                    setImageMatrix(this.matrix);
                } else {
                    return;
                }
            }
            fixTrans();
        }
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.mode = 2;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float f;
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f2 = TouchImageView.this.saveScale;
            TouchImageView.this.saveScale *= scaleFactor;
            if (TouchImageView.this.saveScale > TouchImageView.this.maxScale) {
                TouchImageView touchImageView = TouchImageView.this;
                touchImageView.saveScale = touchImageView.maxScale;
                f = TouchImageView.this.maxScale;
            } else {
                if (TouchImageView.this.saveScale < TouchImageView.this.minScale) {
                    TouchImageView touchImageView2 = TouchImageView.this;
                    touchImageView2.saveScale = touchImageView2.minScale;
                    f = TouchImageView.this.minScale;
                }
                if (TouchImageView.this.origWidth * TouchImageView.this.saveScale > ((float) TouchImageView.this.viewWidth) || TouchImageView.this.origHeight * TouchImageView.this.saveScale <= ((float) TouchImageView.this.viewHeight)) {
                    TouchImageView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
                } else {
                    TouchImageView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                }
                TouchImageView.this.fixTrans();
                return true;
            }
            scaleFactor = f / f2;
            TouchImageView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
            TouchImageView.this.fixTrans();
            return true;
        }
    }
}
