package com.AttendanceSimplified.vaibhav.dtustudentapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class btdiscovery extends AppCompatActivity {
    private static final int pstatus = 101;
    Button discover;
    int f11i;
    ImageView img;
    BluetoothAdapter mBluetoothAdapter;
    private boolean permissionisgranted = true;
    AnimationSet s1;
    AnimationSet s2;

    class C02201 implements OnClickListener {
        C02201() {
        }

        public void onClick(View v) {
            if (btdiscovery.this.f11i % 2 == 0) {
                btdiscovery.this.mBluetoothAdapter.enable();
                btdiscovery.this.discover.setText("Enabling");
                btdiscovery.this.ensureDiscoverable();
                btdiscovery.this.img.setVisibility(0);
                btdiscovery.this.img.startAnimation(btdiscovery.this.s1);
            } else {
                btdiscovery.this.mBluetoothAdapter.disable();
                btdiscovery.this.img.startAnimation(btdiscovery.this.s2);
                btdiscovery.this.img.setVisibility(4);
                btdiscovery.this.discover.setText("Bluetooth is Off");
            }
            btdiscovery com_AttendanceSimplified_vaibhav_dtustudentapp_btdiscovery = btdiscovery.this;
            com_AttendanceSimplified_vaibhav_dtustudentapp_btdiscovery.f11i++;
        }
    }

    class C02212 implements Runnable {
        C02212() {
        }

        public void run() {
            if (btdiscovery.this.mBluetoothAdapter.getScanMode() != 23) {
                Intent discoverableIntent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
                discoverableIntent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 300);
                btdiscovery.this.startActivity(discoverableIntent);
            }
            btdiscovery.this.discover.setText("Enabled and Discoverable");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0218R.layout.activity_btdiscovery);
        this.discover = (Button) findViewById(C0218R.id.discoveryset);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.f11i = 0;
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            if (VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 101);
            } else {
                this.permissionisgranted = true;
            }
        }
        this.img = (ImageView) findViewById(C0218R.id.abv);
        this.img.setVisibility(8);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float a = (float) (((double) displaymetrics.widthPixels) / 1.8d);
        this.img.getLayoutParams().height = (int) ((float) (((double) displaymetrics.heightPixels) / 2.1d));
        this.img.getLayoutParams().width = (int) a;
        Animation animnew = AnimationUtils.loadAnimation(this, C0218R.anim.alpha_anim);
        Animation anim2 = AnimationUtils.loadAnimation(this, C0218R.anim.rotate_anim3);
        Animation anim3 = AnimationUtils.loadAnimation(this, C0218R.anim.scale_anim);
        Animation revanimnew = AnimationUtils.loadAnimation(this, C0218R.anim.reversealphaanim);
        Animation revanim2 = AnimationUtils.loadAnimation(this, C0218R.anim.reverserotate);
        Animation revanim3 = AnimationUtils.loadAnimation(this, C0218R.anim.reversescale);
        this.s1 = new AnimationSet(false);
        this.s2 = new AnimationSet(false);
        this.s1.addAnimation(animnew);
        this.s1.addAnimation(anim2);
        this.s1.addAnimation(anim3);
        this.s2.addAnimation(revanimnew);
        this.s2.addAnimation(revanim2);
        this.s2.addAnimation(revanim3);
        this.discover.setOnClickListener(new C02201());
    }

    private void ensureDiscoverable() {
        new Handler().postDelayed(new C02212(), 2000);
    }
}
