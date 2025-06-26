package com.skyline.terraexplorer.mainapps.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.activity.AddbsxxActivity;
import com.skyline.terraexplorer.mainapps.activity.AddscjdActivity;
import com.skyline.terraexplorer.mainapps.activity.BsglListActivity;
import com.skyline.terraexplorer.mainapps.activity.BstjBarChartActivity;
import com.skyline.terraexplorer.mainapps.activity.CdjyListActivity;
import com.skyline.terraexplorer.mainapps.activity.DyjyListActivity;
import com.skyline.terraexplorer.mainapps.activity.FanghuoListActivity;
import com.skyline.terraexplorer.mainapps.activity.FzxxListActivity;
import com.skyline.terraexplorer.mainapps.activity.GsmmListActivity;
import com.skyline.terraexplorer.mainapps.activity.GylcListActivity;
import com.skyline.terraexplorer.mainapps.activity.JsxmListActivity;
import com.skyline.terraexplorer.mainapps.activity.LcpBarChartActivity;
import com.skyline.terraexplorer.mainapps.activity.LcprcwhListActivity;
import com.skyline.terraexplorer.mainapps.activity.LcpzljgListActivity;
import com.skyline.terraexplorer.mainapps.activity.LmcfListActivity;
import com.skyline.terraexplorer.mainapps.activity.OtherBarChartActivity;
import com.skyline.terraexplorer.mainapps.activity.ScjdListActivity;
import com.skyline.terraexplorer.mainapps.activity.ScxcListActivity;
import com.skyline.terraexplorer.mainapps.activity.SdbhListActivity;
import com.skyline.terraexplorer.mainapps.activity.SlgyListActivity;
import com.skyline.terraexplorer.mainapps.activity.SlzyListActivity;
import com.skyline.terraexplorer.mainapps.activity.SsjdListActivity;
import com.skyline.terraexplorer.mainapps.activity.SylListActivity;
import com.skyline.terraexplorer.mainapps.activity.XmglListActivity;
import com.skyline.terraexplorer.mainapps.activity.XmzlListActivity;
import com.skyline.terraexplorer.mainapps.activity.YhswfkListActivity;
import com.skyline.terraexplorer.mainapps.activity.YsdwListActivity;
import com.skyline.terraexplorer.mainapps.activity.YsglListActivity;
import com.skyline.terraexplorer.mainapps.activity.ZjsyListActivity;
import com.skyline.terraexplorer.mainapps.activity.ZrbhqListActivity;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import rx.functions.Action1;

public class ResourceFragment extends Fragment {
    private LinearLayout slfhjdLayout;
    private LinearLayout jsxmLayout;
    private LinearLayout zrbhqLayout;
    private LinearLayout lmcfLayout;
    private LinearLayout sdbhLayout;
    private LinearLayout ysdwLayout;
    private LinearLayout yhswLayout;
    private LinearLayout msgmLayout;
    private LinearLayout slgyLayout;
    private LinearLayout sylcpLayout;
    private LinearLayout gylcLayout;
    private LinearLayout xmghLayout;
    private LinearLayout ssjdLayout;
    private LinearLayout xmzlLayout;
    private LinearLayout zjsyLayout;
    private LinearLayout jcysLayout;
    private LinearLayout scxcbLayout;
    private LinearLayout dyjyLayout;
    private LinearLayout cdjyLayout;
    private LinearLayout fzxxLayout;
    private LinearLayout bsxxLayout;
    private LinearLayout bstjLayout;
    private LinearLayout slzydtLayout;
    private LinearLayout slzyLayout;
    private LinearLayout scjdxxLayout;
    private LinearLayout lcpcyLayout;
    private LinearLayout lcptjLayout;
    private LinearLayout lcprcwhLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resource, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        slfhjdLayout=getView().findViewById(R.id.slfhjd_layout);
        jsxmLayout=getView().findViewById(R.id.jsxm_layout);
        zrbhqLayout=getView().findViewById(R.id.zrbhq_layout);
        lmcfLayout=getView().findViewById(R.id.lmcf_layout);
        sylcpLayout=getView().findViewById(R.id.sylcp_layout);
        sdbhLayout=getView().findViewById(R.id.sdbh_layout);
        ysdwLayout=getView().findViewById(R.id.ysdw_layout);
        msgmLayout=getView().findViewById(R.id.msgm_layout);
        slgyLayout=getView().findViewById(R.id.slgy_layout);
        yhswLayout=getView().findViewById(R.id.yhsw_layout);
        gylcLayout=getView().findViewById(R.id.gylc_layout);
        xmghLayout=getView().findViewById(R.id.xmgh_layout);
        ssjdLayout=getView().findViewById(R.id.ssjd_layout);
        xmzlLayout=getView().findViewById(R.id.xmzl_layout);
        zjsyLayout=getView().findViewById(R.id.zjsy_layout);
        jcysLayout=getView().findViewById(R.id.jcys_layout);
        scxcbLayout=getView().findViewById(R.id.scxcb_layout);
        dyjyLayout=getView().findViewById(R.id.dyjy_layout);
        cdjyLayout=getView().findViewById(R.id.cdjy_layout);
        fzxxLayout=getView().findViewById(R.id.fzxx_layout);
        bsxxLayout=getView().findViewById(R.id.bsgl_layout);
        bstjLayout=getView().findViewById(R.id.bstj_layout);
        slzyLayout=getView().findViewById(R.id.slzy_layout);
        slzydtLayout=getView().findViewById(R.id.slzydt_layout);
        scjdxxLayout=getView().findViewById(R.id.scjdxx_layout);
        lcpcyLayout=getView().findViewById(R.id.lcpcy_layout);
        lcptjLayout=getView().findViewById(R.id.lcptj_layout);
        lcprcwhLayout=getView().findViewById(R.id.lcprcwh_layout);
        RxViewAction.clickNoDouble(slfhjdLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), FanghuoListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(jsxmLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), JsxmListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(zrbhqLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), ZrbhqListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(lmcfLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), LmcfListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(sylcpLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), SylListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(sdbhLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), SdbhListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(ysdwLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), YsdwListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(msgmLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), GsmmListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(slgyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), SlgyListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(yhswLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), YhswfkListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(gylcLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), GylcListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(xmghLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), XmglListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(ssjdLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), SsjdListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(xmzlLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), XmzlListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(zjsyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), ZjsyListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(jcysLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), YsglListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(scxcbLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), ScxcListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(dyjyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), DyjyListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(cdjyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), CdjyListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(fzxxLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), FzxxListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(bsxxLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), BsglListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(bstjLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), BstjBarChartActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(slzydtLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), OtherBarChartActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(slzyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), SlzyListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(scjdxxLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), ScjdListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(lcpcyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), LcpzljgListActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(lcptjLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), LcpBarChartActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(lcprcwhLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), LcprcwhListActivity.class));
                    }
                });

    }
}