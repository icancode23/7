package com.AttendanceSimplified.vaibhav.dtustudentapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class listviewAdapter extends BaseAdapter {
    Activity activity;
    public ArrayList<Model> productList;

    private class ViewHolder {
        TextView mabs;
        TextView mperc;
        TextView mpres;
        TextView msub;

        private ViewHolder() {
        }
    }

    public listviewAdapter(Activity activity, ArrayList<Model> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    public int getCount() {
        return this.productList.size();
    }

    public Object getItem(int position) {
        return this.productList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = this.activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(C0218R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.msub = (TextView) convertView.findViewById(C0218R.id.sub);
            holder.mpres = (TextView) convertView.findViewById(C0218R.id.pres);
            holder.mabs = (TextView) convertView.findViewById(C0218R.id.abs);
            holder.mperc = (TextView) convertView.findViewById(C0218R.id.perc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Model item = (Model) this.productList.get(position);
        holder.msub.setText(item.getsub().toString());
        holder.mpres.setText(item.getpres().toString());
        holder.mabs.setText(item.getabs().toString());
        holder.mperc.setText(item.getperc().toString());
        return convertView;
    }
}
