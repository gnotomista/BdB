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

        String[] alt_key_100 = {"~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+"};
        String[] alt_key_200 = {"{", "}", "|"}; // 212-213
        String[] alt_key_300 = {":", "\"", "|"}; // 311-312
        String[] alt_key_400 = {"<", ">", "?"}; // 409-411

        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);
        paint.setColor(Color.parseColor("#FFFFFF"));
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if (key.codes[0] >= 101 && key.codes[0] <= 113)
                canvas.drawText(alt_key_100[key.codes[0]-101], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.5)), paint);
            else if (key.codes[0] >= 212 && key.codes[0] <= 213)
                canvas.drawText(alt_key_200[key.codes[0]-212], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.3)), paint);
            else if (key.codes[0] >= 311 && key.codes[0] <= 312)
                canvas.drawText(alt_key_300[key.codes[0]-311], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.3)), paint);
            else if (key.codes[0] >= 409 && key.codes[0] <= 411)
                canvas.drawText(alt_key_400[key.codes[0]-409], (float)(key.x + (key.width/3.2)), (float)(key.y + (key.height/2.3)), paint);
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
