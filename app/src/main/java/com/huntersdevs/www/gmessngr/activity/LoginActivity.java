package com.huntersdevs.www.gmessngr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.Util;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = LoginActivity.class.getName();

    private Context mContext;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mStateChangeCallbacks;;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.fl_loading)
    FrameLayout flLoading;
    @BindView(R.id.lav)
    LottieAnimationView lav;

    @BindView(R.id.tv_country_code)
    TextView tvCountryCode;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private String phoneNumber;
    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        mFirebaseAuth = FirebaseAuth.getInstance();

        tvNext.setEnabled(false);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!String.valueOf(s).contains(" ") && s.length() == 10) {
                    phoneNumber = s.toString();
                    tvNext.setEnabled(true);
                } else if (s.length() <= 9) {
                    tvNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick(R.id.tv_app_name)
    public void onClickTvAppName() {
        Util.showShortToast(mContext, getApplicationContext().getString(R.string.youareawesome));
    }

    @OnClick(R.id.tv_next)
    public void onClickBtnNext() {
        Intent intent = new Intent(LoginActivity.this, CodeVerifyActivity.class);
        intent.putExtra(getString(R.string.phone_number), phoneNumber);
        startActivity(intent);
    }

}
