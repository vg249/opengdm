System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiDataSetExtract;
    return {
        setters:[],
        execute: function() {
            GobiiDataSetExtract = (function () {
                function GobiiDataSetExtract(gobiiFileType, accolate, dataSetId, dataSetName) {
                    this.gobiiFileType = gobiiFileType;
                    this.accolate = accolate;
                    this.dataSetId = dataSetId;
                    this.dataSetName = dataSetName;
                    this.setGobiiFileType(gobiiFileType);
                    this.setAccolate(accolate);
                    this.setDataSetName(dataSetName);
                    this.setDataSetId(dataSetId);
                } // ctor 
                GobiiDataSetExtract.prototype.getGobiiFileType = function () {
                    return this.gobiiFileType;
                };
                GobiiDataSetExtract.prototype.setGobiiFileType = function (gobiiFileType) {
                    this.gobiiFileType = gobiiFileType;
                };
                GobiiDataSetExtract.prototype.isAccolate = function () {
                    return this.accolate;
                };
                GobiiDataSetExtract.prototype.setAccolate = function (accolate) {
                    this.accolate = accolate;
                };
                GobiiDataSetExtract.prototype.getDataSetName = function () {
                    return this.dataSetName;
                };
                GobiiDataSetExtract.prototype.setDataSetName = function (dataSetName) {
                    this.dataSetName = dataSetName;
                };
                GobiiDataSetExtract.prototype.getDataSetId = function () {
                    return this.dataSetId;
                };
                GobiiDataSetExtract.prototype.setDataSetId = function (dataSetId) {
                    this.dataSetId = dataSetId;
                };
                GobiiDataSetExtract.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.gobiiFileType = this.gobiiFileType;
                    returnVal.accolate = this.accolate;
                    returnVal.dataSetName = this.dataSetName;
                    returnVal.dataSetId = this.dataSetId;
                    return returnVal;
                };
                GobiiDataSetExtract.fromJson = function (json) {
                    var returnVal = new GobiiDataSetExtract(json.gobiiFileType, json.accolate, json.dataSetId, json.dataSetName);
                    return returnVal;
                };
                return GobiiDataSetExtract;
            }());
            exports_1("GobiiDataSetExtract", GobiiDataSetExtract);
        }
    }
});
//# sourceMappingURL=data-set-extract.js.map