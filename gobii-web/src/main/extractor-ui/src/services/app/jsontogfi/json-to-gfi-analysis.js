System.register(["../../../model/gobii-file-item", "../../../model/type-entity", "../../../model/type-extractor-item", "../../../model/type-process", "../../../model/type-extractor-filter", "../../../model/analysis"], function (exports_1, context_1) {
    "use strict";
    var gobii_file_item_1, type_entity_1, type_extractor_item_1, type_process_1, type_extractor_filter_1, analysis_1, JsonToGfiAnalysis;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (analysis_1_1) {
                analysis_1 = analysis_1_1;
            }
        ],
        execute: function () {
            JsonToGfiAnalysis = /** @class */ (function () {
                function JsonToGfiAnalysis(_filterParams, _filterParamsColl) {
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                } //ctor
                JsonToGfiAnalysis.prototype.convert = function (jsonItem) {
                    var returnVal = null;
                    var analysis = new analysis_1.Analysis(jsonItem.analysisId, jsonItem.analysisName, jsonItem.analysisDescription, jsonItem.anlaysisTypeId, jsonItem.program, jsonItem.programVersion, jsonItem.algorithm, jsonItem.sourceName, jsonItem.sourceVersion, jsonItem.sourceUri, jsonItem.referenceId, jsonItem.timeExecuted, jsonItem.statusId);
                    // note that we are the parent id to the job status ID. This is because
                    // the primary filtering use case is by job status.
                    returnVal = gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, type_process_1.ProcessType.READ).setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setEntityType(type_entity_1.EntityType.ANALYSIS)
                        .setItemName(analysis.analysisName)
                        .setItemId(analysis.analysisId.toString())
                        .setEntity(analysis);
                    return returnVal;
                };
                return JsonToGfiAnalysis;
            }());
            exports_1("JsonToGfiAnalysis", JsonToGfiAnalysis);
        }
    };
});
//# sourceMappingURL=json-to-gfi-analysis.js.map