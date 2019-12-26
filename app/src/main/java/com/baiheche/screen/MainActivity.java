package com.baiheche.screen;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private MainLinearManager manager;
    private FrameLayout frameLayout;

    /**
     * 上一次点击的坐标
     */
    private float lastX;
    private float lastY;
    /**
     * 是否移动
     */
    private boolean isMove;

    private View touchView;
    private boolean isScroll = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1054) {
                Log.i(MainActivity.class.getName(), "setScrollEnabled");
                manager.setScrollEnabled(false);
                frameLayout.addView(touchView);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐去电池等图标和一切修饰部分（状态栏部分）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.screen_recycler);
        frameLayout = findViewById(R.id.frame_layout);


        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("recycler " + i);
        }
        mainAdapter = new MainAdapter(R.layout.item_recycler_view, strings);
        manager = new MainLinearManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                isMove = true;
                isScroll = false;
                mHandler.removeCallbacks(runnable);
            }
        });
        mainAdapter.setOnTouchItemListener(new OnTouchItemListener() {
            @Override
            public boolean onTouchItem(int position, RecyclerView.Adapter adapter, View v, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();
                int[] viewPosition = new int[2];
                v.getLocationInWindow(viewPosition);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isScroll) {
                            isMove = false;
                            lastX = x;
                            lastY = y;
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(v.getWidth(), v.getHeight());
                            params.topMargin = viewPosition[1];
                            params.leftMargin = viewPosition[0];
                            touchView = new LinearLayout(MainActivity.this);
                            touchView.setLayoutParams(params);
                            touchView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            mHandler.postDelayed(runnable, ViewConfiguration.getLongPressTimeout());
                            isScroll = true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isMove && !manager.isScrollEnabled()) {
                            int moveX = (int) (x - lastX);
                            int moveY = (int) (y - lastY);
                            Log.i(MainActivity.class.getName(), "ACTION_MOVE");
                            FrameLayout.LayoutParams moveParams = (FrameLayout.LayoutParams) touchView.getLayoutParams();
                            moveParams.topMargin = viewPosition[1] + moveY;
                            moveParams.leftMargin = viewPosition[0] + moveX;
                            touchView.setLayoutParams(moveParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isScroll) {
                            isScroll = false;
                            if (touchView != null) {
                                if (!manager.isScrollEnabled()) {
                                    frameLayout.removeView(touchView);
                                }
                                touchView = null;
                            }
                        }
                        mHandler.removeCallbacks(runnable);
                        manager.setScrollEnabled(true);
                        break;
                }
                return true;
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = 1054;
            mHandler.sendMessage(message);

        }
    };
}
