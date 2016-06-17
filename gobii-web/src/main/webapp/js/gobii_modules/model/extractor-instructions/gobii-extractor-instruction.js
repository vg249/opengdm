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
                function GobiiExtractorInstruction(extractDestinationDirectory, dataSetExtracts, contactId, contactEmail) {
                    this.extractDestinationDirectory = extractDestinationDirectory;
                    this.dataSetExtracts = dataSetExtracts;
                    this.contactId = contactId;
                    this.contactEmail = contactEmail;
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
                GobiiExtractorInstruction.prototype.getContactId = function () {
                    return this.contactId;
                };
                GobiiExtractorInstruction.prototype.setContactId = function (contactId) {
                    this.contactId = contactId;
                };
                GobiiExtractorInstruction.prototype.setContactEmail = function (contactEmail) {
                    this.contactEmail = contactEmail;
                };
                GobiiExtractorInstruction.prototype.getContactEmail = function () {
                    return this.contactEmail;
                };
                GobiiExtractorInstruction.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
                    returnVal.contactId = this.contactId;
                    returnVal.contactEmail = this.contactEmail;
                    returnVal.dataSetExtracts = [];
                    this.dataSetExtracts.forEach(function (e) {
                        returnVal.dataSetExtracts.push(e.getJson());
                    });
                    return returnVal;
                };
                GobiiExtractorInstruction.fromJson = function (json) {
                    var dataSetExtracts = [];
                    json.dataSetExtracts.forEach(function (e) { return dataSetExtracts.push(data_set_extract_1.GobiiDataSetExtract.fromJson(e)); });
                    var returnVal = new GobiiExtractorInstruction(json.extractDestinationDirectory, dataSetExtracts, json.contactId, json.contactEmail);
                    return returnVal;
                };
                return GobiiExtractorInstruction;
            }());
            exports_1("GobiiExtractorInstruction", GobiiExtractorInstruction);
        }
    }
});
//# sourceMappingURL=gobii-extractor-instruction.js.map