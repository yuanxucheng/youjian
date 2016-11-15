package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.model.Contact;
import com.example.yj.mapapp.util.LogUtil;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {

    private ArrayList<Contact> infos;
    private LayoutInflater inflater;
    private Context context;

    public ContactAdapter(ArrayList<Contact> infos, Context context) {
        this.infos = infos;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    // 重新设置新的数据源
    public void setData(ArrayList<Contact> infos) {
        this.infos = infos;
    }

    // 获取
    public ArrayList<Contact> getData() {
        return infos;
    }

    @Override
    public int getCount() {
        LogUtil.d("size", "size:=============" +
                "" + infos.size());
        return infos == null ? 0 : infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(
                    R.layout.contact_item, null);
            holder.iv_contact_icon = (ImageView) convertView
                    .findViewById(R.id.item_search_iv_icon);
            holder.tv_contact_id = (TextView) convertView
                    .findViewById(R.id.item_search_tv_id);
            holder.tv_contact_name = (TextView) convertView
                    .findViewById(R.id.item_search_tv_name);
            holder.tv_contact_contact = (TextView) convertView
                    .findViewById(R.id.item_search_tv_contacts);
            holder.tv_contact_address = (TextView) convertView
                    .findViewById(R.id.item_search_tv_address);
            holder.tv_contact_authentication = (TextView) convertView
                    .findViewById(R.id.item_search_tv_authentication);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contact info = infos.get(position);
        holder.iv_contact_icon.setImageResource(R.mipmap.desktop_icon);
        holder.tv_contact_id.setText(info.getId() + "");
        holder.tv_contact_name.setText(info.getName());
        holder.tv_contact_contact.setText(info.getContacts());
        holder.tv_contact_address.setText(info.getAddress());
        holder.tv_contact_authentication.setText(info.getAuthentication());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_contact_icon;
        TextView tv_contact_id;
        TextView tv_contact_name;
        TextView tv_contact_contact;
        TextView tv_contact_address;
        TextView tv_contact_authentication;
    }
}
