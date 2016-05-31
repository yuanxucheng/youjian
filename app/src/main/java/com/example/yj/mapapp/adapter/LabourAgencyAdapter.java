package com.example.yj.mapapp.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.model.LabourAgency;

public class LabourAgencyAdapter extends BaseAdapter {

    private List<LabourAgency> mData;
    private Context context;
    private LayoutInflater inflater;

    public LabourAgencyAdapter(List<LabourAgency> mData, Context context) {
        this.mData = mData;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;//创建持有者对象
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.labour_agency_list_item, null);
            holder = new ViewHolder();
            holder.labour_agency_callPhone = (ImageView) convertView.findViewById(R.id.id_labour_agency_callPhone);
            holder.labour_agency_name = (TextView) convertView.findViewById(R.id.id_labour_agency_name);
            holder.labour_agency_area = (TextView) convertView.findViewById(R.id.id_labour_agency_area);
            holder.labour_agency_contacts = (TextView) convertView.findViewById(R.id.id_labour_agency_contacts);
            holder.labour_agency_phone = (TextView) convertView.findViewById(R.id.id_labour_agency_phone);
            holder.labour_agency_address = (TextView) convertView.findViewById(R.id.id_labour_agency_address);
            // 调用setTag方法时，先将holder对象中的所有控件都赋上了布局上所对应的控件 ，再将这个holder对象通过setTag方法传递给布局
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.labour_agency_callPhone.setImageResource(R.mipmap.call_phone);
        holder.labour_agency_name.setText(mData.get(position).getName());
        holder.labour_agency_area.setText(mData.get(position).getArea());
        holder.labour_agency_contacts.setText(mData.get(position).getContacts());
        holder.labour_agency_phone.setText(mData.get(position).getPhone());
        holder.labour_agency_address.setText(mData.get(position).getAddress());

        final String phone = holder.labour_agency_phone.getText() + "";

        holder.labour_agency_callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);//指定意图动作
                intent.setData(Uri.parse("tel:" + phone));//指定电话号码
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView labour_agency_callPhone;
        TextView labour_agency_name;
        TextView labour_agency_area;
        TextView labour_agency_contacts;
        TextView labour_agency_phone;
        TextView labour_agency_address;
    }

    /**
     * 添加数据列表项
     */
    public void addContent(LabourAgency data) {
        mData.add(data);
    }
}

