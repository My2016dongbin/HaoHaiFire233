package com.skyline.terraexplorer.tools;

import android.util.Log;
import android.widget.Toast;

import com.bean.RsFireBuf;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.ProgressTool;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.test.entity.AllFireRows;
import com.skyline.test.entity.Rows;
import com.skyline.test.entity.TokenMessage;
import com.skyline.test.socket.NettyClient;
import com.skyline.test.utils.Constance;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Request;

public class RsFireTool extends ProgressTool  {
    private static final String TAG = "RsFireTool";
    String phoneNumber="";
    String baseurl="http://27.223.18.10:2019/";
    String token="";
    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
            //setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
            //setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            //mTv.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");


            switch (id)
            {
                case 100:
                    Gson gson=new Gson();

                  AllFireRows allFireRows=  gson.fromJson(response,AllFireRows.class);
                  DeleteAllRsFire();
                  DrawFireLable(allFireRows.getRows());
                    //   mTv.setText(String.valueOf(allContent.getRows().size()));
                    break;
                case 101:

                    break;
                case 1:
                    Gson gsons=new Gson();
                    TokenMessage messageContent=gsons.fromJson(response,TokenMessage.class);


                    token=messageContent.getMessage();
                    //getHtml(null);
                    //  mTv.setText( messageContent.getMessage());
                    break;

            }
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            //  mProgressBar.setProgress((int) (100 * progress));
        }
    }
    @Override
    protected void doWork() {

    }

    @Override
    public MenuEntry getMenuEntry() {
       return MenuEntry.createFor(this, R.string.rs_fire_toolname, R.drawable.rs_satellite, 20);
    }

    @Override
    public boolean onBeforeOpenToolContainer() {
        showNormalButtons();

        String loginUrl=baseurl+"/api/Account/Login";
        Map<String, String> params = new HashMap<>();
        params.put("username", "test");
        params.put("password", "123456");
        // GetRsFire(loginUrl,1,params);
        GetLogin(loginUrl,1);
        phoneNumber= SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getString(Constance.USER_PhoneNumber);
        return true;
    }

    @Override
    protected void showNormalButtons() {

        toolContainer.removeButtons();
        toolContainer.setUpperViewHidden(true);
        toolContainer.addButton(1,R.drawable.one_hour,R.string.rs_fire_onehour);
        toolContainer.addButton(2,R.drawable.three_hour, R.string.rs_fire_threehour);
        toolContainer.addButton(3,R.drawable.one_hour,R.string.rs_fire_oneday);
        toolContainer.addButton(4,R.drawable.three_hour,R.string.rs_fire_threeday);
        toolContainer.addButton(5,R.drawable.five_day,R.string.rs_fire_fiveday);
    }

    @Override
    public void onButtonClick(int tag) {
        String dataUrl=baseurl+"/api/Satellite/GetList?Token=" + token;
        switch (tag)
        {

            case 1:
                GetRsFire(dataUrl,100);
                //不同参数，不同数据。
                break;
            case 2:

                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
        super.onButtonClick(tag);
    }

    @Override
    protected void workCompleted() {

    }
    public void sendRsFireCmd(int cmd)
    {
        RsFireBuf.RsFire.RsFireSend.Builder rsFireSend= RsFireBuf.RsFire.RsFireSend.newBuilder();
        rsFireSend.setCmd(cmd);
        rsFireSend.setPhoneNumber(phoneNumber);
        if (!NettyClient.getInstance().getConnectStatus())
        {

        }

    }
    public  void  GetRsFire(String url,int id, Map<String, String> params )
    {
        OkHttpUtils.get()
                .url(url)
                .id(id)

                .params(params)
                .build()
        .execute(new MyStringCallback());
    }
    public  void GetLogin(String url,int id)
    {
        OkHttpUtils.get()
                .url(url)
                .id(id)
                .addParams("username", "内蒙古")
                .addParams("password", "123456")
                .build()
                .execute(new MyStringCallback());
    }
    public  void  GetRsFire(String url,int id )
    {
        OkHttpUtils.get()
                .url(url)
                .id(id)
                .build()
                .execute(new MyStringCallback());
    }

    private  void DrawFireLable(List<Rows> messageList)
    {
        if (messageList.size()>0)
        {
            for (int i=0;i<messageList.size();i++)
            {
                final double x=messageList.get(i).getLongitude();
                final double y=messageList.get(i).getLatitude();
                //ISGWorld.getInstance().getCreator().CreatePosition(x,y);
                //FavoritesStorage.defaultStorage.deleteItem();

                IPosition position= UI.runOnRenderThread(new Callable<IPosition>() {
                    @Override
                    public IPosition call() throws Exception {
                        return ISGWorld.getInstance().getCreator().CreatePosition(x,y);
                    }
                });
                FavoriteItem favoriteItem=new FavoriteItem();
                favoriteItem.icon=R.drawable.icon_rsfire;
                favoriteItem.showOn3D=true;
                favoriteItem.position=position;
                favoriteItem.name=messageList.get(i).getObservationDateTime()+" "+messageList.get(i).getFormattedAddress();
                StringBuilder sb=new StringBuilder();
                sb.append("监测时间："+messageList.get(i).getObservationDateTime());
                sb.append("\n");
                sb.append("发现方式:"+messageList.get(i).getSatellite());
                sb.append("\n");
                sb.append("可信度："+String.valueOf(messageList.get(i).getCredibility()));
                sb.append("\n");
                sb.append("面积:"+String.valueOf(messageList.get(i).getArea()));
                favoriteItem.desc=sb.toString();
                FavoritesStorage.defaultStorage.saveItem(favoriteItem);
            }

        }

    }
    private  void DeleteAllRsFire()
    {
        FavoriteItem[]fs= FavoritesStorage.defaultStorage.getAll();
        for(int i=0;i<fs.length;i++)
        {
//            if (fs[i].icon==R.drawable.icon_rsfire)
//            {
//
//            }
            FavoritesStorage.defaultStorage.deleteItem(fs[i].id);
        }
    }


}
