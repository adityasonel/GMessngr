package com.huntersdevs.www.gmessngr.fragment;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.adapter.ContactAdapter;
import com.huntersdevs.www.gmessngr.app.RealmManager;
import com.huntersdevs.www.gmessngr.pojo.GMessngrContactPOJO;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private static String TAG = ContactFragment.class.getSimpleName();

    private View mView;
    private Context mContext;

    private List<GMessngrContactPOJO> list;
    private ContactAdapter contactAdapter;

    private RealmManager realmManager;

    public ContactFragment() { }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact, container, false);
        mContext = getContext();

        realmManager = RealmManager.getInstance(mContext);

        list = new ArrayList<>();
        contactAdapter = new ContactAdapter(list, mContext);


        return mView;
    }


}
