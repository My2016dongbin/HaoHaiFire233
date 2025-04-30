var Cesium = window.Cesium
var viewer = window.viewer
var erea = '100';
var zhongshanpark;
var ifgo=false;
var parkname;
var parkid;
var parkarea;
var parkownership;
var parkaddress;
var parkimageFile;
var parkintentid;
var sourceNameArray = [];
var arrPoint = {};
var URL_CONFIG = window.URL_CONFIG
let STATE_TIANDITU_CIA = false
let STATE_TIANDITU_IMG = false
let STATE_GOOGLE_MAP = false
let STATE_ISERVER_MAP = false
let tidituBaseLayer = null
let tiandituLayerCIA = null
let tiandituLayerIMG = null
let googleLayer = null
const guijiMap = {};
var item2=""
var billboardConfig = () => ({
    scale: 0.7, // 原始大小的缩放比例
    horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
    verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
    heightReference: Cesium.HeightReference.CLAMP_TO_GROUND, // 贴地
    scaleByDistance: new Cesium.NearFarScalar(8.5e3, 1.2, 4e5, 0.16),
    distanceDisplayCondition: new Cesium.DistanceDisplayCondition(100, 29600000)
});
var labelConfig = () => ({
    font: 'normal small-caps normal 14px 楷体',
    style: Cesium.LabelStyle.FILL_AND_OUTLINE,
    fillColor: Cesium.Color.AZURE,
    outlineColor: Cesium.Color.BLACK,
    outlineWidth: 2,
    horizontalOrigin: Cesium.HorizontalOrigin.LEFT,
    verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
    pixelOffset: new Cesium.Cartesian2(10, 0), // 偏移量
    heightReference: Cesium.HeightReference.CLAMP_TO_GROUND, // 贴地
    distanceDisplayCondition: new Cesium.DistanceDisplayCondition(100, 10000)
})

var resourcetype="checkStation"
var dist=""
var type2=""
var listitem3=[{"id":"2020061515383178102208ee4cc660c",
    "name":"四川省阿坝藏族羌族自治州阿坝县河支乡阿两路",
    "resourceType":"fire_weixng",
    "position":{"lat":32.87999979,"lng":101.69999422,"z":0.0}},
    {"id":"202005251838554332149ebaf12d0bd",
        "name":"四川省阿坝藏族羌族自治州金川县毛日乡",
        "resourceType":"fire_weixng",
        "position":{"lat":31.610000000000003,"lng":101.61999967999999,"z":0.0}}]
var item1={"userId":"510865864568864768","position":{"lat":36.3029,"lng":120.3025}};
var list= [];
console.log(listitem3.length)
for (var i=0;i<listitem3.length;i++){
    console.log(listitem3[i])
    list.push(listitem3[i]);
}
console.log(list)
function createMap(id, config) {
  const defaultMapConfig = {
    animation: false,
    timeline: false,
    fullscreenButton: false,
    vrButton: false,
    geocoder: false,
    sceneModePicker: false,
    homeButton: false,
    navigationHelpButton: false,
    baseLayerPicker: false,
    selectionIndicator: false,// 设置绿色框不可见
      infoBox: false
  }
    window.viewer = new Cesium.Viewer(id, { ...defaultMapConfig, ...config })
    CesiumPopup();
  }
function onload(Cesium) {
    //初始化viewer部件

    const imageryLayers = viewer.imageryLayers
    const baseLayer = imageryLayers.get(1)
    //changeMapCIA();
    viewer.imageryLayers.addImageryProvider(new Cesium.ArcGisMapServerImageryProvider({	url: 'https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer' }));

}
if (typeof Cesium !== 'undefined') {
    window.startupCalled = true;
    createMap("cesiumContainer");
    onload(Cesium);
     flyToCenter();
     //showPointBillbordDataOnMap(resourcetype, list, dist, type2);
    LoadBaiduMapScript()
}
//动态引入百度地图SDK
function LoadBaiduMapScript() {
    console.log("初始化百度地图脚本...");
    const AK = "pex4Z1sjmhNQ7lCXyWXVmYbBDvtx5xAZ";
    const BMap_URL = "https://api.map.baidu.com/api?v=2.0&ak="+ AK +"&s=1&callback=onBMapCallback";
    return new Promise((resolve, reject) => {
        // 如果已加载直接返回
        if(typeof BMap !== "undefined") {
            resolve(BMap);
            return true;
        }
        // 百度地图异步加载回调处理
        window.onBMapCallback = function () {
            console.log("百度地图脚本初始化成功...");
            resolve(BMap);
        };
        // 插入script脚本
        let scriptNode = document.createElement("script");
        scriptNode.setAttribute("type", "text/javascript");
        scriptNode.setAttribute("src", BMap_URL);
        document.body.appendChild(scriptNode);
    });
    getGPS();
}
function getGPS() {
    console.log(111111111111)
    var map = new BMap.Map("bmapContainer");
    var point = new BMap.Point(116.331398,39.897445);
    map.centerAndZoom(point,12);

    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            var mk = new BMap.Marker(r.point);
            map.addOverlay(mk);
            map.panTo(r.point);
            alert('您的位置：'+r.point.lng+','+r.point.lat);
        }
        else {
            alert('failed'+this.getStatus());
        }
    });
}
// 切换到超图本地server端地图
function changeLocal() {
  viewer.terrainProvider = new Cesium.CesiumTerrainProvider({
    url: 'http://192.168.1.144:8090/iserver/services/3D-Qingdao/rest/realspace/datas/青岛市_高程@Qingdao',
    isSct: true// 地形服务源自SuperMap iServer发布时需设置isSct为true
  })

  const imageryLayers = viewer.imageryLayers
  const baseLayer = imageryLayers.get(1)
  const labelImagery = new Cesium.SuperMapImageryProvider({
    url: 'http://192.168.1.144:8090/iserver/services/3D-Qingdao/rest/realspace/datas/Qingdao_image@Qingdao'
  })
  imageryLayers.addImageryProvider(labelImagery)
  imageryLayers.remove(baseLayer)
}
 //县区界线
// viewer.dataSources.add(Cesium.GeoJsonDataSource.load('xianjie.json', {
//     stroke: Cesium.Color.WHITE,//设置多边形轮廓的默认颜色
//     fill: Cesium.Color.RED.withAlpha(0.0),//多边形的内部默认颜色
//     strokeWidth: 5,//轮廓的宽度
//     clamToGround: true//让地图贴地
// }));
// 移动中心点，便于观察效果
function flyToCenter(data) {
    const defaultCenter = {
        longitude: 120.3306728037,
        latitude: 36.1106233801,
        height: 40000
    }
    const center = Object.assign({}, defaultCenter, data)

    viewer.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(center.longitude, center.latitude, center.height), //经度、纬度、高度
        orientation: {
            heading: Cesium.Math.toRadians(359.2), //绕垂直于地心的轴旋转
            pitch: Cesium.Math.toRadians(-90), //绕纬度线旋转
            roll: Cesium.Math.toRadians(0) //绕经度线旋转
        },
        duration: 3 //动画持续时间
    });
}
viewer.scene.screenSpaceCameraController.minimumZoomDistance = 200;
viewer.scene.globe.depthTestAgainstTerrain = false;
//单击地图
/*var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
handler.setInputAction(event => {
    varpick = viewer.scene.pick(event.position);
    if(Cesium.defined(varpick)) {
        console.log(varpick.id.id)
        parkintentid=varpick.id.id;
        const idobj={id:varpick.id.id}
        $('#parkdetail').show();
        dsBridge.call("zhyl.giveidgetinfo", varpick.id.id, function (res) {
            var parkjson =JSON.parse(res);
            console.log(parkjson.data[0].createTime)
            var addressstr =parkjson.data[0].address;
            $('#parkname').html(parkjson.data[0].name);
            $('#parkqu').html("所属区："+parkjson.data[0].ownership=="null"?"":parkjson.data[0].ownership);
            $('#parkarea').html("面积："+parkjson.data[0].area);
            $('#parkadd').html("地址："+addressstr.substring(0,30));
            console.log(parkjson.data[0].imageFile)
            console.log(parkjson.data[0].imageFile!=null)
            if (parkjson.data[0].imageFile!=null){
                $('#parkimg').attr('src',parkjson.data[0].imageFile);
            }else {
                $('#parkimg').attr('src',"./ic_park.png");
            }
        });
    }else {
        $('#parkdetail').hide();
        var point = viewer.scene.pickPosition(event.position);
        var cartographic = Cesium.Cartographic.fromCartesian(point);
        var longitudeString = Cesium.Math.toDegrees(cartographic.longitude);
        var latitudeString = Cesium.Math.toDegrees(cartographic.latitude);
        var height = cartographic.height;
        //初始化百度地图
        var map = new BMap.Map("bmapContainer");
        var Bpoint = new BMap.Point(longitudeString, latitudeString);
        var gc = new BMap.Geocoder();
        gc.getLocation(Bpoint, function (rs) {
            var addComp = rs.addressComponents;
            if (addComp.district=="黄岛区"){
                district="西海岸新区"
            }else {
                district = addComp.district;
            }

            var street = addComp.street;
            var treenum = "80";
            console.log(district);
            //jqAlert.Confirm(addComp.city+","+district+","+street,treenum)
            //alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
            const data = {
                longitude: longitudeString,
                latitude: latitudeString,
                height: height,
                ifpark: ifgo,
                district: district
            }
            $('#pointinfo').show();
            $('#longitude').html("经度"+longitudeString);
            $('#latitude').html("纬度"+latitudeString);
            $('#height').html("高度"+height);
            $('#district').html("地址"+district);
//            dsBridge.call("zhyl.givejavapoint", data, function (res) {
//                console.log(res);
//                ifgo = false;
//            });
        });
    }
}, Cesium.ScreenSpaceEventType.LEFT_CLICK);*/
function flyTo(entities) {
    viewer.flyTo(entities, { duration: 2, offset: new Cesium.HeadingPitchRange(0, Cesium.Math.toRadians(-85), 32500) })
}
//andoird 请求显示谷歌在线
dsBridge.register('google_yingxiang', function (responseCallback) {
    changeOnline();
    return "chenggong";
});
//andoird 请求显示天地图影像
dsBridge.register('tianditu_shiliang', function (responseCallback) {
    changeMapIMG();
    return "chenggong";
});
//andoird 请求显示天地图中文标记
dsBridge.register('tianditu_yingxiang', function (responseCallback) {
    changeMapCIA();
    return "chenggong";
});
//实时轨迹
dsBridge.register('GPSpoint', function (data) {
var item = JSON.parse(data)
    console.log(item);
    item2 = JSON.parse(item)
    flyTo(item2)
    addHulinyuan(item2);
    return "chenggong";
});
//实时位置飞行控制
dsBridge.register('GPSflyto', function (gpsinfo) {
    var gpsInfo=JSON.parse(gpsinfo)
    var gpsInfo1=JSON.parse(gpsInfo)
    console.log("经度",gpsInfo1.position.lng)
    if(gpsInfo1.position.lng!=undefined) {
        viewer.camera.flyTo({
            destination: Cesium.Cartesian3.fromDegrees(gpsInfo1.position.lng, gpsInfo1.position.lat, 25000.0)
        });
    }else {
        console.log("没打开GPS！");
        viewer.camera.flyTo({
            destination: Cesium.Cartesian3.fromDegrees(116.404844,39.916485,15000.0)
        });
    }
});
dsBridge.register('showPoint', function (resourcetype1,data,dist1) {
    console.log(resourcetype1);
    console.log(typeof data);
    console.log(data);
    var resourselist=JSON.parse(data);
    console.log(resourselist);
        showPointBillbordDataOnMap(resourcetype1, resourselist, dist1, type2);
});
dsBridge.register('showPointforresource', function (resourcetype1,data,dist1) {
    console.log(JSON.stringify(data));
    console.log(data);
    showPointBillbordDataOnMap(resourcetype1, data, dist1, type2);
});
//清楚点位
dsBridge.register('removeDataSource', function (data) {
    removeDataSource(data);
});
 //地图打点
 function showPointBillbordDataOnMap(resourcetype, list, dist, type2) {
     console.log(JSON.stringify(list))
    // https://cesiumjs.org/Cesium/Build/Documentation/EntityCollection.html#EntityCollection
    return new Promise((resolve, reject) => {
        // console.profile('showPointBillbordDataOnMap')
        try {
            var sourceName = resourcetype + dist + type2
            console.log(sourceName)
            sourceNameArray.push(sourceName)
            var dataSource_ = viewer.dataSources.getByName(sourceName)
            console.log(dataSource_, dataSource_.length)
            if (dataSource_!=null&&dataSource_.length >= 1) {
                viewer.flyTo(dataSource_.entities, { duration: 3 })
                resolve()
                return
            }
            var dataSource = new Cesium.CustomDataSource(sourceName)

            if (arrPoint[sourceName]) {
                setTimeout(() => _hideDivPoint(sourceName, true), 3000)
            }
            let lastEntity = null
            viewer.entities.suspendEvents()
            console.log(list)
            for (const item of list) {
                console.log(item)
                // 添加实体
                if (!item.position) {
                    console.log('position为空：', item)
                    continue
                }
                // console.log(item.position.z)
                lastEntity = dataSource.entities.add({
                    id: item.id,
                    name: item.name,
                    position: Cesium.Cartesian3.fromDegrees(item.position.lng, item.position.lat, item.position.z),
                    billboard:{
                        image: `img/marker/resource/${resourcetype}.png`,
                        ...billboardConfig()
                    },
                    label: {
                        text: item.name,
                        ...labelConfig()
                    },
                    data: item,
                    tooltip: {
                        html: item.name,
                        anchor: [0, -12]
                    }
                    ,
                    click: clickcallback(resourcetype,item.id)
                })
                // if (resourcetype === 'monitor') {
                //     lastEntity.ellipse = {
                //         height: 0.0,
                //         semiMinorAxis: 3000.0,
                //         semiMajorAxis: 3000.0,
                //         outline: true,
                //         outlineColor: Cesium.Color.WHITE,
                //         outlineWidth: 3.0,
                //         distanceDisplayCondition: Cesium.DistanceDisplayCondition(100, 10000),
                //         heightReference: Cesium.HeightReference.CLAMP_TO_GROUND, // 贴地
                //         fill: false
                //     }
                //     setInterval(forpolygon, 1000, item, lastEntity, Cesium.DistanceDisplayCondition(100, 10000))
                // }
            }
            viewer.entities.resumeEvents()
            viewer.dataSources.add(dataSource)
            flyTo(lastEntity)
            lastEntity = null
            resolve()
        } catch (e) {
            reject(e)
        }
        // console.profileEnd()
    })
}
//清除卫星点位
 function removeDataSource(dict_resourcetype, type = '') {
    const dataSource = viewer.dataSources.getByName(dict_resourcetype + type)
    if (dataSource!=null&&dataSource.length >= 1) {
        try {
            viewer.dataSources.remove(dataSource[0])
            setTimeout(() => _hideDivPoint(dict_resourcetype + type), 2000)
        } catch (e) {
            console.log(`removeDataSource`, e)
        }
    }
}
function _hideDivPoint(sourceName, visib) {
    const arr = arrPoint[sourceName]
    if (arr) {
        for (let i = 0; i < arr.length; i++) {
            arr[i].visible = visib
        }
    }
}
//清除点位
 function removeAllDataSourceForTree() {
    sourceNameArray.forEach(sourceName => {
        removeDataSource(sourceName)
    })
    sourceNameArray.splice(0, sourceNameArray.length)
}
function clickcallback(resourcetype,id) {
     return function(entity) {
         var data = new Object();
         //为对象添加动态属性
         data.resourcetype = resourcetype;
         data.id = id;
         //输出
         console.log(data);
             dsBridge.call("pointclick", data);
     }
    // if (resourcetype=="fire_weixing") {
    //     dsBridge.call("showAndroidEscapeDetails", id, function (res) {
    //         console.log(res)
    //     });
    // }
}
//实时位置
function addHulinyuan(item) {
    return new Promise((resolve, reject) => {
        try {
            if (!item) {
                return
            }

            const sourceName = 'source-hulinyuan-' + item.userId
            const dataSource_ = viewer.dataSources.getByName(sourceName)
            let dataSource = null
            if (dataSource_!=null&&dataSource_.length >= 1) {
                dataSource = dataSource_[0]
                dataSource_[0].show = true

                if (!item) {
                    // debounce_flyTo(dataSource_[0].entities)
                    resolve()
                    return
                }
            } else {
                dataSource = new Cesium.CustomDataSource(sourceName)
            }

            // for (const item of list) {
            // 添加实体
            /* eslint-disable new-cap   */
            /* eslint-disable no-irregular-whitespace */
            const id = 'p' + item.userId
            const entity = dataSource.entities.getById(id)

            // 累加轨迹点
            let positionArr = guijiMap[id]
            if (!positionArr) {
                positionArr = [item.position.lng, item.position.lat]
            } else {
                positionArr.push(item.position.lng)
                positionArr.push(item.position.lat)
            }
            guijiMap[id] = positionArr

            if (entity) {
                entity.position = Cesium.Cartesian3.fromDegrees(item.position.lng, item.position.lat)
                // todo 更新轨迹线
                entity.lineColor = entity.lineColor || Cesium.Color.fromRandom({
                    // red: 1.0,
                    // green: 1.0,
                    alpha: 1.0
                })
                // entity.polyline = {
                //     positions: Cesium.Cartesian3.fromDegreesArray(positionArr),
                //     width: 10,
                //     hMax: 500000,
                //     // material: new Cesium.PolylineDynamicMaterialProperty({
                //     //   color: Cesium.Color.PURPLE,
                //     //   outlineWidth: 0,
                //     //   outlineColor: Cesium.Color.BLACK
                //     // })
                //     // material: new Cesium.PolylineTrailMaterialProperty({
                //     //   color: Cesium.Color.PURPLE,
                //     //   trailLength: 0.3,
                //     //   constantSpeed: 6000000.0,
                //     //   period: 1.0
                //     // })
                //     material: new Cesium.PolylineArrowMaterialProperty(
                //         entity.lineColor
                //     )
                // }
            } else {
                dataSource.entities.add({
                    id: id,
                    name: sourceName,
                    // position: Cesium.Cartesian3.fromDegrees(120.306908, 36.302866),
                    position: Cesium.Cartesian3.fromDegrees(item.position.lng, item.position.lat),
                    billboard: {
                        image: `img/marker/resource/hulinyuan.png`,
                        ...billboardConfig(),
                        scale: 1.0,
                        distanceDisplayCondition: new Cesium.DistanceDisplayCondition(10, 296000)
                    },
                    label: {
                        text: item.createUser,
                        ...labelConfig(),
                        font: 'normal small-caps normal 26px 楷体',
                        fillColor: Cesium.Color.RED,
                        distanceDisplayCondition: new Cesium.DistanceDisplayCondition(10, 296000)
                    },
                    tooltip: {
                        html: item.createUser,
                        anchor: [0, -12]
                    },
                    // customizePopup: {
                    //     html: `<div style="background-color: rgba(63, 72, 84, 0.9);padding 10px 0px;text-align: center;"><div>${item.createUser}</div><div>${formatTimeFromLong(item.actualUploadTime)}</div><div>`,
                    //     anchor: [0, -10]
                    // },
                    click: function(e) {
                        viewer.haohai.popup.show(e, e.position._value)
                    }
                })
                viewer.dataSources.add(dataSource)
            }
            // if (viewer.camera.positionCartographic.height > 32500) {
            //   debounce_flyTo(dataSource.entities)
            // }

            resolve()
        } catch (e) {
            reject(e)
        }
    })
}

function changeMapCIA() {

    const imageryLayers = viewer.imageryLayers

    if (!tidituBaseLayer) {
        tidituBaseLayer = imageryLayers.addImageryProvider(new Cesium.TiandituImageryProvider({
            credit: new Cesium.Credit('天地图全球影像服务     数据来源：国家地理信息公共服务平台 & 四川省测绘地理信息局'),
            token: URL_CONFIG.TOKEN_TIANDITU
        }))
    }
    // 初始化天地图全球中文注记服务，并添加至影像图层
    if (tiandituLayerCIA) {
        if (imageryLayers.indexOf(tiandituLayerCIA) > 0) {
            imageryLayers.raiseToTop(tiandituLayerCIA)
            return
        } else {
            imageryLayers.remove(tiandituLayerCIA)
        }
        // imageryLayers.remove(tiandituLayerCIA)
    }
    tiandituLayerCIA = imageryLayers.addImageryProvider(new Cesium.TiandituImageryProvider({
        mapStyle: Cesium.TiandituMapsStyle.CIA_C, // 天地图全球中文注记服务（经纬度投影）
        token: URL_CONFIG.TOKEN_TIANDITU
    }))

    _setStateClose('cia')
}
function changeMapIMG() {
    const imageryLayers = viewer.imageryLayers

    if (!tidituBaseLayer) {
        tidituBaseLayer = imageryLayers.addImageryProvider(new Cesium.TiandituImageryProvider({
            credit: new Cesium.Credit('天地图全球影像服务     数据来源：国家地理信息公共服务平台 & 四川省测绘地理信息局'),
            token: URL_CONFIG.TOKEN_TIANDITU
        }))
    }
    // 初始化天地图全球中文注记服务，并添加至影像图层
    if (tiandituLayerIMG) {
        if (imageryLayers.indexOf(tiandituLayerIMG) > 0) {
            imageryLayers.raiseToTop(tiandituLayerIMG)
            return
        } else {
            imageryLayers.remove(tiandituLayerIMG)
        }
    }
    tiandituLayerIMG = imageryLayers.addImageryProvider(new Cesium.TiandituImageryProvider({
        mapStyle: Cesium.TiandituMapsStyle.IMG_C, // 全球影像地图服务(经纬度)
        token: URL_CONFIG.TOKEN_TIANDITU
    }))
    _setStateClose('img')
}
// 切换到在线（谷歌）地图
function changeOnline() {
    const imageryLayers = viewer.imageryLayers
    if (googleLayer) {
        if (imageryLayers.indexOf(googleLayer) > 0) {
            imageryLayers.raiseToTop(googleLayer)
            // viewer.imageryLayers.layerMoved.addEventListener((e) => {
            //   console.log('图层移除变化')
            //   imageryLayers.raiseToTop(googleLayer)
            // })
            return
        } else {
            imageryLayers.remove(googleLayer)
        }
    }
    googleLayer = imageryLayers.addImageryProvider(new Cesium.UrlTemplateImageryProvider({
        url: 'http://mt2.google.cn/vt?lyrs=y@189&x={x}&y={y}&z={z}'
    }))
    _setStateClose('google')
}
function _setStateClose(flag) {
    switch (flag) {
        case 'cia':
            STATE_TIANDITU_CIA = true
            removeMapIMG()
            removeGoogleMap()
            break
        case 'img':
            STATE_TIANDITU_IMG = true
            removeGoogleMap()
            break
        case 'google':
            STATE_GOOGLE_MAP = true
            removeMapCIA()
            removeMapIMG()
            break
        case 'local':
            STATE_ISERVER_MAP = true
            break
    }
}
// todo 关闭各个图层
function removeMapCIA() {
    if (tiandituLayerCIA) {
        viewer.imageryLayers.remove(tiandituLayerCIA)
    }
    STATE_TIANDITU_CIA = false
}
function removeMapIMG() {
    if (tiandituLayerIMG) {
        viewer.imageryLayers.remove(tiandituLayerIMG)
    }
    STATE_TIANDITU_IMG = false
}
function removeGoogleMap() {
    if (googleLayer) {
        const i = viewer.imageryLayers.indexOf(googleLayer)
        const s = viewer.imageryLayers.remove(googleLayer)
        console.log(s, i)
    }
    STATE_GOOGLE_MAP = false
}
function gohome(){
     console.log(item2.position.lat)
    viewer.camera.flyTo({
        destination : Cesium.Cartesian3.fromDegrees(item2.position.lng, item2.position.lat, 32500)
    });
}
