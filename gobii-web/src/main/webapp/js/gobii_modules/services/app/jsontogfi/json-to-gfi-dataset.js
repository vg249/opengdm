System.register(["../../../model/gobii-file-item", "../../../model/type-entity", "../../../model/type-extractor-item", "../../../model/type-process", "../../../model/type-extractor-filter", "../../../model/dataset"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gobii_file_item_1, type_entity_1, type_extractor_item_1, type_process_1, type_extractor_filter_1, dataset_1, JsonToGfiDataset;
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
            function (dataset_1_1) {
                dataset_1 = dataset_1_1;
            }
        ],
        execute: function () {
            JsonToGfiDataset = (function () {
                function JsonToGfiDataset() {
                }
                JsonToGfiDataset.prototype.convert = function (jsonItem) {
                    var dataset = new dataset_1.DataSet(jsonItem.id, jsonItem.name, jsonItem.experimentId, jsonItem.callingAnalysisId, jsonItem.dataTable, jsonItem.dataFile, jsonItem.qualityTable, jsonItem.qualityFile, jsonItem.status, jsonItem.typeId, jsonItem.analysesIds, new Date(jsonItem.createdDate), jsonItem.jobStatusId, jsonItem.jobStatusName, jsonItem.jobSubmittedDate ? new Date(jsonItem.jobSubmittedDate) : null);
                    return gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, type_process_1.ProcessType.READ).setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setEntityType(type_entity_1.EntityType.DATASET)
                        .setItemName(dataset.name)
                        .setItemId(dataset.id.toString())
                        .setEntity(dataset);
                };
                return JsonToGfiDataset;
            }());
            exports_1("JsonToGfiDataset", JsonToGfiDataset);
        }
    };
});
//# sourceMappingURL=json-to-gfi-dataset.js.map