package com.huntersdevs.www.gmessngr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.PrefManager;
import com.huntersdevs.www.gmessngr.app.Util;

import java.util.concurrent.TimeUnit;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = LoginActivity.class.getName();

    private Context mContext;
    private PrefManager mPrefManager;

    private boolean isCodeSended = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mStateChangeCallbacks;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.ll_verifying)
    LinearLayout llVerifying;
    @BindView(R.id.lav)
    LottieAnimationView lav;

    @BindView(R.id.tv_country_code)
    TextView tvCountryCode;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private String phoneNumber, mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        mPrefManager = PrefManager.getInstance(mContext);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.useAppLanguage();

        tvNext.setEnabled(false);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 10) {
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

        mStateChangeCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if (isCodeSended) {
                    isVerifying(false);
                    startCodeVerifyActivity();
                } else {
                    instantVerify(phoneAuthCredential);
                }

                Log.d("kuku", "onVerificationCompleted!");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.d("kuku", "onVerificationFailed! " + e.toString());

                isVerifying(false);

                Log.e(TAG, "onVerificationFailed: " + e.toString());
                Util.showShortToast(mContext, "Verification failed, please try again!!!");

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e(TAG, "onVerificationFailed: InvalidPhoneNumber: " + e.toString());
                    Util.showShortToast(mContext, "Verification failed, Invalid phone number!!!");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e(TAG, "onVerificationFailed: TooManyRequest: " + e.toString());
                    Util.showShortToast(mContext, "Verification failed, please try again after some time!!!");
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                isCodeSended = true;
                Log.d("kuku", "CodeSent!" + forceResendingToken);
            }
        };

    }

    @OnClick(R.id.tv_app_name)
    public void onClickTvAppName() {
        Util.showShortToast(mContext, getApplicationContext().getString(R.string.youareawesome));
    }

    @OnClick(R.id.tv_next)
    public void onClickBtnNext() {

        hideKeyboard();
        isVerifying(true);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mStateChangeCallbacks
        );
    }

    private void startCodeVerifyActivity() {
        Intent intent = new Intent(LoginActivity.this, CodeVerifyActivity.class);
        intent.putExtra(getString(R.string.phone_number), phoneNumber);
        intent.putExtra(getString(R.string.verification_id), mVerificationId);
        startActivity(intent);
    }

    private void instantVerify(PhoneAuthCredential mCredential) {
        mFirebaseAuth.signInWithCredential(mCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    isVerifying(false);
                    mPrefManager.setPhoneNumber(phoneNumber);
                    startActivity(new Intent(LoginActivity.this, ProfileSetupActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    isVerifying(false);
                    Util.showShortToast(mContext, "Login failed, Invalid code!!!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
                isVerifying(false);
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

    private void isVerifying(boolean isVerifying) {
        if (isVerifying) {
            tvCountryCode.setVisibility(View.GONE);
            etPhoneNumber.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);

            llVerifying.setVisibility(View.VISIBLE);
            lav.playAnimation();
        } else {
            tvCountryCode.setVisibility(View.VISIBLE);
            etPhoneNumber.setVisibility(View.VISIBLE);
            tvNext.setVisibility(View.VISIBLE);

            llVerifying.setVisibility(View.GONE);
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
