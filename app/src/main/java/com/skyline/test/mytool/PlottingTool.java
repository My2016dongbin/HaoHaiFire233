package com.skyline.test.mytool;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.IColor;
import com.skyline.teapi.IGeometry;
import com.skyline.teapi.ILineString;
import com.skyline.teapi.ILinearRing;
import com.skyline.teapi.IMouseInfo;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ITerrainArrow;
import com.skyline.teapi.ITerrainImageLabel;
import com.skyline.teapi.ITerrainLabel;
import com.skyline.teapi.ITerrainPolygon;
import com.skyline.teapi.ITerrainPolyline;
import com.skyline.teapi.IWorldPointInfo;
import com.skyline.teapi.ItemCode;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.TEImageHelper;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.ProgressTool;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.TEView;
import com.skyline.terraexplorer.views.ToolContainer;
import com.skyline.test.entity.PlottingItem;
import com.skyline.test.entity.PointLatLng;
import com.skyline.test.myview.MyModalDialog;
import com.skyline.test.socket.NettyClient;
import com.skyline.test.utils.Constance;
import com.skyline.test.utils.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import yuku.ambilwarna.AmbilWarnaView;

/**
 * Created by Yellow on 2017-05-08.
 */
public class PlottingTool extends ProgressTool implements ISGWorld.OnLButtonDownListener,ISGWorld.OnFrameListener,ModalDialog.ModalDialogDelegate,MyModalDialog.MyModalDialogDelegate, TEView.OnLongPressListener {
    private String operateType = "";
    private List<PointLatLng> connPoints = new ArrayList<PointLatLng>();//连接点
    ITerrainPolyline drawPolyline = null;
    ITerrainPolygon drawPolygon = null;
    ITerrainArrow draw2DArrow = null;

    ITerrainLabel label = null;
    ITerrainImageLabel imageLabel=null;
    String mColor;
    String textLable = "";
    String name="";
    String phoneNumber="";

    private static final String TAG = "PlottingTool";


    //
    String lineStyle="";

    public List<PointLatLng> getPoints() {
        return Points;
    }

    public void setPoints(List<PointLatLng> points) {
        Points = points;
        Generate();
    }
    private List<PointLatLng> Points;

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.mm_plottool, R.drawable.whiteboard, 20);
    }

    @Override
    public boolean onBeforeOpenToolContainer() {

        super.onBeforeOpenToolContainer();
        showNormalButtons();
        mColor = getLastUsedColor();
        //添加长按删除
        UI.getTEView().addOnLongPressListener(PlottingTool.this);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                ISGWorld.getInstance().addOnLButtonDownListener(PlottingTool.this);
                ISGWorld.getInstance().addOnFrameListener(PlottingTool.this);
            }
        });
        phoneNumber= SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getString(Constance.USER_PhoneNumber);
        //2018-01-30
        //getMyClient().connect();
        //
        return true;
    }

    @Override
    public boolean onBeforeCloseToolContainer(ToolContainer.CloseReason closeReason) {
        super.onBeforeCloseToolContainer(closeReason);
        UI.getTEView().removeOnLongPressListener(PlottingTool.this);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                ISGWorld.getInstance().removeOnLButtonDownListener(PlottingTool.this);
                ISGWorld.getInstance().removeOnFrameListener(PlottingTool.this);
            }
        });
        operateType = "";

        return true;
    }

    @Override
    protected void doWork() {

    }

    @Override
    protected void showNormalButtons() {
        toolContainer.removeButtons();
        toolContainer.setUpperViewHidden(true);
        toolContainer.addButton(1, R.drawable.add_polyline, R.string.mm_plottool_drawpolyline);
        toolContainer.addButton(2, R.drawable.add_polygon, R.string.mm_plottool_drawpolygon);
        toolContainer.addButton(3, R.drawable.add_arrow, R.string.mm_plottool_drawarrow);
        toolContainer.addButton(4, R.drawable.label, R.string.mm_plottool_textlable);
        toolContainer.addButton(5,R.drawable.fire,"火点");
        toolContainer.addButton(6, R.drawable.color, R.string.mm_plottool_color);
        toolContainer.addButton(7, R.drawable.delete, R.string.mm_plottool_deleteplot);
    }


    @Override
    protected void workCompleted() {
    }

    /**
     * 发送内容
     * @param id
     * @param plotName
     * @param Content
     */
    private  void sendContent(int id,String plotName,String Content)
    {
        if (!NettyClient.getInstance().getConnectStatus()) {
            com.blankj.utilcode.util.ToastUtils.showShort("当前服务器不可用");
          return;
        }
        com.bean.PlottingItem.PlottingBean.PlottingSend.Builder plottingSend= com.bean.PlottingItem.PlottingBean.PlottingSend.newBuilder();
        com.bean.PlottingItem.PlottingBean.PlottingContent.Builder plottingContent= com.bean.PlottingItem.PlottingBean.PlottingContent.newBuilder();
        plottingSend.setCmd(id);
        plottingContent.setContent(Content);
        plottingContent.setFillcolor(mColor);
        plottingContent.setLinecolor(mColor);
        plottingContent.setPhoneNumber(phoneNumber);

        plottingContent.setName(plotName);
        for (int i=0;i<Points.size();i++)
        {
           com.bean.PlottingItem.PlottingBean.PointLonLat.Builder PointLonLat=  com.bean.PlottingItem.PlottingBean.PointLonLat.newBuilder();
            PointLonLat.setLon(Points.get(i).getLng());
            PointLonLat.setLat(Points.get(i).getLat());
            PointLonLat.setAlt(0);
            plottingContent.addPoints( PointLonLat.build());
        }

        plottingSend.setPlottingContent(plottingContent.build());
        if(!NettyClient.getInstance().getConnectStatus())
        {
           ToastUtils.showShort("连接服务失败");

            return;
        }
        NettyClient.getInstance().sendMsgToServer(Protocol.generateSend(2,plottingSend.build().toByteString()), new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {                //4
                    Log.d(TAG, "operationComplete: write message successful");
                } else {
                    Log.d(TAG, "operationComplete: write message failed");

                }
            }
        });
    }
    /**
     * 上传数据
     * @param type
     */
    private void uPdate(String type,String plotName)
    {

        if (!NettyClient.getInstance().getConnectStatus()) {
            NettyClient.getInstance().connect();
        }
        PlottingItem plottingItem=new PlottingItem();
        plottingItem.setPoints(connPoints);
        plottingItem.setType(type);
        plottingItem.setLineStyle("0xFF0000FF");
        String id=UUID.randomUUID().toString();
        plottingItem.setId(id);
        plottingItem.setLineColor(mColor);
        plottingItem.setName(plotName);
        plottingItem.setLableContent(textLable);
        Gson gson=new Gson();
        String content=gson.toJson(plottingItem);
        Log.i(TAG, "uPdate: "+content);
        //发送数据




    }

    @Override
    public void onButtonClick(int tag) {
        switch (tag) {
            case 1://画线

                if (toolContainer.getButtonName(tag).equals("停止")) {
                    //添加上传 2018-01-30
                   String pid= UI.runOnRenderThread(new Callable<String>() {

                        @Override
                        public String call() throws Exception {
                            return drawPolyline.getID();
                        }
                    });
                   // uPdate("line",pid);
                    sendContent(10124,"线","");
                    cleanOperateType();
                    toolContainer.updateButton(tag, R.drawable.add_polyline, R.string.mm_plottool_drawpolyline);
                } else {
                    cleanOperateType();
                    operateType = Constance.drawPolyline;
                    toolContainer.updateButton(tag, R.drawable.add_polyline, R.string.mm_plottool_stop);
                }
                break;
            case 2://画面

                if (toolContainer.getButtonName(tag).equals("停止")) {

                    String  pid=UI.runOnRenderThread(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            return drawPolygon.getID();
                        }
                    });
                    sendContent(10125,pid,"");
                   // uPdate("polygon",pid);
                    cleanOperateType();
                    toolContainer.updateButton(tag, R.drawable.add_polygon, R.string.mm_plottool_drawpolygon);
                } else {
                    cleanOperateType();
                    operateType = Constance.drawPolygon;
                    toolContainer.updateButton(tag, R.drawable.add_polygon, R.string.mm_plottool_stop);
                }
                break;
            case 3://箭头
                //cleanOperateType();
                initButton();
                operateType = Constance.draw2DArrow;
                break;
            case 4://文本
                initButton();
                setTextLableContent();
                break;
            case 5://火点
                initButton();
                operateType = Constance.P_FirePosition;
                break;
            case 6:
                showColorEdit();
                break;
            case 7:
                deleteAllPlot();
                break;
        }
        super.onButtonClick(tag);
    }

    /**
     * 把之前button回复到原状
     */
    private void initButton()
    {
        toolContainer.updateButton(1, R.drawable.add_polyline, R.string.mm_plottool_drawpolyline);
        toolContainer.updateButton(2, R.drawable.add_polygon, R.string.mm_plottool_drawpolygon);
        cleanOperateType();
    }

    @Override
    public boolean OnLButtonDown(int Flags, int X, int Y) {
        IWorldPointInfo worldPointInfo = ISGWorld.getInstance().getWindow().PixelToWorld(X, Y);
        IPosition downPosition = worldPointInfo.getPosition();
        if (operateType.isEmpty()) {
            return false;
        }
        if (operateType == Constance.drawPolyline) {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
        }
        if (operateType == Constance.drawPolygon) {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
        }
        if (operateType == Constance.draw2DArrow) {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
            if (connPoints.size() > 1) {
                cleanOperateType();
            }
        }
        if (operateType == Constance.drawTextLable) {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
            if (connPoints.size() > 0) {
                cleanOperateType();
            }
        }
        if (operateType==Constance.P_FirePosition)
        {
            connPoints.add(new PointLatLng(downPosition.getY(),downPosition.getX()));
            this.setPoints(connPoints);
            if (connPoints.size()>0)
            {
                cleanOperateType();
            }
        }
        return false;
    }

    @Override
    public void OnFrame() {
        IMouseInfo mouseInfo = ISGWorld.getInstance().getWindow().GetMouseInfo();
        IWorldPointInfo worldPointInfo = ISGWorld.getInstance().getWindow().PixelToWorld(mouseInfo.getX(), mouseInfo.getY());
        IPosition framePosition = worldPointInfo.getPosition();
        if (framePosition == null) {
            return;
        }
        if (operateType == Constance.drawPolyline) {
            if (connPoints.size() < 1) {
                return;
            }
            if (distance(new PointLatLng(framePosition.getY(), framePosition.getX()), new PointLatLng(this.connPoints.get(connPoints.size() - 1).getLat(), this.connPoints.get(connPoints.size() - 1).getLng())) < 0.001) {
                return;
            }
            List<PointLatLng> oPoints = new ArrayList<PointLatLng>(this.connPoints);
            oPoints.add(new PointLatLng(framePosition.getY(), framePosition.getX()));
            this.Points = oPoints;
        }
        if (operateType == Constance.draw2DArrow) {
            if (connPoints.size() < 1) {
                return;
            }
            if (distance(new PointLatLng(framePosition.getY(), framePosition.getX()), new PointLatLng(this.connPoints.get(connPoints.size() - 1).getLat(), this.connPoints.get(connPoints.size() - 1).getLng())) < 0.001) {
                return;
            }
            List<PointLatLng> oPoints = new ArrayList<PointLatLng>(this.connPoints);
            oPoints.add(new PointLatLng(framePosition.getY(), framePosition.getX()));
            this.Points = oPoints;
        }
    }

    /***
     *
     */
    private void Generate() {
        if (operateType.isEmpty()) {
            return;
        }
        if (operateType == Constance.drawPolyline) {
            if (this.getPoints().size() < 2) {
                return;
            }
            if (this.getPoints().size() == 2) {

                setLine(this.getPoints());
            }
            if (this.getPoints().size() > 2) {
                setLine(this.getPoints());
            }
        }
        if (operateType == Constance.drawPolygon) {
            if (this.getPoints().size() < 2) {
                return;
            }
            if (this.getPoints().size() > 2) {
                setPolygon(this.getPoints());
            }
        }
        if (operateType == Constance.draw2DArrow) {
            set2DArrow(this.getPoints());
        }
        if (operateType == Constance.drawTextLable) {
            setTextLable(this.getPoints());
        }
        if (operateType==Constance.P_FirePosition)
        {
            setFireAlarm(this.getPoints());
        }

    }

    /***
     * 清除操作
     */
    private void cleanOperateType() {
        operateType = "";
        connPoints.clear();
        drawPolyline = null;
        drawPolygon = null;
        draw2DArrow = null;
        label = null;
        textLable = "";
        imageLabel=null;
        name="";
    }

    /***
     * 计算距离
     * @param pnt1
     * @param pnt2
     * @return
     */
    private double distance(PointLatLng pnt1, PointLatLng pnt2) {
        return Math.sqrt(Math.pow((pnt1.getLat() - pnt2.getLat()), 2) + Math.pow((pnt1.getLng() - pnt2.getLng()), 2));
    }

    String testName="";
    /**
     * 线
     *
     * @param points
     */
    private void setLine(List<PointLatLng> points) {

        List<Double> mList = new ArrayList<Double>();
        for (int i = 0; i < points.size(); i++) {
            mList.add(points.get(i).getLng());
            mList.add(points.get(i).getLat());
            mList.add(1.1);
        }
        final double[] num = new double[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            num[i] = mList.get(i).doubleValue();
        }
        name=UUID.randomUUID().toString();
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if (drawPolyline != null) {
                    ISGWorld.getInstance().getCreator().DeleteObject(drawPolyline.getID());
                }
                ILineString lineString = ISGWorld.getInstance().getCreator().getGeometryCreator().CreateLineStringGeometry(num);
                String groupId= ISGWorld.getInstance().getProjectTree().FindItem(Constance.groupName);
                drawPolyline = ISGWorld.getInstance().getCreator().CreatePolyline(lineString.CastTo(IGeometry.class), Color.RED, AltitudeTypeCode.ATC_ON_TERRAIN,groupId,name);
//                IColor color = ISGWorld.getInstance().getCreator().CreateColor(255, 0, 0);
                IColor color=getColor(Integer.parseInt(mColor) );
                drawPolyline.getLineStyle().setPattern(0xFF0000FF);
                drawPolyline.getLineStyle().setColor(color);

                //2018-03-08 测试用
                //testName=drawPolyline.getID();

            }
        });
    }

    /***
     * 颜色转换存在问题
     * 颜色转换已经更改 2017-5-31
     * @param argb
     * @return
     */
    private IColor getColor(int argb)
    {
        int cv = argb;
        int b = (cv >> 16) & 0xFF;
        int g = (cv >> 8) & 0xFF;
        int r = (cv >> 0) & 0xFF;
        IColor color= ISGWorld.getInstance().getCreator().CreateColor(r,g,b);
        return  color;
    }
    /***
     * 面
     * @param points
     */
    private void setPolygon(List<PointLatLng> points) {
        List<Double> mList = new ArrayList<Double>();
        for (int i = 0; i < points.size(); i++) {
            mList.add(points.get(i).getLng());
            mList.add(points.get(i).getLat());
            mList.add(1.1);
        }

        final double[] num = new double[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            num[i] = mList.get(i).doubleValue();
        }
        name=UUID.randomUUID().toString();
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if (drawPolygon != null) {
                    ISGWorld.getInstance().getCreator().DeleteObject(drawPolygon.getID());
                }
                String groupId= ISGWorld.getInstance().getProjectTree().FindItem(Constance.groupName);
                ILinearRing linearRing = ISGWorld.getInstance().getCreator().getGeometryCreator().CreateLinearRingGeometry(num);
                drawPolygon = ISGWorld.getInstance().getCreator().CreatePolygon(linearRing.CastTo(IGeometry.class), Color.RED, Color.RED, AltitudeTypeCode.ATC_ON_TERRAIN,groupId,name);
                IColor color=getColor(Integer.parseInt(mColor) );
//                IColor color = ISGWorld.getInstance().getCreator().CreateColor(255, 0, 0);
                drawPolygon.getFillStyle().setColor(color);
                drawPolygon.getLineStyle().setColor(color);
                drawPolygon.getFillStyle().getColor().SetAlpha(0.5);
            }
        });

    }

    /***
     * 二维箭头
     * @param points
     */
    private void set2DArrow(final List<PointLatLng> points) {
        if (points.size() < 2) {
            return;
        }
        name=UUID.randomUUID().toString();
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if (draw2DArrow != null) {
                    ISGWorld.getInstance().getCreator().DeleteObject(draw2DArrow.getID());
                }
                IPosition p1 = ISGWorld.getInstance().getCreator().CreatePosition(points.get(0).getLng(), points.get(0).getLat());
                IPosition p2 = ISGWorld.getInstance().getCreator().CreatePosition(points.get(1).getLng(), points.get(1).getLat());
                double d = p1.DistanceTo(p2);
                IPosition temPosition = p1.AimTo(p2);
                String groupId= ISGWorld.getInstance().getProjectTree().FindItem(Constance.groupName);
                IPosition newPosition = ISGWorld.getInstance().getCreator().CreatePosition(p2.getX(), p2.getY(), 0, AltitudeTypeCode.ATC_ON_TERRAIN, temPosition.getYaw(), temPosition.getPitch());
                draw2DArrow = ISGWorld.getInstance().getCreator().CreateArrow(newPosition, d, 4, Color.RED, Color.RED,groupId,name);
                IColor color=getColor(Integer.parseInt(mColor) );
//                IColor color = ISGWorld.getInstance().getCreator().CreateColor(255, 0, 0);
                draw2DArrow.getLineStyle().setColor(color);
                draw2DArrow.getFillStyle().setColor(color);
                draw2DArrow.getFillStyle().getColor().SetAlpha(0.7);
                draw2DArrow.setStyle(3);

            }
        });
        String pid=UI.runOnRenderThread(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return draw2DArrow.getID();
            }
        });
        sendContent(10128,pid,"");
       // uPdate("arrow",pid);
    }

    /***
     * 设置文本内容
     */
    private void setTextLableContent() {
        convertor = new Converter() {
            @Override
            public String convertValueToString(ModalDialog dlg) {
                return dlg.getTextField().getText().toString();
            }
        };
        ModalDialog dlg = new ModalDialog(R.string.mm_plottool_texttitle, this);
        dlg.setContentTextField(InputType.TYPE_CLASS_TEXT, "");
        dlg.show();
    }

    /***
     * 设置文本标签
     * @param points
     */
    private void setTextLable(final List<PointLatLng> points) {

        name=UUID.randomUUID().toString();
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                String groupId= ISGWorld.getInstance().getProjectTree().FindItem(Constance.groupName);
                IPosition position = ISGWorld.getInstance().getCreator().CreatePosition(points.get(0).getLng(), points.get(0).getLat(), 0, AltitudeTypeCode.ATC_ON_TERRAIN);
                label = ISGWorld.getInstance().getCreator().CreateLabel(position, textLable, null,null,groupId,name);
                IColor color=getColor(Integer.parseInt(mColor) );
                label.getStyle().setTextColor(color);
                label.getStyle().setFontSize(Constance.textFontSize);
            }
        });
        String pid=UI.runOnRenderThread(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return label.getID();
            }
        });
        sendContent(10120,pid,textLable);
        //uPdate("lable",pid);
    }

    /**
     * 设置火点位置
     * @param points
     */
    private  void setFireAlarm(final  List<PointLatLng> points)
    {
        final String fileName= TEImageHelper.prepareImageForTE(R.drawable.fire);

        name=UUID.randomUUID().toString();
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                String groupId= ISGWorld.getInstance().getProjectTree().FindItem(Constance.groupName);
                IPosition position = ISGWorld.getInstance().getCreator().CreatePosition(points.get(0).getLng(), points.get(0).getLat(), 0, AltitudeTypeCode.ATC_ON_TERRAIN);
                imageLabel = ISGWorld.getInstance().getCreator().CreateImageLabel(position,fileName,null,groupId,name);
            }
        });
        String pid=UI.runOnRenderThread(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return imageLabel.getID();
            }
        });
        sendContent(10121,pid,"");
    }

    /***
     * 显示颜色选择器
     */
    private void showColorEdit() {
        int cv = Integer.parseInt(mColor);
        int b = (cv >> 16) & 0xFF;
        int g = (cv >> 8) & 0xFF;
        int r = (cv >> 0) & 0xFF;
        final AmbilWarnaView colorPicker = new AmbilWarnaView(TEApp.getAppContext(), Color.argb(255, r, g, b));
         myConverter= new MyConverter() {
             @Override
             public String convertValueToString(MyModalDialog dlg) {

                     int color = colorPicker.getColor();
                     int red = (color >> 16) & 0xFF;
                     int green = (color >> 8) & 0xFF;
                     int blue = (color >> 0) & 0xFF;
                     int selectedColorValue = blue * (256 * 256) + green * (256) + red;
                     String selectedColor = String.valueOf(selectedColorValue);
                     setLastUsedColor(selectedColor);
                     return selectedColor;
             }

        };
        MyModalDialog dlg = new MyModalDialog(R.string.mm_plottool_selectcolor, this);
        dlg.setContent(colorPicker.getRootView());
        dlg.show();
    }

    private void setLastUsedColor(String color) {
        TEApp.getAppContext().getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("com.skyline.terraexplorer.whiteboard.last_used_color", color)
                .apply();
    }

    private String getLastUsedColor() {
        return TEApp.getAppContext().getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString("com.skyline.terraexplorer.whiteboard.last_used_color", "16777215" /* white color */);
    }

    private Converter convertor;

    @Override
    public void onLongPress(MotionEvent ev) {
       final Point coords = new Point((int)ev.getX(), (int)ev.getY());
        String pid=UI.runOnRenderThread(new Callable<String>() {

            @Override
            public String call() throws Exception {
                IWorldPointInfo worldPointInfo= ISGWorld.getInstance().getWindow().PixelToWorld(coords.x,coords.y);
                if (worldPointInfo.getObjectID().isEmpty()||worldPointInfo.getObjectID()==null)
                {
                    return null;
                }
                else
                {
                    return worldPointInfo.getObjectID();
                }
            }
        });
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                IWorldPointInfo worldPointInfo= ISGWorld.getInstance().getWindow().PixelToWorld(coords.x,coords.y);
                if (worldPointInfo.getObjectID().isEmpty()||worldPointInfo.getObjectID()==null)
                {
                    return;
                }
                ISGWorld.getInstance().getCreator().DeleteObject(worldPointInfo.getObjectID());
            }
        });
        if (pid!=null) {
           // uPdate("delete", pid);
            sendContent(10000,pid,"");
        }

    }
    private interface Converter {
        String convertValueToString(ModalDialog dlg);
    }
    private MyConverter myConverter;
    private interface MyConverter
    {
        String convertValueToString(MyModalDialog dlg);
    }

    @Override
    public void mymodalDialogDidDismissWithOk(MyModalDialog dlg) {

        mColor=myConverter.convertValueToString(dlg);
        myConverter=null;
    }

    @Override
    public void mymodalDialogDidDismissWithCancel(MyModalDialog dlg) {


    }


    @Override
    public void modalDialogDidDismissWithOk(ModalDialog dlg) {
        textLable=convertor.convertValueToString(dlg);

        if (!textLable.isEmpty())
        {
            operateType=Constance.drawTextLable;
        }
        convertor=null;

    }

    @Override
    public void modalDialogDidDismissWithCancel(ModalDialog dlg) {

    }

    /**
     * 删除所有标绘
     */
    private  void deleteAllPlot()
    {
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                String groupId= ISGWorld.getInstance().getProjectTree().FindItem(Constance.groupName);
                String ss= ISGWorld.getInstance().getProjectTree().GetNextItem(groupId, ItemCode.CHILD);
                List<String> mListID = new ArrayList<String>();
                while (!ss.isEmpty())
                {
                    mListID.add(ss);
                    ss = ISGWorld.getInstance().getProjectTree().GetNextItem(ss, ItemCode.NEXT);
                }
                if (mListID == null)
                {
                    return;
                }
                for (int i = 0; i < mListID.size(); i++)
                {
                    ISGWorld.getInstance().getProjectTree().DeleteItem(mListID.get(i));
                }
            }
        });

    }
}
