package com.stenisway.wan_android.util;

import android.text.TextUtils;

public class StringUtil {

    public boolean isInvalid(String text){
        return TextUtils.isEmpty(text) || text.contains(" ");
    }

    public String replaceInvalidChar(String text) {
        return text.replace("<em class='highlight'>", "")
                .replace("</em>", "")
                .replace("&mdash;", "-")
                .replace("&ndash;", "-")
                .replace("&ldquo;", "'")
                .replace("&rdquo;", "'")
                .replace("&amp;","&");
    }
}
