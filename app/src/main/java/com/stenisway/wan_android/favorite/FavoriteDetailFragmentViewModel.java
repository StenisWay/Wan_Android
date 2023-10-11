package com.stenisway.wan_android.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.roomutil.GetMethod;
import com.stenisway.wan_android.roomutil.ItemsRepository;

import java.util.List;

public class FavoriteDetailFragmentViewModel extends AndroidViewModel {


    private final ItemsRepository repository;

    public FavoriteDetailFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = ItemsRepository.getInstance(application);
    }

    public LiveData<List<New_Item>> getFavoriteList(){
        return repository.useNewItemMethod(GetMethod.SELECT_FAVORITE);
    }



}