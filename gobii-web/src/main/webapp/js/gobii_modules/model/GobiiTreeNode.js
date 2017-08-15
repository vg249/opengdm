System.register(["./type-entity", "./cv-filter-type", "./file-model-node", "./guid", "./type-extractor-filter"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_filter_type_1, file_model_node_1, guid_1, type_extractor_filter_1, ContainerType, GobiiTreeNode;
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
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            }
        ],
        execute: function () {
            (function (ContainerType) {
                ContainerType[ContainerType["NONE"] = 0] = "NONE";
                ContainerType[ContainerType["TREE_NODE"] = 1] = "TREE_NODE";
                ContainerType[ContainerType["ITEM_NODE"] = 2] = "ITEM_NODE";
            })(ContainerType || (ContainerType = {}));
            exports_1("ContainerType", ContainerType);
            GobiiTreeNode = (function () {
                function GobiiTreeNode(parent, fileModelNodeId, fileItemId, required) {
                    this.itemType = file_model_node_1.ExtractorItemType.ENTITY;
                    this.entityType = type_entity_1.EntityType.UNKNOWN;
                    this.entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    this.cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    //UI properties
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.children = [];
                    this.required = false;
                    this.active = false;
                    this.containerType = ContainerType.NONE;
                    this.id = guid_1.Guid.generateUUID();
                    this.parent = parent;
                    this.fileModelNodeId = fileModelNodeId;
                    this.fileItemId = fileItemId;
                    this.required = required;
                }
                GobiiTreeNode.build = function (gobiiExtractFilterType, extractoItemType) {
                    var returnVal = new GobiiTreeNode(null, null, null, null);
                    returnVal.gobiiExtractFilterType = gobiiExtractFilterType;
                    returnVal.setItemType(extractoItemType);
                    return returnVal;
                }; //build
                GobiiTreeNode.prototype.getId = function () {
                    return this.id;
                };
                //unique identifiers
                GobiiTreeNode.prototype.getItemType = function () {
                    return this.itemType;
                };
                GobiiTreeNode.prototype.setItemType = function (value) {
                    this.itemType = value;
                    return this;
                };
                GobiiTreeNode.prototype.getEntityType = function () {
                    return this.entityType;
                };
                GobiiTreeNode.prototype.setEntityType = function (value) {
                    this.entityType = value;
                    return this;
                };
                GobiiTreeNode.prototype.getEntitySubType = function () {
                    return this.entitySubType;
                };
                GobiiTreeNode.prototype.setEntitySubType = function (value) {
                    this.entitySubType = value;
                    return this;
                };
                GobiiTreeNode.prototype.setGobiiExtractFilterType = function (gobiiExtractFilterType) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    return this;
                };
                GobiiTreeNode.prototype.getGobiiExtractFilterType = function () {
                    return this.gobiiExtractFilterType;
                };
                GobiiTreeNode.prototype.getActive = function () {
                    return this.active;
                };
                GobiiTreeNode.prototype.setActive = function (value) {
                    this.active = value;
                    return this;
                };
                GobiiTreeNode.prototype.getCvFilterType = function () {
                    return this.cvFilterType;
                };
                GobiiTreeNode.prototype.setCvFilterType = function (value) {
                    this.cvFilterType = value;
                    return this;
                };
                GobiiTreeNode.prototype.getLabel = function () {
                    return this.label;
                };
                GobiiTreeNode.prototype.setLabel = function (value) {
                    this.label = value;
                    return this;
                };
                GobiiTreeNode.prototype.getData = function () {
                    return this.data;
                };
                GobiiTreeNode.prototype.setData = function (value) {
                    this.data = value;
                    return this;
                };
                GobiiTreeNode.prototype.getIcon = function () {
                    return this.icon;
                };
                GobiiTreeNode.prototype.setIcon = function (value) {
                    this.icon = value;
                    return this;
                };
                GobiiTreeNode.prototype.getExpandedIcon = function () {
                    return this.expandedIcon;
                };
                GobiiTreeNode.prototype.setExpandedIcon = function (value) {
                    this.expandedIcon = value;
                    return this;
                };
                GobiiTreeNode.prototype.getCollapsedIcon = function () {
                    return this.collapsedIcon;
                };
                GobiiTreeNode.prototype.setCollapsedIcon = function (value) {
                    this.collapsedIcon = value;
                    return this;
                };
                GobiiTreeNode.prototype.getChildren = function () {
                    return this.children;
                };
                GobiiTreeNode.prototype.setChildren = function (value) {
                    this.children = value;
                    return this;
                };
                GobiiTreeNode.prototype.getLeaf = function () {
                    return this.leaf;
                };
                GobiiTreeNode.prototype.setLeaf = function (value) {
                    this.leaf = value;
                    return this;
                };
                GobiiTreeNode.prototype.getExpanded = function () {
                    return this.expanded;
                };
                GobiiTreeNode.prototype.setExpanded = function (value) {
                    this.expanded = value;
                    return this;
                };
                GobiiTreeNode.prototype.getType = function () {
                    return this.type;
                };
                GobiiTreeNode.prototype.setType = function (value) {
                    this.type = value;
                    return this;
                };
                GobiiTreeNode.prototype.getPartialSelected = function () {
                    return this.partialSelected;
                };
                GobiiTreeNode.prototype.setPartialSelected = function (value) {
                    this.partialSelected = value;
                    return this;
                };
                GobiiTreeNode.prototype.getFileItemId = function () {
                    return this.fileItemId;
                };
                GobiiTreeNode.prototype.setFileItemId = function (value) {
                    this.fileItemId = value;
                    return this;
                };
                GobiiTreeNode.prototype.getRequired = function () {
                    return this.required;
                };
                GobiiTreeNode.prototype.setRequired = function (value) {
                    this.required = value;
                    return this;
                };
                GobiiTreeNode.prototype.getContainerTYpe = function () {
                    return this.containerType;
                };
                GobiiTreeNode.prototype.setContainerType = function (value) {
                    this.containerType = value;
                    return this;
                };
                return GobiiTreeNode;
            }());
            exports_1("GobiiTreeNode", GobiiTreeNode);
        }
    };
});
//# sourceMappingURL=GobiiTreeNode.js.map