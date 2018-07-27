package com.huntersdevs.www.gmessngr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.Util;
import com.huntersdevs.www.gmessngr.fragment.ContactFragment;
import com.huntersdevs.www.gmessngr.fragment.MessageFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    @BindView(R.id.fl_mesaage_tab)
    FrameLayout flMessageTab;
    @BindView(R.id.iv_message)
    ImageView ivMessage;

    @BindView(R.id.fl_contact_tab)
    FrameLayout flContactTab;
    @BindView(R.id.iv_contact)
    ImageView ivContact;

    @BindView(R.id.iv_setting)
    ImageView ivSetting;

    private Context mContext;
    private Activity mActivity;

    private MessageFragment mMessageFragment;
    private ContactFragment mContactFragment;

    private static boolean isContactFragmentVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Util.isLogin()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        mMessageFragment = MessageFragment.newInstance();
        mContactFragment = ContactFragment.newInstance();

        onClickFlMessageTab();
    }

    @OnClick(R.id.fl_mesaage_tab)
    public void onClickFlMessageTab() {
        isContactFragmentVisible = false;

        ivMessage.setSelected(true);
        ivContact.setSelected(false);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, mMessageFragment)
                .commit();

        ivSetting.setVisibility(View.GONE);
    }

    @OnClick(R.id.fl_contact_tab)
    public void onClickFlContactTab() {
        isContactFragmentVisible = true;

        ivContact.setSelected(true);
        ivMessage.setSelected(false);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, mContactFragment)
                .commit();

        ivSetting.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_setting)
    public void onClickIvSetting() {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (isContactFragmentVisible) {
            onClickFlMessageTab();
        } else {
            super.onBackPressed();
        }
    }
}
