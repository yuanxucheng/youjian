package com.example.yj.mapapp.adapter;

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
import com.example.yj.mapapp.model.HardSuper;

import java.util.List;

public class HardSuperDetailAdapter extends BaseAdapter {

    private List<HardSuper> mData;//五金超市对象集合
    private Context context;//上下文对象
    private LayoutInflater inflater;//布局插入器(打气筒)

    /**
     * 构造函数
     *
     * @param mData
     * @param context
     */
    public HardSuperDetailAdapter(List<HardSuper> mData, Context context) {
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
            convertView = inflater.inflate(R.layout.hard_super_detail_item, null);
            //实例化ViewHolder对象
            holder = new ViewHolder();
            //通过findViewById找到对应的布局的id
            holder.hard_super_callPhone = (ImageView) convertView.findViewById(R.id.id_hard_super_callPhone);
            holder.hard_super_name = (TextView) convertView.findViewById(R.id.id_hard_super_name);
            holder.hard_super_area = (TextView) convertView.findViewById(R.id.id_hard_super_area);
            holder.hard_super_category = (TextView) convertView.findViewById(R.id.id_hard_super_category);
            holder.hard_super_contacts = (TextView) convertView.findViewById(R.id.id_hard_super_contacts);
            holder.hard_super_phone = (TextView) convertView.findViewById(R.id.id_hard_super_phone);
            holder.hard_super_address = (TextView) convertView.findViewById(R.id.id_hard_super_address);
            // 调用setTag方法时,先将holder对象中的所有控件都赋上了布局上所对应的控件,再将这个holder对象通过setTag方法传递给布局
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置各个控件的内容
        holder.hard_super_callPhone.setImageResource(R.mipmap.call_phone);
        holder.hard_super_name.setText(mData.get(position).getName());
        holder.hard_super_area.setText(mData.get(position).getArea());
        holder.hard_super_category.setText(mData.get(position).getCategory());
        holder.hard_super_contacts.setText(mData.get(position).getContacts());

        if (mData.get(position).getAddress().equals("null")) {
            holder.hard_super_address.setText("无");
        } else {
            holder.hard_super_address.setText(mData.get(position).getAddress());
        }

        holder.hard_super_phone.setText(mData.get(position).getPhone());

        //获取电话号码
        final String phone = holder.hard_super_phone.getText() + "";
        //设置拨号图标的点击事件
        holder.hard_super_callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//创建Intent对象
                intent.setAction(Intent.ACTION_CALL);//指定意图动作
                intent.setData(Uri.parse("tel:" + phone));//指定电话号码
                context.startActivity(intent);//跳转界面
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView hard_super_callPhone;
        TextView hard_super_name;
        TextView hard_super_area;
        TextView hard_super_category;
        TextView hard_super_contacts;
        TextView hard_super_phone;
        TextView hard_super_address;
    }

    /**
     * 添加数据列表项
     */
    public void addContent(HardSuper data) {
        mData.add(data);
    }

}
