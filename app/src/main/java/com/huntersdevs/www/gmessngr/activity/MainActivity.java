package com.huntersdevs.www.gmessngr.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.app.PrefManager;
import com.huntersdevs.www.gmessngr.app.RealmManager;
import com.huntersdevs.www.gmessngr.app.Util;
import com.huntersdevs.www.gmessngr.fragment.ContactFragment;
import com.huntersdevs.www.gmessngr.fragment.MessageFragment;
import com.huntersdevs.www.gmessngr.pojo.ContactPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmObject;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

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
    private PrefManager mPrefManager;

    private MessageFragment mMessageFragment;
    private ContactFragment mContactFragment;

    private static boolean isContactFragmentVisible;

    private ArrayList<String> mContactList = new ArrayList<>();

    private RealmManager realmManager;

    /**
     * if
     *      mPrefManager.isFirstTime => true => start PERMISSION_ACTIVITY
     * else if
     *      mPrefManager.isFirstTime => false -> start LOGIN_ACTIVITY
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mPrefManager = PrefManager.getInstance(mContext);

        realmManager = RealmManager.getInstance(mContext);

        if (mPrefManager.getIsFirstTime()) {
            startActivity(new Intent(MainActivity.this, PermissionActivity.class));
            finish();
        } else if (Util.isLogin() && !mPrefManager.getIsProfileSetuped()) {
            startActivity(new Intent(MainActivity.this, ProfileSetupActivity.class));
            finish();
        } else if (!Util.isLogin()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMessageFragment = MessageFragment.newInstance();
        mContactFragment = ContactFragment.newInstance();

        onClickFlMessageTab();

        queryDataFromFirestore();

//        Log.i(TAG, "onCreate: " + realmManager.getContacts());
    }

    private void queryDataFromFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference colRef = firestore.collection(getString(R.string.users_list_collection));

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                        Log.i(TAG, realmManager.getGMessngrContact(task.getResult().getDocuments().get(i).getId()));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    private void getData() {
        String uid = null;
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null) {
            uid = mFirebaseAuth.getCurrentUser().getUid();
        }
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child(getString(R.string.users_info))
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i(TAG, "onDataChange: " + dataSnapshot
                                .child(getString(R.string.profile_info_collection))
                                .child(getString(R.string.profile_name_db))
                                .getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(TAG, "onCancelled: " + databaseError.toString());
                    }
                });

        FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference mDocRef = mFirebaseFirestore.collection(getString(R.string.users_list_collection)).document("8085658521");
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnapshot = task.getResult();
                    if (docSnapshot.exists()) {
                        Log.i(TAG, "onComplete: " + docSnapshot.get(getString(R.string.user_id)));
                    }
                } else {
                    Log.i(TAG, "onComplete: " + task.getException());
                }
            }
        });
    }

    @OnClick(R.id.fl_mesaage_tab)
    public void onClickFlMessageTab() {
        isContactFragmentVisible = false;

        tvToolbarTitle.setText(getString(R.string.app_name));

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

        tvToolbarTitle.setText(getString(R.string.contacts));

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
