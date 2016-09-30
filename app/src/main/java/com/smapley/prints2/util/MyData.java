package com.smapley.prints2.util;

/**
 * Created by smapley on 2015/5/20.
 */
public class MyData {

    public static String UserName;
    public static String PassWord;
    public static int User = 0;
    public static String UserName1;
    public static String UserName2;
    public static String PassWord1;
    public static String PassWord2;
    public static boolean Login1 = false;
    public static boolean Login2 = false;

    public static String IP;

    private static String URL_INDEX1 = "xiazhuKX.php";
    private static String URL_TUIMA = "tuimaX1.php";
    private static String URL_GETJILU1 = "getJilu1.php";
    private static String URL_DAYIN = "dayin.php";
    private static String URL_GETJILU2 = "getJilu2.php";
    private static String URL_updateZt1 = "updateZt1.php";
    private static String URL_getSoudj = "getSoudj.php";
    private static String URL_updateZt2 = "updateZt2.php";
    private static String URL_reg = "reg9.php";
    private static String URL_GETZILIAO = "getZiliao.php";
    private static String URL_UPDATAZILIAO = "updateZiliao1.php";
    private static String URL_GETMINGXI = "getMingxi.php";
    private static String URL_GETJIANG = "getJiang1.php";
    private static String URL_GENGXIN = "gengxin.php";
    private static String URL_Reg2 = "reg2.php";
    private static String URL_GETZHANGDAN = "getZhangdan.php";
    private static String URL_DELTINGYA = "delTingya.php";
    private static String URL_reggaimi = "reggaimi.php";
    private static String URL_getTongzhi = "getTongzhi.php";
    private static String URL_XIAZAI = "dayin1.apk";


    public static String getBaseUrl() {
        return "http://" + IP + "/neibu/";
    }

    public static String getUrlXiazai() {
        return getBaseUrl() + URL_XIAZAI;
    }

    public static String getUrlIndex1() {
        return getBaseUrl() + URL_INDEX1;
    }

    public static String getUrlTuima() {
        return getBaseUrl() + URL_TUIMA;
    }

    public static String getUrlGetjilu1() {
        return getBaseUrl() + URL_GETJILU1;
    }
    public static String getUrlDayin() {
        return getBaseUrl() + URL_DAYIN;
    }

    public static String getUrlGetjilu2() {
        return getBaseUrl() + URL_GETJILU2;
    }

    public static String getURL_updateZt1() {
        return getBaseUrl() + URL_updateZt1;
    }

    public static String getURL_getSoudj() {
        return getBaseUrl() + URL_getSoudj;
    }

    public static String getURL_updateZt2() {
        return getBaseUrl() + URL_updateZt2;
    }

    public static String getURL_reg() {
        return getBaseUrl() + URL_reg;
    }

    public static String getUrlGetziliao() {
        return getBaseUrl() + URL_GETZILIAO;
    }

    public static String getUrlUpdataziliao() {
        return getBaseUrl() + URL_UPDATAZILIAO;
    }

    public static String getUrlGetmingxi() {
        return getBaseUrl() + URL_GETMINGXI;
    }

    public static String getUrlGetjiang() {
        return getBaseUrl() + URL_GETJIANG;
    }

    public static String getUrlGengxin() {
        return getBaseUrl() + URL_GENGXIN;
    }

    public static String getURL_Reg2() {
        return getBaseUrl() + URL_Reg2;
    }

    public static String getUrlGetzhangdan() {
        return getBaseUrl() + URL_GETZHANGDAN;
    }

    public static String getUrlDeltingya() {
        return getBaseUrl() + URL_DELTINGYA;
    }

    public static String getURL_reggaimi() {
        return getBaseUrl() + URL_reggaimi;
    }

    public static String getURL_getTongzhi() {
        return getBaseUrl() + URL_getTongzhi;
    }
}
