package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class Path
{
    private String startForm;
    
    private String endForm;
    
    private String startField;
    
    private boolean wildCard;

    private boolean forTopLevelDynaEntity;

    private List<PathLink> links = new ArrayList<>();

    public Path() {
    }

    public void addLink(String key, String refTabKey) {
        links.add(new PathLink(key, refTabKey));
    }

    public void addLink(String key, String refTab, String refTabKey) {
        links.add(new PathLink(key, refTab, refTabKey));
    }

    public String getStartForm() {
        return startForm;
    }

    public void setStartForm(String startForm) {
        this.startForm = startForm;
    }

    public String getEndForm() {
        return endForm;
    }

    public void setEndForm(String endForm) {
        this.endForm = endForm;
    }

    public String getStartField() {
        return startField;
    }

    public void setStartField(String startField) {
        this.startField = startField;
    }

    public List<PathLink> getLinks() {
        return links;
    }

    public void setLinks(List<PathLink> links) {
        this.links = links;
    }
    
    public boolean isWildCard() {
    	return wildCard;
    }
    
    public void setWildCard(boolean wildCard) {
    	this.wildCard = wildCard;
    }

    public boolean isForTopLevelDynaEntity() {
        return forTopLevelDynaEntity;
    }

    public void setForTopLevelDynaEntity(boolean forTopLevelDynaEntity) {
        this.forTopLevelDynaEntity = forTopLevelDynaEntity;
    }
}