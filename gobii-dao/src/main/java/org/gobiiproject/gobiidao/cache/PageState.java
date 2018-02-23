package org.gobiiproject.gobiidao.cache;

public class PageState {

    public PageState(Integer pageNumber, String nameValue, Integer idValue) {
        this.pageNumber = pageNumber;
        this.nameValue = nameValue;
        this.idValue = idValue;
    }

    Integer pageNumber;
    String nameValue;
    Integer idValue;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public String getNameValue() {
        return nameValue;
    }

    public Integer getIdValue() {
        return idValue;
    }
}
