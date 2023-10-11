package com.stenisway.wan_android.newitem.newsbean;

import java.util.List;

public class New_Items {
    public New_Items(List<New_Item> datas, int curpage) {
        this.datas = datas;
        this.curpage = curpage;
    }

    public List<New_Item> getDatas() {
        return datas;
    }

    private int pageCount;
    public void setDatas(List<New_Item> datas) {
        this.datas = datas;
    }

    private List<New_Item> datas;
    public int curpage;

    public int getPageCount() {
        return pageCount;
    }


}
