package com.haohai.platform.fireforestplatform.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haohai.platform.fireforestplatform.R;
import com.haohai.platform.mapmodel.fragment.MapFragment;
import com.haohai.platform.mapmodel.fragment.WeixingFragment;
import com.ruyiruyi.rylibrary.base.BaseFragment;
import com.ruyiruyi.rylibrary.db.DbConfig;

/**
 * Created by geyang on 2020/11/6.
 */

public class MapHomeFragment extends BaseFragment {

    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_new, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
        token = new DbConfig(getContext()).getUser().getToken();


        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        MapFragment mapFragment = new MapFragment();

        WeixingFragment weixingFragment = new WeixingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN",token);
        weixingFragment.setArguments(bundle);

        if (!mapFragment.isAdded()){
            ft.add(R.id.fragment_layout,weixingFragment).commit();
        }


    }
}
