package com.stenisway.wan_android;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.banner.bannerbean.Banner_Items;
import com.stenisway.wan_android.categories.categoriesbean.CgBean;
import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.retrofitutil.MyAPIService;
import com.stenisway.wan_android.retrofitutil.RetrofitUtil;
import com.stenisway.wan_android.roomutil.GetMethod;
import com.stenisway.wan_android.roomutil.ItemsRepository;
import com.stenisway.wan_android.search.hkbean.HKItem;
import com.stenisway.wan_android.search.hkbean.HokeyItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_ViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getName();
    private final ItemsRepository repository;
    private final ConnectivityManager connectivityManager;

    private final MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>();

    private Timer timer;

    private Boolean disConnectIsShow = false;

    public Boolean getDisConnectIsShow() {
        return disConnectIsShow;
    }

    public void setDisConnectIsShow(Boolean disConnectIsShow) {
        this.disConnectIsShow = disConnectIsShow;
    }

    public Main_ViewModel(@NonNull Application application) {
        super(application);
        repository = ItemsRepository.getInstance(application);
        connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        isNetEnable();
    }

    public LiveData<Boolean> getNetSituation(){
        return isNetworkAvailable;
    }

    public LiveData<List<HKItem>> getHkLocal(){
        Log.d(TAG + "經過db", "成功");
        return repository.useHKItemMethod(GetMethod.SELECT_ALL);
    }

    public void isNetEnable(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (disConnectIsShow){
                    timer.cancel();
                }
                new Handler(Looper.getMainLooper()).post(() -> isNetworkAvailable.setValue(getIsNetworkAvailable()));
            }
        }, 0, 2000);
    }

    public void getHkOnNet(){

        MyAPIService myAPIService = RetrofitUtil.getInstance().getAPI();
        Call<HokeyItems> call = myAPIService.getHotKeys();
        call.enqueue(new Callback<HokeyItems>() {
            @Override
            public void onResponse(@NonNull Call<HokeyItems> call, @NonNull Response<HokeyItems> response) {
                assert response.body() != null;
                List<HKItem> items = response.body().data;
                for (HKItem item : items){
                    item.setName("         "+item.getName());
                }
                repository.useHKItemMethod(GetMethod.INSERT, items.toArray(new HKItem[0]));
            }

            @Override
            public void onFailure(@NonNull Call<HokeyItems> call, @NonNull Throwable t) {

            }
        });

    }

    public void getCategoriesTitleOnNet(){

        MyAPIService myAPIService = RetrofitUtil.getInstance().getAPI();
        Call<CgBean> call = myAPIService.getCategories();
        call.enqueue(new Callback<CgBean>() {
            @Override
            public void onResponse(@NonNull Call<CgBean> call, @NonNull Response<CgBean> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    List<CgTitle> list = response.body().data;
                    repository.useCgTitleMethod(GetMethod.INSERT, list.toArray(new CgTitle[0]));
                    for (CgTitle item: list){
                        repository.useCgItemMethod(GetMethod.INSERT, item.children.toArray(new CgItem[0]));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CgBean> call, @NonNull Throwable t) {

            }
        });

    }

    public void getBannerDataOnNet(){

        MyAPIService myAPIService = RetrofitUtil.getInstance().getAPI();
        Call<Banner_Items> call = myAPIService.getBannerUrl();
        call.enqueue(new Callback<Banner_Items>() {
            @Override
            public void onResponse(@NonNull Call<Banner_Items> call, @NonNull Response<Banner_Items> response) {
                assert response.body() != null;
                List<Banner_Item> list = response.body().data;
                repository.useBannerItemMethod(GetMethod.INSERT, list.toArray(new Banner_Item[0]));
            }

            @Override
            public void onFailure(@NonNull Call<Banner_Items> call, @NonNull Throwable t) {

            }
        });
    }

    public String[] toHKStringArray(List<HKItem> item){
        List<String> names = new ArrayList<>();
        for (int i = 0; i < item.size(); i++){
            names.add( item.get(i).getName());
            Log.d(TAG + "sql_hokey_array", item.get(i).getName());
        }
        return names.toArray(new String[0]);
    }

    public void deleteLocalUnnecessaryData(New_Item[] itemArray){
        repository.useNewItemMethod(GetMethod.DELETE, itemArray);

    }

    public LiveData<List<New_Item>> getDeleteItem(){
        return repository.useNewItemMethod(GetMethod.SELECT_DELETE);
    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        repository.everythingClose();
//        timer.cancel();
//    }


    public boolean getIsNetworkAvailable(){
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }
}
