package com.stenisway.wan_android.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.newitem.newsbean.NewItemBean;
import com.stenisway.wan_android.retrofitutil.MyAPIService;
import com.stenisway.wan_android.retrofitutil.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    public boolean needToScrollToTop = true;
    private final MutableLiveData<List<New_Item>> _search = new MutableLiveData<>();
    public LiveData<List<New_Item>> getSearch() {
        return _search;
    }
    private int CURRENT_PAGE = -1;

    private final String TAG = this.getClass().getName();

    public Boolean SEARCH_ISLOADING = false;
    public int TOTAL_PAGE = 1;

    public void getSearchData(String search){

        if(SEARCH_ISLOADING){
            return;
        }

        SEARCH_ISLOADING = true;

        CURRENT_PAGE += 1;

        if (CURRENT_PAGE > TOTAL_PAGE){
            return;
        }

        Log.d(TAG + "process_here_search)", "有執行到search");
        MyAPIService myAPIService = RetrofitUtil.getInstance().getAPI();
        Call<NewItemBean> call = myAPIService.getSearchUrl(CURRENT_PAGE, search);

        call.enqueue(new Callback<NewItemBean>() {
            @Override
            public void onResponse(@NonNull Call<NewItemBean> call, @NonNull Response<NewItemBean> response) {
                Log.d(TAG + "search_connectSuccess", "123");
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG + "search_connectSuccess", response.body().getData().getDatas().toString());
                    assert response.body() != null;
                    if (TOTAL_PAGE != 1){
                        TOTAL_PAGE = response.body().getData().getPageCount();
                    }
                    List<New_Item> searchData = response.body().getData().getDatas();
                    NewItemBean item = response.body();
                    TOTAL_PAGE = response.body().getData().getPageCount();
//                    如果取出的值為0或是null，很有可能是json格式解析錯誤
                    Log.d(TAG + "search_connectSuccess total", "onResponse: " + item.getData().getPageCount());
                    List<New_Item> newItemList = _search.getValue();
                    if (newItemList != null){
                        newItemList.addAll(searchData);
                        _search.setValue(newItemList);
                    }else {
                        _search.setValue(searchData);
                    }


                }else {
                    Log.d(TAG + "search_connectFail", "responseFail");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewItemBean> call, @NonNull Throwable t) {
                Log.d("search_connectFail", t.toString());
            }
        });
        SEARCH_ISLOADING = false;
    }



}