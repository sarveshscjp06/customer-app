package com.samcrm.vo;


public class SearchDataFilterVO {
    private Object dataObj;
    private boolean isSelected;

    public SearchDataFilterVO(Object obj) {
        this.dataObj = obj;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}