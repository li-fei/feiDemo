package com.fei.demo.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fei.demo.R;
import java.util.List;

public class DroneListRecyclerViewAdapter extends RecyclerView.Adapter<DroneViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<String> mDatas;

    public DroneListRecyclerViewAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DroneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_drone_list_recycler_view, parent, false);
        DroneViewHolder viewHolder = new DroneViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DroneViewHolder holder, final int position) {
        holder.tv_remote_control_list_item_connect_drone_name.setText(mDatas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT).show();
                mOnItemClickListener.onClick(position,mDatas.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int pos) {
        mDatas.add("add");
        notifyItemInserted(pos);
    }

    public void deleateData(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

    public interface OnItemClickListener {
        void onClick(int position,String name);
    }
    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
}

class DroneViewHolder extends RecyclerView.ViewHolder {

    TextView tv_remote_control_list_item_connect_drone_name;

    public DroneViewHolder(View itemView) {
        super(itemView);

        tv_remote_control_list_item_connect_drone_name = (TextView) itemView.findViewById(R.id.tv_remote_control_list_item_connect_drone_name);

    }
}
