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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgKeyboard {

    private KeyboardView keyboardView;
    private String keyCodeString = "";
    private Map<Integer, ArrayList<String>> pressedKey = new HashMap<>();
    XmlPullParser parser;
    boolean caps_lock = false;

    private KeyboardView.OnKeyboardActionListener onKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int keycode) {

        }

        @Override
        public void onRelease(int keycode) {

        }

        @Override
        public void onKey(int keycode, int[] keycodes) {
            decodeAndSend(keycode, keycodes);
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
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
        // Hide the standard keyboard initially
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            parser = activity.getResources().getXml(R.xml.prog_keyboard_qwerty);
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

    private void decodeAndSend(int keyCode, int[] keyCodes) {
        List<Keyboard.Key> keys = this.keyboardView.getKeyboard().getKeys();
        for (Keyboard.Key k : keys) {
            if (k.codes[0] == 301) {
                caps_lock = k.on;
            }
        }

        String keyChar = (pressedKey.get(keyCode)).get(1);
        if (keyCode < 0) {
            keyCodeString = keyChar + "+";
        } else {
            // two lines below are not needed since caps lock key is sending caps_lock command
            // could it be safer to send Shift+key though?
            //if (caps_lock && keyCodeString.isEmpty())
            //    keyCodeString += "Shift+";
            keyCodeString += keyChar;
            MainActivity.sendString(keyCodeString);
            keyCodeString = "";
        }
    }

    private Map<Integer, ArrayList<String>> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        Map<Integer, ArrayList<String>> map = new HashMap<>();
        Integer keyCode;
        String keyLabel;
        String keyChar;
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Key")){
                        ArrayList<String> keyLabelChar = new ArrayList<>();
                        keyCode = Integer.parseInt(parser.getAttributeValue("http://schemas.android.com/apk/res/android","codes"));
                        keyLabel = parser.getAttributeValue("http://schemas.android.com/apk/res/android","keyLabel");
                        keyChar = parser.nextText();
                        keyLabelChar.add(keyLabel);
                        keyLabelChar.add(keyChar);
                        map.put(keyCode, keyLabelChar);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return map;
    }

}
