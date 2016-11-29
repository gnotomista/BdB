package com.example.gennaro.bdb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MouseKeyboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mouse_keyboard_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
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

