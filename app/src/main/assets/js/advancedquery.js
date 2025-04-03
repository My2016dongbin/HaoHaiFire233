

$('#search-tool > a').click(function (item) {
    $('#search-tool > a').removeClass();
    item.currentTarget.className = 'active'
    
   
    if (IsMobile) {
        hideSearchTool();
        if (item.currentTarget.id == "advancedQuery") {
            showAdvancedQuery();
        }
        else {
            showFireInfo();
        }
    }
    else {
        if (item.currentTarget.id == "advancedQuery") {
            $(".advanced-query").fadeToggle(300);
        }
    }
    //{
    //    $(".advanced-query").fadeIn(300);
    //}
    //else {
    //    $(".advanced-query").fadeOut(300);
    //}
});


//andoird请求不同时间段之内的火点信息
dsBridge.register('huodian_jpush', function (hour,responseCallback) {
    HourQuery(hour);
    return "chenggong";
});

//andoird请求不同时间段之内的火点信息 设置好的默认参数查询
dsBridge.register('huodian', function (hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong,responseCallback) {
    HourQueryTwo(hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong);
    return "chenggong";
});
//andoird请求不同时间段之内的火点信息 设置好的默认参数查询-分页后上拉加载
dsBridge.register('huodian_more', function (hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong,responseCallback) {
    HourQueryTwoMore(hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong);
    return "chenggong";
});
//andoird请求高级查询的的火点信息
dsBridge.register('huodian_gaoji', function (startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,number,isChooseHuanchong,isCountry,shengId,shiId,quId,responseCallback) {
    HourQueryOne(startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,number,isChooseHuanchong,isCountry,shengId,shiId,quId);
    return "chenggong";
});
//andoird请求高级查询的的火点信息-分页后上拉加载
dsBridge.register('huodian_gaoji_more', function (startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,page,number,isChooseHuanchong,isCountry,shengId,shiId,quId,responseCallback) {
    HourQueryOneMore(startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,page,number,isChooseHuanchong,isCountry,shengId,shiId,quId);
    return "chenggong";
});

function HourQuery(hour)
{
    alert(hourQuery);
    var satellite = "";
    if ($("#satelliteAll").prop("checked"))
        satellite = "ALL";
    else {
        var arr = new Array();
        $(".satelliteCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        satellite = arr.join(",");
    }
    var sky = "";
    if ($("#skyAll").prop("checked"))
        sky = "ALL";
    else {
        var arr = new Array();
        $(".skyCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        sky = arr.join(",");
    }
    var ground = "";
    if ($("#groundAll").prop("checked"))
        ground = "ALL";
    else {
        var arr = new Array();
        $(".groundCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        ground = arr.join(",");
    }
    var landtype = "";
    if ($("#landtypeAll").prop("checked"))
        landtype = "ALL";
    else {
        var arr = new Array();
        $(".landtypeCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        landtype = arr.join(",");
    }
    QueryParam = {
        page: 1, //请求页数
        rows: 500, //每页行数
        sort: 'PutStorageTime', //  排序字段
        order: 'desc',

        hour: hour,
        startTime: "",
        endTime: "",
        satellite: satellite,
        sky: sky,
        ground: ground,
        landtype: landtype,
        isbuffer: $("#isBuffer").prop("checked"),
        Country:"中国"
    }
    QueryFireInfo(QueryParam);
}

function HourQueryOne(startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,number,isChooseHuanchong,isCountry,shengId,shiId,quId)
{
    QueryParam = {
        page: 1, //请求页数
        rows: number, //每页行数
        sort: 'PutStorageTime', //  排序字段
        order: 'desc',
        hour: 0,
        startTime: startTimeStr,
        endTime: endTimeStr,
        satellite: satellite,
        sky: tiankongStr,
        ground: dimianStr,
        landtype: dimaoStr,
        isbuffer: isChooseHuanchong,
        Country:isCountry,
        Province:shengId,
        City:shiId,
        county:quId,
    }
    QueryFireInfo(QueryParam);


}

function HourQueryOneMore(startTimeStr,endTimeStr,satellite,tiankongStr,dimianStr,dimaoStr,page,number,isChooseHuanchong,isCountry,shengId,shiId,quId)
{
    QueryParam = {
        page: 1, //请求页数
        rows: page*number, //每页行数
        sort: 'PutStorageTime', //  排序字段
        order: 'desc',
        hour: 0,
        startTime: startTimeStr,
        endTime: endTimeStr,
        satellite: satellite,
        sky: tiankongStr,
        ground: dimianStr,
        landtype: dimaoStr,
        isbuffer: isChooseHuanchong,
        Country:isCountry,
        Province:shengId,
        City:shiId,
        county:quId,
    }
    QueryFireInfo(QueryParam);


}


function HourQueryTwo (hour,weixing,tiankong,dimian,dimao,number,jingwai,huanchong)
{
    QueryParam = {
          page: 1, //请求页数
          rows: number, //每页行数
          sort: 'PutStorageTime', //  排序字段
          order: 'desc',
          hour: hour,
          satellite: weixing,
          sky: tiankong,
          ground: dimian,
          landtype: dimao,
          isbuffer: huanchong,
          Country:jingwai,

      }
      QueryFireInfo(QueryParam);
}

function HourQueryTwoMore (hour,weixing,tiankong,dimian,dimao,page,number,jingwai,huanchong)
{
    QueryParam = {
          page: 1, //请求页数
          rows: number*page, //每页行数  暂用累加单页数量查询未用缓存上页数据
          sort: 'PutStorageTime', //  排序字段
          order: 'desc',
          hour: hour,
          satellite: weixing,
          sky: tiankong,
          ground: dimian,
          landtype: dimao,
          isbuffer: huanchong,
          Country:jingwai,

      }
      QueryFireInfo(QueryParam);
}



function HourQuery(hour)
{
    var satellite = "";
    if ($("#satelliteAll").prop("checked"))
        satellite = "ALL";
    else {
        var arr = new Array();
        $(".satelliteCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        satellite = arr.join(",");
    }
    var sky = "";
    if ($("#skyAll").prop("checked"))
        sky = "ALL";
    else {
        var arr = new Array();
        $(".skyCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        sky = arr.join(",");
    }
    var ground = "";
    if ($("#groundAll").prop("checked"))
        ground = "ALL";
    else {
        var arr = new Array();
        $(".groundCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        ground = arr.join(",");
    }
    var landtype = "";
    if ($("#landtypeAll").prop("checked"))
        landtype = "ALL";
    else {
        var arr = new Array();
        $(".landtypeCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        landtype = arr.join(",");
    }
    QueryParam = {
        page: 1, //请求页数
        rows: 500, //每页行数
        sort: 'ObservationDateTime', //  排序字段
        order: 'desc',

        hour: hour,
        startTime: "",
        endTime: "",
        satellite: satellite,
        sky: sky,
        ground: ground,
        landtype: landtype,
        isbuffer: $("#isBuffer").prop("checked"),
        Country:"中国"
    }
    QueryFireInfo(QueryParam);
}


$.datetimepicker.setLocale('ch');

//$('#datetimepicker1').datetimepicker({ lang: 'ch', startDate: '2018-06-20 00:00:00', step: 10 });
//$('#datetimepicker2').datetimepicker({ lang: "ch", step: 10 });
//高级查询
$('#startadvancedquery').click(function () {
    var isCountry="中国";
    var province="";
    var city="";
    if($("#firstSelect").val()!=0){
        province=$("#firstSelect").val();
    }
    if($("#secondSelect").val()!=0){
        city=$("#secondSelect").val();
    }
     if($("#isCountry").prop("checked")){
        isCountry=null;
    }
    var satellite = "";
    if ($("#satelliteAll").prop("checked"))
        satellite = "ALL";
    else {
        var arr = new Array();
        $(".satelliteCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        satellite = arr.join(",");
    }
    var sky = "";
    if ($("#skyAll").prop("checked"))
        sky = "ALL";
    else {
        var arr = new Array();
        $(".skyCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        sky = arr.join(",");
    }
    var ground = "";
    if ($("#groundAll").prop("checked"))
        ground = "ALL";
    else {
        var arr = new Array();
        $(".groundCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        ground = arr.join(",");
    }
    var landtype = "";
    if ($("#landtypeAll").prop("checked"))
        landtype = "ALL";
    else {
        var arr = new Array();
        $(".landtypeCheck:checked").each(function (i) {
            arr[i] = $(this).val();
        });
        landtype = arr.join(",");
    }

    QueryParam = {
        page: 1, //请求页数
        rows: 500, //每页行数
        sort: 'ObservationDateTime', //  排序字段
        order: 'desc',

        hour: 0,
        startTime: $('#datetimepicker1').val(),
        endTime: $('#datetimepicker2').val(),
        satellite: satellite,
        sky: sky,
        ground: ground,
        landtype: landtype,
        isbuffer: $("#isBuffer").prop("checked"),
        Country:isCountry,
        Province:province,
        City:city,
    }
    QueryFireInfo(QueryParam);
    


    if (IsMobile) {
        hideAdvancedQuery();
        showFireInfo();
    }
    else {
        $(".advanced-query").fadeOut(300);
    }
});




//$("#advancedQuery").click(function () {
//    $(".advanced-query").fadeToggle(300);
//});

$("#satelliteAll").change(function () {
    if ($(this).prop("checked")) {
        $(".satelliteCheck").prop("checked", true);
    } else {
        $(".satelliteCheck").prop("checked", false);
    }
});

$(".satelliteCheck").change(function () {
    if ($(".satelliteCheck:checked").length == $(".satelliteCheck").length) {
        $("#satelliteAll").prop("checked", true);
    }
    else {
        $("#satelliteAll").prop("checked", false);
    }
});


$("#skyAll").change(function () {
    if ($(this).prop("checked")) {
        $(".skyCheck").prop("checked", true);
    } else {
        $(".skyCheck").prop("checked", false);
    }
});

$(".skyCheck").change(function () {
    if ($(".skyCheck:checked").length == $(".skyCheck").length) {
        $("#skyAll").prop("checked", true);
    }
    else {
        $("#skyAll").prop("checked", false);
    }
});


$("#groundAll").change(function () {
    if ($(this).prop("checked")) {
        $(".groundCheck").prop("checked", true);
    } else {
        $(".groundCheck").prop("checked", false);
    }
});

$(".groundCheck").change(function () {
    if ($(".groundCheck:checked").length == $(".groundCheck").length) {
        $("#groundAll").prop("checked", true);
    }
    else {
        $("#groundAll").prop("checked", false);
    }
});


$("#landtypeAll").change(function () {
    if ($(this).prop("checked")) {
        $(".landtypeCheck").prop("checked", true);
    } else {
        $(".landtypeCheck").prop("checked", false);
    }
});

$(".landtypeCheck").change(function () {
    if ($(".landtypeCheck:checked").length == $(".landtypeCheck").length) {
        $("#landtypeAll").prop("checked", true);
    }
    else {
        $("#landtypeAll").prop("checked", false);
    }
});

//清空查询条件
$("#advancedqueryreset").click(function () {
    $("#satelliteAll").prop("checked", true);
    $("#satelliteAll").change();
    $("#skyAll").prop("checked", false);
    $("#skyAll").change();
    $("#groundAll").prop("checked", false);
    $("#groundAll").change();
    $("#landtypeAll").prop("checked", true);
    $("#landtypeAll").change();
    $("#isBuffer").prop("checked", false);
    $("#isCountry").prop("checked", false);
    $("#firstSelect").val("0");
    $("#secondSelect").val("0");

    $("#datetimepicker1").val("");
    $("#datetimepicker2").val("");
});
