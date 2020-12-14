package org.gobiiproject.gobiimodel.dto.gdmv3.templates;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectMap;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectMaps;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LinkageGroupTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MarkerLinkageGroupTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MarkerTable;
import org.gobiiproject.gobiimodel.entity.LinkageGroup;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerTemplateDTO {

    @GobiiAspectMaps(maps = {
        @GobiiAspectMap(aspectTable = MarkerTable.class),
        @GobiiAspectMap(aspectTable = MarkerLinkageGroupTable.class),
    })
    private List<String> markerName = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerTable.class)})
    private  List<String> markerRef = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerTable.class)})
    private  List<String> markerAlt = new ArrayList<>();

    @GobiiAspectMaps(maps = {
        @GobiiAspectMap(aspectTable = MarkerTable.class),
        @GobiiAspectMap(aspectTable = MarkerLinkageGroupTable.class),
    })
    private List<String> platformName = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerLinkageGroupTable.class)})
    private  List<String> markerStart = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerLinkageGroupTable.class)})
    private  List<String> markerEnd = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerTable.class)})
    private  List<String> markerSequence = new ArrayList<>();

    @GobiiAspectMaps(maps = {
        @GobiiAspectMap(aspectTable = LinkageGroupTable.class),
        @GobiiAspectMap(aspectTable = MarkerLinkageGroupTable.class)
    })
    private List<String> linkageGroupName = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = LinkageGroupTable.class)})
    private List<String> linkageGroupStart = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = LinkageGroupTable.class)})
    private List<String> linkageGroupStop = new ArrayList<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerTable.class)})
    private Map<String, List<String>> markerProperties = new HashMap<>();

    @GobiiAspectMaps(maps = {@GobiiAspectMap(aspectTable = MarkerTable.class)})
    private List<String> markerStrandName = new ArrayList<>();

    private List<String> markerReferenceName = new ArrayList<>();

    @GobiiAspectMaps(maps = {
        @GobiiAspectMap(paramName = "mapName", aspectTable = MarkerTable.class),
        @GobiiAspectMap(paramName = "mapName", aspectTable = MarkerLinkageGroupTable.class),
    })
    private List<String> genomeMapName = new ArrayList<>();

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

    public List<String> getPlatformName() {
        return platformName;
    }

    public void setPlatformName(List<String> platformName) {
        this.platformName = platformName;
    }

    public List<String> getMarkerReferenceName() {
        return markerReferenceName;
    }

    public void setMarkerReferenceName(List<String> markerReferenceName) {
        this.markerReferenceName = markerReferenceName;
    }

    public List<String> getGenomeMapName() {
        return genomeMapName;
    }

    public void setGenomeMapName(List<String> genomeMapName) {
        this.genomeMapName = genomeMapName;
    }
}
