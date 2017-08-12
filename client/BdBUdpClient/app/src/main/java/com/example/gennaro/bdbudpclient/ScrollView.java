package com.example.gennaro.bdbudpclient;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScrollView extends View {

    boolean init = false;
    double x, y, yPrev;

    double touchViewWidth, touchViewHeight;

    UdpClient udpClient = UdpClient.getInstance();

    public ScrollView(Context context, AttributeSet attrs) {
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

        if (!init) {
            yPrev = y;
            init = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (x > 0 && x < touchViewWidth && y > 0 && y < touchViewHeight) {
                    boolean scrollDown = (y - yPrev > 2);
                    boolean scrollUp = (y - yPrev < -2);
                    if (udpClient != null) {
                        if (scrollDown) {
                            udpClient.mouseScrollDown();
                        } else if (scrollUp) {
                            udpClient.mouseScrollUp();
                        }
                    }
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
