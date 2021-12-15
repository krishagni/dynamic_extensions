package edu.common.dynamicextensions.query;

public class PathLink {
    private String refTab;
    
    private String refTabKey;
    
    private String key;

    private String extnKey = "RECORD_ID";

    private String formIdCol = "FORM_ID";

    private boolean toExtension = false;

    public PathLink() {
    }

    public PathLink(String key, String refTabKey) {
        this.key = key;
        this.refTabKey = refTabKey;
    }

    public PathLink(String key, String refTab, String refTabKey) {
        this.key = key;
        this.refTab = refTab;
        this.refTabKey = refTabKey;
    }

    public String getRefTab() {
        return refTab;
    }

    public void setRefTab(String refTab) {
        this.refTab = refTab;
    }

    public String getRefTabKey() {
        return refTabKey;
    }

    public void setRefTabKey(String refTabKey) {
        this.refTabKey = refTabKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExtnKey() {
        return extnKey;
    }

    public void setExtnKey(String extnKey) {
        this.extnKey = extnKey;
    }

    public String getFormIdCol() {
        return formIdCol;
    }

    public void setFormIdCol(String formIdCol) {
        this.formIdCol = formIdCol;
    }

    public boolean isToExtension() {
        return toExtension;
    }

    public void setToExtension(boolean toExtension) {
        this.toExtension = toExtension;
    }
}