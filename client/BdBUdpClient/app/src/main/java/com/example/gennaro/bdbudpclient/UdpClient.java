package com.example.gennaro.bdbudpclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static android.content.Context.WIFI_SERVICE;

import static com.example.gennaro.bdbudpclient.Constants.*;
import static com.example.gennaro.bdbudpclient.MainActivity.build_JSON_object;
import static com.example.gennaro.bdbudpclient.MainActivity.put_key_value;
import static com.example.gennaro.bdbudpclient.MainActivity.put_key_value_int;

public class UdpClient {

    private static Activity activity_;
    private static Context context_;

    private InetAddress ip_;

    DatagramSocket datagramSocketRecv, datagramSocketSend;

    private boolean setup_ = false;

    // singleton pattern
    private static UdpClient instance = null;

    protected UdpClient()
    {
        try {
            datagramSocketRecv = new DatagramSocket(UDP_PORT_RECV);
            datagramSocketSend = new DatagramSocket(UDP_PORT_SEND);
        } catch (Exception e) {

        }
    }

    public static UdpClient getInstance() {
        if (instance == null){
            instance = new UdpClient();
        }
        return instance;
    }

    public void init(Activity activity, Context context) {
        activity_ = activity;
        context_ = context;
    }
    // end singleton pattern

    private class Setup extends AsyncTask<String, Void, Void> {

        private Setup() { }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) { super.onPostExecute(result); }

        @Override
        protected Void doInBackground(String... params) {
            try {
                byte[] receive_data = new byte[UDP_BUFFER_SIZE];
                DatagramPacket receive_packet = new DatagramPacket(receive_data, receive_data.length);

                showToast(context_, "Setting up communication ", Toast.LENGTH_SHORT);

                datagramSocketRecv.receive(receive_packet);
                String received_string = new String(receive_packet.getData());
                JSONObject received_json = new JSONObject(received_string);
                //showToast(context, "received: "+received_string);
                if (received_json.getInt("id") == ID_SETUP) {
                    final String ip_string = received_json.getString("ip");
                    ip_ = InetAddress.getByName(ip_string);

                    new Handler(context_.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) activity_.findViewById(R.id.server_ip)).setText(ip_string);
                        }
                    });
                }
            } catch (Exception e) {

            }

            JSONObject start_msg = build_JSON_object(ID_START);
            send(start_msg);

            setCommunicationSetup(true);

            showToast(context_, "...done", Toast.LENGTH_SHORT);

            return null;
        }

    }

    private class Send extends AsyncTask<String, Void, Void> {

        private Send() { }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) { super.onPostExecute(result); }

        @Override
        protected Void doInBackground(String... params) {

            // check if connected/initialized
            if (!setup_) {
                showToast(context_, "Communication not setup");
            } else {

                try {
                    String command = params[0];

                    byte[] send_data = command.getBytes();
                    DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, ip_, UDP_PORT_SEND);

                    datagramSocketSend.send(send_packet);

                } catch (IOException e) {

                }
            }

            return null;
        }

    }

    private void showToast(final Context context, final String message, final int length) {
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, length).show();
            }
        });
    }

    private void showToast(final Context context, final String message) {
        showToast(context, message, 1);
    }

    // public methods
    public void setCommunicationSetup(boolean state) {
        setup_ = state;
    }

    public void setup() {
        WifiManager wifiManager = (WifiManager) context_.getSystemService(WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // ip
        int ip = wifiInfo.getIpAddress();
        final String ip_string = String.format("%d.%d.%d.%d",(ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
        new Handler(context_.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((TextView) activity_.findViewById(R.id.host_ip)).setText(ip_string);
            }
        });
        //showToast(context, ip_string);
        // netmask
        int netmask = dhcpInfo.netmask;
        final String netmask_string = String.format("%d.%d.%d.%d",(netmask & 0xff),(netmask >> 8 & 0xff),(netmask >> 16 & 0xff),(netmask >> 24 & 0xff));
        //showToast(context, netmask_string);
        // broadcast ip
        int ip_broadcast = (ip & netmask) | ~netmask;
        final String broadcast_string = String.format("%d.%d.%d.%d",(ip_broadcast & 0xff),(ip_broadcast >> 8 & 0xff),(ip_broadcast >> 16 & 0xff),(ip_broadcast >> 24 & 0xff));
        //showToast(context, broadcast_string);

        try {
            new Setup().execute();
        } catch (Exception e) {

        }
    }

    public void send(String command) { new Send().execute(command); }

    public void send(JSONObject json_command)
    {
        String command = json_command.toString();
        send(command);
    }

    public void mouseMove(int x, int y) {
        JSONObject jMsg = build_JSON_object(ID_MOUSE_MOVE);
        put_key_value_int(jMsg, "x", x);
        put_key_value_int(jMsg, "y", y);
        send(jMsg);
    }

    public void mouseScrollUp() {
        JSONObject jMsg = build_JSON_object(ID_MOUSE_CLICK);
        put_key_value_int(jMsg, "button", MOUSE_BUTTON_WHEEL_UP);
        send(jMsg);
    }

    public void mouseScrollDown() {
        JSONObject jMsg = build_JSON_object(ID_MOUSE_CLICK);
        put_key_value_int(jMsg, "button", MOUSE_BUTTON_WHEEL_DOWN);
        send(jMsg);
    }

    public void sendString(String string) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", string);
        send(jMsg);
    }
}