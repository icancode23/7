package com.AttendanceSimplified.vaibhav.dtustudentapp;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class show_attendance2 extends AppCompatActivity {
    BluetoothAdapter adapter;
    Button bdiscovery;
    Button brefresh;
    String fin;
    JSONObject json4;
    JSONArray myarray3;
    String password = "";
    private ArrayList<Model> productList;
    String rollnumber;
    String username = "";
    int f16x;
    int x1;

    class C02331 implements OnClickListener {
        C02331() {
        }

        public void onClick(View v) {
            Editor editor = show_attendance2.this.getSharedPreferences(front.PREFS_NAME, 0).edit();
            editor.remove("logged");
            editor.remove("username");
            editor.remove("password");
            editor.remove("token");
            editor.commit();
            Toast.makeText(show_attendance2.this.getApplicationContext(), "Logout Successfull", 1).show();
            Intent intent = new Intent(show_attendance2.this, front.class);
            show_attendance2.this.finish();
            show_attendance2.this.startActivity(intent);
        }
    }

    class C02353 implements OnClickListener {
        C02353() {
        }

        public void onClick(View v) {
            show_attendance2.this.brefresh.setEnabled(false);
            show_attendance2.this.bdiscovery.setEnabled(false);
            show_attendance2.this.brefresh.setText("Refreshing");
            new loginrequest().execute(new Void[0]);
        }
    }

    class C02364 implements OnItemClickListener {
        C02364() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String sub1 = ((TextView) view.findViewById(C0218R.id.sub)).getText().toString();
            String pres1 = ((TextView) view.findViewById(C0218R.id.pres)).getText().toString();
            String abs1 = ((TextView) view.findViewById(C0218R.id.abs)).getText().toString();
            Toast.makeText(show_attendance2.this.getApplicationContext(), "Subject : " + sub1 + "\nPresent : " + pres1 + "\nAbsent : " + abs1 + "\nAttendance Percent : " + ((TextView) view.findViewById(C0218R.id.perc)).getText().toString(), 0).show();
        }
    }

    public class loginrequest extends AsyncTask<Void, Void, Void> {
        String dstAddress = dbcred.dstAddress1;
        int dstPort = 19091;
        int id;
        String message;
        String name2;
        String password2;
        TextView status2;
        String username2;

        loginrequest() {
            this.username2 = show_attendance2.this.username;
            this.password2 = show_attendance2.this.password;
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
                    json2.put("username", this.username2);
                    json2.put("password", this.password2);
                    json2.put("rollnumber", show_attendance2.this.rollnumber);
                    String js = json2.get("status").toString();
                    show_attendance2.this.fin = json2.toString();
                    this.id = Integer.parseInt(js);
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
            show_attendance2.this.brefresh.setEnabled(true);
            show_attendance2.this.brefresh.setText("Refresh");
            show_attendance2.this.bdiscovery.setEnabled(true);
            if (this.id == 0) {
                new Builder(show_attendance2.this).setMessage("Refresh Failed").setNegativeButton("Retry", null).create().show();
                return;
            }
            Intent intent = new Intent(show_attendance2.this, show_attendance2.class);
            intent.putExtra("message", show_attendance2.this.fin);
            show_attendance2.this.finish();
            show_attendance2.this.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0218R.layout.activity_show_attendance2);
        TextView tvname = (TextView) findViewById(C0218R.id.tvname);
        TextView tvatt = (TextView) findViewById(C0218R.id.tvatt);
        this.brefresh = (Button) findViewById(C0218R.id.brefresh);
        this.bdiscovery = (Button) findViewById(C0218R.id.bdiscover);
        ((Button) findViewById(C0218R.id.blogout)).setOnClickListener(new C02331());
        tvatt.setTextColor(Color.parseColor("#ffffff"));
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        final String mess = getIntent().getExtras().getString("message");
        String last_activity = "";
        this.bdiscovery.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(show_attendance2.this, discovery.class);
                intent1.putExtra("message", mess);
                show_attendance2.this.startActivity(intent1);
            }
        });
        try {
            this.json4 = new JSONObject(mess);
            String js = this.json4.get("table").toString();
            this.username = this.json4.get("username").toString();
            this.password = this.json4.get("password").toString();
            this.rollnumber = this.json4.get("rollnumber").toString();
            String name = this.json4.get("name").toString();
            last_activity = this.json4.get("last_activity").toString();
            this.myarray3 = new JSONArray(js);
            this.x1 = this.myarray3.length();
            this.f16x = this.x1 + 1;
            tvname.setText("Hi !  " + name + "    ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.brefresh.setOnClickListener(new C02353());
        this.productList = new ArrayList();
        ListView lview = (ListView) findViewById(C0218R.id.listview);
        listviewAdapter adapter = new listviewAdapter(this, this.productList);
        lview.setAdapter(adapter);
        populateList(this.myarray3);
        adapter.notifyDataSetChanged();
        lview.setOnItemClickListener(new C02364());
    }

    private void populateList(JSONArray myarray3) {
        Model[] item = new Model[this.f16x];
        item[0] = new Model("Subject", "P", "A", "Percentage");
        this.productList.add(item[0]);
        int row = 0 + 1;
        for (int row1 = 0; row1 < this.f16x; row1++) {
            try {
                JSONObject json3 = new JSONObject(myarray3.get(row1).toString());
                JSONObject jSONObject;
                try {
                    item[row] = new Model(json3.get("subject").toString(), json3.get("presence").toString(), json3.get("absent").toString(), json3.get("attendance_percent").toString());
                    this.productList.add(item[row]);
                    row++;
                    jSONObject = json3;
                } catch (Exception e) {
                    jSONObject = json3;
                }
            } catch (Exception e2) {
            }
        }
    }
}
