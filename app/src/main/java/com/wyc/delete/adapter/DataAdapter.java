package com.wyc.delete.adapter;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.wyc.delete.bean.DataBean;
import com.wyc.delete.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "WYC-DataAdapter";
    private List<DataBean> mDataList;
    private Map<String, Boolean> mCheckMap = new HashMap<>();
    private Map<Integer, DataBean> mPositionMap = new HashMap<>();
    private List<DataBean> mSelectedList = new ArrayList<>();

    public interface OperateData {

        void uploadByNet(List<DataBean> beanList);

        void deleteLocalData(DataBean bean);
    }

    private OperateData mOperate;

    public void setOperateData(OperateData operateData) {
        this.mOperate = operateData;
    }

    public DataAdapter(List<DataBean> dataList) {
        this.mDataList = dataList;
        if (mDataList != null) {
            for (DataBean bean : mDataList) {
                mCheckMap.put(bean.getFlag(), false);
            }
        }
    }

    public void addDataList(List<DataBean> dataList) {
        this.mDataList.addAll(dataList);
        if (mDataList != null) {
            for (DataBean bean : mDataList) {
                mCheckMap.put(bean.getFlag(), false);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final DataBean bean = mDataList.get(position);
            ((ViewHolder) holder).mTitle.setText(bean.getTitle());
            if (TextUtils.equals("1", bean.getState())) {
                ((ViewHolder) holder).mState.setText("上报成功");
            } else if (TextUtils.equals("0", bean.getState())) {
                ((ViewHolder) holder).mState.setText("已保存");
            }

            ((ViewHolder) holder).mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCheckMap.put(bean.getFlag(), isChecked);
                    if (isChecked) {
                        if (!mPositionMap.containsKey(position)) {
                            mPositionMap.put(position, bean);
                        }
                        if (!mSelectedList.contains(bean)) {
                            mSelectedList.add(bean);
                        }
                    } else {
                        mPositionMap.keySet().remove(position);
                        mSelectedList.remove(bean);
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    });
                }
            });
            ((ViewHolder) holder).mBox.setChecked(mCheckMap.get(bean.getFlag()));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void resetMap() {
        mPositionMap.clear();
        mSelectedList.clear();
    }

    /**
     * 调用测方法先要实现OperateData接口
     */
    public void deleteSelectedData() {
        for (DataBean bean : mSelectedList) {
            for (Map.Entry<Integer, DataBean> entry : mPositionMap.entrySet()) {
                if (entry.getValue().getId() == bean.getId()) {
                    bean.setSelectedPos(entry.getKey());
                }
            }
        }
        Collections.sort(mSelectedList, new Comparator<DataBean>() {
            @Override
            public int compare(DataBean o1, DataBean o2) {
                return Long.compare(o2.getSelectedPos(), o1.getSelectedPos());
            }
        });
        List<DataBean> beanList = new ArrayList<>();
        for (int i = 0; i < mSelectedList.size(); i++) {
            DataBean bean = mSelectedList.get(i);
            Log.i(TAG, "delete bean = " + bean);
            mDataList.remove(bean);
            if (TextUtils.equals("1", bean.getState())) {
                beanList.add(bean);
            } else {
                if (mOperate != null) {
                    mOperate.deleteLocalData(bean);
                }
            }
            notifyItemRemoved(bean.getSelectedPos());
            notifyItemRangeChanged(bean.getSelectedPos(), mDataList.size());
        }
        if (mOperate != null) {
            mOperate.uploadByNet(beanList);
        }
        resetMap();
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox mBox;
        TextView mTitle;
        TextView mState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBox = itemView.findViewById(R.id.item_box);
            mTitle = itemView.findViewById(R.id.item_title);
            mState = itemView.findViewById(R.id.item_state);
        }
    }
}
