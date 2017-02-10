System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiDataSetExtract;
    return {
        setters: [],
        execute: function () {
            GobiiDataSetExtract = (function () {
                function GobiiDataSetExtract(_gobiiFileType, _accolate, _dataSetId, _dataSetName, _extractDestinationDirectory, _gobiiExtractFilterType, _markerList, _sampleList, _listFileName, _gobiiSampleListType, _gobiiDatasetType, _platformIds) {
                    // this.setGobiiFileType(_gobiiFileType);
                    // this.setAccolate(_accolate);
                    // this.setDataSetId(_dataSetId);
                    // this.setDataSetName(_dataSetName);
                    // this.setExtractDestinationDirectory(_extractDestinationDirectory);
                    // this.setGobiiFileType(_gobiiExtractFilterType);
                    //
                    this._gobiiFileType = _gobiiFileType;
                    this._accolate = _accolate;
                    this._dataSetId = _dataSetId;
                    this._dataSetName = _dataSetName;
                    this._extractDestinationDirectory = _extractDestinationDirectory;
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._markerList = _markerList;
                    this._sampleList = _sampleList;
                    this._listFileName = _listFileName;
                    this._gobiiSampleListType = _gobiiSampleListType;
                    this._gobiiDatasetType = _gobiiDatasetType;
                    this._platformIds = _platformIds;
                } // ctor 
                GobiiDataSetExtract.prototype.getgobiiFileType = function () {
                    return this._gobiiFileType;
                };
                GobiiDataSetExtract.prototype.setgobiiFileType = function (value) {
                    this._gobiiFileType = value;
                };
                GobiiDataSetExtract.prototype.getaccolate = function () {
                    return this._accolate;
                };
                GobiiDataSetExtract.prototype.setaccolate = function (value) {
                    this._accolate = value;
                };
                GobiiDataSetExtract.prototype.getdataSetId = function () {
                    return this._dataSetId;
                };
                GobiiDataSetExtract.prototype.setdataSetId = function (value) {
                    this._dataSetId = value;
                };
                GobiiDataSetExtract.prototype.getdataSetName = function () {
                    return this._dataSetName;
                };
                GobiiDataSetExtract.prototype.setdataSetName = function (value) {
                    this._dataSetName = value;
                };
                GobiiDataSetExtract.prototype.getextractDestinationDirectory = function () {
                    return this._extractDestinationDirectory;
                };
                GobiiDataSetExtract.prototype.setextractDestinationDirectory = function (value) {
                    this._extractDestinationDirectory = value;
                };
                GobiiDataSetExtract.prototype.getgobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                GobiiDataSetExtract.prototype.setgobiiExtractFilterType = function (value) {
                    this._gobiiExtractFilterType = value;
                };
                GobiiDataSetExtract.prototype.getmarkerList = function () {
                    return this._markerList;
                };
                GobiiDataSetExtract.prototype.setmarkerList = function (value) {
                    this._markerList = value;
                };
                GobiiDataSetExtract.prototype.getsampleList = function () {
                    return this._sampleList;
                };
                GobiiDataSetExtract.prototype.setsampleList = function (value) {
                    this._sampleList = value;
                };
                GobiiDataSetExtract.prototype.getlistFileName = function () {
                    return this._listFileName;
                };
                GobiiDataSetExtract.prototype.setlistFileName = function (value) {
                    this._listFileName = value;
                };
                GobiiDataSetExtract.prototype.getgobiiSampleListType = function () {
                    return this._gobiiSampleListType;
                };
                GobiiDataSetExtract.prototype.setgobiiSampleListType = function (value) {
                    this._gobiiSampleListType = value;
                };
                GobiiDataSetExtract.prototype.getgobiiDatasetType = function () {
                    return this._gobiiDatasetType;
                };
                GobiiDataSetExtract.prototype.setgobiiDatasetType = function (value) {
                    this._gobiiDatasetType = value;
                };
                GobiiDataSetExtract.prototype.getplatformIds = function () {
                    return this._platformIds;
                };
                GobiiDataSetExtract.prototype.setplatformIds = function (value) {
                    this._platformIds = value;
                };
                GobiiDataSetExtract.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal._gobiiFileType = this._gobiiFileType;
                    returnVal._accolate = this._accolate;
                    returnVal._dataSetId = this._dataSetId;
                    returnVal._dataSetName = this._dataSetName;
                    returnVal._extractDestinationDirectory = this._extractDestinationDirectory;
                    returnVal._gobiiExtractFilterType = this._gobiiExtractFilterType;
                    returnVal._markerList = this._markerList;
                    returnVal._sampleList = this._sampleList;
                    returnVal._listFileName = this._listFileName;
                    returnVal._gobiiSampleListType = this._gobiiSampleListType;
                    returnVal._gobiiDatasetType = this._gobiiDatasetType;
                    returnVal._platformIds = this._platformIds;
                    return returnVal;
                };
                GobiiDataSetExtract.fromJson = function (json) {
                    var returnVal = new GobiiDataSetExtract(json._gobiiFileType, json._accolate, json._dataSetId, json._dataSetName, json._extractDestinationDirectory, json._gobiiExtractFilterType, json._markerList, json._sampleList, json._listFileName, json._gobiiSampleListType, json._gobiiDatasetType, json._platformIds);
                    return returnVal;
                };
                return GobiiDataSetExtract;
            }());
            exports_1("GobiiDataSetExtract", GobiiDataSetExtract);
        }
    };
});
//# sourceMappingURL=data-set-extract.js.map