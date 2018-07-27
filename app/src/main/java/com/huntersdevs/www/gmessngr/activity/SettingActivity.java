package com.huntersdevs.www.gmessngr.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.fl_main)
    FrameLayout flMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        flMain.setPadding(0, Util.getStatusBarHeight(mContext), 0, 0);
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        onBackPressed();
    }
}
