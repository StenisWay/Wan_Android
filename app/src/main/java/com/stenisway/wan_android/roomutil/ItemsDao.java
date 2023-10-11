package com.stenisway.wan_android.roomutil;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.search.hkbean.HKItem;

import java.util.List;

@Dao
interface ItemsDao {

    @Insert
    void insertNewItem(New_Item... newItems);

    @Insert
    void insertBannerItem(Banner_Item... bannerItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHKItem(HKItem... hkItems);

    @Insert
    void insertCgTitle(CgTitle... cgTitles);

    @Insert
    void insertCgItem(CgItem... cgItems);

    @Update
    void updateNewItem(New_Item ... items);

    @Delete
    void deleteNewItem(New_Item... items);

    @Query(" DELETE FROM New_Item")
    void deleteAllNewItem();

    @Query("SELECT * FROM CgTitle")
    LiveData<List<CgTitle>> getAllCgTitleLive();

    @Query("SELECT * FROM CgItem")
    LiveData<List<CgItem>> getAllCgItemLive();

    @Query("SELECT * FROM NEW_ITEM")
    LiveData<List<New_Item>> getAllNewItemLive();

    @Query("SELECT * FROM NEW_ITEM WHERE id = :id")
    LiveData<New_Item> getNewsItem(int id);

    @Query("SELECT * FROM NEW_ITEM WHERE favorite = 1")
    LiveData<List<New_Item>> getAllFavoriteNewItemLive();

    @Query("SELECT * FROM NEW_ITEM WHERE laterRead = 1")
    LiveData<List<New_Item>> getAllLaterReadNewItemLive();

    @Query("SELECT * FROM HKITEM ORDER BY ID DESC")
    LiveData<List<HKItem>> getAllHKItemLive();

    @Query("SELECT * FROM BANNER_ITEM ORDER BY ID DESC")
    LiveData<List<Banner_Item>> getAllBannerItemLive();

    @Query("SELECT * FROM NEW_ITEM WHERE laterRead = 0 and favorite = 0")
    LiveData<List<New_Item>> getAllDeleteItemLive();

}
