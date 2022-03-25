package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.LoginActivity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.ActivityMain;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConfirmLockPatternViewLogin extends View {
    public static final int DEFAULT_PATTERN_GREEN_LINE_COLOR = NodeDrawable.white;
    public static final int DEFAULT_PATTERN_RED_LINE_COLOR = NodeDrawable.red;
    protected static Paint mLinePaint;
    public Context con;
    public String DecoyPattern;
    
    protected boolean mPracticeMode;
    protected boolean mTactileFeedback;
    protected boolean mDisplayingPracticeResult = false;
    protected boolean mDrawTouchExtension = false;

    protected int mCellLength;
    protected int mTouchThreshold;
    protected int mLengthNodes = 3;
    protected int mLengthPx = 100;

    protected List<Point> mPracticePattern;
    protected Set<Point> mPracticePool;
    protected List<Point> mCurrentPattern = Collections.emptyList();
    protected NodeDrawable[][] mNodeDrawables = (NodeDrawable[][]) Array.newInstance(NodeDrawable.class, 0, 0);
    protected HighlightMode mPracticeFailureMode = new FailureHighlight();
    protected HighlightMode mPracticeSuccessMode = new SuccessHighlight();
    protected Paint mEdgePaint = new Paint();
    protected Handler mHandler = new Handler();
    protected HighlightMode mHighlightMode = new NoHighlight();
    protected Point mTouchCell = new Point(-1, -1);
    protected Point mTouchPoint = new Point(-1, -1);
    protected Vibrator mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

    public ConfirmLockPatternViewLogin(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        con = context;
        mEdgePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
        mEdgePaint.setStrokeCap(Paint.Cap.ROUND);
        mEdgePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint();
        mLinePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setAlpha(100);
        mLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        DecoyPattern = SecurityLocksSharedPreferences.GetObject(con).GetDecoySecurityCredential();
    }

    private void buildDrawables() {
        mNodeDrawables = (NodeDrawable[][]) Array.newInstance(NodeDrawable.class, mLengthNodes, mLengthNodes);
        mCellLength = mLengthPx / mLengthNodes;
        float f = ((float) mCellLength) * 0.9f;
        mEdgePaint.setStrokeWidth(0.33f * f);
        mTouchThreshold = (int) (f / 2.0f);
        int i3 = mCellLength / 2;
        long currentTimeMillis = System.currentTimeMillis();
        for (int i4 = 0; i4 < mLengthNodes; i4++) {
            for (int i5 = 0; i5 < mLengthNodes; i5++) {
                if (System.currentTimeMillis() - currentTimeMillis >= 30) {
                    PatternActivityMethods.clearAndBail(getContext());
                }
                int i6 = mCellLength;
                mNodeDrawables[i5][i4] = new NodeDrawable(f, new Point((i5 * i6) + i3, (i6 * i4) + i3), con);
            }
        }
        if (!mPracticeMode) {
            loadPattern(mCurrentPattern, mHighlightMode);
        }
    }

    private void clearPattern(List<Point> list) {
        for (Point point : list) {
            mNodeDrawables[point.x][point.y].setNodeState(0);
            mLinePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
            mLinePaint.setAlpha(100);
        }
    }

    private void loadPattern(List<Point> list, HighlightMode highlightMode) {
        for (int i = 0; i < list.size(); i++) {
            Point point = list.get(i);
            NodeDrawable nodeDrawable = mNodeDrawables[point.x][point.y];
            nodeDrawable.setNodeState(highlightMode.select(nodeDrawable, i, list.size(), point.x, point.y, mLengthNodes));
            if (i < list.size() - 1) {
                Point point2 = list.get(i + 1);
                Point center = mNodeDrawables[point.x][point.y].getCenter();
                Point center2 = mNodeDrawables[point2.x][point2.y].getCenter();
                mNodeDrawables[point.x][point.y].setExitAngle((float) Math.atan2(center.y - center2.y, center.x - center2.x));
            }
        }
    }

    private void appendPattern(List<Point> list, Point point) {
        NodeDrawable nodeDrawable = mNodeDrawables[point.x][point.y];
        nodeDrawable.setNodeState(1);
        if (list.size() > 0) {
            Point point2 = list.get(list.size() - 1);
            NodeDrawable nodeDrawable2 = mNodeDrawables[point2.x][point2.y];
            Point center = nodeDrawable2.getCenter();
            Point center2 = nodeDrawable.getCenter();
            nodeDrawable2.setExitAngle((float) Math.atan2(center.y - center2.y, center.x - center2.x));
        }
        list.add(point);
    }

    private void testPracticePattern() {
        mDisplayingPracticeResult = true;
        if (PatternActivityMethods.ConvertPatternToNo(mPracticePattern).equals(SecurityLocksCommon.PatternPassword)) {
            mPracticeFailureMode = mPracticeSuccessMode;
        } else if (PatternActivityMethods.ConvertPatternToNo(mPracticePattern).equals(DecoyPattern) && !SecurityLocksCommon.IsConfirmPatternActivity) {
            mPracticeFailureMode = mPracticeSuccessMode;
        }
        loadPattern(mPracticePattern, mPracticeFailureMode);
        mHandler.postDelayed(() -> {
            if (mDisplayingPracticeResult) {
                testPasswordPattern(PatternActivityMethods.ConvertPatternToNo(mPracticePattern));
                resetPractice();
                invalidate();
            }
        }, 30);
    }

    public void testPasswordPattern(String str) {
        SecurityLocksSharedPreferences GetObject = SecurityLocksSharedPreferences.GetObject(con);
        if (str.equals(SecurityLocksCommon.PatternPassword)) {
            SecurityLocksCommon.IsFakeAccount = 0;
            if (SecurityLocksCommon.IsConfirmPatternActivity) {
                SecurityLocksCommon.IsAppDeactive = false;
                SecurityLocksCommon.isBackupPattern = false;
                SecurityLocksCommon.isSettingDecoy = false;
                SecurityLocksCommon.IsConfirmPatternActivity = false;
                ((Activity) getContext()).finish();
            } else if (SecurityLocksCommon.isSettingDecoy) {
                SecurityLocksCommon.IsAppDeactive = false;
                SecurityLocksCommon.isSettingDecoy = false;
                SecurityLocksCommon.IsConfirmPatternActivity = false;
                SecurityLocksCommon.isBackupPattern = false;
                Intent intent = new Intent(con, SetPatternActivity.class);
                intent.putExtra("isSettingDecoy", true);
                con.startActivity(intent);
                ((Activity) getContext()).finish();
            } else if (!SecurityLocksCommon.isBackupPattern) {
                Common.loginCount = GetObject.GetRateCount();
                Common.loginCount++;
                GetObject.SetRateCount(Common.loginCount);
                SecurityLocksCommon.IsFakeAccount = 0;
                SecurityLocksCommon.IsnewloginforAd = true;
                SecurityLocksCommon.IsAppDeactive = false;
                con.startActivity(new Intent(con, ActivityMain.class));
                ((Activity) getContext()).finish();
            }
        } else if (!str.equals(DecoyPattern)) {
            if (SecurityLocksCommon.IsConfirmPatternActivity) {
                loadPattern(mPracticePattern, mPracticeFailureMode);
                ConfirmPatternActivity.lblConfirmpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Tryagain);
            } else {
                loadPattern(mPracticePattern, mPracticeFailureMode);
                if (!SecurityLocksCommon.IsStealthModeOn) {
                    LoginActivity loginActivity = new LoginActivity();
                    loginActivity.getClass();
                    loginActivity.HackAttempt();
                    LoginActivity.wrongPassword = str;
                    LoginActivity.txt_wrong_pttern.setVisibility(View.VISIBLE);
                    LoginActivity.txt_wrong_pttern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Tryagain);
                }
            }
        } else if (SecurityLocksCommon.IsConfirmPatternActivity) {
            loadPattern(mPracticePattern, mPracticeFailureMode);
            ConfirmPatternActivity.lblConfirmpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Tryagain);
        } else if (!DecoyPattern.trim().isEmpty() && DecoyPattern.trim().length() != 0) {
            SecurityLocksCommon.IsAppDeactive = false;
            SecurityLocksCommon.IsFakeAccount = 1;
            con.startActivity(new Intent(con, ActivityMain.class));
            ((Activity) getContext()).finish();
        }
    }

    public void resetPractice() {
        clearPattern(mPracticePattern);
        mPracticePattern.clear();
        mPracticePool.clear();
        mDisplayingPracticeResult = false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mPracticeMode) {
            mCurrentPattern = mPracticePattern;
        }
        @SuppressLint("DrawAllocation") CenterIterator centerIterator = new CenterIterator(mCurrentPattern.iterator());
        if (centerIterator.hasNext()) {
            mLinePaint.setStrokeWidth(9.0f);
            mLinePaint.setAlpha(100);
            Point next = centerIterator.next();
            while (centerIterator.hasNext()) {
                Point next2 = centerIterator.next();
                canvas.drawLine((float) next.x, (float) next.y, (float) next2.x, (float) next2.y, mLinePaint);
                next = next2;
            }
            if (mDrawTouchExtension) {
                canvas.drawLine((float) next.x, (float) next.y, (float) mTouchPoint.x, (float) mTouchPoint.y, mLinePaint);
            }
        }
        for (int i = 0; i < mLengthNodes; i++) {
            for (int i2 = 0; i2 < mLengthNodes; i2++) {
                mNodeDrawables[i2][i].draw(canvas);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!mPracticeMode) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            if (mDisplayingPracticeResult) {
                resetPractice();
            }
            mDrawTouchExtension = true;
        } else if (action == 1) {
            mDrawTouchExtension = false;
            testPracticePattern();
        } else if (action != 2) {
            return super.onTouchEvent(motionEvent);
        }
        if (!SecurityLocksCommon.IsStealthModeOn) {
            try {
                LoginActivity.txt_wrong_pttern.setVisibility(View.INVISIBLE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Point point = mTouchPoint;
        int i = (int) x;
        point.x = i;
        int i2 = (int) y;
        point.y = i2;
        Point point2 = mTouchCell;
        int i3 = mCellLength;
        point2.x = i / i3;
        point2.y = i2 / i3;
        if (point2.x >= 0 && mTouchCell.x < mLengthNodes && mTouchCell.y >= 0 && mTouchCell.y < mLengthNodes) {
            Point center = mNodeDrawables[mTouchCell.x][mTouchCell.y].getCenter();
            if (((int) Math.sqrt(Math.pow(x - ((float) center.x), 2.0d) + Math.pow(y - ((float) center.y), 2.0d))) < mTouchThreshold && !mPracticePool.contains(mTouchCell)) {
                if (mTactileFeedback) {
                    mVibrator.vibrate(35);
                }
                Point point3 = new Point(mTouchCell);
                appendPattern(mPracticePattern, point3);
                mPracticePool.add(point3);
            }
        }
        invalidate();
        return true;
    }

    @Override
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode2 = View.MeasureSpec.getMode(i2);
        if (mode == 0 && mode2 == 0) {
            setMeasuredDimension(100, 100);
            size = 100;
        } else if (mode == 0) {
            size = size2;
        } else if (mode2 != 0) {
            size = Math.min(size, size2);
        }
        setMeasuredDimension(size, size);
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        mLengthPx = Math.min(i, i2);
        buildDrawables();
        if (!mPracticeMode) {
            loadPattern(mCurrentPattern, mHighlightMode);
        }
    }

    public List<Point> getPattern() {
        return mCurrentPattern;
    }

    public void setPattern(List<Point> list) {
        clearPattern(mCurrentPattern);
        loadPattern(list, mHighlightMode);
        mCurrentPattern = list;
    }

    public int getGridLength() {
        return mLengthNodes;
    }

    public void setGridLength(int i) {
        mLengthNodes = i;
        mCurrentPattern = Collections.emptyList();
        buildDrawables();
    }

    public void setHighlightMode(HighlightMode highlightMode, boolean z) {
        mHighlightMode = highlightMode;
        if (!z) {
            loadPattern(mCurrentPattern, highlightMode);
        }
    }

    public HighlightMode getHighlightMode() {
        return mHighlightMode;
    }

    public void setHighlightMode(HighlightMode highlightMode) {
        setHighlightMode(highlightMode, mPracticeMode);
    }

    public boolean getPracticeMode() {
        return mPracticeMode;
    }

    public void setPracticeMode(boolean z) {
        mDisplayingPracticeResult = false;
        mPracticeMode = z;
        if (z) {
            mPracticePattern = new ArrayList<>();
            mPracticePool = new HashSet<>();
            clearPattern(mCurrentPattern);
            return;
        }
        clearPattern(mPracticePattern);
        loadPattern(mCurrentPattern, mHighlightMode);
    }

    public boolean getTactileFeedbackEnabled() {
        return mTactileFeedback;
    }

    public void setTactileFeedbackEnabled(boolean z) {
        mTactileFeedback = z;
    }


    public interface HighlightMode {
        int select(NodeDrawable nodeDrawable, int i, int i2, int i3, int i4, int i5);
    }

    public static class FirstHighlight implements HighlightMode {
        @Override
        public int select(NodeDrawable nodeDrawable, int i, int i2, int i3, int i4, int i5) {
            return i == 0 ? 2 : 1;
        }
    }

    public static class NoHighlight implements HighlightMode {
        @Override
        public int select(NodeDrawable nodeDrawable, int i, int i2, int i3, int i4, int i5) {
            return 1;
        }
    }

    public static class FailureHighlight implements HighlightMode {
        @Override
        public int select(NodeDrawable nodeDrawable, int i, int i2, int i3, int i4, int i5) {
            ConfirmLockPatternViewLogin.mLinePaint.setColor(ConfirmLockPatternViewLogin.DEFAULT_PATTERN_RED_LINE_COLOR);
            ConfirmLockPatternViewLogin.mLinePaint.setAlpha(100);
            return 4;
        }
    }

    public static class RainbowHighlight implements HighlightMode {
        @Override
        public int select(NodeDrawable nodeDrawable, int i, int i2, int i3, int i4, int i5) {
            nodeDrawable.setCustomColor(Color.HSVToColor(new float[]{(((float) i) / ((float) i2)) * 360.0f, 1.0f, 1.0f}));
            return 5;
        }
    }

    public static class SuccessHighlight implements HighlightMode {
        @Override
        public int select(NodeDrawable nodeDrawable, int i, int i2, int i3, int i4, int i5) {
            ConfirmLockPatternViewLogin.mLinePaint.setColor(ConfirmLockPatternViewLogin.DEFAULT_PATTERN_GREEN_LINE_COLOR);
            ConfirmLockPatternViewLogin.mLinePaint.setAlpha(100);
            return 3;
        }
    }

    private class CenterIterator implements Iterator<Point> {
        private final Iterator<Point> nodeIterator;

        public CenterIterator(Iterator<Point> it) {
            nodeIterator = it;
        }

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public Point next() {
            return mNodeDrawables[nodeIterator.next().x][nodeIterator.next().y].getCenter();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
