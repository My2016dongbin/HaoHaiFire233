var remoteServerIP = "web.ehaohai.com:2018";

var currentFireJsonObject;//当前选择的火点json

var mapOpertionType = 0; //地图操作代码0：代表无动作，1：代表选择元素，2：代表测量 ,3:代表修改,4:创建新标注(点、线、面)
var zoomNum=8;
var minZoomNum=1;
var maxZoomNum=18;

//天地图访问token 集合
var tokens = ['3783f3a9b0b82e78a7e9db88ac310aaf', '21799afc1b7f1d85a0e4f20e007501c9','41b4c937e78063d5b05c500a35479245']
// 天地图服务负载子域
var subdomains = ['0', '1', '2', '3', '4', '5', '6', '7'];

var token = tokens[Math.floor((Math.random() * tokens.length))];

var tdtUrl = 'https://t' + subdomains[Math.floor((Math.random() * subdomains.length))]+'.tianditu.gov.cn/';

var view = new ol.View({
  center: ol.proj.fromLonLat([120, 36]),
    zoom: zoomNum,
    minZoom: minZoomNum,
    maxZoom: maxZoomNum,
    rotate:false
});


//高德卫星图层
var satlayerGaoDe = new ol.layer.Tile({
    source: new ol.source.XYZ({
        title: "卫星图-GD",
        url: 'https://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}'
        //url: 'https://wprd04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&style=7&x={x}&y={y}&z={z}&scl=1&ltype=11'

    }),
    name: '高德-卫星',
    visible: true

});



//卫星图层
var satlayerTianDi = new ol.layer.Tile({
    source: new ol.source.XYZ({
        title: "卫星图-TD",
        url: tdtUrl+'DataServer?T=img_w&x={x}&y={y}&l={z}&tk='+token,
    }),
    name: '天地图-卫星',
    visible: false

});


//卫星图标注层 f27378689ecff4871a2b41af03519144   183e4e3e1b008e4e188e981b00b7c73e
var satannolayerTianDi = new ol.layer.Tile({
    title: "卫星图标注-TD",
    source: new ol.source.XYZ({
        url: tdtUrl + 'DataServer?T=cia_w&x={x}&y={y}&l={z}&tk=' + token,
    }),
    name: '天地图-卫星标注',
    visible: false
});


//注层
var cvalayerTianDi = new ol.layer.Tile({
    title: "矢量图标注-TD",
    source: new ol.source.XYZ({
        url: tdtUrl + 'DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=' + token,
    }),
    name: '天地图-矢量标注',
    visible: false
});

//矢量
var veclayerTianDi = new ol.layer.Tile({
    title: "矢量图标注-TD",
    source: new ol.source.XYZ({
        url: tdtUrl+'DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=' + token,
    }),
    name: '天地图-矢量',
    visible: false
});

//谷歌被封，不再使用
var googleMapLayer = new ol.layer.Tile({
    source: new ol.source.XYZ({
        //url: 'http://www.google.cn/maps/vt/pb=!1m4!1m3!1i{z}!2i{x}!3i{y}!2m3!1e0!2sm!3i380072576!3m8!2szh-CN!3scn!5e1105!12m4!1e68!2m2!1sset!2sRoadmap!4e0!5m1!1e0'
        url: 'http://mt2.google.cn/vt?lyrs=p@189&x={x}&y={y}&z={z}',
    //    url: 'http://www.google.cn/maps/vt?lyrs=p@189&x={x}&y={y}&z={z}' // 谷歌高程
    }),
    //name: 'googleMap-矢量',
    name: 'googleMap-高程',
    visible: true
});

/*旧方法*/
/*var googleWXLayer = new ol.layer.Tile({
    source: new ol.source.XYZ({
        url: 'http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}'
    }),
    name: 'googleMap-卫星',
    visible: true,
    rotate:false
});
var googleWXMarkLayer = new ol.layer.Tile({
    source: new ol.source.XYZ({
        url: 'http://www.google.cn/maps/vt?lyrs=h@189&gl=cn&x={x}&y={y}&z={z}'
    }),
    name: 'googleMap-卫星标注',
    visible: true
});*/
var googleWXLayer = new ol.layer.Tile({
    source: new ol.source.XYZ({
        //url: 'http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}',
        //url:'http://www.google.cn/maps/vt?lyrs=s&x={x}&y={y}&z={z}'
        url: 'https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}'

        //url: 'http://www.google.cn/maps/vt?lyrs=s&x={x}&y={y}&z={z}'
        //'mt{ s }.google.cn / vt / lyrs=s & hl=zh - CN & gl=CN & x={ x } & y={ y } & z={ z } & s=Gali'
        // wrapX:false
    }),
    name: 'googleMap-卫星底图',
    visible: true
});

var googleWXMarkLayer = new ol.layer.Tile({
    source: new ol.source.XYZ({
        //url: 'http://www.google.cn/maps/vt?lyrs=h@189&gl=cn&x={x}&y={y}&z={z}',
        //url: 'http://www.google.cn/maps/vt?lyrs=s&x={x}&y={y}&z={z}'
        url: tdtUrl + 'DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=' + token,

        // wrapX:false
    }),
    name: 'googleMap-卫星注记',
    visible: true
});


//卫星图层
var gaofenMarkLayer = new ol.layer.Tile({
    source: new ol.source.XYZ({
        title: "卫星图-GF",
        url: 'https://service.siweiearth.com/wmts/seis/v3/wmts/tile/1012/0?mgt_token=3e4714ae48d1c4f469a73e0abb9f74a9&product_id=102&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fpng&TileMatrix={z}&TileCol={x}&TileRow={y}',
        projection:'EPSG:4326'
    }),
    name: '高分',
    visible: false

});

//构建矢量图层-Marker
var sourceMarker = new ol.source.Vector();
var vectorMarker = new ol.layer.Vector({
    source: sourceMarker,
    name: '标注图层',
    visible: true
});


//二维地图
var map = new ol.Map({
    /*interactions: ol.interaction.defaults({ doubleClickZoom: false }).extend([
      new ol.interaction.DragRotateAndZoom() //2018-03-13增加DragRotateAndZoom
    ]),*/
    target: 'map',

    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM(),
            name: 'OSM图层',
            visible: false
        })
    ],//,
    /*controls: ol.control.defaults({
        attributionOptions: {
            collapsible: false
        }
    }),*/
    view: view
});

 Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDhhYmQxOC1mYzMwLTRhYmEtOTI5Ny1iNGExNTQ3ZTZhODkiLCJpZCI6NTQ1NCwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MzM3Mzc0NH0.RU6ynAZcwQvdfygt_N_j2rb2lpsuyyROzdaLQg0emAg';

//三维地图
var ol3d = new olcs.OLCesium({
    map: map
});


//向左移动地图
function moveToLeft() {//普通平移，我设置的lonlat值是0.06,具体lonlat值需要自己配置
    var lonlat = 1000;
    var view = map.getView();
    var mapCenter = view.getCenter();
    mapCenter[0] -= lonlat * Math.pow(2, 11 - view.getZoom());
    view.animate({
        center: mapCenter,
        duration: 250
    })
}

//向右移动地图
function moveToRight() {
    var lonlat = 1000;
    var view = map.getView();
    var mapCenter = view.getCenter();
    mapCenter[0] += lonlat * Math.pow(2, 11 - view.getZoom());
    view.animate({
        center: mapCenter,
        duration: 250
    })
}

//向上移动地图
function moveToUp() {
    var lonlat = 1000;
    var view = map.getView();
    var mapCenter = view.getCenter();
    mapCenter[1] += lonlat * Math.pow(2, 11 - view.getZoom());
    view.animate({
        center: mapCenter,
        duration: 250
    })
}

//向下移动地图
function moveToDown() {
    var lonlat = 1000;
    var view = map.getView();
    var mapCenter = view.getCenter();
    mapCenter[1] -= lonlat * Math.pow(2, 11 - view.getZoom());
    view.animate({
        center: mapCenter,
        duration: 250
    })
}

//放大地图
function zoomIn() {
    var view = map.getView();
    view.animate({
        zoom: view.getZoom() + 1,
        duration: 250
    })
}

//缩小地图
function zoomOut() {
    var view = map.getView();
     view.animate({
            zoom: view.getZoom() - 1,
            duration: 250
        })
}

//移除地图控件
function removeAllControl() {
    var mapControls = map.getControls();
    mapControls.forEach(function (e) {
        map.removeControl(e);
    })
}


//添加鼠标定位控件
function addMousePosition() {
    map.addControl(new ol.control.MousePosition({
        undefinedHTML: '',
        projection: 'EPSG:4326',
        coordinateFormat: function (coordinate) {
            return ol.coordinate.format(coordinate, '{x}, {y}', 4);
        },
        //,className: 'mouse-position-wrapper'
        className: 'custom-mouse-position', //自定义位置
        target: document.getElementById('mouse-position')//自定义位置
    })
);
}

//添加缩略图控件
function addOverviewMap() {
    map.addControl(new ol.control.OverviewMap({
        collapsed: true,
        //label: ">>",
        tipLabel: "鹰眼"
    }));
}

//添加旋转控件
function addRotate() {
    map.addControl(new ol.control.Rotate({
        className: 'map-rotate', //自定义位置
        autoHide: true,

    }));
}

//添加比例尺控件
function addScaleLine() {
    map.addControl(new ol.control.ScaleLine({
        className: 'ol-scale-line-custom'
    }));
}

//添加缩放控件
function addZoom() {
    map.addControl(new ol.control.Zoom());
}

//添加缩放滑动控件
function addZoomSlider() {
    map.addControl(new ol.control.ZoomSlider());
}
//添加缩放到当前视图滑动控件
//map.addControl(new ol.control.ZoomToExtent());

//添加全屏控件
function addFullScreen() {
    map.addControl(new ol.control.FullScreen());
}

//显示火情信息
function showFireInfo(Idstr) {

}

//构建弹出页面详细信息
function buildPopupString(fireJsonData, featureId)
{
    //var idstr = feature.getId() + ': ' + feature.get('name');
    //构建火点详细信息
    var jsonStr = fireJsonData;
    featureId = featureId.replace("CurrSelect", "");
    var FireInfoHtml = "";
    for (var ps in jsonStr) {//遍历json对象的每个key/value对,p为key
        for (var p in jsonStr[ps]) {
            if (jsonStr[ps][p].Id == featureId) {
                currentFireJsonObject=jsonStr[ps][p];
                var addres = "";
                if (jsonStr[ps][p].Country != "中国") {
                    addres = "境外热源";
                }
                else {
                    addres = jsonStr[ps][p].Province + " " + jsonStr[ps][p].City + " " + jsonStr[ps][p].County;
                }
                $("#VISImage-phone").attr('src', "");
                $("#IRImage-phone").attr('src', "");
                if (IsMobile)
                {
                    //$("#fire-address").html(addres);
                    $("#fire-address").html(jsonStr[ps][p].FormattedAddress);
                    $("#fire-ObservationDateTime").html((jsonStr[ps][p].ObservationDateTime + "").replace("T", " "));
                    $("#fire-LonLat").html((jsonStr[ps][p].Longitude + "").substring(0, 6) + "&nbsp&nbsp" + (jsonStr[ps][p].Latitude + "").substring(0, 5));
                    $("#fire-Credibility").html(parseFloat((jsonStr[ps][p].Credibility + "").substring(0, 5)) * 100);
                    $("#fire-Area").html(jsonStr[ps][p].Area);
                    $("#fire-ObservationFrequency").html(jsonStr[ps][p].ObservationFrequency);
                    //$("#fire-LandType").html("林地（" + parseFloat((jsonStr[ps][p].Woodland + "").substring(0, 4)) * 100 + "%）草地（" + parseFloat((jsonStr[ps][p].Grassland + "").substring(0, 4)) * 100 + "%）农田（" + parseFloat((jsonStr[ps][p].Farmland + "").substring(0, 4)) * 100 + "%）其他（" + parseFloat((jsonStr[ps][p].Otherland + "").substring(0, 4)) * 100 + "%）");
                    $("#fire-LandType").html("林地(" + Math.fround(jsonStr[ps][p].Woodland * 100) + "%)草地(" + Math.fround(jsonStr[ps][p].Grassland * 100) + "%)农田(" + Math.fround(jsonStr[ps][p].Farmland * 100) + "%)其他(" +Math.fround(jsonStr[ps][p].Otherland * 100) + "%)");
                    $("#fire-Satellite").html(jsonStr[ps][p].Satellite);
                    $("#fire-No").html(jsonStr[ps][p].FireNo);

                    if (jsonStr[ps][p].Satellite == "NOAA-18" || jsonStr[ps][p].Satellite == "NOAA-19") {
                        if (jsonStr[ps][p].VisibleLightImageAddress != null) {
                            $("#VISImage-phone").attr('src', "http://219.239.221.19" + jsonStr[ps][p].VisibleLightImageAddress);
                        }
                        if (jsonStr[ps][p].IRImageAddress != null) {
                            $("#IRImage-phone").attr('src', "http://219.239.221.19" + jsonStr[ps][p].IRImageAddress);
                        }

                    }
                    else {
                        if (jsonStr[ps][p].VisibleLightImageAddress != null) {
                            $("#VISImage-phone").attr('src', "http://" + remoteServerIP   + jsonStr[ps][p].VisibleLightImageAddress);
                        }
                        if (jsonStr[ps][p].IRImageAddress != null) {
                            $("#IRImage-phone").attr('src', "http://" + remoteServerIP  + jsonStr[ps][p].IRImageAddress);
                        }
                    }

                    //查询火点详情
                      var  param = {

                            fireAlarmId: jsonStr[ps][p].Id
                         }

                        $("#feedPerson").html("");
                        $("#feedTime").html("");
                        $("#feedAddress").html("");
                        $("#feedDes").html("");
                        $("#fire-img1").html("");
                        $("#fire-img2").html("");
                        $("#fire-img3").html("");

                     $.ajax({
                            type: "get",
                            url: feedbackUrl + getFeedbackApi,
                            data: param,
                            dataType: "jsonp",    //跨域json请求一定是jsonp
                            async:false,
                            success: function (data, status) {

                            if(data.result=="1"){
                              if(data.data.length>0){
                                   $("#fireFeedback").css("display", "block");

                                 $("#feedPerson").html(data.data[0].UserName);
                                 $("#feedTime").html((data.data[0].InsertTime+"").replace("T", " "));
                                 $("#feedAddress").html(data.data[0].Address);
                                 $("#feedDes").html(data.data[0].description);
                                    var imgWidth="260px";
                                    if(data.data[0].imgUrl2!=""){
                                        imgWidth="130px";
                                    }

                                 if(data.data[0].imgUrl1!=""){
                                  var bigImg = document.createElement("img");     //创建一个img元素     
                                    bigImg.src=data.data[0].imgUrl1;  
                                  bigImg.style.width=imgWidth;
                                   bigImg.style.height="240px";
                                    var myp = document.getElementById('fire-img1'); //获得dom对象
                                     myp.appendChild(bigImg);    //为dom添加子元素img
                                 }
                                if(data.data[0].imgUrl2!=""){
                                    var bigImg = document.createElement("img");     //创建一个img元素     
                                     bigImg.src=data.data[0].imgUrl2;  
                                     bigImg.style.width=imgWidth;
                                     bigImg.style.height="240px";
                                      var myp = document.getElementById('fire-img2'); //获得dom对象
                                     myp.appendChild(bigImg);    //为dom添加子元素img
                                 }

                                }else{

                                 $("#fireFeedback").css("display", "none");
                                }

                               }else{
                                 $("#fireFeedback").css("display", "none");
                               }

                            },
                            error: function (e) {

                            },
                            complete: function () {

                            }
                        });



                }

                break;
            }
        }
    }
    return FireInfoHtml;
}

//添加事件
function initMapEvent() {
    //var Viewport = map.getViewport();
    //鼠标单击
     var diff;
     var zoom;

     map.on('singleclick', function (evt) {

           zoom = parseInt(map.getView().getZoom());
        //   alert(zoom);
           /*if (ol3d.getEnabled()) {//3D
                    if(zoom < 5){
                         diff = 1.00;
                    }else if(zoom > 10){
                         diff = 0.01;
                    }else{
                         diff = 0.3;
                    }
                  //  diff = 0.2;
           } else {
                    if(zoom < 5){
                        diff = 1.5;
                    }else if(zoom == 10){
                        diff = 0.2;
                    }else if(zoom == 11){
                        diff = 0.1;
                    }else if(zoom == 13 || zoom == 12){
                        diff = 0.01;
                    }else if(zoom>=14){
                        diff = 0.001;
                    }else{
                        diff = 0.4;
                    }

            }*/
               diff = 0.01;//bingo

        //    alert(diff);
            var pixel = map.getEventPixel(evt.originalEvent);
            var destination = ol.proj.transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326'); //坐标系转换
            if (mapOpertionType == 0) {
                    var feature;//点击的feature
                    var destCoor;//点击的位置
                    var layers = map.getLayers().array_;

                    for (var i = 0; i < layers.length; i++) {
                            var source = layers[i].getSource();
                            if (source instanceof ol.source.Vector) {
                                var features = source.getFeatures();
                                if(features==null || features.length==0){//bingo
                                  return;
                                }
                                if (features.length > 0) {
                                      for (var j = 0; j < features.length; j++) {
                                          var coor = ol.proj.transform(features[j].getGeometry().flatCoordinates, 'EPSG:3857', 'EPSG:4326');
                                          if (coor.length == 2) {
                                                  if (Math.abs(coor[0] - destination[0]) < diff && Math.abs(coor[1] - destination[1]) < diff) {
                                                           feature = features[j];
                                                           destCoor = coor;
                                                           SelectMarker(map, sourceMarker, feature.getId(), null, null, destCoor[0], destCoor[1], null, null, null);
                                                           flyTo(destCoor[0], destCoor[1]);
                                                           dsBridge.call("androidShowFire",feature.getId());  //android 源生弹出火警详情dialog
                                                   }
                                           }
                                      }
                                }
                             }
                     }
                  /*  feature = map.forEachFeatureAtPixel(pixel, function (feature) {
                            return feature;
                        });
                    if (feature != undefined && feature.getId() != undefined) {
                     //      destCoor = ol.proj.transform(feature.getGeometry().flatCoordinates, 'EPSG:3857', 'EPSG:4326');
                        if (IsMobile) {
                           *//* SelectMarker(map, sourceMarker, feature.getId(), null, null, destCoor[0], destCoor[1], null, null, null);
                            flyTo(destCoor[0], destCoor[1]);
                            buildPopupString(fireJsonData, feature.getId());
                            $("#fire-info").animate({ bottom: '0px' }, 500);*//*

                             SelectMarker(map, sourceMarker, feature.getId(), null, null, destCoor[0], destCoor[1], null, null, null);
                             flyTo(destCoor[0], destCoor[1]);
                             dsBridge.call("androidShowFire",feature.getId());  //android 源生弹出火警详情dialog
                        }
                    }
                    else {
                            hideAllWindow();
                    }*/
            }
        });
    //鼠标双击
    map.on('dblclick', function (evt) {

        if (mapOpertionType == 2) { //如果是出于测量状态则结束测量
            //unintMeasure();
        }
        else if (mapOpertionType == 3) {//如果是修改元素，则要退出
            //UnIntModify();
        }
        else if (mapOpertionType == 4) {//如果是创建新标注，则要退出
            //unintCustomTag();
        }
        else if (mapOpertionType == 0) {
            var pixel = map.getEventPixel(evt.originalEvent);
            var feature = map.forEachFeatureAtPixel(pixel, function (feature) { return feature; }); //查询方式获取Feature
            if (feature) {  //双击弹出窗口-显示详细信息
                //                var openNewLink = window.open('http://www.baidu.com', 'title', 'height=300, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=false, location=no, status=no'); //参数： url, 名称, 窗体样式
                //                if (window.focus) {
                //                    openNewLink.focus();
                //                }
            }
        }
        else {
            mousePostion = map.getEventPixel(evt.originalEvent);
            var destination = ol.proj.transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326'); //坐标系转换

            map.render();
        }
    });

    //
    //document.getElementById("map").addEventListener('mousemove', mapMousemove);

    //鼠标移动的事件
    map.on('pointermove', function (evt) {
        if (evt.dragging) {   //如果是拖动地图造成的鼠标移动，则不作处理
            return;
        }
        //mapOpertionType=0;//地图操作代码0：代表无动作，1：代表选择元素，2：代表测量
        var pixel = map.getEventPixel(evt.originalEvent);
        if (mapOpertionType == 0) {
        /*
            //var element = document.getElementById('popup');
            var feature = map.forEachFeatureAtPixel(pixel, function (feature) { return feature; }); //查询方式获取Feature
            //alert(feature);
            if (feature != undefined) {
                var idstr = feature.getId() + ': ' + feature.get('name');

                var coordinate = evt.coordinate;
                content.innerHTML = '<p>详细信息：</p><br><code>' + idstr + '</code>';
                overlay.setPosition(coordinate);
                map.addOverlay(overlay);
            }
            else {
                overlay.setPosition(undefined);
                closer.blur();
            } */

        }
        else if (mapOpertionType == 1) {  //1：代表选择元素
            /*var feature = map.forEachFeatureAtPixel(pixel, function (feature) { return feature; }); //查询方式获取Feature
            if (feature) {
                var messgae = feature.getId() + ': ' + feature.get('name');
                //alert(messgae);
            }*/
        }
        else if (mapOpertionType == 2) {  // 代表测量
            /** @type {string} */
            //pointerMoveHandler(evt);
        }
        else if (mapOpertionType == 3) {  //修改元素
            /*if (currentfeature == null) {
            currentfeature = map.forEachFeatureAtPixel(pixel, function (feature) { return feature; });
            alert("选择修改：" + currentfeature.getId());
            }*/
            //            currentfeature = map.forEachFeatureAtPixel(pixel, function (feature) { return feature; });
        }
        else { //后期扩展其它
        }
    });

    //鼠标移出地图区域
    map.getViewport().addEventListener('mouseout', function () {
        //helpTooltipElement.classList.add('hidden'); //隐藏提示信息
    });
}

//andoird 请求移动地图中心点
dsBridge.register('android_fly_to', function (arg1,arg2,responseCallback) {
   // alert(arg1+"    "+arg2)
    flyTo(arg1,arg2)
    //flyTo(108.72499868273735,22.284999825060368);
    return "chenggong";
});


//移动地图中心点
function flyTo(lon, lat) {

    var mapCenter = ol.proj.fromLonLat([lon, lat])

//    if (IsMobile) {
//        var lonlat = 1000;
//        mapCenter[1] -= lonlat * Math.pow(2, 11 - 12)*15;
//    }

    var view = map.getView();
    view.animate({
        center:mapCenter,
        zoom: 14,
        duration: 250
    })
    //view.setCenter(ol.proj.fromLonLat([lon, lat]));
    
}

function flyToWithZoom(lon, lat) {

    var mapCenter = ol.proj.fromLonLat([lon, lat])

    if (IsMobile) {
        var lonlat = 1000;
        mapCenter[1] -= lonlat * Math.pow(2, 11 - map.getView().getZoom()) * 15;
    }

    var view = map.getView();
    view.animate({
        center: mapCenter,
        duration: 250
    })
    //view.setCenter(ol.proj.fromLonLat([lon, lat]));

}


var geolocation = new ol.Geolocation({
  projection: view.getProjection(),
  tracking: true
});
geolocation.once('change:position', function() {
  view.setCenter(geolocation.getPosition());
  view.setResolution(2.388657133911758);
});

