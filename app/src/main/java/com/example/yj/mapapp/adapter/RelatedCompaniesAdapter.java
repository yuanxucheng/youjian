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
import com.example.yj.mapapp.model.RelatedCompanies;

public class RelatedCompaniesAdapter extends BaseAdapter {

    private List<RelatedCompanies> mData;
    private Context context;
    private LayoutInflater inflater;

    public RelatedCompaniesAdapter(List<RelatedCompanies> mData, Context context) {
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
            convertView = inflater.inflate(R.layout.related_companies_list_item, null);
            holder = new ViewHolder();
            holder.related_companies_callPhone = (ImageView) convertView.findViewById(R.id.id_related_companies_callPhone);
            holder.related_companies_name = (TextView) convertView.findViewById(R.id.id_related_companies_name);
            holder.related_companies_area = (TextView) convertView.findViewById(R.id.id_related_companies_area);
            holder.related_companies_contacts = (TextView) convertView.findViewById(R.id.id_related_companies_contacts);
            holder.related_companies_phone = (TextView) convertView.findViewById(R.id.id_related_companies_phone);
            holder.related_companies_address = (TextView) convertView.findViewById(R.id.id_related_companies_address);
            // 调用setTag方法时，先将holder对象中的所有控件都赋上了布局上所对应的控件 ，再将这个holder对象通过setTag方法传递给布局
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.related_companies_callPhone.setImageResource(R.mipmap.call_phone);
        holder.related_companies_name.setText(mData.get(position).getName());
        holder.related_companies_area.setText(mData.get(position).getArea());
        holder.related_companies_contacts.setText(mData.get(position).getContacts());
        holder.related_companies_phone.setText(mData.get(position).getPhone());
        holder.related_companies_address.setText(mData.get(position).getAddress());

        final String phone = holder.related_companies_phone.getText() + "";

        holder.related_companies_callPhone.setOnClickListener(new View.OnClickListener() {
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
        ImageView related_companies_callPhone;
        TextView related_companies_name;
        TextView related_companies_area;
        TextView related_companies_contacts;
        TextView related_companies_phone;
        TextView related_companies_address;
    }

    /**
     * 添加数据列表项
     */
    public void addContent(RelatedCompanies data) {
        mData.add(data);
    }
}
