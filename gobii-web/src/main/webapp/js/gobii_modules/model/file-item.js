System.register(["./type-process", "./guid", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./file-model-node"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_process_1, guid_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, file_model_node_1, FileItem;
    return {
        setters: [
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (guid_1_1) {
                guid_1 = guid_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            FileItem = (function () {
                function FileItem(_gobiiExtractFilterType, _processType, _extractorItemType, _entityType, _entitySubType, _cvFilterType, _itemId, _itemName, _checked, _required) {
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._processType = _processType;
                    this._extractorItemType = _extractorItemType;
                    this._entityType = _entityType;
                    this._entitySubType = _entitySubType;
                    this._cvFilterType = _cvFilterType;
                    this._itemId = _itemId;
                    this._itemName = _itemName;
                    this._checked = _checked;
                    this._required = _required;
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._processType = _processType;
                    this._entityType = _entityType;
                    this._entitySubType = _entitySubType;
                    this._extractorItemType = _extractorItemType;
                    this._cvFilterType = _cvFilterType;
                    this._itemId = _itemId;
                    this._itemName = _itemName;
                    this._checked = _checked;
                    this._required = _required;
                    this._fileItemUniqueId = guid_1.Guid.generateUUID();
                    if (this._cvFilterType === null) {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    if (this._extractorItemType == null) {
                        this._extractorItemType = file_model_node_1.ExtractorItemType.UNKNOWN;
                    }
                    if (this._entityType == null) {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    if (this._entitySubType == null) {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                }
                FileItem.build = function (gobiiExtractFilterType, processType) {
                    var returnVal = new FileItem(gobiiExtractFilterType, processType, file_model_node_1.ExtractorItemType.UNKNOWN, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null, null, null, null);
                    return returnVal;
                };
                //OnChange does not see the FileItemEvent as being a new event unless it's
                //a branch new instance, even if any of the property values are different.
                //I'm sure there's a better way to do this. For example, the tree component should
                //subscribe to an observer that is fed by the root component?
                FileItem.fromFileItem = function (fileItem, gobiiExtractFilterType) {
                    var existingUniqueId = fileItem._fileItemUniqueId;
                    var returnVal = FileItem
                        .build(gobiiExtractFilterType, fileItem._processType)
                        .setEntityType(fileItem._entityType)
                        .setCvFilterType(fileItem._cvFilterType)
                        .setItemId(fileItem._itemId)
                        .setItemName(fileItem._itemName)
                        .setChecked(fileItem._checked)
                        .setRequired(fileItem._required);
                    returnVal._fileItemUniqueId = existingUniqueId;
                    return returnVal;
                };
                FileItem.prototype.setFileItemUniqueId = function (fileItemUniqueId) {
                    this._fileItemUniqueId = fileItemUniqueId;
                    return this;
                };
                FileItem.prototype.getFileItemUniqueId = function () {
                    return this._fileItemUniqueId;
                };
                FileItem.prototype.getGobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                FileItem.prototype.setGobiiExtractFilterType = function (value) {
                    if (value != null) {
                        this._gobiiExtractFilterType = value;
                    }
                    else {
                        this._gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    }
                    return this;
                };
                FileItem.prototype.getProcessType = function () {
                    return this._processType;
                };
                FileItem.prototype.setProcessType = function (value) {
                    if (value != null) {
                        this._processType = value;
                    }
                    else {
                        this._processType = type_process_1.ProcessType.UNKNOWN;
                    }
                    return this;
                };
                FileItem.prototype.getExtractorItemType = function () {
                    return this._extractorItemType;
                };
                FileItem.prototype.setExtractorItemType = function (value) {
                    if (value != null) {
                        this._extractorItemType = value;
                    }
                    else {
                        this._extractorItemType = file_model_node_1.ExtractorItemType.UNKNOWN;
                    }
                    return this;
                };
                FileItem.prototype.getEntityType = function () {
                    return this._entityType;
                };
                FileItem.prototype.setEntityType = function (value) {
                    if (value != null) {
                        this._entityType = value;
                    }
                    else {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    return this;
                };
                FileItem.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                FileItem.prototype.setEntitySubType = function (value) {
                    if (value != null) {
                        this._entitySubType = value;
                    }
                    else {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                    return this;
                };
                FileItem.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                FileItem.prototype.setCvFilterType = function (value) {
                    if (value != null) {
                        this._cvFilterType = value;
                    }
                    else {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    return this;
                };
                FileItem.prototype.getItemId = function () {
                    return this._itemId;
                };
                FileItem.prototype.setItemId = function (value) {
                    this._itemId = value;
                    return this;
                };
                FileItem.prototype.getItemName = function () {
                    return this._itemName;
                };
                FileItem.prototype.setItemName = function (value) {
                    this._itemName = value;
                    return this;
                };
                FileItem.prototype.getChecked = function () {
                    return this._checked;
                };
                FileItem.prototype.setChecked = function (value) {
                    this._checked = value;
                    return this;
                };
                FileItem.prototype.getRequired = function () {
                    return this._required;
                };
                FileItem.prototype.setRequired = function (value) {
                    this._required = value;
                    return this;
                };
                return FileItem;
            }()); // FileItem()
            exports_1("FileItem", FileItem);
        }
    };
});
//# sourceMappingURL=file-item.js.map