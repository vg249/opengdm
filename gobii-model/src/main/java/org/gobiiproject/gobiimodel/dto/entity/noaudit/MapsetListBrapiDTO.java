package org.gobiiproject.gobiimodel.dto.entity.noaudit;

/**
 * Brapi DTO for Genome Maps List
 */
public class MapsetListBrapiDTO {

   private String comments;

   private String type;

   private String name;

   private Integer linkageGroupCount;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLinkageGroupCount() {
        return linkageGroupCount;
    }

    public void setLinkageGroupCount(Integer linkageGroupCount) {
        this.linkageGroupCount = linkageGroupCount;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    private Integer markerCount;

}
