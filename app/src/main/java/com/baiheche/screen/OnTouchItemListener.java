package com.baiheche.screen;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

public interface OnTouchItemListener {
    boolean onTouchItem(int position, RecyclerView.Adapter adapter, View v, MotionEvent event);
}
