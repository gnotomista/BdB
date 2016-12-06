package com.example.gennaro.bdb;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

public class ProgKeyboardView extends KeyboardView {

    public static boolean inEditMode = true;
    Paint paint = new Paint();

    @TargetApi(21) // Build.VERSION_CODES.L
    public ProgKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(inEditMode ? new ContextWrapperInner(context) : context, attrs, defStyleAttr, defStyleRes);
    }

    public ProgKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(inEditMode ? new ContextWrapperInner(context) : context, attrs, defStyleAttr);
    }

    public ProgKeyboardView(Context context, AttributeSet attrs) {
        super(inEditMode ? new ContextWrapperInner(context) : context, attrs);
    }

//    @Override
//    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setTextSize(25);
//        paint.setColor(Color.parseColor("#E95420"));
//
//        List<Keyboard.Key> keys = getKeyboard().getKeys();
//        for(Keyboard.Key key: keys) {
//            if(key.label != null)
//                canvas.drawText(key.label.toString(), key.x + (key.width/3), key.y + (key.height/3), paint);
//        }
//    }

    public static class ContextWrapperInner extends ContextWrapper {
        Context base;
        public ContextWrapperInner(Context base) {
            super(base);
            this.base = base;
        }
        public Object getSystemService(String name) {
            return Context.AUDIO_SERVICE.equals(name) ? null : base.getSystemService(name);
        }
    }

}
