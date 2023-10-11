package com.stenisway.wan_android.newitem.newsbean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class New_Item implements Serializable, Comparable<New_Item>{

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "chapterName")
    private String chapterName;
    @ColumnInfo(name = "link")
    private String link;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "niceDate")
    private String niceDate;

    @ColumnInfo(name = "favorite", defaultValue = "false")
    private Boolean favorite = false;

    @ColumnInfo(name = "laterRead", defaultValue = "false")
    private Boolean laterRead = false;

    @ColumnInfo(name = "publishTime")
    private double publishTime;

    public double getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(double publishTime) {
        this.publishTime = publishTime;
    }

    public Boolean getLaterRead() {
        return laterRead;
    }

    public void setLaterRead(Boolean laterRead) {
        this.laterRead = laterRead;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    @Override
    public int compareTo(New_Item other) {
        return (int)(other.publishTime - this.publishTime);
    }
}
