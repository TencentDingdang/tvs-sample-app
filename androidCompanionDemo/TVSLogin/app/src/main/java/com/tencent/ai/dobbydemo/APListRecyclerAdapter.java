package com.tencent.ai.dobbydemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.ai.tvsdevice.info.APListInfo;
import com.tencent.ai.tvsdevice.util.StringHexUtil;

import java.util.List;

public class APListRecyclerAdapter extends RecyclerView.Adapter<APListRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private APListInfo mAPListInfo;

    private List<APListInfo.AplistBean> mAPList;

    private OnItemClickListener mOnItemClickListener;

    public APListRecyclerAdapter(APListInfo info) {
        mAPListInfo = info;
        mAPList = info.getAplist();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public APListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tvs_apitem, parent, false);
        APListRecyclerAdapter.ViewHolder vh = new APListRecyclerAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(APListRecyclerAdapter.ViewHolder holder, int position) {
        if (holder != null) {
            holder.mSSIDText.setText(StringHexUtil.hexStr2Str(mAPList.get(position).getSsid()));
            holder.mENCRYText.setText("Encry:" + mAPList.get(position).getEncry());
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.valueOf(mAPListInfo.getRes());
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mSSIDText, mENCRYText;

        public ViewHolder(View itemView) {
            super(itemView);
            mSSIDText = (TextView)itemView.findViewById(R.id.ssidText);
            mENCRYText = (TextView)itemView.findViewById(R.id.encryText);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
}