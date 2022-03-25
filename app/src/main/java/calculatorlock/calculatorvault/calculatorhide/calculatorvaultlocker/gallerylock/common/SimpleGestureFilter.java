package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SimpleGestureFilter extends GestureDetector.SimpleOnGestureListener {
    private static final int ACTION_FAKE = -13;
    private final Activity context;
    private final GestureDetector detector;
    private final SimpleGestureListener listener;
    private int mode = 2;
    private int swipe_Max_Distance = 1000;
    private int swipe_Min_Distance = 10;
    private int swipe_Min_Velocity = 20;
    private boolean running = true;
    private boolean tapIndicator = false;

    public SimpleGestureFilter(Activity activity, SimpleGestureListener simpleGestureListener) {
        this.context = activity;
        this.detector = new GestureDetector(activity, this);
        this.listener = simpleGestureListener;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        if (this.running) {
            boolean onTouchEvent = this.detector.onTouchEvent(motionEvent);
            int i = this.mode;
            if (i == 1) {
                motionEvent.setAction(3);
            } else if (i != 2) {
            } else {
                if (motionEvent.getAction() == ACTION_FAKE) {
                    motionEvent.setAction(1);
                } else if (onTouchEvent) {
                    motionEvent.setAction(3);
                } else if (this.tapIndicator) {
                    motionEvent.setAction(0);
                    this.tapIndicator = false;
                }
            }
        }
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int i) {
        this.mode = i;
    }

    public void setEnabled(boolean z) {
        this.running = z;
    }

    public int getSwipeMaxDistance() {
        return this.swipe_Max_Distance;
    }

    public void setSwipeMaxDistance(int i) {
        this.swipe_Max_Distance = i;
    }

    public int getSwipeMinDistance() {
        return this.swipe_Min_Distance;
    }

    public void setSwipeMinDistance(int i) {
        this.swipe_Min_Distance = i;
    }

    public int getSwipeMinVelocity() {
        return this.swipe_Min_Velocity;
    }

    public void setSwipeMinVelocity(int i) {
        this.swipe_Min_Velocity = i;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float abs = Math.abs(motionEvent.getX() - motionEvent2.getX());
        float abs2 = Math.abs(motionEvent.getY() - motionEvent2.getY());
        float f3 = (float) this.swipe_Max_Distance;
        if (abs > f3 || abs2 > f3) {
            return false;
        }
        float abs3 = Math.abs(f);
        float abs4 = Math.abs(f2);
        if (abs3 <= ((float) this.swipe_Min_Velocity) || abs <= ((float) this.swipe_Min_Distance)) {
            if (abs4 <= ((float) this.swipe_Min_Velocity) || abs2 <= ((float) this.swipe_Min_Distance)) {
                return false;
            }
            if (motionEvent.getY() > motionEvent2.getY()) {
                this.listener.onSwipe(1);
            } else {
                this.listener.onSwipe(2);
            }
        } else if (motionEvent.getX() > motionEvent2.getX()) {
            this.listener.onSwipe(3);
        } else {
            this.listener.onSwipe(4);
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        this.tapIndicator = true;
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        this.listener.onDoubleTap();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (this.mode != 2) {
            return false;
        }
        motionEvent.setAction(ACTION_FAKE);
        this.context.dispatchTouchEvent(motionEvent);
        return false;
    }

    public interface SimpleGestureListener {
        void onDoubleTap();

        void onSwipe(int i);
    }
}
