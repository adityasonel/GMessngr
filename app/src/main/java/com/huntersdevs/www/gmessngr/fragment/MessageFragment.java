package com.huntersdevs.www.gmessngr.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private View mView;
    private Context mContext;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, mView);
        mContext = getContext();
        return mView;
    }

    @OnClick(R.id.tv_message_fragment)
    public void onClickTvMessage() {

    }

}
