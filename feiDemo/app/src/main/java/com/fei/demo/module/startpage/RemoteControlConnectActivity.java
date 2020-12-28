package com.fei.demo.module.startpage;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;
import com.fei.demo.adapter.YuneecPagerAdapter;
import com.fei.demo.utils.ToastUtil;
import com.yuneec.qrcodelibrary.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fei.demo.module.startpage.FirstPageActivity.REQUEST_CODE;

public class RemoteControlConnectActivity extends BaseActivity implements View.OnClickListener {

    private boolean isConnectRemoteControl = true;
    int showConnectHintPage = 0;
    Button btn_remote_control_connect_next;
    ViewPager vp_remote_control_connect;
    private List<View> viewList = new ArrayList<>();
    View view_remote_control_connect_hint_skip_2,view_remote_control_connect_hint_skip_3;
    ImageView iv_remote_control_connect_hint_skip_2,iv_remote_control_connect_hint_skip_3;
    TextView tv_remote_control_connect_hint_page;
    LinearLayout ll_remote_control_connect_page_3_scan,ll_remote_control_connect_page_3_to_wifi_setting;
    TextView tv_control_connect_page_3_to_where;
    ImageView iv_remote_control_connect_page_3_to_wifi_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control_connect);
        Bundle bundle = this.getIntent().getExtras();
        showConnectHintPage = bundle.getInt("connectFlag"); //0: remote connect  1: mobile connect
        initBackClick();
        initView();
        initViewPager();
        initSkip(true, new CallBack() {
            @Override
            public void skipCallBack() {
//                finish();
            }
        });
    }

    private void initView() {
        btn_remote_control_connect_next = findViewById(R.id.btn_remote_control_connect_next);
        btn_remote_control_connect_next.setOnClickListener(this);
        tv_remote_control_connect_hint_page = findViewById(R.id.tv_remote_control_connect_hint_page);
        vp_remote_control_connect = findViewById(R.id.vp_remote_control_connect);
        view_remote_control_connect_hint_skip_2 = findViewById(R.id.view_remote_control_connect_hint_skip_2);
        view_remote_control_connect_hint_skip_3 = findViewById(R.id.view_remote_control_connect_hint_skip_3);
        iv_remote_control_connect_hint_skip_2 = findViewById(R.id.iv_remote_control_connect_hint_skip_2);
        iv_remote_control_connect_hint_skip_3 = findViewById(R.id.iv_remote_control_connect_hint_skip_3);
    }

    private void initViewPager() {
        viewList.clear();
        View hint1 = LayoutInflater.from(RemoteControlConnectActivity.this).inflate(R.layout.remote_control_connect_hint_page1, null);
        View hint2 = LayoutInflater.from(RemoteControlConnectActivity.this).inflate(R.layout.remote_control_connect_hint_page2, null);
        View hint3 = LayoutInflater.from(RemoteControlConnectActivity.this).inflate(R.layout.remote_control_connect_hint_page3, null);
        ll_remote_control_connect_page_3_scan = hint3.findViewById(R.id.ll_remote_control_connect_page_3_scan);
        ll_remote_control_connect_page_3_to_wifi_setting = hint3.findViewById(R.id.ll_remote_control_connect_page_3_to_wifi_setting);
        ll_remote_control_connect_page_3_scan.setOnClickListener(this);
        ll_remote_control_connect_page_3_to_wifi_setting.setOnClickListener(this);
        tv_control_connect_page_3_to_where = hint3.findViewById(R.id.tv_control_connect_page_3_to_where);
        iv_remote_control_connect_page_3_to_wifi_setting = hint3.findViewById(R.id.iv_remote_control_connect_page_3_to_wifi_setting);
        viewList.add(hint1);
        if (showConnectHintPage == 0){
            tv_remote_control_connect_hint_page.setText(R.string.str_remote_control);
            tv_control_connect_page_3_to_where.setText(R.string.str_qr_code_info2);
            iv_remote_control_connect_page_3_to_wifi_setting.setImageResource(R.drawable.drone_signal_img);
            viewList.add(hint2);
            view_remote_control_connect_hint_skip_3.setVisibility(View.VISIBLE);
            iv_remote_control_connect_hint_skip_3.setVisibility(View.VISIBLE);
        }else {
            tv_remote_control_connect_hint_page.setText(R.string.str_mobile_phone);
            tv_control_connect_page_3_to_where.setText(R.string.str_remote_control_page3_to_wifi_setting);
            iv_remote_control_connect_page_3_to_wifi_setting.setImageResource(R.drawable.remote_control_connect_img3);
            view_remote_control_connect_hint_skip_3.setVisibility(View.GONE);
            iv_remote_control_connect_hint_skip_3.setVisibility(View.GONE);
        }
        viewList.add(hint3);
        vp_remote_control_connect.setAdapter(new YuneecPagerAdapter(viewList));
        vp_remote_control_connect.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setControlConnectNextBtnStatus(position);
                switch (position){
                    case 0:
                        view_remote_control_connect_hint_skip_2.setBackgroundResource(R.color.connect_hint_num_unselect);
                        iv_remote_control_connect_hint_skip_2.setBackgroundResource(R.drawable.connect_hint_skip_2_unselect);
                        view_remote_control_connect_hint_skip_3.setBackgroundResource(R.color.connect_hint_num_unselect);
                        iv_remote_control_connect_hint_skip_3.setBackgroundResource(R.drawable.connect_hint_skip_3_unselect);
                        break;
                    case 1:
                        view_remote_control_connect_hint_skip_2.setBackgroundResource(R.color.drone_connect_btn_click);
                        iv_remote_control_connect_hint_skip_2.setBackgroundResource(R.drawable.connect_hint_skip_2_selected);
                        view_remote_control_connect_hint_skip_3.setBackgroundResource(R.color.connect_hint_num_unselect);
                        iv_remote_control_connect_hint_skip_3.setBackgroundResource(R.drawable.connect_hint_skip_3_unselect);
                        break;
                    case 2:
                        view_remote_control_connect_hint_skip_3.setBackgroundResource(R.color.drone_connect_btn_click);
                        iv_remote_control_connect_hint_skip_3.setBackgroundResource(R.drawable.connect_hint_skip_3_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setControlConnectNextBtnStatus(int position) {
        if (position == viewList.size()-1){
            btn_remote_control_connect_next.setText(R.string.str_yuneec_drone_unconnected);
            btn_remote_control_connect_next.setEnabled(false);
            btn_remote_control_connect_next.setBackground(getResources().getDrawable(R.color.welcome_btn_unclick));
        }else {
            btn_remote_control_connect_next.setText(R.string.str_wel_next);
            btn_remote_control_connect_next.setEnabled(true);
            btn_remote_control_connect_next.setBackground(getResources().getDrawable(R.drawable.agree_button_selector));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_remote_control_connect_next:
                vp_remote_control_connect.setCurrentItem(vp_remote_control_connect.getCurrentItem() + 1, true);
                break;
            case R.id.ll_remote_control_connect_page_3_scan:
                Intent intent = new Intent(RemoteControlConnectActivity.this, QRcodeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
            case R.id.ll_remote_control_connect_page_3_to_wifi_setting:
                if (showConnectHintPage == 0){
                    if (isConnectRemoteControl){
                        startActivity(this,RemoteControlConnectListActivity.class);
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.str_remote_control_connect_insert), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                    startActivity(wifiSettingsIntent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    ToastUtil.getInstance().toastShow(this, result);
//                    QRUtil.startConnectWifi(FirstPageActivity.this,result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                }
            }
        }
    }

}
