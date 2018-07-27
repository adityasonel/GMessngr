package com.huntersdevs.www.gmessngr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.GoogleAuthProvider;
import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.Util;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static String TAG = LoginActivity.class.getName();

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.fl_loading)
    FrameLayout flLoading;
    @BindView(R.id.lav)
    LottieAnimationView lav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        if (mGoogleApiClient == null) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                        .addConnectionCallbacks(this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                        .build();
            } catch (Exception ex) {
                Log.d(TAG, "Exception while building GoogleApiClient: " + ex.toString());
            }
        }
    }

    @OnClick(R.id.tv_app_name)
    public void onClickTvAppName() {
        Util.showShortToast(mContext, getApplicationContext().getString(R.string.youareawesome));
    }

    @OnClick(R.id.tv_login)
    public void onClickTVLogin() {
        if (!Util.isNetworkAvailable(mContext)) {
            Util.showShortToast(mContext, getString(R.string.checknetworkconnection));
        } else {
            flLoading.setVisibility(View.VISIBLE);
            tvLogin.setEnabled(false);
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult mGoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (mGoogleSignInResult.isSuccess()) {
                GoogleSignInAccount mGoogleSignInAccount = mGoogleSignInResult.getSignInAccount();
                if (mGoogleSignInAccount != null) {
                    firebaseAuthByGoogle(mGoogleSignInAccount);
                }
            }
        }
    }

    private void firebaseAuthByGoogle(GoogleSignInAccount mGoogleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(mGoogleSignInAccount.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            flLoading.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            tvLogin.setEnabled(true);
                            mFirebaseAuth.signOut();
                            flLoading.setVisibility(View.GONE);
                            Util.showLongToast(mContext, getString(R.string.somethingwentwrong));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tvLogin.setEnabled(true);
                mFirebaseAuth.signOut();
                flLoading.setVisibility(View.GONE);
                Util.showLongToast(mContext, getString(R.string.somethingwentwrong));
                Log.d(TAG, "onFailure listenr: " + e.toString());
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}
