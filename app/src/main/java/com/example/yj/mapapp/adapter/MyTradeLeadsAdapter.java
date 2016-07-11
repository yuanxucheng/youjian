package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.model.MyTradeLeads;

import java.util.List;

public class MyTradeLeadsAdapter extends BaseAdapter {

    private List<MyTradeLeads> mData;//我的供求对象集合
    private Context context;//上下文对象
    private LayoutInflater inflater;//布局插入器(打气筒)

    public MyTradeLeadsAdapter(List<MyTradeLeads> mData, Context context) {
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
        if (convertView == null) {//判断布局是否为null
            //使用插入器插入布局
            convertView = inflater.inflate(R.layout.my_trade_leads_listview, null);
            //实例化ViewHolder对象
            holder = new ViewHolder();
            //通过findViewById找到对应的布局的id
            holder.my_buying_leads_title = (TextView) convertView.findViewById(R.id.id_my_buying_leads_title);
            holder.my_buying_leads_category = (TextView) convertView.findViewById(R.id.id_my_buying_leads_category);
            holder.my_buying_leads_contacts = (TextView) convertView.findViewById(R.id.id_my_buying_leads_contacts);
            holder.my_buying_leads_phone = (TextView) convertView.findViewById(R.id.id_my_buying_leads_phone);
            holder.my_buying_leads_content = (TextView) convertView.findViewById(R.id.id_my_buying_leads_content);
            holder.my_buying_leads_address = (TextView) convertView.findViewById(R.id.id_my_buying_leads_address);
            holder.my_buying_leads_ctime = (TextView) convertView.findViewById(R.id.id_my_buying_leads_ctime);
            // 调用setTag方法时，先将holder对象中的所有控件都赋上了布局上所对应的控件 ，再将这个holder对象通过setTag方法传递给布局
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.my_buying_leads_title.setText(mData.get(position).getNAME());
        holder.my_buying_leads_category.setText(mData.get(position).getTYPE());
        holder.my_buying_leads_contacts.setText(mData.get(position).getCONTACTS());
        holder.my_buying_leads_phone.setText(mData.get(position).getPHONE());
        holder.my_buying_leads_content.setText(mData.get(position).getCONTENT());
        holder.my_buying_leads_address.setText(mData.get(position).getADDRESS());
        holder.my_buying_leads_ctime.setText(mData.get(position).getCTIME());

        return convertView;
    }

    class ViewHolder {
        TextView my_buying_leads_title;
        TextView my_buying_leads_category;
        TextView my_buying_leads_contacts;
        TextView my_buying_leads_phone;
        TextView my_buying_leads_content;
        TextView my_buying_leads_address;
        TextView my_buying_leads_ctime;
    }

    /**
     * 添加数据列表项
     */
    public void addContent(MyTradeLeads data) {
        mData.add(data);
    }
}
