var Token = "";
var loginUrl = "http://web.ehaohai.com:2019/api/Account/Login";
//var loginUrl = "http://192.168.1.160:2022/api/Account/Login";//2025


function checkIP()
{
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
            if (returnCitySN.cip == "web.ehaohai.com")
                         {
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
function saveUserInfo() {
    $.cookie('username', $("#username").val(), { expires: 7 });
    $.cookie('password', $("#password").val(), { expires: 7 });

}

/*清除cookie内容*/
function clearUserInfo() {
    $.cookie('username', null);
    $.cookie('password', null);
}

function checkUserInfo() {
    if ($("#username").val().length == 0) {
        //$("#sysInfo").html("用户名不能为空");
         isLogin("密码不能为空！");
        $("#username").focus();
        return;
    }
    else {

        if ($("#password").val().length == 0) {
           // $("#sysInfo").html("密码不能为空");
            $("#password").focus();
             isLogin("密码不能为空！");
            return;
        }
        else {
            userLogin();
        }
    }
}

function isLogin(alertContent)
{
   dsBridge.call("IsLogin", alertContent);
  //alert(dsBridge.call("IsLogin", alertContent));
}

function saveUser(userContent)
{

   dsBridge.call("saveUser",userContent);
}

function GetToken(myToken)
{
   dsBridge.call("GetToken", myToken);
}

function saveHser(hname,hpass)
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

//用户登录
function userLogin() {
    //checkUserInfo();

    /*监测记住密码是否选中*/
    if ($("#RememberPwd").is(':checked')) {
        saveUserInfo();

    }
    else {
        clearUserInfo();
    }
    var  users= $("#username").val()+"," +$("#password").val() ;

    var hname=$("#username").val();
    var hpss=$("#password").val();
    var deviceid=getDeviceId();

    $.ajax({
        type: "get",
        url: loginUrl,
        timeout:10000,
        data: {
        userName: $("#username").val(),
        password: $("#password").val(),
        deviceNo:deviceid
        },
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            if (status == "success") {
                if (data.type == 0) {
                    sessionStorage.myToken = "NoData";
                    $("#sysInfo").html(data.message);
                    $("#btnSumit").removeAttr("style","").html("登录");
                    return;
                }
                if(data.type == 1){
                   $("#sysInfo").html("登录成功！");
                }
                //alert("登录成功:Token" + data.message);
                sessionStorage.myToken = data.message;
                //$("#myToken").html(sessionStorage.myToken);
                //GetToken(data.message);
               // isLogin(window.location.href);


                 saveHser(hname,hpss);
                 saveUser(users);
                 setTimeout( window.location.href="file:///android_asset/index.html",1000);


                 //window.location.href="file:///android_asset/index.html";

              // $(location).attr('href', 'index.html');

            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            /*弹出jqXHR对象的信息*/
            /*alert(jqXHR.responseText);
            alert(jqXHR.status);
            alert(jqXHR.readyState);
            alert(jqXHR.statusText);*/
            /*弹出其他两个参数的信息*/
            /*alert(textStatus);
            alert(errorThrown);*/

            alert("登录失败!");
            $("#btnSumit").removeAttr("style","").html("登录");
        },
        complete: function (XMLHttpRequest,status) {
　           if(status=='timeout'){//超时,status还有success,error等值的情况
                   $("#sysInfo").html("操作超时，请重试！");
                   $("#btnSumit").removeAttr("style","").html("登录");
 　　　　    }
        }
    });

}

//自动登录
dsBridge.registerAsyn('autoLogin', function (arg1, arg2, arg3,responseCallback) {

    alert("g");
  $("#username").val(arg1);
  $("#password").val(arg2);
  $("#token").val(arg3);
  $("#RememberPwd").prop("checked", true);
  $("#btnSumit").css({
          "pointer-events":"none",
          "background-color":"#868686"
      }).html("登录中，请稍后.....");


    responseCallback("正在登录..." );
    alert(arg3);
    isAutoLogin(arg1,arg2)


    });
function isAutoLogin(username,pwd){

    alert("gg");
  $.ajax({
        type: "get",
        url: loginUrl,
        data: { userName:username , password: pwd,deviceNo:getDeviceId() },
        dataType: "jsonp",    //跨域json请求一定是jsonp
        success: function (data, status) {
            if (status == "success") {
                //alert("status" + status);
                //alert("data" + data);
                if (data.type == 0) {
                    sessionStorage.myToken = "NoData";
                    $("#sysInfo").html(data.message);
                    return;
                }
                if(data.type == 1){
                  $("#sysInfo").html("登录成功！");
                }
                sessionStorage.myToken = data.message;
                setTimeout( window.location.href="file:///android_asset/index.html",1000);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            /*弹出jqXHR对象的信息*/
            /*alert(jqXHR.responseText);
            alert(jqXHR.status);
            alert(jqXHR.readyState);
            alert(jqXHR.statusText);*/
            /*弹出其他两个参数的信息*/
            /*alert(textStatus);
            alert(errorThrown);*/
            alert("登录失败!");
        },
        complete: function () {

        }
    });
}