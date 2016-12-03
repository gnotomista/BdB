package com.example.gennaro.bdb;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;

    static TcpClient tcpClient = new TcpClient();
    EditText ip, port;
    DrawerLayout drawerLayout;
    EditText msg;

    boolean normalSpeed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relative_layout, new ConnectFragment()).commit();
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
        tcpClient.send("-999999999");
        tcpClient.disconnect(this);
    }

    public void onConnectClick(View v) {
        ip = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);

        tcpClient.connect(this, ip.getText().toString(), Integer.parseInt(port.getText().toString()));
        //tcpClient.send("[test message] Ueila 1846");
    }

    public void onDisconnectClick(View v) {
        tcpClient.send("quit");
        tcpClient.disconnect(this);
    }

    public void onNextSlide(View v) {
        tcpClient.send("Page_Down");
    }

    public void onPreviousSlide(View v) {
        tcpClient.send("Page_Up");
    }

    public void onPlayPause(View v) {
        if (!normalSpeed){
            tcpClient.send("equal");
            normalSpeed = true;
        } else {
            tcpClient.send("space");
        }
    }

    public void onStop(View v) {
        tcpClient.send("s");
    }

    public void onSlower(View v) {
        tcpClient.send("KP_Subtract");
        normalSpeed = false;
    }

    public void onFaster(View v) {
        tcpClient.send("KP_Add");
        normalSpeed = false;
    }

    public void onNext(View v) {
        tcpClient.send("n");
    }

    public void onPrevious(View v) {
        tcpClient.send("p");
    }

    public void onVolumeUp(View v) {
        tcpClient.send("Ctrl+Up");
    }

    public void onVolumeDown(View v) {
        tcpClient.send("Ctrl+Down");
    }

    public void onMute(View v) {
        tcpClient.send("m");
    }

    public void onFullScreen(View v) {
        tcpClient.send("f");
    }

    public void onExitFullScreen(View v) {
        tcpClient.send("Escape");
    }

    public void onSendClick(View v) {
        msg = (EditText) findViewById(R.id.msg);
        tcpClient.send(msg.getText().toString());
    }

    public static void onMouseMove(int x, int y) {
        String mouseMovements = "mouse," + Integer.toString(x) + "," + Integer.toString(y);
        tcpClient.send(mouseMovements);
    }

    public void onLeftMouseButton(View v) {
        tcpClient.send("left_mouse_button");
    }

    public void onRightMouseButton(View v) {
        tcpClient.send("right_mouse_button");
    }

    public void onMiddleMouseButton(View v) {
        tcpClient.send("middle_mouse_button");
    }

    public static void onMouseScrollUp() {
        tcpClient.send("scroll_up_mouse_button");
    }

    public static void onMouseScrollDown() {
        tcpClient.send("scroll_down_mouse_button");
    }

}
