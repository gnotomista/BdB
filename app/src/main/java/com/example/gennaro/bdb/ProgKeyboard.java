package com.example.gennaro.bdb;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class ProgKeyboard {

        private KeyboardView keyboardView;

        private KeyboardView.OnKeyboardActionListener onKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {

            }

            @Override
            public void onRelease(int i) {

            }

            @Override
            public void onKey(int i, int[] ints) {
                System.out.println(i);
            }

            @Override
            public void onText(CharSequence charSequence) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        };

        public ProgKeyboard(Fragment fragment, int viewid, int layoutid) {
            Activity activity = (Activity)fragment.getContext();
            keyboardView = (KeyboardView)(activity.findViewById(viewid));
            keyboardView.setKeyboard(new Keyboard(activity, layoutid));
            keyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
            keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
            // Hide the standard keyboard initially
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        public boolean isCustomKeyboardVisible() {
            return keyboardView.getVisibility() == View.VISIBLE;
        }

        public void showCustomKeyboard( View v ) {
            keyboardView.setVisibility(View.VISIBLE);
            keyboardView.setEnabled(true);
            //if( v!=null ) ((InputMethodManager)mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        public void hideCustomKeyboard() {
            keyboardView.setVisibility(View.GONE);
            keyboardView.setEnabled(false);
        }

}
