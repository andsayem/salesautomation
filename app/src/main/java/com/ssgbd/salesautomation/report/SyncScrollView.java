package com.ssgbd.salesautomation.report;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class SyncScrollView extends HorizontalScrollView {

    private SyncScrollView linkedScrollView;

    public SyncScrollView(Context context) {
        super(context);
    }

    public SyncScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLinkedScrollView(SyncScrollView scrollView) {
        linkedScrollView = scrollView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (linkedScrollView != null) {
            linkedScrollView.scrollTo(l, t);
        }
    }
}