package com.huntersdevs.www.gmessngr.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeVerifyActivity extends AppCompatActivity {

    private static String TAG = CodeVerifyActivity.class.getName();

    private Context mContext;

    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.tv_info_sms_pin)
    TextView tvInfoSmsPin;

    @BindView(R.id.et_sms_pin)
    EditText etSmsPin;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        rlMain.setPadding(0, Util.getStatusBarHeight(mContext), 0, 0);

        if (getIntent() != null) {
            phoneNumber = getIntent().getStringExtra(getString(R.string.phone_number));
        }

        String temp = getString(R.string.sms_pin_sent_to) + " +91-" + phoneNumber + "\n" + getString(R.string.please_enter_pin_below);
        tvInfoSmsPin.setText(temp);

        tvNext.setEnabled(false);
        etSmsPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!String.valueOf(s).contains(" ") && s.length() == 4) {
                    tvNext.setEnabled(true);
                } else if (s.length() <= 3) {
                    tvNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick(R.id.tv_wrong_number)
    public void onClickTvWrongNumber() {
        onBackPressed();
    }

}
