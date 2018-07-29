package com.huntersdevs.www.gmessngr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.PrefManager;
import com.huntersdevs.www.gmessngr.app.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileSetupActivity extends AppCompatActivity implements TextWatcher {

    private Context mContext;
    private PrefManager mPrefManager;

    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_counter)
    TextView tvCounter;

    @BindView(R.id.ll_settingup)
    LinearLayout llSettingup;
    @BindView(R.id.lav)
    LottieAnimationView lav;

    private String phoneNumber, profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        mPrefManager = PrefManager.getInstance(mContext);

        phoneNumber = mPrefManager.getPhoneNumber();

        tvNext.setEnabled(false);
        etName.addTextChangedListener(this);

        setUserInfoOnFirestore();
    }

    @OnClick(R.id.tv_next)
    public void onClickTvNext() {
        hideKeyboard();

        etName.setVisibility(View.GONE);
        tvCounter.setVisibility(View.GONE);
        tvNext.setVisibility(View.GONE);

        llSettingup.setVisibility(View.VISIBLE);
        lav.playAnimation();

        Map<String, String> value = new HashMap<>();
        value.put("phoneNumber", phoneNumber);
        value.put("profileName", profileName);

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mDatabaseReference
                .child(getString(R.string.users_info))
                .child(uid)
                .child(getString(R.string.profile_info_collection))
                .setValue(value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mPrefManager.setIsProfileSetuped(true);
                        lav.pauseAnimation();
                        startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("kuku", "onFailure: " + e.toString());

                etName.setVisibility(View.VISIBLE);
                tvCounter.setVisibility(View.VISIBLE);
                tvNext.setVisibility(View.VISIBLE);

                llSettingup.setVisibility(View.GONE);
                lav.pauseAnimation();

                if (e instanceof FirebaseNetworkException) {
                    Util.showShortToast(mContext, "Please check your network connection, and try again!!!");
                } else {
                    Util.showShortToast(mContext, "Setup failed, please try again!!!");
                }
            }
        });

    }

    private void setUserInfoOnFirestore() {
        FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Map<String, String> value = new HashMap<>();
        value.put(getString(R.string.user_id), uid);
        mFirebaseFirestore.collection(getString(R.string.users_list_collection))
                .document(phoneNumber)
                .set(value);
    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        lav.pauseAnimation();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String counter = s.length() + getString(R.string.counter_limit);
        tvCounter.setText(counter);
        if (s.length() > 0) {
            profileName = s.toString();
            tvNext.setEnabled(true);
        } else {
            tvNext.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}