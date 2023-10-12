package com.stenisway.wan_android.categories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.newitem.newsbean.NewItemBean;
import com.stenisway.wan_android.retrofitutil.MyAPIService;
import com.stenisway.wan_android.retrofitutil.RetrofitUtil;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesDetailViewModel extends ViewModel {

    private final String TAG = this.getClass().getName();
    public boolean needToScrollToTop = true;

    private final MutableLiveData<Integer> categoriesId = new MutableLiveData<>(-1);

    public LiveData<Integer> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(int categoriesId) {
        this.categoriesId.setValue(categoriesId);
    }

    private final MutableLiveData<List<New_Item>> _categories = new MutableLiveData<>();
    public LiveData<List<New_Item>> getCategoriesDetails() {
        return _categories;
    }
    private int CURRENT_PAGE = -1;

    public Boolean NEWS_ISLOADING = false;
    public int TOTAL_PAGE = 1;

    private final MutableLiveData<Boolean> nullItem = new MutableLiveData<>(false);

    public LiveData<Boolean> getNullItem() {
        return nullItem;
    }


    private Boolean isLastPage = false;

    public Boolean getIsLastPage() {
        return isLastPage;
    }

    public void getCategoriesData(int id){

        if(NEWS_ISLOADING){
            return;
        }

        NEWS_ISLOADING = true;

        CURRENT_PAGE += 1;

        if (CURRENT_PAGE > TOTAL_PAGE){
            isLastPage = true;
            return;
        }
        MyAPIService myAPIService = RetrofitUtil.getInstance().getAPI();
        Call<NewItemBean> call = myAPIService.getCategoriesDetail(CURRENT_PAGE, id);
        Log.d(TAG +  "id", id+"");
        Log.d(TAG + "page", CURRENT_PAGE+"");
        call.enqueue(new Callback<NewItemBean>() {
            @Override
            public void onResponse(@NonNull Call<NewItemBean> call, @NonNull Response<NewItemBean> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (TOTAL_PAGE == 1){
                        TOTAL_PAGE = response.body().getData().getPageCount();
                    }
                    List<New_Item> newsData = response.body().getData().getDatas();
                    NewItemBean item = response.body();
                    TOTAL_PAGE = response.body().getData().getPageCount();
                    if (item.getData().getPageCount() == 0){
                        nullItem.setValue(true);
                    }
                    Log.d(TAG + "cg_connectSuccess", response.body().getData().getDatas().toString());
                    Log.d(TAG + "cg_connectSuccessCurPage", "onResponse: " + response.body().getData().curpage);
                    Log.d(TAG + "cg_connectSuccess total", "onResponse: " + item.getData().getPageCount());
                    List<New_Item> newItemList = _categories.getValue();
                    if (newItemList != null){
                        Collections.sort(newItemList);
                        newItemList.addAll(newsData);
                        _categories.setValue(newItemList);
                    }else {
                        Collections.sort(newsData);
                        _categories.setValue(newsData);
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<NewItemBean> call, @NonNull Throwable t) {
                Log.e(TAG + "connectFail", t.toString());
            }
        });

        NEWS_ISLOADING = false;

    }


}