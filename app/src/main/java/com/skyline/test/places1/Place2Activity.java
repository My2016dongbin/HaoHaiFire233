package com.skyline.test.places1;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.skyline.terraexplorer.R;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.terraexplorer.models.ToolManager;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.EditFavoriteTool;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;
import java.util.ArrayList;
import java.util.EnumSet;

public class Place2Activity extends MatchParentActivity {
    private SlideAndDragListView slideAndDragListView;
    private Places1Adapter places1Adapter;
    private ArrayList<Places1Info> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place2);
        UI.addHeader(R.string.title_activity_places,R.drawable.places,this, EnumSet.of(UI.HeaderOptions.SearchButton));
        initView();
        initData();
    }
    private  void initView()
    {
        slideAndDragListView=(SlideAndDragListView)findViewById(R.id.slideAndDragListView_place);
        Menu menu=new Menu(false,0);

        menu.addItem(new MenuItem.Builder().setWidth(120).setBackground(new ColorDrawable(Color.RED)).setText("删除").setDirection(MenuItem.DIRECTION_RIGHT).setTextColor(Color.BLUE).build());
        menu.addItem(new MenuItem.Builder().setWidth(120).setText("编辑").setBackground(new ColorDrawable(Color.BLUE)).setDirection(MenuItem.DIRECTION_RIGHT).setTextColor(Color.BLUE).build());
        menu.addItem(new MenuItem.Builder().setWidth(120).setBackground(new ColorDrawable(Color.GRAY)).setText("定位").setDirection(MenuItem.DIRECTION_RIGHT).setTextColor(Color.BLUE).build());

        slideAndDragListView.setMenu(menu);
    }
    private  void initData()
    {
        new DownloadTask().execute();

    }
    class DownloadTask extends AsyncTask<Void,Integer,Object>
    {

        @Override
        protected Object doInBackground(Void... voids) {
            if (infos!=null)
            {
                infos.clear();
            }
            infos=new ArrayList<Places1Info>();
            for (FavoriteItem fav : FavoritesStorage.defaultStorage.getAll()) {
                IPosition position = fav.position;
                String name = fav.name;
                String id = fav.id;
                infos.add(new Places1Info(position, id, name));
            }
            return infos;

        }

        @Override
        protected void onPostExecute(Object o) {
            final ArrayList<Places1Info>places1Infos=(ArrayList<Places1Info>)o;
            places1Adapter=new Places1Adapter(Place2Activity.this,places1Infos);
            slideAndDragListView.setAdapter(places1Adapter);
            slideAndDragListView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
                @Override
                public int onMenuItemClick(View view, final int itemPosition, int buttonPosition, int direction) {
                    switch (direction)
                    {
                        case MenuItem.DIRECTION_RIGHT:
                            switch (buttonPosition)
                            {
                                case 0:
                                    FavoritesStorage.defaultStorage.deleteItem(places1Infos.get(itemPosition).getId());
                                    initData();
                                    return  Menu.ITEM_SCROLL_BACK;
                                case 1:
                                    ToolManager.INSTANCE.openTool(EditFavoriteTool.class.getName(), places1Infos.get(itemPosition).getId());
                                    return  Menu.ITEM_SCROLL_BACK;
                                case 2:
                                    UI.runOnRenderThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            IPosition pp=places1Infos.get(itemPosition).getPosition();
                                            ISGWorld.getInstance().getNavigate().FlyTo(pp);
                                            finish();
                                        }
                                    });
                                    return  Menu.ITEM_SCROLL_BACK;
                            }
                            break;
                        default:
                            return Menu.ITEM_NOTHING;
                    }
                    return Menu.ITEM_NOTHING;
                }
            });
            super.onPostExecute(o);
        }
    }
}
