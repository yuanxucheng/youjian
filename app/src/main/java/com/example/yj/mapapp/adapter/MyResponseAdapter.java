package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.model.MyResponse;
import com.example.yj.mapapp.model.MyTradeLeads;
import com.example.yj.mapapp.util.LogUtil;

import java.util.List;

public class MyResponseAdapter extends BaseAdapter {

    private List<MyResponse> mData;//我的响应对象集合
    private Context context;//上下文对象
    private LayoutInflater inflater;//布局插入器(打气筒)

    public MyResponseAdapter(List<MyResponse> mData, Context context) {
        this.mData = mData;
        this.context = context;
        //实例化打气筒
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<MyResponse> mData) {
        this.mData = mData;
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
            convertView = inflater.inflate(R.layout.my_response_listview, null);
            //实例化ViewHolder对象
            holder = new ViewHolder();
            //通过findViewById找到对应的布局的id
            holder.my_response_title = (TextView) convertView.findViewById(R.id.id_my_response_title);
            holder.my_response_intention = (TextView) convertView.findViewById(R.id.id_my_response_intention);
            holder.my_response_contacts = (TextView) convertView.findViewById(R.id.id_my_response_contacts);
            holder.my_response_phone = (TextView) convertView.findViewById(R.id.id_my_response_phone);
            holder.my_response_content = (TextView) convertView.findViewById(R.id.id_my_response_content);
            holder.my_response_ctime = (TextView) convertView.findViewById(R.id.id_my_response_ctime);
            // 调用setTag方法时，先将holder对象中的所有控件都赋上了布局上所对应的控件 ，再将这个holder对象通过setTag方法传递给布局
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置各个控件的内容
        holder.my_response_title.setText(mData.get(position).getSd_name());
        holder.my_response_contacts.setText(mData.get(position).getContacts());
        holder.my_response_content.setText(mData.get(position).getContent());
        holder.my_response_ctime.setText(mData.get(position).getCtime());

        //处理电话号码显示的样式
        String mobile = mData.get(position).getPhone();
        String maskNumber = "";
        if (mobile.length() == 11) {
            maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            holder.my_response_phone.setText(maskNumber);
        } else {
            holder.my_response_phone.setText(mobile);
        }
        //处理意向显示的文字
        Boolean isIntention = mData.get(position).isIsintention();
        if (isIntention == true) {
            holder.my_response_intention.setText("意向响应");
            holder.my_response_phone.setText(mobile);

        } else {
            holder.my_response_intention.setText("暂无意向");
            holder.my_response_phone.setText(maskNumber);
        }

        //获取电话号码
        String phone = holder.my_response_phone.getText() + "";
        LogUtil.d("phone================" + phone);

        return convertView;
    }

    class ViewHolder {
        TextView my_response_title;
        TextView my_response_intention;
        TextView my_response_contacts;
        TextView my_response_phone;
        TextView my_response_content;
        TextView my_response_ctime;
    }

    /**
     * 添加数据列表项
     */
    public void addContent(MyResponse data) {
        mData.add(data);
    }
}