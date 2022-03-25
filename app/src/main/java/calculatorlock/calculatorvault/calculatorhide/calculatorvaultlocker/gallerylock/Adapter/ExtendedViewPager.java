package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.TouchImageView;

public class ExtendedViewPager extends ViewPager {
    public ExtendedViewPager(Context context) {
        super(context);
    }

    public ExtendedViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        if (view instanceof TouchImageView) {
            return ((TouchImageView) view).canScrollHorizontallyFroyo(-i);
        }
        return super.canScroll(view, z, i, i2, i3);
    }
}
