package com.stenisway.wan_android.categories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.roomutil.GetMethod;
import com.stenisway.wan_android.roomutil.ItemsRepository;

import java.util.List;

public class CategoriesViewModel extends AndroidViewModel {

    private final ItemsRepository repository;
    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        repository = ItemsRepository.getInstance(application);
    }

    public LiveData<List<CgTitle>> getCgTitles() {
        return repository.useCgTitleMethod(GetMethod.SELECT_ALL);
    }

    public LiveData<List<CgItem>> getCgItem(){
        return repository.useCgItemMethod(GetMethod.SELECT_ALL);
    }

}