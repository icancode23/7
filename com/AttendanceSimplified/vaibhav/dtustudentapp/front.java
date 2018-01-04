package com.AttendanceSimplified.vaibhav.dtustudentapp;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.json.JSONObject;

public class front extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    String f14a = "";
    String aa;
    Button blogin;
    Button btdiscovery;
    EditText etpass;
    EditText etuser;
    String fin;
    ProgressBar mProgressBar;
    String mac;
    String message;
    ProgressDialog progressDialog2;
    TextView tvload;
    int f15x;

    class C02291 implements OnClickListener {
        C02291() {
        }

        public void onClick(View v) {
            front.this.startActivity(new Intent(front.this, btdiscovery.class));
        }
    }

    class C02302 implements OnClickListener {
        C02302() {
        }

        public void onClick(View v) {
            front.this.startActivity(new Intent(front.this, UpdatePassword.class));
        }
    }

    class C02313 implements OnClickListener {
        C02313() {
        }

        public void onClick(View v) {
            front.this.mProgressBar.setVisibility(0);
            front.this.tvload.setText("loading...");
            front.this.blogin.setEnabled(false);
            NetworkInfo activeNetwork = ((ConnectivityManager) front.this.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                front.this.progressDialog2.hide();
                new Builder(front.this).setMessage("Connect to Internet").setNegativeButton("OK", null).create().show();
                front.this.mProgressBar.setVisibility(4);
                front.this.blogin.setEnabled(true);
                front.this.tvload.setText("");
                return;
            }
            new loginrequest(front.this.etuser.getText().toString(), front.this.etpass.getText().toString()).execute(new Void[0]);
        }
    }

    public class loginrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 19091;
        int id;
        String macadd;
        String message;
        String name2;
        String password2;
        String roll2;
        TextView status2;
        String username2;

        loginrequest(String username, String password) {
            this.roll2 = username;
            this.password2 = password;
            this.macadd = front.this.mac;
        }

        protected Void doInBackground(Void... arg0) {
            Exception e;
            try {
                this.username2 = "1@" + this.roll2;
                Socket socket = new Socket(this.dstAddress, this.dstPort);
                Socket socket2;
                try {
                    JSONObject json = new JSONObject();
                    json.put("username", this.username2);
                    json.put("password", this.password2 + "");
                    json.put("MAC", this.macadd);
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
                    json2.put("username", this.username2);
                    json2.put("password", this.password2);
                    json2.put("rollnumber", this.roll2);
                    String js = json2.get("status").toString();
                    front.this.fin = json2.toString();
                    this.id = Integer.parseInt(js);
                    front.this.progressDialog2.hide();
                    socket2 = socket;
                } catch (Exception e2) {
                    e = e2;
                    socket2 = socket;
                    this.message = e.toString();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                this.message = e.toString();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            front.this.tvload.setText(front.this.f14a);
            front.this.mProgressBar.setVisibility(4);
            front.this.blogin.setEnabled(true);
            if (this.id == 0) {
                new Builder(front.this).setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                return;
            }
            if (front.this.f15x != 1) {
                Editor editor = front.this.getSharedPreferences(front.PREFS_NAME, 0).edit();
                editor.putString("logged", "logged");
                editor.putString("username", this.roll2);
                editor.putString("password", this.password2);
                editor.commit();
            }
            Intent intent = new Intent(front.this, show_attendance2.class);
            intent.putExtra("message", front.this.fin);
            front.this.finish();
            front.this.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0218R.layout.activity_front);
        this.etuser = (EditText) findViewById(C0218R.id.etuser);
        this.etpass = (EditText) findViewById(C0218R.id.etpass);
        this.blogin = (Button) findViewById(C0218R.id.done);
        Button uppass = (Button) findViewById(C0218R.id.uppass);
        this.btdiscovery = (Button) findViewById(C0218R.id.bdiscoverable);
        TextView tvinfo = (TextView) findViewById(C0218R.id.tvinfo);
        this.btdiscovery.setOnClickListener(new C02291());
        this.mac = BluetoothAdapter.getDefaultAdapter().getAddress();
        this.tvload = (TextView) findViewById(C0218R.id.tvload);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        ImageView yourimg = (ImageView) findViewById(C0218R.id.aaa);
        float a = (float) (((double) width) / 1.45d);
        float b = (float) (((double) height) / 1.9d);
        this.blogin.getLayoutParams().width = width / 2;
        this.blogin.getLayoutParams().height = height / 10;
        this.mProgressBar = (ProgressBar) findViewById(C0218R.id.progressBar);
        this.mProgressBar.getLayoutParams().width = width / 4;
        this.mProgressBar.getLayoutParams().height = height / 12;
        this.mProgressBar.setVisibility(8);
        yourimg.getLayoutParams().height = (int) b;
        yourimg.getLayoutParams().width = (int) a;
        tvinfo.startAnimation(AnimationUtils.loadAnimation(this, C0218R.anim.alpha_anim));
        uppass.getLayoutParams().width = width / 2;
        uppass.getLayoutParams().height = height / 10;
        this.f15x = 0;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            this.f15x = 1;
            this.progressDialog2 = new ProgressDialog(this, C0218R.style.AppTheme.Dark.Dialog);
            this.progressDialog2.setIndeterminate(true);
            this.progressDialog2.setMessage("Autologging in");
            this.progressDialog2.show();
            String username = settings.getString("username", "").toString();
            String password = settings.getString("password", "").toString();
            this.etuser.setText(username);
            this.etpass.setText(password);
            NetworkInfo activeNetwork = ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                this.progressDialog2.dismiss();
                new Builder(this).setMessage("Connect to Internet").setNegativeButton("OK", null).create().show();
                this.mProgressBar.setVisibility(4);
                this.blogin.setEnabled(true);
                this.tvload.setText("");
            } else {
                new loginrequest(username, password).execute(new Void[0]);
            }
        }
        uppass.setOnClickListener(new C02302());
        this.blogin.setOnClickListener(new C02313());
    }
}
