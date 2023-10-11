package com.stenisway.wan_android.newitem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.roomutil.GetMethod;
import com.stenisway.wan_android.roomutil.ItemsRepository;

public class NewsDetailViewModel extends AndroidViewModel {

    private final ItemsRepository repository;

    public NewsDetailViewModel(@NonNull Application application) {
        super(application);
        repository = ItemsRepository.getInstance(application);

    }

    private final MutableLiveData<New_Item> netItem = new MutableLiveData<>();


    private LiveData<New_Item> localItem;

    public void setNetItem(New_Item item) {
        netItem.setValue(item);
    }

    public LiveData<New_Item> getNetItem(){
        return netItem;
    }

    public LiveData<New_Item> getLocalItem(){
        return localItem;
    }

    public void getItemFromLocal(int id){
        localItem = repository.selectById(id);
    }

    public void updateNewItem(){
        repository.useNewItemMethod(GetMethod.UPDATE, netItem.getValue());
    }

    public void insertNewItem(){
        repository.useNewItemMethod(GetMethod.INSERT, netItem.getValue());
    }


}
