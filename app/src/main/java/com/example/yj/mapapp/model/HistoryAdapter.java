package com.example.yj.mapapp.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private List<SearchBeams> data;
    private Context context;

    public HistoryAdapter(Context context, List<SearchBeams> data) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
