package com.haohai.platform.fireforestplatform.ui.utils;

import android.content.Context;
import android.os.Bundle;

import com.haohai.platform.fireforestplatform.R;
import com.vsg.trustaccess.sdks.VSGService;

public class AuthTypeUtil {
	
	private static void basicInfoSet(Context context){

		Bundle bundle = new Bundle();
		bundle.putString(VSGService.Key.ACCESSMODE, VSGService.AccessMode.MODENC);
		bundle.putString(VSGService.Key.GATEWAY,"222.173.76.34");
		bundle.putString(VSGService.Key.PORT,"443");

	//	bundle.putString(VSGService.Key.SDKLOGPATH,"/sdcard/debug.txt");

		/*使用国密标准*/
	//	bundle.putString(VSGService.Key.USEGUOMISTANDARD,VSGService.BooleanType.TRUE);
		/*设置使用外部存储卡类型*/
        //bundle.putString(VSGService.EXTERNALKEYTYPE,ExternalKeyType.SANSECTF);
		VSGService.getInstance().commonParamInit(bundle);

		/*app的限制类型  0：不限制 1：白名单 2：黑名单*/
	//	VSGService.getInstance().setNcTunnelLimitedAppType(0);
	//	SortedSet<String> apps = new TreeSet<>();
		/*限制app包名*/
	//	apps.add("com.tencent.mm"); /*微信*/
	//	VSGService.getInstance().setNcTunnelLimitedAppList(apps);

		/*设置通知消息类型
		* 参数为 authNoticationID，icon，ncNoticationID，icon
		* authNoticationID和ncNoticationID使用未使用的notification id，不能相同*/
		VSGService.getInstance().setNotificationInfo(R.mipmap.ic_launcher,2,1);
	}

	public static void checkNetworkConnectivity(Context context){
		basicInfoSet(context);

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,true);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.CHECKNETWORKCONNECTIVITY);
		VSGService.getInstance().authStart(context,bundle);
	}
	
	public static void userPasswordAuth(Context context, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.USERNAMEPASSWORD);
		bundle.putString(VSGService.Key.USERNAME,"admin20G");
		bundle.putString(VSGService.Key.PASSWORD,"Hh123456@");
		VSGService.getInstance().authStart(context,bundle);
	}

	public static void certificateAuth(Context context, String p12cert, String p12certpwd, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.CERTIFICATE);
		bundle.putString(VSGService.CertParamKey.CERTSTORAGETYPE,VSGService.CertStorageType.LOCAL);
		bundle.putString(VSGService.CertParamKey.LOCALCERTP12CERT, p12cert);
		bundle.putString(VSGService.CertParamKey.LOCALCERTP12PASSWORD,p12certpwd);
		if(VSGService.getInstance().isCertDecryptEnable(p12cert)) {
			bundle.putBoolean(VSGService.CertParamKey.LOCALCERTP12ENCRYPTED, true);
		}else{
			bundle.putBoolean(VSGService.CertParamKey.LOCALCERTP12ENCRYPTED, false);
		}

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void certificateAuth(Context context, String signcert, String signcertpwd, String enccert, String encpwd, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.CERTIFICATE);
		bundle.putString(VSGService.CertParamKey.CERTSTORAGETYPE,VSGService.CertStorageType.LOCAL);
		bundle.putString(VSGService.CertParamKey.LOCALCERTSIGNCERT,signcert);
		bundle.putString(VSGService.CertParamKey.LOCALCERTSIGNCERTPASSWORD,signcertpwd);
		bundle.putString(VSGService.CertParamKey.LOCALCERTENCCERT,enccert);
		bundle.putString(VSGService.CertParamKey.LOCALCERTENCCERTPASSWORD,encpwd);

		VSGService.getInstance().authStart(context,bundle);
	}

	public static void certificateAuth(Context context, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.CERTIFICATE);
		bundle.putString(VSGService.CertParamKey.CERTSTORAGETYPE,VSGService.CertStorageType.EXTERNAL);

		bundle.putString(VSGService.CertParamKey.EXTERNALCERTPIN,"111111");
		bundle.putInt(VSGService.CertParamKey.EXTERNALCERTCONTAINERNUM,0);
		bundle.putString(VSGService.CertParamKey.EXTERNALCERTUSEABLE, VSGService.CertExternalCertUsable.SIGN);
		VSGService.getInstance().authStart(context,bundle);
	}

	public static void anonymityAuth(Context context, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.ANONYMITY);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void dynamicTokenAuth(Context context, String token, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.DYNAMICTOKEN);
		bundle.putString(VSGService.Key.DYNAMICTOKEN,token);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void sendSmsAuth(Context context, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.SENDSMS);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void smsAuth(Context context, String sms, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.SMS);
		bundle.putString(VSGService.Key.SMS,sms);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void terminalAuth(Context context, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.TERMINAL);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void commitTerminalInfoAuth(Context context, boolean isBaseAuth){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.TERMINALCOLLECTINFO);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void terminalGetRegisterInfo(Context context,boolean isBaseAuth){
		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.TERMINALGETREGISTERINFO);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void commitTerminalInfoAuth(Context context, boolean isBaseAuth,String extraInfo){

		Bundle bundle = new Bundle();
		bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
		bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.TERMINALCOLLECTINFO);
		bundle.putString(VSGService.Key.TERMINALCOLLECTINFO,extraInfo);

		VSGService.getInstance().authStart(context,bundle);
	}
	public static void pwdchangeAuth(Context context, String oldpwd, String newpwd, boolean isBaseAuth, boolean isFirstLogin){

			Bundle bundle = new Bundle();
			bundle.putBoolean(VSGService.Key.ISFRISTLOGINAUTH,isBaseAuth);
			if(isFirstLogin){
				bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.FIRSTLOGINPWDCHANGE);
		}else{
			bundle.putString(VSGService.Key.AUTHTYPE,VSGService.AuthenticateType.PWDCHANGE);
		}
		bundle.putString(VSGService.Key.OLDPASSWORD,oldpwd);
		bundle.putString(VSGService.Key.NEWPASSWORD,newpwd);

		VSGService.getInstance().authStart(context,bundle);
	}
}
