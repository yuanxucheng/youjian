package com.example.yj.mapapp.adapter;

import android.content.Context;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.model.Firm;

import java.util.List;

public class SearchAdapter extends CommonAdapter<Firm> {

    public SearchAdapter(Context context, List<Firm> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {

        holder.setImageResource(R.id.item_search_iv_icon, mData.get(position).getIconId())
                .setText(R.id.item_search_tv_name, mData.get(position).getName())
                .setText(R.id.item_search_tv_address, mData.get(position).getAddress())
                .setText(R.id.item_search_tv_contacts, mData.get(position).getContacts())
                .setText(R.id.item_search_tv_authentication, mData.get(position).getAuthentication())
                .setText(R.id.item_search_tv_id, mData.get(position).getId() + "");
    }
}
