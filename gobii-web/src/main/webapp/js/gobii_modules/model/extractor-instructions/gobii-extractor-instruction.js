System.register(["./data-set-extract"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var data_set_extract_1;
    var GobiiExtractorInstruction;
    return {
        setters:[
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            }],
        execute: function() {
            GobiiExtractorInstruction = (function () {
                function GobiiExtractorInstruction(extractDestinationDirectory, dataSetExtracts) {
                    this.extractDestinationDirectory = extractDestinationDirectory;
                    this.dataSetExtracts = dataSetExtracts;
                    this.extractDestinationDirectory = extractDestinationDirectory;
                    this.dataSetExtracts = dataSetExtracts;
                }
                GobiiExtractorInstruction.prototype.getExtractDestinationDirectory = function () {
                    return this.extractDestinationDirectory;
                };
                GobiiExtractorInstruction.prototype.setExtractDestinationDirectory = function (value) {
                    this.extractDestinationDirectory = value;
                };
                GobiiExtractorInstruction.prototype.getDataSetExtracts = function () {
                    return this.dataSetExtracts;
                };
                GobiiExtractorInstruction.prototype.setDataSetExtracts = function (value) {
                    this.dataSetExtracts = value;
                };
                GobiiExtractorInstruction.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
                    returnVal.dataSetExtracts = [];
                    this.dataSetExtracts.forEach(function (e) {
                        returnVal.dataSetExtracts.push(e.getJson());
                    });
                    return returnVal;
                };
                GobiiExtractorInstruction.fromJson = function (json) {
                    var dataSetExtracts = [];
                    json.dataSetExtracts.forEach(function (e) { return dataSetExtracts.push(data_set_extract_1.GobiiDataSetExtract.fromJson(e)); });
                    var returnVal = new GobiiExtractorInstruction(json.extractDestinationDirectory, dataSetExtracts);
                    return returnVal;
                };
                return GobiiExtractorInstruction;
            }());
            exports_1("GobiiExtractorInstruction", GobiiExtractorInstruction);
        }
    }
});
//# sourceMappingURL=gobii-extractor-instruction.js.map