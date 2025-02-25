/*
package com.haohai.haohai.address.arg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import com.haohai.haohai.address.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.esri.arcgisruntime.mapping.ArcGISScene;

public class ArcGisActivity extends BaseActivity {

    private MapView mMapView;
    private SceneView mSceneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_gis);

        mMapView = (MapView) findViewById(R.id.mapview);
       */
/* mSceneView = (SceneView) findViewById(R.id.sceneview);

        ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 34.056295, -117.195800, 16);
        // Initialize the portal with ArcGIS Online
        Portal portal = new Portal("http://www.arcgis.com");


// Get the portal item
        PortalItem portalItem = new PortalItem(portal, "31874da8a16d45bfbc1273422f772270");


// Create the scene from the portal item
        //ArcGISScene scene3d = new ArcGISScene(portalItem);

       ArcGISScene scene3d = new ArcGISScene("https://www.arcgis.com/home/item.html?id=31874da8a16d45bfbc1273422f772270");
        ArcGISScene arcGISScene = new ArcGISScene();
        mSceneView.setScene(arcGISScene);
        mMapView.setMap(map);*//*

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
        mSceneView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
        mSceneView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
        mSceneView.dispose();
    }
}
*/
