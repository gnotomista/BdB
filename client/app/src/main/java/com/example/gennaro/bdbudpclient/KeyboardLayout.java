package com.example.gennaro.bdbudpclient;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class KeyboardLayout extends RelativeLayout {

    String mText;

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyboardLayout(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection baseInputConnection = new BaseInputConnection(this, false);
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_NULL;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return baseInputConnection;
    }

    public void init(){
        setFocusable(true);
        setFocusableInTouchMode(true);

        mText ="";
        setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                        Toast.makeText(getContext(), "The text is: " + mText , Toast.LENGTH_LONG).show();
                        mText = "";
                        return true;
                    }
                    else {
                        mText = mText + (char) event.getUnicodeChar();
                        System.out.println(keyCode);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        }
        return true;
    }

}
