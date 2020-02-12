System.register([], function (exports_1, context_1) {
    "use strict";
    var GobiiDataSetExtract;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            GobiiDataSetExtract = class GobiiDataSetExtract {
                constructor(gobiiFileType, accolate, extractDestinationDirectory, gobiiExtractFilterType, markerList, sampleList, listFileName, gobiiSampleListType, gobiiDatasetType, platforms, principleInvestigator, project, dataSet, markerGroups) {
                    // this.setGobiiFileType(gobiiFileType);
                    // this.setAccolate(accolate);
                    // this.setDataSetId(dataSetId);
                    // this.setDataSetName(dataSetName);
                    // this.setExtractDestinationDirectory(extractDestinationDirectory);
                    // this.setGobiiFileType(gobiiExtractFilterType);
                    //
                    this.gobiiFileType = gobiiFileType;
                    this.accolate = accolate;
                    this.extractDestinationDirectory = extractDestinationDirectory;
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.markerList = markerList;
                    this.sampleList = sampleList;
                    this.listFileName = listFileName;
                    this.gobiiSampleListType = gobiiSampleListType;
                    this.gobiiDatasetType = gobiiDatasetType;
                    this.platforms = platforms;
                    this.principleInvestigator = principleInvestigator;
                    this.project = project;
                    this.dataSet = dataSet;
                    this.markerGroups = markerGroups;
                } // ctor 
                getgobiiFileType() {
                    return this.gobiiFileType;
                }
                setgobiiFileType(value) {
                    this.gobiiFileType = value;
                }
                getaccolate() {
                    return this.accolate;
                }
                setaccolate(value) {
                    this.accolate = value;
                }
                getextractDestinationDirectory() {
                    return this.extractDestinationDirectory;
                }
                setextractDestinationDirectory(value) {
                    this.extractDestinationDirectory = value;
                }
                getgobiiExtractFilterType() {
                    return this.gobiiExtractFilterType;
                }
                setgobiiExtractFilterType(value) {
                    this.gobiiExtractFilterType = value;
                }
                getmarkerList() {
                    return this.markerList;
                }
                setmarkerList(value) {
                    this.markerList = value;
                }
                getsampleList() {
                    return this.sampleList;
                }
                setsampleList(value) {
                    this.sampleList = value;
                }
                getlistFileName() {
                    return this.listFileName;
                }
                setlistFileName(value) {
                    this.listFileName = value;
                }
                getgobiiSampleListType() {
                    return this.gobiiSampleListType;
                }
                setgobiiSampleListType(value) {
                    this.gobiiSampleListType = value;
                }
                getgobiiDatasetType() {
                    return this.gobiiDatasetType;
                }
                setgobiiDatasetType(value) {
                    this.gobiiDatasetType = value;
                }
                getplatforms() {
                    return this.platforms;
                }
                setplatforms(value) {
                    this.platforms = value;
                }
                getJson() {
                    let returnVal = {};
                    returnVal.gobiiFileType = this.gobiiFileType;
                    returnVal.accolate = this.accolate;
                    returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
                    returnVal.gobiiExtractFilterType = this.gobiiExtractFilterType;
                    returnVal.markerList = this.markerList;
                    returnVal.sampleList = this.sampleList;
                    returnVal.listFileName = this.listFileName;
                    returnVal.gobiiSampleListType = this.gobiiSampleListType;
                    returnVal.gobiiDatasetType = this.gobiiDatasetType;
                    returnVal.platforms = this.platforms;
                    returnVal.principleInvestigator = this.principleInvestigator;
                    returnVal.project = this.project;
                    returnVal.dataSet = this.dataSet;
                    returnVal.markerGroups = this.markerGroups;
                    return returnVal;
                }
                static fromJson(json) {
                    let returnVal = new GobiiDataSetExtract(json.gobiiFileType, json.accolate, json.extractDestinationDirectory, json.gobiiExtractFilterType, json.markerList, json.sampleList, json.listFileName, json.gobiiSampleListType, json.gobiiDatasetType, json.platforms, json.principleInvestigator, json.project, json.dataSet, json.markerGroups);
                    return returnVal;
                }
            };
            exports_1("GobiiDataSetExtract", GobiiDataSetExtract);
        }
    };
});
//# sourceMappingURL=data-set-extract.js.map