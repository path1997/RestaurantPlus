package com.example.restaurantplus;

public class URLs {

    public static final String URL_PPHOTO= "http://carparts.5v.pl/photo/";
    private static final String ROOT_URL = "http://carparts.5v.pl/android/Api.php?apicall=";

    //-------------------------------------------ORDER AND CART---------------------------------------------------
    public static final String URL_GETCART ="http://carparts.5v.pl/android/cart.php?apicall=getcart";
    public static final String URL_MINUSITEM ="http://carparts.5v.pl/android/cart.php?apicall=minusitem";
    public static final String URL_PLUSITEM ="http://carparts.5v.pl/android/cart.php?apicall=plusitem";
    public static final String URL_REMOVEITEM ="http://carparts.5v.pl/android/cart.php?apicall=removeitem";
    public static final String URL_CONFIRMORDER ="http://carparts.5v.pl/android/order.php?apicall=confirmorder";
    public static final String URL_GETMYORDER ="http://carparts.5v.pl/android/order.php?apicall=getmyorder";
    public static final String URL_GETMYORDERDETAIL ="http://carparts.5v.pl/android/order.php?apicall=getmyorderdetail";
    public static final String URL_ADDRESERVATION ="http://carparts.5v.pl/android/order.php?apicall=addreservation";
    public static final String URL_REMOVERESERVATION ="http://carparts.5v.pl/android/order.php?apicall=removereservation";

    //-------------------------------------------------USER-------------------------------------------------------
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_CHANGEDATA ="http://carparts.5v.pl/android/Api.php?apicall=changedata";
    public static final String URL_CHANGEPASSWORD ="http://carparts.5v.pl/android/Api.php?apicall=changepassword";

    //------------------------------------------------PRODUCT-----------------------------------------------------
    public static final String URL_CATEGORY= "http://restaurantplus.5v.pl/android/products.php?apicall=category";
    public static final String URL_PRODUCTLIST= "http://restaurantplus.5v.pl/android/products.php?apicall=productlist";
    public static final String URL_PRODUCTLISTFORHOME= "http://restaurantplus.5v.pl/android/products.php?apicall=productlistforhome";
    public static final String URL_PRODUCTDETAIL= "http://restaurantplus.5v.pl/android/products.php?apicall=productdetail";
    public static final String URL_ADDPRODUCT ="http://carparts.5v.pl/android/cart.php?apicall=addproduct";

    //------------------------------------------------ANNOUNCEMENT------------------------------------------------
    public static final String URL_ALLANNOUNCEMENT ="http://carparts.5v.pl/android/announcement.php?apicall=allannouncement";
    public static final String URL_MYANNOUNCEMENT ="http://carparts.5v.pl/android/announcement.php?apicall=myannouncement";
    public static final String URL_ANNOUNCEMENTDETAIL ="http://carparts.5v.pl/android/announcement.php?apicall=announcementdetail";
    public static final String URL_ADDANNOUNCEMENT= "http://carparts.5v.pl/android/announcement.php?apicall=addannouncement";
    public static final String URL_DELETEANNOUNCEMENT= "http://carparts.5v.pl/android/announcement.php?apicall=deleteannouncement";
    public static final String URL_GETMYANNOUNCEMENT= "http://carparts.5v.pl/android/announcement.php?apicall=getmyannouncement";
    public static final String URL_EDITMYANNOUNCEMENT= "http://carparts.5v.pl/android/announcement.php?apicall=editmyannouncement";
}