package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ManualLinkList;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Model(
        adaptables = {SlingHttpServletRequest.class, Resource.class},
        adapters = {ManualLinkList.class},
        resourceType = {"wknd/components/manuallinklist"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)

public class ManualLinkListImpl implements ManualLinkList {

    @Inject
    private Page currentPage;

    @Inject
    private PageManager myPageManager;

    @Self
    private SlingHttpServletRequest request;

    @Default(values = "Default title")
    @ValueMapValue
    private String title;

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualLinkList.class);
    private String myCurrentPagePath;

    @PostConstruct
    public void init(){
        currentPage.getPath();
        myPageManager = currentPage.getPageManager();
        myPageManager.getPage(myCurrentPagePath);

    }

    @Override
    public String getTitle() {
        return title;
    }
}
