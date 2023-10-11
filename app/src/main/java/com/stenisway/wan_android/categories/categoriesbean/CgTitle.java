package com.stenisway.wan_android.categories.categoriesbean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class CgTitle {
    @Ignore
    public List<CgItem> children;

    @ColumnInfo(name = "name")
    public String name;
    @PrimaryKey
    private int id;

    public List<CgItem> getChildren() {
        return children;
    }

    public void setChildren(List<CgItem> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
