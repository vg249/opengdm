package org.gobiiproject.gobiimodel.dto.gdmv3.templates;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.LinkageGroup;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;

import java.util.List;
import java.util.Map;

public class MarkerTemplateDTO {

    @GobiiEntityMap(paramName = "markerName", entity = Marker.class)
    private List<String> markerName;

    @GobiiEntityMap(paramName = "ref", entity = Marker.class)
    private  List<String> markerRef;

    @GobiiEntityMap(paramName = "alts", entity = Marker.class)
    private  List<String> markerAlt;

    @GobiiEntityMap(paramName = "start", entity = MarkerLinkageGroup.class)
    private  List<String> markerStart;

    @GobiiEntityMap(paramName = "stop", entity = MarkerLinkageGroup.class)
    private  List<String> markerEnd;

    @GobiiEntityMap(paramName = "sequence", entity = Marker.class)
    private  List<String> markerSequence;

    @GobiiEntityMap(paramName = "strandName", entity = Marker.class)
    private List<String> markerStrandName;

    @GobiiEntityMap(paramName = "linkageGroupName", entity = LinkageGroup.class)
    private List<String> linkageGroupName;

    @GobiiEntityMap(paramName = "linkageGroupStart", entity = LinkageGroup.class)
    private List<String> linkageGroupStart;

    @GobiiEntityMap(paramName = "linkageGroupStop", entity = LinkageGroup.class)
    private List<String> linkageGroupStop;

    private Map<String, List<String>> markerProperties;

    public List<String> getMarkerName() {
        return markerName;
    }

    public void setMarkerName(List<String> markerName) {
        this.markerName = markerName;
    }

    public List<String> getMarkerRef() {
        return markerRef;
    }

    public void setMarkerRef(List<String> markerRef) {
        this.markerRef = markerRef;
    }

    public List<String> getMarkerAlt() {
        return markerAlt;
    }

    public void setMarkerAlt(List<String> markerAlt) {
        this.markerAlt = markerAlt;
    }

    public List<String> getMarkerStart() {
        return markerStart;
    }

    public void setMarkerStart(List<String> markerStart) {
        this.markerStart = markerStart;
    }

    public List<String> getMarkerEnd() {
        return markerEnd;
    }

    public void setMarkerEnd(List<String> markerEnd) {
        this.markerEnd = markerEnd;
    }

    public List<String> getMarkerSequence() {
        return markerSequence;
    }

    public void setMarkerSequence(List<String> markerSequence) {
        this.markerSequence = markerSequence;
    }

    public List<String> getMarkerStrandName() {
        return markerStrandName;
    }

    public void setMarkerStrandName(List<String> markerStrandName) {
        this.markerStrandName = markerStrandName;
    }

    public List<String> getLinkageGroupName() {
        return linkageGroupName;
    }

    public void setLinkageGroupName(List<String> linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public List<String> getLinkageGroupStart() {
        return linkageGroupStart;
    }

    public void setLinkageGroupStart(List<String> linkageGroupStart) {
        this.linkageGroupStart = linkageGroupStart;
    }

    public List<String> getLinkageGroupStop() {
        return linkageGroupStop;
    }

    public void setLinkageGroupStop(List<String> linkageGroupStop) {
        this.linkageGroupStop = linkageGroupStop;
    }

    public Map<String, List<String>> getMarkerProperties() {
        return markerProperties;
    }

    public void setMarkerProperties(Map<String, List<String>> markerProperties) {
        this.markerProperties = markerProperties;
    }
}
