System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiDataSetExtract;
    return {
        setters:[],
        execute: function() {
            GobiiDataSetExtract = (function () {
                function GobiiDataSetExtract(dataSetId, dataSetName) {
                    this.gobiiFileType = null;
                    this.accolate = false;
                    this.dataSetName = null;
                    this.dataSetId = null;
                    this.dataSetName = dataSetName;
                    this.dataSetId = dataSetId;
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
                return GobiiDataSetExtract;
            }());
            exports_1("GobiiDataSetExtract", GobiiDataSetExtract);
        }
    }
});
//# sourceMappingURL=data-set-extract.js.map