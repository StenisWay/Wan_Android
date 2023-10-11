package com.stenisway.wan_android.favorite;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

    private List<String> tabTitles;

    public List<String> getTabTitles() {
        return tabTitles;
    }

    public void setTabTitles(List<String> tabTitles) {
        this.tabTitles = tabTitles;
    }

}