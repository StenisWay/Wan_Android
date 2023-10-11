package com.stenisway.wan_android.roomutil;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.search.hkbean.HKItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemsRepository {

    private static ItemsRepository repository;

    public static ItemsRepository getInstance(Context context){
        if (repository == null){
            return repository = new ItemsRepository(context);
        }else {
            return repository;
        }
    }
    private final WanSqlLiteDatabase database;
    private final ItemsDao dao;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public ItemsRepository(Context context) {
        database = WanSqlLiteDatabase.getDatabase(context);
        dao = database.getItemDao();

    }

    public void everythingClose(){
        executor.shutdown();
        database.close();
    }

    public LiveData<List<New_Item>> useNewItemMethod(GetMethod method, New_Item...bean) {

        switch (method){

            case CLEAR:
                executor.submit(dao::deleteAllNewItem);
                break;

            case DELETE:
                executor.submit(() -> dao.deleteNewItem(bean));
                break;

            case INSERT:
                executor.submit(() -> dao.insertNewItem(bean));
                break;

            case UPDATE:
                executor.submit(() -> dao.updateNewItem(bean));
                break;
            case SELECT_ALL:
                return dao.getAllNewItemLive();
            case SELECT_DELETE:
                return dao.getAllDeleteItemLive();
            case SELECT_LATER_READ:
                return dao.getAllLaterReadNewItemLive();

            case SELECT_FAVORITE:
                return dao.getAllFavoriteNewItemLive();

        }
        return null;
    }

    public LiveData<New_Item> selectById(int id){
        return dao.getNewsItem(id);
    }

    public LiveData<List<HKItem>> useHKItemMethod(GetMethod method, HKItem...bean)  {
        switch (method){

            case CLEAR:
//                executor.submit(dao::deleteAllNewItem);
                return null;

            case DELETE:
//                executor.submit(() -> dao.deleteWords(bean));
                return null;

            case INSERT:
                executor.submit(() -> dao.insertHKItem(bean));
                return null;

            case UPDATE:
//                executor.submit(() -> dao.updateWords(bean));
                return null;
            case SELECT_ALL:
                return dao.getAllHKItemLive();
        }
        return null;
    }

    public LiveData<List<Banner_Item>> useBannerItemMethod(GetMethod method, Banner_Item... bean) {

        switch (method){

            case INSERT:
                executor.submit(() -> dao.insertBannerItem(bean));
                return null;

            case SELECT_ALL:
                return dao.getAllBannerItemLive();
        }
        return null;
    }

    public LiveData<List<CgTitle>> useCgTitleMethod(GetMethod method, CgTitle... bean){

        switch (method){

            case INSERT:
                executor.submit(() -> dao.insertCgTitle(bean));
                return null;
            case SELECT_ALL:
                return dao.getAllCgTitleLive();
        }
        return null;

    }

    public LiveData<List<CgItem>> useCgItemMethod(GetMethod method, CgItem... bean){

        switch (method){

            case INSERT:
                executor.submit(() -> dao.insertCgItem(bean));
                return null;
            case SELECT_ALL:
                return dao.getAllCgItemLive();
        }
        return null;

    }

}



