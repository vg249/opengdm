System.register(["./type-entity", "./cv-filter-type", "./file-model-node", "./guid"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_filter_type_1, file_model_node_1, guid_1, GobiiTreeNode;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (guid_1_1) {
                guid_1 = guid_1_1;
            }
        ],
        execute: function () {
            GobiiTreeNode = (function () {
                function GobiiTreeNode(parent, fileModelNodeId, fileItemId, required) {
                    this._itemType = file_model_node_1.ExtractorItemType.ENTITY;
                    this._entityType = type_entity_1.EntityType.UNKNOWN;
                    this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    this.children = [];
                    this.required = false;
                    this._active = false;
                    this._id = guid_1.Guid.generateUUID();
                    this.parent = parent;
                    this.fileModelNodeId = fileModelNodeId;
                    this.fileItemId = fileItemId;
                    this.required = required;
                }
                GobiiTreeNode.prototype.getId = function () {
                    return this._id;
                };
                //unique identifiers
                GobiiTreeNode.prototype.getItemType = function () {
                    return this._itemType;
                };
                GobiiTreeNode.prototype.setItemType = function (value) {
                    this._itemType = value;
                };
                GobiiTreeNode.prototype.getEntityType = function () {
                    return this._entityType;
                };
                GobiiTreeNode.prototype.setEntityType = function (value) {
                    this._entityType = value;
                };
                GobiiTreeNode.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                GobiiTreeNode.prototype.setEntitySubType = function (value) {
                    this._entitySubType = value;
                };
                GobiiTreeNode.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                GobiiTreeNode.prototype.setCvFilterType = function (value) {
                    this._cvFilterType = value;
                };
                GobiiTreeNode.prototype.getActive = function () {
                    return this._active;
                };
                GobiiTreeNode.prototype.setActive = function (value) {
                    this._active = value;
                };
                return GobiiTreeNode;
            }());
            exports_1("GobiiTreeNode", GobiiTreeNode);
        }
    };
});
//# sourceMappingURL=GobiiTreeNode.js.map