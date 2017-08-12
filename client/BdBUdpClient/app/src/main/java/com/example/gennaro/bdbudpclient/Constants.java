package com.example.gennaro.bdbudpclient;

/**
 * Created by gennaro on 8/12/17.
 */

public final class Constants {

    public static final int ID_STOP = -1;
    public static final int ID_SETUP = 0;
    public static final int ID_START = 1;
    public static final int ID_KEY = 2;
    public static final int ID_MOUSE_MOVE = 3;
    public static final int ID_MOUSE_CLICK = 4;

    public static final int MOUSE_BUTTON_LEFT = 1;
    public static final int MOUSE_BUTTON_MIDDLE = 2;
    public static final int MOUSE_BUTTON_RIGHT = 3;
    public static final int MOUSE_BUTTON_WHEEL_UP = 4;
    public static final int MOUSE_BUTTON_WHEEL_DOWN = 5;

    public static final int UDP_PORT_RECV = 1846;
    public static final int UDP_PORT_SEND = 1847;
    public static final int UDP_BUFFER_SIZE = 1024;

    private Constants(){
        //this prevents even the native class from calling this ctor as well
        throw new AssertionError();
    }
}
