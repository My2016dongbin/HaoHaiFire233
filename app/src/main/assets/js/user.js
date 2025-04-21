/*检查用户是否登录*/
var deviceId;
function checkUserLogin() {
  //alert(sessionStorage.myToken);
    if (sessionStorage.myToken == undefined) {
       // alert(sessionStorage.myToken);
        sessionStorage.setItem("myToken", "NoData");
        $(location).attr('href', 'login.html');
    }
    else {
        if (sessionStorage.myToken != "NoData") { //已经登录
        }
        else {  //用户未登录，跳转到登录页面
            $(location).attr('href', 'login.html');
        }
    }
}

function  getDeviceId(){
    return jk.getDeviceId();
}