package com.adobe.aem.guides.wknd.core.models.impl;



import com.adobe.aem.guides.wknd.core.models.Link;
import com.adobe.aem.guides.wknd.core.models.LinkList;
import com.day.cq.wcm.api.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

import static java.lang.System.getProperties;

@Model(
        adaptables = {SlingHttpServletRequest.class, Resource.class},
        adapters = {LinkList.class},
        resourceType = {"wknd/components/linklist"},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)

public class DefaultLinkList implements LinkList {

    @ValueMapValue
    @Default(values="Default Title")
    private String title;

    @ValueMapValue
    private Boolean myCheckbox = false;

    @ValueMapValue
    private String orderBy = "";

    @ValueMapValue
    private String customPath = "";

    @ValueMapValue
    private List<Link> linksNameList = new ArrayList<>(); //List only with page's name and links

    @Inject
    private Page currentPage;

    @Inject
    private PageManager myPageManager;

    @Self
    private SlingHttpServletRequest request;

    private ResourceResolver myResourceResolver;
    public Iterator<Page> revisionsList;//Iterator with raw childs of given page
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkList.class);
    private String myCurrentPagePath;


    @PostConstruct
    public void init(){
        myCurrentPagePath = currentPage.getPath();
        myPageManager = currentPage.getPageManager();
        myPageManager.getPage(myCurrentPagePath);
        definePath();
        fillLinksNameList();
        if(myCheckbox)
            getAllChildren();
        defineOrder();
        LOGGER.error("checkbox es "+ myCheckbox);
    }



    private void defineOrder(){
        if(orderBy.equals("Alphabet"))
            orderByAlphabet();
        else if(orderBy.equals("Date"))
            orderByDate();
    }


    //Bubble sort by date
    private void orderByDate(){
        for(int i = 0 ; i<linksNameList.size(); i++){
            for(int j = 0; j<linksNameList.size(); j++){
                if(linksNameList.get(i).getDate().compareTo(linksNameList.get(j).getDate())<0){
                    Link auxLink = linksNameList.get(i);
                    linksNameList.set(i,linksNameList.get(j));
                    linksNameList.set(j,auxLink);
                }
            }
        }
        for(Link name : linksNameList)
            LOGGER.error("las fechas "+name.getDate().getTime().getTime());
    }


    //Bubble sort by name
    private void orderByAlphabet(){
        for(int i = 0 ; i<linksNameList.size(); i++){
            for(int j = 0; j<linksNameList.size(); j++){
                if(linksNameList.get(i).getLinkName().compareTo(linksNameList.get(j).getLinkName())<0){
                    Link auxLink = linksNameList.get(i);
                    linksNameList.set(i,linksNameList.get(j));
                    linksNameList.set(j,auxLink);
                }
            }
        }
        for(Link name : linksNameList)
            LOGGER.error("el valor "+name.getLinkName());
    }


    private void definePath(){
        if(customPath!=""){
            revisionsList = myPageManager.getPage(customPath).listChildren();
            LOGGER.error("checkbox antes de llamado es "+ myCheckbox);
        }
        else{
            revisionsList =  myPageManager.getPage(myCurrentPagePath).listChildren();
            LOGGER.error("checkbox antes de llamado es "+ myCheckbox);
        }

    }


   private void fillLinksNameList(){
       while(revisionsList.hasNext()){
           Page auxPage;
           auxPage = revisionsList.next();
           LinkImpl auxLink = new LinkImpl(auxPage.getName(), auxPage.getPath(), auxPage.getLastModified());
           linksNameList.add(auxLink);
           LOGGER.error("nombre de pagina "+auxPage.getName());
           LOGGER.error("path de la pagina"+auxPage.getPath());
       }
    }


    private void getAllChildren(){
        Iterator<Page> auxIteraPages = currentPage.listChildren(null, true);
        Page auxPage;
        Link auxLink;
        linksNameList.clear();
        while(auxIteraPages.hasNext()){
            auxPage = auxIteraPages.next();
            auxLink = new LinkImpl(auxPage.getName(), auxPage.getPath(), auxPage.getLastModified());
            linksNameList.add(auxLink);
            LOGGER.error("todos los hijosss "+auxPage.getName());
        }
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Boolean getMyCheckbox() {
        return myCheckbox;
    }

    @Override
    public String getCustomPath() {
        return customPath;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public List<Link> getLinksNameList() {
        return linksNameList;
    }
}
