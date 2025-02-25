//var loginUrl = "http://web.ehaohai.com:2019/api/Satellite/GetListByPutTime?Token=";
var loginUrl = "http://192.168.1.160:2022/api/Satellite/GetListByPutTime?Token=";//2025
//var GetRegionURL = "http://web.ehaohai.com:2019/api/Satellite/GetRegionPolygon?Token=";
var GetRegionURL = "http://192.168.1.160:2022/api/Satellite/GetRegionPolygon?Token=";//2025

//判定轮询消息条数是人为原因还是时间原因
let isTime = true;
//判定是否弹框提示
let isAlarmAlert = false;
//报警声音检测   false关闭，true开启
let alarmFlag  = false;
var start;
var alarmState = 1;//0：关闭 1:开启 2：有火警

/*关闭火警定时器*/
function stopInterval() {
    alarmState = 0;
    $("#alartImg").attr('src', "images/alartClose.png");
    clearTimeout(start);
}

/*显示火警信息*/
function showfireImage() {
    $("#fireImage").toggle(500);
}

function checkIP() {

}
/*播放火警声音*/
function playMusic() {
    var player = $("#player")[0]; /*jquery对象转换成js对象*/
        player.play(); /*播放*/
        $("#alartImg").attr('src', "images/alart.gif");
        alarmState = 2;
    }

/*停止火警声音*/
function stopMusic() {
    alarmState = 0;
    var player = $("#player")[0]; /*jquery对象转换成js对象*/
    player.pause(); /*暂停*/
    $("#alartImg").attr('src', "images/alartClose.png");
}
/* 2018-12-05 hht 获取火警数据*/
function GetFireCount(fireCount)
{
   dsBridge.call("GetAlarmCount", fireCount);
}
function  clearLoginContent(loginContent)
{
   dsBridge.call("ClearLogin", loginContent);
}
function changeAlarmState() {
    if (alarmState == 0) {
        alarmState = 1;
        $("#alartImg").attr('src', "images/alartOn.png");
        // $(".alart").attr('title', "停止本次报警");
        alarmFlag = true;

    }
    else {
        alarmFlag = false;
        // $(".alart").attr('title', "继续监视警情");
    }
    alarmFlag?playMusic():stopMusic();
}

function logOut() {
        var storage=window.localStorage;

             alert(storage.getItem("name"););

             storage.removeItem("name");
              storage.removeItem("pass");


    var msg = confirm('确认退出系统吗？');
    if (msg == true) {
        sessionStorage.clear();
         clearLoginContent("正在退出...");
        $(location).attr('href', 'login.html');
    }
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
// var QueryParam;

/*获取热点信息*/
function QueryFireInfo(param) {
    $('.warning-num').hide();
    $('.page').hide();
    $('#fireListUl').html("");
    $('#DataLoading').html("正在加载数据...");
    $('#DataLoading').show(10);

    $.ajax({
        type: "get",
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
                InitPage(data.total,param.page, param.rows)
            }
            else {
                $('#DataLoading').html("无数据！");
                $('.warning-num').hide();
                $('.page').hide();
            }
        },
        error: function (e) {
            alert("QueryFireInfo");
           reLogin();
           //isReLogin(arg1,arg2);
        },
        complete: function () {

        }
    });
}

/*获取热点信息*/
function GetFireInfo(hh, startDate, endDate) {
    //alert(loginUrl + sessionStorage.myToken);
    $('#fireListUl').html("");
    $('#DataLoading').html("正在加载数据...");
    $('#DataLoading').show(10);

    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
        url: loginUrl + sessionStorage.myToken,
        data: {
                page: 1, //请求页数
                rows: 10000, //每页行数
                sort: 'ObservationDateTime', //  排序字段
                order: 'desc',
                hour: hh,//最近几小时数据,
                startTime: startDate, //开始时间
                endTime: endDate //结束时间
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
            //isReLogin(arg1,arg2);
        },
        complete: function () {

        }
    });
}

/*获取热点数量*/
let lastRows = null;
function GetFireInfoInterval(queryData) {
    $.ajax({
        type: "get",
        url: loginUrl + sessionStorage.myToken,
        data: queryData,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            // startTimeOut = new Date().getTime();
            let rows = data.rows;
            console.log(rows);
            isPlayMusic(rows);
            lastRows = rows;
            alarmFlag?playMusic():stopMusic();
            // isTime true 时间推移
            isTime = true;
        },
        error: function (e,s,o) {
            console.log("234224123断网");
          reLogin();

            //isReLogin(arg1,arg2);
        },
    });
    function isPlayMusic(rows) {

        if (rows.length===0)
        {
            alarmFlag = false;
            return;
        }
        // let jsonData = {
        //     rows: rows,
        //     total:rows.length
        // };
        if (lastRows===null)
        {
            alarmFlag = true;
            $("#newAlarmAlert").fadeIn(150);
            // changeListInfo(jsonData)
        }
        else
        {
            GetFireCount(lastRows["rows"]);
            //检测新火警根据数据第一条Id对比判断
            //1.随着时间推移，部分数据查询不到
            //2.人为修改轮询参数设置

            //随着时间推移，若alarmFlag为false    图标为关闭
            // 需求：不点响铃按钮，火警一直报。
            // 也就是说alarmFlag一直为true ,但是来新火警需要弹框提示，
            //监测是否来新消息

            alarmFlag = true;

            //响铃为关闭状态
            if (alarmState===0)
            {
                //随时间推移
                if (isTime)
                {
                    alarmFlag = !compareTo(rows[0],lastRows[0]);
                    // console.log(alarmFlag);
                }
                // 手动修改参数
                else
                {
                    alarmFlag = true;

                }
                if (alarmFlag)
                {
                    $("#newAlarmAlert").fadeIn(150);
                    // changeListInfo(jsonData);
                }
            }
            // 响铃为开启状态
            else
            {
                isAlarmAlert = !compareTo(rows[0],lastRows[0]);
                if (isAlarmAlert)
                {
                    $("#newAlarmAlert").fadeIn(150);
                    // changeListInfo(jsonData);
                }
            }
        }
    }
}
// 刷新火警列表
function changeListInfo(jsonData) {
    ClearMarker(map, sourceMarker);
    // $("#newAlarmAlert").fadeIn(150);
    $('#DataLoading').hide();
    $('.warning-num').show();
    $("#alarmCount").text(jsonData.total);
    ShowJsonData(jsonData);

    //隐藏分页查询
    $('.page').hide();
}
// 判断obj是否一致
function compareTo(newObj,oldObj) {
    return newObj.Id === oldObj.Id;
}



//断线重新登录
function reLogin() {
alert('123');
    var storage=window.localStorage;
    let username =storage.getItem("name");
    let password = storage.getItem("pass");
    let url = "http://27.223.18.10:2019/api/Account/Login";
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
                    $("#sysInfo").html("用户信息有误！");
                    return;
                }
                sessionStorage.myToken = data.message;

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
// 加载矢量地图
function addGeoJSON(source) {

    var styles = new ol.style.Style(
        {
            stroke: new ol.style.Stroke({ color: '#F5A101', width: 3 })
            // fill: new ol.style.Fill({color: 'rgba(0, 0, 0, 0.0)'}
        });

    var GeoJSON = new ol.format.GeoJSON();
    var ProjectionConfig = { dataProjection: 'EPSG:4326', featureProjection: 'EPSG:3857' };
    var RegionFeatures = GeoJSON.readFeatures(source, ProjectionConfig);

    var sourceVector = new ol.source.Vector( { features: RegionFeatures });

    var layer = new ol.layer.Vector({ source: sourceVector, style: styles });

    map.addLayer(layer);

    var view = map.getView();

    //view.fit(RegionFeatures[0].getGeometry(), { padding: [170, 50, 30, 150] });
    view.fit(RegionFeatures[0].getGeometry(), { padding: [0, 0, 0, 0] });
}

/*获取行政区划信息*/
var RegionData = null;
function GetRegionInfo() {
    //alert(loginUrl + sessionStorage.myToken);
    $.ajax({
        type: "get",
        //url: "/api/Values/Get/5?Token=" + $("#myToken").html(),
        url: GetRegionURL + sessionStorage.myToken,
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            //$("#map").style.background = '';
            $("#map").css("background", "#000000");
            if (data.RegionCode == '100000') {
                var zoom = 4;
                if (IsMobile)
                {
                    zoom = 3
                }
                map.getView().animate({
                    center: ol.proj.fromLonLat([103.7109,34.7248]),
                    zoom: zoom,
                    duration: 250
                })
            }
            else {
                RegionData = data;
                addGeoJSON(RegionData.RegionPolygonGeoJson);
            }
            map.addLayer(vectorMarker);
            //$('#search-tool > a')[2].click();
            HourQuery(3);
            startInterval();
        },
        error: function (e) {
            // alert("用户信息超时，请重新登陆!");
            // sessionStorage.clear();
            reLogin();
            //isReLogin(arg1,arg2);
            console.log($.cookie("password"))
            // $(location).attr('href', 'login.html');
        },
        complete: function () {

        }
    });
}


function ShowJsonData(jsonStr) {
    fireJsonData = jsonStr;
    //$("#fireJsonData_input").val(fireJsonData);
    //alert($("#fireJsonData_input").val());
    $("#fireListUl").html();
    //var jsonObj = eval('(' + jsonStr + ')');
    //alert(jsonStr);
    var htmlstr = "";
    for (var ps in jsonStr) {//遍历json对象的每个key/value对,p为key
        var htmlstrSub = "";
        var ObservationDateTime = "";
        for (var p in jsonStr[ps]) {
            //alert(p + " " + jsonStr[ps][p]);
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
            Marker(map, sourceMarker, jsonStr[ps][p].Id, addres, jsonStr[ps][p].FormattedAddress, jsonStr[ps][p].Longitude, jsonStr[ps][p].Latitude, '#4271AE', 'images/red.png', 0.4);
        }
        htmlstr = htmlstr + htmlstrSub;
    }
    //alert(htmlstr);
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
        var coord = ol.proj.fromLonLat([lon, lat]);
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
            $("#search-tool").animate({ left: '500px' }, 500);
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
}

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
}
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
}

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
