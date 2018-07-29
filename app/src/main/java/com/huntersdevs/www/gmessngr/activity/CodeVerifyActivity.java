package com.huntersdevs.www.gmessngr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.PrefManager;
import com.huntersdevs.www.gmessngr.app.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class CodeVerifyActivity extends AppCompatActivity {

    private static String TAG = CodeVerifyActivity.class.getName();

    private Context mContext;
    private PrefManager mPrefManager;

    @BindView(R.id.tv_info_sms_pin)
    TextView tvInfoSmsPin;

    @BindView(R.id.et_sms_pin)
    EditText etSmsPin;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_wrong_number)
    TextView tvWrongNumber;

    @BindView(R.id.ll_logging)
    LinearLayout llLogging;
    @BindView(R.id.lav)
    LottieAnimationView lav;


    private FirebaseAuth mFirebaseAuth;
    private PhoneAuthCredential mCredential;

    private String phoneNumber, mVerificationId, mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        mPrefManager = PrefManager.getInstance(mContext);

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (getIntent() != null) {
            phoneNumber = getIntent().getStringExtra(getString(R.string.phone_number));
            mVerificationId = getIntent().getStringExtra(getString(R.string.verification_id));
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
                if (s.length() == 6) {

                    tvNext.setEnabled(true);
                    mCode = s.toString();
                    mCredential = PhoneAuthProvider.getCredential(mVerificationId, mCode);

                    onClickTvNext();
                } else if (s.length() <= 5) {
                    tvNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick(R.id.tv_next)
    public void onClickTvNext() {

        hideKeyboard();
        isLogin(true);

        mFirebaseAuth.signInWithCredential(mCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mPrefManager.setPhoneNumber(phoneNumber);
                    startActivity(new Intent(CodeVerifyActivity.this, ProfileSetupActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    isLogin(false);
                    Util.showShortToast(mContext, "Login failed, Invalid code!!!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
                isLogin(false);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Util.showShortToast(mContext, "Please check your number, and try again!!!");
                } else if (e instanceof FirebaseNetworkException) {
                    Util.showShortToast(mContext, "Please check your network connection, and try again!!!");
                } else {
                    Util.showShortToast(mContext, "Login failed, please try again!!!");
                }
            }
        });
    }

    @OnClick(R.id.tv_wrong_number)
    public void onClickTvWrongNumber() {
        onBackPressed();
    }

    private void isLogin(boolean isLogin) {
        if (isLogin) {
            etSmsPin.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
            tvWrongNumber.setVisibility(View.GONE);

            llLogging.setVisibility(View.VISIBLE);
            lav.playAnimation();
        } else {
            etSmsPin.setVisibility(View.VISIBLE);
            tvNext.setVisibility(View.VISIBLE);
            tvWrongNumber.setVisibility(View.VISIBLE);

            llLogging.setVisibility(View.GONE);
            lav.pauseAnimation();
        }
    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

}
