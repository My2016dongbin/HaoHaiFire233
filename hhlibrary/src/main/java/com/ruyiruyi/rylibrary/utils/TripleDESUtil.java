package com.ruyiruyi.rylibrary.utils;



import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 3DES加解密
 *
 * @author howard
 */
public class TripleDESUtil {

    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish

    /**
     * 加密算法
     * password为加密密钥，长度为24字节
     * src为被加密的数据缓冲区（源）
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encryptMode(String password, byte[] src) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //生成密钥
        SecretKey deskey = new SecretKeySpec(password.getBytes(), Algorithm);
        //加密
        Cipher c1 = Cipher.getInstance(Algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return c1.doFinal(src);
    }

	   /* public static byte[] encryptMode(String password, byte[] src) {
               try {
		            //生成密钥
		            SecretKey deskey = new SecretKeySpec(password.getBytes(), Algorithm);
		            //加密
		            Cipher c1 = Cipher.getInstance(Algorithm);
		            c1.init(Cipher.ENCRYPT_MODE, deskey);
		            return c1.doFinal(src);
		            //return c1.doFinal(src);
		        } catch (java.security.NoSuchAlgorithmException e1) {
		        	log.error("未知",e1);
		            e1.printStackTrace();
		        } catch (javax.crypto.NoSuchPaddingException e2) {
		        	log.error("未知",e2);
		            e2.printStackTrace();
		        } catch (Exception e3) {
		        	log.error("未知",e3);
		            e3.printStackTrace();
		        }
		        return null;
		    }*/

    /**
     * 解密算法
     * keybyte为加密密钥，长度为24字节
     * src为被加密的数据缓冲区（源）
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptMode(String password, byte[] src) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //生成密钥
        SecretKey deskey = new SecretKeySpec(password.getBytes(), Algorithm);
        //解密
        Cipher c1 = Cipher.getInstance(Algorithm + "/ECB/NoPadding");
        c1.init(Cipher.DECRYPT_MODE, deskey);
        System.out.println("待解密内容长度" + src.length);
        return c1.doFinal(src);
    }

    /*  public static byte[] decryptMode(String password, byte[] src) {
          try {
                  //生成密钥
                  SecretKey deskey = new SecretKeySpec(password.getBytes(), Algorithm);
                  //解密
                  Cipher c1 = Cipher.getInstance(Algorithm+"/ECB/NoPadding");
                  c1.init(Cipher.DECRYPT_MODE, deskey);
                  System.out.println("待解密内容长度"+src.length);
                  return c1.doFinal(src);
              } catch (java.security.NoSuchAlgorithmException e1) {
                  log.error("未知",e1);
                  e1.printStackTrace();
              } catch (javax.crypto.NoSuchPaddingException e2) {
                  log.error("未知",e2);
                  e2.printStackTrace();
              } catch (Exception e3) {
                  log.error("未知",e3);
                  e3.printStackTrace();
              }
              return null;
          }*/
    //转换成十六进制字符串
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }

    /**
     * BASE64加密
     */
   /* public static byte[] encode(byte[] res) {
        try {
            return org.apache.commons.codec.binary.Base64.encodeBase64(res);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] decode(byte[] data) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(data);
    }*/

    public static String getRandomNum() {
        int a[] = new int[10];
        String result = "";
        for (int i = 0; i < a.length; i++) {
            a[i] = (int) (10 * (Math.random()));
            result = result + a[i] + "";
        }
        return result;
    }

    public static int getRangeRandom(int max, int min) {
        Random random = new Random();
        //System.out.print(s);
        return random.nextInt(max - min + 1) + min;
    }
    public static String getRandomNum(int Num){
        int a[] = new int[Num];
        String result = "";
        for(int i=0;i<a.length;i++) {
            a[i] = (int)(10*(Math.random()));
            result = result+a[i]+"";
        }
        return result;
    }

    public static byte[] MD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
        mdTemp.update(str.getBytes("UTF-8"));
        byte[] digest = mdTemp.digest();
        return digest;
    }


    public static String HMACSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
