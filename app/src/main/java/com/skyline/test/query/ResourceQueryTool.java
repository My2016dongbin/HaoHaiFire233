package com.skyline.test.query;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.IGeometry;
import com.skyline.teapi.IPoint;
import com.skyline.teapi.IPolygon;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ISpatialRelation;
import com.skyline.teapi.ITerrainPolygon;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.BaseToolWithContainer;
import com.skyline.terraexplorer.views.ToolContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2017/5/18 10:57.
 */

public class ResourceQueryTool extends BaseToolWithContainer implements ISGWorld.OnLButtonUpListener {

    private static final String TAG = "ResourceQueryTool";
    private String toolContainerText;
    private List<String> list = new ArrayList<String>();

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.resource_query, R.drawable.tool_resource_query, MenuEntry.MenuEntrySearchResource(), 100);
    }

    @Override
    public boolean onBeforeOpenToolContainer() {
        super.onBeforeOpenToolContainer();
        //初始化按钮
        showNormalButtons();

        //更新数据
        updateArea(list);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                // start draw polygon 开始绘制多边形
                startDrawPolygon();
                // subscribe to lButtonUp as an event that causes the polygon to change 订阅lButtonUp作为导致多边形改变的事件
                ISGWorld.getInstance().addOnLButtonUpListener(ResourceQueryTool.this);
            }
        });

        //获取收藏中的点
//        loadData();

        return true;
    }


    private void loadData() {

        UI.runOnRenderThread(new Runnable() {
            private ArrayList<IPoint> iPoints;

            @Override
            public void run() {
                for (FavoriteItem fav : FavoritesStorage.defaultStorage.getAll()) {
                    final IPosition position = fav.position;
                    IPoint point = ISGWorld.getInstance().getCreator().getGeometryCreator().CreatePointGeometry(new double[]{position.getX(), position.getY(), 0});
                    iPoints.add(point);
                }
                UI.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (IPoint point : iPoints) {
                            ISpatialRelation spatialRelation = point.getSpatialRelation();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onBeforeCloseToolContainer(ToolContainer.CloseReason closeReason) {
        super.onBeforeCloseToolContainer(closeReason);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                String objectId = (String) ISGWorld.getInstance().GetParam(7200);
                // simulate right click to end drawing 模拟右键单击结束绘图
                ISGWorld.getInstance().SetParam(8044, 0);
                ISGWorld.getInstance().getCreator().DeleteObject(objectId); //删除线
                ISGWorld.getInstance().removeOnLButtonUpListener(ResourceQueryTool.this);    //取消绘制事件
            }
        });
        return true;
    }

    @Override
    public void onButtonClick(int tag) {
        super.onButtonClick(tag);
        switch (tag) {
            case 3: { // delete all points 删除所有点
                updateArea(list);
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
                        // simulate right click to end drawing 模拟右键单击结束绘图
                        ISGWorld.getInstance().SetParam(8044, 0);
                        // delete object    删除对象
                        ISGWorld.getInstance().getCreator().DeleteObject(objectId);
                        // and start adding again 并重新开始绘制
                        startDrawPolygon();
                    }
                });
                ResourceData.infos = null;
                break;
            }
            case 4: // 包含点的列表
                Intent intent = new Intent(TEApp.getCurrentActivityContext(), ResourceQueryActivity1.class);
                if (ResourceData.infos == null) {
                    Toast.makeText(TEApp.getCurrentActivityContext(), "没有数据", Toast.LENGTH_SHORT).show();
                } else {
                    TEApp.getCurrentActivityContext().startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void showNormalButtons() {
        toolContainer.removeButtons();
        toolContainer.setText("新的");
        toolContainer.addButton(3, R.drawable.delete,R.string.searchresource_tool_delete);
        toolContainer.addButton(4, R.drawable.list,R.string.searchresource_tool_list);
    }

    /**
     * 开始绘制多边形
     */
    private void startDrawPolygon() {
        // start draw polygon 开始绘制多边形
        ISGWorld.getInstance().getCommand().Execute(1012, 5);

        // get object 获取对象
        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
        ITerrainPolygon areaPolygon = ISGWorld.getInstance().getCreator().GetObject(objectId).CastTo(ITerrainPolygon.class);
        if (areaPolygon != null) {
            areaPolygon.getPosition().setAltitudeType(AltitudeTypeCode.ATC_TERRAIN_RELATIVE);
            Log.d(TAG, "startDrawPolygon: position = " + areaPolygon.getPosition().getX() + "," + areaPolygon.getPosition().getY());
            areaPolygon.SetParam(5440, null);    // Give the polygon X-Ray look 给出了多边形x射线
            areaPolygon.SetParam(5441, null);    // // Make sure we do not see the red "edit vertex helper polyline" 确保我们看不到红色的“编辑顶点帮助器折线”
            areaPolygon.getLineStyle().setWidth(-2.0);  // Make the polygon a bit wider 使多边形更宽一点
        }
    }


    /**
     * 更新容器中的文字
     * 这里应该是传入一个列表，里面保存着框选中的地理信息，像地点一样
     */
    private void updateArea(final List<String> list) {
        UI.runOnUiThreadAsync(new Runnable() {
            @Override
            public void run() {
                if (list.size() == 0) {
                    toolContainerText = "";
                } else {
                    toolContainerText = "测试文本";
                }
                toolContainer.setText(toolContainerText);
            }
        });
    }

    /**
     * 更新容器中的文字
     * 这里应该是传入一个列表，里面保存着框选中的地理信息，像地点一样
     */
    private void updateArea(final String text, ArrayList<ResourceInfo> infos) {
        ResourceData.infos = infos;
        UI.runOnUiThreadAsync(new Runnable() {
            @Override
            public void run() {
                if (text == null) {
                    toolContainerText = "";
                } else {
                    toolContainerText = text;
                }
                toolContainer.setText(toolContainerText);
            }
        });
    }


    @Override
    public boolean OnLButtonUp(int Flags, int X, int Y) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {//延时执行
            @Override
            public void run() {
                UI.runOnRenderThread(new Runnable() {   //渲染线程
                    @Override
                    public void run() {
                        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
                        ITerrainPolygon areaPolygon = ISGWorld.getInstance().getCreator().GetObject(objectId).CastTo(ITerrainPolygon.class);//面积
                        IPolygon poly = areaPolygon.getGeometry().CastTo(IPolygon.class);   //周长

                        IGeometry geometry = areaPolygon.getGeometry(); //求关系用的
                        ArrayList<IPoint> iPoints;
                        int i = 0;  //判断有几个点
                        ArrayList<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
                        if (poly != null) {
                            /*double area = (Double) areaPolygon.GetParam(5430);  //面积
                            double perimeter = poly.getExteriorRing().getLength();  //周长*/
                            for (FavoriteItem fav : FavoritesStorage.defaultStorage.getAll()) {
                                final IPosition position = fav.position;
                                IPoint point = ISGWorld.getInstance().getCreator().getGeometryCreator().CreatePointGeometry(new double[]{position.getX(), position.getY(), 0});
                                ISpatialRelation spatialRelation = point.getSpatialRelation();
                                boolean within = spatialRelation.Within(geometry);
                                if (within) {
                                    i = i + 1;

//                                    resourceInfos.add(new ResourceInfo(fav.name, fav.position));
                                    resourceInfos.add(new ResourceInfo(fav.name,fav.position,fav.desc,fav.icon));
                                }
                            }
                            String num = "您选中了" + i + "个资源点:\n";
                            StringBuffer name = new StringBuffer("");
                            for (ResourceInfo resourceInfo : resourceInfos) {
                                name.append(resourceInfo.getName() + "\n");
                            }

                            //这里重新获取list
                            String inputText = num + name.toString();
                            updateArea(inputText, resourceInfos);
                        }
                    }
                });
            }
        }, 10);
        return false;
    }
}
