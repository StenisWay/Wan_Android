package com.stenisway.wan_android.roomutil;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.search.hkbean.HKItem;

@Database(entities = {New_Item.class, Banner_Item.class, HKItem.class, CgItem.class, CgTitle.class}, version = 1, exportSchema = false)
public abstract class WanSqlLiteDatabase extends RoomDatabase {

    private static WanSqlLiteDatabase INSTANCE;
    static synchronized WanSqlLiteDatabase getDatabase(Context context){

        if (INSTANCE == null ){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WanSqlLiteDatabase.class, "wanAndroid_database")
//                    .addMigrations()
                    .build();
        }

        return INSTANCE;

    }

    public abstract ItemsDao getItemDao();

}
