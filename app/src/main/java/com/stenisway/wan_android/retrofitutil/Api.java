package com.stenisway.wan_android.retrofitutil;

public class Api {
    static final String baseUrl = "https://www.wanandroid.com/";
//  基本的Url

    static final String NewsUrl = "article/list";
//  最新文章

    static final String BannerUrl = "banner/json";
//  廣告板

    static final String Categories = "tree/json";
//    分類項的標題

    static final String CategoriesDetail = "article/list";
//    分類項的細項(id要從上面的標題api取得)

    static final String HotKeys = "hotkey/json";

//    搜尋熱鍵
    static final String Search = "article/query";

}
