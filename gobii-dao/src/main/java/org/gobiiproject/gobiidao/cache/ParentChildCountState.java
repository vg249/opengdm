package org.gobiiproject.gobiidao.cache;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Date;

public class ParentChildCountState {

    private GobiiEntityNameType gobiiEntityNameTypeParent;
    private Date parentLastModified;
    private Integer parentId;
    private Date ChildLastModified;
    private GobiiEntityNameType gobiiEntityNameTypeChild;
private Integer count;
    public ParentChildCountState(GobiiEntityNameType gobiiEntityNameTypeParent,
                                 Date parentLastModified,
                                 Integer parentId,
                                 GobiiEntityNameType gobiiEntityNameTypeChild,
                                 Date childLastModified,
                                 Integer count) {
        this.gobiiEntityNameTypeParent = gobiiEntityNameTypeParent;
        this.parentLastModified = parentLastModified;
        this.parentId = parentId;
        this.gobiiEntityNameTypeChild = gobiiEntityNameTypeChild;
        ChildLastModified = childLastModified;
        this.count = count;
    }

    public GobiiEntityNameType getGobiiEntityNameTypeParent() {
        return gobiiEntityNameTypeParent;
    }

    public void setGobiiEntityNameTypeParent(GobiiEntityNameType gobiiEntityNameTypeParent) {
        this.gobiiEntityNameTypeParent = gobiiEntityNameTypeParent;
    }

    public Date getParentLastModified() {
        return parentLastModified;
    }

    public void setParentLastModified(Date parentLastModified) {
        this.parentLastModified = parentLastModified;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getChildLastModified() {
        return ChildLastModified;
    }

    public void setChildLastModified(Date childLastModified) {
        ChildLastModified = childLastModified;
    }

    public GobiiEntityNameType getGobiiEntityNameTypeChild() {
        return gobiiEntityNameTypeChild;
    }

    public void setGobiiEntityNameTypeChild(GobiiEntityNameType gobiiEntityNameTypeChild) {
        this.gobiiEntityNameTypeChild = gobiiEntityNameTypeChild;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
