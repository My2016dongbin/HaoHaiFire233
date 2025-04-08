package com.haohai.platform.fireforestplatform.ui.acticity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.haohai.platform.fireforestplatform.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import rx.functions.Action1;

public class TestActivity extends BaseActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    private TextView buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        buttonView = (TextView) findViewById(R.id.button_view);

        RxViewAction.clickNoDouble(buttonView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       goActivity();
                    }
                });
    }

    private void goActivity() {
        ARouter.getInstance().build("/test/activity").navigation(this, new NavCallback() {
            @Override
            public void onFound(Postcard postcard) {
                super.onFound(postcard);
                Log.e(TAG, "onFound: 找到了");
            }

            @Override
            public void onLost(Postcard postcard) {
                super.onLost(postcard);
                Log.e(TAG, "onLost: 找不到" );
            }

            @Override
            public void onArrival(Postcard postcard) {
                Log.e(TAG, "onArrival: 跳完了" );
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                super.onInterrupt(postcard);
                Log.e(TAG, "onInterrupt:被拦截了");
            }
        });
    }
}
