package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.Link;

import java.util.Calendar;

public class LinkImpl implements Link {

    String linkName;
    String linkUrl;
    Calendar date;

    public LinkImpl(String linkName, String linkUrl, Calendar date) {
        this.linkName = linkName;
        this.linkUrl = linkUrl;
        this.date = date;
    }



    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public String getLinkName() {
        return linkName;
    }

    @Override
    public String getLinkUrl() {
        return linkUrl;
    }

}
