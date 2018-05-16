System.register(["../../../model/gobii-file-item", "../../../model/type-entity", "../../../model/type-extractor-item", "../../../model/type-process", "../../../model/type-extractor-filter", "../../../model/vertex"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gobii_file_item_1, type_entity_1, type_extractor_item_1, type_process_1, type_extractor_filter_1, vertex_1, JsonToGfiVertex;
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
            function (vertex_1_1) {
                vertex_1 = vertex_1_1;
            }
        ],
        execute: function () {
            JsonToGfiVertex = (function () {
                function JsonToGfiVertex(_filterParams, _filterParamsColl) {
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                } //ctor
                JsonToGfiVertex.prototype.convert = function (jsonItem) {
                    var vertex = new vertex_1.Vertex(jsonItem.vertexId, jsonItem.vertexName, jsonItem.gobiiEntityNameTypeName, jsonItem.cvGroupName, []);
                    var returnVal = gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, type_process_1.ProcessType.READ)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX)
                        .setEntityType(type_entity_1.EntityType.UNKNOWN)
                        .setItemName(vertex.vertexName)
                        .setItemId(vertex.vertexId.toString())
                        .setEntity(vertex);
                    return returnVal;
                };
                return JsonToGfiVertex;
            }());
            exports_1("JsonToGfiVertex", JsonToGfiVertex);
        }
    };
});
//# sourceMappingURL=json-to-gfi-vertex.js.map