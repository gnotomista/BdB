package com.example.gennaro.bdbudpclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONObject;

import static com.example.gennaro.bdbudpclient.Constants.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    DrawerLayout drawerLayout;

    UdpClient udpClient = UdpClient.getInstance();

    boolean normalSpeed_ = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relative_layout, new ConnectFragment()).commit();

        udpClient.init(this, MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = item.getItemId();

        if (id == R.id.nav_connect) {
            fragment = new ConnectFragment();
        } else if (id == R.id.nav_presentation) {
            fragment = new PresentationFragment();
        } else if (id == R.id.nav_vlc) {
            fragment = new VlcFragment();
        } else if (id == R.id.nav_mouse_keyboard) {
            fragment = new MouseKeyboardFragment();
        } else if (id == R.id.nav_test) {
            fragment = new TestFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relative_layout, fragment).commit();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject exit_msg = build_JSON_object(-1);
        udpClient.send(exit_msg);
    }

    public static JSONObject build_JSON_object (int id) {
        JSONObject jObj = new JSONObject();
        put_key_value_int(jObj, "id", id);
        return jObj;
    }

    public static JSONObject put_key_value(JSONObject jObj, String key, String value) {
        try {
            jObj.put(key, value);
        } catch (Exception e) {

        }
        return jObj;
    }

    public static JSONObject put_key_value_int(JSONObject jObj, String key, int value) {
        try {
            jObj.put(key, value);
        } catch (Exception e) {

        }
        return jObj;
    }

    // callbacks
    public void onConnectClick(View v) { udpClient.setup(); }

    public void onDisconnectClick(View v) {
        JSONObject exit_msg = MainActivity.build_JSON_object(ID_STOP);
        udpClient.send(exit_msg);
    }

    public void onNextSlide(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "Page_Down");
        udpClient.send(jMsg);
    }

    public void onPreviousSlide(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "Page_Up");
        udpClient.send(jMsg);
    }

    public void onPlayPause(View v) {
        if (!normalSpeed_){
            JSONObject jMsg = build_JSON_object(ID_KEY);
            put_key_value(jMsg, "key", "equal");
            udpClient.send(jMsg);
            normalSpeed_ = true;
        } else {
            JSONObject jMsg = build_JSON_object(ID_KEY);
            put_key_value(jMsg, "key", "space");
            udpClient.send(jMsg);
        }
    }

    public void onStop(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "s");
        udpClient.send(jMsg);
    }

    public void onSlower(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "KP_Subtract");
        udpClient.send(jMsg);
        normalSpeed_ = false;
    }

    public void onFaster(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "KP_Add");
        udpClient.send(jMsg);
        normalSpeed_ = false;
    }

    public void onNext(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "n");
        udpClient.send(jMsg);
    }

    public void onPrevious(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "p");
        udpClient.send(jMsg);
    }

    public void onVolumeUp(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "Ctrl+Up");
        udpClient.send(jMsg);
    }

    public void onVolumeDown(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "Ctrl+Down");
        udpClient.send(jMsg);
    }

    public void onMute(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "m");
        udpClient.send(jMsg);
    }

    public void onFullScreen(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "f");
        udpClient.send(jMsg);
    }

    public void onExitFullScreen(View v) {
        JSONObject jMsg = build_JSON_object(ID_KEY);
        put_key_value(jMsg, "key", "Escape");
        udpClient.send(jMsg);
    }

    public void onSendClick(View v) {
        EditText msg = (EditText) findViewById(R.id.msg);
        udpClient.send(msg.getText().toString());
    }

    public void onLeftMouseButton(View v) {
        JSONObject jMsg = build_JSON_object(ID_MOUSE_CLICK);
        put_key_value_int(jMsg, "button", MOUSE_BUTTON_LEFT);
        udpClient.send(jMsg);
    }

    public void onRightMouseButton(View v) {
        JSONObject jMsg = build_JSON_object(ID_MOUSE_CLICK);
        put_key_value_int(jMsg, "button", MOUSE_BUTTON_RIGHT);
        udpClient.send(jMsg);
    }

    public void onMiddleMouseButton(View v) {
        JSONObject jMsg = build_JSON_object(ID_MOUSE_CLICK);
        put_key_value_int(jMsg, "button", MOUSE_BUTTON_MIDDLE);
        udpClient.send(jMsg);
    }

}
