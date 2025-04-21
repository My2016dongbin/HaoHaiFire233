var Token = "";
var loginUrl = "http://web.ehaohai.com:2019/api/Register/RegisterAccount";
var localUrl = "http://web.ehaohai.com:2019/api/Register/Create";
//var localUrl = "http://27.223.18.10:2019/api/Register/Create";

function checkIP() {
    $.ajax({
        type: "get",
        url: 'http://pv.sohu.com/cityjson',
        dataType: "jsonp",    //跨域json请求一定是jsonp
        jsonp: "callback",//用于指示后台（php）获取数据
        async: false,
        timeout: 3000,
        success: function (data, status) {

        },
        error: function (e) {
            if (returnCitySN.cip == "web.ehaohai.com") {
                loginUrl = loginUrl.replace("web.ehaohai.com", "192.168.1.150");
                sessionStorage.setItem("myIP", returnCitySN.cip);
            }
        }
    });
}

/*检查用户登录信息是否存在*/
function checkmyToken() {
    if (sessionStorage.myToken == undefined) {
        sessionStorage.setItem("myToken", "NoData");
    }
}

/*读取cookie内容*/
function readUserInfo() {
    var username = $.cookie('username');
    var password = $.cookie('password');
    //alert(username + "-" + password);
    if (username != undefined && username != "null") {
        $("#username").attr("value", username);
    }
    //
    if (password != undefined && password != "null") {
        $("#password").attr("value", password);
        $("#RememberPwd").attr("checked", true);
    }
}

/*保存cookie内容*/
//function saveUserInfo() {
//    $.cookie('username', $("#username").val(), { expires: 7 });
//    $.cookie('password', $("#password").val(), { expires: 7 });
//}

///*清除cookie内容*/
//function clearUserInfo() {
//    $.cookie('username', null);
//    $.cookie('password', null);
//}

function checkAccountInfo() {    

    if ($("#UserName").val().length == 0) {
        $("#sysInfo").html("登陆ID不能为空");
        $("#UserName").focus();
        alert("用户名不能为空");
        return;
    }
    else {
        if ($("#TrueName").val().length == 0) {
            $("#sysInfo").html("真实姓名不能为空");
            $("#TrueName").focus();
            alert("真实姓名不能为空");
            return;
        }
        else {
            if ($("#MobileNumber").val().length == 0) {
                $("#sysInfo").html("联系电话不能为空");
                $("#MobileNumber").focus();
                alert("联系电话不能为空");
                return;
            }
            else {
                if ($("#OrganizationName").val().length == 0) {
                    $("#sysInfo").html("机构名称不能为空");
                    $("#OrganizationName").focus();
                    alert("机构名称不能为空");
                    return;
                }
                else {
                    if ($("[name='AdministrativeLevel']:eq(0)").prop("checked") == false && $("[name='AdministrativeLevel']:eq(1)").prop("checked") == false && $("[name='AdministrativeLevel']:eq(2)").prop("checked") == false) {
                        //if ($("#AdministrativeLevel").prop("checked") == undefined) {
                        $("#sysInfo").html("申请地区级别不能为空");
                        $("#AdministrativeProvince").focus();    
                        alert("申请地区级别不能为空");
                        return;
                    }
                    else {
                        if ($("#AdministrativeProvince").val().length == 0) {
                            $("#sysInfo").html("申请省份不能为空");
                            $("#AdministrativeProvince").focus();
                            alert("申请省份不能为空");
                            return;
                        }
                        else {
                            if ($("[name='AdministrativeLevel']:eq(1)").prop("checked") == true && $("#AdministrativeCity").val().length == 0) {
                                $("#sysInfo").html("申请市级选择不能为空");
                                $("#AdministrativeProvince").focus();
                                alert("申请市级选择不能为空");
                                return;
                            }
                            else {
                                if ($("[name='AdministrativeLevel']:eq(2)").prop("checked") == true && $("#AdministrativeCounty").val().length == 0) {
                                    $("#sysInfo").html("申请县级选择不能为空");
                                    $("#AdministrativeProvince").focus();
                                    alert("申请县级选择不能为空");
                                    return;
                                }
                                else {
                                    userRegister();
                                }
                            }
                        }
                    }
                }
            }
        }        
    }
    
}

function userRegister() {

    $.ajax({
        type: "post",
        url: localUrl,
        data: $("#Register").serialize(),
        //data: { UserName: $("#UserName").val(), TrueName: $("#TrueName").val(), MobileNumber: $("#MobileNumber").val(), Organization1: $("input[name='OrganizationType']:checked").val(), OrganizationName: $("#OrganizationName").val(), Level1: $("input[name='AdministrativeLevel']:checked").val(), AdministrativeProvince: $("#AdministrativeProvince").val(), AdministrativeCity: $("#AdministrativeCity").val(), AdministrativeCounty: $("#AdministrativeCounty").val() },
        //dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            if (status == "success") {
                //alert("status" + status);
                //alert("data" + data);
                if (data.type == 0) {
                    sessionStorage.myToken = "NoData";
                    $("#sysInfo").html("用户信息有误！");;
                    return;
                }
                //alert("登录成功:Token" + data.message);
                sessionStorage.myToken = data.message;
                //$("#myToken").html(sessionStorage.myToken);
                $(location).attr('href', 'login.html');
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            /*弹出jqXHR对象的信息*/
            alert(jqXHR.responseText);
            alert(jqXHR.status);
            alert(jqXHR.readyState);
            alert(jqXHR.statusText);
            /*弹出其他两个参数的信息*/
            /*alert(textStatus);
            alert(errorThrown);*/
            alert("用户已注册!请重新选择用户名");
        },
        complete: function () {

        }
    });

}

$(document).ready(function () {
    $("#distpicker").distpicker({
        autoSelect: false
    });
    $("#Level1").click(function () {
        $("#AdministrativeCity").hide();
        $("#AdministrativeCounty").hide();
        $("#citylabel").hide();
        $("#Countylabel").hide();

    });
    $("#Level2").click(function () {
        $("#AdministrativeCity").show();
        $("#AdministrativeCounty").hide();
        $("#citylabel").show();
        $("#Countylabel").hide();
    });
    $("#Level3").click(function () {
        $("#AdministrativeCity").show();
        $("#AdministrativeCounty").show();
        $("#citylabel").show();
        $("#Countylabel").show();

    });
});

