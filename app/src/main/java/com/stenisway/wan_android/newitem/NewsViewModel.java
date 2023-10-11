package com.stenisway.wan_android.newitem;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.newitem.newsbean.NewItemBean;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.retrofitutil.MyAPIService;
import com.stenisway.wan_android.retrofitutil.RetrofitUtil;
import com.stenisway.wan_android.roomutil.GetMethod;
import com.stenisway.wan_android.roomutil.ItemsRepository;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getName();

    public boolean needToScrollToTop = true;

    private final MutableLiveData<List<New_Item>> _news = new MutableLiveData<>();

    private final ItemsRepository repository;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        getNewsData();
        repository = ItemsRepository.getInstance(application);
    }

    public LiveData<List<New_Item>> getNews() {
        return _news;
    }
    private int CURRENT_PAGE = -1;

    public Boolean NEWS_ISLOADING = false;
    public int TOTAL_PAGE = 1;

    public LiveData<List<Banner_Item>> getBanner(){
        return repository.useBannerItemMethod(GetMethod.SELECT_ALL);
    }

    public void getNewsData(){

        if(NEWS_ISLOADING){
            return;
        }

        NEWS_ISLOADING = true;

        CURRENT_PAGE += 1;

        if (CURRENT_PAGE > TOTAL_PAGE){
            return;
        }
        MyAPIService myAPIService = RetrofitUtil.getInstance().getAPI();
        Call<NewItemBean> call = myAPIService.getNewUrl(CURRENT_PAGE);
        call.enqueue(new Callback<NewItemBean>() {
            @Override
            public void onResponse(@NonNull Call<NewItemBean> call, @NonNull Response<NewItemBean> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG + "connectSuccess", response.body().getData().getDatas().toString());
                    Log.d(TAG + "connectSuccessCurPage", "onResponse: " + response.body().getData().curpage);
                    assert response.body() != null;
                    if (TOTAL_PAGE == 1){
                        TOTAL_PAGE = response.body().getData().getPageCount();
                    }
                    List<New_Item> newsData = response.body().getData().getDatas();
                    NewItemBean item = response.body();
                    TOTAL_PAGE = response.body().getData().getPageCount();
//                    如果取出的值為0或是null，很有可能是json格式解析錯誤
                    Log.d(TAG +"connectSuccess total", "onResponse: " + item.getData().getPageCount());
                    List<New_Item> newItemList = _news.getValue();
                    if (newItemList != null){
                        newItemList.addAll(newsData);
                        Collections.sort(newItemList);
                        _news.setValue(newItemList);
                        for (int i = 0; i < newItemList.size(); i++) {
                            Log.d(TAG + "順序", "日期: " + newItemList.get(i).getNiceDate() + "分享日期: " + newItemList.get(i).getPublishTime());
                        }
                    }else {
                        Collections.sort(newsData);
                        _news.setValue(newsData);
                        for (int i = 0; i < newsData.size(); i++) {
                            Log.d(TAG + "順序", newsData.get(i).getNiceDate() +  "分享日期: " + newsData.get(i).getPublishTime() );
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<NewItemBean> call, @NonNull Throwable t) {
                Log.e(TAG + "connectFail", "connectFail");
            }
        });

        NEWS_ISLOADING = false;

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}