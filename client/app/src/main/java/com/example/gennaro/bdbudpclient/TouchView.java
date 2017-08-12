package com.example.gennaro.bdbudpclient;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
//import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

public class TouchView extends View {

    boolean init = false;
    double x, y, xPrev, yPrev;

    SeekBar seekBar;
    double progress;

    UdpClient udpClient = UdpClient.getInstance();

    double touchViewWidth, touchViewHeight;

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        touchViewWidth = this.getWidth();
        touchViewHeight = this.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        seekBar = ((Activity)getContext()).findViewById(R.id.mouse_sensitivity_seekbar);
        double rawProgress = (double)(seekBar.getProgress());
        progress = Math.pow(10, -0.477 + rawProgress * (2.0 * 0.477) / 100.0);

        if (!init) {
            xPrev = x;
            yPrev = y;
            init = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (x > 0 && x < touchViewWidth && y > 0 && y < touchViewHeight) {
                    udpClient.mouseMove((int)(progress * (x - xPrev)), (int)(progress * (y - yPrev)));
                    xPrev = x;
                    yPrev = y;
                }
                return true;
            case MotionEvent.ACTION_UP:
                init = false;
                return true;
            default:
                return true;
        }
    }

}
