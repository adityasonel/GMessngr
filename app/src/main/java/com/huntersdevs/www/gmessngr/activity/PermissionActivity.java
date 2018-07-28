package com.huntersdevs.www.gmessngr.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.PrefManager;
import com.huntersdevs.www.gmessngr.app.Util;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PermissionActivity extends AppCompatActivity {

    private PrefManager mPrefManager;

    private static final int PERMISSION_CALLBACK_CONSTANT = 1000;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
        Context mContext = getApplicationContext();
        mPrefManager = PrefManager.getInstance(mContext);
    }

    @OnClick(R.id.tv_continue)
    public void onClickTvContinue() {
        if(ActivityCompat.checkSelfPermission(PermissionActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(PermissionActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(PermissionActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,permissionsRequired[2])){
                ActivityCompat.requestPermissions(PermissionActivity.this, permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            } else {
                ActivityCompat.requestPermissions(PermissionActivity.this, permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
        }
    }

    @OnClick(R.id.tv_not_now)
    public void onClickTvNotNow() {
        mPrefManager.setIsFirstTime(false);
        startActivity(new Intent(PermissionActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                mPrefManager.setIsFirstTime(false);
                startActivity(new Intent(PermissionActivity.this, LoginActivity.class));
                finish();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,permissionsRequired[2])){
                ActivityCompat.requestPermissions(PermissionActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
        }
    }
    
}
