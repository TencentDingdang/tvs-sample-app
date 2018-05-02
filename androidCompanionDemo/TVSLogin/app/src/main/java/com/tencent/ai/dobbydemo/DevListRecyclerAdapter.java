package com.tencent.ai.dobbydemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.teleal.cling.model.meta.Device;

import java.util.List;

public class DevListRecyclerAdapter extends RecyclerView.Adapter<DevListRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private List<Device> mDeviceList;

    private OnItemClickListener mOnItemClickListener;

    public void setDeviceList(List<Device> deviceList) {
        mDeviceList = deviceList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public DevListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tvs_devitem, parent, false);
        DevListRecyclerAdapter.ViewHolder vh = new DevListRecyclerAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(DevListRecyclerAdapter.ViewHolder holder, int position) {
        if (holder != null) {
            holder.mDevName.setText(mDeviceList.get(position).getDetails().getFriendlyName());
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceList == null ? 0 : mDeviceList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mDevName;

        public ViewHolder(View itemView) {
            super(itemView);
            mDevName = (TextView)itemView.findViewById(R.id.devname);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}