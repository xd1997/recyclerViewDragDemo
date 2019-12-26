package com.baiheche.screen;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private OnTouchItemListener onTouchItemListener;

    public MainAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        helper.setText(R.id.review_tv, item);
        LinearLayout layout = helper.getView(R.id.item_linear);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onTouchItemListener != null) {
                    return onTouchItemListener.onTouchItem(helper.getLayoutPosition(), MainAdapter.this, v, event);
                }
                return false;
            }
        });
    }

    public void setOnTouchItemListener(OnTouchItemListener onTouchItemListener) {
        this.onTouchItemListener = onTouchItemListener;
    }
}
