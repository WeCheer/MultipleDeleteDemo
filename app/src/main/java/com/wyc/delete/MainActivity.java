package com.wyc.delete;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wyc.delete.adapter.DataAdapter;
import com.wyc.delete.bean.DataBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "WYC-MainActivity";


    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new DataAdapter(getDataList());
        mRecyclerView.setAdapter(mAdapter);
    }


    private List<DataBean> getDataList() {
        List<DataBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DataBean bean = new DataBean();
            bean.setId(i);
            bean.setFlag(String.valueOf(i));
            bean.setTitle("item 测试标题 --> " + i);
            bean.setState(i <= 4 ? "1" : "0");
            list.add(bean);
        }
        return list;
    }

    public void onClick(View view) {
        mAdapter.setOperateData(new DataAdapter.OperateData() {

            @Override
            public void uploadByNet(List<DataBean> beanList) {
                List<String> list = new ArrayList<>();
                for (DataBean bean : beanList) {
                    Log.e(TAG, "uploadByNet bean = " + bean);
                    list.add(bean.getTitle());
                }
                Toast.makeText(MainActivity.this, "uploadByNet bean = " +
                        list.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void deleteLocalData(DataBean bean) {
                Log.e(TAG, "deleteLocalData bean = " + bean);
                Toast.makeText(MainActivity.this, "deleteLocalData bean = " +
                        bean.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
        mAdapter.deleteSelectedData();
    }

}
