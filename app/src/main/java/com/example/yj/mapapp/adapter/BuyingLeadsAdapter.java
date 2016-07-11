package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.model.BuyingLeads;
import com.example.yj.mapapp.util.LogUtil;

import java.util.List;

public class BuyingLeadsAdapter extends BaseAdapter {

    private List<BuyingLeads> mData;//求购对象集合
    private Context context;//上下文对象
    private LayoutInflater inflater;//布局插入器(打气筒)

    /**
     * 构造函数
     *
     * @param mData
     * @param context
     */
    public BuyingLeadsAdapter(List<BuyingLeads> mData, Context context) {
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
            convertView = inflater.inflate(R.layout.buying_leads_list_item, null);
            //实例化ViewHolder对象
            holder = new ViewHolder();
            //通过findViewById找到对应的布局的id
            holder.buying_leads_name = (TextView) convertView.findViewById(R.id.id_buying_leads_name);
            holder.buying_leads_type = (TextView) convertView.findViewById(R.id.id_buying_leads_type);
            holder.buying_leads_contacts = (TextView) convertView.findViewById(R.id.id_buying_leads_contacts);
            holder.buying_leads_phone = (TextView) convertView.findViewById(R.id.id_buying_leads_phone);
            holder.buying_leads_content = (TextView) convertView.findViewById(R.id.id_buying_leads_content);
            holder.buying_leads_address = (TextView) convertView.findViewById(R.id.id_buying_leads_address);
            holder.buying_leads_ctime = (TextView) convertView.findViewById(R.id.id_buying_leads_ctime);
            // 调用setTag方法时,先将holder对象中的所有控件都赋上了布局上所对应的控件,再将这个holder对象通过setTag方法传递给布局
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置各个控件的内容
        holder.buying_leads_name.setText(mData.get(position).getNAME());
        holder.buying_leads_type.setText(mData.get(position).getTYPE());
        holder.buying_leads_contacts.setText(mData.get(position).getCONTACTS());

        //处理电话号码显示的样式
        String mobile = mData.get(position).getPHONE();
        String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        //设置数据内容
        holder.buying_leads_phone.setText(maskNumber);

        holder.buying_leads_content.setText(mData.get(position).getCONTENT());
        holder.buying_leads_address.setText(mData.get(position).getADDRESS());
        holder.buying_leads_ctime.setText(mData.get(position).getCTIME());

        //获取电话号码
        String phone = holder.buying_leads_phone.getText() + "";
        LogUtil.d("phone================" + phone);

        return convertView;
    }

    class ViewHolder {
        TextView buying_leads_name;
        TextView buying_leads_type;
        TextView buying_leads_contacts;
        TextView buying_leads_phone;
        TextView buying_leads_content;
        TextView buying_leads_address;
        TextView buying_leads_ctime;
    }

    /**
     * 添加数据列表项
     */
    public void addContent(BuyingLeads data) {
        mData.add(data);
    }
}
