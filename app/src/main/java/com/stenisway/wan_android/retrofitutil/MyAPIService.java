package com.stenisway.wan_android.retrofitutil;

import com.stenisway.wan_android.banner.bannerbean.Banner_Items;
import com.stenisway.wan_android.categories.categoriesbean.CgBean;
import com.stenisway.wan_android.newitem.newsbean.NewItemBean;
import com.stenisway.wan_android.search.hkbean.HokeyItems;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyAPIService {

    @GET(Api.NewsUrl + "/{page}/json?page_size=40")
    Call<NewItemBean> getNewUrl(@Path("page") int page);

    @GET(Api.BannerUrl)
    Call<Banner_Items> getBannerUrl();

    @GET(Api.HotKeys)
    Call <HokeyItems> getHotKeys();

    @POST(Api.Search + "/{page}/json")
    Call<NewItemBean> getSearchUrl(@Path("page") int page, @Query("k") String searchTitle);

    @GET(Api.Categories)
    Call<CgBean> getCategories();

    @GET(Api.CategoriesDetail + "/{page}/json")
    Call<NewItemBean> getCategoriesDetail(@Path("page") int page, @Query("cid") int id);

}



