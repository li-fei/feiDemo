package com.fei.demo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fei.demo.R;

public class MoreInfoItemView extends LinearLayout{

    ImageView iv_more_info_item;
    TextView tv_more_info_item;

    public MoreInfoItemView(Context context) {
        super(context);
    }

    public MoreInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.more_item_info_view, this, true);
        iv_more_info_item = findViewById(R.id.iv_more_info_item);
        tv_more_info_item = findViewById(R.id.tv_more_info_item);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MoreInfoItemView);
        if (attributes != null) {
            int pic = attributes.getResourceId(R.styleable.MoreInfoItemView_picture,-1);
            if (pic != -1){
                iv_more_info_item.setBackgroundResource(pic);
            }
            String titleText = attributes.getString(R.styleable.MoreInfoItemView_text);
            if (!TextUtils.isEmpty(titleText)) {
                tv_more_info_item.setText(titleText);
            }
        }
    }

}
