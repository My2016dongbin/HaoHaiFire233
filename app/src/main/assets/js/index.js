var baseUrl="http://web.ehaohai.com:2019";
//var baseUrl="http://192.168.1.160:2022";//2025

var feedbackUrl="http://web.ehaohai.com:10172";

//查询火点接口
var loginUrl = baseUrl+"/api/Satellite/GetListByPutTime?Token=";
//查询区域边界点接口
var GetRegionURL = baseUrl+"/api/Satellite/GetRegionPolygon?Token="
//获取用户信息接口
var getUserMsgUrl=baseUrl+"/api/Account/GetUserMsg?Token=";
//获取子区域查询接口
var getAreaUrl=baseUrl+"/api/Account/GetAreaListByParentId?Token=";
var getBufferApi= baseUrl+"/api/SatelliteStatistics/GetRegionBufferPolygon?Token=";
var getFeedbackApi="/api/SatelliteFire/GetFireFeedbackList";

var userId;
function checkIP() {


}

//android 请求自动登录
/*dsBridge.register('androidRequestMap', function (username,password,token,hour,responseCallback) {

  // sessionStorage.myToken = token;
   alert(hour)
   getUserMsg();
   GetRegionInfo(hour);
  // getUserMsg();
  // GetRegionInfo(hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong);
  // GetRegionInfo(hour);

   return "success";
  // return "success"

});*/

function saveAndroidHser(hname,hpass)
{
   if(!window.localStorage){
                 // alert("浏览器不支持localstorage");
       }
       else
      {
         var storage=window.localStorage;

        storage.removeItem("name");
        storage.removeItem("pass");
      // alert(data);
       storage.setItem("name",hname);
       storage.setItem("pass",hpass);
      // alert(hname);

   }
}


//android 请求自动登录
dsBridge.register('abc', function (arg1, arg2, token,hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong,responseCallback) {
//dsBridge.register('abc', function (arg1, arg2, token,hour) {
    //alert(token);
    sessionStorage.myToken = token;
    saveAndroidHser(arg1,arg2);
   //alert("test1=" + sessionStorage.myToken);
    getUserMsg();
    GetRegionInfo(hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong);
   // GetRegionInfo(hour);
    return "success";
});



//登录成功后获取用户信息
function getUserMsg(){
    $.ajax({
        type: "get",
        url: getUserMsgUrl + sessionStorage.myToken,
        data: null,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
                userId= data.UserId;
                if(data.CityNo!=null)
                {//市级权限，地区查询隐藏
                $("#areaQueryLi").css("display", "none");
                }
                 if(data.ProvinceNo!=null&&data.CountyNo==null&&data.CityNo==null)
                 {//省级权限，省查询隐藏，
                   $("#firstSelect").css("display", "none");
                           $.ajax({
                                                  type : "get",
                                                  url :getAreaUrl+ sessionStorage.myToken, //此次url改为真正需要的url
                                                  data:{parentId:data.ProvinceNo},
                                                  dataType: "jsonp",
                                                  success : function(data, status) {
                                                       $.each(data, function(index, item) {
                                                      if(item.Enable==true){
                                                      $("#secondSelect").append(  //此处向select中循环绑定数据
                                                           "<option value="+item.Id+">" + item.Name+ "</option>");
                                                      }
                                                      });
                                                  },
                                              });

                }
                if(data.ProvinceNo==null)
                {//全国权限
                    $.ajax({
                        type : "get",
                        url :getAreaUrl+ sessionStorage.myToken, //此次url改为真正需要的url
                        data:{parentId:"0"},
                        dataType: "jsonp",
                        success : function(data, status) {
                            $.each(data, function(index, item) {
                            if(item.Enable==true){
                            $("#firstSelect").append(  //此处向select中循环绑定数据
                                                    "<option value="+item.Id+">" + item.Name+ "</option>");
                            }
                            });
                        },
                    });
                }


        },
        error: function (e) {
             dsBridge.call("requestError","1");  //地图请求输出错误
         //    alert("getUserMsg")
             reLogin();
        },
        complete: function () {

        }
    });

}

//省级下拉框改变时触发的事件
 $("#firstSelect").change(function(){
    $("#secondSelect").empty();
    $("#secondSelect").append("<option value='0'>--请选择--</option>");
    var seled = $("option:selected").val();
        $.ajax({
                               type : "get",
                               url :getAreaUrl+ sessionStorage.myToken, //此次url改为真正需要的url
                               data:{parentId:seled},
                               dataType: "jsonp",
                               success : function(data, status) {
                                    $.each(data, function(index, item) {
                                   if(item.Enable==true){
                                   $("#secondSelect").append(  //此处向select中循环绑定数据
                                        "<option value="+item.Id+">" + item.Name+ "</option>");
                                   }
                                   });
                               },
                           });
 });

var start;
var alarmState = 1;//0：关闭 1:开启 2：有火警

/*开启火警定时器，轮询查询*/
function startInterval() {
    alarmState = 1;
    $("#alartImg").attr('src', "images/alartOn.png");
    start = setInterval("GetFireInfoInterval(1)", 10000);
}


/*关闭火警定时器*/
function stopInterval() {
    alarmState = 0;
    $("#alartImg").attr('src', "images/alartClose.png");
    clearTimeout(start);
}
var istest = false;


/*显示火警信息*/
function showfireImage() {
    $("#fireImage").toggle(500);
}

/*播放火警声音*/
function playMusic() {
    if (alarmState == 0) return;
   // var player = $("#player")[0]; /*jquery对象转换成js对象*/
    //if (player.paused) { /*如果已经暂停*/
   // player.pause(); /*暂停*/
      // player.play(); /*播放*/
        $("#alartImg").attr('src', "images/alart.gif");
        alarmState = 2;
        //GetFireInfo(1);
//    } else {
//        player.pause(); /*暂停*/
//        $("#alartImg").attr('src', "images/alartClose.png");
//    }
    }

/*停止火警声音*/
    function stopMusic() {
        alarmState = 0;
        //var player = $("#player")[0]; /*jquery对象转换成js对象*/
       // player.pause(); /*暂停*/
        $("#alartImg").attr('src', "images/alartClose.png");
    }

    function changeAlarmState() {
        if (alarmState == 0) {
            alarmState = 1;
            //$("#alartImg").attr('src', "images/alartOn.png");
            //startInterval();
        }
        else {
            //stopInterval();
           // stopMusic();
        }
    }

//退出登录
function logOut() {
    var msg = confirm('确认退出登录吗？');
    if (msg == true) {
        sessionStorage.clear();
        clearLoginContent("正在退出...");
        $(location).attr('href', 'login.html');
    }
    else {
    }
}

/* 2018-12-05 hht 获取火警数据*/
function  clearLoginContent(loginContent)
{
   dsBridge.call("ClearLogin", loginContent);
}
Date.prototype.format = function (format) {
    var args = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var i in args) {
        var n = args[i];
        if (new RegExp("(" + i + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
    }
    return format;
};
var QueryParam;



/*获取热点信息*/
function QueryFireInfo(param) {
    $('.warning-num').hide();
    $('.page').hide();
    $('#fireListUl').html("");
    $('#DataLoading').html("正在加载数据...");
    $('#DataLoading').show(10);

    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
      //  alert(sessionStorage.myToken);
    //    alert(param);
        url: loginUrl + sessionStorage.myToken,
        data: param,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            $("#alarmCount").text(data.total);
            ClearMarker(map, sourceMarker);
            if (data.rows.length > 0) {
//            console.log("loginByPassword1: param---html 00" + JSON.stringify(param));
//            console.log("loginByPassword1: param---html 0" + JSON.stringify(data.rows[0]));
//            console.log("loginByPassword1: param---html 1" + JSON.stringify(data.rows[1]));
//            console.log("loginByPassword1: param---html 2" + JSON.stringify(data.rows[2]));
//            console.log("loginByPassword1: param---html 3" + JSON.stringify(data.rows[3]));
//            console.log("loginByPassword1: param---html 4" + JSON.stringify(data.rows[4]));
//            console.log("loginByPassword1: param---html ", data.rows);
//            console.log("loginByPassword1: param---html" + data.rows.length);
                $('#DataLoading').hide(10);
                $('.warning-num').show();
                $('.page').show();
                ShowJsonData(data);
                //初始化分页控件
                InitPage(data.total,param.page, param.rows)
            }
            else {
                $('#DataLoading').html("无数据！");
                $('.warning-num').hide();
                $('.page').hide();
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // reLogin();
           //  alert("QueryFireInfo");
           //  alert(XMLHttpRequest.status);
          //   alert(XMLHttpRequest.readyState);
           //  alert(textStatus);
             dsBridge.call("queryFireError","1");
         //    dsBridge.call("requestError","1");  //地图请求输出错误
            //sessionStorage.clear();
            //$(location).attr('href', 'login.html');
            //$('#DataLoading').html("数据加载失败,请重新登陆尝试！")
        },
        complete: function () {

        }
    });
}


/*获取热点信息loadMore*/
function QueryFireInfoMore(param) {
    $('.warning-num').hide();
    $('.page').hide();
    $('#fireListUl').html("");
    $('#DataLoading').html("正在加载数据...");
    $('#DataLoading').show(10);

    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
      //  alert(sessionStorage.myToken);
    //    alert(param);
        url: loginUrl + sessionStorage.myToken,
        data: param,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            $("#alarmCount").text(data.total);
            ClearMarker(map, sourceMarker);
            if (data.rows.length > 0) {
                $('#DataLoading').hide(10);
                $('.warning-num').show();
                $('.page').show();
                ShowJsonData(data);
                //初始化分页控件
                InitPage(data.total,param.page, param.rows)
            }
            else {
                $('#DataLoading').html("无数据！");
                $('.warning-num').hide();
                $('.page').hide();
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // reLogin();
           //  alert("QueryFireInfo");
           //  alert(XMLHttpRequest.status);
          //   alert(XMLHttpRequest.readyState);
           //  alert(textStatus);
             dsBridge.call("queryFireError","1");
         //    dsBridge.call("requestError","1");  //地图请求输出错误
            //sessionStorage.clear();
            //$(location).attr('href', 'login.html');
            //$('#DataLoading').html("数据加载失败,请重新登陆尝试！")
        },
        complete: function () {

        }
    });
}

/*获取热点信息*/
function GetFireInfo(hh, startDate, endDate) {
    $('#fireListUl').html("");
    $('#DataLoading').html("正在加载数据...");
    $('#DataLoading').show(10);

    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
        url: loginUrl + sessionStorage.myToken,
        data: {
            //pager: {
                page: 1, //请求页数
                rows: 100, //每页行数
                sort: 'ObservationDateTime', //  排序字段
                order: 'desc',
            //},
            //query: {
                hour: hh,//最近几小时数据,
                //sort: 'ObservationDateTime', //  排序字段
                //order: 'desc'
                startTime: startDate, //开始时间
                endTime: endDate //结束时间
            //}
        },
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            $("#alarmCount").text(data.total);
            if (data.rows.length > 0) {
                $('#DataLoading').hide(10);
                $('.warning-num').show();
                $('.page').show();
                ShowJsonData(data);
            }
            else
            {
                $('#DataLoading').html("无数据！");
                $('.warning-num').hide();
                $('.page').hide();
            }

        },
        error: function (e) {
        reLogin();
            //sessionStorage.clear();
            //$(location).attr('href', 'login.html');
            //$('#DataLoading').html("数据加载失败,请重新登陆尝试！")
        },
        complete: function () {

        }
    });
}
/* 2018-12-05 hht 获取火警数据*/
function GetFireCount(fireCount)
{
   dsBridge.call("GetAlarmCount", fireCount);
}


/*获取热点数量*/
function GetFireInfoInterval(hh) {
    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
        url: loginUrl + sessionStorage.myToken,
        data: {
            page: 1, //请求页数
            rows: 500, //每页行数
            sort: 'ObservationDateTime', //  排序字段
            order: 'desc',
            hour: hh,
            startTime: "",
            endTime: "",
            satellite: "ALL",
            sky: "ALL",
            ground: "ALL",
            landtype: "ALL",
            isbuffer: false
        },
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            for (var ps in data) {
                if (ps == "total") {
                    var totalrows = data[ps];
                    if (totalrows > 0) {
                    //2018-12-05
                    var tmp=data["rows"]
                       GetFireCount(tmp);
                        //playMusic();
                    }
                }
            }
        },
        error: function (e) {
        reLogin();
           // sessionStorage.clear();
           // $(location).attr('href', 'login.html');
        },
        complete: function () {

        }
    });
}


// 加载矢量地图
function addGeoJSON(source, isBuffer, strokeStyleParam) {
    let strokeStyle = strokeStyleParam || new ol.style.Stroke({ color: 'rgba(245,161,1,0.6)', width: 2 });
    var styles = new ol.style.Style();
    let strokeBuffer = new ol.style.Stroke({ color: '#ebdef9', width: 2 });
    let fillStyle = new ol.style.Fill({ color: 'rgba(0, 0, 0, 0.4)' });

    var GeoJSON = new ol.format.GeoJSON();
    var ProjectionConfig = { dataProjection: 'EPSG:4326', featureProjection: 'EPSG:3857' };
    let feature = [];
    if (isBuffer) {
        styles.setFill(fillStyle);
        styles.setStroke(strokeBuffer);
        var featuresCoor = GeoJSON.readFeatures(source);
        let coor = featuresCoor[0].getGeometry().getCoordinates();
        let p3 = areaMasking(coor);
        let polygon = new ol.geom.Polygon(p3);
        polygon.applyTransform(ol.proj.getTransform('EPSG:4326', 'EPSG:3857'));
        feature.push(new ol.Feature(polygon));
    }
    else {
        styles.setStroke(strokeStyle);
        styles.setFill(null);
        feature = GeoJSON.readFeatures(source, ProjectionConfig);

    }
    var sourceVector = new ol.source.Vector({
        features: feature,
        // wrapX:false
    });
    var layer = new ol.layer.Vector({
        source: sourceVector,
        style: styles,
    });
    map.addLayer(layer);
    if (!isBuffer) {
        var view = map.getView();
        view.fit(feature[0].getGeometry(), { padding: [0, 0, 0, 0] });
    }
    function areaMasking(coor) {
        let p3 = [[[-180, 90], [180, 90], [180, -90], [-180, -90], [-180, 90]]];
        // let p3 =[[[37,58],[158,58],[158,15],[37,15],[37,58]]];
        // let p3 =[];
        if (source.type === "MultiPolygon") {
            coor.forEach(function (el) {
                el.forEach(function (ell) {
                    p3.push(ell);
                })
            });
        }
        else if (source.type === "Polygon") {
            coor.forEach(function (el) {
                p3.push(el);
            });
        }
        return p3;
    }
}


//加载缓冲区范围
function bufferArea() {
    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
        url: getBufferApi + sessionStorage.myToken,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            addGeoJSON(data.RegionPolygonGeoJson, true);
        }
    });
}
//组合GeoJson，添加properties属性
function returnGeoAddName(regionPolygon, name) {
    return {
        "geometry": regionPolygon,
        "properties": { name: name },
        "type": "Feature",
    }
}

var RegionData=null;
//获取行政区划
//function GetRegionInfo(hour) {
function GetRegionInfo(hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong) {

    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
        url: GetRegionURL + sessionStorage.myToken,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            //$("#map").style.background = '';
            $("#map").css("background", "#000000");
            if (data.RegionCode == '100000') {
                var zoom = 3;
                map.getView().animate({
                    center: ol.proj.fromLonLat([103.7109,34.7248]),
                    zoom: zoom,
                    duration: 250
                })
            }else {
                   bufferArea();
                   RegionData = data;
                   let regionChild = null;
                   let fatherArea = data.RegionPolygonGeoJson;
                   let geoParam = returnGeoAddName(fatherArea, data.RegionName);
//                   if (RegionData.RegionChild) {
//                       regionChild = RegionData.RegionChild;
//                       for (let i = 0; i < regionChild.length; i++) {
//                           let geoParam = returnGeoAddName(regionChild[i].RegionPolygonGeoJson, regionChild[i].RegionName);
//                           let strokeStyle = new ol.style.Stroke({ color: '#f4f4f4', width: 0.5 });
//                           addGeoJSON(geoParam, false, strokeStyle);
//
//
//                       }
//                   }
                   addGeoJSON(geoParam, false);

               }
            map.addLayer(vectorMarker);
            //$('#search-tool > a')[2].click();
            HourQueryTwo(hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong);
          //  HourQuery(hour);

            startInterval();

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            dsBridge.call("requestError","1");  //地图请求输出错误
        //    alert("1")
         //   alert(XMLHttpRequest.status);
       //     alert(XMLHttpRequest.readyState);
       //     alert(textStatus);

            reLogin();

        },
        complete: function () {

        }
    });
}


function ShowJsonData(jsonStr) {

    fireJsonData = jsonStr;
    //$("#fireJsonData_input").val(fireJsonData);
    $("#fireListUl").html();
    //var jsonObj = eval('(' + jsonStr + ')');
    var htmlstr = "";
    for (var ps in jsonStr) {//遍历json对象的每个key/value对,p为key
        var htmlstrSub = "";
        var ObservationDateTime = "";
        for (var p in jsonStr[ps]) {
            //htmlstrSub = htmlstrSub + " " + p + ":" + jsonStr[ps][p];
//            var htmlstrSub1 = "";
//            for (var pp in jsonStr[ps][p]) {
//                htmlstrSub1 = htmlstrSub1 + " " + pp + ":" + jsonStr[ps][p][pp];
            //            }
            var addres = "";
            if (jsonStr[ps][p].Country != "中国") {
                addres = "境外热源";
            }
            else {
                addres = jsonStr[ps][p].Province + " " + jsonStr[ps][p].City + " " + jsonStr[ps][p].County;
            }

            if (ObservationDateTime != jsonStr[ps][p].ObservationDateTime) {
                //alert(ObservationDateTime +"!="+ jsonStr[ps][p].ObservationDateTime);
                ObservationDateTime = jsonStr[ps][p].ObservationDateTime;
                if (htmlstrSub != "") {
                    htmlstrSub = htmlstrSub + "</li>";
                }
                htmlstrSub = htmlstrSub + "<li><span class=\"list-time\">" + (jsonStr[ps][p].ObservationDateTime + "").replace("T", " ") + "</span><a href=\"javascript:;showSingleInfo(" + jsonStr[ps][p].Longitude + "," + jsonStr[ps][p].Latitude + ",'" + jsonStr[ps][p].Id  + "')\" class=\"list-loc\" title=\"" + addres + "\">" + addres + "</a>";
            }
            else {
                htmlstrSub = htmlstrSub + " <a href=\"javascript:;showSingleInfo(" + jsonStr[ps][p].Longitude + "," + jsonStr[ps][p].Latitude + ",'" + jsonStr[ps][p].Id + "')\" class=\"list-loc\" title=\"" + addres + "\">" + addres + "</a>";
            }

         CircleMarker(map, sourceMarker, jsonStr[ps][p].Longitude, jsonStr[ps][p].Latitude, 0.012, '#4271AE');
                    var credibility=jsonStr[ps][p].Credibility;
                    let imgSrc = 'images/red.png';
                        if (credibility>=0.80&&credibility<0.9)
                        {
                            imgSrc = "images/orange.png";
                        }
                        else if (credibility>=0.7&&credibility<0.80)
                        {
                            imgSrc = "images/yellow.png";
                        }
                        else if (credibility<0.7)
                        {
                            imgSrc = "images/darkblue.png";
                        }
         Marker(map, sourceMarker, jsonStr[ps][p].Id, addres, jsonStr[ps][p].FormattedAddress, jsonStr[ps][p].Longitude, jsonStr[ps][p].Latitude, '#4271AE', imgSrc, 0.4);}
        htmlstr = htmlstr + htmlstrSub;
    }
    $("#fireListUl").html(htmlstr);
    //$("#content").html(htmlstr);
}


/*显示火警列表*/
function showFireInfo() {
    //$("div.info-list-close").css("display", "none");
    //$("div.info-list").css("display", "block");
    if (IsMobile) {
        hideAllWindow();
        //$("#fire-info").animate({ bottom: '-1000' }, 500);
        $("#info-list").animate({ top: '0px' }, 500);
    }
    else {
        $("#info-list-close").hide(1000);
        $("#info-list").show(1000);
    }

}

//显示单条信息
function showSingleInfo(lon, lat, id) {
    if (IsMobile) {
        hideAllWindow();
        SelectMarker(map, sourceMarker, id, null, null, lon, lat, null, null, null);
        flyTo(lon, lat);
        //$("#dialog-info-box-phone")[0].innerHTML = buildPopupString(fireJsonData, id);
        buildPopupString(fireJsonData, id);
        $("#fire-info").animate({ bottom: '0px' }, 500);
    }
    else {
        flyTo(lon, lat);
        content.innerHTML = buildPopupString(fireJsonData, id);
        buildPopupString(fireJsonData, id);
        var coord = ol.proj.fromLonLat([lon, lat])
        overlay.setPosition(coord);
        map.addOverlay(overlay);
        map.render();
    }
}

function showSearchTool() {
    if (IsMobile) {
        hideAllWindow();
        $("#search-tool").animate({ left: '0px' }, 500);
    }
}
function showAdvancedQuery() {
    if (IsMobile) {
        hideAllWindow();
        $("#advanced-query").animate({ bottom: '0px' }, 500);
    }
}


/*隐藏火警列表*/
function hideFireInfo() {
    if (IsMobile) {
        if ($("#info-list").css('top') != '1000px') {
            $("#info-list").animate({ top: '1000px' }, 500);
        }
    }
    else {
        $("#info-list-close").show(1000);
        $("#info-list").hide(1000);
    }
}

/*隐藏火警详情*/
function hideSingleInfo() {
    if (IsMobile) {
        if ($("#fire-info").css('bottom') != '-1000px') {
            $("#fire-info").animate({ bottom: '-1000px' }, 500);
        }
    }
    else {
        map.getOverlays().clear();
    }
}
function hideSearchTool() {
    if (IsMobile) {
        if ($("#search-tool").css('left') != '500px') {
            $("#search-tool").animate({ left: '100%' }, 500);
        }
    }
}
function hideAdvancedQuery() {
    if (IsMobile) {
        if ($("#advanced-query").css('bottom') != '-1000px')
        {
            $("#advanced-query").animate({ bottom: '-1000px' }, 500);
        }
    }
}


function hideAllWindow()
{
    hideFireInfo();
    hideAdvancedQuery();
    hideSearchTool();
    hideSingleInfo();
}

//图层切换控制
var laryerName = "tianDiYingXiang"; //默认显示tianDiTuShiLang、GoogleShiLang、GoogleYingXiang

function RemoveLayer(LayerName) {
    map.removeLayer(LayerName);
    map.render();
}

function AddLayer(LayerName) {
    map.addLayer(LayerName);
    map.render();
}



//andoird 请求显示天地图影像
dsBridge.register('tianditu_yingxiang', function (responseCallback) {
    ShowtianDiYingXiang();
    return "chenggong";
});

//显示天地图影像
function ShowtianDiYingXiang() {
    $("#currentLaryer").html("[天地图影像]");
    ShowAllLayerBotton();
    satlayerTianDi.setVisible(true);
    satannolayerTianDi.setVisible(true);
    veclayerTianDi.setVisible(false);
    cvalayerTianDi.setVisible(false);
    googleMapLayer.setVisible(false);
    googleWXLayer.setVisible(false);
    googleWXMarkLayer.setVisible(false);
    gaofenMarkLayer.setVisible(false);
}

//andoird 请求显示天地图影像
dsBridge.register('tianditu_shiliang', function (responseCallback) {
    ShowtianDiShiLiang();
    return "chenggong";
});

//显示天地图矢量
function ShowtianDiShiLiang() {
    $("#currentLaryer").html("[天地图矢量]");
    ShowAllLayerBotton();
    satlayerTianDi.setVisible(false);
    satannolayerTianDi.setVisible(false);
    veclayerTianDi.setVisible(true);
    cvalayerTianDi.setVisible(true);
    googleMapLayer.setVisible(false);
    googleWXLayer.setVisible(false);
    googleWXMarkLayer.setVisible(false);
    gaofenMarkLayer.setVisible(false);
}

//andoird 请求谷歌影像
dsBridge.register('google_yingxiang', function (responseCallback) {
    ShowgoogleWXLayer();
    return "chenggong";
});
//显示谷歌影像
function ShowgoogleWXLayer() {
    $("#currentLaryer").html("[谷歌影像]");
    ShowAllLayerBotton();
    satlayerTianDi.setVisible(false);
    satannolayerTianDi.setVisible(false);
    veclayerTianDi.setVisible(false);
    cvalayerTianDi.setVisible(false);
    googleMapLayer.setVisible(false);
    googleWXLayer.setVisible(true);
    googleWXMarkLayer.setVisible(true);
    gaofenMarkLayer.setVisible(false);
}


//andoird 请求显示谷歌高程
dsBridge.register('google_gaocheng', function (responseCallback) {
    ShowgoogleMapLayer();
    return "chenggong";
});
//显示谷歌高程
function ShowgoogleMapLayer() {
    $("#currentLaryer").html("[谷歌高程]");
    ShowAllLayerBotton();
    satlayerTianDi.setVisible(false);
    satannolayerTianDi.setVisible(false);
    veclayerTianDi.setVisible(false);
    cvalayerTianDi.setVisible(false);
    googleMapLayer.setVisible(true);
    googleWXLayer.setVisible(false);
    googleWXMarkLayer.setVisible(false);
    gaofenMarkLayer.setVisible(false);
}

//andoird 请求显示谷歌高程
dsBridge.register('map_gaofen', function (responseCallback) {
    ShowGaofenMapLayer();
    return "chenggong";
});

//显示高分地图
function ShowGaofenMapLayer() {
    $("#currentLaryer").html("[高分地图]");
    ShowAllLayerBotton();
    satlayerTianDi.setVisible(false);
    satannolayerTianDi.setVisible(false);
    veclayerTianDi.setVisible(false);
    cvalayerTianDi.setVisible(false);
    googleMapLayer.setVisible(false);
    googleWXLayer.setVisible(false);
    googleWXMarkLayer.setVisible(false);
    gaofenMarkLayer.setVisible(true);
}

//显示全部
function ShowAllLayerBotton() {
    $("#tianDiYingXiang").toggle(100);
    $("#tianDiTuShiLang").toggle(100);
    $("#GoogleShiLang").toggle(100);
    $("#GoogleYingXiang").toggle(100);
}

//隐藏全部
function HideAllLayerBotton() {
    $("#tianDiYingXiang").hide(100);//show
    $("#tianDiTuShiLang").hide(100);
    $("#GoogleShiLang").hide(100);
    $("#GoogleYingXiang").hide(100);
}

function reLogin() {

    var storage=window.localStorage;

    let username =storage.getItem("name");

    let password = storage.getItem("pass");
    let url = "http://web.ehaohai.com:2019/api/Account/Login";
//    let url = "http://192.168.1.160:2022/api/Account/Login";//2025
    let data={ userName: username, password: password };

    $.ajax({
        type: "get",
        url: url,
        data: data,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            if (status == "success") {
                if (data.type == 0) {
                    sessionStorage.myToken = "NoData";
                    $("#sysInfo").html(data.message);
                    return;
                }
                sessionStorage.myToken = data.message;
                   jk.setToken(data.message);

                //初始化时，ajax为异步操作，
                // 函数GetRegionInfo没有Token值，需要重新登陆成功后重新调用
                if (RegionData===null)
                {
                    GetRegionInfo(3);
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("重新登录失败!");
        }
    });
}