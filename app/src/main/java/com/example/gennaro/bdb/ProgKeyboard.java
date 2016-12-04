package com.example.gennaro.bdb;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProgKeyboard {

    private KeyboardView keyboardView;
    private String keyCodeString = "";
    private Map<Integer, String> pressedKey = new HashMap<>();
    XmlPullParser parser;

    private KeyboardView.OnKeyboardActionListener onKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onKey(int i, int[] ints) {
            decodeAndSend(i);
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

    public ProgKeyboard(Fragment fragment, int viewid, int layoutid) throws XmlPullParserException, IOException {
        Activity activity = (Activity)fragment.getContext();
        keyboardView = (KeyboardView)(activity.findViewById(viewid));
        keyboardView.setKeyboard(new Keyboard(activity, layoutid));
        keyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
        // Hide the standard keyboard initially
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            parser = activity.getResources().getXml(R.xml.key_dictionary);
            pressedKey = parseXML(parser);
        } catch (XmlPullParserException | IOException e) {
            System.out.println(e);
        }

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

    private void decodeAndSend(int keyCode) {
        //System.out.println(keyCode);
        //System.out.println(pressedKey.get(keyCode));
        if (keyCode < 0) {
            keyCodeString = pressedKey.get(keyCode) + "+";
        } else {
            keyCodeString += pressedKey.get(keyCode);
            MainActivity.sendString(keyCodeString);
            keyCodeString = "";
        }
    }

    private Map<Integer, String> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        Map<Integer, String> map = new HashMap<>();
        Integer keyCode = null;
        String keyChar = null;
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("key_code")) {
                        keyCode = Integer.parseInt(parser.nextText());
                    } else if (name.equalsIgnoreCase("key_char")) {
                        keyChar = parser.nextText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("key")) {
                        map.put(keyCode, keyChar);
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return map;
    }

}
