package com.AttendanceSimplified.vaibhav.dtustudentapp;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class discovery extends AppCompatActivity {
    private static final int pstatus = 101;
    String btdevname;
    int connects;
    int countscan;
    Button discover;
    String fin;
    int flag;
    int green = Color.parseColor("#00FF00");
    int f12i;
    ImageView img;
    JSONObject json4;
    JSONArray jsonArray_student;
    JSONObject jsonlog;
    JSONObject jsonlogsend;
    String last_activity = null;
    String lastactive = null;
    BluetoothAdapter mBluetoothAdapter;
    private final BroadcastReceiver mReceiver = new C02264();
    ArrayList<String> mac;
    String name;
    String f13p;
    String password;
    private boolean permissionisgranted = true;
    int red = Color.parseColor("#ff0000");
    Button refresh;
    String rname;
    AnimationSet s1;
    AnimationSet s2;
    Socket socketlog;
    TextView tvbtname;
    TextView tvconfirm;
    TextView tvdetail;
    TextView tvdetected;
    TextView tvkk;
    TextView tvroll;
    String username;

    class C02221 implements OnClickListener {
        C02221() {
        }

        public void onClick(View v) {
            discovery.this.refresh.setText("Refreshing");
            discovery.this.refresh.setEnabled(false);
            discovery.this.name = discovery.this.mBluetoothAdapter.getName();
            discovery.this.btdevname = "Device name :" + discovery.this.name;
            discovery.this.btnamecheck();
            new loginrequest().execute(new Void[0]);
        }
    }

    class C02242 implements OnClickListener {

        class C02231 implements Runnable {
            C02231() {
            }

            public void run() {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.bluetooth.device.action.FOUND");
                filter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
                filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
                discovery.this.registerReceiver(discovery.this.mReceiver, filter);
                discovery.this.mBluetoothAdapter.startDiscovery();
            }
        }

        C02242() {
        }

        public void onClick(View v) {
            if (discovery.this.f12i % 2 == 0) {
                discovery.this.mBluetoothAdapter.enable();
                discovery.this.discover.setText("Enabling");
                discovery.this.discover.setEnabled(false);
                discovery.this.jsonArray_student = new JSONArray();
                discovery.this.ensureDiscoverable();
                discovery.this.mac = new ArrayList();
                discovery.this.name = discovery.this.mBluetoothAdapter.getName();
                discovery.this.btdevname = "Device name :" + discovery.this.name;
                discovery.this.btnamecheck();
                discovery.this.img.setVisibility(0);
                discovery.this.img.startAnimation(discovery.this.s1);
                discovery.this.countscan = 0;
                new Handler().postDelayed(new C02231(), 2000);
            } else {
                discovery.this.mBluetoothAdapter.disable();
                discovery.this.img.startAnimation(discovery.this.s2);
                discovery.this.img.setVisibility(4);
                discovery.this.discover.setText("Bluetooth is Off");
            }
            discovery com_AttendanceSimplified_vaibhav_dtustudentapp_discovery = discovery.this;
            com_AttendanceSimplified_vaibhav_dtustudentapp_discovery.f12i++;
        }
    }

    class C02253 implements Runnable {
        C02253() {
        }

        public void run() {
            discovery.this.name = discovery.this.mBluetoothAdapter.getName();
            discovery.this.btdevname = "Device name :" + discovery.this.name;
            discovery.this.tvbtname.setText(discovery.this.btdevname);
            discovery.this.tvroll.setText(discovery.this.rname);
            if (discovery.this.name.equals(discovery.this.f13p)) {
                discovery.this.discover.setEnabled(true);
                discovery.this.tvconfirm.setText("Device names Match");
                discovery.this.tvconfirm.setTextColor(discovery.this.green);
                return;
            }
            discovery.this.mBluetoothAdapter.disable();
            discovery.this.discover.setText("Make Me Discoverable");
            discovery.this.discover.setEnabled(false);
            discovery.this.tvconfirm.setText("Device Name does not match with roll number");
            discovery.this.tvconfirm.setTextColor(discovery.this.red);
            discovery.this.img.startAnimation(discovery.this.s2);
            discovery.this.img.setVisibility(4);
        }
    }

    class C02264 extends BroadcastReceiver {
        C02264() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!"android.bluetooth.adapter.action.DISCOVERY_STARTED".equals(action)) {
                if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                    discovery com_AttendanceSimplified_vaibhav_dtustudentapp_discovery = discovery.this;
                    com_AttendanceSimplified_vaibhav_dtustudentapp_discovery.countscan++;
                    if (discovery.this.countscan < 3) {
                        discovery.this.mBluetoothAdapter.startDiscovery();
                    } else {
                        new datarequest().execute(new Void[0]);
                    }
                } else if ("android.bluetooth.device.action.FOUND".equals(action)) {
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    String s = device.getName();
                    String dev_addr = device.getAddress() + "";
                    discovery.this.flag = 1;
                    for (int mac_i = 0; mac_i < discovery.this.mac.size(); mac_i++) {
                        if (((String) discovery.this.mac.get(mac_i)).equals(dev_addr)) {
                            discovery.this.flag = 0;
                            break;
                        }
                    }
                    if (discovery.this.flag == 1) {
                        discovery.this.mac.add(dev_addr);
                        try {
                            discovery.this.jsonlog = new JSONObject();
                            discovery.this.jsonlog.put("scan_by", discovery.this.f13p);
                            discovery.this.jsonlog.put("student_id", s);
                            discovery.this.jsonArray_student.put(discovery.this.jsonlog);
                            discovery.this.mac.add(dev_addr);
                        } catch (Exception e) {
                            Toast.makeText(discovery.this.getApplicationContext(), "EXCEPTION found", 1).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class C02275 implements Runnable {
        C02275() {
        }

        public void run() {
            if (discovery.this.mBluetoothAdapter.getScanMode() != 23) {
                Intent discoverableIntent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
                discoverableIntent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 300);
                discovery.this.startActivity(discoverableIntent);
            }
            discovery.this.discover.setText("Enabled and Discoverable");
            discovery.this.discover.setEnabled(true);
        }
    }

    class C02286 implements Runnable {
        C02286() {
        }

        public void run() {
            discovery.this.refresh.setText("Refresh");
            discovery.this.refresh.setEnabled(true);
        }
    }

    public class datarequest extends AsyncTask<Void, Void, Void> {
        String address;
        int logport = 10777;
        String student_id;
        String testServerNamelog2 = "54.149.115.243";

        datarequest() {
        }

        protected Void doInBackground(Void... arg0) {
            try {
                discovery.this.socketlog = new Socket(this.testServerNamelog2, this.logport);
                PrintStream printStream = new PrintStream(discovery.this.socketlog.getOutputStream());
                byte[] s3 = Encrypt3.encrypt2(discovery.this.jsonArray_student.toString());
                DataOutputStream dOut = new DataOutputStream(discovery.this.socketlog.getOutputStream());
                dOut.writeInt(s3.length);
                dOut.write(s3);
                discovery.this.socketlog.close();
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
        }
    }

    public class loginrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 19091;
        int id;
        String message;
        String password2;
        String username2;

        loginrequest() {
            this.username2 = discovery.this.username;
            this.password2 = discovery.this.password;
            this.id = 0;
        }

        protected Void doInBackground(Void... arg0) {
            Exception e;
            try {
                Socket socket = new Socket(this.dstAddress, this.dstPort);
                Socket socket2;
                try {
                    JSONObject json = new JSONObject();
                    json.put("username", this.username2);
                    json.put("password", this.password2 + "");
                    String a = json.toString() + "\n";
                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    byte[] s2 = Encrypt3.encrypt2(a);
                    dOut.writeInt(s2.length);
                    dOut.write(s2);
                    DataInputStream dIn = new DataInputStream(socket.getInputStream());
                    int length = dIn.readInt();
                    byte[] messages = new byte[length];
                    if (length > 0 && length < 102400) {
                        dIn.readFully(messages, 0, messages.length);
                    }
                    this.message = Encrypt3.decrypt2(messages);
                    JSONObject json2 = new JSONObject(this.message);
                    discovery.this.lastactive = json2.get("last_activity").toString();
                    discovery.this.last_activity = discovery.this.lastactive;
                    json2.put("username", this.username2);
                    json2.put("password", this.password2);
                    String js = json2.get("status").toString();
                    discovery.this.fin = json2.toString();
                    this.id = Integer.parseInt(js);
                    socket2 = socket;
                } catch (Exception e2) {
                    e = e2;
                    socket2 = socket;
                    discovery.this.last_activity = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                discovery.this.last_activity = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (this.id == 0) {
                discovery.this.connects = 0;
                discovery.this.setDetail();
                return;
            }
            discovery.this.connects = 1;
            discovery.this.setDetail();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0218R.layout.activity_discovery);
        String mess = getIntent().getExtras().getString("message");
        this.tvdetail = (TextView) findViewById(C0218R.id.tvdetail);
        this.refresh = (Button) findViewById(C0218R.id.brefresh);
        this.tvroll = (TextView) findViewById(C0218R.id.roll);
        this.tvbtname = (TextView) findViewById(C0218R.id.tvbluetooth);
        this.tvconfirm = (TextView) findViewById(C0218R.id.tvconfirm);
        this.tvkk = (TextView) findViewById(C0218R.id.tvkk);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.tvdetected = (TextView) findViewById(C0218R.id.tvdetected);
        this.img = (ImageView) findViewById(C0218R.id.abv);
        this.img.setVisibility(8);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float a = (float) (((double) displaymetrics.widthPixels) / 1.8d);
        this.img.getLayoutParams().height = (int) ((float) (((double) displaymetrics.heightPixels) / 2.1d));
        this.img.getLayoutParams().width = (int) a;
        this.discover = (Button) findViewById(C0218R.id.discoveryset);
        this.name = this.mBluetoothAdapter.getName();
        this.btdevname = "Device name :" + this.name;
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            if (VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 101);
            } else {
                this.permissionisgranted = true;
            }
        }
        this.f12i = 0;
        try {
            this.json4 = new JSONObject(mess);
            this.last_activity = this.json4.get("last_activity").toString();
            this.username = this.json4.get("username").toString();
            this.password = this.json4.get("password").toString();
            this.f13p = this.json4.get("rollnumber").toString();
        } catch (JSONException e) {
            this.tvroll.setText("error");
        }
        this.rname = "Roll number :" + this.f13p;
        this.refresh.setOnClickListener(new C02221());
        this.tvdetail.setText(this.last_activity);
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
        this.discover.setOnClickListener(new C02242());
        if ((this.last_activity.contains("(0 min ago") || this.last_activity.contains("(1 min ago")) && this.mBluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.disable();
            this.img.setVisibility(0);
            this.img.startAnimation(this.s2);
            this.img.setVisibility(4);
            this.discover.setText("Bluetooth is Off");
            this.tvdetected.setText("You were Scanned , Your bluetooth is now turned off");
        }
    }

    private void btnamecheck() {
        this.mBluetoothAdapter.enable();
        new Handler().postDelayed(new C02253(), 4000);
    }

    private void ensureDiscoverable() {
        new Handler().postDelayed(new C02275(), 3000);
    }

    public void setDetail() {
        this.tvdetail.setText(this.last_activity);
        if ((this.last_activity.contains("(0 min ago") || this.last_activity.contains("(1 min ago")) && this.mBluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.disable();
            this.f12i++;
            this.discover.setText("Bluetooth is Off");
            this.img.setVisibility(0);
            this.img.startAnimation(this.s2);
            this.img.setVisibility(4);
            this.tvdetected.setText("You were Scanned , Your bluetooth is now turned off");
        }
        if (this.connects == 1) {
            new Handler().postDelayed(new C02286(), 2000);
        } else if (this.connects == 0) {
            this.refresh.setEnabled(true);
            this.refresh.setText("Refresh");
            new Builder(this).setMessage("Refresh Failed").setNegativeButton("Retry", null).create().show();
        }
    }
}
