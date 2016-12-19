package com.example.gennaro.bdb;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String[] alt_key_200 = {"~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+"};
        String[] alt_key_300 = {"{", "}", "|"}; // 212-213
        String[] alt_key_400 = {":", "\"", "|"}; // 311-312
        String[] alt_key_500 = {"<", ">", "?"}; // 409-411

        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);
        paint.setColor(Color.parseColor("#F29879"));
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if (key.codes[0] >= 201 && key.codes[0] <= 213)
                canvas.drawText(alt_key_200[key.codes[0]-201], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.5)), paint);
            else if (key.codes[0] >= 312 && key.codes[0] <= 313)
                canvas.drawText(alt_key_300[key.codes[0]-312], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.3)), paint);
            else if (key.codes[0] >= 411 && key.codes[0] <= 412)
                canvas.drawText(alt_key_400[key.codes[0]-411], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.3)), paint);
            else if (key.codes[0] >= 509 && key.codes[0] <= 511)
                canvas.drawText(alt_key_500[key.codes[0]-509], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.3)), paint);
        }
    }

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
