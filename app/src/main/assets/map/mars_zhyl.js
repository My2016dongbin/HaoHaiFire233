var Cesium = window.Cesium
var viewer = window.viewer
var erea = '100';
var zhongshanpark;
var ifgo=false;
var parkadd=[120.406438,36.082317,120.407039,36.082449,120.407381,36.082787,120.407371,36.082917,120.407992,36.082934,120.408517,36.083012,120.408721,36.083211,120.408817,36.083644,120.408817,36.083947,120.409085,36.084449,120.409096,36.084891,120.408731,36.084943,120.408807,36.085947,120.409878,36.086068,120.410681,36.085886,120.411303,36.086034,120.411635,36.086172,120.411828,36.086899,120.413049,36.086787,120.413339,36.086735,120.413403,36.087064,120.413381,36.087384,120.413114,36.088449,120.412535,36.089583,120.412203,36.090311,120.411431,36.090241,120.410531,36.090311,120.41036,36.090337,120.411175,36.091514,120.411482,36.091945,120.411851,36.092379,120.412356,36.092853,120.412847,36.093243,120.413402,36.093634,120.414008,36.093989,120.41466,36.094288,120.414714,36.094309,120.414911,36.094382,120.414979,36.094408,120.417718,36.095064,120.420727,36.095634,120.420895,36.095666,120.424023,36.096261,120.425399,36.096521,120.426668,36.096765,120.426848,36.096799,120.430007,36.097401,120.430622,36.0975,120.431919,36.097764,120.43247,36.097925,120.433036,36.098163,120.433867,36.098662,120.434935,36.099329,120.435495,36.099602,120.436125,36.099819,120.437215,36.100104,120.437268,36.100118,120.437402,36.100151,120.444271,36.101853,120.44445,36.101604,120.447109,36.098838,120.446284,36.09825,120.448727,36.09638,120.447505,36.095176,120.446916,36.093868,120.446761,36.0938,120.446618,36.093833,120.446381,36.093867,120.446045,36.093907,120.445903,36.09409,120.44574,36.09412,120.445493,36.094298,120.445271,36.093923,120.445536,36.093307,120.445528,36.09321,120.445621,36.093165,120.445651,36.093054,120.4458,36.092918,120.445915,36.09265,120.446049,36.092607,120.447612,36.092103,120.44382,36.088224,120.44142,36.086285,120.441034,36.086545,120.441055,36.086726,120.441784,36.08722,120.440766,36.087947,120.439823,36.087298,120.440456,36.086744,120.439277,36.085843,120.438141,36.085566,120.439245,36.083886,120.438238,36.082059,120.436009,36.080856,120.43558,36.08102,120.434852,36.080994,120.43392,36.080735,120.433266,36.080267,120.432377,36.080129,120.431509,36.079834,120.429281,36.078518,120.428649,36.078449,120.428316,36.078864,120.427481,36.078769,120.427256,36.079358,120.424931,36.079012,120.424995,36.078786,120.424524,36.078665,120.42462,36.078276,120.423828,36.078163,120.423741,36.07844,120.423335,36.078379,120.423174,36.078769,120.42342,36.078899,120.422703,36.080189,120.42162,36.079462,120.420549,36.07915,120.419413,36.077903,120.418963,36.077557,120.418438,36.077765,120.417345,36.077124,120.414956,36.077297,120.414496,36.077288,120.414335,36.077652,120.41397,36.0776,120.41396,36.077869,120.413874,36.078232,120.412106,36.077609,120.411667,36.077591,120.411731,36.078111,120.411506,36.078232,120.410664,36.077978,120.410193,36.079848,120.411153,36.080077,120.410713,36.081202,120.409781,36.080945,120.409582,36.081144,120.409314,36.081322,120.408782,36.0816,120.407517,36.081868,120.40714,36.081955,120.406438,36.082317]
var parkname;
var parkid;
var parkarea;
var parkownership;
var parkaddress;
var parkimageFile;
var parkintentid;
let tidituBaseLayer = null
let tiandituLayerCIA = null
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
      infoBox: true
  }
    window.viewer = new Cesium.Viewer(id, { ...defaultMapConfig, ...config })
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
    LoadBaiduMapScript();
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

    $('#pclose').click(function () {
       $('#parkdetail').hide();
        $('.cesium-infoBox-close').click();

    });

function godetail(){
    dsBridge.call("zhyl.parkintent", parkintentid, function (res) {
    });
    $('#parkdetail').hide();
    $('.cesium-infoBox-close').click();
}
function addpark(){
    console.log(parkimageFile)
    var fushanpark = viewer.entities.add({
        id:parkid,
        name : parkname,
        polygon : {
            hierarchy : Cesium.Cartesian3.fromDegreesArray(parkadd),
            height : 0,
            material : Cesium.Color.CHARTREUSE.withAlpha(0.8),
            outline : true,
            outlineColor : Cesium.Color.BLACK
        },
        click:function (e) {

            ifgo=true;
            $('#detail').show();
        }
    });
}
// //县区界线
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
viewer.scene.screenSpaceCameraController.minimumZoomDistance = 2000;
var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
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
            dsBridge.call("zhyl.givejavapoint", data, function (res) {
                console.log(res);
                ifgo = false;
            });
        });
    }
}, Cesium.ScreenSpaceEventType.LEFT_CLICK);
 dsBridge.register('parkinfo', function (data) {
    console.log(data)
     var parkjson =JSON.parse(data)
     var parkjson2 =JSON.parse(parkjson)
     var parklist = parkjson2.data[0].dataList
     var parkposition=parklist[0].position

     var parkeara;
     var num =0;
     for (var i=2;i<parklist.length;i++){
         parkeara=parklist[i].position;
         parkname=parklist[i].name;
         parkid=parklist[i].id;
         parkaddress=parklist[i].address;
         parkarea=parklist[i].area;
         if (parklist[i].parkownership!=null||parklist[i].parkownership!=undefined) {
             parkownership=parklist[i].ownership
         } else {
             parkownership=""
         }

         if (parklist[i].imageFile!=null||parklist[i].imageFile!=undefined) {
             parkimageFile=parklist[i].imageFile
         }else {
             parkimageFile="./fushan.jpg"
         }
         var parkpositionjson = JSON.parse(parkeara)
         var parkcoordinates=parkpositionjson.coordinates
         console.log(parkcoordinates)
         parkadd=parkcoordinates
         addpark();
     }

 });
function gohome(){
    viewer.camera.flyTo({
        destination : Cesium.Cartesian3.fromDegrees(120.3306728037, 36.1106233801, 40000)
    });
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

}
