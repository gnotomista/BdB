package com.example.gennaro.bdb;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MouseKeyboardFragment extends Fragment {

    ProgKeyboard progKeyboard;
    boolean keyboard_on = false;
    long last = System.currentTimeMillis(), now = System.currentTimeMillis();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mouse_keyboard_fragment, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
                now = System.currentTimeMillis();
                if (now - last > 500) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (keyboard_on) {
                            progKeyboard.hideCustomKeyboard();
                            keyboard_on = false;
                        } else {
                            ((Activity) getContext()).onBackPressed();
                        }
                        last = now;
                        return true;
                    }
                    last = now;
                    return false;
                } else {
                    return false;
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            progKeyboard = new ProgKeyboard(this, R.id.keyboard_view, R.xml.prog_keyboard_qwerty);
        } catch (XmlPullParserException | IOException e) {

        }

        TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.tap_keyboard_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keyboard_on) {
                    progKeyboard.showCustomKeyboard(view);
                    keyboard_on = true;
                }
            }
        });

        int[] buttons = {
                R.id.left_mouse_button,
                R.id.center_mouse_button,
                R.id.right_mouse_button
        };

        int[] durations = {
                1000,
                800,
                600
        };

        for( int i = 0; i < buttons.length; i++ ) {
            buttonAnim(buttons[i], durations[i]);
        }
    }

    private void buttonAnim(int buttonId, long duration){
        Button b = (Button) this.getView().findViewById(buttonId);
        Animation anim = AnimationUtils.loadAnimation(this.getContext(), R.anim.translate);
        anim.setDuration(duration);
        b.startAnimation(anim);
    }

}

