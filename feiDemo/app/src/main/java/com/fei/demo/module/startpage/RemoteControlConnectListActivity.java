package com.fei.demo.module.startpage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;
import com.fei.demo.adapter.DroneListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class RemoteControlConnectListActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rv_drone_connect_refresh_list;
    private List<String> mDroneNameDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control_connect_list);
        initBackClick();
        initDatas();
        initView();
    }

    private void initDatas() {
        for (int i = 0; i <= 15; i++) {
            mDroneNameDatas.add("V18S0000000" + i);
        }
    }

    private void initView() {
        rv_drone_connect_refresh_list = findViewById(R.id.rv_drone_connect_refresh_list);
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_drone_connect_refresh_list.setLayoutManager(linerLayoutManager);
        DroneListRecyclerViewAdapter adapter = new DroneListRecyclerViewAdapter(this,mDroneNameDatas);
        rv_drone_connect_refresh_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new DroneListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String name) {
                Toast.makeText(RemoteControlConnectListActivity.this, position + ":" + name, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_remote_control_connect_next:
                break;
        }
    }

}
