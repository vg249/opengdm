System.register(["./data-set-extract"], function (exports_1, context_1) {
    "use strict";
    var data_set_extract_1, GobiiExtractorInstruction;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            }
        ],
        execute: function () {
            GobiiExtractorInstruction = class GobiiExtractorInstruction {
                constructor(dataSetExtracts, contactId, contactEmail, mapsetIds) {
                    this.dataSetExtracts = dataSetExtracts;
                    this.contactId = contactId;
                    this.contactEmail = contactEmail;
                    this.mapsetIds = mapsetIds;
                    this.dataSetExtracts = dataSetExtracts;
                }
                getDataSetExtracts() {
                    return this.dataSetExtracts;
                }
                setDataSetExtracts(value) {
                    this.dataSetExtracts = value;
                }
                getContactId() {
                    return this.contactId;
                }
                setContactId(contactId) {
                    this.contactId = contactId;
                }
                setContactEmail(contactEmail) {
                    this.contactEmail = contactEmail;
                }
                getContactEmail() {
                    return this.contactEmail;
                }
                setMapsetIds(mapsetIds) {
                    this.mapsetIds = mapsetIds;
                }
                getMapsetIds() {
                    return this.mapsetIds;
                }
                getJson() {
                    let returnVal = {};
                    returnVal.contactId = this.contactId;
                    returnVal.contactEmail = this.contactEmail;
                    returnVal.mapsetIds = this.mapsetIds;
                    returnVal.dataSetExtracts = [];
                    this.dataSetExtracts.forEach(e => {
                        returnVal.dataSetExtracts.push(e.getJson());
                    });
                    return returnVal;
                }
                static fromJson(json) {
                    let dataSetExtracts = [];
                    json.dataSetExtracts.forEach(e => dataSetExtracts.push(data_set_extract_1.GobiiDataSetExtract.fromJson(e)));
                    let returnVal = new GobiiExtractorInstruction(dataSetExtracts, json.contactId, json.contactEmail, json.mapsetIds);
                    return returnVal;
                }
            };
            exports_1("GobiiExtractorInstruction", GobiiExtractorInstruction);
        }
    };
});
//# sourceMappingURL=gobii-extractor-instruction.js.map